package com.cotech.taxislibres.activities;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class ASalirApp extends Activity {
	protected String APPNAME = "TaxisLibres";
	protected String module = C.SALIRAPP;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ventanacerrarapp);
		
		new Handler().postDelayed(new Runnable(){
			public void run(){
				Log.i(APPNAME, module + "Salir porque el usuario no presiono NADA...");
				String s = "No se hace nada";

				Intent i = getIntent();
				i.putExtra("cancela", s);
				setResult(RESULT_OK, i);
				finish();
			};
		}, 10000);
		
	}
	
	//****************************************************************************************
	public void SalirApp(View v){
		String s = "CerrarApp";

		Intent i = getIntent();
		i.putExtra("cerrarApp", s);
		setResult(RESULT_OK, i);
		finish();

	}
	
	//****************************************************************************************
	public void CancelarCerrar(View v){
		String s = "No se solicita el servicio";

		Intent i = getIntent();
		i.putExtra("cancela", s);
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
