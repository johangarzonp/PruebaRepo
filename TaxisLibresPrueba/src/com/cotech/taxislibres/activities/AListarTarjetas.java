package com.cotech.taxislibres.activities;

import java.util.ArrayList;
import java.util.List;








import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.TaxisLi.TrackerName;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;




import android.os.Handler;






//import android.util.Log;
import com.cotech.taxislibres.activities.Log;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AListarTarjetas extends Activity {
	
	
	protected String APPNAME = "TaxisLibres";
	protected String module = C.LISTAR_TARJETAS;
		
	private TaxisLi appState;
	private int cont;
	
	private IntentFilter filter6;
	private String tokenTarjeta="";
	private ProgressDialog progDailog6 = null;
	
	private LinearLayout informacionTarjeta1;
	private TextView nombreTarjeta1;
	private TextView numeroTarjeta1;
	
	private LinearLayout informacionTarjeta2;
	private TextView nombreTarjeta2;
	private TextView numeroTarjeta2;
	
	private LinearLayout informacionTarjeta3;
	private TextView nombreTarjeta3;
	private TextView numeroTarjeta3;
	
	private LinearLayout barraSoloAdicionar;
	private LinearLayout barraAdicionarBorrar;
	
	private String numeroTarjetaSeleccionada="";
	
	private RadioButton selectorTarjeta1;
	private RadioButton selectorTarjeta2;
	private RadioButton selectorTarjeta3;
	
	public final BroadcastReceiver receiver6 = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {									
			Integer cmd = intent.getIntExtra("CMD", 0);
			String escogioTC= intent.getStringExtra("DATA");
			switch(cmd){
			case C.BORRAR_TARJETA:
				Log.i(APPNAME, module + "SOLICITO BORRAR TARJETA");
				String numTC="";
				Log.i(APPNAME, module + "ESCOGIO TC: "+escogioTC);
				if(escogioTC.contains("1")){
					Log.i(APPNAME, module + "FUE LA TC 1");
					numTC=InfoTarjeta(appState.getInfoTarjeta1(),4);
					tokenTarjeta=InfoTarjeta(appState.getInfoTarjeta1(),0);
				}
				else if(escogioTC.contains("2")){
					Log.i(APPNAME, module + "FUE LA TC 2");
					numTC=InfoTarjeta(appState.getInfoTarjeta2(),4);
					tokenTarjeta=InfoTarjeta(appState.getInfoTarjeta2(),0);
				}
				else if(escogioTC.contains("3")){
					Log.i(APPNAME, module + "FUE LA TC 3");
					numTC=InfoTarjeta(appState.getInfoTarjeta3(),4);
					tokenTarjeta=InfoTarjeta(appState.getInfoTarjeta3(),0);
				}
					

				AlertDialog.Builder builder = new AlertDialog.Builder(AListarTarjetas.this);
				builder.setTitle(" BORRAR TARJETA ");
				builder.setMessage("¿Confirma que desea borrar la tarjeta de credito "+numTC+"?");
				builder.setPositiveButton("Aceptar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i(APPNAME, module + "Si quiere borrar la tarjeta de credito.");	
						dialog.cancel();
						//String borrarToken = "{\"language\": \"es\", \"command\": \"REMOVE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"removeCreditCardToken\": {\"payerId\": \"10\", \"creditCardTokenId\": \"1d1767e0-44c4-435b-b20f-7f57e5958df3\"}}";
                        //String borrarToken = "{\"language\": \"es\", \"command\": \"REMOVE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"removeCreditCardToken\": {\"payerId\": "+appState.getPayerIdTc()+", \"creditCardTokenId\": \"f11aae7c-2f39-4c6b-8446-5ebe9027a28b\"}}";
                        
						
						//String borrarToken = "{\"language\": \"es\", \"command\": \"REMOVE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"removeCreditCardToken\": {\"payerId\": "+appState.getPayerIdTc()+", \"creditCardTokenId\": \""+tokenTarjeta+"\"}}";
						String colocaPais="";
						if(appState.getCiudadPais().contains("COLOMBIA"))	colocaPais="CO";
						else if(appState.getCiudadPais().contains("MEXICO"))	colocaPais="MX";
						else colocaPais="PA";
						String borrarToken = "eliminarTarjeta|"+colocaPais+"|"+appState.getCorreoUsuario()+"|"+tokenTarjeta;
                        
                        //appState.setComandoPayu("REMOVE_TOKEN");
                        appState.setComandoPayu("ELIMINAR_TARJETA");

            			Intent borrarTarjeta = new Intent();
            			borrarTarjeta.putExtra("CMD", C.ENCRIPTADO);
            			borrarTarjeta.putExtra("DATA", borrarToken);
            			borrarTarjeta.setAction(C.COM);
            			getApplicationContext().sendBroadcast(borrarTarjeta);
            			
            			progDailog6 = new ProgressDialog(AListarTarjetas.this);
            	   		progDailog6.setTitle("BORRANDO TARJETA DE CREDITO");
            	   		progDailog6.setMessage("ESPERE UN MOMENTO... ");
            	   		progDailog6.setCancelable(true);
            	   		progDailog6.show();
					}
				});
	    
				builder.setNegativeButton("Cancelar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i(APPNAME, module + "No Quiere borrar la tarjeta de credito.");
						dialog.cancel();
					}
				});
	 
				builder.show();
			break;
			
			case C.BORRADO_OK:
				progDailog6.dismiss();
				Toast.makeText(getApplicationContext(), "TARJETA BORRADA CORRECTAMENTE", Toast.LENGTH_LONG).show();
				finish();
			break;
			
			case C.BORRADO_ERROR:
				progDailog6.dismiss();
				Toast.makeText(getApplicationContext(), "ERROR AL BORRAR LA TARJETA", Toast.LENGTH_LONG).show();
			break;
			
			case C.ERROR_SOCKET:
				progDailog6.dismiss();
				Toast.makeText(getApplicationContext(), "ERROR EN LA COMUNICACION, INTENTE NUEVAMENTE", 5000).show();
			break;
			
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listar_tarjetas_credito);
		
		final Context context = getApplicationContext();
		appState = ((TaxisLi) context);
		
		informacionTarjeta1=(LinearLayout) findViewById(R.id.infotarjeta1);
		informacionTarjeta1.setVisibility(View.INVISIBLE);
		
		informacionTarjeta2=(LinearLayout) findViewById(R.id.infotarjeta2);
		informacionTarjeta2.setVisibility(View.INVISIBLE);
		
		informacionTarjeta3=(LinearLayout) findViewById(R.id.infotarjeta3);
		informacionTarjeta3.setVisibility(View.INVISIBLE);
		
		if(appState.isBorrarTarjeta()){
			barraSoloAdicionar=(LinearLayout) findViewById(R.id.layoutadicionar);
			barraSoloAdicionar.setVisibility(View.INVISIBLE);
			barraAdicionarBorrar=(LinearLayout) findViewById(R.id.layoutborraradicionar);
			barraAdicionarBorrar.setVisibility(View.VISIBLE);
		}
		
		selectorTarjeta1= (RadioButton) findViewById(R.id.radioButtonTarjeta1);
		selectorTarjeta1.setVisibility(View.INVISIBLE);
		
		selectorTarjeta2= (RadioButton) findViewById(R.id.radioButtonTarjeta2);
		selectorTarjeta2.setVisibility(View.INVISIBLE);
		
		selectorTarjeta3= (RadioButton) findViewById(R.id.radioButtonTarjeta3);
		selectorTarjeta3.setVisibility(View.INVISIBLE);
		
		//numTarjetas=appState.getNumTarjetas();	//Se carga el # de tarjetas
		Log.i(APPNAME, module + "hay tarjetas:"+ appState.getNumTarjetas());
		for(cont=0;cont< appState.getNumTarjetas();cont++){
			Log.i(APPNAME, module + "ENTRO AL FOR");
						switch(cont){
							
							case(0):
								informacionTarjeta1.setVisibility(View.VISIBLE);
								nombreTarjeta1=(TextView) findViewById(R.id.nombretarjeta1);
								numeroTarjeta1=(TextView) findViewById(R.id.textonombretarjeta1);
								selectorTarjeta1.setVisibility(View.VISIBLE);
								if(InfoTarjeta(appState.getInfoTarjeta1(),3).contains("VISA")){
									informacionTarjeta1.setBackgroundResource(R.drawable.fondoinfovisa);
									nombreTarjeta1.setText(InfoTarjeta(appState.getInfoTarjeta1(),1));
									numeroTarjeta1.setText(InfoTarjeta(appState.getInfoTarjeta1(),4));
									
								}else if(InfoTarjeta(appState.getInfoTarjeta1(),3).contains("MASTER")){
									informacionTarjeta1.setBackgroundResource(R.drawable.fondoinfomaster);
									nombreTarjeta1.setText(InfoTarjeta(appState.getInfoTarjeta1(),1));
									numeroTarjeta1.setText(InfoTarjeta(appState.getInfoTarjeta1(),4));
									
								}else if(InfoTarjeta(appState.getInfoTarjeta1(),3).contains("AMEX")){
									informacionTarjeta1.setBackgroundResource(R.drawable.fondoinfoamerican);
									nombreTarjeta1.setText(InfoTarjeta(appState.getInfoTarjeta1(),1));
									numeroTarjeta1.setText(InfoTarjeta(appState.getInfoTarjeta1(),4));
									
								}else if(InfoTarjeta(appState.getInfoTarjeta1(),3).contains("DINERS")){
									informacionTarjeta1.setBackgroundResource(R.drawable.fondoinfodiners);
									nombreTarjeta1.setText(InfoTarjeta(appState.getInfoTarjeta1(),1));
									numeroTarjeta1.setText(InfoTarjeta(appState.getInfoTarjeta1(),4));
								}
								
							break;
							
							case(1):
								informacionTarjeta2.setVisibility(View.VISIBLE);
								nombreTarjeta2=(TextView) findViewById(R.id.nombretarjeta2);
								numeroTarjeta2=(TextView) findViewById(R.id.textonombretarjeta2);
								selectorTarjeta2.setVisibility(View.VISIBLE);
								if(InfoTarjeta(appState.getInfoTarjeta2(),3).contains("VISA")){
									informacionTarjeta2.setBackgroundResource(R.drawable.fondoinfovisa);
									nombreTarjeta2.setText(InfoTarjeta(appState.getInfoTarjeta2(),1));
									numeroTarjeta2.setText(InfoTarjeta(appState.getInfoTarjeta2(),4));
								}else if(InfoTarjeta(appState.getInfoTarjeta2(),3).contains("MASTER")){
									informacionTarjeta2.setBackgroundResource(R.drawable.fondoinfomaster);
									nombreTarjeta2.setText(InfoTarjeta(appState.getInfoTarjeta2(),1));
									numeroTarjeta2.setText(InfoTarjeta(appState.getInfoTarjeta2(),4));
								}else if(InfoTarjeta(appState.getInfoTarjeta2(),3).contains("AMEX")){
									informacionTarjeta2.setBackgroundResource(R.drawable.fondoinfoamerican);
									nombreTarjeta2.setText(InfoTarjeta(appState.getInfoTarjeta2(),1));
									numeroTarjeta2.setText(InfoTarjeta(appState.getInfoTarjeta2(),4));
								}else if(InfoTarjeta(appState.getInfoTarjeta2(),3).contains("DINERS")){
									informacionTarjeta2.setBackgroundResource(R.drawable.fondoinfodiners);
									nombreTarjeta2.setText(InfoTarjeta(appState.getInfoTarjeta2(),1));
									numeroTarjeta2.setText(InfoTarjeta(appState.getInfoTarjeta2(),4));
								}
								
							break;
							
							case(2):
								informacionTarjeta3.setVisibility(View.VISIBLE);
								nombreTarjeta3=(TextView) findViewById(R.id.nombretarjeta3);
								numeroTarjeta3=(TextView) findViewById(R.id.textonombretarjeta3);
								selectorTarjeta3.setVisibility(View.VISIBLE);
								if(InfoTarjeta(appState.getInfoTarjeta3(),3).contains("VISA")){
									informacionTarjeta3.setBackgroundResource(R.drawable.fondoinfovisa);
									nombreTarjeta3.setText(InfoTarjeta(appState.getInfoTarjeta3(),1));
									numeroTarjeta3.setText(InfoTarjeta(appState.getInfoTarjeta3(),4));
								}else if(InfoTarjeta(appState.getInfoTarjeta3(),3).contains("MASTER")){
									informacionTarjeta3.setBackgroundResource(R.drawable.fondoinfomaster);
									nombreTarjeta3.setText(InfoTarjeta(appState.getInfoTarjeta3(),1));
									numeroTarjeta3.setText(InfoTarjeta(appState.getInfoTarjeta3(),4));
								}else if(InfoTarjeta(appState.getInfoTarjeta3(),3).contains("AMEX")){
									informacionTarjeta3.setBackgroundResource(R.drawable.fondoinfoamerican);
									nombreTarjeta3.setText(InfoTarjeta(appState.getInfoTarjeta3(),1));
									numeroTarjeta3.setText(InfoTarjeta(appState.getInfoTarjeta3(),4));
								}else if(InfoTarjeta(appState.getInfoTarjeta3(),3).contains("DINERS")){
									informacionTarjeta3.setBackgroundResource(R.drawable.fondoinfodiners);
									nombreTarjeta3.setText(InfoTarjeta(appState.getInfoTarjeta3(),1));
									numeroTarjeta3.setText(InfoTarjeta(appState.getInfoTarjeta3(),4));
								}

							break;
					}
		}

		appState.setActividad(module);
		appState.setTarjetaPago("Nada");
		
		Log.i(APPNAME, module + "Contador Quedo en:"+ cont);		
		filter6 = new IntentFilter();
		filter6.addAction(module);
		
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Entro a Listar Tarjetas");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//************************************************************************
	
	public void EscogioTarjeta(View view){

		//boolean checked = ((RadioButton) view).isChecked();
		if(appState.isBorrarTarjeta()){	//Viene x el Menu Lateral 
			
			switch(view.getId()) {
				case R.id.radioButtonTarjeta1:
				case R.id.infotarjeta1:
					Log.i(APPNAME, module + "FUE LA TC 1");
					numeroTarjetaSeleccionada=InfoTarjeta(appState.getInfoTarjeta1(),4);
					tokenTarjeta=InfoTarjeta(appState.getInfoTarjeta1(),0);
					selectorTarjeta1.setBackgroundResource(R.drawable.fondoselectortrue);
					selectorTarjeta2.setBackgroundResource(R.drawable.fondoselectorfalse);
					selectorTarjeta3.setBackgroundResource(R.drawable.fondoselectorfalse);
				break;
				
				case R.id.radioButtonTarjeta2:
				case R.id.infotarjeta2:
					Log.i(APPNAME, module + "FUE LA TC 2");
					numeroTarjetaSeleccionada=InfoTarjeta(appState.getInfoTarjeta2(),4);
					tokenTarjeta=InfoTarjeta(appState.getInfoTarjeta2(),0);
					selectorTarjeta1.setBackgroundResource(R.drawable.fondoselectorfalse);
					selectorTarjeta2.setBackgroundResource(R.drawable.fondoselectortrue);
					selectorTarjeta3.setBackgroundResource(R.drawable.fondoselectorfalse);
				break;
				
				case R.id.radioButtonTarjeta3:
				case R.id.infotarjeta3:
					Log.i(APPNAME, module + "FUE LA TC 3");
					numeroTarjetaSeleccionada=InfoTarjeta(appState.getInfoTarjeta3(),4);
					tokenTarjeta=InfoTarjeta(appState.getInfoTarjeta3(),0);
					selectorTarjeta1.setBackgroundResource(R.drawable.fondoselectorfalse);
					selectorTarjeta2.setBackgroundResource(R.drawable.fondoselectorfalse);
					selectorTarjeta3.setBackgroundResource(R.drawable.fondoselectortrue);
				break;
			}
			
		}else{	//Viene para escoger tarjeta
			switch(view.getId()) {
			case R.id.radioButtonTarjeta1:
			case R.id.infotarjeta1:
				Log.i(APPNAME, module + " Escogio la Tarjeta 1");
				selectorTarjeta1.setBackgroundResource(R.drawable.fondoselectortrue);
				Toast.makeText(getApplicationContext(), "TARJETA SELECCIONADA: "+InfoTarjeta(appState.getInfoTarjeta1(),4), Toast.LENGTH_LONG).show();
				appState.setTarjetaPago(appState.getInfoTarjeta1());
				appState.setInfoTarjeta1("");
				appState.setInfoTarjeta2("");
				appState.setInfoTarjeta3("");
				appState.setFormaPago(4);	//Escogio Pago con Tarjeta de Credito
				break;
			case R.id.radioButtonTarjeta2:
			case R.id.infotarjeta2:
				Log.i(APPNAME, module + " Escogio la Tarjeta 2");
				selectorTarjeta2.setBackgroundResource(R.drawable.fondoselectortrue);
				Toast.makeText(getApplicationContext(), "TARJETA SELECCIONADA: "+InfoTarjeta(appState.getInfoTarjeta2(),4), Toast.LENGTH_LONG).show();
				appState.setTarjetaPago(appState.getInfoTarjeta2());
				appState.setInfoTarjeta1("");
				appState.setInfoTarjeta2("");
				appState.setInfoTarjeta3("");
				appState.setFormaPago(4);	//Escogio Pago con Tarjeta de Credito
				break;
			case R.id.radioButtonTarjeta3:
			case R.id.infotarjeta3:
				Log.i(APPNAME, module + " Escogio la Tarjeta 3");
				selectorTarjeta3.setBackgroundResource(R.drawable.fondoselectortrue);
				Toast.makeText(getApplicationContext(), "TARJETA SELECCIONADA: "+InfoTarjeta(appState.getInfoTarjeta3(),4), Toast.LENGTH_LONG).show();
				appState.setTarjetaPago(appState.getInfoTarjeta3());
				appState.setInfoTarjeta1("");
				appState.setInfoTarjeta2("");
				appState.setInfoTarjeta3("");
				appState.setFormaPago(4);	//Escogio Pago con Tarjeta de Credito
				break;

			}
			new Handler().postDelayed(new Runnable(){
				public void run(){

					try {
						finish();	
					} catch (Exception e) {
						e.printStackTrace();
					}	

				};
			}, 500);
			
		}
	}
	//************************************************************************
	public void EliminarTarjeta(View view){
		if(numeroTarjetaSeleccionada.equals("")){
			Toast.makeText(getApplicationContext(),"NO ESCOGIO TARJETA A BORRAR" ,Toast.LENGTH_LONG).show();
		}else{

		AlertDialog.Builder builder = new AlertDialog.Builder(AListarTarjetas.this);
		builder.setTitle(" BORRAR TARJETA ");
		builder.setMessage("¿Confirma que desea borrar la tarjeta de credito "+numeroTarjetaSeleccionada+"?");
		builder.setPositiveButton("Aceptar", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(APPNAME, module + "Si quiere borrar la tarjeta de credito.");	
				dialog.cancel();
				//String borrarToken = "{\"language\": \"es\", \"command\": \"REMOVE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"removeCreditCardToken\": {\"payerId\": \"10\", \"creditCardTokenId\": \"1d1767e0-44c4-435b-b20f-7f57e5958df3\"}}";
				//String borrarToken = "{\"language\": \"es\", \"command\": \"REMOVE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"removeCreditCardToken\": {\"payerId\": "+appState.getPayerIdTc()+", \"creditCardTokenId\": \"f11aae7c-2f39-4c6b-8446-5ebe9027a28b\"}}";


				//String borrarToken = "{\"language\": \"es\", \"command\": \"REMOVE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"removeCreditCardToken\": {\"payerId\": "+appState.getPayerIdTc()+", \"creditCardTokenId\": \""+tokenTarjeta+"\"}}";
				String colocaPais="";
				if(appState.getCiudadPais().contains("COLOMBIA"))	colocaPais="CO";
				else if(appState.getCiudadPais().contains("MEXICO"))	colocaPais="MX";
				else colocaPais="PA";
				String borrarToken = "eliminarTarjeta|"+colocaPais+"|"+appState.getCorreoUsuario()+"|"+tokenTarjeta;

				//appState.setComandoPayu("REMOVE_TOKEN");
				appState.setComandoPayu("ELIMINAR_TARJETA");

				Intent borrarTarjeta = new Intent();
				borrarTarjeta.putExtra("CMD", C.ENCRIPTADO);
				borrarTarjeta.putExtra("DATA", borrarToken);
				borrarTarjeta.setAction(C.COM);
				getApplicationContext().sendBroadcast(borrarTarjeta);

				progDailog6 = new ProgressDialog(AListarTarjetas.this);
				progDailog6.setTitle("BORRANDO TARJETA DE CREDITO");
				progDailog6.setMessage("ESPERE UN MOMENTO... ");
				progDailog6.setCancelable(true);
				progDailog6.show();
				
				try{
					Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
					t.setScreenName("Entro a Borrar Tarjetas");
					t.send(new HitBuilders.AppViewBuilder().build());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		builder.setNegativeButton("Cancelar", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(APPNAME, module + "No Quiere borrar la tarjeta de credito.");
				dialog.cancel();
			}
		});

		builder.show();
		}

	}
	
	//************************************************************************
	
	public String InfoTarjeta(String infotarjeta,int posicion){
		String[] Data = infotarjeta.split("\\|");
		Log.i(APPNAME, module + "ENTRO A INFO TARJETA");	
		Log.i(APPNAME, module + Data[0]);	//Token
		Log.i(APPNAME, module + Data[1]);	//Nombre
		Log.i(APPNAME, module + Data[2]);	//Supuesta Cedula
		Log.i(APPNAME, module + Data[3]);	//Franquicia
		Log.i(APPNAME, module + Data[4]);	//Numero de Tarjeta

		String infoRetorna="";
		if(posicion==0){
			infoRetorna=Data[0];
		}else if(posicion==1){
			infoRetorna=Data[1];
		}else if(posicion==3){
			infoRetorna=Data[3];
		}else if(posicion==4){
			infoRetorna=Data[4];
		}
		return infoRetorna;

	}
	
	//************************************************************************
	public void AddTarjeta(View v){
		Log.i("MENU TARJETA", "Entro a Adicionar Tarjeta");
		if(appState.getNumTarjetas()<3){
			RegistraTarjeta();
		}else{
			//AvisoHablado("USTEDNO PUEDE REGISTRARMAS DETRES TARJETAS");
			Toast.makeText(getApplicationContext(),"USTED NO PUEDE REGISTRAR MAS DE TRES TARJETAS" ,Toast.LENGTH_LONG).show();
		}
		//finish();
	}

	//************************************************************************

	public void RegistraTarjeta(){
		//Colocar Ventana para ingreso de Datos de Tarjeta de Credito...
		if((appState.getCiudadPais().contains("MEXICO"))||(appState.getCiudadPais().contains("COLOMBIA"))){


//			final AlertDialog.Builder builder = new AlertDialog.Builder(this); 
//			builder.setTitle("REGISTRAR TARJETA DE CREDITO");
//			builder.setMessage("¿Desea Registrar su Tarjeta de Credito? " );
//			builder.setPositiveButton("SI", new OnClickListener() {
//
//
//				public void onClick(DialogInterface dialog, int which) {
//					Log.i("REGISTRAR DATOS TARJETA", " Va a registrar su Tarjeta");
//					dialog.cancel();
//					Intent login = new Intent();
//					login.setClass(getApplicationContext(), ARegistrarTarjeta.class);
//					startActivity(login);
//					appState.setActividad(C.MAP);//Como si viniera del Mapa...
//					finish();
//				}
//			});
//			builder.setNegativeButton("NO", new OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					Log.i("REGISTRAR DATOS TARJETA", "Registro de Tarjeta Cancelado");
//					dialog.cancel();
//					//					        Intent login = new Intent();
//					//					        login.setClass(getApplicationContext(), AMapaUpVersion.class);
//					//					        startActivity(login);
//				}
//			});
//
//			builder.show();

			try{
				Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
				t.setScreenName("Desde Listar Tarjetas va a Registrar Tarjeta");
				t.send(new HitBuilders.AppViewBuilder().build());
			}catch(Exception e){
				e.printStackTrace();
			}
			
			Intent login = new Intent();
			login.setClass(getApplicationContext(), ARegistrarTarjeta.class);
			startActivity(login);
			appState.setActividad(C.MAP);//Como si viniera del Mapa...
			finish();

		}else{
			//AvisoHablado("SISTEMA DEPAGO PROXIMAMENTE ENSERVICIO");
			Toast.makeText(getApplicationContext(),"SISTEMA DE PAGO PROXIMAMENTE EN SERVICIO" ,Toast.LENGTH_LONG).show();
		}

	}
	//************************************************************************

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onPause");
		super.onPause();
		try {
			unregisterReceiver(receiver6);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onStart");
		super.onStart();
		registerReceiver(receiver6, filter6);
		GoogleAnalytics.getInstance(AListarTarjetas.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onStop");
		super.onStop();
		GoogleAnalytics.getInstance(AListarTarjetas.this).reportActivityStop(this);
	}
	
	
	

}
