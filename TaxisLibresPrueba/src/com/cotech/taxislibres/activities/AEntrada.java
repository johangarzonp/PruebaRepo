package com.cotech.taxislibres.activities;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;






import com.cotech.taxislibres.TaxisLi.TrackerName;
//import android.util.Log;
import com.cotech.taxislibres.activities.Log;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import co.com.taxislibres.pojo.PayUCommon;
import co.com.taxislibres.pojo.token.BasicResponse;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.payu.PagoPayu;
import com.cotech.taxislibres.services.ServiceTCP;
import com.cotech.taxislibres.services.ServiceTTS;
import com.cotech.taxislibres.services.ServiceTimer;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


@SuppressLint("NewApi")
public class AEntrada extends Activity {

	
	private static final String TAG = "TaxisLibres";
	protected String module = "Entrada";
	private boolean initialized = false;
	private TaxisLi appState;
	private LinearLayout layoutReferencia;
	private LinearLayout layoutPrimerTitulo;
	private LinearLayout layoutNombreUsuario;
	private LinearLayout layoutSegundoTitulo;
	private LinearLayout layoutTelefonoUsuario;
	private LinearLayout layoutTercerTitulo;
	private LinearLayout layoutEmailUsuario;
	private LinearLayout layoutCuartoTitulo;
	private LinearLayout layoutEscogerPais;
	private LinearLayout layoutTerminos;
	//private LinearLayout layoutIngresar;
	private EditText nombre;
	private EditText telefono;
	private EditText correo;
	private ImageView entrar;
	private TextView textnombre;
	private TextView texttelefono;
	private TextView textcorreo;
	private int altonombre;
	private int anchonombre;
	private int altotelefono;
	private int anchotelefono;
	private int anchoentrar;
	private int altoentrar;
	private int altotextnombre;
	private int anchotextnombre;
	private int altotexttelefono;
	private int anchotexttelefono;
	
	private Spinner selectorPais;
	
	private String Alto;
	private String Ancho;
	private int anchoPantalla;
	private int altoPantalla;		
	private int Densidad;
	
	private IntentFilter filterIn;
	private ProgressDialog esperaRegistro;
	private boolean aceptoTerminos=false;

	private int flagesperaRtaJuanD  = 0;
	
	
	
	public final BroadcastReceiver receiverIn = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, Intent intent) {									
			Integer cmd = intent.getIntExtra("CMD", 0);
			String infoRta = intent.getStringExtra("DATA");
			flagesperaRtaJuanD=0;
			switch(cmd){
			case C.RESPUESTA_REGISTRO:
				esperaRegistro.dismiss();
				Log.i(TAG, module + "llego Info del Registro de Usuario");
				appState.setFlagActDatos(1);	//Cambiar estado de Flag de Registro...
				if(infoRta.contains("false")){	//Hay que hacer Registro Miembro
					Log.i("FUE FALSE", "HAY QUE INVITAR A REGISTRAR TARJETAS");
					RevisarInfoRegistro();
				}else{	//Debe retornar true y ya tiene miembro, se debe preguntar la clave...

					//Log.i("FUE TRUE", "DEBE TENER REGISTRADOS METODOS DE PAGO");
					//Colocar Ventana para pedir la Clave... Si tiene medios electrónicos
					//RecuperarInfo();
					Log.i("FUE TRUE", "ALMACENAMOS ID MODIPAY Y # DE TARJETAS...");
					appState.setPedirClaveElectronicos(false);
					appState.setTengoClaveElectronicos(true);
					String[] posiciones=infoRta.split("\\|");
					Log.i(TAG,"info: "+posiciones[3]+posiciones[4]);
					appState.setNumTarjetas(Integer.parseInt(posiciones[3]));
					appState.setPayerIdTc(posiciones[4]);
					Intent login = new Intent();
					login.setClass(getApplicationContext(), AMapaUpVersion.class);
					login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(login);
					finish();
				}
				break;

			case C.RESPUESTA_ACCESAR_USUARIO:
				esperaRegistro.dismiss();
				Log.i(TAG, module + "llego Info de Accesar Miembro");
				if(infoRta.contains("0|true")){	//Informacion correcta se recupera Info
					appState.setPedirClaveElectronicos(false);
					appState.setTengoClaveElectronicos(true);
					String[] posiciones=infoRta.split("\\|");
					//0|true|PayerId|numero de tarjetas
//					Log.i("posicion 0", posiciones[0] );
//					Log.i("posicion 1", posiciones[1] );
//					Log.i("posicion 2", posiciones[2] );
					appState.setPayerIdTc(posiciones[2]);
//					Log.i("posicion 3", posiciones[3] );
					appState.setNumTarjetas(Integer.parseInt(posiciones[3]));
					Toast.makeText(context, "CLAVE CORRECTA, INFORMACION RECUPERADA", 5000).show();
					Intent login = new Intent();
					login.setClass(getApplicationContext(), AMapaUpVersion.class);
					login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(login);
					finish();
				}else if(infoRta.contains("1|false")){	//Información Incorrecta
					Toast.makeText(context, "CLAVE INCORRECTA", 5000).show();
					VolveraIntentar();
				}else if(infoRta.contains("1|true")){
					String[] separaRta=infoRta.split("\\|");
					Log.i(TAG, module + " Vienen este numero de campos: " + separaRta.length);
					//Se supone error debe venir en la posicion 3
					Toast.makeText(context, separaRta[3], 5000).show();
					Intent login = new Intent();
					login.setClass(getApplicationContext(), AMapaUpVersion.class);
					login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(login);
					finish();
				}
				break;

			case C.ERROR_SOCKET:
				esperaRegistro.dismiss();
				Toast.makeText(context, "ERROR EN LA COMUNICACION, INTENTE NUEVAMENTE", 5000).show();
				break;
				
			
			case C.RESPUESTA_OLVIDO_CLAVE:
				esperaRegistro.dismiss();
				try {
					String[] Separa = infoRta.split("\\|");				
					AlertDialog alertDialog = new AlertDialog.Builder(AEntrada.this).create();
					alertDialog.setTitle("RESPUESTA CLAVE OLVIDADA!!!");
					alertDialog.setMessage(Separa[2]);
					alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
					    new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
					        	finish();
					        }
					    });
					alertDialog.show();
				} catch (Exception e) {
					
				}
				
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, module+": * PASA X onCreate* ");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
	    Context context = getApplicationContext();
		appState = ((TaxisLi) context);
	    setContentView(R.layout.entrada);
	    
	    
	    layoutReferencia=(LinearLayout) findViewById(R.id.layoutentrada);
	    layoutPrimerTitulo=(LinearLayout) findViewById(R.id.layouttextnombre);
	    layoutNombreUsuario=(LinearLayout) findViewById(R.id.layoutnombre);
	    layoutSegundoTitulo=(LinearLayout) findViewById(R.id.layouttexttelefono);
	    layoutTelefonoUsuario=(LinearLayout) findViewById(R.id.telefonolayout);
	    layoutTercerTitulo=(LinearLayout) findViewById(R.id.layouttextoemail);
	    layoutEmailUsuario=(LinearLayout) findViewById(R.id.emaillayout);
	    layoutCuartoTitulo=(LinearLayout) findViewById(R.id.paislayout);
	    layoutEscogerPais=(LinearLayout) findViewById(R.id.escogerpaislayout);
	    layoutTerminos=(LinearLayout) findViewById(R.id.layoutterminos);
	    //layoutIngresar=(LinearLayout) findViewById(R.id.layoutbotoningresar);
	    
	    	    
	    Log.i(TAG, module + ":+++++++++++++++  " + appState.getAnchoentrar() + "  "+ appState.getAltoentrar() ); 
	    Log.i(TAG, module + ":+++++++++++++++  " + appState.getNombreUsuario() + "  "+ appState.getNumberPhone()  + "  "  + appState.getPaisSeleccionado()); 

	    /************************************************************/
		
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
						
		Ancho= Integer.toString(display.getWidth());
        Alto=Integer.toString(display.getHeight());                      
        appState.setAnchoPantalla(Integer.parseInt(Ancho));
        appState.setAltoPantalla(Integer.parseInt(Alto));
        appState.setDensidad(getResources().getDisplayMetrics().densityDpi);
        Densidad = getResources().getDisplayMetrics().densityDpi;
        anchoPantalla = Integer.parseInt(Ancho);
        altoPantalla = Integer.parseInt(Alto);
                       
        Log.i( TAG, module+":+++++++++++ DENSIDAD+++++++: " + Densidad);
        Log.i( TAG, module+":+++++++++++ ANCHO ++++++++: " + appState.getAnchoPantalla());
        Log.i( TAG, module+":+++++++++++ ALTO +++++++++: " + appState.getAltoPantalla());
        
        if(Densidad<=240){
        	
        	layoutReferencia.setPaddingRelative(15, (appState.getAltoPantalla()/4)+(appState.getAltoPantalla()/853), 0, 0);
        	layoutPrimerTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/180), 0, 0);
        	layoutNombreUsuario.setPaddingRelative(0,(appState.getAltoPantalla()/140), 0, 0);
        	layoutSegundoTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/120), 0, 0);
        	layoutTelefonoUsuario.setPaddingRelative(0,(appState.getAltoPantalla()/140), 0, 0);
        	layoutTercerTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/120), 0, 0);
        	layoutEmailUsuario.setPaddingRelative(0,(appState.getAltoPantalla()/140), 0, 0);
        	layoutCuartoTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/120), 0, 0);
        	layoutEscogerPais.setPaddingRelative(0,(appState.getAltoPantalla()/200), 0, 0);
        	layoutTerminos.setPaddingRelative(0,(appState.getAltoPantalla()/100), 0, 0);
        	//layoutIngresar.setPaddingRelative(0,(appState.getAltoPantalla()/25), 0, 0);	
        }
        //else if(Densidad==320 || Densidad==480|| Densidad==640){
        else if(Densidad==320){
        	layoutReferencia.setPaddingRelative(60, (appState.getAltoPantalla()/4)+(appState.getAltoPantalla()/853), 0, 0);
        	layoutPrimerTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/160), 0, 0);
        	layoutNombreUsuario.setPaddingRelative(0,(appState.getAltoPantalla()/80), 0, 0);
        	layoutSegundoTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/60), 0, 0);
        	layoutTelefonoUsuario.setPaddingRelative(0,(appState.getAltoPantalla()/80), 0, 0);
        	layoutTercerTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/60), 0, 0);
        	layoutEmailUsuario.setPaddingRelative(0,(appState.getAltoPantalla()/80), 0, 0);
        	layoutCuartoTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/60), 0, 0);
        	layoutEscogerPais.setPaddingRelative(0,(appState.getAltoPantalla()/80), 0, 0);
        	layoutTerminos.setPaddingRelative(0,(appState.getAltoPantalla()/25), 0, 0);
        	//layoutIngresar.setPaddingRelative(0,(appState.getAltoPantalla()/14), 0, 0);
        }
        else{
        	if(Densidad==480)	layoutReferencia.setPaddingRelative(90, (appState.getAltoPantalla()/4)+(appState.getAltoPantalla()/853), 0, 0);
        	else if(Densidad==640)	layoutReferencia.setPaddingRelative(120, (appState.getAltoPantalla()/4)+(appState.getAltoPantalla()/853), 0, 0); 
        	layoutPrimerTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/160), 0, 0);
        	layoutNombreUsuario.setPaddingRelative(0,(appState.getAltoPantalla()/80), 0, 0);
        	layoutSegundoTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/60), 0, 0);
        	layoutTelefonoUsuario.setPaddingRelative(0,(appState.getAltoPantalla()/80), 0, 0);
        	layoutTercerTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/60), 0, 0);
        	layoutEmailUsuario.setPaddingRelative(0,(appState.getAltoPantalla()/80), 0, 0);
        	layoutCuartoTitulo.setPaddingRelative(0,(appState.getAltoPantalla()/60), 0, 0);
        	layoutEscogerPais.setPaddingRelative(0,(appState.getAltoPantalla()/80), 0, 0);
        	layoutTerminos.setPaddingRelative(0,(appState.getAltoPantalla()/50), 0, 0);
        	//layoutIngresar.setPaddingRelative(0,(appState.getAltoPantalla()/20), 0, 0);
        }
        
	    textnombre=(TextView) findViewById(R.id.textnombre);
	    nombre = (EditText)  findViewById(R.id.nombre);
	    nombre.setMaxWidth(nombre.getWidth());
	    
	    texttelefono=(TextView) findViewById(R.id.texttelefono);
	    telefono = (EditText)  findViewById(R.id.telefono);
	    telefono.setMaxWidth(telefono.getWidth());
	    
	    textcorreo=(TextView) findViewById(R.id.textoemail);
	    correo = (EditText)  findViewById(R.id.emailusuario);
	    correo.setMaxWidth(correo.getWidth());
	    
	    String seleccionePais[]={"Seleccione...","Colombia","Mexico","Panama"};
		selectorPais= (Spinner)findViewById(R.id.escogerpais);
		ArrayAdapter<String> spinnerPaises = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item,seleccionePais);
		spinnerPaises.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		selectorPais.setAdapter(spinnerPaises);

		    
	    entrar = (ImageView) findViewById(R.id.entrar);		//Boton para ingresar
	    
	    appState.setActividad(module);
	    
	    filterIn = new IntentFilter();
		filterIn.addAction(module);
		registerReceiver(receiverIn, filterIn);
              
	    if((!appState.getNombreUsuario().equals("Jaime Lombana"))&&(!appState.getNumberPhone().equals("3111111"))){
	    	Log.i("CON DATOS:", "EL USUARIO YA TIENE DATOS REGISTRADOS");
	    	//Aca en esta parte se verificarian Futuras Actualizaciones en datos
	    	if(appState.getFlagActDatos()==0){
	    		Log.i("CON DATOS SIN ACTUALIZAR:", "HAY QUE ACTUALIZAR DATOS");
	    		nombre.setText(appState.getNombreUsuario());
	    		telefono.setText(appState.getNumberPhone());
	    		correo.setText(appState.getCorreoUsuario());
	    		selectorPais.setSelection(appState.getPaisSeleccionado());
	    		
	    		
	    		appState.setNumFrecuentes(0); //Borra los frecuentes...
	    		
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    builder.setTitle("ACTUALIZACION DE DATOS");
			    builder.setMessage("Por tu seguridad estamos verificando tu información, si deseas modificar alguno de tus datos, cambialo en el formulario de registro y pulsa el botón de Ingresar. Agradecemos tu valiosa colaboración");
			    builder.setPositiveButton("ACEPTAR", new OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			        Log.i("Dialogos", "Confirmacion Aceptada.");		
			        
			    }
			    });
			    
			    builder.show();
	    	}else{
	    		try{
	    			Intent login = new Intent();
	    			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	    				login.setClass(getApplicationContext(), AMapa.class);
	    			}else{
	    				login.setClass(getApplicationContext(), AMapaUpVersion.class);
	    				login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	    			}	    			
	    			//login.setClass(getApplicationContext(), AFormulario.class);
	    			startActivity(login);
	    			finish();
	    		}catch (Exception e){
	    			Log.i( TAG, module+"ERROR AL PASAR AL MAPA..." + e);
	    		}
	    	}
	    }else{	//No tiene aun datos almacenados en el celular...
	    	Log.i("SIN DATOS:", "SE VERIFICA EL MAIL DEL USUARIO");
	    	AccountManager accountManager = AccountManager.get(context); 
			Account account = getAccount(accountManager);

			if (account == null) {
				Log.i("AEntrada", module+"EL USUARIO NO TIENE MAIL");
			} else {
				Log.i("AEntrada", module+"EL USUARIO TIENE EL MAIL: "+ account.name);
				correo.setText(account.name);
			}
			
			try{
				Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
				t.setScreenName("Formulario de Registro- Registro Inicial");
				t.send(new HitBuilders.AppViewBuilder().build());
			}catch(Exception e){
				e.printStackTrace();
			}
	    }	    	   
	}
	

//*******************************************************************************************
	
	public void Entrar(View v){	
		Log.i(TAG, module+": LOGIN CLICKED");
		
		String nombreusuario = nombre.getText().toString();
		String telefonousuario = telefono.getText().toString();
		String correousuario = correo.getText().toString();
						
//		appState.setNombreUsuario(nombreusuario);
//		appState.setNumberPhone(telefonousuario);
		//appState.setCorreoUsuario(correousuario);
		
		//if(nombreusuario.equals("")&&(telefonousuario.equals("")&&(correousuario.equals("")))){
		//if(nombreusuario.equals("")&&(telefonousuario.equals(""))){
		if((nombreusuario.equals(""))||(correousuario.equals(""))){	//Se Valida que el usuario Ingrese información del Nombre
			Toast.makeText(getApplicationContext(), "POR FAVOR INGRESE SUS DATOS ", 5000).show();
		}else{
			if(selectorPais.getLastVisiblePosition()==0){
				Toast.makeText(getApplicationContext(), "POR FAVOR SELECCIONE SU PAIS", 5000).show();
			}else{
				String enviarPais="";
				if(selectorPais.getLastVisiblePosition()==1){
					appState.setCiudadPais("COLOMBIA");
					appState.setPaisSeleccionado(1);
					enviarPais="CO";
				}
				else if(selectorPais.getLastVisiblePosition()==2){
					appState.setCiudadPais("MEXICO");
					appState.setPaisSeleccionado(2);
					enviarPais="MX";
				}
				else{
					appState.setCiudadPais("PANAMA");
					appState.setPaisSeleccionado(3);
					enviarPais="PA";
				}
				//if(telefonousuario.length()==10){	//Validar tamaño antes de hacer los substring...
				if((telefonousuario.length()==10)&&(appState.getCiudadPais().equals("COLOMBIA")||appState.getCiudadPais().equals("MEXICO"))
						||((telefonousuario.length()==8)&&(appState.getCiudadPais().equals("PANAMA")))){	//Validar tamaño antes de hacer los substring...
					//Aca si se debe Almacenar el telefono y nombre del Usuario...
					appState.setNombreUsuario(nombreusuario);
					appState.setNumberPhone(telefonousuario);
					appState.setCorreoUsuario(correousuario);

					String uno = telefonousuario.substring(0, 1);
					String dos = telefonousuario.substring(1, 2);
					String tres = telefonousuario.substring(2, 3);
					String cuatro = telefonousuario.substring(3, 4);
					Log.i (TAG, module+ " " + uno + " " + dos +" " + tres +" " +cuatro);
					//if(uno.equals("3")){
					if((uno.equals("3"))||(uno.equals("5"))){
						//Para Colocar propaganda de Mexico
						//if(!uno.equals("3"))	appState.setCiudadPais("MEXICO");
						Log.i (TAG, module+"PAIS: "+ appState.getCiudadPais());
						//if((dos.equals("0"))||(dos.equals("1"))||(dos.equals("2"))){
						if((dos.equals("0"))||(dos.equals("1"))||(dos.equals("2"))||(dos.equals("3"))||(dos.equals("4"))||(dos.equals("5"))){
							//if((cuatro.equals("2"))||(cuatro.equals("3"))||(cuatro.equals("4"))||(cuatro.equals("5"))||(cuatro.equals("6"))
							//||(cuatro.equals("7"))||(cuatro.equals("8"))||(cuatro.equals("9"))){
							if((cuatro.equals("0"))||(cuatro.equals("1"))||(cuatro.equals("2"))||(cuatro.equals("3"))||(cuatro.equals("4"))||(cuatro.equals("5"))||(cuatro.equals("6"))
									||(cuatro.equals("7"))||(cuatro.equals("8"))||(cuatro.equals("9"))){
								//if(telefonousuario.length()==10){
								Log.i(TAG, module+": USUARIO: " + appState.getNombreUsuario() + " TELEFONO: " + appState.getNumberPhone());
								if(appState.getNombreUsuario().equals("")){

									Toast.makeText(getApplicationContext(), "POR FAVOR DIGITE SUS DATOS ", 5000).show();

								}else if(appState.getNumberPhone().equals("")){
									Toast.makeText(getApplicationContext(), "POR FAVOR DIGITE SUS DATOS ", 5000).show();
								}else{
									//Se debe enviar info del Registro
									String realizar_registro= "registrarCliente|"+enviarPais+"|{\"perNombre\":\""+appState.getNombreUsuario()+
									"\",\"perApellido\":\"\",\"perIdentificacion\":\""+appState.getNumberPhone()+"\",\"perEmail\":\""+appState.getCorreoUsuario()+"\",\"perMovil\":\""+appState.getNumberPhone()+"\"}";
		        					
									appState.setComandoPayu("REGISTAR_USUARIO");
		        					
		        					Intent envia_registro = new Intent();
		        					envia_registro.putExtra("CMD", C.ENCRIPTADO);
		        					envia_registro.putExtra("DATA", realizar_registro);
		        					envia_registro.setAction(C.COM);
		        					getApplicationContext().sendBroadcast(envia_registro);
		        			   		 
		        			   		esperaRegistro = new ProgressDialog(AEntrada.this);
		        			   		
		        			   		esperaRegistro.setTitle("ENVIANDO INFORMACION DE USUARIO");
		        			   		esperaRegistro.setMessage("ESPERE UN MOMENTO... ");
		        			   		esperaRegistro.setCancelable(true);
		        			   		esperaRegistro.show();
		        			   		
		        			   		EsperarRta();
		        			   		
								}

							}else{
								Toast.makeText(getApplicationContext(), "EL NUMERO DE CELULAR ESTA INCORRECTO ", 5000).show();

							}
						}else{
							Toast.makeText(getApplicationContext(), "EL NUMERO DE CELULAR ESTA INCORRECTO ", 5000).show();

						}
					}else{
						Toast.makeText(getApplicationContext(), "EL NUMERO DE CELULAR ESTA INCORRECTO ", 5000).show();

					}
				}else{
					Toast.makeText(getApplicationContext(), "EL NUMERO DE CELULAR ESTA INCORRECTO ", 5000).show();
				}	
			}
		}
	
	
	}
	//*******************************************************************************************
	public void EsperarRta(){
		flagesperaRtaJuanD=1;

		new Handler().postDelayed(new Runnable(){
			public void run(){

				try {
					if(flagesperaRtaJuanD==1){
						Log.i("ENTRADA:", "No Hay Respuesta de Juan D");
						esperaRegistro.dismiss();	
						//finish();
						Toast.makeText(getApplicationContext(), "NO FUE POSIBLE ENVIAR SU INFORMACION,INTENTE NUEVAMENTE", 5000).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}	

			};
		}, 190000);
	}
//*******************************************************************************************
	
	public void Terminos(View v){
		Log.i("TERMINOS Y CONDICIONES", "Presiono boton");	
		Intent login = new Intent();
		login.setClass(getApplicationContext(), ATermyCond.class);
		startActivity(login);
		
	}
	
//*******************************************************************************************
	
	public void AceptaTerminos(View view) {
		boolean checked = ((CheckBox) view).isChecked();
		aceptoTerminos = checked;
		if(aceptoTerminos)	Log.i("LOS ACEPTO:", "Acepto");
		else	Log.i("NO LOS ACEPTO:", "No Acepto"); 
	}
	
//*******************************************************************************************
	
	private static Account getAccount(AccountManager accountManager) {
	    Account[] accounts = accountManager.getAccountsByType("com.google");
	    Account account;
	    if (accounts.length > 0) {
	      account = accounts[0];      
	    } else {
	      account = null;
	    }
	    return account;
	  }
	
//*******************************************************************************************	
	
	public void RevisarInfoRegistro(){


		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			Intent login = new Intent();
			login.setClass(getApplicationContext(), AMapa.class);
			startActivity(login);
			finish();
		}else{

			//Colocar Ventana para ingreso de Datos de Tarjeta de Credito...
			if((appState.getCiudadPais().contains("MEXICO"))||(appState.getCiudadPais().contains("COLOMBIA"))){


				AlertDialog.Builder builder = new AlertDialog.Builder(this); 
				builder.setTitle("REGISTRAR TARJETA DE CREDITO");
				builder.setMessage("¿Desea Registrar su Tarjeta de Credito? " );
				builder.setPositiveButton("SI", new OnClickListener() {


					public void onClick(DialogInterface dialog, int which) {
						Log.i("REGISTRAR DATOS TARJETA", " Va a registrar su Tarjeta");
						dialog.cancel();
						Intent login = new Intent();
						login.setClass(getApplicationContext(), ARegistrarTarjeta.class);
						startActivity(login);
						finish();	
						//											Intent i = new Intent(getApplicationContext(), ARegistrarTarjeta.class);
						//									        startActivityForResult(i, 1);

					}
				});
				builder.setNegativeButton("NO", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i("REGISTRAR DATOS TARJETA", "Registro de Tarjeta Cancelado");
						dialog.cancel();
						Intent login = new Intent();
						login.setClass(getApplicationContext(), AMapaUpVersion.class);
						login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(login);
						finish();
					}
				});

				builder.show();



			}else{
				Intent login = new Intent();
				login.setClass(getApplicationContext(), AMapaUpVersion.class);
				login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(login);
				finish();
			}
			
		}

	}
	
//*******************************************************************************************
	
	public void RecuperarInfo() {
		appState.setPedirClaveElectronicos(true);
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setTitle("RECUPERACION DE MEDIOS ELECTRONICOS");
		builder.setMessage("¿Desea Recuperar las tarjetas registradas? " );
		builder.setPositiveButton("SI", new OnClickListener() {


			public void onClick(DialogInterface dialog, int which) {
				Log.i("MIRAR CUANTAS TARJETAS TIENE", "Aca se debe hacer eso");
				dialog.cancel();
				//PedirClaveUsuario();
				Intent i = new Intent(AEntrada.this, APedirClaveElectronicos.class);
				startActivityForResult(i, 1);
			}
		});
		
		builder.setNegativeButton("NO", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i("NO QUISO RECUPERAR INFO", "Consulta Cancelada");
				dialog.cancel();
				Intent login = new Intent();
				login.setClass(getApplicationContext(), AMapaUpVersion.class);
				login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(login);
				finish();
			}
		});
		
		builder.show();
	}
	//*******************************************************************************************
	public void PedirClaveUsuario(){

		Log.i(TAG, module + "Leer Clave para Pagos Electronicos");
				
			LayoutInflater factory = LayoutInflater.from(this);  
			final View textEntryView = factory.inflate(R.layout.leer_vale, null);
			
			final TextView tituloCodigo;
			tituloCodigo=(TextView) textEntryView.findViewById(R.id.titulovales);
			
			tituloCodigo.setText("REGISTAR CLAVE PARA PAGOS ELECTRONICOS");
			
			final EditText boxClave;
			boxClave = (EditText) textEntryView.findViewById(R.id.numerovale);
			boxClave.setHint("Ingrese su clave");
						

			Builder builderClave = new AlertDialog.Builder(this);
			// Get the layout inflater				
			LayoutInflater inflater = this.getLayoutInflater();

			builderClave.setView(inflater.inflate(R.layout.menu_mas, null)).setTitle("").setView(textEntryView);

			builderClave.setPositiveButton("ACEPTAR", new OnClickListener() {
				@SuppressWarnings("deprecation")
				public void onClick(DialogInterface dialog, int id) {
					Log.i(TAG, module + "ENTRO X OK");
					
					String enviarPais="";
					if(appState.getCiudadPais().contains("COLOMBIA"))	enviarPais="CO";
					else if(appState.getCiudadPais().contains("MEXICO"))	enviarPais="MX";
					else if(appState.getCiudadPais().contains("PANAMA"))	enviarPais="PA";

					String realizar_consulta= "accesarMiembro|"+enviarPais+"|"+appState.getCorreoUsuario()+"|"+boxClave.getText().toString();
					appState.setComandoPayu("ACCESAR_USUARIO_ENTRADA");

					Intent envia_consulta = new Intent();
					envia_consulta.putExtra("CMD", C.ENCRIPTADO);
					envia_consulta.putExtra("DATA", realizar_consulta);
					envia_consulta.setAction(C.COM);
					getApplicationContext().sendBroadcast(envia_consulta);

					esperaRegistro = new ProgressDialog(AEntrada.this);

					esperaRegistro.setTitle("ENVIANDO CONSULTA");
					esperaRegistro.setMessage("ESPERE UN MOMENTO... ");
					esperaRegistro.setCancelable(true);
					esperaRegistro.show();
					
					EsperarRta();
					
				}
			});

			builderClave.show();
	
	}
	
	//*******************************************************************************************
	
	public void VolveraIntentar(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setTitle("CONTRASEÑA INCORRECTA!!!");
		builder.setMessage("¿Desea intentar nuevamente ingresar la clave? " );
		
      
		builder.setPositiveButton("Intentar", new OnClickListener() {


			public void onClick(DialogInterface dialog, int which) {
				Log.i("MIRAR CUANTAS TARJETAS TIENE", "Aca se debe hacer eso");
				dialog.cancel();
				//PedirClaveUsuario();
				Intent i = new Intent(AEntrada.this, APedirClaveElectronicos.class);
				startActivityForResult(i, 1);
			}
		});
		
		builder.setNegativeButton("Recordar", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				String recordar ="olvidoPasswd|" + appState.getCorreoUsuario();
				Intent forgot = new Intent();
				forgot.putExtra("CMD", C.ENCRIPTADO);
				forgot.putExtra("DATA", recordar);
				forgot.setAction(C.COM);
				getApplicationContext().sendBroadcast(forgot);
				
		   		 
		   		esperaRegistro = new ProgressDialog(AEntrada.this);
		   		esperaRegistro.setTitle("ENVIANDO INFORMACION DE USUARIO");
		   		esperaRegistro.setMessage("ESPERE UN MOMENTO... ");
		   		esperaRegistro.setCancelable(true);
		   		esperaRegistro.show();
		   		
		   		EsperarRta();
				//Toast.makeText(getApplicationContext(), "Se llama el metodo de Juan Delgado", Toast.LENGTH_LONG).show();
			}
		});
		builder.setNeutralButton("Cancelar", new OnClickListener() {


			public void onClick(DialogInterface dialog, int which) {
				Log.i("NO QUISO RECUPERAR INFO", "Consulta Cancelada");
				dialog.cancel();
				Intent login = new Intent();
				login.setClass(getApplicationContext(), AMapaUpVersion.class);
				login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(login);
				finish();
			}
		});
		
		builder.show();
	}
	
	//*******************************************************************************************
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, module+": * PASA X onDestroy* ");
		super.onDestroy();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data.getExtras().containsKey("enviarClaveElectronicos")){
			registerReceiver(receiverIn, filterIn);
			Log.i(TAG, module + "ENTRO X OK");
			
			String enviarPais="";
			if(appState.getCiudadPais().contains("COLOMBIA"))	enviarPais="CO";
			else if(appState.getCiudadPais().contains("MEXICO"))	enviarPais="MX";
			else if(appState.getCiudadPais().contains("PANAMA"))	enviarPais="PA";

			String realizar_consulta= "accesarMiembro|"+enviarPais+"|"+appState.getCorreoUsuario()+"|"+data.getStringExtra("enviarClaveElectronicos");
			appState.setComandoPayu("ACCESAR_USUARIO_ENTRADA");

			Intent envia_consulta = new Intent();
			envia_consulta.putExtra("CMD", C.ENCRIPTADO);
			envia_consulta.putExtra("DATA", realizar_consulta);
			envia_consulta.setAction(C.COM);
			getApplicationContext().sendBroadcast(envia_consulta);

			esperaRegistro = new ProgressDialog(AEntrada.this);

			esperaRegistro.setTitle("ENVIANDO CONSULTA");
			esperaRegistro.setMessage("ESPERE UN MOMENTO... ");
			esperaRegistro.setCancelable(true);
			esperaRegistro.show();
			
			EsperarRta();
			
		}
		//****************************************************************************************
		else if(data.getExtras().containsKey("cancela")){

		}
	}

	//****************************************************************************************

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(TAG, module+": * PASA X onPause* ");
		super.onPause();
		try {
			unregisterReceiver(receiverIn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(TAG, module+": * PASA X onResume* ");
		super.onResume();
		registerReceiver(receiverIn, filterIn);
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(TAG, module+": * PASA X onStart* ");
		super.onStart();
		GoogleAnalytics.getInstance(AEntrada.this).reportActivityStart(this);
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(TAG, module+": * PASA X onStop* ");
		super.onStop();
		GoogleAnalytics.getInstance(AEntrada.this).reportActivityStop(this);
	}
	
	
	
}