package com.cotech.taxislibres.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.TaxisLi.TrackerName;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ASolicitarNuevamente extends Activity {
	protected String APPNAME = "TaxisLibres";
	protected String module = C.NVASOLICITUD;
	
	private TaxisLi appState;
	private TextView contadorSeg;
	private int cont;
	private boolean flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.solicitarnuevamenteservicio);
		contadorSeg= (TextView) findViewById(R.id.contadortiempo);
		final Context context = getApplicationContext();
		appState = ((TaxisLi) context);
		
		cont=5;
		flag=true;
		new Handler().postDelayed(new Runnable(){
			public void run(){
				MostrarContador();
			};
		}, 1000);
		
		
	}
	
	//****************************************************************************************
	public void MostrarContador(){
		cont--;
		contadorSeg.setText(String.valueOf(cont));
		ChequearContador();	
	}
	//****************************************************************************************
	public void ChequearContador(){
		if(cont>0){
			new Handler().postDelayed(new Runnable(){
				public void run(){
					MostrarContador();
				};
			}, 1000);	
		}
		else{
			
			try{
				Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
				t.setScreenName("Reintento de Buscar Taxi");
				t.send(new HitBuilders.AppViewBuilder().build());
			}catch(Exception e){
				e.printStackTrace();
			}
			
			String s = "NuevaSolicitud";
			Intent i = getIntent();
			i.putExtra("nuevaSolicitud", s);
			setResult(RESULT_OK, i);
			finish();
		
		}
	}




	//****************************************************************************************
//	public void NuevaSolicitud(View v){
//		String s = "NuevaSolicitud";
//
//		Intent i = getIntent();
//		i.putExtra("nuevaSolicitud", s);
//		setResult(RESULT_OK, i);
//		finish();
//
//	}
	
	//****************************************************************************************
	public void CancelarSolicitud(View v){
		
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Usuario Cancela Reintento de Buscar Taxi");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
		
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
			GoogleAnalytics.getInstance(ASolicitarNuevamente.this).reportActivityStart(this);
		}
		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			GoogleAnalytics.getInstance(ASolicitarNuevamente.this).reportActivityStop(this);
		}
	

}
