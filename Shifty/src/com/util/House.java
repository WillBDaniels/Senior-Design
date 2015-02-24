package com.util;

import java.util.ArrayList;
import java.util.List;


import com.haven.skilltest.demo.R;
import com.service.CreateShift;
import com.service.SendMSM;
import com.service.ShiftHistory;
import com.start.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class House extends Activity {
	private getInfo getinfo;
	private ArrayList<String> lay;
	private String returnout;
	private String houseId;
	private TextView textview;
	private List<String> tempList;
	private ListView listInfo;
	private String[] shift = { "time --- location"};
	private ArrayAdapter<String> contentListAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.house);
		textview = (TextView) findViewById(R.id.test);
		listInfo = (ListView) findViewById(R.id.listinfo);
		tempList = new ArrayList<String>();
		getinfo = new getInfo();
		Bundle bundle = this.getIntent().getExtras();
		houseId = bundle.getString("houseId");
		// textview.setText(position);
		whichHouse(houseId);
		
		
		listInfo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String info = shift[position];
				Intent intent = new Intent();
				intent.setClass(House.this, SendMSM.class);
				Bundle bundle = new Bundle();
				bundle.putString("massage", info);
				intent.putExtras(bundle);
				startActivity(intent);
//				Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();		
			}
		});
	}

	protected void whichHouse(String data) {
		
			tempList.add("get_shift_specific");
			tempList.add("location=\""+data+"\"");
			returnout = getinfo.sendData(tempList).toString();
			lay = getinfo.formatData(returnout);
			for(String tmp:lay){
				String[] arr = tmp.split("~~");
				shift = insert(shift, arr[1]+" --- House"+arr[3]);
			}

			contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
					shift);
			listInfo.setAdapter(contentListAdapter);
		
	}
	
	private static String[] insert(String[] arr, String str) {
		int size = arr.length;

		String[] tmp = new String[size + 1];

		System.arraycopy(arr, 0, tmp, 0, size);

		tmp[size] = str;

		return tmp;
	}

}
