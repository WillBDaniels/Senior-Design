package com.service;

import java.util.ArrayList;
import java.util.List;

import com.haven.skilltest.demo.R;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateList extends Activity {

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
	private String name;
	private String namecode;
	private EditText listname;
	private ArrayList<String> lay = new ArrayList<String>();
	private ArrayList<String> numbers = new ArrayList<String>();
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<String> id = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createlist);
		numbers.clear();
		names.clear();
		name = "";
		cancelButton = (Button) findViewById(R.id.addcancel);
		listname = (EditText) findViewById(R.id.listname);

		cancelButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
		// Button goto2 = (Button)findViewById(R.id.goTo2);
		// goto2.setOnClickListener(new Button.OnClickListener(){

		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// jumpToLayout2();
		// }
		//
		// });

		displayListView();

		checkButtonClick();

	}

	private void displayListView() {
		ArrayList<Employee> EmployeeList = new ArrayList<Employee>();
		tempList = new ArrayList<String>();
		data = new getInfo();
		userinfo = (userInfo) getApplication();
		userId = userinfo.getEmployeeId();
		username = userinfo.getEmployeeName();
		tempList.add("employee_info");
		tempList.add("1=1 ORDER BY employee_name ASC");
		returnOut = data.sendData(tempList).toString();
		lay = data.formatData(returnOut);
		for (String tmp : lay) {

			String[] arr = tmp.split("~~");
			if (!userId.equals(arr[0])) {
				Employee Employee = new Employee(arr[5], arr[1], false);
				Employee.setId(arr[0]);
				EmployeeList.add(Employee);
			}

		}
		// Toast.makeText(CreateShift.this, lay.toString(), Toast.LENGTH_LONG)
		// .show();
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
				Employee Employee = (Employee) parent
						.getItemAtPosition(position);
				// Toast.makeText(getApplicationContext(),
				// "Clicked on Row: " + Employee.getName(),
				// Toast.LENGTH_LONG).show();
			}
		});
	}

	private class MyCustomAdapter extends ArrayAdapter<Employee> {

		private ArrayList<Employee> EmployeeList;

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<Employee> EmployeeList) {
			super(context, textViewResourceId, EmployeeList);
			this.EmployeeList = new ArrayList<Employee>();
			this.EmployeeList.addAll(EmployeeList);
		}

		private class ViewHolder {
			TextView phone;
			CheckBox name;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
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
						// Toast.makeText(
						// getApplicationContext(),
						// "Clicked on Checkbox: " + cb.getText() + " is "
						// + cb.isChecked(), Toast.LENGTH_LONG)
						// .show();
						Employee.setSelected(cb.isChecked());
						if (cb.isChecked()) {
							selectedNumber++;
						} else {
							if (selectedNumber > 0) {
								selectedNumber--;
							}
						}
						String showNumber = "create(" + selectedNumber + ")";
						okButton.setText(showNumber);
					}

				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Employee Employee = EmployeeList.get(position);
			holder.phone.setText(" (" + Employee.getPhone() + ")");
			holder.name.setText(Employee.getName());
			holder.name.setChecked(Employee.isSelected());
			holder.name.setTag(Employee);

			return convertView;

		}

	}

	@SuppressLint("NewApi")
	private void checkButtonClick() {

		okButton = (Button) findViewById(R.id.findSelected);
		String showNumber = "create(" + selectedNumber + ")";
		okButton.setText(showNumber);
		okButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {

				ArrayList<Employee> EmployeeList = dataAdapter.EmployeeList;
				for (int i = 0; i < EmployeeList.size(); i++) {
					Employee Employee = EmployeeList.get(i);
					if (Employee.isSelected()) {
						numbers.add(Employee.getPhone());
						names.add(Employee.getName());
						id.add(Employee.getId());
					}
				}
				List<String> temp1 = new ArrayList<String>();
				temp1.add("get_group");
				temp1.add("groupName=\"" + listname.getText().toString()
						+ "\" and managerID=" + userId);
				name = data.sendData(temp1);
				namecode = listname.getText().toString();
				if (listname.getText().toString().equals("") | id.size() < 1) {
					Toast.makeText(getApplicationContext(),
							"Please confirm information", Toast.LENGTH_LONG)
							.show();
					numbers.clear();
					names.clear();
					id.clear();
				} else if (name.equals("")) {
					List<String> temp = new ArrayList<String>();
					int a = namecode.hashCode();
					String code = String.valueOf(a);
					temp.add("create_group");
					temp.add("managerID=" + userId);
					temp.add("groupName=\"" + listname.getText().toString()
							+ "\"");
					temp.add("groupID=" + userId + code);
					for (String tmp : id) {
						temp.add("\"" + tmp + "\"");
					}
					temp.add("Done");
					Toast.makeText(getApplicationContext(), temp.toString(),
							Toast.LENGTH_LONG).show();
					if (data.sendData(temp).equals("")) {
						Toast.makeText(getApplicationContext(),
								"Name is too long", Toast.LENGTH_LONG)
								.show();
						numbers.clear();
						names.clear();
						id.clear();
					} else {
						Toast.makeText(getApplicationContext(), "success",
								Toast.LENGTH_LONG).show();
						finish();
					}
				} else {
					Toast.makeText(getApplicationContext(), "list name exists",
							Toast.LENGTH_LONG).show();
					numbers.clear();
					names.clear();
					id.clear();
				}

			}
		});

	}
}
