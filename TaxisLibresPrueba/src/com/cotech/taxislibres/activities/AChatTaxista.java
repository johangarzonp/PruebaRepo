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
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AChatTaxista extends Activity {

	private static final String TAG = "TaxisLibres";
	protected String module = "AChatTaxista";

	private LinearLayout listaMensajes;
	//	private String mesajesPrueba="uno,dos,tres,cuatro,cinco,seis,siete,ocho,nueve,diez,once,doce,trece";
	private String mensajesPrueba="";
	private String [] vectorMensajes; 

	private EditText mensajeTaxista;

	private Context context;
	private TaxisLi appState;

	private IntentFilter filterChatTax;
	public MediaPlayer mp;
	//************************************************************************************************
	public final BroadcastReceiver receiverChatTax = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, Intent intent) {									
			Integer cmd = intent.getIntExtra("CMD", 0);
			
			Log.i(TAG, module + "Receiver en AChatTaxista");
			switch(cmd){
			case C.MSGTAXISTA:
				Sonido();
				Log.i(TAG, module + "Verificar si el mensaje tiene comas...");
				String cadena = appState.getMsgTaxista();
				cadena=cadena.replace(",","");
				appState.setMsgTaxista(cadena);


				String adicionarMensaje = appState.getChatTaxista()+"T|"+ appState.getMsgTaxista()+",";
				appState.setChatTaxista(adicionarMensaje);

				TextView text = new TextView(AChatTaxista.this);
				text.setBackgroundResource(R.drawable.cuadrochattaxista);
				text.setText(appState.getMsgTaxista());			
				text.setTextSize(14);
				//text.setMaxWidth(sendMsg.length());
				text.setMaxWidth(listaMensajes.getWidth());
				text.setGravity(Gravity.CENTER);
				text.setTextColor(Color.BLACK);
				listaMensajes.addView(text);

				break;
				
			case C.SONIDOALFRENTE:
				appState.setPushPendiente(true);
				appState.setMsgPushPendiente("{MF|El taxi de placas");
				finish();
				break;
			}
		}
	};
	//************************************************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_taxista);

		context = getApplicationContext();
		appState = ((TaxisLi) context);

		listaMensajes=(LinearLayout) findViewById(R.id.listamensajestaxista);
		mensajeTaxista= (EditText) findViewById(R.id.msgaltaxista);
		mensajeTaxista.setMaxWidth(mensajeTaxista.getWidth());
		
		mensajesPrueba=appState.getChatTaxista();

		if(!mensajesPrueba.equals("")){

			vectorMensajes= mensajesPrueba.split(",");
			Log.i(module,"Numero de Mensajes: "+ vectorMensajes.length);

			int cont=0;
			while(vectorMensajes.length > cont){

				String[] partesMensaje = vectorMensajes[cont].split("\\|");
				//La parte 0 contiene quien envio el mensaje, la parte 1 el mensaje...
				TextView text = new TextView(this);
				
				if(partesMensaje[0].contains("U"))	text.setBackgroundResource(R.drawable.cuadrochatusuario);
				else if(partesMensaje[0].contains("T"))	text.setBackgroundResource(R.drawable.cuadrochattaxista);
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

		filterChatTax = new IntentFilter();
		filterChatTax.addAction(module);
		registerReceiver(receiverChatTax, filterChatTax);
		appState.setActividad(module);
		
		new Handler().postDelayed(new Runnable(){
			public void run(){
				Log.i(TAG, module + " HABILITAR EL PRIMER PLANO POR LA SALIDA DEL MAPA");
				appState.setAppActiva(true); 	//App Activa
				appState.setAppPrimerPlano(true);	//App en Primer Plano
				
			};
		}, 1500);
	}

	public void Sonido(){
		mp= MediaPlayer.create(context, R.raw.sonido);
		mp.start();
	}
	//************************************************************************************************
	public void EnviarMsg(View v){

		String sendMsg = mensajeTaxista.getText().toString();

		if(sendMsg.equals("")){
			Toast.makeText(getApplicationContext(), "ESCRIBE TU MENSAJE", Toast.LENGTH_LONG).show();
		}else{
			
			try{
				Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
				t.setScreenName("Enviando Msg al Taxista");
				t.send(new HitBuilders.AppViewBuilder().build());
			}catch(Exception e){
				e.printStackTrace();
			}

			mensajeTaxista.setText(""); 	//Colocar Msg Vacio...
			
			Toast.makeText(context, "SE ENVIA EL MENSAJE... "  , Toast.LENGTH_LONG).show();
			String Mensaje_Taxista = "F|" + "1" +  "|" + sendMsg +"|" + appState.getNumberPhone() + "|" + appState.getNombreUsuario() 
					+"|"+ appState.getPinMovil() + "|"+appState.getPlaca() +  "|" +appState.getIdServicio() +  "|"+ appState.getDireccionServicio() + "\n";

			Intent msg = new Intent();
			msg.putExtra("CMD", C.SEND);
			msg.putExtra("DATA", Mensaje_Taxista);
			msg.setAction(C.COM);
			context.sendBroadcast(msg);
			
			String adicionarMensaje = appState.getChatTaxista()+"U|"+ sendMsg+",";
			appState.setChatTaxista(adicionarMensaje);
			
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
			GoogleAnalytics.getInstance(AChatTaxista.this).reportActivityStart(this);
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
			GoogleAnalytics.getInstance(AChatTaxista.this).reportActivityStop(this);
			Log.i(TAG, module + " Entra por onStop");
			appState.setAppPrimerPlano(false);	//App en Segundo Plano, BackGround...
		}


}
