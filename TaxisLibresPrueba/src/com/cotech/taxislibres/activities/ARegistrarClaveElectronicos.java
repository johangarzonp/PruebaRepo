package com.cotech.taxislibres.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;

public class ARegistrarClaveElectronicos extends Activity {
	protected String APPNAME = "TaxisLibres";
	protected String module = C.REGISTROCLAVEELECTRONICOS;
	
	private EditText claveElectronicos;
	private TextView tituloPrimerRenglon;
	private boolean ingresoPrimeraClave=false;
	private String clave1="";
	private String clave2="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingresarclaveelectronicos);
		
		claveElectronicos=(EditText) findViewById(R.id.boxclaveelectronicos);
		claveElectronicos.setMaxWidth(claveElectronicos.getWidth());
		
		tituloPrimerRenglon=(TextView) findViewById(R.id.renglon1claveelectronicos);
		tituloPrimerRenglon.setText("REGISTRAR LA CLAVE PARA");
		
	}
	
	//****************************************************************************************
	public void EnviarClave(View v){
		if(ingresoPrimeraClave){	//Hay que comparar las 2 claves
			clave2=claveElectronicos.getText().toString();
			if(clave2.equals("")){
				Toast.makeText(getApplicationContext(), "CONFIRME SU CLAVE POR FAVOR", Toast.LENGTH_LONG).show();
			}else{
				if(clave1.equals(clave2)){	//Si las claves son iguales se enviar para registrarla
					Intent i = getIntent();
					i.putExtra("registrarClaveElectronicos", clave1);
					setResult(RESULT_OK, i);
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "LA CLAVE NO COINCIDE,REGISTRE NUEVAMENTE LA CLAVE", 1000).show();
					ingresoPrimeraClave=false;
					tituloPrimerRenglon.setText("REGISTRAR LA CLAVE PARA");
					claveElectronicos.setText("");
				}
			}
			
		}else{	//Se lee clave inicial para pedir confirmación de Clave
			clave1=claveElectronicos.getText().toString();
			if(clave1.equals("")){
				Toast.makeText(getApplicationContext(), "INGRESE SU CLAVE POR FAVOR", Toast.LENGTH_LONG).show();
			}else{
				ingresoPrimeraClave=true;
				tituloPrimerRenglon.setText("CONFIRMAR LA CLAVE PARA");
				claveElectronicos.setText("");
			}
			
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
