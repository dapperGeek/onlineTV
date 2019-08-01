package com.thegeek0.fragment;


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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thegeek0.adapter.ScheduleAdapter;
import com.thegeek0.item.ItemSchedule;
import com.thegeek0.ontv.MyApplication;
import com.thegeek0.ontv.R;
import com.thegeek0.util.Constant;
import com.thegeek0.util.JSONParser;
import com.thegeek0.util.NetworkUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    ArrayList<ItemSchedule> mScheduleList;
    public RecyclerView recyclerView;
    ScheduleAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    private String url = "get_all_schedules";
    MyApplication MyApp;
    GetScheduleTask getScheduleTask = null;
    private TextView scheduleHeader, scheduleDate;

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.schedule_recyclerview, container, false);
        scheduleDate = rootView.findViewById(R.id.schedule_date);
        scheduleHeader = rootView.findViewById(R.id.schedule_header);

        scheduleDate.setVisibility(View.VISIBLE);
        scheduleHeader.setVisibility(View.VISIBLE);

        MyApp = MyApplication.getInstance();
        mScheduleList = new ArrayList<>();

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

        adapter = new ScheduleAdapter(getActivity(), mScheduleList);
        recyclerView.setAdapter(adapter);

        if (NetworkUtils.isConnected(getActivity())) {
            getSchedule();
        } else {
            Toast.makeText(getActivity(), getString(R.string.conne_msg1), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    private void getSchedule() {
        if (Build.VERSION.SDK_INT >= 24){
            doGetSchedule();
        }
        else {
            getScheduleTask = new GetScheduleTask();
            getScheduleTask.execute();
        }
    }

    private void doGetSchedule(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(url, "xxx");
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
                    processJson(mainJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showProgress(false);
                lyt_not_found.setVisibility(View.VISIBLE);
            }

        });
    }

    // API post interaction for Marshmallows and previous android builds
    public class GetScheduleTask extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        //task constructor
        GetScheduleTask(){
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
                params.put(url, "xxx");

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
                processJson(jsonObject);
            }
            else {
                lyt_not_found.setVisibility(View.VISIBLE);
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

                    ItemSchedule itemSchedule = new ItemSchedule();
                    itemSchedule.setScheduleId(objJson.getString(Constant.SCHEDULE_ID));
                    itemSchedule.setScheduleTitle(objJson.getString(Constant.SCHEDULE_TITLE));
                    itemSchedule.setScheduleTime(objJson.getString(Constant.SCHEDULE_TIME));

                    mScheduleList.add(itemSchedule);
                }

            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        displayData();
    }

    private void displayData() {
        adapter = new ScheduleAdapter(getActivity(), mScheduleList);
        recyclerView.setAdapter(adapter);
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
