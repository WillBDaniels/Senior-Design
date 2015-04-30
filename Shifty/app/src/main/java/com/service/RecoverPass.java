package com.service;

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
import com.util.Action;
import com.util.DataPOJO;
import com.util.Employee;
import com.util.HolderClass;
import com.util.Type;
import com.util.getInfo;

import edu.csci.teamshifty.R;



public class RecoverPass extends Activity {

    private EditText username = null;
    private Button gonext = null;
    private String name = null;
    private TextView error = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_pass);

        username = (EditText) findViewById(R.id.username);
        gonext = (Button) findViewById(R.id.gonext);
        error = (TextView) findViewById(R.id.error);
        gonext.setOnClickListener(new next_step());

    }
    private class next_step implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            name = username.getText().toString();
            name = name.trim();
            if(name.equals("")){
                error.setVisibility(View.VISIBLE);
            }else{
                HolderClass hc = new HolderClass();
                hc.setAction(Action.GETALLDATA);
                hc.setType(Type.SHIFTY);
                hc.setJson("");
                new GetEmployeeQuestions().execute(hc);
            }
        }
    }

    private class GetEmployeeQuestions extends AsyncTask<HolderClass, Void, String> {

        @Override
        protected String doInBackground(HolderClass... params) {
            String returnValue = getInfo.postToServer(params[0].getType(), params[0].getAction(), params[0].getJson());
            DataPOJO dataFirst;
            Gson gson = new Gson();
            int employeeID = 0;
            dataFirst = gson.fromJson(returnValue, DataPOJO.class);
            for (Employee emp : dataFirst.getAllEmployees()){
                if (emp.getUsername().equals(name)){
                    employeeID = emp.getEmployeeID();
                }
            }
            getInfo.employeeID = String.valueOf(employeeID);
            DataPOJO data = new DataPOJO();
            data.setEmployeeID(Integer.valueOf(getInfo.employeeID));
            data = gson.fromJson(getInfo.postToServer(Type.SHIFTY, Action.GETQA, gson.toJson(data, DataPOJO.class)), DataPOJO.class);
            getInfo.secretQuestion = data.getSecretQuestion();
            getInfo.secretAnswer = data.getSecretAnswer();


            return returnValue;
        }

        @Override
        protected void onPostExecute(String result) {

            Intent intent = new Intent();
            intent.setClass(RecoverPass.this, QuestionRest.class);
            startActivity(intent);
            finish();

        }

        @Override
        protected void onPreExecute() {
            ((ProgressBar)findViewById(R.id.progressBar4)).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
