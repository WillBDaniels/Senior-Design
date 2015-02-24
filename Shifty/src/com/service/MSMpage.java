package com.service;

import java.util.ArrayList;
import java.util.List;

import com.haven.skilltest.demo.R;
import com.start.MainActivity;
import com.util.House;
import com.util.getInfo;
import com.util.userInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MSMpage extends Activity {

	private Button addContacts;
	private Button back;
	private Button sendMsm;
	private String[] day = { "Monday", "Tuesday", "Wednesday", "Thurday",
			"Friday", "Saturday", "Sunday" };
	private String[] house = { "" };
	private Spinner daySpin;
	private Spinner location;
	private ArrayAdapter<String> arrayAdapter;
	private ArrayAdapter<String> arrayAdapterlocaltion;
	private EditText from;
	private EditText toend;
	private EditText extraNumber;
	private EditText textContent;
	private TextView selected;
	private userInfo userinfo;
	private String employeeId;
	private String selectedDay;
	private String selectedLocation;
	private String massage;
	private int houseNumber;
	private LinearLayout relative;
	private ArrayList<String> numbers = new ArrayList<String>();
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<String> lay = new ArrayList<String>();
	private int locationIdposition;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			numbers = data.getExtras().getStringArrayList("numbers");
			names = data.getExtras().getStringArrayList("names");
//			Toast.makeText(MSMpage.this,
//					 numbers.toString(), Toast.LENGTH_LONG)
//					 .show();
			if(names.size()==0){
				selected.setText("");
			}else{
				selected.setText("selected: " + names.toString());
			}
			
			break;
		default:
			break;
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msmpage);
		sendMsm = (Button) findViewById(R.id.sendMsm);
		from = (EditText) findViewById(R.id.from);
		toend = (EditText) findViewById(R.id.toend);
		selected = (TextView) findViewById(R.id.selected);
		extraNumber = (EditText) findViewById(R.id.extraNumber);
		textContent = (EditText) findViewById(R.id.textContent);
		location = (Spinner) findViewById(R.id.location);
		daySpin = (Spinner) findViewById(R.id.day);
		addContacts = (Button) findViewById(R.id.addContacts);
		back = (Button) findViewById(R.id.cancle);
		userinfo = (userInfo) getApplication();
		employeeId = userinfo.getEmployeeId();
		houseNumber = userinfo.getHouseNumber();
		relative = (LinearLayout)findViewById(R.id.background);
		
		relative.setOnTouchListener(new LinearLayout.OnTouchListener() {
            
            public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    relative.setFocusable(true);
                    relative.setFocusableInTouchMode(true);
                    relative.requestFocus();
                    
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    boolean isOpen = imm.isActive();
                    if (isOpen) {
                        // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
                        imm.hideSoftInputFromWindow(extraNumber.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    return false;
            }
    });
		
		
		
		
		List<String> temp = new ArrayList<String>();
		getInfo data = new getInfo();
		temp.add("get_all_houses");
		String returnOut = data.sendData(temp).toString();
		lay = getInfo.formatData(returnOut);
		for (String tmp : lay) {
			String[] arr1 = tmp.split("~~");
			house = insert(house, "House" + arr1[0]);
		}

		
		
		back.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}

		});

		addContacts.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MSMpage.this, CreateShift.class);
				Bundle bl = new Bundle();
				bl.putString("employeeId", employeeId);
				intent.putExtras(bl);
				startActivityForResult(intent, 0);
			}

		});

		sendMsm.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				massage = "There is a new shift: on " + selectedDay + " from "
						+ from.getText().toString() + " to "
						+ toend.getText().toString() + " in "
						+ selectedLocation + ". "
						+ textContent.getText().toString() + "---"
						+ userinfo.getEmployeeName();
				if (extraNumber.getText().toString().length() > 1) {
					numbers.add(extraNumber.getText().toString());
				}
				
				if ((numbers.size() > 0)
						& (!from.getText().toString().trim().equals(""))
						& (!toend.getText().toString().trim().equals(""))
						& (!selectedLocation.equals(""))) {
					massage = massage.trim();
					SmsManager smsManager = SmsManager.getDefault();
					for (String temp : numbers) {
						smsManager.sendTextMessage(temp.trim(), null, massage,
								null, null);
					}
					String[] house_id = lay.get(locationIdposition-1).split("~~");
					List<String> temp1 = new ArrayList<String>();
					getInfo data1 = new getInfo();
					temp1.add("new_shift");
					temp1.add("\"shift_time\"=" + "\""
							+ from.getText().toString() + " to "
							+ toend.getText().toString() + "\"");
					temp1.add("\"employee_id\"=" + "\"" + employeeId + "\"");
					temp1.add("\"house_id\"=" + "\"" + house_id[0] + "\"");
					String result = getInfo.sendData(temp1).toString();

//					Toast.makeText(
//							getApplicationContext(),
//							"\"shift_time\"=" + "\""
//									+ from.getText().toString() + " to "
//									+ toend.getText().toString() + "\"  "
//									+ "\"employee_id\"=" + "\"" + employeeId
//									+ "\"  " + "\"house_id\"=" + "\""
//									+ house_id[0] + "\"", Toast.LENGTH_LONG)
//							.show();

						Toast.makeText(getApplicationContext(),"Success",
								Toast.LENGTH_LONG).show();
						finish();

				} else {
					selected.setText("Please confirm information!!");
				}

			}

		});

		arrayAdapterlocaltion = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, house);
		location.setAdapter(arrayAdapterlocaltion);
		location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectedLocation = (String) location.getItemAtPosition(arg2);
				locationIdposition = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, day);
		daySpin.setAdapter(arrayAdapter);
		daySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectedDay = (String) daySpin.getItemAtPosition(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private static String[] insert(String[] arr, String str) {
		int size = arr.length;

		String[] tmp = new String[size + 1];

		System.arraycopy(arr, 0, tmp, 0, size);

		tmp[size] = str;

		return tmp;
	}
}
