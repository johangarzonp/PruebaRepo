package com.cotech.taxislibres.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;

public class ACalificarServicio extends Activity {
	protected String APPNAME = "TaxisLibres";
	protected String module = C.CALIFICARSERVICIO;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ventanadeseacalificar);
		
		new Handler().postDelayed(new Runnable(){
			public void run(){
				Log.i(APPNAME, module + "Salir porque el usuario no presiono NADA...");
				String s = "NoCalificarServicio";

				Intent i = getIntent();
				i.putExtra("noCalificarServicio", s);
				setResult(RESULT_OK, i);
				finish();
			};
		}, 10000);
		
	}
	
	//****************************************************************************************
	public void CalificarServicio(View v){
		String s = "CalificarServicio";

		Intent i = getIntent();
		i.putExtra("cafilicarServicio", s);
		setResult(RESULT_OK, i);
		finish();

	}
	
	//****************************************************************************************
	public void NoCalificarServicio(View v){
		String s = "NoCalificarServicio";

		Intent i = getIntent();
		i.putExtra("noCalificarServicio", s);
		setResult(RESULT_OK, i);
		finish();

	}
	//****************************************************************************************
	
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
		}
		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
		}
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
		}
		@Override
		protected void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}
		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
		}
	

}
