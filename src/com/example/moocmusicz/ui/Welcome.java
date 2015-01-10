package com.example.moocmusicz.ui;
import com.example.moocmusicz.R;

import com.example.moocmusicz.util.Util;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Welcome extends Activity {

	public int [] ids = {R.id.text1,R.id.text2,R.id.text3,R.id.text4};
	public final int GO_HOME = 1000;
	public final int GO_GUIDE = 1001;
	public boolean IsFirstIn = true;
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case GO_HOME:
				Intent intent = new Intent();
				intent.setClass(Welcome.this,MainActivity.class);
				startActivity(intent);
				break;
			case GO_GUIDE:
				Intent intent2 = new Intent();
				intent2.setClass(Welcome.this,Guide.class);
				startActivity(intent2);
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		if(!Util.isNetworkConnected(this)) {
			//选择网络连接
			Util.openNetworkSettings(this, Welcome.this);
		} else {
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
			animation.setStartOffset(500);
			
			for(int i = 0; i < ids.length; i++) {
				TextView textView = (TextView) findViewById(ids[i]);
				textView.setAnimation(animation);
			}
			
			init();
		}
	}
	
	public void init() {
	
		SharedPreferences sharedPreferences = getSharedPreferences("lizhaohong", MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		IsFirstIn = sharedPreferences.getBoolean("IsFirstIn", true);
		
		if(IsFirstIn) {
			handler.sendEmptyMessageDelayed(GO_GUIDE, 2000);
			
			editor.putBoolean("IsFirstIn",false);
			editor.commit();
		} else {
			handler.sendEmptyMessageDelayed(GO_HOME, 2000);
		}
	}
}
