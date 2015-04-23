package com.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.start.login;
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
        question.setText(name);//get question and answer from database
        correctAnswer = "ok";
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
                    Toast.makeText(QuestionRest.this,"reset password successfully",Toast.LENGTH_LONG).show();
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
