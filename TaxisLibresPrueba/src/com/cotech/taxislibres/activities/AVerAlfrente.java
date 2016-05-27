package com.cotech.taxislibres.activities;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.activities.Log;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class AVerAlfrente extends Activity{

	protected String APPNAME = "TaxisLibres";
	protected String module = C.VERALFRENTE;

	private TaxisLi appState;
	private Context context;

	private TextView colocarPlacas;
	private TextView colocarHr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ventanaalfrente);

		context = getApplicationContext();
		appState = ((TaxisLi) context);

		colocarPlacas=(TextView) findViewById(R.id.textoplacasalfrente);
		colocarPlacas.setText(appState.getPlaca());

		colocarHr=(TextView) findViewById(R.id.textoclavealfrente);
		String numPhone=appState.getNumberPhone();
		String hr = numPhone.substring(numPhone.length()-2,numPhone.length());
		colocarHr.setText("Clave: " + hr);
		
		new Handler().postDelayed(new Runnable(){
			public void run(){
				Log.i(APPNAME, module + "Salir porque el usuario no presiono NADA...");
				String s = "SalioAlfrente";

				Intent i = getIntent();
				i.putExtra("salidaalfrente", s);
				setResult(RESULT_OK, i);
				finish();
			};
		}, 10000);
		
	}

	//****************************************************************************************
	public void ConfirmarAlfrente(View v){
		String s = "SalioAlfrente";

		Intent i = getIntent();
		i.putExtra("salidaalfrente", s);
		setResult(RESULT_OK, i);
		finish();

	}

	//****************************************************************************************
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "Paso x onDestroy");
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "Paso x onPause");
		super.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "Paso x onResume");
		super.onResume();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "Paso x onStart");
		super.onStart();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "Paso x onStop");
		super.onStop();
	}

	//****************************************************************************************
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "Paso x onKeyDown");
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Log.i(APPNAME, module + "Oprimio Atras");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}



}
