package com.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haven.skilltest.demo.R;



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
                Intent intent = new Intent();
                intent.setClass(RecoverPass.this, QuestionRest.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", name);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }
    }
}
