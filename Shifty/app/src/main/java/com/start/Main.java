package com.start;

import java.util.ArrayList;
import java.util.List;

import com.haven.skilltest.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;

import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageView;
import android.widget.LinearLayout;

public class Main extends Activity {
	private Animation anm;
	private int marginsTop;
	public List<ImageView> images;
	public LinearLayout ll;
	int times = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		DisplayMetrics dm = this.getResources().getDisplayMetrics();

		int height = dm.heightPixels;
		marginsTop = height - 100;
		anm = AnimationUtils.loadAnimation(this, R.anim.myanim);
		ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.background);

		images = new ArrayList<ImageView>();
		initImage(ll);
		setContentView(ll);
		playAnimation();
		
    }

    private void playAnimation(){    	
    		new Thread(){
    			@Override
    			public void run()
    			{
    				try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					int runcount=0;
    				while(true)
    				{
    					if(runcount<2)
    					{
	    					for(int i=0;i<=6;i++)
	    					{
	    						handler.sendEmptyMessage(i);
	    						try {
	    							Thread.sleep(400);
	    						} catch (InterruptedException e) {
	    							e.printStackTrace();
	    						}
	    					}
	    					runcount++;
    					}else
    					{
    						if(times>1){
    							handler.sendEmptyMessage(100);
    							break;
    						}
    						handler.sendEmptyMessage(99);
    						runcount=0;
    					}
    					times++;
    				}
    			}

    		}.start();
    	}
    	

    Handler handler=new Handler(){

    			@Override
    			public void handleMessage(Message msg) {  				
    				switch(msg.what)
    				{
    				case 0:
    					images.get(0).setImageDrawable(Main.this.getResources().getDrawable(R.drawable.l));
    					images.get(0).startAnimation(anm);
    					
    					break;
    				case 1:
    					images.get(1).setImageDrawable(Main.this.getResources().getDrawable(R.drawable.o));
    					images.get(1).startAnimation(anm);
    					break;
    				case 2:
    					images.get(2).setImageDrawable(Main.this.getResources().getDrawable(R.drawable.a));
    					images.get(2).startAnimation(anm);
    					break;
    				case 3:
    					images.get(3).setImageDrawable(Main.this.getResources().getDrawable(R.drawable.d));
    					images.get(3).startAnimation(anm);
    					break;
    				case 4:
    					images.get(4).setImageDrawable(Main.this.getResources().getDrawable(R.drawable.i));
    					images.get(4).startAnimation(anm);
    					break;
    				case 5:
    					images.get(5).setImageDrawable(Main.this.getResources().getDrawable(R.drawable.n));
    					images.get(5).setAnimation(anm);
    					break;
    				case 6:
    					images.get(6).setImageDrawable(Main.this.getResources().getDrawable(R.drawable.g));
    					images.get(6).setAnimation(anm);
    					break;
    				case 99:
    					clearImage();
    					break;
    				case 100:
    					GotoLogin();
    					break;
    				}
    			}
    		};
    
    private void GotoLogin(){
    	Intent intent = new Intent();
    	intent.setClass(Main.this, login.class);
    	finish();
    	startActivity(intent);
    	
    }
    		
    private void clearImage()
    {
    	for(ImageView image:images)
    	{
    		image.setImageDrawable(null);
    		image.destroyDrawingCache();
    	}
    }
    
    private void initImage(LinearLayout layout) {
    	
    	layout.setGravity(Gravity.CENTER_HORIZONTAL);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(40, 40);
		param.setMargins(30, marginsTop, 0, 0);
		
		LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(40, 40);
		param2.setMargins(-5, marginsTop, 0, 0);

		ImageView l = new ImageView(this);
		l.setLayoutParams(param);
		layout.addView(l);
		images.add(l);

		ImageView o = new ImageView(this);
		o.setLayoutParams(param2);
		layout.addView(o);
		images.add(o);
		
		
		ImageView a = new ImageView(this);
		a.setLayoutParams(param2);
		layout.addView(a);
		images.add(a);
		
		ImageView d = new ImageView(this);
		d.setLayoutParams(param2);
		layout.addView(d);
		images.add(d);
		
		ImageView i = new ImageView(this);
		i.setLayoutParams(param2);
		layout.addView(i);
		images.add(i);
		
		ImageView n = new ImageView(this);
		n.setLayoutParams(param2);
		layout.addView(n);
		images.add(n);
		
		ImageView g = new ImageView(this);
		g.setLayoutParams(param2);
		layout.addView(g);
		images.add(g);
	}
}