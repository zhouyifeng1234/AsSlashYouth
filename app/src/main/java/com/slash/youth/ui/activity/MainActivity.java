package com.slash.youth.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.slash.youth.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("Start Slash Youth");
	}
}
