package com.app.setv;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.util.Constant;
import com.app.util.IsRTL;
import com.app.util.JSONParser;
import com.app.util.NetworkUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity implements Validator.ValidationListener {
    @NotEmpty
    EditText edtFullName;
    @Email
    EditText edtEmail;
    @Password
    EditText edtPassword;
    @Length(max = 14, min = 6, message = "Enter valid Phone Number")
    EditText edtMobile;
    Button btnSignUp;
    String strName, strEmail, strPassword, strMobi, strMessage;
    private Validator validator;
    ProgressDialog pDialog;
    MyApplication myApp;
    private GetProfileTask getProfileTask = null;
    private EditProfileTask editProfileTask = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        IsRTL.ifSupported(ProfileActivity.this);
        pDialog = new ProgressDialog(this);
        myApp = MyApplication.getInstance();
        edtFullName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtMobile = findViewById(R.id.edt_phone);
        btnSignUp = findViewById(R.id.button);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        if (NetworkUtils.isConnected(ProfileActivity.this)) {
            getUserProfile();
        } else {
            showToast(getString(R.string.conne_msg1));
        }

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        if (NetworkUtils.isConnected(ProfileActivity.this)) {
            putEditProfile();
        } else {
            showToast(getString(R.string.conne_msg1));
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getUserProfile() {
        if (Build.VERSION.SDK_INT >= 24){

            doGetUserProfile();
        }
        else {
            getProfileTask = new GetProfileTask();
            getProfileTask.execute();
        }
    }

    private void doGetUserProfile(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("get_user_profile", myApp.getUserId());

        client.get(Constant.API_URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dismissProgressDialog();
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
                dismissProgressDialog();
            }

        });
    }

    // API post interaction for Marshmallows and previous android builds
    public class GetProfileTask extends AsyncTask<String, String, JSONObject> {

        JSONParser jsonParser = new JSONParser();

        //task constructor
        GetProfileTask(){
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
                params.put("get_user_profile", myApp.getUserId());

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
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                edtFullName.setText(objJson.getString(Constant.USER_NAME));
                edtEmail.setText(objJson.getString(Constant.USER_EMAIL));
                edtMobile.setText(objJson.getString(Constant.USER_PHONE));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void putEditProfile() {
        strName = edtFullName.getText().toString();
        strEmail = edtEmail.getText().toString();
        strPassword = edtPassword.getText().toString();
        strMobi = edtMobile.getText().toString();

        if (Build.VERSION.SDK_INT >= 24){
            doEditProfile();
        }
        else {
            editProfileTask = new EditProfileTask(strName, strEmail, strPassword, strMobi);
            editProfileTask.execute();
        }
    }

    // API post interaction for Marshmallows and previous android builds
    public class EditProfileTask extends AsyncTask<String, String, JSONObject> {
        private final String strName;
        private final String strEmail;
        private final String strPassword;
        private final String strMobi;
        JSONParser jsonParser = new JSONParser();

        //task constructor
        EditProfileTask(String strName, String strEmail, String strPassword, String strMobi){
            this.strName = strName;
            this.strEmail = strEmail;
            this.strPassword = strPassword;
            this.strMobi = strMobi;
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
                params.put("post_user_profile_update", "xxx");
                params.put("user_id", myApp.getUserId());
                params.put("name", strName);
                params.put("email", strEmail);
                params.put("password", strPassword);
                params.put("phone", strMobi);

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
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        strMessage = objJson.getString(Constant.MSG);
                        Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            setResult();
        }
    }

    private void doEditProfile(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post_user_profile_update", "xxx");
        params.put("user_id", myApp.getUserId());
        params.put("name", strName);
        params.put("email", strEmail);
        params.put("password", strPassword);
        params.put("phone", strMobi);


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
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        strMessage = objJson.getString(Constant.MSG);
                        Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dismissProgressDialog();
            }

        });
    }

    public void setResult() {

        if (Constant.GET_SUCCESS_MSG == 0) {
            showToast(strMessage);
        } else {
            myApp.saveLogin(myApp.getUserId(), strName, strEmail);
            showToast("Your Profile Updated");
            onBackPressed();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_LONG).show();
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
}
