package com.cotech.taxislibres.activities;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import java.util.Calendar;

import co.com.taxislibres.pojo.PayUCommon;
import co.com.taxislibres.pojo.token.BasicResponse;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.EstadosUnidad;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.payu.PagoPayu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;




import android.os.Handler;












import com.cotech.taxislibres.TaxisLi.TrackerName;
//import android.util.Log;
import com.cotech.taxislibres.activities.Log;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ARegistrarTarjeta extends Activity {
	
	private static final String TAG = "TaxisLibres";
	protected String module = C.REGISTRAR_TARJETA;
	
	private Context context;
	private TaxisLi appState;
	
	private EditText nombreUsuarioTarjeta;
	//private EditText numeroIdentificacion;
	private EditText numeroTarjetaUsuario;
	//private Spinner selectorTarjeta;
	private EditText anoVence;
	private EditText mesVence;
	private EditText codigoSeguridad;
	
	private IntentFilter filter3;
	
	private ProgressDialog esperaRta = null;
	
	private int actividadAnterior=0;
	private String clave1="";
	private String clave2="";
	
	private  String franquicia;
	
	private RadioButton tarjetaVisa;
	private RadioButton tarjetaMaster;
	private RadioButton tarjetaAmerican;
	private RadioButton tarjetaDiners;
	
	private int Densidad;
	
	private String nombreUsuario;
	private String numeroTarjeta;
	private String anoVencimiento;
	private String mesVencimiento;
	private String cvv;
	
	private ImageView iconoVisa;
	private ImageView iconoMaster;
	private ImageView iconoAmerican;
	private ImageView iconoDiners;
	
	
	//*******************************************************************************************
	public final BroadcastReceiver receiver3 = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {									
			Integer cmd = intent.getIntExtra("CMD", 0);
			String infoRta = intent.getStringExtra("DATA");
			Log.i(TAG, module + " ENTRO POR EL RECEIVER DE REGISTRAR TARJETA");
			switch(cmd){
			
				case C.RESPUESTA_OK:
					try{
						esperaRta.dismiss();	//Cierra la ventana de Espera
						Toast.makeText(getApplicationContext(), "TARJETA REGISTRADA CORRECTAMENTE", 1000).show();
						appState.setPayerIdTc("SiHay");
						if(actividadAnterior==2){
							Log.i(TAG, module + "Viene de Solicitud");
						}
						else if(actividadAnterior==0){
							Intent login = new Intent();
							login.setClass(getApplicationContext(), AMapaUpVersion.class);
							startActivity(login);
						}
						finish();
					}catch (Exception e){
						e.printStackTrace();
					}
				break;
				
				case C.RESPUESTA_MAL:
					try{
						esperaRta.dismiss();	//Cierra la ventana de Espera
						Toast.makeText(getApplicationContext(), "TARJETA NO SE REGISTRO CORRECTAMENTE", 5000).show();
					}catch (Exception e){
						e.printStackTrace();
					}
				break;
				
				case C.RESPUESTA_SOCKET:
				case C.RESPUESTA_FALLOPING:
					try{
						esperaRta.dismiss();	//Cierra la ventana de Espera
						Toast.makeText(getApplicationContext(), "ERROR EN LA COMUNICACION, INTENTE NUEVAMENTE", 5000).show();
					}catch (Exception e){
						e.printStackTrace();
					}
				break;
				
				case C.RESPUESTA_DESCONOCIDA:
					try{
						esperaRta.dismiss();	//Cierra la ventana de Espera
						Toast.makeText(getApplicationContext(), "ERROR INESPERADO", 5000).show();
					}catch (Exception e){
						e.printStackTrace();
					}
				break;
				
				case C.RESPUESTA_REGISTRO_MIEMBRO:
					esperaRta.dismiss();	//Cierra la ventana de Espera
					Log.i(TAG, module + "HAY QUE REVISAR LA RESPUESTA DE REGISTRO_MIEMBRO");
					
					if(infoRta.contains("0|")){	
						
						appState.setPedirClaveElectronicos(false);
						appState.setTengoClaveElectronicos(true);	//Se registraron medios electrónicos
						String[] posiciones=infoRta.split("\\|");
						try {
							appState.setPayerIdTc(posiciones[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						InscribirTarjeta();
						
						//Toast.makeText(getApplicationContext(), "CLAVE REGISTRADA CORRECTAMENTE,PUEDE INICIAR EL REGISTRO DE SU TARJETA", 5000).show();
					}
					else if(infoRta.contains("1|La contrasena es muy simple")){
						Toast.makeText(getApplicationContext(), "CLAVE MUY SIMPLE,INTENTE CON OTRA CLAVE NUEVAMENTE", 5000).show();
						//IngresarClaveElectronicos();
						Intent i = new Intent(ARegistrarTarjeta.this, ARegistrarClaveElectronicos.class);
						startActivityForResult(i, 1);
					}
					else{
						Toast.makeText(getApplicationContext(), "ERROR AL REGISTRAR SU CLAVE,INTENTE NUEVAMENTE", 5000).show();
						//IngresarClaveElectronicos();
						Intent i = new Intent(ARegistrarTarjeta.this, ARegistrarClaveElectronicos.class);
						startActivityForResult(i, 1);
					}
				break;
				
				case C.RESPUESTA_VERIFICACION_MIEMBRO:
					esperaRta.dismiss();	//Cierra la ventana de Espera
					Log.i(TAG, module + "llego Info de Verificar Miembro");
					if(infoRta.contains("0|true")){	//Informacion correcta se recupera Info
						appState.setPedirClaveElectronicos(false);
						appState.setTengoClaveElectronicos(true);
						String[] posiciones=infoRta.split("\\|");
						//0|true|PayerId|numero de tarjetas
						appState.setPayerIdTc(posiciones[2]);
						appState.setNumTarjetas(Integer.parseInt(posiciones[3]));
						Toast.makeText(context, "CLAVE CORRECTA, INFORMACION RECUPERADA", 5000).show();
						Log.i(TAG, module + " NUMERO DE TARJETAS: " + appState.getNumTarjetas());
						if(appState.getNumTarjetas()>0){
							Intent rta_gettoken = new Intent();
							//rta_gettoken.putExtra("DATA","Nada");
							//rta_gettoken.putExtra("CMD",C.CONSULTAR_TARJETAS_REG);
							//rta_gettoken.setAction(C.MAP);
							//finish();
							ConsultarTarjetasMapa();
						}

					}else if(infoRta.contains("1|false")){	//Información Incorrecta
						Toast.makeText(context, "CLAVE INCORRECTA", 5000).show();
						Intent i = new Intent(ARegistrarTarjeta.this, APedirClaveElectronicos.class);
						startActivityForResult(i, 1);
					}else if(infoRta.contains("1|true")){
						String[] separaRta=infoRta.split("\\|");
						Log.i(TAG, module + " Vienen este numero de campos: " + separaRta.length);
						//Se supone error debe venir en la posicion 3
						Toast.makeText(context, separaRta[3], 5000).show();
					}
				break;
					
			}
		}
	};
	//*******************************************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		context = getApplicationContext();
		appState = ((TaxisLi) context);
		
		Densidad = getResources().getDisplayMetrics().densityDpi;
		
		if(Densidad<=240){
			setContentView(R.layout.registrar_tarjeta);
		}else{
			setContentView(R.layout.registrar_tarjeta_320);
		}	
	    
	    
	    nombreUsuarioTarjeta = (EditText)  findViewById(R.id.nombretarjeta);
	    //numeroIdentificacion = (EditText)  findViewById(R.id.numeroidentificacion);
	    numeroTarjetaUsuario = (EditText)  findViewById(R.id.numerotarjeta);
	    
	    listenerEditText(numeroTarjetaUsuario);
	    //selectorTarjeta=(Spinner)findViewById(R.id.seleccionaTarjeta);
	    
	    tarjetaAmerican= (RadioButton) findViewById(R.id.radioButtonAmerican);
	    tarjetaDiners=(RadioButton) findViewById(R.id.radioButtonDiners);
	    tarjetaMaster=(RadioButton) findViewById(R.id.radioButtonMaster);
	    tarjetaVisa=(RadioButton) findViewById(R.id.radioButtonVisa);
	    
	    iconoAmerican=(ImageView) findViewById(R.id.iconotarjeta1);
	    iconoDiners=(ImageView) findViewById(R.id.iconotarjeta2);
	    iconoMaster=(ImageView) findViewById(R.id.iconotarjeta3);
	    iconoVisa=(ImageView) findViewById(R.id.iconotarjeta4);
	    
	    anoVence = (EditText)  findViewById(R.id.yearvenc);
	    mesVence = (EditText)  findViewById(R.id.mesvenc);
    

//	    
//	    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, tipoTarjeta);
//	    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
	    //selectorTarjeta.setAdapter(spinnerArrayAdapter);
	    
	    
	    codigoSeguridad= (EditText) findViewById(R.id.numerocvv);
	    
	    franquicia="0";
	    
	    filter3 = new IntentFilter();
		filter3.addAction(module);
		
		
		try {
			if(appState.getActividad().equals(C.MAP)){
				actividadAnterior=1;
			}else if(appState.getActividad().equals(C.SOLICITUD)){
				actividadAnterior=2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		//Obliga a no pedir Clave para Medios Electronicos
//		appState.setPedirClaveElectronicos(false);
//		appState.setTengoClaveElectronicos(true);
//		
//		if(appState.isPedirClaveElectronicos()){	//No hay Clave de Medios Electrónicos
//			//Hay que pedir la clave para recuperar la info de tarjetas...
//			Log.i(TAG, module + " Ya tiene medios electronicos");
//			//RecuperarDatos();
//			
////			new Handler().postDelayed(new Runnable(){
////				public void run(){
////					//Pasan 3 segundos
////					Intent i = new Intent(ARegistrarTarjeta.this, APedirClaveElectronicos.class);
////					startActivityForResult(i, 1);
////				};
////			}, 5000);
//			
//			Intent i = new Intent(ARegistrarTarjeta.this, APedirClaveElectronicos.class);
//			startActivityForResult(i, 1);
//			
//		}
//		else if(!appState.isTengoClaveElectronicos()){	//No hay Clave de Medios Electrónicos
//			Log.i(TAG, module + " Como si no tuviera medios electronicos");
//			//IngresarClaveElectronicos();
//			Intent i = new Intent(ARegistrarTarjeta.this, ARegistrarClaveElectronicos.class);
//			startActivityForResult(i, 1);
//		}
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Ingresa a Registrar Tarjeta");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//*******************************************************************************************
	public void ConsultarTarjetasMapa(){
		String s = "ConsultarTarjetas";
		Intent i = getIntent();
		i.putExtra("consultarTarjetas", s);
		setResult(RESULT_OK, i);
		finish();
	}
	
	//*******************************************************************************************
	public void CheckNumeroTarjeta(View v){
		Log.i(TAG, module + "Para Chequear numero tarjeta...");
		
	}
	//*******************************************************************************************
	public void EscogioTarjeta(View view){

		//boolean checked = ((RadioButton) view).isChecked();

		switch(view.getId()) {

		case R.id.radioButtonAmerican:
		case R.id.iconotarjeta1:
			Log.i(TAG, module + "Selecciono American...");
			SeleccionarAmerican();
			break;

		case R.id.radioButtonDiners:
		case R.id.iconotarjeta2:
			Log.i(TAG, module + "Selecciono Diners...");
			SeleccionarDiners();	
			break;

		case R.id.radioButtonMaster:
		case R.id.iconotarjeta3:
			Log.i(TAG, module + "Selecciono Master...");
			SeleccionarMaster();	
			break;

		case R.id.radioButtonVisa:
		case R.id.iconotarjeta4:
			Log.i(TAG, module + "Selecciono Visa...");
			SeleccionarVisa();
				
			break;

		}

	}
	//*******************************************************************************************	
	public void Cancelar(View v){
		
		Log.i(TAG, module + " LA ACTIVIDAD ES:"+appState.getActividad());
		if(actividadAnterior==2){
			Log.i(TAG, module + "Viene de Solicitud");
			Intent cancela = getIntent();
			cancela.putExtra("cancela", "cancela");
			setResult(RESULT_OK, cancela);
		}
		else if(actividadAnterior==0){
			Intent login = new Intent();
			login.setClass(getApplicationContext(), AMapaUpVersion.class);
			startActivity(login);
		}
		finish();
		
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Presiono Boton Cancelar en Registrar Tarjeta");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	//*******************************************************************************************	
	public void EscanearTarjeta(View v){
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Presiono Boton Escanear Tarjeta");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
		ScanearT();
	}

	//*******************************************************************************************
	public void InscribirTarjeta(){
		String colocaPais="";
		if(appState.getCiudadPais().contains("COLOMBIA"))	colocaPais="CO";
		else if(appState.getCiudadPais().contains("MEXICO"))	colocaPais="MX";
		else colocaPais="PA";
		String prueba_com = "insertarTarjeta|"+colocaPais+"|"+appState.getCorreoUsuario()+"|{\"nombre\":\""+nombreUsuario
				+"\",\"codigo\":\"\",\"id\":\"\",\"direccion\":\"\",\"imagen\":\"\",\"numero_tarjeta\":\""+numeroTarjeta+"\",\"vencimiento\":\""+mesVencimiento
				+anoVencimiento+"\",\"franquicia\":\""+franquicia+"\",\"usuario\":\"\",\"codigo_seguridad\":\""+cvv+"\"}";
		//String prueba_com = "{\"test\": true, \"language\": \"es\", \"command\": \"CREATE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"creditCardToken\": {\"payerId\": \"10\", \"name\": \"full name\", \"identificationNumber\": \"1234567890\", \"paymentMethod\": \"VISA\", \"number\": \"4111111111111111\", \"expirationDate\": \"2015/02\" }}";
		//String prueba_com = "{\"language\": \"es\", \"command\": \"GET_TOKENS\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"creditCardTokenInformation\": {\"payerId\": \"10\", \"creditCardTokenId\": null}}";
		//String prueba_com = "{\"language\": \"es\", \"command\": \"REMOVE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"removeCreditCardToken\": {\"payerId\": \"10\", \"creditCardTokenId\": \"1d1767e0-44c4-435b-b20f-7f57e5958df3\"}}";
		//String prueba_com = "{\"language\":\"es\", \"command\":\"SUBMIT_TRANSACTION\", \"merchant\":{\"apiLogin\":\"APILOGIN\", \"apiKey\":\"APIKEY\"}, \"transaction\":{\"order\":{\"accountId\":\"500537\", \"referenceCode\":\"pruebaJCD13\", \"description\":\"584716|Test order Colombia\", \"language\":\"es\", \"signature\":\"SIGNATURE\", \"shippingAddress\":{\"country\":\"CO\"}, \"buyer\":{\"fullName\": \"MARITZA M. HERNANDEZ\",\"emailAddress\":\"jdelgadop@taxislibres.com.co\"}, \"additionalValues\":{\"TX_VALUE\":{\"value\":\"60\", \"currency\":\"USD\"}}},\"creditCardTokenId\":\"aca09507-7485-45e0-8fb4-f7d3d7a905c2\", \"type\":\"AUTHORIZATION_AND_CAPTURE\", \"paymentMethod\":\"VISA\",	\"paymentCountry\":\"CO\", \"extraParameters\":{\"INSTALLMENTS_NUMBER\":1}}, \"test\":true}";

		//appState.setComandoPayu("PING");
		//appState.setComandoPayu("CREATE_TOKEN");
		//appState.setComandoPayu("GET_TOKENS");
		//appState.setComandoPayu("REMOVE_TOKEN");
		//appState.setComandoPayu("SUBMIT_TRANSACTION");
		appState.setComandoPayu("INSERTAR_TARJETA");
		Intent service = new Intent();
		service.putExtra("CMD", C.ENCRIPTADO);
		service.putExtra("DATA", prueba_com);
		service.setAction(C.COM);
		getApplicationContext().sendBroadcast(service);

		esperaRta = new ProgressDialog(ARegistrarTarjeta.this);

		esperaRta.setTitle("TARJETA DE CREDITO");
		esperaRta.setMessage("REALIZANDO REGISTRO ESPERE UN MOMENTO... ");
		esperaRta.setCancelable(true);
		esperaRta.show();
	}
	//*******************************************************************************************	
	public void RegistroTarjeta(View v){
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Presiono Boton Registrar Tarjeta");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
		EnviarInfoTarjeta();
	}
	//*******************************************************************************************
	public void EnviarInfoTarjeta(){
		nombreUsuario = nombreUsuarioTarjeta.getText().toString();
		numeroTarjeta = numeroTarjetaUsuario.getText().toString();
		anoVencimiento = anoVence.getText().toString();
		mesVencimiento = mesVence.getText().toString();
		cvv= codigoSeguridad.getText().toString();
		
		//Log.i(TAG, module + "SE SELECCIONO: "+ selectorTarjeta.getLastVisiblePosition());
		
		//String franquicia="";
//		if(selectorTarjeta.getLastVisiblePosition()==0)	franquicia="\""+"VISA"+"\"";	
//		else if(selectorTarjeta.getLastVisiblePosition()==1)	franquicia="\""+"MASTERCARD"+"\"";
//		else if(selectorTarjeta.getLastVisiblePosition()==2)	franquicia="\""+"AMEX"+"\"";
//		else if(selectorTarjeta.getLastVisiblePosition()==3)	franquicia="\""+"DINERS"+"\"";
//		if(selectorTarjeta.getLastVisiblePosition()==0)	franquicia="1";	
//		else if(selectorTarjeta.getLastVisiblePosition()==1)	franquicia="2";
//		else if(selectorTarjeta.getLastVisiblePosition()==2)	franquicia="3";
//		else if(selectorTarjeta.getLastVisiblePosition()==3)	franquicia="4";
		
//		if((nombreUsuario.equals(""))||(identificacionIngresada.equals(""))||(numeroTarjeta.equals(""))||
//		(anoVencimiento.equals(""))||(mesVencimiento.equals(""))){	//Se Valida que el usuario Ingrese información completa
		if((nombreUsuario.equals(""))||(numeroTarjeta.equals(""))||
		(anoVencimiento.equals(""))||(mesVencimiento.equals(""))){	//Se Valida que el usuario Ingrese información completa
			Toast.makeText(getApplicationContext(), "POR FAVOR INGRESE SUS DATOS ", 5000).show();
		}else{
			if((numeroTarjeta.length()<17)&&(numeroTarjeta.length()>13)){
				Log.i(TAG, module + " LONGITUD MES VENCIMIENTO: "+mesVencimiento.length());
				//if(Integer.parseInt(mesVencimiento)>12){
				if((Integer.parseInt(mesVencimiento)>12)||(mesVencimiento.length()<2)||(anoVencimiento.length()<2)){
					if(Integer.parseInt(mesVencimiento)>12){
						Toast.makeText(getApplicationContext(), "EL MES NO PUEDE SER MAYOR A 12", 5000).show();
					}else if(mesVencimiento.length()<2){
						Toast.makeText(getApplicationContext(), "EL MES DEBE TENER 2 DIGITOS", 5000).show();
					}else{
						Toast.makeText(getApplicationContext(), "EL AÑO DEBE TENER 2 DIGITOS", 5000).show();
					}
				}else{
					if(cvv.length()<3){
						Toast.makeText(getApplicationContext(), "EL CODIGO DE SEGURIDAD ES DEMASIADO CORTO", 5000).show();
					}
					else{
						if(franquicia.equals("0")){
							Toast.makeText(getApplicationContext(), "DEBE SELECCIONAR TIPO DE TARJETA", 5000).show();
						}else{
							//String prueba_com = "{\"test\": true, \"language\": \"es\", \"command\": \"PING\", \"merchant\": { \"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\" }}";
							//String prueba_com = "{\"test\": true, \"language\": \"es\", \"command\": \"CREATE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"creditCardToken\": {\"payerId\": \"10\", \"name\": \"full name\", \"identificationNumber\": \"32144457\", \"paymentMethod\": \"VISA\", \"number\": \"4111111111111111\", \"expirationDate\": \"2015/02\" }}";

							//OJO HAY QUE MODIFICAR EL PAYER ID, con este se debe consultar los tokens.
							//String payerId= "\""+appState.getNombreUsuario()+appState.getNumberPhone()+"\"";	//Posible Payer Id

							//						String payerId= "";
							//						if(appState.getPayerIdTc().equals("NoHay")){
							//							String fecha = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
							//							fecha =fecha.replaceAll(":","-");
							//							fecha =fecha.replaceAll(",","-");
							//							fecha =fecha.replaceAll("/","-");
							//							payerId= "\""+appState.getNumberPhone()+fecha+"\"";
							//							appState.setPayerIdTc(payerId);
							//						}else{
							//							payerId=appState.getPayerIdTc();
							//						}	
							//
							//						//String parte1= "{\"test\": true, \"language\": \"es\", \"command\": \"CREATE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"creditCardToken\": {\"payerId\": \"10\", \"name\": ";
							//						String parte1= "{\"test\": true, \"language\": \"es\", \"command\": \"CREATE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"creditCardToken\": {\"payerId\": "+payerId+", \"name\": ";
							//						//String parte2= ", \"identificationNumber\": \""+identificacionIngresada+"\", \"paymentMethod\": ";
							//						String parte2= ", \"identificationNumber\": \""+appState.getNumberPhone()+"\", \"paymentMethod\": ";
							//						String parte3= ", \"number\":"; 
							//						String parte4= ", \"expirationDate\": ";
							//						String parte5= " }}";	
							//						String prueba_com =parte1+"\""+nombreUsuario+"\""+parte2+franquicia+parte3+"\""+numeroTarjeta+"\""+parte4+"\""+"20"+anoVencimiento+"/"+mesVencimiento+"\""+parte5;
							
//							if(appState.isPedirClaveElectronicos()){	//No hay Clave de Medios Electrónicos
//								//Hay que pedir la clave para recuperar la info de tarjetas...
//								//RecuperarDatos();
//								Intent i = new Intent(ARegistrarTarjeta.this, APedirClaveElectronicos.class);
//								startActivityForResult(i, 1);
//							}
//							else if(!appState.isTengoClaveElectronicos()){	//No hay Clave de Medios Electrónicos
//								//IngresarClaveElectronicos();
//								Intent i = new Intent(ARegistrarTarjeta.this, ARegistrarClaveElectronicos.class);
//								startActivityForResult(i, 1);
						if(appState.getPayerIdTc().contains("NoHay")){	//Es la Primera Tarjeta a Insertar, no tiene Id Modipay
							//Enviar Registro de Usuario a Juan D
							String colocaPais="";
							clave2="1234";
							if(appState.getCiudadPais().contains("COLOMBIA"))	colocaPais="CO";
							else if(appState.getCiudadPais().contains("MEXICO"))	colocaPais="MX";
							else colocaPais="PA";
							String realizar_registro="registrarMiembro|"+colocaPais+"|{\"numCelular\":\""+appState.getNumberPhone()+
							"\",\"correo\":\""+appState.getCorreoUsuario()+"\",\"cedula\":\"\"}|"+clave2;
	
							appState.setComandoPayu("REGISTAR_MIEMBRO_CLAVE");
	
							Intent envia_registro = new Intent();
							envia_registro.putExtra("CMD", C.ENCRIPTADO);
							envia_registro.putExtra("DATA", realizar_registro);
							envia_registro.setAction(C.COM);
							getApplicationContext().sendBroadcast(envia_registro);
	
							esperaRta = new ProgressDialog(ARegistrarTarjeta.this);
	
							esperaRta.setTitle("INSCRIBIENDO POR PRIMERA VEZ EN EL SISTEMA");
							esperaRta.setMessage("REALIZANDO REGISTRO ESPERE UN MOMENTO... ");
							esperaRta.setCancelable(true);
							esperaRta.show();
							
						}else{
							InscribirTarjeta();
							}
						}
					}
				}
	            
//	            if(basicResponse.getCode().equals("SUCCESS")){
//	            	Log.i(TAG, module+"ENTRO AL IF");
//	            	Toast.makeText(getApplicationContext(), "TARJETA REGISTRADA CORRECTAMENTE", 5000).show();
//	            	Intent login = new Intent();
//			        login.setClass(getApplicationContext(), AMapaUpVersion.class);
//			        startActivity(login);
//					finish();
//	            }else{
//	            	Toast.makeText(getApplicationContext(), "TARJETA NO SE REGISTRO CORRECTAMENTE", 5000).show();
//	            }
			}else{
				Toast.makeText(getApplicationContext(), "NUMERO DE TARJETA INCOMPLETO", 5000).show();
			}
		}
	}
	//*****************************************************
	public void SeleccionarVisa(){
		tarjetaAmerican.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaDiners.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaMaster.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaVisa.setBackgroundResource(R.drawable.fondoselectortrue);
		iconoAmerican.setImageResource(R.drawable.iconoamerican);
		iconoDiners.setImageResource(R.drawable.iconodiners);
		iconoMaster.setImageResource(R.drawable.iconomaster);
		iconoVisa.setImageResource(R.drawable.iconocolorvisa);
		franquicia="1";
	}
	//*****************************************************
	public void SeleccionarMaster(){
		tarjetaAmerican.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaDiners.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaMaster.setBackgroundResource(R.drawable.fondoselectortrue);
		tarjetaVisa.setBackgroundResource(R.drawable.fondoselectorfalse);
		iconoAmerican.setImageResource(R.drawable.iconoamerican);
		iconoDiners.setImageResource(R.drawable.iconodiners);
		iconoMaster.setImageResource(R.drawable.iconocolormaster);
		iconoVisa.setImageResource(R.drawable.iconovisa);
		franquicia="2";
	}
	//*****************************************************
	public void SeleccionarDiners(){
		tarjetaAmerican.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaDiners.setBackgroundResource(R.drawable.fondoselectortrue);
		tarjetaMaster.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaVisa.setBackgroundResource(R.drawable.fondoselectorfalse);
		iconoAmerican.setImageResource(R.drawable.iconoamerican);
		iconoDiners.setImageResource(R.drawable.iconocolordiners);
		iconoMaster.setImageResource(R.drawable.iconomaster);
		iconoVisa.setImageResource(R.drawable.iconovisa);
		franquicia="4";
	}
	//*****************************************************
	public void SeleccionarAmerican(){
		tarjetaAmerican.setBackgroundResource(R.drawable.fondoselectortrue);
		tarjetaDiners.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaMaster.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaVisa.setBackgroundResource(R.drawable.fondoselectorfalse);
		iconoAmerican.setImageResource(R.drawable.iconocoloramerican);
		iconoDiners.setImageResource(R.drawable.iconodiners);
		iconoMaster.setImageResource(R.drawable.iconomaster);
		iconoVisa.setImageResource(R.drawable.iconovisa);
		franquicia="3";		
	}
	//*****************************************************
	public void SinFranquicia(){
		tarjetaAmerican.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaDiners.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaMaster.setBackgroundResource(R.drawable.fondoselectorfalse);
		tarjetaVisa.setBackgroundResource(R.drawable.fondoselectorfalse);
		iconoAmerican.setImageResource(R.drawable.iconoamerican);
		iconoDiners.setImageResource(R.drawable.iconodiners);
		iconoMaster.setImageResource(R.drawable.iconomaster);
		iconoVisa.setImageResource(R.drawable.iconovisa);
		franquicia="0";		
	}
	//*******************************************************************************************
//	public void IngresarClaveElectronicos(){
//
//		Log.i(TAG, module + "Leer Clave para Pagos Electronicos");
//				
//			LayoutInflater factory = LayoutInflater.from(this);  
//			final View textEntryView = factory.inflate(R.layout.leer_vale, null);
//			
//			final TextView tituloCodigo;
//			tituloCodigo=(TextView) textEntryView.findViewById(R.id.titulovales);
//			
//			tituloCodigo.setText("REGISTAR CLAVE PARA PAGOS ELECTRONICOS");
//			
//			final EditText boxClave;
//			boxClave = (EditText) textEntryView.findViewById(R.id.numerovale);
//			boxClave.setHint("Ingrese su clave");
//						
//
//			Builder builderClave = new AlertDialog.Builder(this);
//			// Get the layout inflater				
//			LayoutInflater inflater = this.getLayoutInflater();
//
//			builderClave.setView(inflater.inflate(R.layout.menu_mas, null)).setTitle("").setView(textEntryView);
//
//			builderClave.setPositiveButton("ACEPTAR", new OnClickListener() {
//				@SuppressWarnings("deprecation")
//				public void onClick(DialogInterface dialog, int id) {
//					Log.i(TAG, module + "ENTRO X OK");
//					
//					if(boxClave.getText().toString().equals("")){
//						Toast.makeText(getApplicationContext(), "Por favor digite Clave", Toast.LENGTH_LONG).show();
//						IngresarClaveElectronicos();
//					}else{
//						clave1=boxClave.getText().toString();
//						ConfirmarClave();
//					}
//				}
//			});
//
//			builderClave.show();
//	
//	}
	//*******************************************************************************************
//	public void ConfirmarClave(){
//
//		Log.i(TAG, module + "Confirmar Clave Medios Electronicos");
//				
//			LayoutInflater factory = LayoutInflater.from(this);  
//			final View textEntryView = factory.inflate(R.layout.leer_vale, null);
//			
//			final TextView tituloCodigo;
//			tituloCodigo=(TextView) textEntryView.findViewById(R.id.titulovales);
//			
//			tituloCodigo.setText("CONFIRME SU CLAVE");
//			
//			final EditText boxClave2;
//			boxClave2 = (EditText) textEntryView.findViewById(R.id.numerovale);
//			boxClave2.setHint("Ingrese su clave");
//						
//
//			Builder builderClave = new AlertDialog.Builder(this);
//			// Get the layout inflater				
//			LayoutInflater inflater = this.getLayoutInflater();
//
//			builderClave.setView(inflater.inflate(R.layout.menu_mas, null)).setTitle("").setView(textEntryView);
//
//			builderClave.setPositiveButton("ACEPTAR", new OnClickListener() {
//				@SuppressWarnings("deprecation")
//				public void onClick(DialogInterface dialog, int id) {
//					Log.i(TAG, module + "ENTRO X OK");
//					clave2=boxClave2.getText().toString();
//					if(clave1.equals(clave2)){
//						Log.i(TAG, module + "Las Claves Coinciden...");
//						//Enviar Registro de Usuario a Juan D
//						String colocaPais="";
//						if(appState.getCiudadPais().contains("COLOMBIA"))	colocaPais="CO";
//						else if(appState.getCiudadPais().contains("MEXICO"))	colocaPais="MX";
//						else colocaPais="PA";
//						String realizar_registro="registrarMiembro|"+colocaPais+"|{\"numCelular\":\""+appState.getNumberPhone()+
//						"\",\"correo\":\""+appState.getCorreoUsuario()+"\",\"cedula\":\"\"}|"+clave2;
//
//						appState.setComandoPayu("REGISTAR_MIEMBRO_CLAVE");
//
//						Intent envia_registro = new Intent();
//						envia_registro.putExtra("CMD", C.ENCRIPTADO);
//						envia_registro.putExtra("DATA", realizar_registro);
//						envia_registro.setAction(C.COM);
//						getApplicationContext().sendBroadcast(envia_registro);
//
//						esperaRta = new ProgressDialog(ARegistrarTarjeta.this);
//
//						esperaRta.setTitle("REGISTRANDO CLAVE ELECTRONICA");
//						esperaRta.setMessage("REALIZANDO REGISTRO ESPERE UN MOMENTO... ");
//						esperaRta.setCancelable(true);
//						esperaRta.show();
//					}else{
//						Toast.makeText(getApplicationContext(), "LA CLAVE NO COINCIDE,INGRESE NUEVAMENTE LA CLAVE", 1000).show();
//						IngresarClaveElectronicos();
//					}
//				}
//			});
//
//			builderClave.show();
//	
//	}
	//*******************************************************************************************
//	public void RecuperarDatos(){
//
//		Log.i(TAG, module + "Leer Clave para Pagos Electronicos");
//				
//			LayoutInflater factory = LayoutInflater.from(this);  
//			final View textEntryView = factory.inflate(R.layout.leer_vale, null);
//			
//			final TextView tituloCodigo;
//			tituloCodigo=(TextView) textEntryView.findViewById(R.id.titulovales);
//			
//			tituloCodigo.setText("INGRESE CLAVE PAGOS ELECTRONICOS");
//			
//			final EditText boxClave;
//			boxClave = (EditText) textEntryView.findViewById(R.id.numerovale);
//			boxClave.setHint("Ingrese su clave");
//						
//
//			Builder builderClave = new AlertDialog.Builder(this);
//			// Get the layout inflater				
//			LayoutInflater inflater = this.getLayoutInflater();
//
//			builderClave.setView(inflater.inflate(R.layout.menu_mas, null)).setTitle("").setView(textEntryView);
//
//			builderClave.setPositiveButton("ACEPTAR", new OnClickListener() {
//				@SuppressWarnings("deprecation")
//				public void onClick(DialogInterface dialog, int id) {
//					Log.i(TAG, module + "ENTRO X OK");
//					
//					if(boxClave.getText().toString().equals("")){
//						Toast.makeText(getApplicationContext(), "Por favor digite Clave", Toast.LENGTH_LONG).show();
//						RecuperarDatos();
//					}else{
//						String enviarPais="";
//						if(appState.getCiudadPais().contains("COLOMBIA"))	enviarPais="CO";
//						else if(appState.getCiudadPais().contains("MEXICO"))	enviarPais="MX";
//						else if(appState.getCiudadPais().contains("PANAMA"))	enviarPais="PA";
//
//						String realizar_consulta= "accesarMiembro|"+enviarPais+"|"+appState.getCorreoUsuario()+"|"+boxClave.getText().toString();
//						appState.setComandoPayu("ACCESAR_USUARIO_REGISTRO");
//						
//
//						Intent envia_consulta = new Intent();
//						envia_consulta.putExtra("CMD", C.ENCRIPTADO);
//						envia_consulta.putExtra("DATA", realizar_consulta);
//						envia_consulta.setAction(C.COM);
//						getApplicationContext().sendBroadcast(envia_consulta);
//
//						esperaRta = new ProgressDialog(ARegistrarTarjeta.this);
//
//						esperaRta.setTitle("ENVIANDO CLAVE");
//						esperaRta.setMessage("ESPERE UN MOMENTO... ");
//						esperaRta.setCancelable(true);
//						esperaRta.show();
//					}
//					
//					
//					
//					
//					
//				}
//			});
//
//			builderClave.show();
//	
//	}
	
	//****************************************************************************************
	//public void ScanearT(View v){
	public void ScanearT(){
		
//		if(nombreUsuarioTarjeta.getText().toString().equals("")){
//			nombreUsuarioTarjeta.setError("Para Escanear: \nEs necesario escribir el nombre como Aparece en la Tarjeta");
//		}else{
			
			try {
//				Intent scanIntent = new Intent(this, CardIOActivity.class);
//				
//				scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
//				scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
//				scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
//				
//				scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false);
//				
//				startActivityForResult(scanIntent, 100);

				
				// This method is set up as an onClick handler in the layout xml
		        // e.g. android:onClick="onScanPress"

		        Intent scanIntent = new Intent(ARegistrarTarjeta.this, CardIOActivity.class);

		        // required for authentication with card.io
		        //scanIntent.putExtra(CardIOActivity.EXTRA_APP_TOKEN, "app_id");

		        // customize these values to suit your needs.
		        //scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: true
		        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false); // default: true
		        //scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
		        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
		        //scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_ZIP, false); // default: false

		        // hides the manual entry button
		        // if set, developers should provide their own manual entry mechanism in the app
		        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false
		        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION,true);

		        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
		        startActivityForResult(scanIntent, 100);
				
				
				
				
				
				
			} catch (Exception e) {
				Log.i(TAG, module + "***************ERROR AL SCANEAR TARJETA**********  " );
				e.printStackTrace();
			}
			
			
//		}
		
		
		
	}
	
	//*******************************************************************************************
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, module + " Entra por onDestroy");
		super.onDestroy();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(TAG, module + " Entra por onPause");
		super.onPause();
		try {
			unregisterReceiver(receiver3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, module + " Entra por onStart");
		registerReceiver(receiver3, filter3);
		appState.setActividad(module);
		GoogleAnalytics.getInstance(ARegistrarTarjeta.this).reportActivityStart(this);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(TAG, module + "Entra por onStop");
		super.onStop();
		GoogleAnalytics.getInstance(ARegistrarTarjeta.this).reportActivityStop(this);
	}
	//****************************************************************************************
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//			switch(keyCode){
//				case KeyEvent.KEYCODE_BACK:
//					Intent cancela = getIntent();
//					cancela.putExtra("cancela", "cancela");
//					setResult(RESULT_OK, cancela);
//					
//					finish();
//					return true;
//				
//			}
//			return super.onKeyDown(keyCode, event);
//	}
	//****************************************************************************************
	private void listenerEditText(EditText editText){

		   editText.addTextChangedListener(new TextWatcher() {
		      @Override
		      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		    	  Log.i(TAG, module + "Entra x beforeTextChanged");
		      }

		      @Override
		      public void onTextChanged(CharSequence s, int start, int before, int count) {
		         //Aca se revisan los cambios en el editText
		    	  Log.i(TAG, module + "Entra x onTextChanged"+numeroTarjetaUsuario.getText().toString().length());
		    	  if(numeroTarjetaUsuario.getText().toString().length() == 0){
		    		  Log.i(TAG, module + "No hay numero de Tarjeta de Credito Digitado");
		    		  SinFranquicia();
		    	  }
//		  	    String tipoTarjeta[]={"VISA","MASTER CARD","AMERICAN EXPRESS","DINERS"};
//		  	    American 	34,37
//		  	    Diners		300-305,309,36,38-39
//		  	    Master		51-55
//		  	    Visa		4
		    	  else if(numeroTarjetaUsuario.getText().toString().length() == 1){
		    		  if(numeroTarjetaUsuario.getText().toString().equals("4")){
		    			  Log.i(TAG, module + "Tarjeta es Visa");
		    			  SeleccionarVisa();
		    		  }else{
		    			  SinFranquicia();
		    		  }
		    	  }
		    	  else if(numeroTarjetaUsuario.getText().toString().length() == 2){
		    		  String dosnumeros = numeroTarjetaUsuario.getText().toString();
		    		  if(dosnumeros.equals("34") || dosnumeros.equals("37")){
		    			  Log.i(TAG, module + "Tarjeta es American");
		    			  SeleccionarAmerican();
		    		  }else if(dosnumeros.equals("36")){
		    			  Log.i(TAG, module + "Tarjeta es Diners");
		    			  SeleccionarDiners();
		    		  }else if(dosnumeros.equals("51") || dosnumeros.equals("52")||dosnumeros.equals("53") || dosnumeros.equals("54") || dosnumeros.equals("55")){
		    			  Log.i(TAG, module + "Tarjeta es Master");
		    			  SeleccionarMaster();
		    		  }
		    	  }
		      }

		      
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.i(TAG, module + "Entra x afterTextChanged");
				
			}
		   });
		}
	//****************************************************************************************
	@SuppressWarnings("unused")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		Log.i(TAG, module + "ENTRA A onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		registerReceiver(receiver3, filter3);
		appState.setActividad(module);
		Log.i(TAG, module + "Paso el Super");
		if (data == null){	//Hay que validar antes que nada que data no este null... 
			Log.i(TAG, module +"USUARIO CANCELA EL ESCANEO");
			//resultStr = "Se Cancela el Scaneo";
		}
		
		else if(data.getExtras().containsKey("enviarClaveElectronicos")){
			
			String enviarPais="";
			if(appState.getCiudadPais().contains("COLOMBIA"))	enviarPais="CO";
			else if(appState.getCiudadPais().contains("MEXICO"))	enviarPais="MX";
			else if(appState.getCiudadPais().contains("PANAMA"))	enviarPais="PA";

			String realizar_consulta= "accesarMiembro|"+enviarPais+"|"+appState.getCorreoUsuario()+"|"+data.getStringExtra("enviarClaveElectronicos");
			appState.setComandoPayu("ACCESAR_USUARIO_REGISTRO");
			

			Intent envia_consulta = new Intent();
			envia_consulta.putExtra("CMD", C.ENCRIPTADO);
			envia_consulta.putExtra("DATA", realizar_consulta);
			envia_consulta.setAction(C.COM);
			getApplicationContext().sendBroadcast(envia_consulta);

			esperaRta = new ProgressDialog(ARegistrarTarjeta.this);

			esperaRta.setTitle("ENVIANDO CLAVE");
			esperaRta.setMessage("ESPERE UN MOMENTO... ");
			esperaRta.setCancelable(true);
			esperaRta.show();
		
		}
		//****************************************************************************************
		else if(data.getExtras().containsKey("registrarClaveElectronicos")){

			Log.i(TAG, module + "Las Claves Coinciden...");
			//Enviar Registro de Usuario a Juan D
			String colocaPais="";
			if(appState.getCiudadPais().contains("COLOMBIA"))	colocaPais="CO";
			else if(appState.getCiudadPais().contains("MEXICO"))	colocaPais="MX";
			else colocaPais="PA";
			String realizar_registro="registrarMiembro|"+colocaPais+"|{\"numCelular\":\""+appState.getNumberPhone()+
			"\",\"correo\":\""+appState.getCorreoUsuario()+"\",\"cedula\":\"\"}|"+data.getStringExtra("registrarClaveElectronicos");

			appState.setComandoPayu("REGISTAR_MIEMBRO_CLAVE");

			Intent envia_registro = new Intent();
			envia_registro.putExtra("CMD", C.ENCRIPTADO);
			envia_registro.putExtra("DATA", realizar_registro);
			envia_registro.setAction(C.COM);
			getApplicationContext().sendBroadcast(envia_registro);

			esperaRta = new ProgressDialog(ARegistrarTarjeta.this);

			esperaRta.setTitle("REGISTRANDO CLAVE ELECTRONICA");
			esperaRta.setMessage("REALIZANDO REGISTRO ESPERE UN MOMENTO... ");
			esperaRta.setCancelable(true);
			esperaRta.show();
		
			
		}
				
		//****************************************************************************************
		else{
			Log.i(TAG, module +"ENTRA AL RESULTADO DEL ESCANEO");
			try{
			String resultStr;

			String vencimiento=null;
			String mes=null;
			String ano=null;
			String numerotarjeta = null;
			String cvv= null;

			if(data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)){
				CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

				resultStr = "Numero Tarjeta: " + scanResult.getRedactedCardNumber()  + scanResult.getCardType() +  scanResult.getFormattedCardNumber() +  "     " + scanResult.getClass();
				numerotarjeta= "" + scanResult.getFormattedCardNumber();
				numerotarjeta = numerotarjeta.replaceAll(" ", "");


				if(scanResult.isExpiryValid()){
					resultStr += "Vencimiento: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
					//vencimiento =""+ scanResult.expiryMonth  + scanResult.expiryYear;	

					mes = ""+ scanResult.expiryMonth;
					if(mes.length()==1) mes = "0" + mes;

					ano = ""+scanResult.expiryYear;
					ano = ano.substring(2,4);
					vencimiento =  mes  + ano;

				}

				if(scanResult.cvv != null){
					resultStr += "CVV: " + scanResult.cvv.length() + "digitos.\n"; 
					cvv = "" + scanResult.cvv;
				}


				String tipoTarjeta = "" + scanResult.getCardType();
				Log.d(TAG, tipoTarjeta);


				if(tipoTarjeta.equals("MasterCard"))franquicia="2";
				if(tipoTarjeta.equals("Visa"))franquicia="1";
				if(tipoTarjeta.equals("AmEx"))franquicia="3";
				if(tipoTarjeta.equals("Diners"))franquicia="4";

				if(resultStr.equals("Se Cancela el Scane")){

				}else{
					SaveCard(numerotarjeta, mes,ano, cvv,franquicia);
					
					
				} 

			}else if (data == null){
				Log.i(TAG, module +"USUARIO CANCELA EL ESCANEO");
				//resultStr = "Se Cancela el Scaneo";
			}

			// Log.i(TAG, "Lista Tarjetas  " +  resultStr +  "   "  + franquisia);
//			if(resultStr.equals("Se Cancela el Scane")){
//
//			}else{
//				SaveCard(numerotarjeta, mes,ano, cvv,franquicia);
//				
//				
//			} 
			}catch (Exception e) {
				e.printStackTrace();
				Log.i(TAG, module +"RETORNA DE CARDIO, NO SE PUDO REALIZAR EL ESCANEO");
				Toast.makeText(getApplicationContext(), "EL INTENTO DE ESCANEO FALLO, INGRESA TUS DATOS MANUALMENTE", 1000).show();
			}
		}
	}
	//****************************************************************************************

	private void SaveCard(String numerotarjeta, String mes, String ano,String cvv, String tipotarjeta) {
		Log.i(TAG, module + "SE DEBE COLOCAR: NUMTARJETA: "+numerotarjeta+"VENCE: "+mes +"/" + ano+"CVV: "+cvv+"TARJETA: "+tipotarjeta);
		numeroTarjetaUsuario.setText(numerotarjeta);	//Colocar Numero de Tarjeta...
		anoVence.setText(ano);
		mesVence.setText(mes);
		codigoSeguridad.setText(cvv);
		if(tipotarjeta=="1"){
			SeleccionarVisa();
		}else if(tipotarjeta=="2"){
			SeleccionarMaster();
		}else if(tipotarjeta=="3"){
			SeleccionarAmerican();
		}else if(tipotarjeta=="4"){
			SeleccionarDiners();
		}else{
			SinFranquicia();
		}
		
		EnviarInfoTarjeta();
		
	}
	
	

}
