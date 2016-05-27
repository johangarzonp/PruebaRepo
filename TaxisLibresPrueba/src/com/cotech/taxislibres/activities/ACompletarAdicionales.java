package com.cotech.taxislibres.activities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.TaxisLi.TrackerName;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;











//import android.util.Log;
import com.cotech.taxislibres.activities.Log;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appstate.AppState;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ACompletarAdicionales extends Activity {

	protected String APPNAME = "TaxisLibres";
	protected String module = C.ADICIONALES;

	private TaxisLi appState;
	private Context context;
	private EditText editarDireccion;
	private EditText editarBarrio;
	private EditText editarAdicionales;
	private TextView barrioColonia;

	static final int READ_BLOCK_SIZE = 300;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datosadicionales);

		context = getApplicationContext();
		appState = ((TaxisLi) context);

		editarDireccion= (EditText) findViewById(R.id.boxeditardireccion);
		editarDireccion.setMaxWidth(editarDireccion.getWidth());

		editarBarrio= (EditText) findViewById(R.id.boxeditarbarrio);
		editarBarrio.setMaxWidth(editarBarrio.getWidth());

		editarAdicionales= (EditText) findViewById(R.id.boxeditarindicaciones);
		editarAdicionales.setMaxWidth(editarAdicionales.getWidth());

		barrioColonia=(TextView) findViewById(R.id.titulobarrioadicionales);

		appState.setGuardarFavorito(false);
		
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Usuario Ingresa a Completar Info Direccion");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}

		try{
			String pais = appState.getCiudadPais();
			if((pais.contains("MEXICO"))||(pais.contains("Mexico"))){
				barrioColonia.setBackgroundResource(R.drawable.titulocolonia);
			}

		}catch(Exception e){
			e.printStackTrace();

		}

		if(appState.getFlagrecordardireccion()==1){
			/******************PARA LEER LA DIRECCION************************************************/
			try{
				FileInputStream fis = openFileInput("textFile.txt");
				InputStreamReader isr = new InputStreamReader(fis);

				char[] inputBuffer = new char[READ_BLOCK_SIZE];
				String s = "";

				int charRead;
				while((charRead = isr.read(inputBuffer))>0){
					String readString = String.copyValueOf(inputBuffer, 0, charRead);
					s += readString;

					inputBuffer= new char[READ_BLOCK_SIZE];
				}

				Log.i(APPNAME, module +" Este es el Archivo: "+ s);

				editarDireccion.setText(appState.getDireccionUsuario());

				String[] separaDir=s.split("\\|");
				if(separaDir.length==2){
					editarBarrio.setText(separaDir[1]);
				}else{
					editarBarrio.setText(separaDir[1]);
					editarAdicionales.setText(separaDir[2]);
				}
				isr.close();

			}catch(IOException e){
				e.printStackTrace();
			}

		}else{
			//			 editarDireccion.setText(appState.getDireccionGoogle());
			//			 editarDireccion.setSelection(appState.getDireccionGoogle().length());
			editarDireccion.setText(appState.getDireccionUsuario());
			editarDireccion.setSelection(appState.getDireccionUsuario().length());
			editarBarrio.setText(appState.getBarrio());
		}
		appState.setAdicionalesServicio("");
	}

	//****************************************************************************************
	public void CancelarAdd(View v){
		String s = "No cambio Informacion";
		Intent i = getIntent();
		//i.putExtra("pedirTaxi", s);
		i.putExtra("cancela", s);
		setResult(RESULT_OK, i);
		finish();

	}
	//****************************************************************************************
	public void ConfirmarAdd(View v){
		if(editarDireccion.getText().toString().length()<10){
			Toast.makeText(context, "COMPLETE SU DIRECCION", Toast.LENGTH_LONG).show();
		}else{
			setContentView(R.layout.ventanaguardafavorito);
			Intent i = new Intent(ACompletarAdicionales.this, AGuardarFavoritos.class);
			startActivityForResult(i, 1);
		}
	}

	//****************************************************************************************
	public void MasOpciones(View v){
		Log.i("MAS OPCIONES", "Se debe mostrar cuadro de mas opciones...");
		Intent i = new Intent(ACompletarAdicionales.this, AMostrarAdicionales.class);
		startActivityForResult(i, 1);
	}
	//****************************************************************************************
	//public void AddFavoritos(View v){
	public void AddFavoritos(){
		Log.i(APPNAME, module + "Pasa x AddFavoritos");
		if(editarDireccion.getText().toString().length()<10){
			Toast.makeText(context, "COMPLETE SU DIRECCION", Toast.LENGTH_LONG).show();
		}else{
			appState.setGuardarFavorito(true);
			Log.i(APPNAME, module + "Se debe enviar a almacenar el favorito");
			String s = editarDireccion.getText().toString() +"|" + editarBarrio.getText().toString()+"|" + editarAdicionales.getText().toString()+"|";

			Intent i = getIntent();
			i.putExtra("completoDireccion", s);
			setResult(RESULT_OK, i);
			finish();
		}
	}

	//****************************************************************************************

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(APPNAME, module + "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(APPNAME, module + "_____________________________ " + requestCode +"    " + resultCode + "   " + data);
		try{
			if(data.getExtras().containsKey("guardarFavorito")){
				
				try{
					Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
					t.setScreenName("Usuario Guarda Favorito");
					t.send(new HitBuilders.AppViewBuilder().build());
				}catch(Exception e){
					e.printStackTrace();
				}
				
				Log.i("GUARDAR FAVORITOS", "Se debe guardar en Favoritos");
				AddFavoritos();
			}
			else if(data.getExtras().containsKey("NoGuardarFavorito")){
				
				try{
					Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
					t.setScreenName("Usuario No Guarda Favorito");
					t.send(new HitBuilders.AppViewBuilder().build());
				}catch(Exception e){
					e.printStackTrace();
				}
				
				Log.i("NO GUARDAR FAVORITOS", "No guardar favoritos");
				String s = editarDireccion.getText().toString() +"|" + editarBarrio.getText().toString()+"|" + editarAdicionales.getText().toString()+"|";

				Intent i = getIntent();
				i.putExtra("completoDireccion", s);
				setResult(RESULT_OK, i);
				finish();
			}else if(data.getExtras().containsKey("NoGuardarAdicionales")){
				Log.i("NO GUARDAR ADICIONALES", "No guardar Adicionales");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
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
		GoogleAnalytics.getInstance(ACompletarAdicionales.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(ACompletarAdicionales.this).reportActivityStop(this);
	}


}
