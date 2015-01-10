package com.example.moocmusicz.ui;

import com.example.moocmusicz.R;
import com.example.moocmusicz.R.id;
import com.example.moocmusicz.R.layout;
import com.example.moocmusicz.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class AllPassView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_pass_view);
		
		findViewById(R.id.layout_bar_coin).setVisibility(View.INVISIBLE);
	}
}
