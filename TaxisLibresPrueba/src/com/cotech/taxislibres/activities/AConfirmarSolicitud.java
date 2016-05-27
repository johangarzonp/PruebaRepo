package com.cotech.taxislibres.activities;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.TaxisLi.TrackerName;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AConfirmarSolicitud extends Activity {

	protected String APPNAME = "TaxisLibres";
	protected String module = C.CONFSOLICITUD;

	private TaxisLi appState;
	private Context context;

	private TextView colocarDireccion;
	private TextView colocarTipopago;
	private TextView textoConfirma;

	private int valorPropina=0;
	private EditText boxPropina;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ventanaconfirmaservicio);

		context = getApplicationContext();
		appState = ((TaxisLi) context);

		textoConfirma=(TextView) findViewById(R.id.avisoconfirmarservicio);

		colocarDireccion=(TextView) findViewById(R.id.espaciodireccion);
		//colocarDireccion.setMaxWidth(textoConfirma.getWidth());	//Del mismo tamaño del aviso superior
		colocarTipopago=(TextView) findViewById(R.id.avisotipodepago);

		String formaPago="Efectivo";
		if((appState.getFormaPago()==1))	formaPago="Vale Digital";
		else if((appState.getFormaPago()==3))	formaPago="Vale Fisico";
		else if(appState.getFormaPago()==4)	formaPago="Tarjeta de Crédito";

		appState.setEscogioPago(true);

		colocarTipopago.setText(formaPago);
		colocarDireccion.setText(appState.getDireccionUsuario());
		try{
			colocarDireccion.setGravity(Gravity.CENTER);
			colocarDireccion.setTextAlignment(Gravity.CENTER);
		}catch (Exception e){
			e.printStackTrace();
		}

		boxPropina=(EditText) findViewById(R.id.cajapropina);
		boxPropina.setText("$0");
		appState.setIncentivoTaxista("0");
	}

	//****************************************************************************************
	public void ConfirmarSolicitud(View v){
		//String s = "Solicitar el Servicio";
		
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Presiono Boton Confirmar Solicitud de Servicio");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		appState.setIncentivoTaxista(String.valueOf(valorPropina));
		String s = String.valueOf(valorPropina);
		Intent i = getIntent();
		i.putExtra("confirmarsolicitud", s);
		setResult(RESULT_OK, i);
		finish();

	}

	//****************************************************************************************
	public void CancelarSolicitud(View v){
		String s = "No se solicita el servicio";

		Intent i = getIntent();
		i.putExtra("cancela", s);
		setResult(RESULT_OK, i);
		finish();

	}

	//*******************************************************************************************
	//Para sumar a la propina
	public void Sumar(View v){
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Presiono Sumar Propina");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
		Log.i(APPNAME, module+" Entro a Sumar");
		String pais2 = appState.getCiudadPais();
		if((pais2.contains("MEXICO"))||(pais2.contains("Mexico"))){
			if(valorPropina>49)	valorPropina=50;
			else valorPropina+=5;
		}
		else if(pais2.contains("COLOMBIA")){
			if(valorPropina>9999)	valorPropina=10000;
			else valorPropina+=1000;
		}
		else{//Es panama
			if(valorPropina>9)	valorPropina=10;
			else valorPropina+=1;
		}
		String cadena;
		cadena= "$"+String.valueOf(valorPropina);
		boxPropina.setText(cadena);
	}

	//*******************************************************************************************
	//Para restar a la propina
	public void Restar(View v){
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Presiono Restar Propina");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
		Log.i(APPNAME, module+" Entro a Restar");
		String pais2 = appState.getCiudadPais();
		if((pais2.contains("MEXICO"))||(pais2.contains("Mexico"))){
			if(valorPropina<1)	valorPropina=0;
			else valorPropina-=5;
		}
		else if(pais2.contains("COLOMBIA")){
			if(valorPropina<1)	valorPropina=0;
			else valorPropina-=1000;
		}
		else{//Es panama
			if(valorPropina<1)	valorPropina=0;
			else valorPropina-=1;
		}
		String cadena;
		cadena= "$"+String.valueOf(valorPropina);
		boxPropina.setText(cadena);

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
		GoogleAnalytics.getInstance(AConfirmarSolicitud.this).reportActivityStart(this);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(AConfirmarSolicitud.this).reportActivityStop(this);
	}

}
