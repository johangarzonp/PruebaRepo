package com.cotech.taxislibres.activities;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.TaxisLi.TrackerName;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AActualizarDatos extends Activity {

	private static final String TAG = "TaxisLibres";
	protected String module = "AActualizarDatos";
	private TaxisLi appState;
	//************************************************************
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
	//************************************************************
	private EditText nombre;
	private EditText telefono;
	private EditText correo;
	private ImageView entrar;
	private TextView textnombre;
	private TextView texttelefono;
	private TextView textcorreo;

	private Spinner selectorPais;
	//************************************************************
	private String Alto;
	private String Ancho;
	private int anchoPantalla;
	private int altoPantalla;		
	private int Densidad;
	private IntentFilter filterAct;
	private ProgressDialog esperaActualizar;
	private String nombreusuario;
	private String telefonousuario;
	private String correousuario;

	private int flagesperaRtaJuanD  = 0;

	//************************************************************
	public final BroadcastReceiver receiverAct = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, Intent intent) {									
			Integer cmd = intent.getIntExtra("CMD", 0);
			String infoRta = intent.getStringExtra("DATA");
			flagesperaRtaJuanD=0;
			switch(cmd){

			case C.ERROR_SOCKET:
				esperaActualizar.dismiss();
				Toast.makeText(context, "ERROR EN LA COMUNICACION, INTENTE NUEVAMENTE", 5000).show();
				break;

			case C.ACTUALIZACION_OK:	//Se realiza el cambio de la Informacion...
				appState.setNombreUsuario(nombreusuario);
				appState.setNumberPhone(telefonousuario);
				appState.setCorreoUsuario(correousuario);
				esperaActualizar.dismiss();
				Toast.makeText(context, "INFORMACION ACTUALIZADA CORRECTAMENTE", 5000).show();
				finish();
				break;

			case C.ACTUALIZACION_NADA:	//No Se realiza el cambio de la Informacion...
				esperaActualizar.dismiss();
				Toast.makeText(context, "INFORMACION SIN CAMBIOS,IGUAL A LA ANTERIOR", 5000).show();
				finish();
				break;

			case C.ACTUALIZACION_MAL:	//Error al actualizar info de usuario...
				esperaActualizar.dismiss();
				Toast.makeText(context, "ERROR AL ACTUALIZAR LA INFORMACION,INTENTE NUEVAMENTE", 5000).show();
				break;
			}
		}
	};

	//************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, module+": * PASA X onCreate* ");
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


		entrar = (ImageView) findViewById(R.id.entrar);		//Boton para actualizar
		entrar.setImageResource(R.drawable.botonactualizar);

		filterAct = new IntentFilter();
		filterAct.addAction(module);
		registerReceiver(receiverAct, filterAct);

		nombre.setText(appState.getNombreUsuario());
		telefono.setText(appState.getNumberPhone());
		correo.setText(appState.getCorreoUsuario());
		selectorPais.setSelection(appState.getPaisSeleccionado());
	}

	//*******************************************************************************************

	public void Entrar(View v){	
		Log.i(TAG, module+": LOGIN CLICKED");
		
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Presiono Boton Actualizar Actualizar Datos");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}

		nombreusuario = nombre.getText().toString();
		telefonousuario = telefono.getText().toString();
		correousuario = correo.getText().toString();

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
							if((cuatro.equals("1"))||(cuatro.equals("2"))||(cuatro.equals("3"))||(cuatro.equals("4"))||(cuatro.equals("5"))||(cuatro.equals("6"))
									||(cuatro.equals("7"))||(cuatro.equals("8"))||(cuatro.equals("9"))){
								//if(telefonousuario.length()==10){
								Log.i(TAG, module+": USUARIO: " + appState.getNombreUsuario() + " TELEFONO: " + appState.getNumberPhone());
								if(appState.getNombreUsuario().equals("")){

									Toast.makeText(getApplicationContext(), "POR FAVOR DIGITE SUS DATOS ", 5000).show();

								}else if(appState.getNumberPhone().equals("")){
									Toast.makeText(getApplicationContext(), "POR FAVOR DIGITE SUS DATOS ", 5000).show();
								}else{
									//Se debe enviar info del Registro
									String realizar_registro= "actualizarCliente|"+enviarPais+"|{\"perNombre\":\""+nombreusuario+
											"\",\"perApellido\":\"\",\"perIdentificacion\":\""+telefonousuario+"\",\"perEmail\":\""+correousuario+"\",\"perMovil\":\""+telefonousuario+"\"}|"+appState.getCorreoUsuario();

									appState.setComandoPayu("ACTUALIZAR_USUARIO");

									Intent envia_registro = new Intent();
									envia_registro.putExtra("CMD", C.ENCRIPTADO);
									envia_registro.putExtra("DATA", realizar_registro);
									envia_registro.setAction(C.COM);
									getApplicationContext().sendBroadcast(envia_registro);

									esperaActualizar = new ProgressDialog(AActualizarDatos.this);

									esperaActualizar.setTitle("ENVIANDO INFORMACION DE USUARIO");
									esperaActualizar.setMessage("ESPERE UN MOMENTO... ");
									esperaActualizar.setCancelable(true);
									esperaActualizar.show();

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
	public void Terminos(View v){
		try{
			Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
			t.setScreenName("Presiono Boton Terminos en Actualizar Datos");
			t.send(new HitBuilders.AppViewBuilder().build());
		}catch(Exception e){
			e.printStackTrace();
		}
		Log.i("ACTUALIZAR DATOS", "Presiono Terminos y condiciones");	
		Intent login = new Intent();
		login.setClass(getApplicationContext(), ATermyCond.class);
		startActivity(login);
		
	}
	//*******************************************************************************************
	public void EsperarRta(){
		flagesperaRtaJuanD=1;

		new Handler().postDelayed(new Runnable(){
			public void run(){

				try {
					if(flagesperaRtaJuanD==1){
						Log.i(module, "No Hay Respuesta de Juan D");
						esperaActualizar.dismiss();	
						//finish();
						Toast.makeText(getApplicationContext(), "NO FUE POSIBLE ENVIAR SU INFORMACION,INTENTE NUEVAMENTE", 5000).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}	

			};
		}, 190000);
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
		GoogleAnalytics.getInstance(AActualizarDatos.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(AActualizarDatos.this).reportActivityStop(this);
	}



}
