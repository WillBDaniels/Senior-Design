package com.service;

import java.util.ArrayList;
import java.util.List;

import com.haven.skilltest.demo.R;
import com.util.Employee;
import com.util.getInfo;
import com.util.userInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class CreateShift extends Activity {

	private MyCustomAdapter dataAdapter = null;
	int selectedNumber = 0;
	private String userId;
	private userInfo userinfo;
	private Button okButton = null;
	private Button cancelButton = null;
	private getInfo data;
	private List<String> tempList;
	private String returnOut;
	private ArrayList<String> lay = new ArrayList<String>();
	private ArrayList<String> numbers = new ArrayList<String>();
	private ArrayList<String> names = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addemployee);
		cancelButton = (Button) findViewById(R.id.addcancel);

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
						String showNumber = "ok(" + selectedNumber + ")";
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

	private void checkButtonClick() {

		okButton = (Button) findViewById(R.id.findSelected);
		String showNumber = "ok(" + selectedNumber + ")";
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

				Intent data = new Intent();
				data.putExtra("numbers", numbers);
				data.putExtra("names", names);
				setResult(RESULT_OK,data);
				finish();
//				
//				Toast.makeText(getApplicationContext(), responseText,
//						Toast.LENGTH_LONG).show();

			}
		});

	}

	private void jumpToLayout2() {
		setContentView(R.layout.addemployee2);
		Button goto1 = (Button) findViewById(R.id.goTo1);
		goto1.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jumpToLayout1();
			}

		});
	}

	private void jumpToLayout1() {
		setContentView(R.layout.addemployee);

		displayListView();

		checkButtonClick();
		// Button goto2 = (Button)findViewById(R.id.goTo2);
		// goto2.setOnClickListener(new Button.OnClickListener(){
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// jumpToLayout2();
		// }
		//
		// });
	}
}
