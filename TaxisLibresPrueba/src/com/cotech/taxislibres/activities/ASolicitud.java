package com.cotech.taxislibres.activities;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.EstadosUnidad;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
//import android.util.Log;
import com.cotech.taxislibres.activities.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ASolicitud extends Activity {
	
	protected String APPNAME = "TaxisLibres";
	protected String module = C.SOLICITUD;
	private TaxisLi appState;
	private Context context;
	private TextView avisoComplete;
	private EditText direccion;
	private EditText barrio;
	private TextView InfoBarr;
	
	private Button	guardaFavoritos;
	private Button	verFavoritos;
	
	private TextView avisoPago;
	private Spinner selectorPago;
	
	private int copiaNumFrec;
	private int valorPropina;

	private EditText boxDestino;
	
	private CheckBox pagoValefisico;
	private CheckBox pagoValeelectronico;
	private CheckBox pagoCredito;
	
	private IntentFilter filter2;
	
	private ProgressDialog esperaToken = null;
	private String numeroValeIngresado;
	private boolean nuevoFisicoDigital=false;
	private boolean  seleccionoPago=false;
	String valeTipo;
	
		
	static final int READ_BLOCK_SIZE = 200;
	
	//*******************************************************************************************
	public final BroadcastReceiver receiver2 = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {									
			Integer cmd = intent.getIntExtra("CMD", 0);
			switch(cmd){
			
			case C.GET_TOKENS_OK:
				esperaToken.dismiss();
				
				MostrarTarjetas();
				break;
				
			case C.GET_TOKENS_ERROR:
				esperaToken.dismiss();
				Toast.makeText(context, "ERROR AL CONSULTAR TARJETAS", Toast.LENGTH_LONG).show();
				break;
				
			case C.VALE_OK_SIN_CODIGO:	//Vale Fisico en BD Antigua Disponible
			case C.VALE_CODIGO_OK:
				esperaToken.dismiss();
				//Se debe validar si es Fisico o Digital
				if(nuevoFisicoDigital)	appState.setFormaPago(1);	//Escogio Pago con Vale Digital
				else	appState.setFormaPago(3);	//Escogio Pago con Vale Fisico
				appState.setValeUsuario(numeroValeIngresado);
				
				AvisoHablado2("VALE DISPONIBLE PARA UTILIZAR");
				Toast.makeText(context, "VALE PROCESADO CORRECTAMENTE", Toast.LENGTH_LONG).show();
				break;
				
			case C.VALE_MAL_SIN_CODIGO:	//Vale NO Disponible ni en la base antigua ni en la nueva
				appState.setFormaPago(0);	//Para escoger otra forma de pago
				esperaToken.dismiss();
				
				Intent valeMal = new Intent();
				valeMal.setAction(C.TEXT_TO_SPEECH);
				valeMal.putExtra("CMD", C.HABLAR);
				valeMal.putExtra("TEXTHABLA", "VALE NODISPONIBLE PARA UTILIZAR");
				sendBroadcast(valeMal);
				
				Toast.makeText(context, "VALE NO DISPONIBLE PARA UTILIZAR", Toast.LENGTH_LONG).show();
				
				break;
				
			case C.VALE_OK_CON_CODIGO:	//Vale de la Base Nueva
				String digitalfisico = intent.getStringExtra("DATA");
				if(digitalfisico.equals("0")){
					nuevoFisicoDigital=false;
					Log.i(APPNAME, module + "EL VALE ESTA EN LA BASE NUEVA ES FISICO");
				}
				else if(digitalfisico.equals("1")){
					nuevoFisicoDigital=true;
					Log.i(APPNAME, module + "EL VALE ESTA EN LA BASE NUEVA ES DIGITAL");
				}
				esperaToken.dismiss();
				Toast.makeText(context, "INGRESAR CODIGO DE EMPLEADO", Toast.LENGTH_LONG).show();
				IngresarCodigoEmpeado();
				
				break;
				
			case C.VALE_OK_CODIGO_MAL:
				esperaToken.dismiss();
				Toast.makeText(context, "CODIGO DE EMPLEADO INCORRECTO,INGRESE NUEVAMENTE EL CODIGO", Toast.LENGTH_LONG).show();
				IngresarCodigoEmpeado();
				break;
				
						
			case C.FAVORITOS_OK:
				esperaToken.dismiss();
				
				String infoFrecuentes = intent.getStringExtra("DATA");
				String buscarFrecuentes= infoFrecuentes.replace("{Z|1|","");
				
				MostrarFavoritos(buscarFrecuentes);
				break;
				
			case C.CERRARAPP:
				try{
					Intent i = getIntent();
			  		String s = "";
					i.putExtra("cerrarApp", s);
					ASolicitud.this.setResult(RESULT_OK, i);
			  		finish();
					
				}catch (Exception e){
					e.printStackTrace();
				}
			break;
			
			case C.NO_HAY_FAVORITOS:
				esperaToken.dismiss();
				Intent favtalk = new Intent();
				favtalk.setAction(C.TEXT_TO_SPEECH);
				favtalk.putExtra("CMD", C.HABLAR);
				favtalk.putExtra("TEXTHABLA", "USTEDNO TIENE DIRECCIONES GUARDADAS");
				sendBroadcast(favtalk);
				Toast.makeText(context, "USTED NO TIENE DIRECCIONES GUARDADAS", Toast.LENGTH_SHORT).show();
				break;
				
			case C.ERROR_CARGANDO_FAVORITOS:
				esperaToken.dismiss();
				Toast.makeText(context, "ERROR AL CARGAR SUS DIRECCIONES FAVORITAS", 5000).show();
				break;
				
			case C.ALMACENAR_FAVORITOS_OK:
				esperaToken.dismiss();
				Toast.makeText(context, "DIRECCION ALMACENADA CORRECTAMENTE", 5000).show();
				break;
				
			case C.ALMACENAR_FAVORITOS_ERROR:
				esperaToken.dismiss();
				Toast.makeText(context, "ERROR AL ALMACENAR DIRECCION", 5000).show();
				break;
				
			case C.FAVORITO_BORRADO_OK:
				esperaToken.dismiss();
				Toast.makeText(context, "DIRECCION BORRADA CORRECTAMENTE", 5000).show();
				break;
				
			case C.FAVORITO_BORRADO_ERROR:
				esperaToken.dismiss();
				Toast.makeText(context, "ERROR AL BORRAR DIRECCION", 5000).show();
				break;
			
			case C.ERROR_SOCKET:
				esperaToken.dismiss();
				Toast.makeText(context, "ERROR EN LA COMUNICACION, INTENTE NUEVAMENTE", 5000).show();
			break;
			
			
			}

			
		}
	};
	
	//*******************************************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.simple_solicitud);
		context = getApplicationContext();
		appState = ((TaxisLi) context);
		
		float factorTamano=1;
		boolean cambiarTamano=false;
		
		avisoComplete = (TextView) findViewById(R.id.avisodireccion);
		
		float tamanoActual=avisoComplete.getTextSize();
		Log.i(APPNAME, module + "TAMAÑO DE COMPLETE SU DIRECCION: "+ tamanoActual );
		
		SpannableString content = new SpannableString("COMPLETE SU DIRECCION");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		
		avisoComplete.setText("Complete su Dirección");
		
		direccion = (EditText) findViewById(R.id.compdireccion);
		barrio = (EditText) findViewById(R.id.compbarrio);
		
						
		barrio.setText(appState.getBarrio());
		
		InfoBarr = (TextView) findViewById(R.id.infobarr);
		try{
			String pais = appState.getCiudadPais();
			String avisoPais="";
			if((pais.contains("MEXICO"))||(pais.contains("Mexico"))){
				avisoPais="Complete la Colonia";
			}else{
				
				avisoPais="Complete el Barrio";
			}
			SpannableString content1 = new SpannableString(avisoPais);
			content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
			
			InfoBarr.setText(avisoPais);
		}catch(Exception e){
			 e.printStackTrace();
			 InfoBarr.setText("ERROR AL LEER INFO ADICIONAL");
     	 }
		InfoBarr.setTextColor(Color.BLACK);
		
		
		guardaFavoritos=(Button)findViewById(R.id.guardarfrecuentes);
		verFavoritos=(Button)findViewById(R.id.leerfrecuentes);
		avisoPago=(TextView) findViewById(R.id.avisometododepago);
			
		
		appState.setFormaPago(0);
		appState.setEscogioPago(false);	//Aun no se escoge pago...	
		appState.setDestinoUsuario("");
		appState.setIncentivoTaxista("0"); //No hay Propina
		appState.setActividad(module);
		
		filter2 = new IntentFilter();
		filter2.addAction(module);
		
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
				 
				 direccion.setText(s);
				 
				 //Toast.makeText(getApplicationContext(), "CARGANDOOOOO   " + s, Toast.LENGTH_LONG).show();
				 
				 isr.close();
				 
			 }catch(IOException e){
				 e.printStackTrace();
			 }
			 
		 }else{
			 direccion.setText(appState.getDireccionGoogle());
			 direccion.setSelection(appState.getDireccionGoogle().length());
		 }
		 

	}
	
	//*******************************************************************************************
	public void GuardarFrec(View v){}
	//*******************************************************************************************
	
	public void GuardarFrec2(View v){}

	//*******************************************************************************************
	
	public void LeerFrec2(View v){}
	
	//*******************************************************************************************
	
	public void LeerFrec(View v){}
	
	//*******************************************************************************************
	
	public void MostrarFavoritos(String favoritos){}
	
	//*******************************************************************************************
	
	public void PedirTaxi (View v){}
	

	//*******************************************************************************************
	
	public String TraerSoloDireccion(String info){
		
		String[] Data = info.split("\\|");
		
		//Log.i(APPNAME, module + Data[4]);
		//return Data[4];
		
//		Log.i(APPNAME, module + Data[Data.length -6]);
//		return Data[Data.length -6];
		
		Log.i(APPNAME, module + Data[Data.length -5]);	//Sacar la direccion que coloca el usuario en el Box
		return Data[Data.length -5];
	}
	
	//*******************************************************************************************
	
	public void Cancelar(View v){}
	
	//*******************************************************************************************
	
		
		public void onCheckboxClicked(View view) {}
		
		//*******************************************************************************************
		
		//Para Mostrar la Propina
		//public void PropinaTaxista(View v){
		public void PropinaTaxista(){}
		
		//*******************************************************************************************
		//Para sumar a la propina
		public void Sumar(View v){
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
			
		}
		
		//*******************************************************************************************
		//Para restar a la propina
		public void Restar(View v){
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
			
		}
		
		//*******************************************************************************************
		//Funcion para enviar broadcast de aviso que debe ser de voz...
		public void AvisoHablado2(String aviso){}
	
		//*******************************************************************************************	
	public void MostrarTarjetas(){}
	
	//*******************************************************************************************
	
	public String TraerInfoTarjeta(String infotarjeta){
		String[] Data = infotarjeta.split("\\|");
		Log.i(APPNAME, module + "ENTRO A TRAER INFO TARJETA");	
		Log.i(APPNAME, module + Data[0]);
		Log.i(APPNAME, module + Data[1]);
		Log.i(APPNAME, module + Data[2]);
		Log.i(APPNAME, module + Data[3]);
		Log.i(APPNAME, module + Data[4]);
		
		String infoRetorna= Data[3]+"  "+ Data[4];
		return infoRetorna;
		
	}
	
	//*******************************************************************************************
	
	public void LeerVale(final int tipovale){}
	
	//*******************************************************************************************
	
	public void ReconfirmarVale(String vale){}
	
	//*******************************************************************************************
	
	public void IngresarCodigoEmpeado(){} 
	
	//*******************************************************************************************
	
	public void PagoConTarjeta(){}
	
	//*******************************************************************************************
	
	public void OpcionesVale1(){}
	
	//*******************************************************************************************	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
			switch(keyCode){
				case KeyEvent.KEYCODE_BACK:
					Intent cancela = getIntent();
					cancela.putExtra("cancela", "cancela");
					setResult(RESULT_OK, cancela);
					
					
					
					finish();
					return true;
				
			}
			return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.i(APPNAME, module + " Entra por onStart");
		registerReceiver(receiver2, filter2);
		appState.setActividad(module);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(APPNAME, module + "Entra por onDestroy");
		//appState.setEstadoUnidad(EstadosUnidad.LIBRE);
		finish();		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "Entra por onPause");
		super.onPause();
		if(appState.getFormaPago()!=0)	appState.setEscogioPago(true);
		else	appState.setEscogioPago(false);
		//Hay que validar que formas de pago pueden llevar incentivo al Taxista
		if((appState.getFormaPago()==1)||(appState.getFormaPago()==3))	appState.setIncentivoTaxista("0");
		else{
			if(valorPropina==0)	appState.setIncentivoTaxista("0");
			else{
				String incentivo;
				incentivo= String.valueOf(valorPropina);
				Log.i(APPNAME, module + "VALOR PROPINA: "+ valorPropina);
				appState.setIncentivoTaxista(incentivo);
				Log.i(APPNAME, module + "VALOR PROPINA GUARDADO: "+ appState.getIncentivoTaxista());
			}
		}
		try {
			unregisterReceiver(receiver2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "Entra por onResume");
		super.onResume();
		if(appState.getVale()==20){	
			appState.setEscogioPago(false);
			appState.setFormaPago(0);
			Log.i(APPNAME, module + "Cambio estado del pago...");
			appState.setVale(30);	//VALOR X DEFECTO
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "Entra por onStop");
		super.onStop();
	}

	//*******************************************************************************************
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(APPNAME, module + "onActivityResult");
	    	super.onActivityResult(requestCode, resultCode, data);
	    
	    	Log.i(APPNAME, module + "_____________________________ " + requestCode +"    " + resultCode + "   " + data);
	    
	    try{
		    if(data.getExtras().containsKey("cancela")){
		    	  Log.i(APPNAME, "Salio de Registrar Tarjeta...");
		    	  
		      }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}
		
	
}
