package com.thegeek0.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thegeek0.adapter.ScheduleAdapter;
import com.thegeek0.cast.Casty;
import com.thegeek0.cast.MediaData;
import com.thegeek0.db.DatabaseHelper;
import com.thegeek0.item.ItemSchedule;
import com.thegeek0.ontv.MainActivity;
import com.thegeek0.ontv.MyApplication;
import com.thegeek0.ontv.R;
import com.thegeek0.ontv.TVPlayActivity;
import com.thegeek0.ontv.YtPlayActivity;
import com.thegeek0.item.ItemChannel;
import com.thegeek0.util.Constant;
import com.thegeek0.util.JSONParser;
import com.thegeek0.util.NetworkUtils;
import com.github.ornolfr.ratingview.RatingView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ChannelDetailsFragment extends Fragment {

    ImageView imageChannel, imagePlay, imageRate;
    TextView textChannelName, textChannelCategory, textRate, textScheduleDate;
    RatingView ratingView;
    ProgressBar mProgressBar;
    LinearLayout mPageView;
    Button mViewSchedules;
    LinearLayout lyt_not_found;
    ItemChannel objBean;
    String Id, rateMsg = "", newRate = "", todaysDate;
    ProgressDialog pDialog;
    ArrayList<ItemSchedule> mListSchedule;
    RecyclerView mRecyclerView;
    ScheduleAdapter scheduleAdapter;
    private FragmentManager fragmentManager;
    boolean isFromNotification = false;
    Menu menu;
    DatabaseHelper databaseHelper;
    private Casty casty;
    MyApplication myApp;
    View v1;
    GetChannelDetailsTask getChannelDetailsTask = null;
    DoSentRateTask doSentRateTask = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_details, container, false);
        setHasOptionsMenu(true);
        v1 = rootView.findViewById(R.id.view_fake);
        myApp = MyApplication.getInstance();
        databaseHelper = new DatabaseHelper(getActivity());
        fragmentManager = getFragmentManager();
        mListSchedule = new ArrayList<>();
        pDialog = new ProgressDialog(getActivity());
        imageChannel = rootView.findViewById(R.id.image);
        imagePlay = rootView.findViewById(R.id.imagePlay);
        textChannelName = rootView.findViewById(R.id.text);
        textScheduleDate = rootView.findViewById(R.id.schedule_date);
        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        textChannelCategory = rootView.findViewById(R.id.textCategory);
        mProgressBar = rootView.findViewById(R.id.progressBar1);
        mPageView = rootView.findViewById(R.id.pageView);
        mViewSchedules = rootView.findViewById(R.id.view_schedules);
        mRecyclerView = rootView.findViewById(R.id.rv_channel_schedule);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        objBean = new ItemChannel();

        Id = Constant.ONE_CHANNEL_ID;

        if (NetworkUtils.isConnected(getActivity())) {
            getChannelDetails();
        } else {
            showToast(getString(R.string.conne_msg1));
        }

        mViewSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                String fragTag = getString(R.string.menu_schedule);
                ((MainActivity) requireActivity()).hideShowBottomView(false);
                ((MainActivity) requireActivity()).navigationItemSelected(2);

                FragmentManager fm = getFragmentManager();
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(ChannelDetailsFragment.this);
                ft.add(R.id.Container, scheduleFragment, fragTag);
                ft.addToBackStack(fragTag);
                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(fragTag);
            }
        });
        imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (casty.isConnected()) {
                    playViaCast();
                } else {
                    if (objBean.isTv()) {
                        if (myApp.getExternalPlayer()) {
                            showExternalPlay();
                        } else {
                                Intent intent = new Intent(getActivity(), TVPlayActivity.class);
                                intent.putExtra("videoUrl", objBean.getChannelUrl());
                                startActivity(intent);

                        }
                    } else {
                        String videoId = NetworkUtils.getVideoId(objBean.getChannelUrl());
                        Intent intent = new Intent(getActivity(), YtPlayActivity.class);
                        intent.putExtra("id", videoId);
                        startActivity(intent);
                    }
                }
            }
        });

        casty = Casty.create(getActivity());
        return rootView;

    }

    private void getChannelDetails() {
        if (Build.VERSION.SDK_INT >= 24){
            doChannelDetails();
        }
        else {
            getChannelDetailsTask = new GetChannelDetailsTask(Id);
            getChannelDetailsTask.execute();
        }
    }

    private void doChannelDetails(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("get_channel", "xxx");
        client.get(Constant.API_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                mProgressBar.setVisibility(View.VISIBLE);
                mPageView.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mProgressBar.setVisibility(View.GONE);
                mPageView.setVisibility(View.VISIBLE);

                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    processJson(mainJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mProgressBar.setVisibility(View.GONE);
                mPageView.setVisibility(View.GONE);
                lyt_not_found.setVisibility(View.VISIBLE);
            }
        });
    }

    // API post interaction for Marshmallows and previous android builds
    public class GetChannelDetailsTask extends AsyncTask<String, String, JSONObject> {
        private String Id;

        JSONParser jsonParser = new JSONParser();

        //task constructor
        GetChannelDetailsTask(String Id){
            this.Id = Id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }


        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("get_channel", "xxx");

                //make post
                JSONObject json = jsonParser.makeHttpRequest(Constant.API_URL, "GET", params);


                // Process server response
                if (json != null) {
                    return json;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final JSONObject jsonObject){
            dismissProgressDialog();

            if (jsonObject != null) {
                processJson(jsonObject);
            }
        }
    }

    private void processJson(JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.ARRAY_NAME);
            if (jsonArray.length() > 0) {
                JSONObject objJson;
                for (int i = 0; i < jsonArray.length(); i++) {
                    objJson = jsonArray.getJSONObject(i);
                    objBean.setId(objJson.getString(Constant.CHANNEL_ID));
                    objBean.setChannelName(objJson.getString(Constant.CHANNEL_TITLE));
//                    objBean.setChannelCategory(objJson.getString(Constant.CATEGORY_NAME));
                    objBean.setImage(objJson.getString(Constant.CHANNEL_IMAGE));
//                    objBean.setChannelAvgRate(objJson.getString(Constant.CHANNEL_AVG_RATE));
                    objBean.setDescription(objJson.getString(Constant.CHANNEL_DESC));
                    objBean.setChannelUrl(objJson.getString(Constant.CHANNEL_URL));
                    objBean.setIsTv(objJson.getString(Constant.CHANNEL_TYPE).equals("live_url"));
                    todaysDate = objJson.getString("today_date");

                }
                displayData();

            } else {
                mProgressBar.setVisibility(View.GONE);
                mPageView.setVisibility(View.GONE);
                lyt_not_found.setVisibility(View.VISIBLE);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void displayData() {
        Picasso.get().load(objBean.getImage()).placeholder(R.drawable.placeholder).into(imageChannel);
        textChannelName.setText(getString(R.string.app_name));
        textChannelCategory.setText(getString(R.string.app_caption));
        textScheduleDate.setText(todaysDate);

        ChannelScheduleFragment scheduleFragment = ChannelScheduleFragment.newInstance();
        try {
            fragmentManager.beginTransaction().replace(R.id.schedule_container, scheduleFragment).commit();
        }catch (Exception e){
            e.printStackTrace();
        }



        casty.setOnConnectChangeListener(new Casty.OnConnectChangeListener() {
            @Override
            public void onConnected() {
                playViaCast();
            }

            @Override
            public void onDisconnected() {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        casty.addMediaRouteMenuItem(menu);
        inflater.inflate(R.menu.menu_details, menu);
        this.menu = menu;
//        isFavourite();
    }

    private void doSentRate(String rate){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post_item_rate", "xxx");
        params.put("post_id", Id);
        params.put("user_id", myApp.getUserId());
        params.put("rate", rate);

        client.post(Constant.API_URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dismissProgressDialog();
                String result = new String(responseBody);
//                String rateMsg = "", newRate = "";
                try {
                    JSONObject mainJson = new JSONObject(result);
                    processRateJson(mainJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dismissProgressDialog();
            }

        });
    }

    // API post interaction for Marshmallows and previous android builds
    public class DoSentRateTask extends AsyncTask<String, String, JSONObject> {
        private String rate;

        JSONParser jsonParser = new JSONParser();

        //task constructor
        DoSentRateTask(String rate){
            this.rate = rate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }


        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("post_item_rate", "xxx");
                params.put("post_id", Id);
                params.put("user_id", myApp.getUserId());
                params.put("rate", rate);

                //make post
                JSONObject json = jsonParser.makeHttpRequest(Constant.API_URL, "POST", params);


                // Process server response
                if (json != null) {
                    return json;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final JSONObject jsonObject){
            dismissProgressDialog();

            if (jsonObject != null) {
                processRateJson(jsonObject);
            }
        }
    }

    private void processRateJson(JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                rateMsg = objJson.getString(Constant.MSG);
                if (objJson.has(Constant.CHANNEL_AVG_RATE)) {
                    newRate = objJson.getString(Constant.CHANNEL_AVG_RATE);
                }
            }
            showToast(rateMsg);
            if (!newRate.isEmpty()) {
                ratingView.setRating(Float.parseFloat(newRate));
                textRate.setText(newRate);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void dismissProgressDialog() {
        pDialog.dismiss();
    }

//    @Override
//    public void onBackPressed() {
//        if (isFromNotification) {
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//
//    }

//    private void isFavourite() {
//        if (databaseHelper.getFavouriteById(Id)) {
//            menu.findItem(R.id.menu_favourite).setIcon(R.drawable.favorite_hover);
//        } else {
//            menu.findItem(R.id.menu_favourite).setIcon(R.drawable.favorite_normal);
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        v1.requestFocus();
    }

    private void playViaCast() {
        if (objBean.isTv()) {
            casty.getPlayer().loadMediaAndPlay(createSampleMediaData(objBean.getChannelUrl(), objBean.getChannelName(), objBean.getImage()));
        } else {
            showToast(getResources().getString(R.string.cast_youtube));
        }
    }

    private MediaData createSampleMediaData(String videoUrl, String videoTitle, String videoImage) {
        return new MediaData.Builder(videoUrl)
                .setStreamType(MediaData.STREAM_TYPE_BUFFERED)
                .setContentType("videos/mp4")
                .setMediaType(MediaData.MEDIA_TYPE_MOVIE)
                .setTitle(videoTitle)
                .setSubtitle(getString(R.string.app_name))
                .addPhotoUrl(videoImage)
                .build();
    }

    private void showExternalPlay() {
        final Dialog mDialog = new Dialog(requireActivity(), R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.player_dialog);
        LinearLayout lytMxPlayer = mDialog.findViewById(R.id.lytMxPlayer);
        LinearLayout lytVLCPlayer = mDialog.findViewById(R.id.lytVLCPlayer);
        LinearLayout lytXMTVPlayer = mDialog.findViewById(R.id.lytXMTVPlayer);

        lytMxPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (isExternalPlayerAvailable(requireActivity(), "com.mxtech.videoplayer.ad")) {
                    playMxPlayer();
                } else {
                    appNotInstalledDownload(getString(R.string.mx_player), "com.mxtech.videoplayer.ad", false);
                }
            }
        });

        lytVLCPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (isExternalPlayerAvailable(requireActivity(), "org.videolan.vlc")) {
                    playVlcPlayer();
                } else {
                    appNotInstalledDownload(getString(R.string.vlc_player), "org.videolan.vlc", false);
                }
            }
        });

        lytXMTVPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (isExternalPlayerAvailable(requireActivity(), "com.xmtex.videoplayer.ads")) {
                    playXmtvPlayer();
                } else {
                    appNotInstalledDownload(getString(R.string.xmtv_player), "com.xmtex.videoplayer.ads", true);
                }
            }
        });


        mDialog.show();
    }

    private void playMxPlayer() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri videoUri = Uri.parse(objBean.getChannelUrl());
        intent.setDataAndType(videoUri, "application/x-mpegURL");
        intent.setPackage("com.mxtech.videoplayer.ad");
        startActivity(intent);
    }

    private void playVlcPlayer() {
        Uri uri = Uri.parse(objBean.getChannelUrl());
        Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
        vlcIntent.setPackage("org.videolan.vlc");
        vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
        vlcIntent.setComponent(new ComponentName("org.videolan.vlc", "org.videolan.vlc.gui.video.VideoPlayerActivity"));
        startActivity(vlcIntent);
    }

    private void playXmtvPlayer() {
        Bundle bnd = new Bundle();
        bnd.putString("path", objBean.getChannelUrl());
        Intent intent = new Intent();
        intent.setClassName("com.xmtex.videoplayer.ads", "org.zeipel.videoplayer.XMTVPlayer");
        intent.putExtras(bnd);
        startActivity(intent);
    }

    private boolean isExternalPlayerAvailable(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void appNotInstalledDownload(final String appName, final String packageName, final boolean isDownloadExternal) {
        String text = getString(R.string.download_msg, appName);
        new AlertDialog.Builder(requireActivity())
                .setTitle(getString(R.string.important))
                .setMessage(text)
                .setPositiveButton(getString(R.string.download), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (isDownloadExternal) {
                            startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(getString(R.string.xmtv_download_link))));
                        } else {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id="
                                                + packageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id="
                                                + packageName)));
                            }
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }
}
