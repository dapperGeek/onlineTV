package com.app.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.adapter.CommentAdapter;
import com.app.setv.MyApplication;
import com.app.setv.R;
import com.app.item.ItemComment;
import com.app.util.Constant;
import com.app.util.JSONParser;
import com.app.util.NetworkUtils;
import com.app.util.RecyclerTouchListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class CommentFragment extends Fragment {

    ArrayList<ItemComment> mCommentList;
    public RecyclerView recyclerView;
    CommentAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    MyApplication MyApp;
    String selectedVideoId;
    private static final String bundleVideoId = "ID";
    String strMessage;
    SentCommentTask sentCommentTask = null;
    GetCommentTask getCommentTask = null;

    public static CommentFragment newInstance(String VideoId) {
        CommentFragment f = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(bundleVideoId, VideoId);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.row_recyclerview, container, false);
        assert getArguments() != null;
        selectedVideoId = getArguments().getString(bundleVideoId);

        MyApp = MyApplication.getInstance();
        mCommentList = new ArrayList<>();

        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);


        if (NetworkUtils.isConnected(getActivity())) {
            getComment();
        } else {
            Toast.makeText(getActivity(), getString(R.string.conne_msg1), Toast.LENGTH_SHORT).show();
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position == 0) {
                    if (MyApp.getIsLogin()) {
                        showComment();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.login_msg), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return rootView;
    }


    private void showComment() {
        final Dialog mDialog = new Dialog(requireActivity(), R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.activity_comment);
        final EditText edt_comment = mDialog.findViewById(R.id.edt_comment);
        final ImageView img_sent = mDialog.findViewById(R.id.image_sent);

        img_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edt_comment.getText().toString();
                if (!comment.isEmpty()) {
                    if (NetworkUtils.isConnected(getActivity())) {
                        sentComment(comment);
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.conne_msg1), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        edt_comment.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(edt_comment.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        mDialog.show();
    }

    private void getComment() {
        if (Build.VERSION.SDK_INT >= 24){
            doGetComment();
        }
        else {
            getCommentTask = new GetCommentTask();
            getCommentTask.execute();
        }
    }

    private void doGetComment(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("get_item_comments_id", selectedVideoId);
        client.get(Constant.API_URL, params, new AsyncHttpResponseHandler() {
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
                    processCommentJson(mainJson);

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
    public class GetCommentTask extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        //task constructor
        GetCommentTask(){
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
                params.put("get_item_comments_id", selectedVideoId);

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
            showProgress(false);

            if (jsonObject != null) {
                processCommentJson(jsonObject);
                displayData();
            }
            else {
                lyt_not_found.setVisibility(View.VISIBLE);
            }
        }
    }

    private void processCommentJson(JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.ARRAY_NAME);
            if (jsonArray.length() > 0) {
                JSONObject objJson;
                for (int i = 0; i < jsonArray.length(); i++) {
                    objJson = jsonArray.getJSONObject(i);
                    if (i == 0) {
                        mCommentList.add(0, null);
                    }
                    ItemComment objItem = new ItemComment();
                    objItem.setName(objJson.getString(Constant.COMMENT_NAME));
                    objItem.setId(objJson.getString(Constant.COMMENT_ID));
                    objItem.setDesc(objJson.getString(Constant.COMMENT_DESC));
                    objItem.setDate(objJson.getString(Constant.COMMENT_DATE));
                    mCommentList.add(objItem);
                }
            } else {
                mCommentList.add(0, null);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void displayData() {
        adapter = new CommentAdapter(getActivity(), mCommentList);
        recyclerView.setAdapter(adapter);
    }

    private void sentComment(String comment) {
        if (Build.VERSION.SDK_INT >= 24){
            doSentComment(comment);
        }
        else {
            sentCommentTask = new SentCommentTask(comment);
            sentCommentTask.execute();
        }
    }

    private void doSentComment(String comment){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post_item_comment", "xxx");
        params.put("post_id", selectedVideoId);
        params.put("user_id", MyApp.getUserId());
        params.put("comment_text", comment);
        client.post(Constant.API_URL, params, new AsyncHttpResponseHandler() {
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
                displaySentComment();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showProgress(false);
                lyt_not_found.setVisibility(View.VISIBLE);
            }

        });
    }

    // API post interaction for Marshmallows and previous android builds
    public class SentCommentTask extends AsyncTask<String, String, JSONObject> {
        private String comment;

        JSONParser jsonParser = new JSONParser();

        //task constructor
        SentCommentTask(String comment){
            this.comment = comment;
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
                params.put("post_item_comment", "xxx");
                params.put("post_id", selectedVideoId);
                params.put("user_id", MyApp.getUserId());
                params.put("comment_text", comment);

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
            showProgress(false);

            if (jsonObject != null) {
                processJson(jsonObject);
                displaySentComment();
            }
            else {
                lyt_not_found.setVisibility(View.VISIBLE);
            }
        }
    }

    private void processJson(JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson = jsonArray.getJSONObject(0);
            strMessage = objJson.getString(Constant.MSG);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void displaySentComment() {
        Toast.makeText(getActivity(), strMessage, Toast.LENGTH_SHORT).show();
        mCommentList.clear();
        if (NetworkUtils.isConnected(getActivity())) {
            getComment();
        } else {
            Toast.makeText(getActivity(), getString(R.string.conne_msg1), Toast.LENGTH_SHORT).show();
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
