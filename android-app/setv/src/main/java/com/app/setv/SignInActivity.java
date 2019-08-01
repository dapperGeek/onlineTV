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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.app.util.Constant;
import com.app.util.IsRTL;
import com.app.util.JSONParser;
import com.app.util.NetworkUtils;
import com.loopj.android.http.*;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignInActivity extends AppCompatActivity implements Validator.ValidationListener {
    @NotEmpty
    @Email
    EditText edtEmail;
    @NotEmpty
    @Password
    EditText edtPassword;
    String strEmail, strPassword, strMessage, strName, strUserId;
    Button btnLogin, btnForgotPass, btnRegister;
    private Validator validator;
    MyApplication MyApp;
    ProgressDialog pDialog;
    CheckBox checkBox;

    private DoSignInTask doSignInTask = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        IsRTL.ifSupported(SignInActivity.this);
        pDialog = new ProgressDialog(this);
        MyApp = MyApplication.getInstance();
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.button_login);
        btnForgotPass = findViewById(R.id.button_forgot);
        btnRegister = findViewById(R.id.button_sign_up);
        checkBox = findViewById(R.id.checkBox);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });


        if (MyApp.getIsRemember()) {
            checkBox.setChecked(true);
            edtEmail.setText(MyApp.getRememberEmail());
            edtPassword.setText(MyApp.getRememberPassword());
        }
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        if (NetworkUtils.isConnected(SignInActivity.this)) {
            putSignIn();
        } else {
            showToast(getString(R.string.conne_msg1));
        }
    }

    public void putSignIn() {
        strEmail = edtEmail.getText().toString();
        strPassword = edtPassword.getText().toString();

        if (checkBox.isChecked()) {
            MyApp.saveIsRemember(true);
            MyApp.saveRemember(strEmail, strPassword);
        } else {
            MyApp.saveIsRemember(false);
        }

        // Parameters posting to API back-end depending on Android build
        if (Build.VERSION.SDK_INT >= 24){

            DoSignIn();
        }
        else {

            doSignInTask = new DoSignInTask(strEmail, strPassword);
            doSignInTask.execute();
        }
    }

    // Recent android builds SignInTask
    public void DoSignIn(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post_user_login", "xxx");
        params.put("email", strEmail);
        params.put("password", strPassword);

        client.post(this, Constant.API_URL, params, new AsyncHttpResponseHandler() {

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
                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);

                    processJson(mainJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }

        });
    }

    // API post interaction for Marshmallows and previous android builds
    public class DoSignInTask extends AsyncTask<String, String, JSONObject>{
        private String email;
        private String password;

        JSONParser jsonParser = new JSONParser();

        //task constructor
        DoSignInTask(String email, String password){
            this.email = email;
            this.password = password;
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
                params.put("post_user_login", "xxx");
                params.put("email", email);
                params.put("password", password);

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

//                processJson(jsonObject);
                try {
                    //Get the authentication status or error message
                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        if (objJson.has(Constant.MSG)) {
                            strMessage = objJson.getString(Constant.MSG);
                            Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                        } else {
                            Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                            strName = objJson.getString(Constant.USER_NAME);
                            strUserId = objJson.getString(Constant.USER_ID);
                        }
                    }

                    setResult();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void processJson(JSONObject jsonObject){
        try {
            //Get the authentication status or error message
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                if (objJson.has(Constant.MSG)) {
                    strMessage = objJson.getString(Constant.MSG);
                    Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                } else {
                    Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                    strName = objJson.getString(Constant.USER_NAME);
                    strUserId = objJson.getString(Constant.USER_ID);
                }
            }

            setResult();
        }catch (Exception e){
            e.printStackTrace();
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

    public void setResult() {

        if (Constant.GET_SUCCESS_MSG == 0) {
            showToast("Oops. \n" + strMessage);
//            showToast("Oops. \n" + "failed to Login");

        } else {
            MyApp.saveIsLogin(true);
            MyApp.saveLogin(strUserId, strName, strEmail);

            Intent i = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
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
