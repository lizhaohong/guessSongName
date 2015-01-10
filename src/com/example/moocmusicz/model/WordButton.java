package com.example.moocmusicz.model;

import android.widget.Button;

public class WordButton {

	public int mIndex;
	public String mWordButtonName;
	public boolean mVisible;
	public Button mButton;
	
	public WordButton() {//构造函数，初始化按钮
		mVisible = true;
		mWordButtonName = "";
	}
	
}
