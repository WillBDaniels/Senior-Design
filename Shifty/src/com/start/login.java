package com.start;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haven.skilltest.demo.R;
import com.util.getInfo;
import com.util.userInfo;

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

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);
		loginButton = (Button) findViewById(R.id.loginbutton);
		username = (EditText) findViewById(R.id.userName);
		password = (EditText) findViewById(R.id.passWord);
		error = (TextView) findViewById(R.id.showerror);
		error.setVisibility(View.INVISIBLE);
		
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
					// password =
					// (Hex.encodeHexString(md.digest("PassWord".getBytes())));
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace(System.err);
				}
				tempList = new ArrayList<String>();
				loginInfo = new getInfo();
				tempList.add("check_login");
				tempList.add("username=\"" + name + "\"");
				tempList.add("password=\"" + pass + "\"");
				loginCheck = loginInfo.sendData(tempList);
//				error.setText(loginCheck);
//				error.setVisibility(View.VISIBLE);
				if (loginCheck.contains("Invalid")) {
					error.setVisibility(View.VISIBLE);
				} else {
					
					getInfo data = new getInfo();
					List<String> temp = new ArrayList<String>();
					temp.add("employee_info");
					temp.add("username=\""+name+"\"");
					ArrayList<String> lay = new ArrayList<String>();
					String returnOut = "";
					returnOut = data.sendData(temp).toString();
					lay = data.formatData(returnOut);
					
//					Toast.makeText(getApplicationContext(),returnOut,
//							Toast.LENGTH_LONG).show();
					String[] info = lay.get(0).split("~~");
					employeeId = info[0];
					employeeName = info[1];
					userinfo.setEmployeeId(employeeId);
					userinfo.setEmployeeName(employeeName);
					
					Intent intent = new Intent();
					intent.setClass(login.this, MainActivity.class);
					startActivity(intent);
					finish();
				}

			}

		});
	}
	
}
