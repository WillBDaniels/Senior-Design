package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.haven.skilltest.demo.R;
import com.util.getInfo;
import com.util.userInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateShift extends Activity {

	private final static String TAG = "CreateShift";

	private MyCustomAdapter dataAdapter = null;
	private Button goto2;
	private Button goto1;
	int selectedNumber = 0;
	private String userId;
	private userInfo userinfo;
	private Button okButton = null;
	private Button cancelButton = null;
	private getInfo data;
	private List<String> tempList;
	private List<String> tempList2;
	private String returnOut;
	private ArrayList<String> lay = new ArrayList<String>();
	private ArrayList<String> numbers = new ArrayList<String>();
	private ArrayList<String> names = new ArrayList<String>();

	private List<String> groupDataList;

	private List<List<String>> childDataList;

	private ExpandableListView expandableListView;
	private ExpandableAdapter adapter;

	private List<Map<Integer, Integer>> isSelectedList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addemployee);
		goto2 = (Button) findViewById(R.id.goTo2);
		goto2.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jumpToLayout2();
			}

		});
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
		ArrayList<Employee> EmployeeList = new ArrayList<Employee>();
		tempList = new ArrayList<String>();
		data = new getInfo();
		userinfo = (userInfo) getApplication();
		userId = userinfo.getEmployeeId();
		tempList.add("employee_info");
		tempList.add("1=1 ORDER BY employee_name ASC");
		returnOut = data.sendData(tempList).toString();
		lay = data.formatData(returnOut);
		for (String tmp : lay) {

			String[] arr = tmp.split("~~");
			if (!userId.equals(arr[0])) {
				Employee Employee = new Employee(arr[5], arr[1], false);
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
						String showNumber = "ok(" + selectedNumber + ")";
//						okButton.setText(showNumber);
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

	private void checkButtonClick() {

		okButton = (Button) findViewById(R.id.findSelected);
		okButton.setOnClickListener(new okClick());

	}

	private class okClick implements Button.OnClickListener {

		public void onClick(View v) {

			// StringBuffer responseText = new StringBuffer();
			// responseText.append("The following were selected...\n");

			ArrayList<Employee> EmployeeList = dataAdapter.EmployeeList;
			for (int i = 0; i < EmployeeList.size(); i++) {
				Employee Employee = EmployeeList.get(i);
				if (Employee.isSelected()) {
					// responseText.append("\n" + Employee.getName());
					numbers.add(Employee.getPhone());
					names.add(Employee.getName());
				}
			}
			//
			// Toast.makeText(getApplicationContext(), numbers.toString(),
			// Toast.LENGTH_LONG).show();

			Intent data = new Intent();
			data.putExtra("numbers", numbers);
			data.putExtra("names", names);
			setResult(RESULT_OK, data);
			finish();
			//
			// Toast.makeText(getApplicationContext(), responseText,
			// Toast.LENGTH_LONG).show();

		}
	}

	private void jumpToLayout2() {
		setContentView(R.layout.addemployee2);
		goto1 = (Button) findViewById(R.id.goTo1);
		cancelButton = (Button) findViewById(R.id.addcancel);
		okButton = (Button) findViewById(R.id.findSelected);
		String showNumber = "ok(" + selectedNumber + ")";
//		okButton.setText(showNumber);
		okButton.setOnClickListener(new okClick());

		cancelButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});

		goto1.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jumpToLayout1();
			}

		});
		GroupList();
	}

	private void jumpToLayout1() {
		setContentView(R.layout.addemployee);

		displayListView();

		checkButtonClick();
		goto2 = (Button) findViewById(R.id.goTo2);
		goto2.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jumpToLayout2();
			}

		});
	}

	private void GroupList() {
		loadData();
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);

		// expandableListView.setGroupIndicator(getResources().getDrawable(
		// R.drawable.indicator_selector));

		expandableListView.setGroupIndicator(null);
		adapter = new ExpandableAdapter();
		expandableListView.setAdapter(adapter);
		expandableListView
				.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
							if (groupPosition != i) {
								expandableListView.collapseGroup(i);
							}
						}
					}
				});

		expandableListView
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						
						// Toast.makeText(
						// CreateShift.this,
						// "onChildClick===="
						// + adapter.getChild(groupPosition,
						// childPosition) + "===="
						// + v.getParent(), Toast.LENGTH_LONG)
						// .show();
						String new_name = adapter.getChild(groupPosition,
								childPosition).toString();
						String new_number = adapter.getChild(groupPosition,
								childPosition).toString();
						new_name = new_name.substring(0,
								new_number.indexOf("("));
						new_number = new_number.substring(
								new_number.indexOf("(") + 1,
								new_number.indexOf(")"));
						ViewHolder viewHolder = (ViewHolder) v.getTag();
						viewHolder.checkBox.toggle();
						if (viewHolder.checkBox.isChecked()) {
							
							if(!names.contains(new_name+""+groupPosition)){								
								names.add(new_name+""+groupPosition);
								selectedNumber = selectedNumber+1;
//								okButton.setText("ok(" + selectedNumber + ")");
							}
							if(!numbers.contains(new_number)){
								numbers.add(new_number);
							}							
							isSelectedList.get(groupPosition).put(
									childPosition, 3);
						} else {
							if(names.contains(new_name+"~~"+groupPosition)){
								names.remove(new_name+"~~"+groupPosition);
								if (selectedNumber > 0) {
									selectedNumber = selectedNumber - 1;
//									okButton.setText("ok(" + selectedNumber + ")");
								}
							}
							if(numbers.contains(new_number)){
								numbers.remove(new_number);
							}	
							// Toast.makeText(CreateShift.this,
							// numbers.toString(), Toast.LENGTH_LONG)
							// .show();
							isSelectedList.get(groupPosition).put(
									childPosition, 1);
						}
						int count = 0;
						for (int i = 0, size = isSelectedList
								.get(groupPosition).size(); i < size; i++) {
							if (isSelectedList.get(groupPosition).get(i) == 3) {
								count++;
							}
						}
						View view = (View) v.getParent();
						Log.d(TAG, "view=" + view.findViewById(R.id.checkBox1));
						CheckBox ck = (CheckBox) view
								.findViewById(R.id.checkBox1);
						if (count == isSelectedList.get(groupPosition).size()) {
							// ck.setBackgroundResource(R.drawable.btn_select);
							ck.setButtonDrawable(R.drawable.btn_select);
						} else if (count > 0) {
							// ck.setBackgroundResource(R.drawable.btn_half);
							ck.setButtonDrawable(R.drawable.btn_half);
						} else {
							// ck.setBackgroundResource(R.drawable.btn_unselect);
							ck.setButtonDrawable(R.drawable.btn_unselect);
						}
						adapter.notifyDataSetChanged();
						return false;
					}
				});
		isSelectedList = new ArrayList<Map<Integer, Integer>>();
		for (int i = 0, icount = expandableListView.getCount(); i < icount; i++) {
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (int j = 0, jcount = childDataList.get(i).size(); j < jcount; j++) {
				map.put(j, 1);
			}
			isSelectedList.add(map);
		}

	}

	private void showOrhide(boolean flag) {
		for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
			if (flag) {
				expandableListView.expandGroup(i);
			} else {
				expandableListView.collapseGroup(i);
			}
		}
	}

	private void loadData() {
		groupDataList = new ArrayList<String>();
		childDataList = new ArrayList<List<String>>();
		ArrayList<String> lay2 = new ArrayList<String>();
		tempList2 = new ArrayList<String>();
		tempList2.add("get_group");
		tempList2.add("managerID=" + userId + " group by groupName");
		lay2 = data.formatData(data.sendData(tempList2));
		for (String tmp : lay2) {
			int a = 0;
			String arr[] = tmp.split("~~");
			ArrayList<String> lay3 = new ArrayList<String>();
			ArrayList<String> tempList3 = new ArrayList<String>();
			List<String> child2 = new ArrayList<String>();
			String new_group = "Group:"+arr[3];
			groupDataList.add(new_group);
			tempList3.add("get_detailed_group");
			tempList3.add("groupID=" + arr[4]);
			lay3 = data.formatData(data.sendData(tempList3));
			for (String tm : lay3) {
				if (a > 0) {
					// Toast.makeText(CreateShift.this, tm, Toast.LENGTH_LONG)
					// .show();
					String arr1[] = tm.split("~~");
					child2.add(arr1[0] + "(" + arr1[1] + ")");
				}
				a++;
			}
			childDataList.add(child2);

		}
	}

	private class ExpandableAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return groupDataList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return childDataList.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupDataList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childDataList.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			Log.d(TAG, "getGroupView");
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = View.inflate(CreateShift.this,
						R.layout.expandablelistview2_groups, null);
				viewHolder = new ViewHolder();
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.textView1);
				viewHolder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkBox1);
				viewHolder.imageView = (ImageView) convertView
						.findViewById(R.id.imageView1);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.textView.setText(groupDataList.get(groupPosition));
			viewHolder.checkBox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							int flag = 0;
							if (isChecked) {
								flag = 3;
							} else {
								flag = 1;
							}
							for (int i = 0, size = isSelectedList.get(
									groupPosition).size(); i < size; i++) {
								isSelectedList.get(groupPosition).put(i, flag);
							}
							notifyDataSetChanged();
						}
					});
			int size = 0;
			for (int i = 0, count = isSelectedList.get(groupPosition).size(); i < count; i++) {
				if (isSelectedList.get(groupPosition).get(i) == 3) {
					size++;
				}
			}
			if (size == isSelectedList.get(groupPosition).size()) {// all
																	// selected
				for (int i = 0, count = isSelectedList.get(groupPosition)
						.size(); i < count; i++) {
					String new_name = adapter.getChild(groupPosition, i)
							.toString();
					String new_number = adapter.getChild(groupPosition, i)
							.toString();
					new_name = new_name.substring(0, new_number.indexOf("("));
					new_number = new_number.substring(
							new_number.indexOf("(") + 1,
							new_number.indexOf(")"));
					if (!names.contains(new_name+""+groupPosition)) {
						names.add(new_name+""+groupPosition);
						selectedNumber++;
					}
					if (!numbers.contains(new_number)) {
						numbers.add(new_number);
					}
				}
//				okButton.setText("ok(" + selectedNumber + ")");
//				Toast.makeText(CreateShift.this, numbers.toString(),
//						Toast.LENGTH_LONG).show();
				// viewHolder.checkBox
				// .setBackgroundResource(R.drawable.btn_select);
				viewHolder.checkBox.setButtonDrawable(R.drawable.btn_select);
			} else if (size > 0) {
				// viewHolder.checkBox.setBackgroundResource(R.drawable.btn_half);
				viewHolder.checkBox.setButtonDrawable(R.drawable.btn_half);
			} else {// all unselected

				for (int i = 0, count = isSelectedList.get(groupPosition)
						.size(); i < count; i++) {
					String new_name = adapter.getChild(groupPosition, i)
							.toString();
					String new_number = adapter.getChild(groupPosition, i)
							.toString();
					new_name = new_name.substring(0, new_number.indexOf("("));
					new_number = new_number.substring(
							new_number.indexOf("(") + 1,
							new_number.indexOf(")"));
					if (names.contains(new_name+""+groupPosition)) {
						names.remove(new_name+""+groupPosition);
						if (selectedNumber > 0) {
							selectedNumber--;
						}
					}
					if (numbers.contains(new_number)) {
						numbers.remove(new_number);
					}

				}
//				okButton.setText("ok(" + selectedNumber + ")");
				// viewHolder.checkBox
				// .setBackgroundResource(R.drawable.btn_unselect);
				viewHolder.checkBox.setButtonDrawable(R.drawable.btn_unselect);
			}

			// 判断isExpanded就可以控制是按下还是关闭，同时更换图片
			if (isExpanded) {
				viewHolder.imageView
						.setBackgroundResource(R.drawable.shangjiantou);
			} else {
				viewHolder.imageView
						.setBackgroundResource(R.drawable.xiajiantou);
			}

			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			Log.d(TAG, "getChildView");
			// TextView textView = null;
			// if (null != convertView) {
			// textView = (TextView) convertView;
			// textView.setText(childDataList.get(groupPosition).get(
			// childPosition));
			// } else {
			// textView = createView(childDataList.get(groupPosition).get(
			// childPosition));
			// }
			// return textView;

			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = View.inflate(CreateShift.this,
						R.layout.expandablelistview2_child, null);
				viewHolder = new ViewHolder();
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.textView1);

				viewHolder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkBox1);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (isSelectedList.get(groupPosition).get(childPosition) == 3) {
				// viewHolder.checkBox
				// .setBackgroundResource(R.drawable.btn_select);
				viewHolder.checkBox.setButtonDrawable(R.drawable.btn_select);
			} else {
				// viewHolder.checkBox
				// .setBackgroundResource(R.drawable.btn_unselect);
				viewHolder.checkBox.setButtonDrawable(R.drawable.btn_unselect);
			}
			viewHolder.textView.setText(childDataList.get(groupPosition).get(
					childPosition));
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		private TextView createView(String content) {
			TextView textView = null;
			AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 80);
			textView = new TextView(CreateShift.this);
			textView.setLayoutParams(layoutParams);
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			textView.setPadding(80, 0, 0, 0);
			textView.setText(content);
			return textView;
		}

	}

	private class ViewHolder {
		TextView textView;
		CheckBox checkBox;
		ImageView imageView;
	}

}
