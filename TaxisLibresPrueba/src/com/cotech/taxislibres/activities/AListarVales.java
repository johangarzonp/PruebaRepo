package com.cotech.taxislibres.activities;

import java.util.ArrayList;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AListarVales extends Activity {

	private static final String TAG = "TaxisLibres";
	protected String module = C.LISTAR_VALES;

	private Context context;
	private TaxisLi appState;

	private LinearLayout ingresarDatos;
	private LinearLayout barraContinuar;
	private LinearLayout mostrarListaVales;
	private LinearLayout barraSeleccionar;

	private EditText identificacionUsuario;
	private EditText codigoUsuario;

	private LinearLayout listaVales;
	private String valesPrueba="1|valeuno,2|valedos,3|valetres,4|valecuatro,5|valecinco,6|valeseis,7|valesiete,8|valeocho,9|valenueve,10|valediez,11|valeonce,12|valedoce,13|valetrece";
	private String [] vectorVales;
	ArrayList<String> numeroVales = new ArrayList<String>();
	ArrayList<String> tipoVale = new ArrayList<String>();
	private boolean escogioVale=false;
	private int valeEscogido=0;
	private ProgressDialog progDailogVales = null;
	private IntentFilter filterVales;

	private String numVales="";
	private String tipoVales="";
	private String nomEmpresa="";

	private String codUsuario="";
	private String cedula="";

	//************************************************************************************************
	public final BroadcastReceiver receiverListaVales = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, Intent intent) {	
			progDailogVales.dismiss();
			Integer cmd = intent.getIntExtra("CMD", 0);
			String informacion = intent.getStringExtra("DATA");
			switch(cmd){

			case C.LISTA_VALES_OK:
				Log.i(TAG, module + "HAY QUE PROCESAR LA LISTA DE VALES...");
				if(informacion.contains("\"valesCliente\"")){
					String[] buscanum = informacion.split("\"numeroVale\"");
					String[] buscatipo = informacion.split("\"tipoValeDetalle\"");
					String[] buscaemp = informacion.split("\"empresa\"");
					if((buscanum.length==buscatipo.length)&&(buscatipo.length==buscaemp.length)){

						int cont3=1;
						while(cont3 < buscanum.length){
							String[] encontrarid= buscanum[cont3].split("\"");

							numVales=numVales+encontrarid[1]+",";
							String[] encontrardir= buscatipo[cont3].split("\"");

							tipoVales=tipoVales+encontrardir[1]+",";
							String[] encontraridCli= buscaemp[cont3].split("\"");

							nomEmpresa=nomEmpresa+encontraridCli[1]+",";
							cont3++;
						}
						Log.i (TAG, module+ " numeroVales: "+numVales);
						Log.i (TAG, module+ " tipoVales: "+tipoVales);
						Log.i (TAG, module+ " Empresa: "+nomEmpresa);
						valesPrueba=numVales;
						ingresarDatos.setVisibility(View.INVISIBLE);
						barraContinuar.setVisibility(View.INVISIBLE);
						mostrarListaVales.setVisibility(View.VISIBLE);
						barraSeleccionar.setVisibility(View.VISIBLE);
						
						appState.setUsuarioVales(cedula);
						appState.setClaveVales(codUsuario);
						
						MostrarVales();
					}
				}else{
					Log.i(TAG, module + "NO HAY VALES...");
				}
				break;

			case C.LISTA_VALES_ERROR:
				Log.i(TAG, module + "NO HAY LISTA DE VALES...");
				String[] buscaerror = informacion.split("\"error\"");
				//Log.i(TAG, module + "encontro: "+buscaerror[1]);
				String[] encontroerror =buscaerror[1].split("\"");
				Log.i(TAG, module + "encontro: "+encontroerror[1]);
				Toast.makeText(context, "ERROR: "+encontroerror[1], 5000).show();
				break;

			case C.ERROR_SOCKET:
				Log.i(TAG, module + "ERROR AL ENVIAR LA INFORMACION");
				Toast.makeText(context, "ERROR EN LA COMUNICACION, INTENTE NUEVAMENTE", 5000).show();
				break;

			}
		}
	};
	//************************************************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listar_vales);
		context = getApplicationContext();
		appState = ((TaxisLi) context);


		ingresarDatos= (LinearLayout) findViewById(R.id.layoutpartearribavales);
		ingresarDatos.setVisibility(View.VISIBLE);
		barraContinuar=(LinearLayout) findViewById(R.id.layoutbotoncontinuar);
		barraContinuar.setVisibility(View.VISIBLE);
		mostrarListaVales=(LinearLayout) findViewById(R.id.layoutpartearribavales2);
		mostrarListaVales.setVisibility(View.INVISIBLE);
		barraSeleccionar=(LinearLayout) findViewById(R.id.layoutbotonseleccionar);
		barraSeleccionar.setVisibility(View.INVISIBLE);
		//---------------------------------------------
		identificacionUsuario=(EditText) findViewById(R.id.identificacionvales);
		codigoUsuario=(EditText) findViewById(R.id.clavevales);
		//---------------------------------------------
		listaVales=(LinearLayout) findViewById(R.id.listaValesUsuario);


		filterVales = new IntentFilter();
		filterVales.addAction(module);
		registerReceiver(receiverListaVales, filterVales);
		appState.setActividad(module);

		//Verificar si ya hay usuario para Vales...

		if(!appState.getUsuarioVales().equals("")){		//Se realiza la consulta de vales...
			identificacionUsuario.setText(appState.getUsuarioVales());
			cedula=appState.getUsuarioVales();
			codigoUsuario.setText(appState.getClaveVales());
			codUsuario=appState.getClaveVales();
			String enviaCodigo = "Y|2|" +appState.getNumberPhone()+"|"+appState.getUsuarioVales()+"|"+appState.getClaveVales()+"|D"+"\n";

			Intent consultavale = new Intent();
			consultavale.putExtra("CMD", C.SEND);
			consultavale.putExtra("DATA", enviaCodigo);
			consultavale.setAction(C.COM);
			context.sendBroadcast(consultavale);

			progDailogVales = new ProgressDialog(AListarVales.this);

			progDailogVales.setTitle("ENVIANDO INFORMACION DE USUARIO");
			progDailogVales.setMessage("ESPERE UN MOMENTO... ");
			progDailogVales.setCancelable(true);
			progDailogVales.show();
		}
	}
	//************************************************************
	public void MostrarVales(){

		vectorVales= valesPrueba.split(",");
		String vectorNombres[]=nomEmpresa.split(",");
		String vectorTipo[]=tipoVales.split(",");
		Log.i(module,"Numero de Vales: "+ vectorVales.length);

		int cont=0;
		while(vectorVales.length > cont){

			//String[] partesMensaje = vectorVales[cont].split("\\|");
			//La parte 0 contiene quien envio el mensaje, la parte 1 el mensaje...
			TextView text = new TextView(this);

			text.setBackgroundResource(R.drawable.cuadroenviarmsgs);
			text.setId(900000000+cont);
			//		    numeroVales.add(partesMensaje[1]);	//Guardar el Numero del Vale	
			//			text.setText(partesMensaje[1]);			
			numeroVales.add(vectorVales[cont]);	//Guardar el Numero del Vale
			tipoVale.add(vectorTipo[cont]);
			text.setText(vectorVales[cont]+" - "+vectorNombres[cont]);	
			text.setTextSize(14);
			//text.setMaxWidth(partesMensaje[1].length());
			text.setMaxWidth(listaVales.getWidth());
			text.setGravity(Gravity.CENTER);
			text.setPadding(5,0,5,0);
			text.setTextColor(Color.BLACK);
			listaVales.addView(text);

			Log.i(TAG, module + ": Registro #"+cont+"."+ vectorVales[cont]+"id: "+text.getId());
			cont++;

			text.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {

					Log.i("click",String.valueOf(v.getId()));
					if((escogioVale)&&(v.getId()==valeEscogido)){
						escogioVale=false;
						valeEscogido=0;
						v.setBackgroundResource(R.drawable.cuadroenviarmsgs);
					}else{
						if((!escogioVale)&&(valeEscogido==0)){
							escogioVale=true;
							valeEscogido=v.getId();
							v.setBackgroundResource(R.drawable.cuadrochulito);
							
							new Handler().postDelayed(new Runnable(){
								public void run(){
									SeleccionoVale();
								};
							}, 800);
						}
					}
				}});
		}
	}
	//************************************************************
	public void PresionaContinuar(View v){
		Log.i(TAG, module + " ENTRO POR PresionaContinuar");
		//Se debe enviar la info de Identificación y Código de Usuario...
		codUsuario=codigoUsuario.getText().toString();
		cedula=identificacionUsuario.getText().toString();

		if(codUsuario.equals("")){
			Toast.makeText(getApplicationContext(), "NO INGRESO CODIGO DE USUARIO", 5000).show();
		}else{
			if(cedula.equals("")){
				Toast.makeText(getApplicationContext(), "NO INGRESO SU IDENTIFICACION", 5000).show();
			}else{
				//Se debe Enviar Consulta para Lista de Vales...
				String enviaCodigo = "Y|2|" +appState.getNumberPhone()+"|"+cedula+"|"+codUsuario+"|D"+"\n";

				Intent consultavale = new Intent();
				consultavale.putExtra("CMD", C.SEND);
				consultavale.putExtra("DATA", enviaCodigo);
				consultavale.setAction(C.COM);
				context.sendBroadcast(consultavale);

				progDailogVales = new ProgressDialog(AListarVales.this);

				progDailogVales.setTitle("ENVIANDO INFORMACION DE USUARIO");
				progDailogVales.setMessage("ESPERE UN MOMENTO... ");
				progDailogVales.setCancelable(true);
				progDailogVales.show();
			}

		}
	}

	//************************************************************
	public void PresionaSeleccionar(View v){
		Log.i(TAG, module + " ENTRO POR PresionaSeleccionar");
		SeleccionoVale();
	}
	
	//************************************************************
	public void SeleccionoVale(){
		if(escogioVale){
			//Se debe almacenar el numero de vale escogido...
			//		appState.setFormaPago(1);	//Escogio Pago con Vale Digital
			//		appState.setFormaPago(3);	//Escogio Pago con Vale Fisico
			//		appState.setValeUsuario("");
			Log.i(TAG, module + " El Vale escogido es el: "+numeroVales.get(valeEscogido-900000000));
			Log.i(TAG, module + " El Tipo de Vale escogido es : "+tipoVale.get(valeEscogido-900000000));
			appState.setValeUsuario(numeroVales.get(valeEscogido-900000000));
			
			Toast.makeText(context, " El Vale escogido es el: "+numeroVales.get(valeEscogido-900000000), Toast.LENGTH_LONG).show();
			
			if(tipoVale.get(valeEscogido-900000000).contains("DIGITAL"))	appState.setFormaPago(1);	//Escogio Pago con Vale Digital
			else	appState.setFormaPago(3);	//Escogio Pago con Vale Fisico

			String s = "SeleccionoVale";
			Intent i = getIntent();
			//i.putExtra("pedirTaxi", s);
			i.putExtra("SeleccionoVale", s);
			setResult(RESULT_OK, i);
			finish();

		}else{
			Toast.makeText(context, "DEBE SELLECCIONAR UN VALE", Toast.LENGTH_LONG).show();
		}
	}

	//************************************************************
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, module + " Entra por onDestroy");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, module + " Entra por onPause");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, module + " Entra por onResume");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, module + " Entra por onStart");

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, module + " Entra por onStop");

	}

}
