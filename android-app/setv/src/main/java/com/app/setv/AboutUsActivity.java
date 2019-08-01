package com.app.setv;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.item.ItemAbout;
import com.app.util.BannerAds;
import com.app.util.Constant;
import com.app.util.IsRTL;
import com.app.util.JSONParser;
import com.app.util.NetworkUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class AboutUsActivity extends AppCompatActivity {

    TextView txtAppName, txtVersion, txtCompany, txtEmail, txtWebsite, txtContact;
    ImageView imgAppLogo;
    ScrollView mScrollView;
    ProgressBar mProgressBar;
    WebView webView;
    ItemAbout itemAbout;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        IsRTL.ifSupported(AboutUsActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.menu_about));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        LinearLayout mAdViewLayout = findViewById(R.id.adView);
        BannerAds.ShowBannerAds(getApplicationContext(), mAdViewLayout);
        txtAppName = findViewById(R.id.text_app_name);
        txtVersion = findViewById(R.id.text_version);
        txtCompany = findViewById(R.id.text_company);
        txtEmail = findViewById(R.id.text_email);
        txtWebsite = findViewById(R.id.text_website);
        txtContact = findViewById(R.id.text_contact);
        imgAppLogo = findViewById(R.id.image_app_logo);
        webView = findViewById(R.id.webView);
        itemAbout = new ItemAbout();
        mScrollView = findViewById(R.id.scrollView);
        mProgressBar = findViewById(R.id.progressBar);
        webView.setBackgroundColor(Color.TRANSPARENT);
        if (NetworkUtils.isConnected(AboutUsActivity.this)) {
            getAboutUs();
        } else {
            showToast(getString(R.string.conne_msg1));
        }
    }

    private void getAboutUs() {
        if (Build.VERSION.SDK_INT >= 24){
            doGetAboutUs();
        }else {
            new GetAboutUsTask().execute();
        }
    }

    private void doGetAboutUs(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.API_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                mProgressBar.setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);
                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    processJson(mainJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);
            }

        });
    }

    // API post interaction for Marshmallows and previous android builds
    public class GetAboutUsTask extends AsyncTask<String, String, JSONObject> {

        JSONParser jsonParser = new JSONParser();

        //task constructor
        GetAboutUsTask(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
        }


        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                HashMap<String, String> params = new HashMap<>();

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
            mProgressBar.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);

            if (jsonObject != null) {
                processJson(jsonObject);
                setResult();
            }
        }
    }

    private void processJson(JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                itemAbout.setAppName(objJson.getString(Constant.APP_NAME));
                itemAbout.setAppLogo(objJson.getString(Constant.APP_IMAGE));
                itemAbout.setAppVersion(objJson.getString(Constant.APP_VERSION));
                itemAbout.setAppAuthor(objJson.getString(Constant.APP_AUTHOR));
                itemAbout.setAppEmail(objJson.getString(Constant.APP_EMAIL));
                itemAbout.setAppWebsite(objJson.getString(Constant.APP_WEBSITE));
                itemAbout.setAppContact(objJson.getString(Constant.APP_CONTACT));
                itemAbout.setAppDescription(objJson.getString(Constant.APP_DESC));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void getNumberOfVideo(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONObject pageInfo = mainJson.getJSONObject("pageInfo");
                    pageInfo.getString("totalResults");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });

    }

    private void setResult() {
        txtAppName.setText(itemAbout.getAppName());
        txtVersion.setText(itemAbout.getAppVersion());
        txtCompany.setText(itemAbout.getAppAuthor());
        txtEmail.setText(itemAbout.getAppEmail());
        txtWebsite.setText(itemAbout.getAppWebsite());
        txtContact.setText(itemAbout.getAppContact());
        Picasso.get().load(Constant.IMAGE_PATH + itemAbout.getAppLogo()).into(imgAppLogo);

        String mimeType = "text/html";
        String encoding = "utf-8";
        String htmlText = itemAbout.getAppDescription();
        boolean isRTL = Boolean.parseBoolean(getResources().getString(R.string.isRTL));
        String direction = isRTL ? "rtl" : "ltr";
        String text = "<html dir=" + direction + "><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/custom.otf\")}body{font-family: MyFont;color: #a5a5a5;text-align:justify;line-height:1.2}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
    }


    public void showToast(String msg) {
        Toast.makeText(AboutUsActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
