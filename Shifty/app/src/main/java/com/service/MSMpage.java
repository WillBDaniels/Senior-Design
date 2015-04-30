package com.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.util.Action;
import com.util.DataPOJO;
import com.util.HolderClass;
import com.util.House;
import com.util.Shift;
import com.util.Type;
import com.util.getInfo;
import com.util.userInfo;

import java.util.ArrayList;
import java.util.List;

import edu.csci.teamshifty.R;

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
	private LinearLayout relative;
	private ArrayList<String> numbers = new ArrayList<>();
	private ArrayList<String> names = new ArrayList<>();
	private ArrayList<String> lay = new ArrayList<>();
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
       //getInfo.refreshData();
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
        for(House tmp : getInfo.currentPojo.getHouseList()){
            house = insert(house, tmp.getHouseID() + "~~" + tmp.getHouseName());
            lay.add(tmp.getHouseID() + "~~" + tmp.getHouseName());
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
                    String sTime = (from.getText().toString() + " to "
                            + toend.getText().toString());
                    DataPOJO addShift = new DataPOJO();
                    List<House> houseList = new ArrayList<>();
                    List<Shift> shiftListInner = new ArrayList<>();
                    House house = new House();
                    Shift shift = new Shift();
                    house.setHouseID(Integer.valueOf(house_id[0]));
                    shift.setHouseID(Integer.valueOf(house_id[0]));
                    shift.setEmployeeID(Integer.valueOf(employeeId));
                    shift.setName("");
                    shift.setTime(sTime);
                    shiftListInner.add(shift);
                    house.setShiftList(shiftListInner);
                    houseList.add(house);
                    addShift.setHouseList(houseList);
                    Gson gson = new Gson();
                    HolderClass hc = new HolderClass();
                    hc.setAction(Action.ADDSHIFT);
                    hc.setType(Type.SHIFTY);
                    hc.setJson(gson.toJson(addShift, DataPOJO.class));
                    new CreateShiftOperation().execute(hc);
				} else {
					selected.setText("Please confirm information!!");
				}

			}

		});

		arrayAdapterlocaltion = new ArrayAdapter<>(this,
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

		arrayAdapter = new ArrayAdapter<>(this,
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

    private class CreateShiftOperation extends AsyncTask<HolderClass, Void, String> {

        @Override
        protected String doInBackground(HolderClass... params) {
            String returnValue = getInfo.postToServer(params[0].getType(), params[0].getAction(), params[0].getJson());
            getInfo.refreshData();

            return returnValue;
        }

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            DataPOJO returnPOJO = gson.fromJson(result, DataPOJO.class);
            if (returnPOJO.getReturnMessage().isEmpty()) {
                ((ProgressBar) findViewById(R.id.progressBar3)).setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Shift created successfully!",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "success",
                        Toast.LENGTH_LONG).show();
                finish();
            }else{
                ((ProgressBar) findViewById(R.id.progressBar3)).setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Shift creation error!",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "success",
                        Toast.LENGTH_LONG).show();
                finish();
            }




        }

        @Override
        protected void onPreExecute() {
            ((ProgressBar)findViewById(R.id.progressBar3)).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

	private static String[] insert(String[] arr, String str) {
		int size = arr.length;

		String[] tmp = new String[size + 1];

		System.arraycopy(arr, 0, tmp, 0, size);

		tmp[size] = str;

		return tmp;
	}
}
