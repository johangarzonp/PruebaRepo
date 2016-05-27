package com.cotech.taxislibres.activities;

import java.io.IOException;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.TaxisLi.TrackerName;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridLayout.Alignment;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cotech.taxislibres.activities.Log;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ALlegoTaxi extends Activity {

	protected String APPNAME = "TaxisLibres";
	protected String module = C.LLEGOTAXI;

	private TaxisLi appState;
	private Context context;

	private IntentFilter filterLlegoTaxi;

	private TextView colocarPlacas;
	private TextView colocarNombreConductor;
	private TextView colocarDistancia;
	private ImageView fotoTaxista;

	private Bitmap imagenRecibida;
	private byte[] imagenBytes;
	private boolean llegoImagen=false;

	public final BroadcastReceiver receiverLlegoTaxi = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, Intent intent) {	
			Log.i(APPNAME, module + "Paso x onReceive");
			Integer cmd = intent.getIntExtra("CMD", 0);
			switch(cmd){
			case C.NORECIBIOIMAGEN:
				//imagenRecibida=BitmapFactory.decodeResource(getResources(), R.drawable.sin_foto);
				Log.i(APPNAME, module+"********** NO RECIBIO  IMAGEN ********");
				Toast.makeText(context, "LA FOTO  NO HA SIDO CARGADA", Toast.LENGTH_LONG).show();
				ColocarFoto();
				break;

			case C.RECIBIOIMAGEN:
				llegoImagen=true;
				byte[] data = intent.getByteArrayExtra("EXTRA");
				imagenBytes = intent.getByteArrayExtra("EXTRA");
				imagenRecibida=BitmapFactory.decodeByteArray(data,0,data.length);
				Log.i(APPNAME, module+"**********RECIBIO  IMAGEN ********");
				//Toast.makeText(context, "FOTO CARGADA", Toast.LENGTH_LONG).show();
				ColocarFoto();
				break;

			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "Paso x onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mostrartaxiasignado);

		context = getApplicationContext();
		appState = ((TaxisLi) context);

		filterLlegoTaxi = new IntentFilter();
		filterLlegoTaxi.addAction(module);

		colocarPlacas=(TextView) findViewById(R.id.infoplacataxi);
		colocarPlacas.setText("Placas: "+appState.getPlaca());
		try{
			colocarNombreConductor=(TextView) findViewById(R.id.infonombreconductor);
			colocarNombreConductor.setText(appState.getNombreTaxista());
			colocarNombreConductor.setGravity(Gravity.CENTER);
			colocarNombreConductor.setTextAlignment(Gravity.CENTER);

			colocarDistancia=(TextView) findViewById(R.id.avisodistancia);
			colocarDistancia.setText("DISTANCIA: "+appState.getMetros()+" MTS");
			colocarDistancia.setGravity(Gravity.CENTER);
			colocarDistancia.setTextAlignment(Gravity.CENTER);


			fotoTaxista=(ImageView) findViewById(R.id.fototaxista);
			fotoTaxista.setVisibility(View.INVISIBLE);
			imagenRecibida=BitmapFactory.decodeResource(getResources(), R.drawable.sin_foto);

			registerReceiver(receiverLlegoTaxi, filterLlegoTaxi);

			appState.setActividad(module);

			String informacion = "I|"+ appState.getIdServicio()+"\n";
			Intent solimage = new Intent();
			solimage.putExtra("CMD", C.SEND);
			solimage.putExtra("DATA", informacion);
			solimage.setAction(C.COM);
			context.sendBroadcast(solimage);
			Log.i(APPNAME, module+"**********SOLICITAR IMAGEN********"+ informacion);

			Toast.makeText(context, "CARGANDO FOTO DEL TAXISTA...", Toast.LENGTH_LONG).show();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Muestra Iformacion del Servicio,Asigno Taxi");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		new Handler().postDelayed(new Runnable(){
			public void run(){
				Log.i(APPNAME, module + "Salida x tiempo no presiono Aceptar");
				Intent i = getIntent();
				if(llegoImagen)	i.putExtra("llegoimagen", imagenBytes);
				else	i.putExtra("noimagen", "Nada");
				setResult(RESULT_OK, i);
				finish();
			};
		}, 20000);
	}

	//****************************************************************************************
	public void CerrarInfoTaxi(View v){

		Intent i = getIntent();
		if(llegoImagen)	i.putExtra("llegoimagen", imagenBytes);
		else	i.putExtra("noimagen", "Nada");
		setResult(RESULT_OK, i);
		finish();
	}

	//****************************************************************************************
	public void AceptaTaxi(View v){

		Intent i = getIntent();
		if(llegoImagen)	i.putExtra("llegoimagen", imagenBytes);
		else	i.putExtra("noimagen", "Nada");
		setResult(RESULT_OK, i);
		finish();
	}

	//****************************************************************************************
	public void ColocarFoto(){
		Bitmap imagenMostrar;
		try{
			Log.i(APPNAME, module + "DENSIDAD TOMADA PARA LA FOTO: "+appState.getDensidad());
			if(appState.getDensidad() > 639){	//640
				//imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*8), (int)(imagenRecibida.getHeight()*8), true);
				imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*6), (int)(imagenRecibida.getHeight()*6), true);
			}else if(appState.getDensidad() > 479){	//480
				//imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*6), (int)(imagenRecibida.getHeight()*6), true);
				imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*4), (int)(imagenRecibida.getHeight()*4), true);
			}else if(appState.getDensidad() > 319){	//320
				//imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*4), (int)(imagenRecibida.getHeight()*4), true);
				imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*3), (int)(imagenRecibida.getHeight()*3), true);
			}else if(appState.getDensidad() > 239){	//240
				//imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*3), (int)(imagenRecibida.getHeight()*3), true);
				imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*2), (int)(imagenRecibida.getHeight()*2), true);
			}else{	//160
				//imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*2), (int)(imagenRecibida.getHeight()*2), true);
				imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*1.5), (int)(imagenRecibida.getHeight()*1.5), true);
			}
			fotoTaxista.setVisibility(View.VISIBLE);
			fotoTaxista.setImageBitmap(imagenMostrar);
		}catch (Exception ex) {
			Toast.makeText(context, "NO FUE POSIBLE CARGAR LA FOTO DEL TAXISTA...", Toast.LENGTH_LONG).show();
        }
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
		super.onPause();
		Log.i(APPNAME, module + " Entra por onPause");
		unregisterReceiver(receiverLlegoTaxi);
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
		GoogleAnalytics.getInstance(ALlegoTaxi.this).reportActivityStart(this);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "Paso x onStop");
		super.onStop();
		GoogleAnalytics.getInstance(ALlegoTaxi.this).reportActivityStop(this);
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
