package com.cotech.taxislibres.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;

public class APedirClaveElectronicos extends Activity {
	protected String APPNAME = "TaxisLibres";
	protected String module = C.CLAVEELECTRONICOS;
	
	private EditText claveElectronicos;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingresarclaveelectronicos);
		
		claveElectronicos=(EditText) findViewById(R.id.boxclaveelectronicos);
		claveElectronicos.setMaxWidth(claveElectronicos.getWidth());
		
	}
	
	//****************************************************************************************
	public void EnviarClave(View v){
		String s = claveElectronicos.getText().toString();
		
		if(s.equals("")){
			Toast.makeText(getApplicationContext(), "INGRESE SU CLAVE POR FAVOR", Toast.LENGTH_LONG).show();
		}else{

			Intent i = getIntent();
			i.putExtra("enviarClaveElectronicos", s);
			setResult(RESULT_OK, i);
			finish();
		}

	}
	
	//****************************************************************************************
		public void CerrarVentanaClave(View v){
			String s = "NoenviarClave";

			Intent i = getIntent();
			i.putExtra("cancela", s);
			setResult(RESULT_OK, i);
			finish();

		}
	
	//****************************************************************************************
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			Log.i(APPNAME, module + "onKeyDown");
			switch(keyCode){
			case KeyEvent.KEYCODE_BACK:
				
			break;	

			}
			return true;
		}
	
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
