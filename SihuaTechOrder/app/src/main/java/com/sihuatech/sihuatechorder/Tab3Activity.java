package com.sihuatech.sihuatechorder;


import android.os.Bundle;
import android.app.Activity;
import android.view.Window;

public class Tab3Activity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab03);
	}
}
