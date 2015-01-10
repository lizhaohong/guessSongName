package com.example.moocmusicz.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.example.moocmusicz.R;
import com.example.moocmusicz.R.id;
import com.example.moocmusicz.R.layout;
import com.example.moocmusicz.R.menu;
import com.example.moocmusicz.ui.MainActivity;
import com.example.moocmusicz.util.ViewPagerAdapter;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Guide extends Activity implements OnPageChangeListener {

	public ViewPagerAdapter viewPagerAdapter;
	public ViewPager viewPager;
	public List<View>lists;
	public ImageView[] imageViews;
	public Button button;
	public LayoutInflater layoutInflater;
	public int[] ids = {R.id.iv1,R.id.iv2,R.id.iv3};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
	
		initView();
		initPicture();
	}
	
	public void initView() {
		
		layoutInflater = LayoutInflater.from(this);
		lists = new ArrayList<View>();
		imageViews = new ImageView[ids.length];
		lists.add(layoutInflater.inflate(R.layout.one, null));
		lists.add(layoutInflater.inflate(R.layout.two, null));
		lists.add(layoutInflater.inflate(R.layout.three, null));
	
		button = (Button) lists.get(2).findViewById(R.id.button);
		viewPagerAdapter = new ViewPagerAdapter(lists,this);
		viewPager = (ViewPager)findViewById(R.id.viewpager);
		viewPager.setAdapter(viewPagerAdapter);
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Guide.this,MainActivity.class);
				startActivity(intent);
			}
		});
		
		viewPager.setOnPageChangeListener(this);
	}
	
	public void initPicture() {
		for(int i = 0 ;i < ids.length; i++) {
			imageViews[i] = (ImageView) findViewById(ids[i]);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		for(int i = 0; i < ids.length; i++) {
			if(arg0 == i) {
				imageViews[i].setImageResource(R.drawable.login_point_selected);
			} else {
				imageViews[i].setImageResource(R.drawable.login_point);
			}
		}
	}
	
	
}
