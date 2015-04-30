package com.start;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.service.RecoverPass;
import com.util.Action;
import com.util.DataPOJO;
import com.util.HolderClass;
import com.util.Type;
import com.util.getInfo;
import com.util.userInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import edu.csci.teamshifty.R;

public class login extends Activity {

    private Button loginButton;
    private EditText username;
    private EditText password;
    private TextView error;
    private String name;
    private String pass;
    private getInfo loginInfo;
    private List<String> tempList;
    private String loginCheck;
    private String employeeName;
    private String employeeId;
    private userInfo userinfo;
    private Button recover;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        loginButton = (Button) findViewById(R.id.loginbutton);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.passWord);
        error = (TextView) findViewById(R.id.showerror);
        error.setVisibility(View.INVISIBLE);
        recover = (Button) findViewById(R.id.recover);

        recover.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(login.this, RecoverPass.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                name = username.getText().toString();
                userinfo = (userInfo) getApplication();
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] bytes = md.digest(password.getText().toString()
                            .getBytes());
                    StringBuilder ret = new StringBuilder(bytes.length << 1);
                    for (int i = 0; i < bytes.length; i++) {
                        ret.append(Character
                                .forDigit((bytes[i] >> 4) & 0xf, 16));
                        ret.append(Character.forDigit(bytes[i] & 0xf, 16));
                    }
                    pass = ret.toString();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace(System.err);
                }
                loginInfo = new getInfo();
                DataPOJO loginData = new DataPOJO();
                loginData.setUsername(name);
                loginData.setPasswordHash(pass);
                Gson gson = new Gson();
                HolderClass hc = new HolderClass();
                hc.setAction(Action.GETMOBILEINFO);
                hc.setType(Type.SHIFTY);
                hc.setJson(gson.toJson(loginData, DataPOJO.class));
                new loginOperation().execute(hc);
            }

        });
    }
    private class loginOperation extends AsyncTask<HolderClass, Void, String> {

        @Override
        protected String doInBackground(HolderClass... params) {
            String returnValue = getInfo.postToServer(params[0].getType(), params[0].getAction(), params[0].getJson());
            return returnValue;
        }

        @Override
            protected void onPostExecute(String result) {
            Gson gson = new Gson();
            DataPOJO resultData = gson.fromJson(result, DataPOJO.class);
            if (resultData.getReturnMessage().toLowerCase().contains("failure")) {
                error.setVisibility(View.VISIBLE);
            } else {
                employeeId = String.valueOf(resultData.getEmployeeID());
                getInfo.employeeID = employeeId;
                employeeName = resultData.getWholeName();
                getInfo.employeeName = employeeName;
                userinfo.setEmployeeId(employeeId);
                userinfo.setEmployeeName(employeeName);
                getInfo.currentPojo = resultData;
                Intent intent = new Intent();
                intent.setClass(login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }

        @Override
        protected void onPreExecute() {
            ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
