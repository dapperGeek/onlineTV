package com.app.setv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ForgotPasswordActivity extends AppCompatActivity implements Validator.ValidationListener {
    @NotEmpty
    @Email
    EditText edtEmail;

    String strEmail, strMessage;
    private Validator validator;
    Button btnSubmit;
    ProgressDialog pDialog;

    // AsyncTask for Marshmallow and earlier builds
    private DoForgotPasswordTask doForgotPasswordTask = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        IsRTL.ifSupported(ForgotPasswordActivity.this);
        pDialog = new ProgressDialog(this);
        edtEmail = findViewById(R.id.edt_email);
        btnSubmit = findViewById(R.id.button_sign_up);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                validator.validate();
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        if (NetworkUtils.isConnected(ForgotPasswordActivity.this)) {
            putForgotPassword();
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

    public void putForgotPassword() {
        strEmail = edtEmail.getText().toString();

        if (Build.VERSION.SDK_INT >= 24){
            DoForgotPassword();
        }
        else {
            doForgotPasswordTask = new DoForgotPasswordTask(strEmail);
            doForgotPasswordTask.execute();
        }
    }

    public void DoForgotPassword(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_forgot_pass_email", strEmail);

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
                    processJson(mainJson);

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

    // API post interaction for Marshmallows and previous android builds
    public class DoForgotPasswordTask extends AsyncTask<String, String, JSONObject> {
        private String strEmail;

        JSONParser jsonParser = new JSONParser();

        //task constructor
        DoForgotPasswordTask(String strEmail){
            this.strEmail = strEmail;
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
                params.put("user_forgot_pass_email", strEmail);

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
                processJson(jsonObject);
            }
        }
    }

    public void processJson(JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                strMessage = objJson.getString(Constant.MSG);
                Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
            }

            setResult();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setResult() {

        if (Constant.GET_SUCCESS_MSG == 0) {
            showToast("Oops. \n" + strMessage);
            edtEmail.setText("");
            edtEmail.requestFocus();
        } else {
            showToast(strMessage);
            Intent intentco = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
            intentco.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentco);
            finish();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(ForgotPasswordActivity.this, msg, Toast.LENGTH_LONG).show();
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
