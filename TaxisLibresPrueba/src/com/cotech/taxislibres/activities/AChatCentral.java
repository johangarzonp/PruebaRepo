package com.cotech.taxislibres.activities;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.TaxisLi.TrackerName;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AChatCentral extends Activity {

	private static final String TAG = "TaxisLibres";
	protected String module = "AChatCentral";
	
	private LinearLayout listaMensajes;
	//	
	private String mensajesPrueba="";
	private String [] vectorMensajes; 

	private EditText mensajeCentral;

	private Context context;
	private TaxisLi appState;

	private IntentFilter filterChatCentral;
	//************************************************************************************************
	public final BroadcastReceiver receiverChatCentral = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, Intent intent) {									
			Integer cmd = intent.getIntExtra("CMD", 0);
			
			switch(cmd){
			case C.MSGCONTACT:
				//OJO Hay que traer el mensaje
				Log.i(TAG, module + "Verificar si el mensaje tiene comas...");
				String cadena = appState.getMensajeCentral();
				cadena=cadena.replace(",","");
				appState.setMensajeCentral(cadena);
				
				
				String adicionarMensaje = appState.getChatCentral()+"C|"+ appState.getMensajeCentral()+",";
				appState.setChatCentral(adicionarMensaje);
				
				TextView text = new TextView(AChatCentral.this);
				text.setBackgroundResource(R.drawable.cuadrochatcontact);
				text.setText(appState.getMensajeCentral());			
				text.setTextSize(14);
				//text.setMaxWidth(sendMsg.length());
				text.setMaxWidth(listaMensajes.getWidth());
				text.setGravity(Gravity.CENTER);
				text.setTextColor(Color.BLACK);
				listaMensajes.addView(text);
				break;
			}
		}
	};
	//************************************************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_central);

		context = getApplicationContext();
		appState = ((TaxisLi) context);

		listaMensajes=(LinearLayout) findViewById(R.id.listamensajescentral);
		mensajeCentral= (EditText) findViewById(R.id.msgalacentral);
		mensajeCentral.setMaxWidth(mensajeCentral.getWidth());
		
		mensajesPrueba=appState.getChatCentral();

		if(!mensajesPrueba.equals("")){

			vectorMensajes= mensajesPrueba.split(",");
			Log.i(module,"Numero de Mensajes: "+ vectorMensajes.length);

			int cont=0;
			while(vectorMensajes.length > cont){

				String[] partesMensaje = vectorMensajes[cont].split("\\|");
				//La parte 0 contiene quien envio el mensaje, la parte 1 el mensaje...
				TextView text = new TextView(this);
				
				if(partesMensaje[0].contains("U"))	text.setBackgroundResource(R.drawable.cuadrochatusuario);
				else if(partesMensaje[0].contains("C"))	text.setBackgroundResource(R.drawable.cuadrochatcontact);
				else text.setBackgroundResource(R.drawable.cuadroenviarmsgs);
				
				text.setText(partesMensaje[1]);			
				text.setTextSize(14);
				//text.setMaxWidth(partesMensaje[1].length());
				text.setMaxWidth(listaMensajes.getWidth());
				text.setGravity(Gravity.CENTER);
				text.setTextColor(Color.BLACK);
				listaMensajes.addView(text);

				Log.i(TAG, module + ": Registro #"+cont+"."+ vectorMensajes[cont]);
				cont++;
			}
		}

		filterChatCentral = new IntentFilter();
		filterChatCentral.addAction(module);
		registerReceiver(receiverChatCentral, filterChatCentral);
		appState.setActividad(module);
		
		new Handler().postDelayed(new Runnable(){
			public void run(){
				Log.i(TAG, module + " HABILITAR EL PRIMER PLANO POR LA SALIDA DEL MAPA");
				appState.setAppActiva(true); 	//App Activa
				appState.setAppPrimerPlano(true);	//App en Primer Plano
				
			};
		}, 1500);
	}

	//************************************************************************************************
	public void EnviarMsgCentral(View v){

		String sendMsg = mensajeCentral.getText().toString();

		if(sendMsg.equals("")){
			Toast.makeText(getApplicationContext(), "ESCRIBE TU MENSAJE", Toast.LENGTH_LONG).show();
		}else{

			try{
				Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
				t.setScreenName("Enviando Msg a la Central");
				t.send(new HitBuilders.AppViewBuilder().build());
			}catch(Exception e){
				e.printStackTrace();
			}
			
			mensajeCentral.setText(""); 	//Colocar Msg Vacio...
			
			Toast.makeText(context, "SE ENVIA EL MENSAJE... "  , Toast.LENGTH_LONG).show();
			String Mensaje_Taxista = "F|" + "0" +  "|" + sendMsg +"|" + appState.getNumberPhone() + "|" + appState.getNombreUsuario() 
					+"|"+ appState.getPinMovil() + "|"+appState.getPlaca() +  "|" +appState.getIdServicio() +  "|"+ appState.getDireccionServicio() + "\n";

			Intent msg = new Intent();
			msg.putExtra("CMD", C.SEND);
			msg.putExtra("DATA", Mensaje_Taxista);
			msg.setAction(C.COM);
			context.sendBroadcast(msg);
			
			String adicionarMensaje = appState.getChatCentral()+"U|"+ sendMsg+",";
			appState.setChatCentral(adicionarMensaje);
			
			TextView text = new TextView(this);
			text.setBackgroundResource(R.drawable.cuadrochatusuario);
			text.setText(sendMsg);			
			text.setTextSize(14);
			//text.setMaxWidth(sendMsg.length());
			text.setMaxWidth(listaMensajes.getWidth());
			text.setGravity(Gravity.CENTER);
			text.setTextColor(Color.BLACK);
			listaMensajes.addView(text);
			
		}

	}
	//************************************************************
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
				GoogleAnalytics.getInstance(AChatCentral.this).reportActivityStart(this);
				Log.i(TAG, module + " Entra por onStart");
				appState.setAppActiva(true); 	//App Activa
				appState.setAppPrimerPlano(true);	//App en Primer Plano
				if(appState.isPushPendiente()){
					finish();
				}
			}

			@Override
			protected void onStop() {
				// TODO Auto-generated method stub
				super.onStop();
				GoogleAnalytics.getInstance(AChatCentral.this).reportActivityStop(this);
				Log.i(TAG, module + " Entra por onStop");
				appState.setAppPrimerPlano(false);	//App en Segundo Plano, BackGround...
			}

}
