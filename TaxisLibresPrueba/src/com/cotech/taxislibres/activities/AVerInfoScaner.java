package com.cotech.taxislibres.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;

@SuppressLint("SetJavaScriptEnabled")
public class AVerInfoScaner extends Activity {
	
	private static String TAG= "InfoScaner";
	private static String module ="AVerInfoScaner";
	
	private Context context;
	private TaxisLi appState;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i(TAG, module +"PASA X onCreate ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_termycond);
		
		context = getApplicationContext();
		appState = ((TaxisLi) context);
		
		WebView viewInfoTaxista = (WebView) findViewById(R.id.webterminos);
		viewInfoTaxista.getSettings().setJavaScriptEnabled(true);
		viewInfoTaxista.getSettings().setLoadWithOverviewMode(true);
		viewInfoTaxista.getSettings().setUseWideViewPort(true);   
		viewInfoTaxista.setWebChromeClient(new WebChromeClient());
		viewInfoTaxista.clearCache(true);
		viewInfoTaxista.loadUrl(appState.getUrlInfoTaxista());
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
