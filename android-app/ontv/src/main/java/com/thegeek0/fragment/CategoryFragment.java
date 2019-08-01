package com.thegeek0.fragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thegeek0.adapter.CategoryAdapter;
import com.thegeek0.ontv.MainActivity;
import com.thegeek0.ontv.R;
import com.thegeek0.item.ItemCategory;
import com.thegeek0.util.Constant;
import com.thegeek0.util.ItemOffsetDecoration;
import com.thegeek0.util.JSONParser;
import com.thegeek0.util.NetworkUtils;
import com.thegeek0.util.RecyclerTouchListener;
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

public class CategoryFragment extends Fragment {

    ArrayList<ItemCategory> mListItem;
    public RecyclerView recyclerView;
    CategoryAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    private GetCategoryTask getCategoryTask = null;

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
            getCategory();
        } else {
            Toast.makeText(getActivity(), getString(R.string.conne_msg1), Toast.LENGTH_SHORT).show();
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String categoryName = mListItem.get(position).getCategoryName();
                Bundle bundle = new Bundle();
                bundle.putString("name", categoryName);
                bundle.putString("Id", mListItem.get(position).getCategoryId());

                FragmentManager fm = getFragmentManager();
                CategoryItemFragment categoryItemFragment = new CategoryItemFragment();
                categoryItemFragment.setArguments(bundle);
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(CategoryFragment.this);
                ft.add(R.id.Container, categoryItemFragment, categoryName);
                ft.addToBackStack(categoryName);
                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return rootView;
    }

    private void getCategory() {
        if (Build.VERSION.SDK_INT >= 24){
            doGetCategory();
        }
        else {
            getCategoryTask = new GetCategoryTask();
            getCategoryTask.execute();
        }
    }

    private void doGetCategory(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.CATEGORY_URL, new AsyncHttpResponseHandler() {

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
    public class GetCategoryTask extends AsyncTask<String, String, JSONObject> {

        JSONParser jsonParser = new JSONParser();

        //task constructor
        GetCategoryTask(){
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

                //make post
                JSONObject json = jsonParser.makeHttpRequest(Constant.CATEGORY_URL, "GET", params);


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
                ItemCategory objItem = new ItemCategory();
                objItem.setCategoryId(objJson.getString(Constant.CATEGORY_CID));
                objItem.setCategoryName(objJson.getString(Constant.CATEGORY_NAME));
                objItem.setCategoryImage(objJson.getString(Constant.CATEGORY_IMAGE));
                mListItem.add(objItem);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void displayData() {
        adapter = new CategoryAdapter(getActivity(), mListItem, R.layout.row_category_item);
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
