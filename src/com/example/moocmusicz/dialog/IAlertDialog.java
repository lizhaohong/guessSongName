package com.example.moocmusicz.dialog;

import com.example.moocmusicz.R;
import com.example.moocmusicz.R.id;
import com.example.moocmusicz.R.layout;
import com.example.moocmusicz.R.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

public class IAlertDialog extends Dialog {
	
	public ImageButton getOk_image_button() {
		return ok_image_button;
	}

	public void setOk_image_button(ImageButton ok_image_button) {
		this.ok_image_button = ok_image_button;
	}

	public ImageButton getCancel_image_button() {
		return cancel_image_button;
	}

	public void setCancel_image_button(ImageButton cancel_image_button) {
		this.cancel_image_button = cancel_image_button;
	}

	public TextView getContent_text_view() {
		return content_text_view;
	}

	public void setContent_text_view(TextView content_text_view) {
		this.content_text_view = content_text_view;
	}
	
	public void setContent_text_view_content(String content) {
		this.content_text_view.setText(content);	
	}

	public ImageButton ok_image_button;
	public ImageButton cancel_image_button;
	public TextView content_text_view;
	
	public IAlertDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public IAlertDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ialert_dialog);
		
		
		ok_image_button = (ImageButton)findViewById(R.id.ok_image_button);
		cancel_image_button = (ImageButton)findViewById(R.id.cancel_image_button);
		content_text_view = (TextView)findViewById(R.id.content_text_view);
	}
}
