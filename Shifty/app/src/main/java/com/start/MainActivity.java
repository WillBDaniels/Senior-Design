package com.start;

import java.util.ArrayList;
import java.util.List;

import com.haven.skilltest.demo.R;
import com.service.MSMpage;
import com.service.ShiftHistory;
import com.util.House;
import com.util.HouseActivity;
import com.util.getInfo;
import com.util.userInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	

	/**
	 * used to show or hide layout
	 */
	private SlidingLayout slidingLayout;

	
	private Button menuButton;

	
	private ListView contentListView;
	
	private ListView menuContentListView;

	
	private ArrayAdapter<String> contentListAdapter;
	
	private ArrayAdapter<String> menuContentListAdapter;

	
	private String[] contentItems = { "Create Shift","Shift History"};
	private String[] menuContentItems = {"shift history","user center","setting"};
	String returnOut = "";
	String newOne = "";
	private int houseNumber;
	private userInfo userinfo;
	private String houseId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		houseNumber = 0;

		for(House tmp : getInfo.currentPojo.getHouseList()){
			newOne = "House";
			newOne = newOne + tmp.getHouseName();
			contentItems = insert(contentItems,newOne);
			houseNumber++;
		}
		
		userinfo = (userInfo) getApplication();
		userinfo.setHouseNumber(houseNumber);
		setContentView(R.layout.activity_main);
		
		slidingLayout = (SlidingLayout) findViewById(R.id.slidingLayout);
		menuButton = (Button) findViewById(R.id.menuButton);
		contentListView = (ListView) findViewById(R.id.contentList);
		
		menuContentListView = (ListView) findViewById(R.id.menucontentlist);
		menuContentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				menuContentItems);
		menuContentListView.setAdapter(menuContentListAdapter);
		
		contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				contentItems);
		contentListView.setAdapter(contentListAdapter);
		
		
		
		slidingLayout.setScrollEvent(contentListView);
		menuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (slidingLayout.isLeftLayoutVisible()) {
					slidingLayout.scrollToRightLayout();
				} else {
					slidingLayout.scrollToLeftLayout();
				}
			}
		});
		contentListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String text = contentItems[position];
//				Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();	
				if(position == 0){
					Intent intent2 = new Intent();
					intent2.setClass(MainActivity.this, MSMpage.class);
					startActivity(intent2);
					
				}else if(position == 1){
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ShiftHistory.class);
//					Bundle bundle = new Bundle();
//					bundle.putInt("position", position);
//					intent.putExtras(bundle);
					startActivity(intent);
					
				}else{
					House house = getInfo.currentPojo.getHouseList().get(position-2);
					houseId = String.valueOf(house.getHouseID());
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, HouseActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("houseId", houseId);
					intent.putExtras(bundle);
					startActivity(intent);
				}
				
			}
		});
		
		menuContentListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String text = menuContentItems[position];
//				Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();		
			}
		});
		
		
	}
	
	private static String[] insert(String[] arr, String str)
    {
        int size = arr.length;
        
        String[] tmp = new String[size + 1];
        
        System.arraycopy(arr, 0, tmp, 0, size);
        
        tmp[size] = str;
        
        return tmp;
    }

}
