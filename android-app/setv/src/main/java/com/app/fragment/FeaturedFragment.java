package com.app.fragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.adapter.ChannelAdapter;
import com.app.setv.R;
import com.app.item.ItemChannel;
import com.app.util.Constant;
import com.app.util.ItemOffsetDecoration;
import com.app.util.JSONParser;
import com.app.util.NetworkUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by laxmi.
 */
public class FeaturedFragment extends Fragment {

    ArrayList<ItemChannel> mListItem;
    public RecyclerView recyclerView;
    ChannelAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    GetFeaturedTask getFeaturedTask = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.row_recyclerview, container, false);
        mListItem = new ArrayList<>();
        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        if (NetworkUtils.isConnected(getActivity())) {
            getFeaturedItems();
        } else {
            Toast.makeText(getActivity(), getString(R.string.conne_msg1), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private void getFeaturedItems() {
        if (Build.VERSION.SDK_INT >= 24){
            doGetFeaturedItems();
        }
        else {
            getFeaturedTask = new GetFeaturedTask();
            getFeaturedTask.execute();
        }
    }

    private void doGetFeaturedItems(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.FEATURED_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showProgress(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showProgress(false);
                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    processJson(mainJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showProgress(false);
                lyt_not_found.setVisibility(View.VISIBLE);
            }

        });
    }

    // API post interaction for Marshmallows and previous android builds
    public class GetFeaturedTask extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        //task constructor
        GetFeaturedTask(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }


        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                HashMap<String, String> params = new HashMap<>();
//                params.put("featured", "featured");

                //make post
                JSONObject json = jsonParser.makeHttpRequest(Constant.FEATURED_URL, "GET", params);


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
            showProgress(false);

            if (jsonObject != null) {
                processJson(jsonObject);
                displayData();
            }
            else {
                lyt_not_found.setVisibility(View.VISIBLE);
            }
        }
    }

    private void processJson(JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                ItemChannel objItem = new ItemChannel();
                objItem.setId(objJson.getString(Constant.CHANNEL_ID));
                objItem.setChannelName(objJson.getString(Constant.CHANNEL_TITLE));
                objItem.setChannelCategory(objJson.getString(Constant.CATEGORY_NAME));
                objItem.setImage(objJson.getString(Constant.CHANNEL_IMAGE));
                objItem.setChannelAvgRate(objJson.getString(Constant.CHANNEL_AVG_RATE));
                mListItem.add(objItem);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void displayData() {
        adapter = new ChannelAdapter(getActivity(), mListItem, R.layout.row_channel_item);
        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }


    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
