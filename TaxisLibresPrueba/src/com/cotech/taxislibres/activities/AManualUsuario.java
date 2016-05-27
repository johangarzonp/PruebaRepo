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
public class AManualUsuario extends Activity {
	
	private static String TAG= "ManualUsuario";
	private static String module ="AManualUsuario";
	
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i(TAG, module +"PASA X onCreate ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_termycond);
		WebView viewManualUsuario = (WebView) findViewById(R.id.webterminos);
		viewManualUsuario.getSettings().setJavaScriptEnabled(true);
		viewManualUsuario.getSettings().setLoadWithOverviewMode(true);
		viewManualUsuario.getSettings().setUseWideViewPort(true);   
		viewManualUsuario.setWebChromeClient(new WebChromeClient());
		viewManualUsuario.clearCache(true);
		viewManualUsuario.loadUrl("http://taxislibres.com.co:8035/AdminVales/terms/manualuser.html");
		
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
