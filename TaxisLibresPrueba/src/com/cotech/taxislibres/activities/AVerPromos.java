package com.cotech.taxislibres.activities;

import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;

public class AVerPromos extends Activity {

	private static String TAG= "TaxisLibres";
	private static String module ="AVerPromos";
	
	private Context context;
	private TaxisLi appState;
	
	private boolean mostrarCheck=false;
	private ImageButton checkBox;

	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, module +"PASA X onCreate ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ver_promociones);
		
		checkBox= (ImageButton) findViewById(R.id.checkpromo);
		checkBox.setVisibility(View.INVISIBLE);
		
		context = getApplicationContext();
		appState = ((TaxisLi) context);
		
		WebView viewVerPromos = (WebView) findViewById(R.id.webpromos);
		viewVerPromos.getSettings().setJavaScriptEnabled(true);
		viewVerPromos.getSettings().setLoadWithOverviewMode(true);
		viewVerPromos.getSettings().setUseWideViewPort(true);   
		viewVerPromos.setWebChromeClient(new WebChromeClient());
		viewVerPromos.clearCache(true);
		//context.deleteDatabase("webview.db");
		//context.deleteDatabase("webviewCache.db");
		//viewVerPromos.loadUrl("http://www.viajesimperialsas.com/imgn100/siteApp/promocion.html");
		viewVerPromos.loadUrl(appState.getUrlPromo());
		//viewVerPromos.loadUrl("http://www.viajesimperialsas.com/imgn100/siteApp/demo/index.html");
	}
	//-----------------------------------------------------------------
	public void SalirPromo(View v){
		String s = "SalirPromo";

		Intent i = getIntent();
		i.putExtra("nohacernada", s);
		setResult(RESULT_OK, i);
		finish();
	}
	//-----------------------------------------------------------------
	public void NoPromo(View v){
		checkBox.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable(){
			public void run(){
				Intent i = getIntent();
				i.putExtra("NoverPromo", "NoverPromo");
				setResult(RESULT_OK, i);
				finish();
			};
		}, 500);
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
