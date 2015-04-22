package com.service;

import java.util.ArrayList;
import java.util.List;

import com.haven.skilltest.demo.R;
import com.util.Employee;
import com.util.getInfo;
import com.util.userInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SendMSM extends Activity {

	private MyCustomAdapter dataAdapter = null;
	int selectedNumber = 0;
	private String userId;
	private userInfo userinfo;
	private Button okButton = null;
	private Button cancelButton = null;
	private String massage;
	private getInfo data;
	private List<String> tempList;
	private String username;
	private String returnOut;
	private ArrayList<String> lay = new ArrayList<String>();
	private ArrayList<String> numbers = new ArrayList<String>();
	private ArrayList<String> names = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmsm);
		cancelButton = (Button) findViewById(R.id.addcancel);
		Bundle bundle = this.getIntent().getExtras();
		massage = bundle.getString("massage");
		

		cancelButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});

		displayListView();

		checkButtonClick();

	}

	private void displayListView() {
		List<Employee> EmployeeList;
		EmployeeList = getInfo.currentPojo.getAllEmployees();
//		

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.employee_info,
				EmployeeList);
		ListView listView = (ListView) findViewById(R.id.listView1);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
			}
		});
	}

	private class MyCustomAdapter extends ArrayAdapter<Employee> {

		private ArrayList<Employee> EmployeeList;

		public MyCustomAdapter(Context context, int textViewResourceId,
				List<Employee> EmployeeList) {
			super(context, textViewResourceId, EmployeeList);
			this.EmployeeList = new ArrayList<>();
			this.EmployeeList.addAll(EmployeeList);
		}

		private class ViewHolder {
			TextView phone;
			CheckBox name;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.employee_info, null);

				holder = new ViewHolder();
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				holder.name = (CheckBox) convertView
						.findViewById(R.id.checkBox1);
				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Employee Employee = (Employee) cb.getTag();
//						Toast.makeText(
//								getApplicationContext(),
//								"Clicked on Checkbox: " + cb.getText() + " is "
//										+ cb.isChecked(), Toast.LENGTH_LONG)
//								.show();
						Employee.setSelected(cb.isChecked());
						if (cb.isChecked()) {
							selectedNumber++;
						} else {
							if (selectedNumber > 0) {
								selectedNumber--;
							}
						}
						String showNumber = "send(" + selectedNumber + ")";
						okButton.setText(showNumber);
					}

				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Employee Employee = EmployeeList.get(position);
			holder.phone.setText(" (" + Employee.getPhoneNumber() + ")");
			holder.name.setText(Employee.getName());
			holder.name.setChecked(Employee.isSelected());
			holder.name.setTag(Employee);

			return convertView;

		}

	}

	@SuppressLint("NewApi")
	private void checkButtonClick() {

		okButton = (Button) findViewById(R.id.findSelected);
		String showNumber = "send(" + selectedNumber + ")";
		okButton.setText(showNumber);
		okButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {

//				StringBuffer responseText = new StringBuffer();
//				responseText.append("The following were selected...\n");

				ArrayList<Employee> EmployeeList = dataAdapter.EmployeeList;
				for (int i = 0; i < EmployeeList.size(); i++) {
					Employee Employee = EmployeeList.get(i);
					if (Employee.isSelected()) {
//						responseText.append("\n" + Employee.getName());
						numbers.add(String.valueOf(Employee.getPhoneNumber()));
						names.add(Employee.getName());
					}
				}
//				
//				Toast.makeText(getApplicationContext(), numbers.toString(),
//					Toast.LENGTH_LONG).show();
				massage = "There is a new shift. "+massage+". from "+username;
				if ((numbers.size() > 0)){
					massage = massage.trim();
					SmsManager smsManager = SmsManager.getDefault();
					for (String temp : numbers) {
						smsManager.sendTextMessage(temp.trim(), null, massage,
								null, null);
					}
					Toast.makeText(getApplicationContext(), "success",
							Toast.LENGTH_LONG).show();
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "add contacts",
							Toast.LENGTH_LONG).show();
				}
				
//				
//				Toast.makeText(getApplicationContext(), responseText,
//						Toast.LENGTH_LONG).show();

			}
		});

	}
}
