package com.cotech.taxislibres.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;


import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;

public class ASinInternet extends Activity {
	protected String APPNAME = "TaxisLibres";
	protected String module = C.SININTERNET;
	
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ventanasininternet);
		
		new Handler().postDelayed(new Runnable(){
			public void run(){

				try {
					Intent i = getIntent();
					i.putExtra("cerrarApp", "nada");
					setResult(RESULT_OK, i);
					finish();
					
				} catch (Exception e) {
					e.printStackTrace();
				}	

			};
		}, 3000);
		
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
