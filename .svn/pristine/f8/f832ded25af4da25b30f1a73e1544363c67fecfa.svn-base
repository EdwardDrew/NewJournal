package com.ipbase.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ipbase.R;

public class SettingActivity extends Activity
{
	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_settings );

		TextView text = (TextView) findViewById( R.id.textView );

		Intent receive = getIntent();
		String flog = receive.getStringExtra( "flog" );

		text.setText( flog );

	}
};