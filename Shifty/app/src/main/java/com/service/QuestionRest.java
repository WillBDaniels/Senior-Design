package com.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.start.login;
import com.util.Action;
import com.util.DataPOJO;
import com.util.Employee;
import com.util.Type;
import com.util.getInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.csci.teamshifty.R;

/**
 * Created by Administrator on 4/15/2015.
 */
public class QuestionRest extends Activity {

    private Button reset = null;
    private String name = null;
    private TextView showerror = null;
    private TextView question = null;
    private EditText answer = null;
    private EditText newpass = null;
    private String pass = null;
    private String correctAnswer = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.que_reset);

        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("username");

        reset = (Button) findViewById(R.id.reset);
        showerror = (TextView) findViewById(R.id.showerror);
        showerror.setVisibility(View.INVISIBLE);
        question = (TextView) findViewById(R.id.question);
        answer = (EditText) findViewById(R.id.answer);
        newpass = (EditText) findViewById(R.id.newpass);
        DataPOJO dataFirst;
        Gson gson = new Gson();
        dataFirst = gson.fromJson(getInfo.postToServer(Type.SHIFTY, Action.GETALLDATA, ""), DataPOJO.class);
        int employeeID = 0;
        for (Employee emp : dataFirst.getAllEmployees()){
            if (emp.getUsername().equals(name)){
                employeeID = emp.getEmployeeID();
            }
        }
        getInfo.employeeID = String.valueOf(employeeID);
        DataPOJO data = new DataPOJO();
        data.setEmployeeID(employeeID);
        data = gson.fromJson(getInfo.postToServer(Type.SHIFTY, Action.GETQA, gson.toJson(data, DataPOJO.class)), DataPOJO.class);
        question.setText(data.getSecretQuestion());//get question and answer from database
        correctAnswer = data.getSecretAnswer();
        reset.setOnClickListener(new submit());

    }
    private class submit implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            if(correctAnswer.equals(answer.getText().toString())){
                //reset password in database
                pass = newpass.getText().toString();
                pass = pass.trim();
                if(pass.equals("")){
                    showerror.setText("Invalid password ");
                    showerror.setVisibility(View.VISIBLE);
                }else{
                    DataPOJO data = new DataPOJO();
                    try {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        byte[] bytes = md.digest(newpass.getText().toString()
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
                    data.setPasswordHash(pass);
                    data.setEmployeeID(Integer.valueOf(getInfo.employeeID));
                    Gson gson = new Gson();
                    data = gson.fromJson(getInfo.postToServer(Type.SHIFTY, Action.RESETPASSWORD, gson.toJson(data, DataPOJO.class)), DataPOJO.class);
                    if (data.getReturnMessage().toLowerCase().contains("success")) {
                        Toast.makeText(QuestionRest.this, "reset password successfully", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(QuestionRest.this, "reset password unsuccessful, please contact admin for assistance", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent();
                    intent.setClass(QuestionRest.this, login.class);
                    Bundle bundle = new Bundle();
                    startActivity(intent);
                    finish();
                }
            }else{
                showerror.setText("Answer is not correct ");
                showerror.setVisibility(View.VISIBLE);
            }
        }
    }

}
