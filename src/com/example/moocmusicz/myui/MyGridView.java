package com.example.moocmusicz.myui;

import java.util.ArrayList;
import java.util.List;

import com.example.moocmusicz.R;
import com.example.moocmusicz.model.IWordButtonClickListener;
import com.example.moocmusicz.model.WordButton;
import com.example.moocmusicz.util.Util;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class MyGridView extends GridView {

	public IWordButtonClickListener listener;
	public ArrayList<WordButton>lists;
	public MyGridViewAdapter adapter;
	public Context context;
	public Animation animationSet;
	public final static int TOTAL_WORD_BUTTON_NUMBER = 24;
	
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		lists = new ArrayList<WordButton>();
		adapter = new MyGridViewAdapter();
		this.setAdapter(adapter);
	}
	
	public void updateGridView(ArrayList<WordButton>lists) {
		this.lists = lists;
		
		setAdapter(adapter);
	}

	public class MyGridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final WordButton wordButton;
			if(convertView == null) {
				wordButton = lists.get(position);
				convertView = Util.getView(context, R.layout.self_ui_gridview_item);
				wordButton.mIndex = position;
				wordButton.mButton = (Button) convertView.findViewById(R.id.button_item);
				
				animationSet = AnimationUtils.loadAnimation(context,R.anim.scale);
				animationSet.setStartOffset(position * 100);
				wordButton.mButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						listener.OnWordButtonClick(wordButton);
					}
				});
				
				convertView.setTag(wordButton);
			} else {
				wordButton = (WordButton) convertView.getTag();
			}
			wordButton.mButton.setText(wordButton.mWordButtonName);
			convertView.setAnimation(animationSet);
			return convertView;
		}
	}
	
	public void registerListener(IWordButtonClickListener listener) {
		this.listener = listener;
	}
}
