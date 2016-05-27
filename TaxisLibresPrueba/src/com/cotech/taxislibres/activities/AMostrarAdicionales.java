package com.cotech.taxislibres.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.TaxisLi.TrackerName;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AMostrarAdicionales extends Activity {
	protected String APPNAME = "TaxisLibres";
	protected String module = C.MOSTRARADICIONALES;
	
	private TaxisLi appState;
	private Context context;

	private ImageButton escogioAeropuerto;
	private ImageButton escogioFueraCiudad;
	private ImageButton escogioCarroGrande;
	private ImageButton escogioMascota;
	private ImageButton escogioServHoras;
	private ImageButton escogioAireAcondicionado;

	private boolean flagAeropuerto=false;
	private boolean flagFueraCiudad=false;
	private boolean flagCarroGrande=false;
	private boolean flagMascota=false;
	private boolean flagServHoras=false;
	private boolean flagAireAcond=false;
	private String adicionales="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mostraradicionales);
		
		context = getApplicationContext();
		appState = ((TaxisLi) context);

		escogioAeropuerto=(ImageButton) findViewById(R.id.opcionaeropuerto);
		escogioFueraCiudad=(ImageButton) findViewById(R.id.opcionfueraciudad);
		escogioCarroGrande=(ImageButton) findViewById(R.id.opcioncarrogrande);
		escogioMascota=(ImageButton) findViewById(R.id.opcionmascota);
		escogioServHoras=(ImageButton) findViewById(R.id.opcionhoras);
		escogioAireAcondicionado=(ImageButton) findViewById(R.id.opcionaire);

	}

	//****************************************************************************************
	public void ConfirmarAdicionales(View v){
		if(flagAeropuerto)	adicionales=adicionales+" "+"Servicio al Aeropuerto";
		if(flagFueraCiudad)	adicionales=adicionales+" "+"Servicio fuera de la Ciudad";
		if(flagCarroGrande)	adicionales=adicionales+" "+"Carro Grande";
		if(flagMascota)	adicionales=adicionales+" "+"Con Mascota";
		if(flagServHoras)	adicionales=adicionales+" "+"Servicio por Horas";
		if(flagAireAcond)	adicionales=adicionales+" "+"Con Aire Acondicionado";
		appState.setAdicionalesServicio(adicionales);
		
		EnviaInfoAnalytics("Usuario confirma Adicionales a su servicio");
		
		String s = adicionales;
		Intent i = getIntent();
		i.putExtra("guardarAdicionales",s);
		setResult(RESULT_OK, i);
		finish();
	}

	//****************************************************************************************
	public void CerrarAdicionales(View v){
		
		EnviaInfoAnalytics("Usuario Decide no confirmar Adicionales a su servicio");
		
		String s = "NoAdicionales";
		Intent i = getIntent();
		i.putExtra("NoGuardarAdicionales", s);
		setResult(RESULT_OK, i);
		finish();
	}

	//****************************************************************************************

	public void SeleccionoAeropuerto(View v){
		if(flagAeropuerto){
			flagAeropuerto=false;
			escogioAeropuerto.setImageResource(R.drawable.servicioaeropuerto);
			EnviaInfoAnalytics("Decidio no solicitar Servicio al Aeropuerto");
		}else{
			flagAeropuerto=true;
			escogioAeropuerto.setImageResource(R.drawable.servicioaeropuertoseleccionado);
			EnviaInfoAnalytics("Selecciono Servicio al Aeropuerto");
		}
	}
	
	public void SeleccionoFueraCiudad(View v){
		if(flagFueraCiudad){
			flagFueraCiudad=false;
			escogioFueraCiudad.setImageResource(R.drawable.fueradelaciudad);
			EnviaInfoAnalytics("Decidio no ir Fuera de la ciudad");
		}else{
			flagFueraCiudad=true;
			escogioFueraCiudad.setImageResource(R.drawable.fueradelaciudadseleccionado);
			EnviaInfoAnalytics("Selecciono Servicio Fuera de la ciudad");
		}
	}
	
	public void SeleccionoCarroGrande(View v){
		if(flagCarroGrande){
			flagCarroGrande=false;
			escogioCarroGrande.setImageResource(R.drawable.carrogrande);
			EnviaInfoAnalytics("Decidio no pedir Carro Grande");
		}else{
			flagCarroGrande=true;
			escogioCarroGrande.setImageResource(R.drawable.carrograndeseleccionado);
			EnviaInfoAnalytics("Selecciono Carro Grande");
		}
	}
	
	public void SeleccionoMascota(View v){
		if(flagMascota){
			flagMascota=false;
			escogioMascota.setImageResource(R.drawable.mascota);
			EnviaInfoAnalytics("Decidio no llevar Mascota");
		}else{
			flagMascota=true;
			escogioMascota.setImageResource(R.drawable.mascotaseleccionado);
			EnviaInfoAnalytics("Selecciono Mascota");
		}
	}
	
	public void SeleccionoPorHoras(View v){
		if(flagServHoras){
			flagServHoras=false;
			escogioServHoras.setImageResource(R.drawable.servicioporhoras);
			EnviaInfoAnalytics("Decidio no solicitar Servicio x Horas");
		}else{
			flagServHoras=true;
			escogioServHoras.setImageResource(R.drawable.servicioporhorasseleccionado);
			EnviaInfoAnalytics("Selecciono Servicio x Horas");
		}
		
	}
	public void SeleccionoAire(View v){
		if(flagAireAcond){
			flagAireAcond=false;
			escogioAireAcondicionado.setImageResource(R.drawable.aireacondicionado);
			EnviaInfoAnalytics("Decidio no pedir Aire Acondicionado");
		}else{
			flagAireAcond=true;
			escogioAireAcondicionado.setImageResource(R.drawable.aireacondicionadoseleccionado);
			EnviaInfoAnalytics("Selecciono Aire Acondicionado");
		}	
		
	}

	/************************************************************************************************/
	public void EnviaInfoAnalytics(String notificacion){
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(notificacion);
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
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
		GoogleAnalytics.getInstance(AMostrarAdicionales.this).reportActivityStart(this);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(AMostrarAdicionales.this).reportActivityStop(this);
	}


}