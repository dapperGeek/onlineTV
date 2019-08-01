package com.thegeek0.ontv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thegeek0.util.Constant;
import com.thegeek0.util.IsRTL;
import com.thegeek0.util.JSONParser;
import com.thegeek0.util.NetworkUtils;
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

public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {
    @NotEmpty
    EditText edtFullName;
    @Email
    EditText edtEmail;
    @Password
    EditText edtPassword;
    @Length(max = 14, min = 6, message = "Enter valid Phone Number")
    EditText edtMobile;
    Button btnSignUp;
    String strUserId, strName, strEmail, strPassword, strMobi, strMessage;
    private Validator validator;
    TextView txtLogin;
    ProgressDialog pDialog;
    MyApplication MyApp;

    DoSignUpTask doSignUpTask = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //
        MyApp = MyApplication.getInstance();

        IsRTL.ifSupported(SignUpActivity.this);
        pDialog = new ProgressDialog(this);
        edtFullName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtMobile = findViewById(R.id.edt_phone);

        btnSignUp = findViewById(R.id.button);
        txtLogin = findViewById(R.id.txt_login);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        if (NetworkUtils.isConnected(SignUpActivity.this)) {
            putSignUp();
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

    public void putSignUp() {
        strName = edtFullName.getText().toString();
        strEmail = edtEmail.getText().toString();
        strPassword = edtPassword.getText().toString();
        strMobi = edtMobile.getText().toString();

        if (Build.VERSION.SDK_INT >= 24){
            DoSignUp();
        }
        else {
            doSignUpTask = new DoSignUpTask(strName, strEmail, strPassword, strMobi);
            doSignUpTask.execute();
        }
    }


    public void DoSignUp(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("post_user_register", "xxx");
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
    public class DoSignUpTask extends AsyncTask<String, String, JSONObject> {
        private String strName;
        private String strEmail;
        private String strPassword;
        private String strMobi;

        JSONParser jsonParser = new JSONParser();

        //task constructor
        DoSignUpTask(String strName, String strEmail, String strPassword, String strMobi){
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
                params.put("post_user_register", "xxx");
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
                processJson(jsonObject);
            }
        }
    }

    private void processJson(JSONObject jsonObject){
        try {
            //Get the authentication status or error message
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                strMessage = objJson.getString(Constant.MSG);
                Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                strUserId = objJson.getString(Constant.USER_ID);
            }

            setResult();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setResult() {

        if (Constant.GET_SUCCESS_MSG == 0) {
            edtEmail.setText("");
            edtEmail.requestFocus();
            showToast(strMessage);
        } else {
            MyApp.saveIsLogin(true);
            MyApp.saveLogin(strUserId, strName, strEmail);

            showToast(strMessage);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(SignUpActivity.this);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_LONG).show();
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
