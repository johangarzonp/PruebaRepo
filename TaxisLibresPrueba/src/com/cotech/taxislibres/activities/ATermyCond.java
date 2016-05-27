package com.cotech.taxislibres.activities;

import com.cotech.taxislibres.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
//import android.util.Log;
import com.cotech.taxislibres.activities.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class ATermyCond extends Activity {
	
	private static String TAG= "TerminosyCondiciones";
	private static String module ="ATermyCond";
	
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i(TAG, module +"PASA X onCreate ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_termycond);
		WebView viewTermycond = (WebView) findViewById(R.id.webterminos);
		viewTermycond.getSettings().setJavaScriptEnabled(true);
		viewTermycond.getSettings().setLoadWithOverviewMode(true);
		viewTermycond.getSettings().setUseWideViewPort(true);   
		viewTermycond.setWebChromeClient(new WebChromeClient());
		viewTermycond.clearCache(true);
		viewTermycond.loadUrl("http://taxislibres.com.co:8035/AdminVales/terms/");
		
	}
	//-----------------------------------------------------------------
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, module +"PASA X onDestroy ");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(TAG, module +"PASA X onPause ");
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(TAG, module +"PASA X onResume ");
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(TAG, module +"PASA X onStart ");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(TAG, module +"PASA X onStop ");
		super.onStop();
	}

}
