package com.service;

import java.util.ArrayList;
import java.util.List;

import com.haven.skilltest.demo.R;
import com.util.House;
import com.util.Shift;
import com.util.getInfo;
import com.util.userInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShiftHistory extends Activity {

	private String[] shift = { "time --- location"};
	private ArrayList<String> lay = new ArrayList<String>();
	private ListView showHistory;
	private ArrayAdapter<String> history;
	private userInfo userinfo;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shifthistory);
		
		userinfo = (userInfo) getApplication();
		List<String> temp = new ArrayList<>();
		getInfo data = new getInfo();	
		temp.add("get_shift_specific");
//		Toast.makeText(ShiftHistory.this, "employee1=\""+userinfo.getEmployeeId()+"\"", Toast.LENGTH_LONG).show();
		for (House house: getInfo.currentPojo.getHouseList()){
            for (Shift shiftInner : house.getShiftList()){
                if (shiftInner.getEmployeeID() == Integer.valueOf(userinfo.getEmployeeId())){
                    shift = insert(shift, shiftInner.getTime() + " --- House" + house.getHouseName());
                }
            }
        }

		
		showHistory = (ListView) findViewById(R.id.showhistory);
		history = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				shift);
		showHistory.setAdapter(history);
		
		
		showHistory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String info = shift[position];
				Intent intent = new Intent();
				intent.setClass(ShiftHistory.this, SendMSM.class);
				Bundle bundle = new Bundle();
				bundle.putString("massage", info);
				intent.putExtras(bundle);
				startActivity(intent);
//				Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();		
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
