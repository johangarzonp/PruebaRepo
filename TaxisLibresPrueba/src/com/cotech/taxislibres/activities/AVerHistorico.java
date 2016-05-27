package com.cotech.taxislibres.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.database.Handler_sqlite;

public class AVerHistorico extends Activity {
	protected String APPNAME = "TaxisLibres";
	protected String module = C.VERHISTORICO;
	
	private TaxisLi appState;
	private Context context;
	
	private LinearLayout listaMensajes;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ventanahistorico);
		
		context = getApplicationContext();
		appState = ((TaxisLi) context);
		
		listaMensajes=(LinearLayout) findViewById(R.id.listaServicios);
		
			
		Handler_sqlite helper = new Handler_sqlite(this);
	    Log.i(APPNAME, module + ": Crea la base");
	    String vector[]=helper.leer();
	    int campos=0;
		int size=0;
		int contadorMsgs=0;
		
		while(campos < vector.length){
	    	if(vector[campos]!=null){
	    		campos++;
	    	}
	    	else{
	    		size=campos-1;
	    		campos=1000;
	    	}
	    }
	    Log.i(APPNAME, module + "La base tiene: "+ vector.length + "Registros");

	    while(size > -1){
	    	
	    	TextView text = new TextView(this);
	    	if(vector[size]!=null){
	    		//text.setText((contadorMsgs+1)+". "+ vector[size]);
	    		text.setText((size+1)+". "+ vector[size]);
	    	}else{
	    		text.setText(vector[size]);
	    	}
	    		text.setTextSize(14);
	    		text.setMaxWidth(listaMensajes.getWidth());
	    		//text.setMaxHeight(listaMensajes.getHeight());
	    		//text.setHighlightColor(Color.BLACK);
	    		//text.setHighlightColor(Color.BLACK);
	    		text.setTextColor(Color.BLACK);
		    	listaMensajes.addView(text);
	    	Log.i(APPNAME, module + ": Registro #"+contadorMsgs+"."+ vector[size]);
	    	size--;
	    	contadorMsgs++;
	    }
	    helper.cerrar();


	}
	
	//****************************************************************************************
	public void SalirHistorico(View v){
		String s = "SalirdelHistorico";

		Intent i = getIntent();
		i.putExtra("nohacernada", s);
		setResult(RESULT_OK, i);
		finish();

	}

	//****************************************************************************************
	public void BorrarHistorico(View v){
		Log.i(APPNAME, module + "Usuario Quiere Borrar el Historico...");
		Intent i = new Intent(AVerHistorico.this, ABorrarHistorico.class);
		startActivityForResult(i, 1);
	}
	
	//****************************************************************************************

		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			Log.i(APPNAME, module + "onActivityResult");
			super.onActivityResult(requestCode, resultCode, data);
			Log.i(APPNAME, module + "_____________________________ " + requestCode +"    " + resultCode + "   " + data);
			try{
				if(data.getExtras().containsKey("borrarHistorico")){

					Toast.makeText(context, "HISTORICO BORRADO CORRECTAMENTE", Toast.LENGTH_LONG).show();
					String s = "borrarHistorico";
					Intent i = getIntent();
					i.putExtra("borrarHistorico", s);
					setResult(RESULT_OK, i);
					finish();
				}
				else if(data.getExtras().containsKey("nohacernada")){
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
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
