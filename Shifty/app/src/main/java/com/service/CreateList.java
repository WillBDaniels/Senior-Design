package com.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.util.Action;
import com.util.DataPOJO;
import com.util.Employee;
import com.util.Group;
import com.util.HolderClass;
import com.util.Type;
import com.util.getInfo;
import com.util.userInfo;

import java.util.ArrayList;
import java.util.List;

import edu.csci.teamshifty.R;

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
        userId = getInfo.employeeID;
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
        List<com.util.Employee> EmployeeList;
        EmployeeList = getInfo.currentPojo.getAllEmployees();
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
		String showNumber = "create(" + selectedNumber + ")";
		okButton.setText(showNumber);
		okButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				ArrayList<Employee> EmployeeList = dataAdapter.EmployeeList;
				for (int i = 0; i < EmployeeList.size(); i++) {
					Employee Employee = EmployeeList.get(i);
					if (Employee.isSelected()) {
						numbers.add(Employee.getPhoneNumber());
						names.add(Employee.getName());
						id.add(String.valueOf(Employee.getEmployeeID()));
					}
				}
                for (Group group : getInfo.currentPojo.getGroupList()){
                    if (group.getGroupName().equals(listname.getText().toString()) && group.getManagerID() == Integer.valueOf(userId)){
                        name = group.getGroupName();
                    }
                }
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
					int code = a;
                    Group group = new Group();
                    List<Group> groupList = new ArrayList<>();
                    DataPOJO groupPOJO = new DataPOJO();
                    group.setManagerID(Integer.valueOf(userId));
                    group.setGroupName(listname.getText().toString());
                    group.setGroupID(Integer.valueOf(userId) + code);
                    List<Employee> empList = new ArrayList<>();
                    for (String empId : id){
                        Employee emp = new Employee();
                        emp.setEmployeeID(Integer.valueOf(empId));
                        empList.add(emp);
                    }
                    group.setEmpList(empList);
                    groupList.add(group);
                    groupPOJO.setGroupList(groupList);
                    Gson gson = new Gson();
                    HolderClass hc = new HolderClass();
                    hc.setJson(gson.toJson(groupPOJO, DataPOJO.class));
                    hc.setType(Type.SHIFTY);
                    hc.setAction(Action.ADDGROUP);
                    new CreateGroupOperation().execute(hc);

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
    private class CreateGroupOperation extends AsyncTask<HolderClass, Void, String> {

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
                ((ProgressBar) findViewById(R.id.progressBar2)).setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Group created successfully!",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "success",
                        Toast.LENGTH_LONG).show();
                finish();
            }else{
                ((ProgressBar) findViewById(R.id.progressBar2)).setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Group creation error!",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "success",
                        Toast.LENGTH_LONG).show();
                finish();
            }




        }

        @Override
        protected void onPreExecute() {
            ((ProgressBar)findViewById(R.id.progressBar2)).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
