package com.cotech.taxislibres.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;

public class ATaxistaCanceloServ extends Activity {
	protected String APPNAME = "TaxisLibres";
	protected String module = C.TAXISTACANCELO;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ventanataxicancelo);
		
	}
	
	//****************************************************************************************
	public void SiSolicitar(View v){
		String s = "SiSolicitarServicioNuevamente";

		Intent i = getIntent();
		i.putExtra("nuevaSolicitud", s);
		setResult(RESULT_OK, i);
		finish();

	}
	
	//****************************************************************************************
	public void NoSolicitar(View v){
		String s = "NoSolicitarServicioNuevamente";

		Intent i = getIntent();
		i.putExtra("cerrarApp", s);
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
