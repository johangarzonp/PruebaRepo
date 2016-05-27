package com.cotech.taxislibres.activities;


import com.mobileapptracker.*;
import com.rta.structure.payments.confirmMemberPayment.ConfirmMemberPaymentOutput;
import com.rta.structure.payments.memberPayment.MemberPaymentInputData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StatFs;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Base64;




//import android.util.Log;
import com.cotech.taxislibres.activities.Log;

import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.androidquery.service.MarketService;
import com.cotech.taxislibres.C;
import com.cotech.taxislibres.EstadosUnidad;
import com.cotech.taxislibres.GCMIntentService;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TCPSockets;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.TaxisLi.TrackerName;
import com.cotech.taxislibres.database.Handler_sqlite;
import com.cotech.taxislibres.services.ServiceTCP;
import com.cotech.taxislibres.services.ServiceTTS;
import com.cotech.taxislibres.services.ServiceTimer;
import com.cotech.taxislibres.view.viewgroup.CustomList;
import com.cotech.taxislibres.view.viewgroup.FlyOutContainer;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



@SuppressWarnings("unused")
@SuppressLint("NewApi")
public class AMapaUpVersion extends FragmentActivity implements  LocationListener{

	public MobileAppTracker mobileAppTracker = null;

	protected String APPNAME = "TaxisLibres";
	protected String module = C.MAP;
	private Context context;
	private IntentFilter filter;
	private TaxisLi appState;
	private GoogleMap map;
	private LatLng UBICACION;
	private LatLng INICIO;
	private LatLng TAXI;
	private LatLng SERVICIO;
	private int flagYoinicio=0;
	private int flagTaxiinicio=0;
	private double movillat;
	private double movillon;
	private double Lat;
	private double Lon;
	private String Barrio; 

	private ProgressDialog progDailog = null;

	private TextView direccionusuario;
	private EditText direccion;

	private int flagmapa=0;

	protected PowerManager.WakeLock wakelock;	
	private Handler handler;    

	private Marker usuario;
	private Marker ubicaciontaxi;
	private double Latsave;
	private double Lonsave;

	private int flagMarker=0;	
	private int metrosservice;
	private String CompaDirecccion="Nada de Nada";
	private String DireccionBox;
	private int contSolicitud;

	LatLng LatLng;
	LatLng latlng;
		
	private TextView avisodireccion;
	private ImageView imgusuario;
	private LinearLayout avisomarket;
	
	TCPSockets _ss ;
	String mClientMsg = "";
	Thread myCommsThread = null;
	protected static final int MSG_ID = 0x1337;
	/**************************************************************/
	///private ProgressBar pensando;
	/**************************************************************/
	private String newnombre;
	private String newtelefono;
	/**************************************************************/
	public MediaPlayer mp;
	static final int READ_BLOCK_SIZE = 200;
	private int contaviso = 0;	
	private int flagaviso = 0;
	/******************************************************************/
	private String ciudad;
	private String barrio;
	private String compldireccion;
	private EditText direccionInput;
	private EditText barrioInput;
	private int copiaNumFrec;
	private AlertDialog.Builder builderDir;
	private String ciudadPais;

	private Bitmap imagenRecibida;
	private EditText boxPropina;
	private int valorPropina;
	private boolean banderaBarra=false;

	private boolean imagenBien=false;
	Drawable imagenPropaganda =null;
	private ImageButton botonvoz;
	private boolean cargaImagen=false;
	private boolean noMostrarAsignacion=false;
	private boolean ventanaAbordo=false;
	Builder ventanasEmergentes;
	private TextView infoDireccion;
	private LinearLayout barraMetodoPagos;
	private LinearLayout flechaCursor;
	private LinearLayout barraEnSolicitud;
	private LinearLayout barraEnServicio;
	private LinearLayout barraCalificar;
	private ImageButton botonFormaPago;
	private LinearLayout ventanaTipoVales;
	private LinearLayout flechaTipoVales;
	private LinearLayout ventanaIngresarVale; //Se usa junto con flechaTipoVales
	private EditText valeIngresoUsuario;
	private LinearLayout botonesValesLista; 	//Se usa para mostrar boton lista de Vales
	private LinearLayout botonesValesNoLista; 	//Se usa para NO mostrar boton lista de Vales
	private LinearLayout ventanaConfirmarVale; //Se usa junto con flechaTipoVales
	private TextView colocarVale;
	private LinearLayout ventanaIngresarCodigo;	//Se usa junto con flechaTipoVales
	private EditText codigoIngresoUsuario;
	
	private RelativeLayout barraLateralIzq;
	private ImageButton btnMenuActualizarDatos;
	private ImageButton btnMenuCompartirApp;
	private ImageButton btnMenuTarjeta;
	private ImageButton btnMenuMsgCentral;
	private ImageButton btnVerHistorico;
	private ImageButton btnBorrarHistorico;
	private ImageButton btnVerPromos;
	private ImageButton btnVerScaner;
	
	private int flagesperaRtaJuanD=0;
	
	private String numeroValeIngresado;
	private String valeTipo;
	private boolean nuevoFisicoDigital=false;
	private boolean presionoBotonMenuAdicional=false;
	private String adicionalesUsuario="";
	
	private RadioButton selectorValeFisico;
	private RadioButton selectorValeDigital;
	
	private ImageView fotoTaxistaMapa;
	private LinearLayout infoTaxista;
	private TextView nombreConductor;
	private TextView placasTaxi;
	private TextView distanciaTaxi;
	private boolean movimientoIconoUsuario=true;
	private boolean tocoIconoFavorito=false;
	private boolean pedirFavoritos=false;
	private boolean mostrarFotoTaxista=true;
	
	//private EasyTracker easyTracker = null;
	GoogleAnalytics tracker;
	
	//********************************************************************************************
	
	public final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, Intent intent) {									
			Integer cmd = intent.getIntExtra("CMD", 0);
			flagesperaRtaJuanD=0;
			switch(cmd){

			case C.CANCEL_PROCESSDIALOG:
				try{
					progDailog.dismiss();
				}catch (Exception e){
					e.printStackTrace();
				}
				break;

			case C.TOAST:
				String toast = intent.getStringExtra("TOAST");
				try {
					Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
				} catch (Exception e) {

					e.printStackTrace();
				}
				break;

			case C.INVISIBLEBOTON:
				InvisibleBoton();
				break;

			case C.AVISO_TAXI:
				Log.i(APPNAME, module+"**********PASA POR C.AVISO_TAXI********");
				Lat = Double.parseDouble(appState.getLatitudMovil());
				Lon = Double.parseDouble(appState.getLongitudMovil());
				
				appState.setChatTaxista(""); 	//Dejar Chat en Blanco, Es un nuevo Servicio...

				int metros = restarCoordenadas(Latsave, Lonsave, Lat, Lon);
				appState.setMetros(metros);

				Intent i = new Intent(AMapaUpVersion.this, ALlegoTaxi.class);
				startActivityForResult(i, 1);
				break;

			case C.UBICACION:
				ubicacionGoogle();
				break;
				
			case C.PREGUNTAMOVIL:
				PreguntaMovil();
				break;
				
			case C.MSGTAXISTA:
				Log.i(APPNAME, module+"**********RECIBIO  MENSAJE DEL TAXISTA********");
				MensajeTaxista();
				break;
				
			case C.SONIDOALFRENTE:
				play_mp();
				break;

			case C.CERRARAPP:
				CerrardesdeMapa();
				break;

			case C.RECIBIOIMAGEN:
				getFreeMemory();
				byte[] data = intent.getByteArrayExtra("EXTRA");
				imagenRecibida=BitmapFactory.decodeByteArray(data,0,data.length);
				Log.i(APPNAME, module+"**********RECIBIO  IMAGEN EN EL MAPA********");
				mostrarFotoTaxista=true;
				ColocarInfoTaxista();
				break;

			case C.PEDIR_TAXI:
				Log.i(APPNAME, module + "---------------------------------------------------------");
				PedirElTaxi();
				break;

			case C.PAGO_EFECTIVO:
				getFreeMemory();
				Log.i(APPNAME, module + "=======EL PAGO SE HACE EN EFECTIVO SOLO INFORMAR AL USUARIO======");
				MostrarInfoPago();
				break;

			case C.PASA_ENCUESTA:
				Log.e(APPNAME, module + "PASA_ENCUESTA: Hay que pasar a la actividad del Formulario");
				appState.setFlagCerrarMapa(0);
				appState.setEstadoUnidad(EstadosUnidad.ENCUESTA);
				Intent califi = new Intent();
				califi.setClass(AMapaUpVersion.this, AFormulario.class);		        		    				
				startActivity(califi);
				finish();
				break;

			case C.PAGO_TARJETA:
				Log.i(APPNAME, module + "=======EL PAGO SE HACE EN EFECTIVO SOLO INFORMAR AL USUARIO======");
				ConfirmarPagoTarjeta();
				break;

			case C.VERIFICAR_TAXI:
				if(!ventanaAbordo){
					VentanaUsuarioAbordo();
				}    
				break;

			case C.NORECIBIOIMAGEN:
				imagenRecibida=BitmapFactory.decodeResource(getResources(), R.drawable.sin_foto);
				Log.i(APPNAME, module+"********** NO RECIBIO  IMAGEN EN EL MAPA********");
				mostrarFotoTaxista=true;
				ColocarInfoTaxista();
				break;

			case C.TOKENS_MAPA_OK:
				progDailog.dismiss();
				if(appState.getComandoPayu().equals("REMOVE_TOKEN")){
					Toast.makeText(context, "TARJETA BORRADA CORRECTAMENTE", Toast.LENGTH_LONG).show();
				}else{
					if(appState.getNumTarjetas()==1 && !appState.isBorrarTarjeta()){
						Toast.makeText(getApplicationContext(), "TARJETA SELECCIONADA: "+InfoTarjeta(appState.getInfoTarjeta1(),4), Toast.LENGTH_LONG).show();
						appState.setTarjetaPago(appState.getInfoTarjeta1());
						appState.setInfoTarjeta1("");
						appState.setInfoTarjeta2("");
						appState.setInfoTarjeta3("");
						appState.setFormaPago(4);	//Escogio Pago con Tarjeta de Credito
						botonFormaPago.setImageResource(R.drawable.btntarjetaok);
					}else{	//Tiene solo una tarjeta
						Intent tarjetas = new Intent();
						tarjetas.setClass(context, AListarTarjetas.class);
						startActivity(tarjetas);
					}
				}
				break;

			case C.TOKENS_MAPA_ERROR:
				progDailog.dismiss();
				if(appState.getComandoPayu().equals("REMOVE_TOKEN")){
					Toast.makeText(context, "ERROR AL BORRAR LA TARJETA", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(context, "ERROR AL CONSULTAR TARJETAS", Toast.LENGTH_LONG).show();
				}

				break;

			case C.ERROR_SOCKET:
				try{
				progDailog.dismiss();
				pedirFavoritos=false;
				Toast.makeText(context, "ERROR EN LA COMUNICACION, INTENTE NUEVAMENTE", 5000).show();
				}catch (Exception ex){
					Log.i(APPNAME, module+"ERROR DE SOCKET... CATCH NO REVIENTA APP");
				}
				break;

			case C.VOLVER_A_PEDIR_TAXI:
				if(appState.getIntentosSolicitud() > 1){
					int nuevosIntentos=appState.getIntentosSolicitud()-1;
					appState.setIntentosSolicitud(nuevosIntentos);
					Intent i1 = new Intent(AMapaUpVersion.this, ASolicitarNuevamente.class);
					startActivityForResult(i1, 1);
				}
				break;

			case C.PAGO_CUPO_TARJETA:
				appState.setIntentosdepago(0);	//Debe arrancar en Ceros
				//Intent i2 = new Intent(AMapaUpVersion.this, APagarVale.class);
				//startActivityForResult(i2, 1);
				if(appState.isPagoTarjetaExitoso()){
					Toast.makeText(context, "EL PAGO SE REALIZO CORRECTAMENTE", 5000).show();
				}
				else Toast.makeText(context, "EL PAGO NO PUDO REALIZARSE CON SU TAJETA DE CREDITO, PORFAVOR PAGUE AL TAXISTA EN EFECTIVO", 5000).show();
//				appState.setFlagCerrarMapa(0);
//				appState.setEstadoUnidad(EstadosUnidad.ENCUESTA);
//				Intent env = new Intent();
//				env.setClass(AMapaUpVersion.this, AFormulario.class);		        		    				
//				startActivity(env);
//				finish();
				MostrarInfoPago();
				break;

			case C.CANCELO_TAXISTA:
				//El Taxista Cancelo el Servicio se debe volver a preguntar al usuario si solicita nuevamente el Servicio
				//appState.setEstadoUnidad(EstadosUnidad.CANCELADO_POR_TAXISTA);
				appState.setEstadoUnidad(EstadosUnidad.LIBRE);
				//map.clear();
				barraEnSolicitud.setVisibility(View.VISIBLE);
				barraEnServicio.setVisibility(View.INVISIBLE);
				fotoTaxistaMapa.setVisibility(View.INVISIBLE);
				infoTaxista.setVisibility(View.INVISIBLE);
				barraCalificar.setVisibility(View.INVISIBLE);
				avisomarket.setVisibility(View.VISIBLE);
				imgusuario.setVisibility(View.VISIBLE);
				
				EnviaInfoAnalytics("Taxista Cancelo el Servicio");
				
				Intent i3 = new Intent(AMapaUpVersion.this, ATaxistaCanceloServ.class);
				startActivityForResult(i3, 1);
				break;

			case C.CARGAR_PROPAGANDA:
				Log.i(APPNAME, "+++++++++++++++++PASA A DESCARGAR LA IMAGEN DE LA WEB...+++++++++++");
				cargaImagen=true;
				new Thread(new Runnable() {
					@Override public void run() {

						try{	
							//Verificar Pais para Propaganda...
							if((appState.getCiudadPais().contains("COLOMBIA"))||(appState.getCiudadPais().contains("Colombia"))){
								//imagenPropaganda = LoadImageFromWebOperations("http://www.taxislibres.com.co/webtaxislibres/imag350.jpg");
								imagenPropaganda = LoadImageFromWebOperations("http://www.taxiimperial.com.co/imgn100/imaAppCol/imag350.jpg");

								Bitmap imagenMostrar2 = getBitmapFromDrawable(imagenPropaganda);
								if(imagenMostrar2==null){	//No logro cargar imagen bien
									imagenBien=false;
								}else{

									Bitmap imagenMostrar3;

									if(appState.getDensidad() > 320){
										imagenMostrar3 = Bitmap.createScaledBitmap(imagenMostrar2,(int)(imagenMostrar2.getWidth()*3), (int)(imagenMostrar2.getHeight()*3), true);
									}else if((321 > appState.getDensidad())&&(appState.getDensidad() > 160)){
										imagenMostrar3 = Bitmap.createScaledBitmap(imagenMostrar2,(int)(imagenMostrar2.getWidth()*2), (int)(imagenMostrar2.getHeight()*2), true);
									}else{
										imagenMostrar3 = Bitmap.createScaledBitmap(imagenMostrar2,(int)(imagenMostrar2.getWidth()*1), (int)(imagenMostrar2.getHeight()*1), true);
									}

									Drawable d =new BitmapDrawable(getResources(),imagenMostrar3);
									imagenPropaganda=d;
									imagenBien=true;
									Log.i(APPNAME, " Pinta Imagen...");
								}

							}
							else if((appState.getCiudadPais().contains("MEXICO"))||(appState.getCiudadPais().contains("Mexico"))){

								//imagenPropaganda = LoadImageFromWebOperations("http://www.taxislibres.com.mx/webmexico/imag/imag350.jpg");
								imagenPropaganda = LoadImageFromWebOperations("http://www.taxiimperial.com.co/imgn100/imaAppMx/imag350.jpg");

								Bitmap imagenMostrar2 = getBitmapFromDrawable(imagenPropaganda);

								Bitmap imagenMostrar3;

								if(appState.getDensidad() > 320){
									imagenMostrar3 = Bitmap.createScaledBitmap(imagenMostrar2,(int)(imagenMostrar2.getWidth()*3), (int)(imagenMostrar2.getHeight()*3), true);
								}else if((321 > appState.getDensidad())&&(appState.getDensidad() > 160)){
									imagenMostrar3 = Bitmap.createScaledBitmap(imagenMostrar2,(int)(imagenMostrar2.getWidth()*2), (int)(imagenMostrar2.getHeight()*2), true);
								}else{
									imagenMostrar3 = Bitmap.createScaledBitmap(imagenMostrar2,(int)(imagenMostrar2.getWidth()*1), (int)(imagenMostrar2.getHeight()*1), true);
								}

								Drawable d =new BitmapDrawable(getResources(),imagenMostrar3);
								imagenPropaganda=d;
								imagenBien=true;
								Log.i(APPNAME, " Pinta Imagen...");
							}
							
							else imagenBien=false;


						}catch(Exception e){
							//progDailog.setIcon(R.drawable.imagen_taxis_libres);	//Colocar Imagen o Propaganda...
							imagenBien=false;
							Log.i(APPNAME, module + ": Error"+e);
							e.printStackTrace();
						}

					}
				}).start();
				break;
				
			case C.VALE_OK_SIN_CODIGO:	//Vale Fisico en BD Antigua Disponible
			case C.VALE_CODIGO_OK:
				progDailog.dismiss();
				//Se debe validar si es Fisico o Digital
				if(nuevoFisicoDigital)	appState.setFormaPago(1);	//Escogio Pago con Vale Digital
				else	appState.setFormaPago(3);	//Escogio Pago con Vale Fisico
				appState.setValeUsuario(numeroValeIngresado);
				
				AvisoHablado("VALE DISPONIBLE PARA UTILIZAR");
				Toast.makeText(context, "VALE PROCESADO CORRECTAMENTE", Toast.LENGTH_LONG).show();
				ventanaConfirmarVale.setVisibility(View.INVISIBLE); //Vale Fisico que no necesita codigo de usuario...
				ventanaIngresarCodigo.setVisibility(View.INVISIBLE);
				flechaTipoVales.setVisibility(View.INVISIBLE);
				botonFormaPago.setClickable(true);
				break;
				
			case C.VALE_MAL_SIN_CODIGO:	//Vale NO Disponible ni en la base antigua ni en la nueva
				appState.setFormaPago(0);	//Para escoger otra forma de pago
				progDailog.dismiss();
				AvisoHablado("VALE NODISPONIBLE PARA UTILIZAR");
				Toast.makeText(context, "VALE NO DISPONIBLE PARA UTILIZAR", Toast.LENGTH_LONG).show();
				ventanaConfirmarVale.setVisibility(View.INVISIBLE);
				flechaTipoVales.setVisibility(View.INVISIBLE);
				botonFormaPago.setClickable(true);
				botonFormaPago.setImageResource(R.drawable.btnefectivook);
				appState.setFormaPago(0);
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
				progDailog.dismiss();
				Toast.makeText(context, "INGRESAR CODIGO DE EMPLEADO", Toast.LENGTH_LONG).show();
				IngresarCodigoEmpeado();
				
				break;
				
			case C.VALE_OK_CODIGO_MAL:
				progDailog.dismiss();
				Toast.makeText(context, "CODIGO DE EMPLEADO INCORRECTO,INGRESE NUEVAMENTE EL CODIGO", Toast.LENGTH_LONG).show();
				//IngresarCodigoEmpeado();
				break;
				
			case C.FAVORITOS_OK:
				
				//Enviar a Actividad para Mostrar favoritos
				String infoFrecuentes = intent.getStringExtra("DATA");
				String buscarFrecuentes= infoFrecuentes.replace("{Z|1|","");
				appState.setListaFavoritos(buscarFrecuentes);
				if(pedirFavoritos){
					pedirFavoritos=false;
					
				}else{
					progDailog.dismiss();
					Log.i(APPNAME, module + "ENVIAR A MOSTRAR FAVORITOS");
					Intent i4 = new Intent(AMapaUpVersion.this, AListarFavoritos.class);
					startActivityForResult(i4, 1);
				}
				break;
				
			case C.NO_HAY_FAVORITOS:
				if(pedirFavoritos){
					pedirFavoritos=false;
					
				}else{
					progDailog.dismiss();
					AvisoHablado("USTEDNO TIENE DIRECCIONES GUARDADAS");
					Toast.makeText(context, "USTED NO TIENE DIRECCIONES GUARDADAS", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case C.ERROR_CARGANDO_FAVORITOS:
				if(pedirFavoritos){
					pedirFavoritos=false;
					
				}else{
				progDailog.dismiss();
				pedirFavoritos=false;
				Toast.makeText(context, "ERROR AL CARGAR SUS DIRECCIONES FAVORITAS", 5000).show();
				}
				break;
				
			case C.ALMACENAR_FAVORITOS_OK:
				if(pedirFavoritos){
					pedirFavoritos=false;
					
				}else{
				progDailog.dismiss();
				pedirFavoritos=false;
				appState.setListaFavoritos("NoHayFavoritos");
				Toast.makeText(context, "DIRECCION ALMACENADA CORRECTAMENTE", 5000).show();
				}
				break;
				
			case C.ALMACENAR_FAVORITOS_ERROR:
				if(pedirFavoritos){
					pedirFavoritos=false;
					
				}else{
				progDailog.dismiss();
				pedirFavoritos=false;
				Toast.makeText(context, "ERROR AL ALMACENAR DIRECCION", 5000).show();
				}
				break;
			
			case C.CONSULTAR_TARJETAS_REG:
				ConsultarTarjetas();
				break;
				
			case C.VER_PROMO:
				Log.i(APPNAME, module + "ACA SE DEBE MOSTRAR LA PROMO");
				//Cargar la Url de la promo...
				String info = intent.getStringExtra("DATA");
				String[] partes = info.split("\\|");
				Log.i(APPNAME, module + "La url es: "+ partes[1]);
				appState.setUrlPromo(partes[1]);
				Intent promos = new Intent();
				promos.setClass(context, AVerPromos.class);
				startActivityForResult(promos, 1);
				break;
				
			case C.ERROR_PROMO:
				//Toast.makeText(context, "NO FUE POSIBLE CARGAR INFO DE LA PROMO", 5000).show();
				break;
			
			case C.FILRAR_SERVICIO:
				String info2 = intent.getStringExtra("DATA");
				try{
					progDailog.dismiss();
					
					if(info2.contains("{c")){
//						imagenRecibida=BitmapFactory.decodeResource(getResources(), R.drawable.sin_foto);
//						fotoTaxistaMapa.setImageBitmap(imagenRecibida);
//				
//						//nombreConductor.setText("");
//						//placasTaxi.setText("Placa: ");
//						//distanciaTaxi.setText("Dis: ");
//						nombreConductor.setText(appState.getNombreTaxista());
//						placasTaxi.setText("Placa: " +  appState.getPlaca());
//						distanciaTaxi.setText("Dis: " + appState.getMetros() + " mts");
						//Johan
						mostrarFotoTaxista=false;
						//imagenRecibida=BitmapFactory.decodeResource(getResources(), R.drawable.sin_foto);
						InvisibleBoton();
						Log.i(APPNAME, module+ "eN bREVE.....");
						Log.i(APPNAME, module+ "***ESTADO ACTUAL: "+appState.getEstadoUnidad()+" ***");
						AvisoHablado("ENBREVE ELTAXI DEPLACAS "  + appState.getPlaca() + "loatendera");
						
						appState.setEstadoUnidad(EstadosUnidad.TAXI_ASIGNADO);
						Log.i(APPNAME, module+ "***ESTADO CAMBIO A: "+appState.getEstadoUnidad()+" ***");
												
						Log.i(APPNAME, module+"**********PASA POR C.FILRAR_SERVICIO********");
						Lat = Double.parseDouble(appState.getLatitudMovil());
						Lon = Double.parseDouble(appState.getLongitudMovil());
						
						appState.setChatTaxista(""); 	//Dejar Chat en Blanco, Es un nuevo Servicio...

						int metrosTaxi = restarCoordenadas(Latsave, Lonsave, Lat, Lon);
						appState.setMetros(metrosTaxi);
						
						//imagenRecibida=BitmapFactory.decodeResource(getResources(), R.drawable.sin_foto);
						//fotoTaxistaMapa.setImageBitmap(imagenRecibida);

						//nombreConductor.setText(appState.getNombreTaxista());
						//placasTaxi.setText("Placa: " +  appState.getPlaca());
						//distanciaTaxi.setText("Dis: " + appState.getMetros() + " mts");

						Intent in = new Intent(AMapaUpVersion.this, ALlegoTaxi.class);
						startActivityForResult(in, 1);
					}
				}catch (Exception e){
					e.printStackTrace();
					Log.i(APPNAME, module+"Algo Pasa no carga Info Taxista....");
					//nombreConductor.setText(appState.getNombreTaxista());
					//placasTaxi.setText("Placa: " +  appState.getPlaca());
					//distanciaTaxi.setText("Dis: " + appState.getMetros() + " mts");
				}
				break;
				
			case C.NO_MOVIL:
				try{
					progDailog.dismiss();
					InvisibleBoton();
					EnviaInfoAnalytics("No se Ubico Taxi");
					AvisoHablado("LOSENTIMOS NOHAY TAXI DISPONIBLE ENEL MOMENTO PORFAVOR INTENTE DENUEVO ");
					Toast.makeText(context, "LO SENTIMOS NO HAY TAXI DISPONIBLE EN EL MOMENTO!!!" +"\n POR FAVOR INTENTE DE NUEVO", 5000).show();
					appState.setEstadoUnidad(EstadosUnidad.LIBRE);
					if(appState.getIntentosSolicitud() > 1){
						
						int nuevosIntentos=appState.getIntentosSolicitud()-1;
						appState.setIntentosSolicitud(nuevosIntentos);
						Intent i1 = new Intent(AMapaUpVersion.this, ASolicitarNuevamente.class);
						startActivityForResult(i1, 1);
					}
					
				}catch (Exception e){
					e.printStackTrace();
				}	
				break;
			
			case C.MSGCONTACT:
					MensajeCentral();
				break;
				
			}
		
					
		}
	}; 

	//********************************************************************************************
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//registerReceiver(receiver, filter);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mapaupversion);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.barprueba);
		//		if (savedInstanceState == null) {
		Log.i(APPNAME, module + " *ENTRA X ONCREATE* ");

		context = getApplicationContext();
		appState = ((TaxisLi) context);
		Log.i(APPNAME, module + "!!!!!!!!!!! ESTE ES EL ESTADO APENAS ENTRA: "+ appState.getEstadoUnidad()+"!!!!!!!!!!!");

		if(appState.getEstadoUnidad().equals(EstadosUnidad.PRUEBA_RED)) appState.setEstadoUnidad(EstadosUnidad.LIBRE);

		final PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
		this.wakelock=pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "etiqueta");
		wakelock.acquire();

		/*******************************Para Actualizar si hay nueva Version *************************/
		try{
			MarketService ms = new MarketService(this);
			ms.force(true).level(MarketService.REVISION).checkVersion();
		}catch(Exception e){
			e.printStackTrace();
		}
		/********************************************************************************************/
		
		EnviaInfoAnalytics("Entro al Mapa");
		
		 // Get tracker.
//        com.google.android.gms.analytics.Tracker t = ((AnalyticsHelper) AMapaUpVersion.this.getApplication()).getTracker(
//            TrackerName.APP_TRACKER);
     // Set the dispatch period in seconds.
        //GAServiceManager.getInstance().setLocalDispatchPeriod(8);
        
//        easyTracker = EasyTracker.getInstance(AMapaUpVersion.this);
//		easyTracker.send(MapBuilder.createEvent("MapaActivity",
//				"Ingreso al OnCreate", "Prueba1", null).build());

		appState.setActividad(module);
		Time time = new Time();   time.setToNow();
		Long horaNow=time.toMillis(false);
		if(appState.getUltimahora()==123){
			Log.i(APPNAME, module +"-----SE ESCRIBE LA PRIMERA VEZ LA HORA---- :"+ Long.toString(horaNow));
			appState.setUltimahora(horaNow);
		}else{
			Long diferenciaTiempo=horaNow - appState.getUltimahora();
			//if(diferenciaTiempo > 600000){	//10minutos...
			if(diferenciaTiempo > 21600000){	//360minutos...6Horas
				Log.i(APPNAME, module +"-----SE SOBRE ESCRIBE LA ULTIMA HORA---- :"+ Long.toString(horaNow));
				appState.setUltimahora(horaNow);
				appState.setEstadoUnidad(EstadosUnidad.LIBRE);
			}else{
				Log.i(APPNAME, module +"-----ESTA FUE LA ULTIMA HORA REGISTRADA---- :"+ Long.toString(appState.getUltimahora()));
			}
		}

		// Initialize MAT
		MobileAppTracker.init(context, "12116", "6b69c1b7197136782c62cf508d25c98b");
		mobileAppTracker = MobileAppTracker.getInstance();
		mobileAppTracker.setReferralSources(this);

		mobileAppTracker.setExistingUser(true); 

		new Thread(new Runnable() {
			@Override public void run() {
				// See sample code at http://developer.android.com/google/play-services/id.html
				try {
					Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
					mobileAppTracker.setGoogleAdvertisingId(adInfo.getId());
				} catch (IOException e) {
					// Unrecoverable error connecting to Google Play services (e.g.,
					// the old version of the service doesn't support getting AdvertisingId).
				} catch (GooglePlayServicesNotAvailableException e) {
					// Google Play services is not available entirely.
				} catch (GooglePlayServicesRepairableException e) {
					// Encountered a recoverable error connecting to Google Play services.
				}
			}
		}).start();

		AccountManager accountManager = AccountManager.get(context); 
		Account account = getAccount(accountManager);

		if (account == null) {
			Log.i(APPNAME, module+"EL USUARIO NO TIENE MAIL");
		} else {
			Log.i(APPNAME, module+"EL USUARIO TIENE EL MAIL: "+ account.name);
		}

		imgusuario=(ImageView)findViewById(R.id.yo);
		//*****************************************************************
		avisomarket= (LinearLayout)findViewById(R.id.avisomarket);
		avisodireccion=(TextView) findViewById(R.id.direccionusuario);
		avisodireccion.setMaxWidth(avisodireccion.getWidth());

		//*****************************************************************
		barraMetodoPagos=(LinearLayout)findViewById(R.id.barrametodopago);
		botonFormaPago=(ImageButton) findViewById(R.id.botonpago);
		flechaCursor=(LinearLayout)findViewById(R.id.barraflecha);

		//*****************************************************************
		ventanaTipoVales=(LinearLayout)findViewById(R.id.barratipovale);
		flechaTipoVales=(LinearLayout)findViewById(R.id.flechavales);
		selectorValeFisico=(RadioButton) findViewById(R.id.tipovalefisico);
		selectorValeDigital=(RadioButton) findViewById(R.id.tipovaledigital);
		
		//*****************************************************************
		ventanaIngresarVale=(LinearLayout)findViewById(R.id.barraingresarvale);
		valeIngresoUsuario=(EditText) findViewById(R.id.numerodevaleingresado);
		botonesValesLista=(LinearLayout)findViewById(R.id.botoneslistarvale);
		botonesValesNoLista=(LinearLayout)findViewById(R.id.botonesaceptarcancelarvale);
		
		//*****************************************************************
		ventanaConfirmarVale=(LinearLayout)findViewById(R.id.barraconfirmarvale);
		colocarVale=(TextView) findViewById(R.id.elnumerodevale);
		
		//*****************************************************************
		ventanaIngresarCodigo=(LinearLayout)findViewById(R.id.barraingresarcodigo);
		codigoIngresoUsuario=(EditText) findViewById(R.id.codigoingresado);
				
		//*****************************************************************
		barraEnSolicitud=(LinearLayout)findViewById(R.id.botonessolicitud);
		//*****************************************************************
		barraEnServicio=(LinearLayout)findViewById(R.id.botonesenelservicio);
		fotoTaxistaMapa=(ImageView) findViewById(R.id.fotomapataxista);
		infoTaxista=(LinearLayout)findViewById(R.id.infotaxistamapa);
		nombreConductor=(TextView) findViewById(R.id.nombreconductormapa);
		placasTaxi=(TextView) findViewById(R.id.placataximapa);
		distanciaTaxi=(TextView) findViewById(R.id.distanciamapa);
		
		//*****************************************************************
		barraCalificar=(LinearLayout)findViewById(R.id.botonescalificarservicio);
		//*****************************************************************
		barraLateralIzq=(RelativeLayout) findViewById(R.id.menulateral);

		btnMenuActualizarDatos=(ImageButton) findViewById(R.id.botonactualizar);
		btnMenuCompartirApp=(ImageButton) findViewById(R.id.botoncompartir);
		btnMenuTarjeta=(ImageButton) findViewById(R.id.botontarjetacredito);
		btnMenuMsgCentral=(ImageButton) findViewById(R.id.botonmensajecentral);
		btnVerHistorico=(ImageButton) findViewById(R.id.botonhistorico);	
		btnBorrarHistorico=(ImageButton) findViewById(R.id.botonborrarhistorico);
		btnVerPromos=(ImageButton) findViewById(R.id.botonpromos);
		btnVerScaner=(ImageButton) findViewById(R.id.botonescaner);
		//*****************************************************************

		if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERADEPAGO)){
			avisomarket.setVisibility(View.INVISIBLE);
			Log.i(APPNAME, module+"=================PASA X ACA===============: ESPERADEPAGO ");
		}

		botonvoz= (ImageButton) findViewById(R.id.changevoz);
		if(appState.isQuieroEscuchar())	botonvoz.setImageResource(R.drawable.sonidoon);
		else botonvoz.setImageResource(R.drawable.sonidooff);

		if((appState.getEstadoUnidad().equals(EstadosUnidad.ENCUESTA))||(appState.getEstadoUnidad().equals(EstadosUnidad.CANCELADO)))appState.setEstadoUnidad(EstadosUnidad.LIBRE);
		else if(appState.getEstadoUnidad().equals(EstadosUnidad.PAGANDO_VALE))appState.setEstadoUnidad(EstadosUnidad.ESPERADEPAGO);

		//Realizar el registro del Equipo para Mensajeria GCM
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		GCMRegistrar.register(AMapaUpVersion.this,
		GCMIntentService.SENDER_ID);
		
		try{
			if(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE))	{
				InitMapa();
				appState.setFormaPago(0);
				appState.setEscogioPago(false);	//Aun no se escoge pago...	
				appState.setDestinoUsuario("");
				appState.setIncentivoTaxista("0"); //No hay Propina
				
				//Se debe Consultar Aviso Promocional...
				
				
				//String listar_promo = "listarPromosVigentes";
				String listar_promo = "validarPromosVigentesXPersona|"+ appState.getCorreoUsuario();
				appState.setComandoPayu("LISTAR_PROMO");
				Intent service = new Intent();
				service.putExtra("CMD", C.ENCRIPTADO);
				service.putExtra("DATA", listar_promo);
				service.setAction(C.COM);
				getApplicationContext().sendBroadcast(service);
				
//				String consulta_promo = "validarPromo|"+appState.getCorreoUsuario();
//				appState.setComandoPayu("CONSULTA_PROMO");
//				Intent service = new Intent();
//				service.putExtra("CMD", C.ENCRIPTADO);
//				service.putExtra("DATA", consulta_promo);
//				service.setAction(C.COM);
//				getApplicationContext().sendBroadcast(service);
				
				appState.setFlagfrecuentes(0);
				tocoIconoFavorito=false;
				
			}else if(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){
				//No hay que mostrar nuevamente la asignacion del movil ni guardar la info en BD
				noMostrarAsignacion=true;

				Intent intentTcp = new Intent(AMapaUpVersion.this, ServiceTCP.class);
				startService(intentTcp);	

				Intent intentTts = new Intent(AMapaUpVersion.this, ServiceTTS.class);
				startService(intentTts);	

				Intent intentTimer = new Intent(AMapaUpVersion.this, ServiceTimer.class);
				startService(intentTimer);

				Log.i(APPNAME, module + "------------------NUEVO MAPA CON SERVICIO EN PANTALLA-----------------");
				// TODO Auto-generated method stub		

				// Getting reference to the SupportMapFragment of activity_main.xml
				SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

				// Getting GoogleMap object from the fragment
				map = mapFragment.getMap();
				map.setMyLocationEnabled(true);
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

				// Zoom in, animating the camera.
				//map.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);

				map.getUiSettings().setZoomControlsEnabled(false);
				map.getUiSettings().setMyLocationButtonEnabled(false);
				map.setTrafficEnabled(true);

				//Al volver abrir el mapa traer esas coordenadas...

				Latsave=appState.getUltimaLatitud();	
				Lonsave=appState.getUltimaLongitud();
				
				//Hay que cargar la foto...
				//imagenRecibida=BitmapFactory.decodeResource(getResources(), R.drawable.sin_foto);
				String informacion = "I|"+ appState.getIdServicio()+"\n";
				Intent solimage = new Intent();
				solimage.putExtra("CMD", C.SEND);
				solimage.putExtra("DATA", informacion);
				solimage.setAction(C.COM);
				context.sendBroadcast(solimage);
				Log.i(APPNAME, module+"**********SOLICITAR IMAGEN********"+ informacion);

				TaxiAsignado();
				
				//OJO SI SE PINTA UNA BARRA SE DEBEN OCULTAR LAS OTRAS... DEBEN IR SOLO UNA 
				barraEnSolicitud.setVisibility(View.INVISIBLE);
				barraEnServicio.setVisibility(View.VISIBLE);
				fotoTaxistaMapa.setVisibility(View.VISIBLE);
				infoTaxista.setVisibility(View.VISIBLE);
				barraCalificar.setVisibility(View.INVISIBLE);
				avisomarket.setVisibility(View.INVISIBLE);
				imgusuario.setVisibility(View.INVISIBLE);
				
			}else if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERADEPAGO)){

				Intent intentTcp = new Intent(AMapaUpVersion.this, ServiceTCP.class);
				startService(intentTcp);	

				Intent intentTts = new Intent(AMapaUpVersion.this, ServiceTTS.class);
				startService(intentTts);	

				Intent intentTimer = new Intent(AMapaUpVersion.this, ServiceTimer.class);
				startService(intentTimer);

				Log.i(APPNAME, module + "------------------NUEVO MAPA CON ESPERA DE PAGO-----------------");
				// TODO Auto-generated method stub		

				// Getting reference to the SupportMapFragment of activity_main.xml
				SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

				// Getting GoogleMap object from the fragment
				map = mapFragment.getMap();
				map.setMyLocationEnabled(true);
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

				// Zoom in, animating the camera.
				//map.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);

				map.getUiSettings().setZoomControlsEnabled(false);
				map.getUiSettings().setMyLocationButtonEnabled(false);
				map.setTrafficEnabled(true);

				//Al volver abrir el mapa traer esas coordenadas...

				Latsave=appState.getUltimaLatitud();	
				Lonsave=appState.getUltimaLongitud();
				
				//Esta Esperando el Pago... Solo mostrar barra para Calificar
				barraEnSolicitud.setVisibility(View.INVISIBLE);
				barraEnServicio.setVisibility(View.INVISIBLE);
				fotoTaxistaMapa.setVisibility(View.INVISIBLE);
				infoTaxista.setVisibility(View.INVISIBLE);
				barraCalificar.setVisibility(View.VISIBLE);
				avisomarket.setVisibility(View.INVISIBLE);
				imgusuario.setVisibility(View.INVISIBLE);
				appState.setEstadoUnidad(EstadosUnidad.ESPERADEPAGO);

				appState.setInicializar(1);
				RumboUsuario();
			}else if(appState.getEstadoUnidad().equals(EstadosUnidad.LLEGO_CARRO)){
				Intent intentTcp = new Intent(AMapaUpVersion.this, ServiceTCP.class);
				startService(intentTcp);	

				Intent intentTts = new Intent(AMapaUpVersion.this, ServiceTTS.class);
				startService(intentTts);	

				Intent intentTimer = new Intent(AMapaUpVersion.this, ServiceTimer.class);
				startService(intentTimer);

				// Getting reference to the SupportMapFragment of activity_main.xml
				SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

				// Getting GoogleMap object from the fragment
				map = mapFragment.getMap();
				map.setMyLocationEnabled(true);
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

				// Zoom in, animating the camera.
				//map.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);

				map.getUiSettings().setZoomControlsEnabled(false);
				map.getUiSettings().setMyLocationButtonEnabled(false);
				map.setTrafficEnabled(true);

				//Al volver abrir el mapa traer esas coordenadas...

				Latsave=appState.getUltimaLatitud();	
				Lonsave=appState.getUltimaLongitud();

				TaxiAsignado();

				//OJO SI SE PINTA UNA BARRA SE DEBEN OCULTAR LAS OTRAS... DEBEN IR SOLO UNA 
				barraEnSolicitud.setVisibility(View.INVISIBLE);
				barraEnServicio.setVisibility(View.VISIBLE);
				fotoTaxistaMapa.setVisibility(View.VISIBLE);
				infoTaxista.setVisibility(View.VISIBLE);
				barraCalificar.setVisibility(View.INVISIBLE);
				avisomarket.setVisibility(View.INVISIBLE);
				imgusuario.setVisibility(View.INVISIBLE);
				VentanaUsuarioAbordo();
			}
			handler = new Handler(); 
		}catch(Exception e){
			e.printStackTrace();
			Log.e(APPNAME, module + ": Erro en Init Mapa..." + e); 		
		}

		
		try{
			map.setOnCameraChangeListener(new OnCameraChangeListener() {
				@Override
				public void onCameraChange(final CameraPosition position) {
					
					if((appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE))&&(movimientoIconoUsuario))	{
						try{
							///pensando.setVisibility(View.VISIBLE);
							//map.clear();
							appState.setAdicionalesServicio("");
							appState.setFlagfrecuentes(0);
							Latsave = position.target.latitude;
							Lonsave = position.target.longitude;

							appState.setLatitudUsuario(Latsave);
							appState.setLongitudUsuario(Lonsave);

							UBICACION = new LatLng(position.target.latitude, position.target.longitude);
							LatLng= new LatLng(position.target.latitude, position.target.longitude);

							if((position.target.latitude != 0)||(position.target.longitude != 0)){
								appState.setTramaGPSMap(position.target.latitude+ "|" + position.target.longitude);
								//trabajo.setVisibility(View.VISIBLE);
								//FavoritosMapa(Latsave,Lonsave);
							}

							Thread thrd = new Thread(){
								public void run(){

									try {
										JSONObject ret = getLocationInfo(Latsave, Lonsave); 
										String jsonToString = ret.toString().substring(1, ret.toString().length() -1);
										
										if(!tocoIconoFavorito){
											//imgusuario.setBackgroundResource(R.drawable.iconousuario);
											imgusuario.setImageResource(R.drawable.iconousuario);
											Log.i(module,"EL JSON ES ESTE: "+jsonToString);
										JSONObject location;
										JSONObject bar=ret;
										String location_string;
										String barrio_string;

										//Get JSON Array called "results" and then get the 0th complete object as JSON        
										location = ret.getJSONArray("results").getJSONObject(0);

										// Get the value of the attribute whose name is "formatted_string"
										location_string = location.getString("formatted_address");

										String [] addressData = location_string.split(",");

										barrio_string = bar.toString();
										String [] addressBarrio = barrio_string.split("formatted_address");

										String [] sacaBarrio = addressBarrio[2].split(",");

										//appState.setDireccionGoogle(addressData[0].subSequence(0, addressData[0].indexOf("-")) + " - ");

										String[] Diresepa = addressData[0].split("-");
										// appState.setDireccionGoogle(addressData[0]);
										String dirSinEspeciales= quitarCaracteresEsp(Diresepa[0]);
										appState.setDireccionGoogle(Diresepa[0] + " - ");
										appState.setDireccionGoogle(dirSinEspeciales + " - ");

										//appState.setDireccionGoogle(location_string);
										// myAddress.setText("formattted address:" + location_string);

										appState.setDireccionServicio(appState.getDireccionGoogle());
										Log.e("test", "Direccion de Google:" + appState.getDireccionGoogle() +  "    " + addressData[0]);
										String pais;
										try{
											ciudad= quitarCaracteresEsp(addressData[1]);
											String departamento= quitarCaracteresEsp(addressData[2]);

											if(jsonToString.contains("\"Mexico\"")||jsonToString.contains("\"MX\""))	pais="MEXICO";
											else if(jsonToString.contains("\"Panama\"")||jsonToString.contains("\"PA\""))	pais="PANAMA";
											else if(jsonToString.contains("\"Colombia\"")||jsonToString.contains("\"CO\""))	pais="COLOMBIA";
											else pais="NO REGISTRA";

											//pais= quitarCaracteresEsp(addressData[3]);

											barrio= quitarCaracteresEsp(sacaBarrio[0]);
											barrio=barrio.substring(3);
											appState.setBarrio(barrio);

											//Log.i(module,"EL RESTO DE INFO: "+ addressData[1] + addressData[2] + addressData[3]);
											Log.i(module,"EL RESTO DE INFO: "+ barrio +";"+ ciudad +";"+ departamento +";"+ pais);
											//												if(pais.contains("FEDERAL DISTRICT")||pais.contains("Federal District"))	pais="MEXICO";
											//												if(jsonToString.contains("\"Mexico\"")||jsonToString.contains("\"MX\""))	pais="MEXICO";
											//												else if(jsonToString.contains("\"Panama\"")||jsonToString.contains("\"PA\""))	pais="PANAMA";
											ciudadPais=ciudad+","+pais;
											appState.setCiudadPais(ciudadPais);

										}catch(Exception e){
											e.printStackTrace();
											barrio="No existe";
											ciudad="No existe";
											pais="No existe";
											ciudadPais="No existe";
											appState.setCiudadPais(ciudadPais);
										}

									} //catch (JSONException e1) {
									}
									catch (Exception e1){
										e1.printStackTrace();
										//Revisar el Result porque llega pero el resto de Info no llega aparentemente aca es donde se revienta...
										Log.i(module,"--------------ACA ES DONDE TOCA HACER EL CAMBIO---------------------");
										try {
											Geocoder geocoder = new Geocoder(AMapaUpVersion.this, Locale.getDefault());
											List<Address> list = geocoder.getFromLocation(Latsave, Lonsave, 1);
											if (!list.isEmpty()) {
												Address address = list.get(0); //Direccion Calle Cr

												Log.i("module","------------------------------------------------   Direccion: \n"+ address.getAddressLine(0));
												Log.i("module","------------------------------------------------   Dato getFeatureName: \n"+ address.getFeatureName());
												Log.i("module","------------------------------------------------   Dato getSubThoroughfare: \n"+ address.getSubThoroughfare());
												Log.i("module","------------------------------------------------   Dato getSubAdminArea es: \n"+ address.getSubAdminArea());
												Log.i("module","------------------------------------------------   Dato getSubLocality es: \n"+ address.getSubLocality());
												Log.i("module","------------------------------------------------   Pais es: \n"+ address.getCountryName());

												String[] Diresepa = address.getAddressLine(0).split("-");
												// appState.setDireccionGoogle(addressData[0]);
												String dirSinEspeciales= quitarCaracteresEsp(Diresepa[0]);
												appState.setDireccionGoogle(Diresepa[0] + " - ");
												appState.setDireccionGoogle(dirSinEspeciales + " - ");
												appState.setDireccionServicio(appState.getDireccionGoogle());

												ciudad="SIN CIUDAD MONO";

												String pais="";
												if(address.getCountryName().contains("México"))		pais="MEXICO";
												else if(address.getCountryName().contains("Colombia"))	pais="COLOMBIA";
												else if(address.getCountryName().contains("Panamá"))	pais="PANAMA";
												else pais=address.getCountryName();

												if(address.getSubLocality()==null)	barrio="SIN BARRIO";
												else barrio= quitarCaracteresEsp(address.getSubLocality());

												appState.setBarrio(barrio);

												ciudadPais=ciudad+","+pais;
												appState.setCiudadPais(ciudadPais);

												Log.e("test", "Direccion de Google:" + appState.getDireccionGoogle());
												Log.i(module,"EL RESTO DE INFO: "+ barrio +";"+ ciudad +";"+ pais);
											}
										} //catch (IOException e) {
										catch (Exception e){
											e.printStackTrace();
											Log.i(module,"--------------NADA QUE HACER NI CON GEOCODER---------------------");
											barrio="No existe";
											ciudad="No existe";
											//pais="No existe";
											//ciudadPais=ciudad+","+pais;
											ciudadPais="No existe";
											appState.setCiudadPais(ciudadPais);
										}

									}
									handler.post(new Runnable() {
										public void run() {			

											appState.setFlagrecordardireccion(0);
											if(!tocoIconoFavorito){

											if (appState.getDireccionGoogle().indexOf("#")>0 ){
												String [] separanumeral = appState.getDireccionGoogle().split("#");

//												if (separanumeral[1].indexOf("a")>0 ){		
//													avisodireccion.setTextColor(Color.RED);
//													//avisodireccion.setText("POR FAVOR REUBIQUESE");
//													//avisodireccion.setText("Desplace el icono para mejorar su ubicación");
//													avisodireccion.setText("Mueve el icono para tener tu dirección");
//													barraEnSolicitud.setVisibility(View.GONE);
//												}else{
													avisodireccion.setTextColor(Color.BLACK);
													avisodireccion.setText(appState.getDireccionGoogle());
													barraEnSolicitud.setVisibility(View.VISIBLE);
												//}
											}else{
												avisodireccion.setTextColor(Color.BLACK);							                									                									                		
												avisodireccion.setText(appState.getDireccionGoogle());
												barraEnSolicitud.setVisibility(View.VISIBLE);
											}
											}
											FavoritosMapa(Latsave,Lonsave);
										}
									});
										
								}
								
							};

							thrd.start();
							
							
						}catch (Exception e){
							Log.e(APPNAME, module + ": Erro en el Marker..." + e); 		
						}
						
					}else{// Para otros casos
						if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERADEPAGO)){

							UBICACION = new LatLng(position.target.latitude, position.target.longitude);
							LatLng= new LatLng(position.target.latitude, position.target.longitude);

							if((position.target.latitude != 0)||(position.target.longitude != 0)){
								appState.setTramaGPSMap(position.target.latitude+ "|" + position.target.longitude);

							}

						}
					}
				}
			});
		}catch (Exception e){
			e.printStackTrace();
			Log.e(APPNAME, module + ": Erro en el Main..." + e); 		
		}

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		if(provider==null){
			criteria = new Criteria();
			provider = locationManager.getBestProvider(criteria, true);
			if(provider == null){
				provider = locationManager.getBestProvider(criteria, false);
			}
		}
		locationManager.requestLocationUpdates(provider, 20000, 0, this);

		filter = new IntentFilter();
		filter.addAction(module);

		registerReceiver(receiver, filter);

	}

	//****************************************************************************************
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}
	//****************************************************************************************

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onBackPressed");
		super.onBackPressed();
	}

	//****************************************************************************************
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

	//****************************************************************************************
	public void InvisibleBoton(){

		Log.e(APPNAME, module + "Pasa por INVISIBLEBOTON");
		if(appState.getEstadoUnidad().equals(EstadosUnidad.CANCELADO))	appState.setEstadoUnidad(EstadosUnidad.LIBRE);
		if(appState.getFlaginvisible()==1){
			Log.e(APPNAME, module + "PASA X getFlaginvisible");
			map.clear();
			barraEnSolicitud.setVisibility(View.VISIBLE);
			barraEnServicio.setVisibility(View.INVISIBLE);
			fotoTaxistaMapa.setVisibility(View.INVISIBLE);
			infoTaxista.setVisibility(View.INVISIBLE);
			barraCalificar.setVisibility(View.INVISIBLE);

		}else{
			Log.e(APPNAME, module + "PASA X else");
			imgusuario.setVisibility(View.INVISIBLE);
			avisomarket.setVisibility(View.INVISIBLE);

			barraEnSolicitud.setVisibility(View.INVISIBLE);
			barraEnServicio.setVisibility(View.VISIBLE);
			fotoTaxistaMapa.setVisibility(View.VISIBLE);
			infoTaxista.setVisibility(View.VISIBLE);
			barraCalificar.setVisibility(View.INVISIBLE);

		}
		if(appState.getFlagCerrarMapa()== 2){

			if(appState.getEstadoUnidad().equals(EstadosUnidad.PAGANDO_VALE)){
				Log.e(APPNAME, module + "PASA_ENCUESTA: Hay que pasar a la actividad del Formulario");
				//aca es donde toca reportar el pago real del servicio...
				String guardarDb="";
				if(appState.getFormaPago()== 2){	//Cupo Electronico
					guardarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() + "\n"+"PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: CUPO ELECTRONICO"+"\n"+ "Valor del Servicio: "+appState.getValorPagar();
					GuardarInfoDataBase(guardarDb,"\n"+"SERVICIO CUMPLIDO");
				}else if(appState.getFormaPago()== 4){	//Es tarjeta de Credito
					guardarDb ="\n"+ "DIRECCION: "+ appState.getDireccionUsuario() + "\n"+"PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: TARJETA DE CREDITO";
					GuardarInfoDataBase(guardarDb,"\n"+"SERVICIO CUMPLIDO");
				}else{	//Toco pagar en Efectivo...
					guardarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() + "\n"+"PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: EFECTIVO";
					GuardarInfoDataBase(guardarDb,"\n"+"SERVICIO CUMPLIDO");
				}
				appState.setFlagCerrarMapa(0);
				appState.setEstadoUnidad(EstadosUnidad.ENCUESTA);
				Intent califi = new Intent();
				califi.setClass(AMapaUpVersion.this, AFormulario.class);		        		    				
				startActivity(califi);
				finish();
			}else if(!appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){

				barraEnSolicitud.setVisibility(View.INVISIBLE);
				barraEnServicio.setVisibility(View.INVISIBLE);
				fotoTaxistaMapa.setVisibility(View.INVISIBLE);
				infoTaxista.setVisibility(View.INVISIBLE);
				barraCalificar.setVisibility(View.VISIBLE);
				avisomarket.setVisibility(View.INVISIBLE);
				imgusuario.setVisibility(View.INVISIBLE);
				appState.setInicializar(1);
				appState.setEstadoUnidad(EstadosUnidad.ESPERADEPAGO);
				RumboUsuario();

			}

				Intent i2 = new Intent(AMapaUpVersion.this, ACompartirServicio.class);
				startActivityForResult(i2, 1);
		}

		Log.e(APPNAME, module + "Sale de INVISIBLEBOTON");
	}
	//****************************************************************************************
	public static String quitarCaracteresEsp(String input) {
		// Cadena de caracteres original a sustituir.
		String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
		// Cadena de caracteres ASCII que reemplazarÃ¡n los originales.
		String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		String output = input;
		for (int i = 0; i < original.length(); i++) {
			// Reemplazamos los caracteres especiales.
			output = output.replace(original.charAt(i), ascii.charAt(i));
		}//for i
		return output;
	}//quitarCaracteresEsp

	
	//****************************PARA EL SONIDO DEL ALFRENTE***************************************       
	private void play_mp() {	
		contaviso++;

		if((contaviso==1)||(flagaviso==7)){

			flagaviso=3;
			mp= MediaPlayer.create(context, R.raw.sonido);
			mp.start();

			EnviaInfoAnalytics("Taxista se reporta al Frente");
			
			Intent i = new Intent(AMapaUpVersion.this, AVerAlfrente.class);
			startActivityForResult(i, 1);	
		}
	}       
	
	
	//***************************************************************************************************

	public JSONObject getLocationInfo( double lat, double lng) {
		//String urlJson =("http://maps.google.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=false");
		String urlJson =("http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=false");
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = readJsonFromUrl(urlJson);
		} catch (IOException ex) {
			//Logger.getLogger(PruebaJsonObj.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (ParseException ex) {
			//Logger.getLogger(PruebaJsonObj.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}

		return jsonObject;
	}
	
	//****************************************************************************************
	public static JSONObject readJsonFromUrl(String url) throws IOException, ParseException {
		InputStream is = new URL(url).openStream();
		JSONObject json =null;
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			json = new JSONObject(jsonText);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			is.close();
		}
		return json;
	}
	
	//****************************************************************************************
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	//****************************************************************************************
	private void AvisoTaxiAsignado(){

		Toast.makeText(context, "En Breve el Taxi de Placas: "  + appState.getPlaca() + " atendera tu servicio, Nombre del Conductor: " + appState.getNombreTaxista(), Toast.LENGTH_LONG).show();
		AvisoHablado("ENCOTRAMOS TUTAXI ENBREVE LLEGARA");
	}
	
	//****************************************************************************************
	public void cambioMapa(View v){			
		flagmapa++;
		if(flagmapa==1){
			EnviaInfoAnalytics("Usuario Selecciona Tipo Hibrido");
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		}else if(flagmapa>1){
			EnviaInfoAnalytics("Usuario Selecciona Tipo de Mapa Normal");
			flagmapa=0;		
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		}									

	}
	
	//****************************************************************************************
	public void UbicarUsuario(View v){
		try{
			EnviaInfoAnalytics("Usuario presiona boton de Ubicacion");
			appState.setFlagfrecuentes(0);
			com.google.android.gms.maps.model.LatLng ACTUAL = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
			//map.moveCamera(CameraUpdateFactory.newLatLngZoom(ACTUAL, 16));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(ACTUAL, 18));
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(context, "NO HAY SEÑAL DE GPS VALIDA", Toast.LENGTH_LONG).show();
		}
	}
	
	//****************************************************************************************
	public void cambioVoz(View v){	
		if(appState.isQuieroEscuchar()){
			EnviaInfoAnalytics("Usuario Selecciona Off para comandos de voz");
			botonvoz.setImageResource(R.drawable.sonidooff);
			appState.setQuieroEscuchar(false);
		}else{
			EnviaInfoAnalytics("Usuario Selecciona On para comandos de voz");
			botonvoz.setImageResource(R.drawable.sonidoon);
			appState.setQuieroEscuchar(true);
		}

	}

	//****************************************************************************************
	public void MenuFormasdePago(View v){
		RevisaMenusAbiertos();
		barraMetodoPagos.setVisibility(View.VISIBLE);
		flechaCursor.setVisibility(View.VISIBLE);
		EnviaInfoAnalytics("Presiono Boton Formas de Pago");
	}
	
	//****************************************************************************************
	public void OprimioEfectivo(View v){
		//		appState.setFormaPago(0);
		//		barraMetodoPagos.setVisibility(View.INVISIBLE);
		//		flechaCursor.setVisibility(View.INVISIBLE);
		//		botonFormaPago.setImageResource(R.drawable.btnefectivook);
		final ImageButton iconoEfectivo = (ImageButton) findViewById(R.id.botonpagoefectivo);
		iconoEfectivo.setImageResource(R.drawable.botonefectivoon);
		
		EnviaInfoAnalytics("Presiono Boton Efectivo");

		new Handler().postDelayed(new Runnable(){
			public void run(){
				appState.setFormaPago(0);
				barraMetodoPagos.setVisibility(View.INVISIBLE);
				flechaCursor.setVisibility(View.INVISIBLE);
				botonFormaPago.setImageResource(R.drawable.btnefectivook);
				iconoEfectivo.setImageResource(R.drawable.botonefectivooff);
			};
		}, 500);

	}
	
	//****************************************************************************************
	public void OprimioVale(View v){

		final ImageButton iconoVale = (ImageButton) findViewById(R.id.botonpagovale);
		iconoVale.setImageResource(R.drawable.botonvaleon);
		
		EnviaInfoAnalytics("Presiono Boton Vale");
		
		new Handler().postDelayed(new Runnable(){
			public void run(){
				barraMetodoPagos.setVisibility(View.INVISIBLE);
				flechaCursor.setVisibility(View.INVISIBLE);
				botonFormaPago.setImageResource(R.drawable.btnvaleok);
				botonFormaPago.setClickable(false);
				//Se debe Mostrar la Nueva Ventana para Escoger el Tipo de Vale
				//ventanaTipoVales.setVisibility(View.VISIBLE);
				//flechaTipoVales.setVisibility(View.VISIBLE);
				iconoVale.setImageResource(R.drawable.botonvaleoff);
				
				//Se debe pasar a ingreso de Vale Directamente...

				valeTipo="VALE";
				
				try {
					selectorValeDigital.setBackgroundResource(R.drawable.selectorvaleoff);
					ventanaTipoVales.setVisibility(View.INVISIBLE);
					//Se debe mostrar la siguiente ventana... Ingresar # de Vale
					botonesValesNoLista.setVisibility(View.INVISIBLE);					
					ventanaIngresarVale.setVisibility(View.VISIBLE);
					new Handler().postDelayed(new Runnable(){
						public void run(){
							try {
								botonesValesLista.setVisibility(View.VISIBLE);
								botonesValesNoLista.setVisibility(View.INVISIBLE);
								flechaTipoVales.setVisibility(View.VISIBLE);
							}catch (Exception e) {
								e.printStackTrace();
							}
						};
						
					}, 200);
				} catch (Exception e) {
					e.printStackTrace();
				}	

			
			};
		}, 500);
	}
	
	//****************************************************************************************
//	public void EscogioVale(View view){
//
//		boolean checked = ((RadioButton) view).isChecked();
//
//		switch(view.getId()) {
//
//		case R.id.tipovalefisico:
//			Log.i(APPNAME, module + "Selecciono Vale Fisico...");
//			valeTipo="FISICO";
//			selectorValeFisico.setBackgroundResource(R.drawable.selectorvaleon);
//			new Handler().postDelayed(new Runnable(){
//				public void run(){
//
//					try {
//						selectorValeFisico.setBackgroundResource(R.drawable.selectorvaleoff);
//						ventanaTipoVales.setVisibility(View.INVISIBLE);
//						//Se debe mostrar la siguiente ventana... Ingresar # de Vale
//						ventanaIngresarVale.setVisibility(View.VISIBLE);
//						
//						flechaTipoVales.setVisibility(View.VISIBLE);	
//					} catch (Exception e) {
//						e.printStackTrace();
//					}	
//
//				};
//			}, 500);
//			break;
//
//		case R.id.tipovaledigital:
//			Log.i(APPNAME, module + "Selecciono Vale Digital...");
//			valeTipo="DIGITAL";
//			selectorValeDigital.setBackgroundResource(R.drawable.selectorvaleon);
//			new Handler().postDelayed(new Runnable(){
//				public void run(){
//
//					try {
//						selectorValeDigital.setBackgroundResource(R.drawable.selectorvaleoff);
//						ventanaTipoVales.setVisibility(View.INVISIBLE);
//						//Se debe mostrar la siguiente ventana... Ingresar # de Vale
//						ventanaIngresarVale.setVisibility(View.VISIBLE);
//						
//						flechaTipoVales.setVisibility(View.VISIBLE);	
//					} catch (Exception e) {
//						e.printStackTrace();
//					}	
//
//				};
//			}, 500);
//			break;
//
//		}
//
//	}
	
	//****************************************************************************************
	public void BotonValeFisico(View view){

		Log.i(APPNAME, module + "Selecciono Vale Fisico...");
		valeTipo="FISICO";
		selectorValeFisico.setBackgroundResource(R.drawable.selectorvaleon);
		
		
		new Handler().postDelayed(new Runnable(){
			public void run(){

				try {
					selectorValeFisico.setBackgroundResource(R.drawable.selectorvaleoff);
					ventanaTipoVales.setVisibility(View.INVISIBLE);
					//Se debe mostrar la siguiente ventana... Ingresar # de Vale
					botonesValesLista.setVisibility(View.INVISIBLE);					
					ventanaIngresarVale.setVisibility(View.VISIBLE);
					new Handler().postDelayed(new Runnable(){
						public void run(){
							try {
								botonesValesNoLista.setVisibility(View.VISIBLE);
								botonesValesLista.setVisibility(View.INVISIBLE);
								flechaTipoVales.setVisibility(View.VISIBLE);	
							}catch (Exception e) {
								e.printStackTrace();
							}
						};

					}, 200);
				} catch (Exception e) {
					e.printStackTrace();
				}	

			};
		}, 500);
		
	}

	//****************************************************************************************
	public void BotonValeDigital(View view){
		Log.i(APPNAME, module + "Selecciono Vale Digital...");
		valeTipo="DIGITAL";
		selectorValeDigital.setBackgroundResource(R.drawable.selectorvaleon);
		
		new Handler().postDelayed(new Runnable(){
			public void run(){

				try {
					selectorValeDigital.setBackgroundResource(R.drawable.selectorvaleoff);
					ventanaTipoVales.setVisibility(View.INVISIBLE);
					//Se debe mostrar la siguiente ventana... Ingresar # de Vale
					botonesValesNoLista.setVisibility(View.INVISIBLE);					
					ventanaIngresarVale.setVisibility(View.VISIBLE);
					new Handler().postDelayed(new Runnable(){
						public void run(){
							try {
								botonesValesLista.setVisibility(View.VISIBLE);
								botonesValesNoLista.setVisibility(View.INVISIBLE);
								flechaTipoVales.setVisibility(View.VISIBLE);
							}catch (Exception e) {
								e.printStackTrace();
							}
						};
						
					}, 200);
				} catch (Exception e) {
					e.printStackTrace();
				}	

			};
		}, 500);
	}
	//****************************************************************************************
	public void ListarVales(View v){
		EnviaInfoAnalytics("Presiono Boton Lista de Vales");
		ventanaIngresarVale.setVisibility(View.INVISIBLE);
		flechaTipoVales.setVisibility(View.INVISIBLE);
		botonFormaPago.setImageResource(R.drawable.btnefectivook);
		appState.setFormaPago(0);
		botonFormaPago.setClickable(true);
		Log.i(APPNAME, module + "Aca se deben Listar los Vales Digitales...");
		Intent i = new Intent(AMapaUpVersion.this, AListarVales.class);
		startActivityForResult(i, 1);
	}
	//****************************************************************************************
	public void AceptoVale(View v){
		Log.i(APPNAME, module + "Entro x AceptoVale");
		numeroValeIngresado=valeIngresoUsuario.getText().toString();
		if(numeroValeIngresado.equals("")){
			Toast.makeText(getApplicationContext(), "NO INGRESO NUMERO DE VALE", 5000).show();
		}else{
			EnviaInfoAnalytics("Presiono Aceptar en Ingrese Num Vale");
			ventanaIngresarVale.setVisibility(View.INVISIBLE);
			//Se debe mostrar la siguiente ventana... Reconfirmar Vale
			colocarVale.setText("el número "+numeroValeIngresado+"?");
			ventanaConfirmarVale.setVisibility(View.VISIBLE);
		}
		
	}
	
	//****************************************************************************************
	public void NoAceptoVale(View v){
		Log.i(APPNAME, module + "Entro x NoAceptoVale");
		ventanaIngresarVale.setVisibility(View.INVISIBLE);
		flechaTipoVales.setVisibility(View.INVISIBLE);
		botonFormaPago.setClickable(true);
		botonFormaPago.setImageResource(R.drawable.btnefectivook);
		appState.setFormaPago(0);
	}
	
	//****************************************************************************************
	public void ConfirmoVale(View v){
		EnviaInfoAnalytics("Realizar Confirmación de Vale");
		Log.i(APPNAME, module + "Entro x ConfirmoVale");
		//Se debe enviar el numero de Vale a Juan C...
		
		//String prueba_com = "Y|0|" +appState.getNumberPhone()+"|"+numeroValeIngresado+"|"+valeTipo+"\n";
		
		String prueba_com = "Y|0|" +appState.getNumberPhone()+"|"+numeroValeIngresado+"|"+valeTipo+"|"+appState.getTramaGPSMap()+"\n";

		Intent consultavale = new Intent();
		consultavale.putExtra("CMD", C.SEND);
		consultavale.putExtra("DATA", prueba_com);
		consultavale.setAction(C.COM);
		context.sendBroadcast(consultavale);

		progDailog = new ProgressDialog(AMapaUpVersion.this);

		progDailog.setTitle("REALIZANDO VERIFICACION DE VALE");
		progDailog.setMessage("ESPERE UN MOMENTO... ");
		progDailog.setCancelable(true);
		progDailog.show();
		
		EsperarRtaEncriptada();
	}
	
	//****************************************************************************************
	public void NoConfirmoVale(View v){
		Log.i(APPNAME, module + "Entro x NoConfirmoVale");
		ventanaConfirmarVale.setVisibility(View.INVISIBLE);
		flechaTipoVales.setVisibility(View.INVISIBLE);
		botonFormaPago.setClickable(true);
		botonFormaPago.setImageResource(R.drawable.btnefectivook);
		appState.setFormaPago(0);
	}
	
	//****************************************************************************************
	public void IngresarCodigoEmpeado(){
		EnviaInfoAnalytics("Ingresar Codigo de Empleado");
		Log.i(APPNAME, module + "Leer Codigo Empleado");
		appState.setFormaPago(0);	//Aun no se sabe si es el usuario asignado a ese vale...
		ventanaConfirmarVale.setVisibility(View.INVISIBLE);
		//Mostrar Ventana de Codigo de Usuario...
		ventanaIngresarCodigo.setVisibility(View.VISIBLE);
	}
	
	//****************************************************************************************
	public void AceptoCodigo(View v){
		EnviaInfoAnalytics("Verificar Codigo Empleado");
		Log.i(APPNAME, module + "Entro x AceptoCodigo");
		String codigoUsuario=codigoIngresoUsuario.getText().toString();
		
		if(codigoUsuario.equals("")){
			Toast.makeText(getApplicationContext(), "NO INGRESO CODIGO DE USUARIO", 5000).show();
		}else{
			//Se debe Enviar Codigo del Usuario
//			String enviaCodigo = "Y|1|" +appState.getNumberPhone()+"|"+numeroValeIngresado+"|"+codigoUsuario+"|"+valeTipo+"\n";
			String enviaCodigo = "Y|1|" +appState.getNumberPhone()+"|"+numeroValeIngresado+"|"+codigoUsuario+"|"+valeTipo+"|"+appState.getTramaGPSMap()+"\n";

			Intent consultavale = new Intent();
			consultavale.putExtra("CMD", C.SEND);
			consultavale.putExtra("DATA", enviaCodigo);
			consultavale.setAction(C.COM);
			context.sendBroadcast(consultavale);

			progDailog = new ProgressDialog(AMapaUpVersion.this);

			progDailog.setTitle("ENVIANDO CODIGO DE USUARIO");
			progDailog.setMessage("ESPERE UN MOMENTO... ");
			progDailog.setCancelable(true);
			progDailog.show();
			
			EsperarRtaEncriptada();
		}
	}
	
	//****************************************************************************************
	public void NoAceptoCodigo(View v){
		Log.i(APPNAME, module + "Entro x NoAceptoCodigo");
		ventanaIngresarCodigo.setVisibility(View.INVISIBLE);
		flechaTipoVales.setVisibility(View.INVISIBLE);
		botonFormaPago.setClickable(true);
		botonFormaPago.setImageResource(R.drawable.btnefectivook);
		appState.setFormaPago(0);
	}

	//****************************************************************************************
	public void OprimioTarjeta(View v){
		
		final ImageButton iconoTarjeta = (ImageButton) findViewById(R.id.botonpagotarjeta);
		iconoTarjeta.setImageResource(R.drawable.botontarjetaon);
		
		EnviaInfoAnalytics("Presiono Boton T. Credito ");

		new Handler().postDelayed(new Runnable(){
			public void run(){
				appState.setBorrarTarjeta(false);
				barraMetodoPagos.setVisibility(View.INVISIBLE);
				flechaCursor.setVisibility(View.INVISIBLE);
				iconoTarjeta.setImageResource(R.drawable.botontarjetaoff);
				ConsultarTarjetas();
			};
		}, 500);
	}

	//****************************************************************************************
	public void ConsultarTarjetas(){
		if(appState.getPayerIdTc().equals("NoHay")||(appState.getNumTarjetas()==0)){
			Intent login = new Intent();
			//login.setClass(getApplicationContext(), ARegistrarTarjeta.class);
			//startActivity(login);
			Intent i = new Intent(AMapaUpVersion.this, ARegistrarTarjeta.class);
			startActivityForResult(i, 1);

		}else{

			//String consultar_token= "{\"language\": \"es\", \"command\": \"GET_TOKENS\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"creditCardTokenInformation\": {\"payerId\": "+appState.getPayerIdTc()+", \"creditCardTokenId\": null}}";
			//appState.setComandoPayu("GET_TOKENS");
			String colocaPais="";
			if(appState.getCiudadPais().contains("COLOMBIA"))	colocaPais="CO";
			else if(appState.getCiudadPais().contains("MEXICO"))	colocaPais="MX";
			else colocaPais="PA";
			String consultar_token="listarTarjetas|"+colocaPais+"|"+appState.getCorreoUsuario();
			appState.setComandoPayu("GET_TOKENS");
			Intent envia_token = new Intent();
			envia_token.putExtra("CMD", C.ENCRIPTADO);
			envia_token.putExtra("DATA", consultar_token);
			envia_token.setAction(C.COM);
			getApplicationContext().sendBroadcast(envia_token);


			progDailog = new ProgressDialog(AMapaUpVersion.this);
			progDailog.setTitle("CONSULTANDO TARJETA(S) DE CREDITO");
			progDailog.setMessage("ESPERE UN MOMENTO... ");
			progDailog.setCancelable(true);
			progDailog.show();
			
			EsperarRtaEncriptada();
		}

	}
	//****************************************************************************************
	public void MenuIzquierda(View v){
		//Toast.makeText(context, "PRESIONO MENU ADICIONAL", Toast.LENGTH_LONG).show();
		RevisaMenusAbiertos();
		appState.setBorrarTarjeta(false);
		barraLateralIzq.setVisibility(View.VISIBLE);
		btnMenuActualizarDatos.setImageResource(R.drawable.avisoactualizarblanco);
		btnMenuCompartirApp.setImageResource(R.drawable.avisocompappblanco);
		btnMenuTarjeta.setImageResource(R.drawable.avisotarjetablanco);
		btnMenuMsgCentral.setImageResource(R.drawable.avisomsgcentralblanco);
		btnVerHistorico.setImageResource(R.drawable.avisohisblanco);
		btnBorrarHistorico.setImageResource(R.drawable.avisodireccionesblanco);
		btnVerPromos.setImageResource(R.drawable.avisopromosblanco);
		btnVerScaner.setImageResource(R.drawable.avisoscannerblanco);
		
		EnviaInfoAnalytics("Presiono Boton Superior Izquierdo");
	}
	//****************************************************************************************
	public void PresionoImagenActualizar(View v){
		if(presionoBotonMenuAdicional){

		}else{
			presionoBotonMenuAdicional=true;
			btnMenuActualizarDatos.setImageResource(R.drawable.avisoactualizarnegro);
						
			EnviaInfoAnalytics("Presiono Boton Actualizar Datos");
			
			new Handler().postDelayed(new Runnable(){
				public void run(){

					try {
						barraLateralIzq.setVisibility(View.INVISIBLE);
						presionoBotonMenuAdicional=false;
						Intent actualizar = new Intent();
						actualizar.setClass(context, AActualizarDatos.class);
						startActivity(actualizar);
						
					} catch (Exception e) {
						e.printStackTrace();
					}	

				};
			}, 500);
		}

	}
	//****************************************************************************************
	public void PresionoImagenCompartir(View v){
		if(presionoBotonMenuAdicional){

		}else{
			presionoBotonMenuAdicional=true;
			btnMenuCompartirApp.setImageResource(R.drawable.avisocompappnegro);
						
			EnviaInfoAnalytics("Presiono Boton Compartir App");
			
			new Handler().postDelayed(new Runnable(){
				public void run(){

					try {
						barraLateralIzq.setVisibility(View.INVISIBLE);
						presionoBotonMenuAdicional=false;
						CompartirAppLink();
						
					} catch (Exception e) {
						e.printStackTrace();
					}	

				};
			}, 500);
		}
	}
	
	//****************************************************************************************
	public void CompartirAppLink(){
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Descarga esta super aplicación para pedir Taxi \n" +
				"http://www.taxislibres.com.co/webtaxislibres/store.php");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
	//****************************************************************************************
	public void PresionoImagenTarjeta(View v){
		if(presionoBotonMenuAdicional){
			
		}else{
			presionoBotonMenuAdicional=true;
			btnMenuTarjeta.setImageResource(R.drawable.avisotarjetanegro);
			appState.setBorrarTarjeta(true);
		
			EnviaInfoAnalytics("Presiono Boton Tarjeta de Credito");
			
			new Handler().postDelayed(new Runnable(){
				public void run(){

					try {
						barraLateralIzq.setVisibility(View.INVISIBLE);
						presionoBotonMenuAdicional=false;
						ConsultarTarjetas();
						
					} catch (Exception e) {
						e.printStackTrace();
					}	

				};
			}, 500);
		}

	}
	//****************************************************************************************
	public void PresionoImagenMensajeCentral(View v){
		if(presionoBotonMenuAdicional){

		}else{
			presionoBotonMenuAdicional=true;
			btnMenuMsgCentral.setImageResource(R.drawable.avisomsgcentralnegro);
			
			EnviaInfoAnalytics("Presiono Boton Mensaje a la Central");

			new Handler().postDelayed(new Runnable(){
				public void run(){

					try {
						barraLateralIzq.setVisibility(View.INVISIBLE);
						presionoBotonMenuAdicional=false;
						//Intent i = new Intent(AMapaUpVersion.this, AMensajeCentral.class);
						Intent i = new Intent(AMapaUpVersion.this, AChatCentral.class);
						startActivityForResult(i, 1);

					} catch (Exception e) {
						e.printStackTrace();
					}	

				};
			}, 500);
		}
		
	}
	//****************************************************************************************
	public void PresionoImagenHistorico(View v){
		if(presionoBotonMenuAdicional){
			
		}else{
			presionoBotonMenuAdicional=true;
			btnVerHistorico.setImageResource(R.drawable.avisohisnegro);

			EnviaInfoAnalytics("Presiono Boton Historico de Servicios");
			
			new Handler().postDelayed(new Runnable(){
				public void run(){

					try {
						barraLateralIzq.setVisibility(View.INVISIBLE);
						presionoBotonMenuAdicional=false;
						
						Intent i = new Intent(AMapaUpVersion.this, AVerHistorico.class);
						startActivityForResult(i, 1);
						
					} catch (Exception e) {
						e.printStackTrace();
					}	

				};
			}, 500);
		}

	}
	
	//****************************************************************************************
	public void PresionoImagenMisDirecciones(View v){
		if(presionoBotonMenuAdicional){
			
		}else{
			presionoBotonMenuAdicional=true;
			btnBorrarHistorico.setImageResource(R.drawable.avisodireccionesnegro);
						
			EnviaInfoAnalytics("Presiono Boton Tus Direcciones");
			
			new Handler().postDelayed(new Runnable(){
				public void run(){

					try {
						barraLateralIzq.setVisibility(View.INVISIBLE);
						presionoBotonMenuAdicional=false;
						//Intent i = new Intent(AMapaUpVersion.this, ABorrarHistorico.class);
						//startActivityForResult(i, 1);
						ConsultarFavoritos();
						
					} catch (Exception e) {
						e.printStackTrace();
					}	

				};
			}, 500);
		}

	}	
	//****************************************************************************************
	public void OpcionesServicio(View v){
		Log.i(APPNAME, module + "El Usuario quiere colocar adicionales al Servicio...");
		
		EnviaInfoAnalytics("Usuario Presiono Boton Tus Opciones");
		
		Intent i = new Intent(AMapaUpVersion.this, AMostrarAdicionales.class);
		startActivityForResult(i, 1);
	}
	
	//****************************************************************************************
		public void PresionoImagenPromos(View v){
			if(presionoBotonMenuAdicional){
				
			}else{
				presionoBotonMenuAdicional=true;
				btnVerPromos.setImageResource(R.drawable.avisopromosnegro);
				
				EnviaInfoAnalytics("Presiono Boton Promociones");
				
				new Handler().postDelayed(new Runnable(){
					public void run(){

						try {
							barraLateralIzq.setVisibility(View.INVISIBLE);
							presionoBotonMenuAdicional=false;
							
							String listar_promo = "validarPromosVigentesXPersona|"+ appState.getCorreoUsuario();
							appState.setComandoPayu("LISTAR_PROMO");
							Intent service = new Intent();
							service.putExtra("CMD", C.ENCRIPTADO);
							service.putExtra("DATA", listar_promo);
							service.setAction(C.COM);
							getApplicationContext().sendBroadcast(service);

							
						} catch (Exception e) {
							e.printStackTrace();
						}	

					};
				}, 500);
			}

		}
		
	//****************************************************************************************
	public void PresionoImagenEscaner(View v){
		if(presionoBotonMenuAdicional){
			
		}else{
			presionoBotonMenuAdicional=true;
			btnVerScaner.setImageResource(R.drawable.avisoscannernegro);
			
			EnviaInfoAnalytics("Presiono Boton Escanear Codigo");
			
			new Handler().postDelayed(new Runnable(){
				public void run(){

					try {
						barraLateralIzq.setVisibility(View.INVISIBLE);
						presionoBotonMenuAdicional=false;
						
//						String listar_promo = "validarPromosVigentesXPersona|"+ appState.getCorreoUsuario();
//						appState.setComandoPayu("LISTAR_PROMO");
//						Intent service = new Intent();
//						service.putExtra("CMD", C.ENCRIPTADO);
//						service.putExtra("DATA", listar_promo);
//						service.setAction(C.COM);
//						getApplicationContext().sendBroadcast(service);
						
						//Intent i = new Intent(AMapaUpVersion.this, AScanQr.class);
						//startActivityForResult(i, 1);
						
						//ScanQrCode();
						
						try {
							//Intent intent = new Intent("com.google.zxing.client.android.SCAN");
							//startActivityForResult(intent, 0);

						    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
						    //intent.setPackage ("com.cotech.taxislibres");
						    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
						    startActivityForResult(intent, 0);
							
						    Log.i( "AScanQr", "SCAN_MODE");

						} catch (Exception e) {
							e.printStackTrace();
						    //Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
						    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android&hl=es_419");
						    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
						    startActivity(marketIntent);
						    Log.i( "AScanQr", "marketIntent");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}	

				};
			}, 500);
		}

	}
	//*********************************	
	public void ScanQrCode(){
		boolean isZxingInstalled;
		 
		try
		{
		ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.zxing.client.android", 0 );
		 isZxingInstalled = true;
		    }
		catch(PackageManager.NameNotFoundException e){
		                isZxingInstalled=false;
		          }

		

		if(isZxingInstalled) //If it is then intent Zxing application
		 {
		         
		         Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		          intent.setPackage("com.google.zxing.client.android"); 
		          intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
		          intent.putExtra("SCAN_FORMATS",    "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");
		          startActivityForResult(intent, 0); 
		  }
		 else //It's not then redirect user to PlayStore-ZxingPlage
		  {
		    
		     boolean isPlayStoreInstalled;
		     try
		     {
		      ApplicationInfo i=getPackageManager().getApplicationInfo("com.google.vending", 0 );
		       isPlayStoreInstalled = true;
		      }
		     catch(PackageManager.NameNotFoundException e){
		                isPlayStoreInstalled=false;
		          }

		      /*
		       * If it is the download Zxing
		       */ 
		      if(isPlayStoreInstalled)
		       {
		        Intent DownloadZxing = new Intent(Intent.ACTION_VIEW,Uri.parse("market://detailsid=com.google.zxing.client.android"));
		        startActivity(DownloadZxing);
		        }
		      else //Toast message indicating No PlayStore Found
		       {
		         Toast.makeText(this,"Install PlayStore First",Toast.LENGTH_LONG).show();
		         Intent DownloadZxing = new Intent(Intent.ACTION_VIEW,Uri.parse("market://detailsid=com.google.zxing.client.android"));
			        startActivity(DownloadZxing);
		        }
		  }
		
	}

	//****************************************************************************************
	
	public void MenuDerecha(View v){
		RevisaMenusAbiertos();
		//Toast.makeText(context, "PRESIONO MENU DE AYUDA", Toast.LENGTH_LONG).show();
		
		EnviaInfoAnalytics("Presiono Boton Manual Usuario,Bombillo");
		
		Intent manual = new Intent();
		manual.setClass(context, AManualUsuario.class);
		//manual.setClass(context, AVerPromos.class);
		startActivity(manual);
		
//		Intent manual = new Intent();
//		manual.setClass(context, AGcmActivity.class);
//		startActivity(manual);
		
//		Intent i = new Intent(AMapaUpVersion.this, AChatTaxista.class);
//		startActivityForResult(i, 1);
		

	}
	//****************************************************************************************
	public void MenuAdicionales(View v){
		RevisaMenusAbiertos();
		appState.setDireccionUsuario(avisodireccion.getText().toString()); //Para recibirla al otro lado como parametro
		Intent i = new Intent(this, ACompletarAdicionales.class);
		startActivityForResult(i, 1);

	}
	
	//****************************************************************************************
	//public void ConsultarFavoritos(View v){
	public void ConsultarFavoritos(){
		RevisaMenusAbiertos();
		Log.i(APPNAME, module + "Pasa x ConsultarFavoritos");
		if(appState.getListaFavoritos().equals("NoHayFavoritos")){	//Pedrilos Al Servidor
			String favoritos ="Z|0|"+appState.getNumberPhone()+"|"+appState.getCiudadPais().toUpperCase().trim()+"|"+appState.getTramaGPSMap()+"\n";
			//String favoritos ="Z|0|"+appState.getNumberPhone()+"|"+appState.getCiudadPais().toUpperCase().trim()+"\n";

			Intent consultaFavoritos = new Intent();
			consultaFavoritos.putExtra("CMD", C.SEND);
			consultaFavoritos.putExtra("DATA", favoritos);
			consultaFavoritos.setAction(C.COM);
			context.sendBroadcast(consultaFavoritos);

			progDailog = new ProgressDialog(AMapaUpVersion.this);

			progDailog.setTitle("CONSULTANDO FAVORITOS");
			progDailog.setMessage("ESPERE UN MOMENTO... ");
			progDailog.setCancelable(true);
			progDailog.show();

			EsperarRtaEncriptada();
		}else{	//Traerlos que estan almacenados...
			Intent i3 = new Intent(AMapaUpVersion.this, AListarFavoritos.class);
			startActivityForResult(i3, 1);
		}
	}

	//****************************************************************************************
	public void PresionoCalificar(View v){
	//	if(appState.getFormaPago()==4){	//Hay restriccion con Vale Electronico y Tarjeta de Credito
	//		AvisoHablado("PORFAVOR ESPERE TIENE UNPAGO PENDIENTE");
	//		Toast.makeText(context, "POR FAVOR ESPERE TIENE UN PAGO PENDIENTE", Toast.LENGTH_LONG).show();	
	//	}else{	//Pago con Efectivo o Vale Normal
			EnviaInfoAnalytics("Presiono Boton Calificar sin esperar respuesta del Pago");
			Calificar();
	//	}
	}
	//****************************************************************************************
	public void MensajeTaxista(){
//		try{
//
//			AlertDialog.Builder alert = new AlertDialog.Builder(this);
//			alert.setTitle("Mensajes del Taxista");
//			alert.setMessage("Mensajes del Taxista:" + "\n" + appState.getMsgTaxista());
//			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					// here you can add functions
//
//
//				}
//			});
//			
//			alert.setIcon(R.drawable.barrasuperior);
//			alert.show();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		if(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){	

			AvisoHablado("NUEVO MENSAJE DELTAXISTA");
			Log.i(APPNAME, module + "Verificar si el mensaje tiene comas...");
			String cadena = appState.getMsgTaxista();
			cadena=cadena.replace(",","");
			appState.setMsgTaxista(cadena);


			String adicionarMensaje = appState.getChatTaxista()+"T|"+ appState.getMsgTaxista()+",";
			appState.setChatTaxista(adicionarMensaje);

			Intent i = new Intent(AMapaUpVersion.this, AChatTaxista.class);
			startActivityForResult(i, 1);

		}else{
			
		}
	}
	//****************************************************************************
	public void VentanaUsuarioAbordo(){
		appState.setEstadoUnidad(EstadosUnidad.LLEGO_CARRO);
		EnviaInfoAnalytics("Pasa a Confirmar Abordo del Taxi");
		Intent i = new Intent(this, APreguntarAbordo.class);
		startActivityForResult(i, 1);
	}
	//****************************************************************************
	public void ReconfirmarAbordo(){
		EnviaInfoAnalytics("Pasa a Reconfirmar Abordo del Taxi");
		Intent i = new Intent(this, AReconfirmarAbordo.class);
		startActivityForResult(i, 1);
	}
	//****************************************************************************
	public void Solicita(View v){
		RevisaMenusAbiertos();

		if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERADEPAGO)){	//Es Calificar
			if(appState.getFormaPago()==2){	//Hay restriccion con Vale Electronico y Tarjeta de Credito
				AvisoHablado("PORFAVOR ESPERE TIENE UNPAGO PENDIENTE");
				Toast.makeText(context, "POR FAVOR ESPERE TIENE UN PAGO PENDIENTE", Toast.LENGTH_LONG).show();	
			}else{	//Pago con Efectivo o Vale Normal
				Calificar();
			}
		}else{
			//Hay que preguntar si ya hay Id para GCM....
			if(appState.getKeyGcm().equals("No Hay")){
				Toast.makeText(context, "INTENTE EN UN MOMENTO POR FAVOR", Toast.LENGTH_LONG).show();
				EnviaInfoAnalytics("No existes Id GCM aun, el usuario debe esperar");
			}else{
				if(appState.getFlagfrecuentes()==1){	//Se deben reemplazar todos los valores...
					EnviaInfoAnalytics("Usuario desea Solicita Servicio desde un Favorito");
					Log.i(APPNAME, module + "ENTRA A VERIFICAR LA FRECUENTE...");
					String[] copyInfo = appState.getDireccionFrecuente().split("\\|");
					//Antiguo Almacenamiento de Frecuentes
					//				Log.i(APPNAME, module + copyInfo.length);
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-1]);	//Longitud
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-2]);	//Latitud
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-3]);	//0
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-4]);	//Barrio
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-5]);	//Nombre Usuario
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-6]);	//Dir Usuario
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-7]);	//Dir Servicio
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-8]);	//CiudadPais

					//Nuevo Almacenamiento de Frecuentes
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-1]);	//Index en el Servidor
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-2]);	//Longitud
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-3]);	//Latitud
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-4]);	//Barrio
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-5]);	//Dir Usuario(Escrita en el Box)
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-6]);	//Dir Servicio(Dir Google)
					//				Log.i(APPNAME, module + copyInfo[copyInfo.length-7]);	//CiudadPais

					appState.setFlagfrecuentes(0);//Se cambia para que entre a la solicitud normal y tome metodo de pago...
					ciudadPais= copyInfo[copyInfo.length-7];
					appState.setDireccionServicio(copyInfo[copyInfo.length-6]);
					if(copyInfo[copyInfo.length-5].contains("~")){	//Trae Adicionales...
						String[] partes = copyInfo[copyInfo.length-5].split("~");
						appState.setDireccionUsuario(partes[0]);
					}
					else appState.setDireccionUsuario(copyInfo[copyInfo.length-5]);
					appState.setBarrio(copyInfo[copyInfo.length-4]);
					double Lat1 = Double.parseDouble(copyInfo[copyInfo.length-3]);
					Latsave = Lat1;
					double Lon1 = Double.parseDouble(copyInfo[copyInfo.length-2]);
					Lonsave = Lon1;
					appState.setTramaGPSMap(copyInfo[copyInfo.length-3]+"|"+copyInfo[copyInfo.length-2]);
					Log.i(APPNAME, module + "SALE DE VERIFICAR LA FRECUENTE...");
				}
				else{
					EnviaInfoAnalytics("Usuario desea Solicita Servicio");
					appState.setDireccionUsuario(avisodireccion.getText().toString());
				}

				appState.setUltimaLatitud(Latsave);	//Para al volver abrir el mapa traer esas coordenadas...
				appState.setUltimaLongitud(Lonsave);

				EnviaInfoAnalytics("Oprimio Boton de Solicitar Taxi");

				Intent i = new Intent(this, AConfirmarSolicitud.class);
				startActivityForResult(i, 1);
			}
		}

	}
	//****************************************************************************
	@SuppressWarnings("deprecation")
	public void ColocarEsperaServicio(){
		progDailog = new ProgressDialog(AMapaUpVersion.this);

		if(imagenBien)	progDailog.setIcon(imagenPropaganda);	//Colocar Imagen o Propaganda...
		else{
			if((appState.getCiudadPais().contains("COLOMBIA"))||(appState.getCiudadPais().contains("Colombia"))){
				progDailog.setIcon(R.drawable.imagen_taxis_libres);
			}
			else	progDailog.setIcon(R.drawable.imagen_taxis_losunos);
		}

		progDailog.setTitle("SOLICITUD DE TAXI...");
		progDailog.setMessage("BUSCANDO EL TAXI MAS CERCANO... ");
		progDailog.setCancelable(false);
		
		progDailog.setButton("CANCELAR", new OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                // TODO Auto-generated method stub
            	Log.i(APPNAME, module + "El Usuario desea cancelar la Solicitud de Servicio....");
            	
            	AlertDialog.Builder builder = new AlertDialog.Builder(AMapaUpVersion.this);
				 
			    builder.setTitle("DESEA CANCELAR EL SERVICIO");
			    builder.setMessage("¿Confirma que desea Cancelar el Servicio?");
			    builder.setPositiveButton("SI", new OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			        Log.i("Dialogos", "Confirmacion Aceptada.");
			        progDailog.dismiss();

			        if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERA)){	//No ha llegado ID de Servicio aun...
			        	appState.setEstadoUnidad(EstadosUnidad.LIBRE);
			        }else if(appState.getEstadoUnidad().equals(EstadosUnidad.SOLICITUD_TAXI)){
			        	appState.setEstadoUnidad(EstadosUnidad.LIBRE);
			        	String cancelar_servicio = "D|"+ appState.getNumberPhone() +  "|" + appState.getIdServicio() +"\n";
						Intent cancel = new Intent();
						cancel.putExtra("CMD", C.SEND);
						cancel.putExtra("DATA", cancelar_servicio);
						cancel.setAction(C.COM);
						context.sendBroadcast(cancel);
						
						AvisoHablado("CANCELANDO EL SERVICIO");
						
						Toast.makeText(context, "CANCELANDO EL SERVICIO", Toast.LENGTH_LONG).show();
			        }
			       
			    }
			    });
			    builder.setNegativeButton("NO", new OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			        Log.i("Dialogos", "Confirmacion Cancelada.");
			        ColocarEsperaServicio();
			    }
			    });
			 
			    builder.show();
            	
            }
        });
		progDailog.show();

		
	}
	//****************************************************************************

	
	public void EnviarSolicitud(){
		Log.i(APPNAME, module + "DireccionServicio: "+ appState.getDireccionServicio());
		Log.i(APPNAME, module + "DireccionUsuario: "+ appState.getDireccionUsuario());
		
		if(appState.getDireccionServicio().equals("0")){				    		        	
			AvisoHablado("PORFAVOR ESPERE LAUBICACION DELSERVIDOR");
			Toast.makeText(context, "POR FAVOR ESPERE LA UBICACION DEL SERVIDOR", Toast.LENGTH_LONG).show();
		}else if(appState.getDireccionUsuario().contains("Mueve el icono para tener")){
			Toast.makeText(context, "POR FAVOR MUEVE EL ICONO PARA TENER TU DIRECCION CORRECTA", Toast.LENGTH_LONG).show();
		}
		else{

			Log.i(APPNAME, module + "Pasa a Enviar la Solicitud de Servicio...."); 			    						    	
			appState.setFlagsolicitud(4);
			
			ColocarEsperaServicio();	//Aviso Buscando + Propaganda + Boton Cancelar...
			
			EsperarRtaEncriptada();

			try{

				barrio=appState.getBarrio();
				
				String dirAdicionales="";
				if(!adicionalesUsuario.equals(""))	dirAdicionales=appState.getDireccionUsuario()+adicionalesUsuario;
				else	dirAdicionales=appState.getDireccionUsuario();
				

				String solicitud_servicio="";
				if(appState.getFlagfrecuentes()==0){	//Se toma la que el usuario coloco...
					//		    	solicitud_servicio = "B|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + appState.getDireccionUsuario()+"|" + appState.getNombreUsuario()
					//			    		+ "|" + barrio.toUpperCase().trim() + "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";

					//if((appState.isEscogioPago())&&(appState.getDestinoUsuario()!="")){	//Escogio Forma de Pago y Destino
					if((appState.isEscogioPago())&&(appState.getDestinoUsuario().length() > 0)){	//Escogio Forma de Pago y Destino
						if(appState.getFormaPago()==0){	//Es Efectivo, solo colocamos el destino
							if(appState.getIncentivoTaxista()=="0"){
								EnviaInfoAnalytics("Buscando Taxi,Efectivo,sin propina");
								solicitud_servicio = "B|2|0|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
										+ "|" + appState.getBarrio()+appState.getAdicionalesServicio()+"^DESTINO:"+appState.getDestinoUsuario() + "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
							}else{
								EnviaInfoAnalytics("Buscando Taxi,Efectivo,con propina");
								solicitud_servicio = "B|2|0|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
										+ "|" + appState.getBarrio()+appState.getAdicionalesServicio()+"^DESTINO:"+appState.getDestinoUsuario() +" PROPINA "+appState.getIncentivoTaxista()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
							}


						}else if(appState.getFormaPago()==2){	//Es Vale Electronico
							String tarifaVale = quitarCaracteresEsp(appState.getTarificacionVale());
							solicitud_servicio = "B|2|"+appState.getValorCarrera()+ "|"+ appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
									+ "|" + appState.getBarrio()+"*VALE ELECTRONICO,"+tarifaVale+appState.getAdicionalesServicio()+"*^DESTINO:"+appState.getDestinoUsuario() + "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
						}
						else if((appState.getFormaPago()==1)||(appState.getFormaPago()==3)){	//Es Vale de Papel o Digital
							//		    				solicitud_servicio = "B|2|0|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + appState.getDireccionUsuario()+"|" + appState.getNombreUsuario()
							//		    						+ "|" + appState.getBarrio()+"*VALE*^DESTINO:"+appState.getDestinoUsuario() + "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
							EnviaInfoAnalytics("Buscando Taxi,Vale");
							String tipoVale="";
							if(appState.getFormaPago()==1)	tipoVale="*VALED*^DESTINO:";
							else tipoVale="*VALEF*^DESTINO:";
							solicitud_servicio = "B|2|0|NV"+appState.getValeUsuario()+"|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
									+ "|" + appState.getBarrio()+tipoVale+appState.getAdicionalesServicio()+appState.getDestinoUsuario() + "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
						}
						else if(appState.getFormaPago()==4){	//Es Tarjeta de Credito
							if(appState.getIncentivoTaxista()=="0"){
								EnviaInfoAnalytics("Buscando Taxi,T. Credito,sin propina");
								solicitud_servicio = "B|2|0|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
										+ "|" + appState.getBarrio()+"*TARJETA DE CREDITO*^DESTINO:"+appState.getDestinoUsuario() +appState.getAdicionalesServicio()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
							}else{
								EnviaInfoAnalytics("Buscando Taxi,T. Credito,con propina");
								solicitud_servicio = "B|2|0|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
										+ "|" + appState.getBarrio()+"*TARJETA DE CREDITO*^DESTINO:"+appState.getDestinoUsuario() +appState.getAdicionalesServicio()+" PROPINA "+appState.getIncentivoTaxista()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
							}
						}
					}//else if((appState.isEscogioPago())&&(appState.getDestinoUsuario()=="")){	//Escogio Forma de Pago sin Destino
					else if((appState.isEscogioPago())&&(appState.getDestinoUsuario().length() == 0)){	//Escogio Forma de Pago sin Destino
						if(appState.getFormaPago()==0){	//Es Efectivo
							if(appState.getIncentivoTaxista()=="0"){
								EnviaInfoAnalytics("Buscando Taxi,Efectivo,sin propina");
								solicitud_servicio = "B|2|0|"+"PUSH"+appState.getKeyGcm()+"|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
										+ "|" + appState.getBarrio() + appState.getAdicionalesServicio()+"|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
							}else{
								EnviaInfoAnalytics("Buscando Taxi,Efectivo,con propina");
								solicitud_servicio = "B|2|0|"+"PUSH"+appState.getKeyGcm()+"|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
										+ "|" + appState.getBarrio() +appState.getAdicionalesServicio()+" PROPINA "+appState.getIncentivoTaxista()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
							}


						}else if(appState.getFormaPago()==2){	//Es Vale Electronico
							String tarifaVale = quitarCaracteresEsp(appState.getTarificacionVale());
							solicitud_servicio = "B|2|"+appState.getValorCarrera()+ "|"+ appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
									+ "|" + appState.getBarrio()+"*VALE ELECTRONICO,"+tarifaVale+"*"+appState.getDestinoUsuario() +appState.getAdicionalesServicio()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
						}
						else if((appState.getFormaPago()==1)||(appState.getFormaPago()==3)){	//Es Vale de Papel o Digital
							String tipoVale="";
							if(appState.getFormaPago()==1)	tipoVale="*VALED*";
							else tipoVale="*VALEF*";	
							//		    				solicitud_servicio = "B|2|0|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + appState.getDireccionUsuario()+"|" + appState.getNombreUsuario()
							//		    						+ "|" + appState.getBarrio()+"*VALE*"+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
							EnviaInfoAnalytics("Buscando Taxi,Vale");
							solicitud_servicio = "B|2|0|"+"PUSH"+appState.getKeyGcm()+"|"+"NV"+appState.getValeUsuario()+"|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
									+ "|" + appState.getBarrio()+tipoVale+ appState.getAdicionalesServicio()+"|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";	
						}
						else if(appState.getFormaPago()==4){	//Es Tarjeta de Credito
							String infoTarjeta=appState.getTarjetaPago();//tokenId,nombre,identNumber,franquicia de la tarjeta,numero de la tarjeta
							String[] Data = infoTarjeta.split("\\|");
							String addTarjeta="DATTC"+","+appState.getCorreoUsuario()+","+Data[0]+","+Data[3]+","+appState.getNombrePromo()+"|";
							if(appState.getIncentivoTaxista()=="0"){
								EnviaInfoAnalytics("Buscando Taxi,T. Credito,sin propina");
//								solicitud_servicio = "B|2|0|"+ appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
//										+ "|" + appState.getBarrio()+"*TARJETA DE CREDITO*"+appState.getAdicionalesServicio()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
								solicitud_servicio = "B|2|0|"+"PUSH"+appState.getKeyGcm()+"|" +addTarjeta+ appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
										+ "|" + appState.getBarrio()+"*TARJETA DE CREDITO*"+appState.getAdicionalesServicio()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
							}else{
								EnviaInfoAnalytics("Buscando Taxi,T. Credito,con propina");
								solicitud_servicio = "B|2|0|"+"PUSH"+appState.getKeyGcm()+"|"+addTarjeta + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
										+ "|" + appState.getBarrio()+"*TARJETA DE CREDITO*"+appState.getAdicionalesServicio()+" PROPINA "+appState.getIncentivoTaxista()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
							}
						}
					}//else if(appState.getDestinoUsuario()!=""){	//Solo Escribio Destino
					else if(appState.getDestinoUsuario().length() > 0){	//Solo Escribio Destino
						if(appState.getIncentivoTaxista()=="0"){
							solicitud_servicio = "B|2|0|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
									+ "|" + appState.getBarrio()+"^DESTINO:"+appState.getDestinoUsuario() +appState.getAdicionalesServicio()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";	
						}
						else{
							solicitud_servicio = "B|2|0|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
									+ "|" + appState.getBarrio()+"^DESTINO:"+appState.getDestinoUsuario() +appState.getAdicionalesServicio()+" PROPINA "+appState.getIncentivoTaxista()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
						}


					}else{	//Ni forma de Pago ni destino
						if(appState.getIncentivoTaxista()=="0"){
							solicitud_servicio = "B|2|0|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
									+ "|" + appState.getBarrio() +appState.getAdicionalesServicio()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
						}else{
							solicitud_servicio = "B|2|0|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + dirAdicionales+"|" + appState.getNombreUsuario()
									+ "|" + appState.getBarrio() +appState.getAdicionalesServicio()+" PROPINA "+appState.getIncentivoTaxista()+ "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
						}

					}

				}else{		//Se toma de las frecuentes...
					appState.setFlagfrecuentes(0);
					solicitud_servicio=appState.getDireccionFrecuente();
					//builderDir.setCancelable(true);	//Cerrar ventana de Direccion...
					Log.i(APPNAME, module + "PASO POR ENVIO DE FRECUENTES Y EL SERVICIO ES: "+ solicitud_servicio);
				}

				//String solicitud_servicio = "B|" + appState.getNumberPhone() +  "|" + "11001" +"|" + appState.getDireccionServicio() + "|" + appState.getDireccionUsuario()+"|" + appState.getNombreUsuario()
				//		+ "|" + "Castilla" + "|" + "399" + "|" +  appState.getTramaGPSMap() +"\n";

				//Se debe verificar que unicamente se envie una sola vez cuando este LIBRE...

				if(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){

					appState.setEstadoUnidad(EstadosUnidad.ESPERA);
					appState.setUltimoServicio(solicitud_servicio);	//Se almacena la ultima solicitud...
					Intent service = new Intent();
					service.putExtra("CMD", C.SEND);
					service.putExtra("DATA", solicitud_servicio);
					service.setAction(C.COM);
					context.sendBroadcast(service);
					AvisoHablado("BUSCANDO TAXI MASCERCANO");

					Toast.makeText(context, "BUSCANDO TAXI MAS CERCANO... ", Toast.LENGTH_LONG).show();

					//appState.setFlagrecordardireccion(1);
				}else if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERA)){
					Intent closedialog = new Intent();
					closedialog.putExtra("CMD", C.CANCEL_PROCESSDIALOG);
					closedialog.setAction(C.MAP);
					context.sendBroadcast(closedialog);
					AvisoHablado("SUSOLICITUD YAESTA SIENDO PROCESADA");
				}
				//Validar estado PRUEBA_RED
				else if(appState.getEstadoUnidad().equals(EstadosUnidad.PRUEBA_RED)){
					EnviaInfoAnalytics("Error al enviar Solicitud de Taxi 1");
					Intent closedialog = new Intent();
					closedialog.putExtra("CMD", C.CANCEL_PROCESSDIALOG);
					closedialog.setAction(C.MAP);
					context.sendBroadcast(closedialog);

					appState.setEstadoUnidad(EstadosUnidad.LIBRE);
					AvisoHablado("ERROR DERED INTENTE NUEVAMENTE");
					Intent toast = new Intent();
					toast.setAction(C.MAP);
					toast.putExtra("CMD", C.TOAST);
					toast.putExtra("TOAST", "ERROR DE RED INTENTE NUEVAMENTE");
					context.sendBroadcast(toast);
				}
			}catch(Exception e){
				e.printStackTrace();
				Log.i(APPNAME, module + "ESTE ES EL ERROR: "+ e);
				//Se debe cerrar el Dialogo... Y mostrar un aviso
				//*************************************************
				Intent closedialog = new Intent();
				closedialog.putExtra("CMD", C.CANCEL_PROCESSDIALOG);
				closedialog.setAction(C.MAP);
				context.sendBroadcast(closedialog);

				appState.setEstadoUnidad(EstadosUnidad.LIBRE);
				EnviaInfoAnalytics("Error al enviar Solicitud de Taxi 2");

				AvisoHablado("NOFUE POSIBLE PROCESAR SUSOLICITUD INTENTE NUEVAMENTE");

				Intent toast = new Intent();
				toast.setAction(C.MAP);
				toast.putExtra("CMD", C.TOAST);
				toast.putExtra("TOAST", "NO FUE POSIBLE PROCESAR SU SOLICITUD INTENTE NUEVAMENTE");
				context.sendBroadcast(toast);

			}

		}

	}

	//****************************************************************************
	//Ventana para solicitar un nuevo Servicio...

	public void NuevoServicio(){
		AlertDialog.Builder alertaSimple = new AlertDialog.Builder(AMapaUpVersion.this);
		alertaSimple.setTitle("        TAXIS LIBRES ");
		alertaSimple.setMessage("DESEA SOLICITAR EL SERVICIO NUEVAMENTE?");
		alertaSimple.setPositiveButton("SI",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				appState.setEstadoUnidad(EstadosUnidad.LIBRE);	//Volver estado a Libre para Solicitar nuevo Servicio
				PedirElTaxi();
			}
		});

		alertaSimple.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				appState.setEstadoUnidad(EstadosUnidad.LIBRE);	//Volver estado a Libre para Solicitar nuevo Servicio
				CerrardesdeMapa();
			}
		});  
		alertaSimple.show();

	}
	//****************************************************************************
	@SuppressWarnings("deprecation")
	public void PedirElTaxi (){

		if(appState.getFlagfrecuentes()==1){	//Se deben reemplazar todos los valores...
			Log.i(APPNAME, module + "ENTRA A VERIFICAR LA FRECUENTE...");
			String[] copyInfo = appState.getDireccionFrecuente().split("\\|");
			//Antiguo Almacenamiento de Frecuentes
			//			Log.i(APPNAME, module + copyInfo.length);
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-1]);	//Longitud
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-2]);	//Latitud
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-3]);	//0
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-4]);	//Barrio
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-5]);	//Nombre Usuario
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-6]);	//Dir Usuario
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-7]);	//Dir Servicio
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-8]);	//CiudadPais

			//Nuevo Almacenamiento de Frecuentes
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-1]);	//Index en el Servidor
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-2]);	//Longitud
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-3]);	//Latitud
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-4]);	//Barrio
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-5]);	//Dir Usuario(Escrita en el Box)
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-6]);	//Dir Servicio(Dir Google)
			//			Log.i(APPNAME, module + copyInfo[copyInfo.length-7]);	//CiudadPais

			appState.setFlagfrecuentes(0);//Se cambia para que entre a la solicitud normal y tome metodo de pago...
			ciudadPais= copyInfo[copyInfo.length-7];
			appState.setDireccionServicio(copyInfo[copyInfo.length-6]);
			appState.setDireccionUsuario(copyInfo[copyInfo.length-5]);
			barrio=copyInfo[copyInfo.length-4];
			double Lat1 = Double.parseDouble(copyInfo[copyInfo.length-3]);
			Latsave = Lat1;
			double Lon1 = Double.parseDouble(copyInfo[copyInfo.length-2]);
			Lonsave = Lon1;
			appState.setTramaGPSMap(copyInfo[copyInfo.length-3]+"|"+copyInfo[copyInfo.length-2]);
			Log.i(APPNAME, module + "SALE DE VERIFICAR LA FRECUENTE...");
		}

		appState.setUltimaLatitud(Latsave);	//Para al volver abrir el mapa traer esas coordenadas...
		appState.setUltimaLongitud(Lonsave);

		String formaPago="Efectivo";
		if((appState.getFormaPago()==1)||(appState.getFormaPago()==3))	formaPago="Vale";
		else if(appState.getFormaPago()==4)	formaPago="Tarjeta de Crédito";


		AlertDialog alertDialog2 = new AlertDialog.Builder(AMapaUpVersion.this).create();	        
		// Setting Dialog Title
		alertDialog2.setTitle("Confirmacion!!! ");	        
		// Setting Dialog Message
		//alertDialog2.setMessage("Â¿Confirma Solicitud de Taxi a:  "  + compldireccion + "?");	        
		alertDialog2.setMessage("¿Confirma Solicitud de Taxi a:  "  + appState.getDireccionUsuario() + "?" +"Forma De Pago: "+ formaPago );
		// Setting Icon to Dialog
		alertDialog2.setIcon(R.drawable.barra);	        
		// Setting OK Button
		alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {	        


			public void onClick(DialogInterface dialog, int which) {
				EnviarSolicitud();
			}
		});	        
		//   // Showing Alert Message
		alertDialog2.show();


	}

	//****************************************************************************************
	private Drawable LoadImageFromWebOperations(String url)
	{
		try{
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		}catch (Exception e) {
			Log.i(APPNAME, "Error: " + e);
			return null;
		}
	}
	
	//****************************************************************************************
	//Sumar Valor a la propina
	public void ClickMas(View v){
		if(valorPropina == 10000){
			boxPropina.setText("10000");
		}else{
			valorPropina+=500;
			boxPropina.setText(String.valueOf(valorPropina));
		}
	}

	//****************************************************************************************
	//Restar Valor a la propina
	public void ClickMenos(View v){
		if(valorPropina == 0){
			boxPropina.setText("0");
		}else{
			valorPropina-=500;
			boxPropina.setText(String.valueOf(valorPropina));
		}
	}

	//****************************************************************************************
	//Funcion para enviar broadcast de aviso que debe ser de voz...
	public void AvisoHablado(String aviso){
		Intent talk = new Intent();
		talk.setAction(C.TEXT_TO_SPEECH);
		talk.putExtra("CMD", C.HABLAR);
		talk.putExtra("TEXTHABLA",aviso);
		sendBroadcast(talk);
	}

	//****************************************************************************************
	public void Habla(View v){
		Log.i(APPNAME, module + "&&&&&&&&&&&&&&& DEBERIA PASAR A SPEECH TO TEXT &&&&&&&&&&&&&&");
	}

	//****************************************************************************************
	public void Cancela(View v){	

		if(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){

			//Cambio para que funcione		

		}else if(appState.getEstadoUnidad().equals(EstadosUnidad.SOLICITUD_TAXI)){

		}else if(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){
			EnviaInfoAnalytics("Presiono Boton Cancelar Servicio");
			Intent i1 = new Intent(AMapaUpVersion.this, ACancelarServicio.class);
			startActivityForResult(i1, 1);
		}

	}

	//****************************************************************************************
	private void InitMapa() {
		// TODO Auto-generated method stub		
		UBICACION = new LatLng(4.0897222222222, -72.961944444444); //Centro de Colombia
		//UBICACION = new LatLng(appState.getUltimaLatitud(), appState.getUltimaLongitud()); //Centrar con ultima ubicación
		// TODO Auto-generated method stub		

		// Getting reference to the SupportMapFragment of activity_main.xml
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		// Getting GoogleMap object from the fragment
		map = mapFragment.getMap();

		map.setMyLocationEnabled(true);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(UBICACION, 5));
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		// Zoom in, animating the camera.
		//map.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);

		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);
		map.setTrafficEnabled(true);	
	}

	//****************************************************************************************
	private void ubicacionGoogle() {
		// TODO Auto-generated method stub
		try{
			if(map.getMyLocation() != null){

				//Log.e(APPNAME, module + "Ese sere Gps:   " + map.getMyLocation());

				try{		
					/************************************************************************************************/			
					flagYoinicio++;				

					if(appState.getBarrio().equals(null))appState.setBarrio("Ubicando!!!");

					/*********************************  INICIO    ***************************************************/
					if(flagYoinicio==1){				
						INICIO = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());

						//map.moveCamera(CameraUpdateFactory.newLatLngZoom(INICIO, 16));
						map.moveCamera(CameraUpdateFactory.newLatLngZoom(INICIO, 18));
						Log.e(APPNAME, module + " SE CUADRA EL ZOOM   ");
						if(appState.getInicializar()==0){		


							Log.e(APPNAME, module + " ++++++++++++++++EL SALUDO++++++++++   ");

							AvisoHablado("BIENVENIDO" + appState.getNombreUsuario() + " A TAXIS LIBRES");

							//Log.i(APPNAME, module + "El Codigo de la Ciudad es: " +  appState.getCodciudad());


						}
					}								
					/*************************************CUANDO SE ASIGNA TAXI*****************************************/					
					if(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){
						flagTaxiinicio++;	
						TaxiAsignado();	
						/*************************************CUANDO EL USUARIO SUBIO AL TAXI*****************************************/
					}else if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERADEPAGO)){
						RumboUsuario();
					}
					/*****************************************************************************************************/							
				}catch (Exception e){
					e.printStackTrace();
					Log.e(APPNAME, module + "pailaaaaaaa de gps" + e);
					//Toast.makeText(context, "      BUSCANDO SENAL DE GPS\n"   +  "Por favor ubiquese en un lugar despejado", Toast.LENGTH_LONG).show();

				}
			}else{
				Log.e(APPNAME, module + "++++++++++++++++GPS PICHO++++++++++++++");
				Toast.makeText(context, " POR FAVOR ESPERE LOS SERVICIOS DE UBICACION...", Toast.LENGTH_LONG).show();
				barraEnSolicitud.setVisibility(View.INVISIBLE);	//NO MOSTRAR BOTON DE SOLICITAR TAXI...
			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}
	//****************************************************************************************
	public void TaxiAsignado(){
		try{

			if(appState.getNombreTaxista().equals(null))appState.setNombreTaxista(appState.getPlaca());
			map.clear();
			movillat = Double.parseDouble(appState.getLatitudMovil());
			movillon = Double.parseDouble(appState.getLongitudMovil());
			double LatitudTaxi = movillat;
			double LongitudTaxi = movillon;
			SERVICIO = new LatLng(LatitudTaxi, LongitudTaxi);
			//			Marker carro = map.addMarker(new MarkerOptions().position(SERVICIO).title(appState.getNombreUsuario()).snippet("Barrio: "  + appState.getBarrio()  + "\n" + "jaime")
			//					.icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)));

			Marker carro = map.addMarker(new MarkerOptions().position(SERVICIO).icon(BitmapDescriptorFactory.fromResource(R.drawable.botontaxiasignado)));
			

			if(flagTaxiinicio<3){											

				map.moveCamera(CameraUpdateFactory.newLatLngZoom(SERVICIO , 14));										
			}

			//			usuario = map.addMarker(new MarkerOptions().position(new LatLng(Latsave, Lonsave)).title(appState.getNombreUsuario()).snippet("Barrio: "  + appState.getBarrio()  + "\n" + "jaime")
			//					.icon(BitmapDescriptorFactory.fromResource(R.drawable.yo2)));
			usuario = map.addMarker(new MarkerOptions().position(new LatLng(Latsave, Lonsave)).icon(BitmapDescriptorFactory.fromResource(R.drawable.usuarioesperando)));

			Lat = Double.parseDouble(appState.getLatitudMovil());
			Lon = Double.parseDouble(appState.getLongitudMovil());			

			/**********Para SABER CUANDO LLEGA EL TAXI***************/					

			if(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){
				int metros = restarCoordenadas(Latsave, Lonsave, Lat, Lon);
				appState.setMetros(metros);					       
				Log.i(APPNAME, module + "METROS" + appState.getMetros());					        					       
				if(appState.getMetros()<150){
					appState.setFlagAnunciotaxi(7);
				}else{
					appState.setFlagAnunciotaxi(0);
				}

				ColocarInfoTaxista();
			}

		}catch (Exception e) {
			e.printStackTrace();
			Log.e(APPNAME, module + ": UPDATE LOCATION STATUS2 "+ e.getMessage());
		}
	}
	//****************************************************************************************
	public void MostrarUsuario(){
		//		usuario = map.addMarker(new MarkerOptions().position(new LatLng(Latsave, Lonsave)).title(appState.getNombreUsuario()).snippet("Barrio: "  + appState.getBarrio()  + "\n" + "jaime")
		//				.icon(BitmapDescriptorFactory.fromResource(R.drawable.yo2)));
		usuario = map.addMarker(new MarkerOptions().position(new LatLng(Latsave, Lonsave)).title(appState.getNombreUsuario()).snippet("Barrio: "  + appState.getBarrio()  + "\n" + "jaime")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.usuarioesperando)));
	}
	//****************************************************************************************
	public void RumboUsuario(){
		map.clear();
		movillat = Double.parseDouble(appState.getLatitudMovil());
		movillon = Double.parseDouble(appState.getLongitudMovil());
		double LatitudTaxi = movillat;
		double LongitudTaxi = movillon;
		
		if((LatitudTaxi!=0) && (LongitudTaxi!=0))SERVICIO = new LatLng(LatitudTaxi, LongitudTaxi);
		//SERVICIO = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
		//		Marker carro = map.addMarker(new MarkerOptions().position(SERVICIO).title(appState.getNombreUsuario()).snippet("Barrio: "  + appState.getBarrio()  + "\n" + "jaime")
		//				.icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)));

		Marker carro = map.addMarker(new MarkerOptions().position(SERVICIO).title(appState.getNombreUsuario()).snippet("Barrio: "  + appState.getBarrio()  + "\n" + "jaime")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.botontaxiasignado)));



		//		usuario = map.addMarker(new MarkerOptions().position(new LatLng(Latsave, Lonsave)).title(appState.getNombreUsuario()).snippet("Barrio: "  + appState.getBarrio()  + "\n" + "jaime")
		//				.icon(BitmapDescriptorFactory.fromResource(R.drawable.yo2)));

	}
	//****************************************************************************************
	public void PreguntaMovil(){

		Log.i(APPNAME, module + ": ******************* ESTADO *******************************: " + appState.getEstadoUnidad());
		switch (appState.getEstadoUnidad()){				

		//case SOLICITUD_TAXI: SoloPush
		case TAXI_ASIGNADO:

			try{

				Log.i(APPNAME, module + ": ++++++++++++: " + appState.getIdServicio() + "--------------------" + appState.getNumberPhone());	

				String consultar_movil = "C|" + appState.getNumberPhone() +  "|"+ appState.getIdServicio() +"\n";

				Intent cons = new Intent();
				cons.putExtra("CMD", C.SEND);
				cons.putExtra("DATA", consultar_movil);
				cons.setAction(C.COM);
				context.sendBroadcast(cons);

			}catch(Exception e){
				e.printStackTrace();
				Log.e(APPNAME, module + ": Se totea en consultar movil: " + e);
			}

			break;

		case ESPERA:

			try{

				if((appState.getDireccionServicio().equals("0"))||(appState.getDireccionUsuario().equals("0"))||(appState.getBarrio().equals("Buscando!!!"))){
					appState.setEstadoUnidad(EstadosUnidad.LIBRE);

					Log.i(APPNAME, module + ":-----------jaimeeeeee----------------"); 
				}

			}catch (Exception e){
				e.printStackTrace();
				Log.e(APPNAME, module + ": Se totea en consultar servicio: " + e);												
			}

			break;

		case CANCELADO:

			try{
				String cancelar_servicio = "D|"+ appState.getNumberPhone() +  "|" + appState.getIdServicio() +"\n";
				Intent cancel = new Intent();
				cancel.putExtra("CMD", C.SEND);
				cancel.putExtra("DATA", cancelar_servicio);
				cancel.setAction(C.COM);
				context.sendBroadcast(cancel);
			}catch (Exception e){	
				e.printStackTrace();
				Log.e(APPNAME, module + ": Se totea en consultar cancelado: " + e);
			}

			break;
		default:

			break;
		}


	}
	//****************************************************************************************
	public Integer restarCoordenadas(double latSrv, double lonSrv, double latTaxi, double lonTaxi) {
		double lon = lonTaxi - lonSrv;
		double res = Math.sin(latSrv * 0.01745329D) * Math.sin(latTaxi * 0.01745329D) + Math.cos(latSrv * 0.01745329D) * Math.cos(latTaxi * 0.01745329D) * Math.cos(lon * 0.01745329D);

		double res1 = Math.acos(res) * 57.295779510000003D;
		Double metros = res1 * 111.30200000000001D * 1000.0D;
		return metros.intValue();
	}

	//******************************************************************************************
	public void MensajeTaxista(View v){
		
		if(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){	
//			Intent i = new Intent(AMapaUpVersion.this, AMensajeTaxista.class);
//			startActivityForResult(i, 1);

			Intent i = new Intent(AMapaUpVersion.this, AChatTaxista.class);
			startActivityForResult(i, 1);

		}else{
			Toast.makeText(context, "EN ESTE MOMENTO USTED NO TIENE TAXI ASIGNADO... "  , Toast.LENGTH_LONG).show();
			AvisoHablado("ENESTE MOMENTO USTED NOTIENE TAXI ASIGNADO");
		}
		//}

	}
	//******************************************************************************************
	public void LlamarUsuario(View v){
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		if((appState.getCiudadPais().contains("COLOMBIA"))||(appState.getCiudadPais().contains("Colombia"))){
			EnviaInfoAnalytics("Presiono Boton Llamada Colombia");
			callIntent.setData(Uri.parse("tel:"+"0312111111"));  
			startActivity(callIntent);
		}
		else if((appState.getCiudadPais().contains("MEXICO"))||(appState.getCiudadPais().contains("Mexico"))){
			EnviaInfoAnalytics("Presiono Boton Llamada Mexico");
			callIntent.setData(Uri.parse("tel:"+"51111111"));
			startActivity(callIntent);
		}else{
			Toast.makeText(getApplicationContext(), "Llamada no Disponible", 5000).show();
		}
			
		//callIntent.setData(Uri.parse("tel:"+"#100"));
		
	}

	//******************************************************************************************
	public void Calificar(){
		Intent i2 = new Intent(AMapaUpVersion.this, ACalificarServicio.class);
		startActivityForResult(i2, 1);
	}

	//************PAGO EN EFECTIVO**************************
	public void MostrarInfoPago(){

		Intent i = new Intent(this, AVentanaPago.class);
		startActivityForResult(i, 1);
	}

	//************PAGO CON TARJETA DE CREDITO**************************
	public void ConfirmarPagoTarjeta(){
		AlertDialog.Builder alertaSimple = new AlertDialog.Builder(this);
		alertaSimple.setTitle("REALIZAR PAGO CON TARJETA");
		alertaSimple.setMessage("RECUERDE QUE SU PAGO AL SR "+ appState.getNombreTaxista()+
				" POR UN VALOR DE " + appState.getValorPagar() +" PESOS SE REALIZARA CON SU TARJETA DE CREDITO");

		alertaSimple.setPositiveButton("ACEPTAR",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				//String prueba_pago = "{\"language\":\"es\", \"command\":\"SUBMIT_TRANSACTION\", \"merchant\":{\"apiLogin\":\"APILOGIN\", \"apiKey\":\"APIKEY\"}, \"transaction\":{\"order\":{\"accountId\":\"500537\", \"referenceCode\":\"pruebaJCD13\", \"description\":\"584716|Test order Colombia\", \"language\":\"es\", \"signature\":\"SIGNATURE\", \"shippingAddress\":{\"country\":\"CO\"}, \"buyer\":{\"fullName\": \"MARITZA M. HERNANDEZ\",\"emailAddress\":\"jdelgadop@taxislibres.com.co\"}, \"additionalValues\":{\"TX_VALUE\":{\"value\":\"60\", \"currency\":\"USD\"}}},\"creditCardTokenId\":\"aca09507-7485-45e0-8fb4-f7d3d7a905c2\", \"type\":\"AUTHORIZATION_AND_CAPTURE\", \"paymentMethod\":\"VISA\",	\"paymentCountry\":\"CO\", \"extraParameters\":{\"INSTALLMENTS_NUMBER\":1}}, \"test\":true}";

				//appState.getIdModiPayConductor()
				//appState.getValorPagar()
				//referenceCode pin+fecha
				//dniNumber identificationNumber
				String prueba_pago = "{\"language\":\"es\",\"command\":\"SUBMIT_TRANSACTION\",\"merchant\":{\"apiLogin\":\"APILOGIN\",\"apiKey\":\"APIKEY\"},\"transaction\":{\"order\":{\"accountId\":\"ACCOUNTID\",\"referenceCode\":\"pruebage12\",\"description\":\"123|Test order Colombia\",\"language\":\"es\",\"signature\":\"SIGNATURE\",\"shippingAddress\":{\"country\":\"CO\",\"phone\":\"5517517105\"},\"buyer\":{\"fullName\":\"full name\",\"emailAddress\":\"marith20@gmail.com\",\"dniNumber\":\"32144457\",\"shippingAddress\":{\"country\":\"MX\",\"phone\":\"5517517105\"}},\"additionalValues\":{\"TX_VALUE\":{\"value\":\"39\",\"currency\":\"USD\"}}},\"creditCardTokenId\":\"aca09507-7485-45e0-8fb4-f7d3d7a905c2\",\"creditCard\":{\"securityCode\":\"123\"},\"type\":\"AUTHORIZATION_AND_CAPTURE\",\"paymentMethod\":\"VISA\",\"paymentCountry\":\"CO\",\"payer\":{\"fullName\":\"full name\",\"emailAddress\":\"marith20@gmail.com\"},\"extraParameters\":{\"INSTALLMENTS_NUMBER\":1},\"ipAddress\":\"201.217.202.179\"},\"test\":true}";
				appState.setComandoPayu("SUBMIT_TRANSACTION");

				Intent service = new Intent();
				service.putExtra("CMD", C.ENCRIPTADO);
				service.putExtra("DATA", prueba_pago);
				service.setAction(C.COM);
				getApplicationContext().sendBroadcast(service);

				appState.setFlagCerrarMapa(0);
				appState.setEstadoUnidad(EstadosUnidad.ENCUESTA);
				//Cambio 11 Junio Mexico
				Intent califi = new Intent();
				califi.setClass(AMapaUpVersion.this, AFormulario.class);		        		    				
				startActivity(califi);
				finish();
			}
		});  
		try{
			alertaSimple.show();

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	//******************************************************************************************
	public void CerrardesdeMapa(){
		//appState.setEstadoUnidad(EstadosUnidad.LIBRE);	Cmabio Julio3/14

		EnviaInfoAnalytics("Usuario Cierra la App");
		Intent intentComunication = new Intent(AMapaUpVersion.this, ServiceTCP.class);
		stopService(intentComunication);

		Intent intentTts = new Intent(AMapaUpVersion.this, ServiceTTS.class);
		stopService(intentTts);	

		Intent intentTimer = new Intent(AMapaUpVersion.this, ServiceTimer.class);
		stopService(intentTimer);			
		finish();
	}
	
	//******************************************************************************************

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(APPNAME, module + "onKeyDown");

		if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERADEPAGO)){	//Es Calificar
			if((appState.getFormaPago()==2)||(appState.getFormaPago()==4)){	//Hay restriccion con Vale Electronico y Tarjeta de Credito
				AvisoHablado("PORFAVOR ESPERE TIENE UNPAGO PENDIENTE");
				//Toast.makeText(getApplicationContext(), "POR FAVOR ESPERE TIENE UN PAGO PENDIENTE", Toast.LENGTH_LONG).show();
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("NO SE PUEDE CERRAR APLICACION");
				builder.setMessage("Usted tiene un pago Pendiente");
				builder.setPositiveButton("ACEPTAR", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i("Dialogos", "USUARIO PRESIONO ACEPTAR");
						dialog.cancel();
					}
				});
				
				builder.show();
			}else{
				switch(keyCode){
				case KeyEvent.KEYCODE_BACK:
					ColocarVentanaCerrarApp();
					return true;

				}
			}
		}
		else{
			switch(keyCode){
			case KeyEvent.KEYCODE_BACK:
				//Preguntar si hay barras visibles...
				if(barraMetodoPagos.getVisibility()== View.VISIBLE){	//Esta Escogiendo Metodo de Pago?	
					Log.i("BARRA ACTIVA: ", "barraMetodoPagos");
					barraMetodoPagos.setVisibility(View.INVISIBLE);
					flechaCursor.setVisibility(View.INVISIBLE);
					//botonFormaPago.setImageResource(R.drawable.btnefectivook);
					return true;
				}else if(ventanaTipoVales.getVisibility()== View.VISIBLE){	//Esta Escogiendo Tipo de Vale?
					Log.i("VENTANA ACTIVA: ", "ventanaTipoVales");
					ventanaTipoVales.setVisibility(View.INVISIBLE);
					flechaTipoVales.setVisibility(View.INVISIBLE);
					botonFormaPago.setClickable(true);
					botonFormaPago.setImageResource(R.drawable.btnefectivook);
					appState.setFormaPago(0);
					return true;
				}else if(ventanaIngresarVale.getVisibility()== View.VISIBLE){	//Esta Ingresando Vale?
					Log.i("VENTANA ACTIVA: ", "ventanaIngresarVale");
					ventanaIngresarVale.setVisibility(View.INVISIBLE);
					flechaTipoVales.setVisibility(View.INVISIBLE);
					botonFormaPago.setClickable(true);
					botonFormaPago.setImageResource(R.drawable.btnefectivook);
					appState.setFormaPago(0);
					return true;
				}else if(ventanaConfirmarVale.getVisibility()== View.VISIBLE){	//Esta Confirmando # de Vale
					Log.i("VENTANA ACTIVA: ", "ventanaConfirmarVale");
					ventanaConfirmarVale.setVisibility(View.INVISIBLE);
					flechaTipoVales.setVisibility(View.INVISIBLE);
					botonFormaPago.setClickable(true);
					botonFormaPago.setImageResource(R.drawable.btnefectivook);
					appState.setFormaPago(0);
					return true;
				}else if(ventanaIngresarCodigo.getVisibility()== View.VISIBLE){		//Esta Confirmando Codigo del Usuario
					Log.i("VENTANA ACTIVA: ", "ventanaIngresarCodigo");
					ventanaIngresarCodigo.setVisibility(View.INVISIBLE);
					flechaTipoVales.setVisibility(View.INVISIBLE);
					botonFormaPago.setClickable(true);
					botonFormaPago.setImageResource(R.drawable.btnefectivook);
					appState.setFormaPago(0);
				}else if(barraLateralIzq.getVisibility()== View.VISIBLE){		//Esta en el menu lateral
					barraLateralIzq.setVisibility(View.INVISIBLE);
					return true;
				}
				else if(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){
					//No da opcion para cerrar App con el boton atras
					return true;
				}
				else{	//Si no hay ventanas abiertas visibles...
					ColocarVentanaCerrarApp();
					return true;
				}
			}
			//return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//*********************************************************************************************
	public void RevisaMenusAbiertos(){
		if(barraMetodoPagos.getVisibility()== View.VISIBLE){	//Esta Escogiendo Metodo de Pago?	
			Log.i("BARRA ACTIVA: ", "barraMetodoPagos");
			barraMetodoPagos.setVisibility(View.INVISIBLE);
			flechaCursor.setVisibility(View.INVISIBLE);
		}else if(ventanaTipoVales.getVisibility()== View.VISIBLE){	//Esta Escogiendo Tipo de Vale?
			Log.i("VENTANA ACTIVA: ", "ventanaTipoVales");
			ventanaTipoVales.setVisibility(View.INVISIBLE);
			flechaTipoVales.setVisibility(View.INVISIBLE);
			botonFormaPago.setClickable(true);
			botonFormaPago.setImageResource(R.drawable.btnefectivook);
			appState.setFormaPago(0);
		}else if(ventanaIngresarVale.getVisibility()== View.VISIBLE){	//Esta Ingresando Vale?
			Log.i("VENTANA ACTIVA: ", "ventanaIngresarVale");
			ventanaIngresarVale.setVisibility(View.INVISIBLE);
			flechaTipoVales.setVisibility(View.INVISIBLE);
			botonFormaPago.setClickable(true);
			botonFormaPago.setImageResource(R.drawable.btnefectivook);
			appState.setFormaPago(0);
		}else if(ventanaConfirmarVale.getVisibility()== View.VISIBLE){	//Esta Confirmando # de Vale
			Log.i("VENTANA ACTIVA: ", "ventanaConfirmarVale");
			ventanaConfirmarVale.setVisibility(View.INVISIBLE);
			flechaTipoVales.setVisibility(View.INVISIBLE);
			botonFormaPago.setClickable(true);
			botonFormaPago.setImageResource(R.drawable.btnefectivook);
			appState.setFormaPago(0);
		}else if(ventanaIngresarCodigo.getVisibility()== View.VISIBLE){		//Esta Confirmando Codigo del Usuario
			Log.i("VENTANA ACTIVA: ", "ventanaIngresarCodigo");
			ventanaIngresarCodigo.setVisibility(View.INVISIBLE);
			flechaTipoVales.setVisibility(View.INVISIBLE);
			botonFormaPago.setClickable(true);
			botonFormaPago.setImageResource(R.drawable.btnefectivook);
			appState.setFormaPago(0);
		}else if(barraLateralIzq.getVisibility()== View.VISIBLE){		//Esta en el menu lateral
			barraLateralIzq.setVisibility(View.INVISIBLE);
		}
		
	}
	
	//*********************************************************************************************
	public void ColocarVentanaCerrarApp(){

		EnviaInfoAnalytics("Usuario Desea Salir de la App");
		Intent i = new Intent(this, ASalirApp.class);
		startActivityForResult(i, 1);
		
	}
	//*******************************************************************************************
	public void EsperarRtaEncriptada(){
		flagesperaRtaJuanD=1;

		new Handler().postDelayed(new Runnable(){
			public void run(){

				try {
					if(flagesperaRtaJuanD==1){
						Log.i("ENTRADA:", "No Hay Respuesta de Juan D");
						progDailog.dismiss();	
						//finish();
						Toast.makeText(getApplicationContext(), "NO FUE POSIBLE ENVIAR SU INFORMACION,INTENTE NUEVAMENTE", 5000).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}	

			};
		}, 190000);
	}

	//************************************************************************
	public void ListaTarjetas(){
		Log.i("LISTAR TARJETAS A BORRAR", "Entro a ListaTarjetas");

		final ArrayList<String> infoTarjetas = new ArrayList<String>();
		int cont;
		int numTarjetas=appState.getNumTarjetas();	//Se carga el # de tarjetas
		for(cont=0;cont< numTarjetas;cont++){
			switch(cont){

			case(0):
				infoTarjetas.add(cont, InfoTarjeta(appState.getInfoTarjeta1()));
			break;

			case(1):
				infoTarjetas.add(cont, InfoTarjeta(appState.getInfoTarjeta2()));
			break;

			case(2):
				infoTarjetas.add(cont, InfoTarjeta(appState.getInfoTarjeta3()));
			break;
			}
		}

		Dialog dialog = new Dialog(AMapaUpVersion.this);
		dialog.setContentView(R.layout.list);

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(AMapaUpVersion.this);

		builderSingle.setTitle("Tarjetas Registradas");
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AMapaUpVersion.this,android.R.layout.select_dialog_singlechoice);
		cont=infoTarjetas.size();
		int contadorTarjetas=0;

		while(contadorTarjetas < cont){

			arrayAdapter.add(infoTarjetas.get(contadorTarjetas));
			contadorTarjetas++;
		}

		builderSingle.setNegativeButton("CANCELAR",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builderSingle.setAdapter(arrayAdapter,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, final int which1) {
				String strName = arrayAdapter.getItem(which1);
				AlertDialog.Builder builderInner = new AlertDialog.Builder(AMapaUpVersion.this);
				builderInner.setMessage(strName);
				builderInner.setTitle(" Desea Borrar la Tarjeta?");
				builderInner.setPositiveButton("Si",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(
							DialogInterface dialog,
							int which) {
						//Aca hay que guardar esta info para el pago
						//appState.setDireccionFrecuente(copiaFrecuentes.get(which1));
						Log.i(APPNAME, module + "WHICHH1: "+ which1);
						String escogioTarjeta="";
						if(which1==0)	escogioTarjeta= appState.getInfoTarjeta1();
						else if(which1==1)	escogioTarjeta= appState.getInfoTarjeta2();	
						else if(which1==2)	escogioTarjeta= appState.getInfoTarjeta3();	
						appState.setInfoTarjeta1("");
						appState.setInfoTarjeta2("");
						appState.setInfoTarjeta3("");
						dialog.dismiss();

						String[] Partes = escogioTarjeta.split("\\|");


						//String borrarToken = "{\"language\": \"es\", \"command\": \"REMOVE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"removeCreditCardToken\": {\"payerId\": \"10\", \"creditCardTokenId\": \"1d1767e0-44c4-435b-b20f-7f57e5958df3\"}}";
						//String borrarToken = "{\"language\": \"es\", \"command\": \"REMOVE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"removeCreditCardToken\": {\"payerId\": "+appState.getPayerIdTc()+", \"creditCardTokenId\": \"f11aae7c-2f39-4c6b-8446-5ebe9027a28b\"}}";
						String borrarToken = "{\"language\": \"es\", \"command\": \"REMOVE_TOKEN\", \"merchant\": {\"apiLogin\": \"APILOGIN\", \"apiKey\": \"APIKEY\"}, \"removeCreditCardToken\": {\"payerId\": "+appState.getPayerIdTc()+", \"creditCardTokenId\": \""+Partes[0]+"\"}}";

						appState.setComandoPayu("REMOVE_TOKEN");

						Intent borrarTarjeta = new Intent();
						borrarTarjeta.putExtra("CMD", C.ENCRIPTADO);
						borrarTarjeta.putExtra("DATA", borrarToken);
						borrarTarjeta.setAction(C.COM);
						getApplicationContext().sendBroadcast(borrarTarjeta);

						progDailog = new ProgressDialog(AMapaUpVersion.this);
						progDailog.setTitle("BORRANDO TARJETA DE CREDITO");
						progDailog.setMessage("ESPERE UN MOMENTO... ");
						progDailog.setCancelable(true);
						progDailog.show();

						EsperarRtaEncriptada();

					}
				});
				builderInner.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(
							DialogInterface dialog,
							int which) {
						Log.i(APPNAME, module + "NO BORRA LA TARJETA");
						dialog.dismiss();
					}
				});
				builderInner.show();
			}
		});
		builderSingle.show();

	}

	//************************************************************************
	public String InfoTarjeta(String infotarjeta){
		String[] Data = infotarjeta.split("\\|");
		Log.i(APPNAME, module + "ENTRO A INFO TARJETA");	
		Log.i(APPNAME, module + Data[0]);	//Token
		Log.i(APPNAME, module + Data[1]);	//Nombre
		Log.i(APPNAME, module + Data[2]);	//Supuesta Cedula
		Log.i(APPNAME, module + Data[3]);	//Franquicia
		Log.i(APPNAME, module + Data[4]);	//Numero de Tarjeta

		String infoRetorna= Data[3]+"  "+ Data[4];
		return infoRetorna;

	}
	//************************************************************************

	public void GuardarInfoDataBase(String asunto, String label){
		String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
		String asunto2 = mydate + asunto;
		Handler_sqlite helper = new Handler_sqlite(this);
		helper.abrir();

		String leeinfo[]=helper.leer();
		int campos=0;
		int size=0;
		while(campos < leeinfo.length){
			if(leeinfo[campos]!=null){
				campos++;
			}
			else{
				size=campos;
				campos=1000;
			}
		}

		//Verificar numero de Registros antes de Insertar...
		size++;
		Log.i(APPNAME, module + "=================== HAY "+size+" MENSAJES =================");
		if(size < C.NUM_MENSAJES){
			//Se debe insertar NORMALMENTE!!!
			helper.insertarReg(asunto2, label);
			Log.i(APPNAME, module + "=================== INSERTANDO MENSAJE =================");
		}else{
			//Se debe Borrar todo antes de Insertar!!!
			helper.deleteAllMessages();
			AvisoHablado("REGISTRO NOFUE ALMACENADO SEBORRO SUHISTORICO DESERVICIOS");
			Log.i(APPNAME, module + "=================== BORRANDO MENSAJES =================");
		}


		helper.cerrar();
	}

	//************************************************************************

	private void enviar(String[] to, String asunto, String mensaje) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		//String[] to = direccionesEmail;
		//String[] cc = copias;
		emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
		//		        emailIntent.putExtra(Intent.EXTRA_CC, cc);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
		emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
		emailIntent.setType("message/rfc822");
		startActivity(Intent.createChooser(emailIntent, "Email "));
	}

	
	//************************************************************************    

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(APPNAME, module + " Entra por onDestroy");
		appState.setAppActiva(false); 	//App Cerrada
		appState.setAppPrimerPlano(false);	//App Cerrada
		finish();		
		//		    System.gc();
		//			registerReceiver(receiver, filter);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(APPNAME, module + " Entra por onStart");
		
		new Handler().postDelayed(new Runnable(){
			public void run(){
				Log.i(APPNAME, module + " HABILITAR EL PRIMER PLANO POR LA SALIDA DE OTRA ACTIVITY");
				appState.setAppActiva(true); 	//App Activa
				appState.setAppPrimerPlano(true);	//App en Primer Plano
				
			};
		}, 1500);
		
		//registerReceiver(receiver, filter);
		if(banderaBarra){
			banderaBarra=false;
			//CancelarenBarra(0);
		}

		LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			//Colocar Aviso de Habilitar los Servicios del gps..

			Log.i(APPNAME, module + ": HAY QUE HABILITAR EL GPS!!!");
			
			EnviaInfoAnalytics("El Usuario no tiene Habilitado el Gps");

			AlertDialog.Builder alertaSimple = new AlertDialog.Builder(this);
			alertaSimple.setTitle("HABILITAR SERVICIOS DE UBICACION");
			alertaSimple.setMessage("SE DEBEN HABILITAR LOS SERVICIOS DE UBICACION PARA UN OPTIMO FUNCIONAMIENTO");
			alertaSimple.setPositiveButton("Aceptar",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					EnviaInfoAnalytics("El Usuario pasa al menu Android para habilitar su Gps");
					
					Intent in = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);   
					startActivity(in);
					//Entra a mostrar el menu de Gps					    	    	 					    	    	 					    	    	

				}
			});  
			alertaSimple.show();

		}

		if((!cargaImagen)&&(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE))){
			Intent cargaPropaganda = new Intent();
			cargaPropaganda.putExtra("CMD", C.CARGAR_PROPAGANDA);
			cargaPropaganda.setAction(C.MAP);
			getApplicationContext().sendBroadcast(cargaPropaganda);
		}else{
			Log.i(APPNAME, "+++++++++++++++++YA ESTA CARGADA LA PROPAGANDA+++++++++++");
			//Revisar si hay msgs Push pendientes x mostrar....
			if(appState.isPushPendiente()){
				appState.setPushPendiente(false);
				FiltrarPushPendientes();
			}else{
				if(appState.getEstadoUnidad().equals(EstadosUnidad.SOLICITUD_TAXI)){
					//Hay que volver a colocar el ProgDailog....
					ColocarEsperaServicio();
				}
			}
			
		}
		
		if(appState.getActividad().equals(C.LISTAR_TARJETAS)&&(!appState.getTarjetaPago().equals("Nada"))){
			Log.i(APPNAME, module + "Viene de: "+ appState.getActividad());
			botonFormaPago.setImageResource(R.drawable.btntarjetaok);
		}

		//EasyTracker.getInstance(this).activityStart(this);
		GoogleAnalytics.getInstance(AMapaUpVersion.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + " Entra por onStop");
		super.onStop();
		if(appState.getEstadoUnidad().equals(EstadosUnidad.SOLICITUD_TAXI)){
			//OJO QUIZA HAY QUE PREGUNTAR TAMBIEN POR EstadosUnidad.ESPERA
			try{
				progDailog.dismiss();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		appState.setAppPrimerPlano(false);	//App en Segundo Plano, BackGround...
		//EasyTracker.getInstance(this).activityStop(this);
		GoogleAnalytics.getInstance(AMapaUpVersion.this).reportActivityStop(this);
	}

	//************************************************************************
	public void FiltrarPushPendientes(){
		Log.i(APPNAME, module + " ACA SE DEBEN FILTRAR LOS PUSH PENDIENTES...");
		EnviaInfoAnalytics("Llegada de Info con la App en segundo plano");
		String message=appState.getMsgPushPendiente();
		String[] partes = message.split("\\|");
		/**********************PREGUNTAR POR SERVICIO****************************/
		if(partes[0].equals("{c")){

			int estado = Integer.parseInt(partes[1]);
			switch (estado){
			
			case 1:		//Llega el Servicio
			
				Log.i(APPNAME,"Entro por c|1");
				appState.setFlagrecordardireccion(0);
				appState.setPlaca(partes[2]);
				appState.setPinMovil(partes[3]);
				appState.setNombreTaxista(partes[4]);
				appState.setLatitudMovil(partes[5]);
				appState.setLongitudMovil(partes[6]);
				
				appState.setFlaginvisible(2);
				appState.setFlagsolicitud(1);
				appState.setContador(0);
				
				if(appState.getFlagcancelado()==1){
					
				}else{
					
					//appState.setEstadoUnidad(EstadosUnidad.TAXI_ASIGNADO);
					appState.setFlagcancelado(0);
				}

				mostrarFotoTaxista=false;
				//imagenRecibida=BitmapFactory.decodeResource(getResources(), R.drawable.sin_foto);
				
				InvisibleBoton();
				Log.i(APPNAME, module+ "eN bREVE.....");
				Log.i(APPNAME, module+ "***ESTADO ACTUAL: "+appState.getEstadoUnidad()+" ***");
				AvisoHablado("ENBREVE ELTAXI DEPLACAS "  + appState.getPlaca() + "loatendera");
				
				appState.setEstadoUnidad(EstadosUnidad.TAXI_ASIGNADO);
				Log.i(APPNAME, module+ "***ESTADO CAMBIO A: "+appState.getEstadoUnidad()+" ***");
										
				Log.i(APPNAME, module+"**********PASA POR FiltrarPushPendientes********");
				Lat = Double.parseDouble(appState.getLatitudMovil());
				Lon = Double.parseDouble(appState.getLongitudMovil());
				
				appState.setChatTaxista(""); 	//Dejar Chat en Blanco, Es un nuevo Servicio...

				int metrosTaxi = restarCoordenadas(Latsave, Lonsave, Lat, Lon);
				appState.setMetros(metrosTaxi);
				
				//nombreConductor.setText(appState.getNombreTaxista());
				//placasTaxi.setText("Placa: " +  appState.getPlaca());
				//distanciaTaxi.setText("Dis: " + appState.getMetros() + " mts");

				Intent in = new Intent(AMapaUpVersion.this, ALlegoTaxi.class);
				startActivityForResult(in, 1);
			
				break;
			case 2:		//No hay movil...

				Log.i(APPNAME,"Entro por c|2");
				if(appState.getEstadoUnidad().equals(EstadosUnidad.SOLICITUD_TAXI)){
					appState.setFlagrecordardireccion(1);
					appState.setFlagsolicitud(1);
														
					appState.setFlaginvisible(1);
					appState.setIdServicio("0");
					
					InvisibleBoton();
					AvisoHablado("LOSENTIMOS NOHAY TAXI DISPONIBLE ENEL MOMENTO PORFAVOR INTENTE DENUEVO ");
					Toast.makeText(context, "LO SENTIMOS NO HAY TAXI DISPONIBLE EN EL MOMENTO!!!" +"\n POR FAVOR INTENTE DE NUEVO", 5000).show();
					appState.setEstadoUnidad(EstadosUnidad.LIBRE);
					if(appState.getIntentosSolicitud() > 1){
						int nuevosIntentos=appState.getIntentosSolicitud()-1;
						appState.setIntentosSolicitud(nuevosIntentos);
						Intent i1 = new Intent(AMapaUpVersion.this, ASolicitarNuevamente.class);
						startActivityForResult(i1, 1);
					}
				
				}
				else{

					if(!appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){
						Intent preguntaTaxi = new Intent();
						preguntaTaxi.putExtra("CMD", C.VERIFICAR_TAXI);
						preguntaTaxi.setAction(C.MAP);
						context.sendBroadcast(preguntaTaxi);
					}
			        
				}
			
				break;
			
			}
		}
		else if(partes[0].equals("{CT")){	//Mensaje del Taxista...
			MensajeTaxista();
		}
		else if(partes[0].equals("{CC")){	//Mensaje de la central...
			MensajeCentral();
		}
		else if(partes[0].equals("{MF")){	//Movil al Frente...
			play_mp();
		}
		else if(partes[0].equals("{j")){	//Llego el Pago...
			appState.setFlagConfPago(7);
		}
	}
	//************************************************************************
//	public void FavoritosMapa(){
//		String listaFavoritos=appState.getListaFavoritos();
//		String direccionesFrecuentes[];
//		int copiaNumFrec=0;
//
//		Log.i(APPNAME, module + " ESTA ES LA INFO DE FAVORITOS: "+ listaFavoritos);
//
//		direccionesFrecuentes=listaFavoritos.split("\\^");
//		Log.i(APPNAME, module + "Longitud de Frecuentes: "+ direccionesFrecuentes.length);
//		copiaNumFrec = direccionesFrecuentes.length;
//		int cont=0;
//
//		for(cont=0;cont< copiaNumFrec;cont++){
//			Log.i(APPNAME, module + "ENTRO AL FOR");
//			switch(cont){
//
//			case(0):
//				String[] sacaInfo = direccionesFrecuentes[0].split("\\|");
//				LatLng SERVICIO1 = new LatLng(Double.parseDouble(sacaInfo[sacaInfo.length-3]), Double.parseDouble(sacaInfo[sacaInfo.length-2]));
//				Marker carro1 = map.addMarker(new MarkerOptions().position(SERVICIO1).icon(BitmapDescriptorFactory.fromResource(R.drawable.botontaxiasignado)));
//			break;
//
//			case(1):
//				String[] sacaInfo1 = direccionesFrecuentes[1].split("\\|");
//				LatLng SERVICIO2 = new LatLng(Double.parseDouble(sacaInfo1[sacaInfo1.length-3]), Double.parseDouble(sacaInfo1[sacaInfo1.length-2]));
//				Marker carro2 = map.addMarker(new MarkerOptions().position(SERVICIO2).icon(BitmapDescriptorFactory.fromResource(R.drawable.botontaxiasignado)));
//			break;
//
//			case(2):
//				String[] sacaInfo2 = direccionesFrecuentes[2].split("\\|");
//				LatLng SERVICIO3 = new LatLng(Double.parseDouble(sacaInfo2[sacaInfo2.length-3]), Double.parseDouble(sacaInfo2[sacaInfo2.length-2]));
//				Marker carro3 = map.addMarker(new MarkerOptions().position(SERVICIO3).icon(BitmapDescriptorFactory.fromResource(R.drawable.botontaxiasignado)));
//			break;
//
//			case(3):
//				String[] sacaInfo3 = direccionesFrecuentes[3].split("\\|");
//				LatLng SERVICIO4 = new LatLng(Double.parseDouble(sacaInfo3[sacaInfo3.length-3]), Double.parseDouble(sacaInfo3[sacaInfo3.length-2]));
//				Marker carro4 = map.addMarker(new MarkerOptions().position(SERVICIO4).icon(BitmapDescriptorFactory.fromResource(R.drawable.botontaxiasignado)));
//			break;
//
//			case(4):
//				
//				String[] sacaInfo4 = direccionesFrecuentes[4].split("\\|");
//				LatLng SERVICIO5 = new LatLng(Double.parseDouble(sacaInfo4[sacaInfo4.length-3]), Double.parseDouble(sacaInfo4[sacaInfo4.length-2]));
//				Marker carro5 = map.addMarker(new MarkerOptions().position(SERVICIO5).icon(BitmapDescriptorFactory.fromResource(R.drawable.botontaxiasignado)));
//				
//			break;
//			}
//		}
//	}
	
	public void FavoritosMapa(double Lat1,double Lon1){
		if(appState.getListaFavoritos().equals("NoHayFavoritos")){	//Pedrilos Al Servidor
			//Ver si se piden....
						
			if(!pedirFavoritos){
				Log.i(APPNAME, module + " NO HAY FAVORITOS CARGADOS AUN...");
				SolicitarFavoritos();
				
			}else{
				Log.i(APPNAME, module + " SE ESTAN CARGANDO LOS FAVORITOS...");
			}
			
		}else{
			String listaFavoritos=appState.getListaFavoritos();
			String direccionesFrecuentes[];
			int copiaNumFrec=0;


			Log.i(APPNAME, module + " ESTA ES LA INFO DE FAVORITOS: "+ listaFavoritos);

			direccionesFrecuentes=listaFavoritos.split("\\^");
			Log.i(APPNAME, module + "Longitud de Frecuentes: "+ direccionesFrecuentes.length);
			copiaNumFrec = direccionesFrecuentes.length;
			int cont=0;
			int distancia=0;
			int distancia_punto=80;

			for(cont=0;cont< copiaNumFrec;cont++){

				Log.i(APPNAME, module + "ENTRO AL FOR DE FAVORITOS...");
				switch(cont){

				case(0):
					String[] sacaInfo = direccionesFrecuentes[0].split("\\|");
				if((sacaInfo[sacaInfo.length-3]!=null)&&(sacaInfo[sacaInfo.length-2]!=null)){
					distancia=restarCoordenadas(Lat1, Lon1, Double.parseDouble(sacaInfo[sacaInfo.length-3]), Double.parseDouble(sacaInfo[sacaInfo.length-2]));
					LatLng SERVICIO1 = new LatLng(Double.parseDouble(sacaInfo[sacaInfo.length-3]), Double.parseDouble(sacaInfo[sacaInfo.length-2]));
					if(distancia > distancia_punto){
						Marker carro1 = map.addMarker(new MarkerOptions().position(SERVICIO1).icon(BitmapDescriptorFactory.fromResource(R.drawable.estrelladirdeseleccionada)));
						tocoIconoFavorito=false;
					}else{
						//Marker carro1 = map.addMarker(new MarkerOptions().position(SERVICIO1).icon(BitmapDescriptorFactory.fromResource(R.drawable.estrelladirok)));
						EnviaInfoAnalytics("Usuario Selecciono Favorito desde el Mapa");
						imgusuario.setImageResource(R.drawable.estrelladirok);
						Log.i(APPNAME, module + "Toco Direccion: 0");
						tocoIconoFavorito=true;
						appState.setEstadoUnidad(EstadosUnidad.PRUEBA_RED);
						appState.setDireccionFrecuente(direccionesFrecuentes[0]);
						CargarInfoFrecuente();
						cont=copiaNumFrec;
					}
				}else{
					SolicitarFavoritos();
					cont=copiaNumFrec;
				}
				break;

				case(1):
					String[] sacaInfo1 = direccionesFrecuentes[1].split("\\|");
				distancia=restarCoordenadas(Lat1, Lon1, Double.parseDouble(sacaInfo1[sacaInfo1.length-3]), Double.parseDouble(sacaInfo1[sacaInfo1.length-2]));
				LatLng SERVICIO2 = new LatLng(Double.parseDouble(sacaInfo1[sacaInfo1.length-3]), Double.parseDouble(sacaInfo1[sacaInfo1.length-2]));
				if(distancia > distancia_punto){
					Marker carro2 = map.addMarker(new MarkerOptions().position(SERVICIO2).icon(BitmapDescriptorFactory.fromResource(R.drawable.estrelladirdeseleccionada)));
					tocoIconoFavorito=false;
				}else{
					//Marker carro2 = map.addMarker(new MarkerOptions().position(SERVICIO2).icon(BitmapDescriptorFactory.fromResource(R.drawable.estrelladirok)));
					EnviaInfoAnalytics("Usuario Selecciono Favorito desde el Mapa");
					imgusuario.setImageResource(R.drawable.estrelladirok);
					Log.i(APPNAME, module + "Toco Direccion: 1");
					tocoIconoFavorito=true;
					appState.setEstadoUnidad(EstadosUnidad.PRUEBA_RED);
					appState.setDireccionFrecuente(direccionesFrecuentes[1]);
					CargarInfoFrecuente();
					cont=copiaNumFrec;
				}
				break;

				case(2):
					String[] sacaInfo2 = direccionesFrecuentes[2].split("\\|");
				distancia=restarCoordenadas(Lat1, Lon1, Double.parseDouble(sacaInfo2[sacaInfo2.length-3]), Double.parseDouble(sacaInfo2[sacaInfo2.length-2]));
				LatLng SERVICIO3 = new LatLng(Double.parseDouble(sacaInfo2[sacaInfo2.length-3]), Double.parseDouble(sacaInfo2[sacaInfo2.length-2]));
				if(distancia > distancia_punto){
					Marker carro3 = map.addMarker(new MarkerOptions().position(SERVICIO3).icon(BitmapDescriptorFactory.fromResource(R.drawable.estrelladirdeseleccionada)));
					tocoIconoFavorito=false;
				}else{
					//Marker carro3 = map.addMarker(new MarkerOptions().position(SERVICIO3).icon(BitmapDescriptorFactory.fromResource(R.drawable.estrelladirok)));
					EnviaInfoAnalytics("Usuario Selecciono Favorito desde el Mapa");
					imgusuario.setImageResource(R.drawable.estrelladirok);
					Log.i(APPNAME, module + "Toco Direccion: 2");
					tocoIconoFavorito=true;
					appState.setEstadoUnidad(EstadosUnidad.PRUEBA_RED);
					appState.setDireccionFrecuente(direccionesFrecuentes[2]);
					CargarInfoFrecuente();
					cont=copiaNumFrec;
				}
				break;

				case(3):
					String[] sacaInfo3 = direccionesFrecuentes[3].split("\\|");
				distancia=restarCoordenadas(Lat1, Lon1, Double.parseDouble(sacaInfo3[sacaInfo3.length-3]), Double.parseDouble(sacaInfo3[sacaInfo3.length-2]));
				LatLng SERVICIO4 = new LatLng(Double.parseDouble(sacaInfo3[sacaInfo3.length-3]), Double.parseDouble(sacaInfo3[sacaInfo3.length-2]));
				if(distancia > distancia_punto){
					Marker carro4 = map.addMarker(new MarkerOptions().position(SERVICIO4).icon(BitmapDescriptorFactory.fromResource(R.drawable.estrelladirdeseleccionada)));
					tocoIconoFavorito=false;
				}else{
					//Marker carro4 = map.addMarker(new MarkerOptions().position(SERVICIO4).icon(BitmapDescriptorFactory.fromResource(R.drawable.estrelladirok)));
					EnviaInfoAnalytics("Usuario Selecciono Favorito desde el Mapa");
					imgusuario.setImageResource(R.drawable.estrelladirok);
					Log.i(APPNAME, module + "Toco Direccion: 3");
					tocoIconoFavorito=true;
					appState.setEstadoUnidad(EstadosUnidad.PRUEBA_RED);
					appState.setDireccionFrecuente(direccionesFrecuentes[3]);
					CargarInfoFrecuente();
					cont=copiaNumFrec;
				}
				break;

				case(4):
					String[] sacaInfo4 = direccionesFrecuentes[4].split("\\|");
				distancia=restarCoordenadas(Lat1, Lon1, Double.parseDouble(sacaInfo4[sacaInfo4.length-3]), Double.parseDouble(sacaInfo4[sacaInfo4.length-2]));
				LatLng SERVICIO5 = new LatLng(Double.parseDouble(sacaInfo4[sacaInfo4.length-3]), Double.parseDouble(sacaInfo4[sacaInfo4.length-2]));
				if(distancia > distancia_punto){
					Marker carro5 = map.addMarker(new MarkerOptions().position(SERVICIO5).icon(BitmapDescriptorFactory.fromResource(R.drawable.estrelladirdeseleccionada)));
					tocoIconoFavorito=false;
				}else{
					//Marker carro5 = map.addMarker(new MarkerOptions().position(SERVICIO5).icon(BitmapDescriptorFactory.fromResource(R.drawable.estrelladirok)));
					EnviaInfoAnalytics("Usuario Selecciono Favorito desde el Mapa");
					imgusuario.setImageResource(R.drawable.estrelladirok);
					Log.i(APPNAME, module + "Toco Direccion: 4");
					tocoIconoFavorito=true;
					appState.setEstadoUnidad(EstadosUnidad.PRUEBA_RED);
					appState.setDireccionFrecuente(direccionesFrecuentes[4]);
					CargarInfoFrecuente();
					cont=copiaNumFrec;
				}

				break;
				}
			}
		}
	}
	//************************************************************************
	public void SolicitarFavoritos(){
		pedirFavoritos=true;
		String favoritos ="Z|0|"+appState.getNumberPhone()+"|"+appState.getCiudadPais().toUpperCase().trim()+"|"+appState.getTramaGPSMap()+"\n";
		//String favoritos ="Z|0|"+appState.getNumberPhone()+"|"+appState.getCiudadPais().toUpperCase().trim()+"\n";

		Intent consultaFavoritos = new Intent();
		consultaFavoritos.putExtra("CMD", C.SEND);
		consultaFavoritos.putExtra("DATA", favoritos);
		consultaFavoritos.setAction(C.COM);
		context.sendBroadcast(consultaFavoritos);
	}
	
	//************************************************************************
	public void CargarInfoFrecuente(){
		Log.i(APPNAME, module +" DEJAR tocoIconoFavorito=true");
		appState.setFlagfrecuentes(1);
		String[] sacaInfo = appState.getDireccionFrecuente().split("\\|");

		String s ="";
		if(sacaInfo[sacaInfo.length-5].contains("~")){	//Trae Adicionales...
			String[] partes = sacaInfo[sacaInfo.length-5].split("~");
			s = partes[0] +"|" + sacaInfo[sacaInfo.length-4]+"|" + partes[1]+"|";
		}
		else	s = sacaInfo[sacaInfo.length-5] +"|" + sacaInfo[sacaInfo.length-4]+"|" + ""+"|";
		
		ColocarInfoDireccion(s);
		
		new Handler().postDelayed(new Runnable(){
			public void run(){

				try {
					Log.i(APPNAME, module +" VUELVE A DEJAR tocoIconoFavorito=false");
					appState.setEstadoUnidad(EstadosUnidad.LIBRE);
					tocoIconoFavorito=false;
					
				} catch (Exception e) {
					e.printStackTrace();
				}	

			};
		}, 3000);
	}
	//************************************************************************
	public void ColocarInfoDireccion(String Savedireccion ){

		try{
			FileOutputStream fos = openFileOutput("textFile.txt", MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);

			osw.write(Savedireccion);
			osw.flush();
			osw.close();

			//Toast.makeText(getApplicationContext(), "GUARDADO", Toast.LENGTH_LONG).show();

		}catch (Exception e){
			e.printStackTrace();
		}

		adicionalesUsuario="";
		boolean hayAdicional=false;
		String[] separaDir=Savedireccion.split("\\|");
		if(separaDir.length==2)	Log.i(APPNAME, module +"Direccion Modificada: " + separaDir[0] + " Barrio Modificacdo: " + separaDir[1] + " Adicionales: No Colocaron");
		else{
			Log.i(APPNAME, module +"Direccion Modificada: " + separaDir[0] + " Barrio Modificacdo: " + separaDir[1] + " Adicionales: " + separaDir[2]);
			adicionalesUsuario=separaDir[2];
			hayAdicional=true;
		}
		avisodireccion.setText(separaDir[0]);
		appState.setDireccionUsuario(separaDir[0]);
		appState.setBarrio(separaDir[1]);
		appState.setFlagrecordardireccion(1);
		barraEnSolicitud.setVisibility(View.VISIBLE);

		if(appState.getFlagfrecuentes()==1){	//Se debe mover el mapa al punto...
			try{
				String[] sacaInfo = appState.getDireccionFrecuente().split("\\|");
				movimientoIconoUsuario=false;
				//if(!tocoIconoFavorito){
				com.google.android.gms.maps.model.LatLng ACTUAL = new LatLng(Double.parseDouble(sacaInfo[sacaInfo.length-3]), Double.parseDouble(sacaInfo[sacaInfo.length-2]));
				//map.moveCamera(CameraUpdateFactory.newLatLngZoom(ACTUAL, 16));
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(ACTUAL, 18));
				//}
			}catch(Exception e){
				e.printStackTrace();
				Toast.makeText(context, "NO HAY SEÑAL DE GPS VALIDA", Toast.LENGTH_LONG).show();
			}
		}else{
			Log.i(APPNAME, module +"No fue de Favoritos");
		}

		if(appState.isGuardarFavorito()){	//Hay que enviar a guardar Favorito
			appState.setGuardarFavorito(false);
			String dirAlmacenar="";
			if(hayAdicional){
				dirAlmacenar = "Z|1|"+appState.getNumberPhone()+"|"+appState.getCiudadPais().toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + appState.getDireccionUsuario()  +
						"~"+adicionalesUsuario+"|" + appState.getBarrio() + "|" +  appState.getTramaGPSMap()+"|"+appState.getCorreoUsuario()+"|"+appState.getNombreUsuario()+"\n";
			}else{
				dirAlmacenar = "Z|1|"+appState.getNumberPhone()+"|"+appState.getCiudadPais().toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + appState.getDireccionUsuario()  +
						"|" + appState.getBarrio() + "|" +  appState.getTramaGPSMap()+"|"+appState.getCorreoUsuario()+"|"+appState.getNombreUsuario()+"\n";
			}
			Intent almacenarFavoritos = new Intent();
			almacenarFavoritos.putExtra("CMD", C.SEND);
			almacenarFavoritos.putExtra("DATA", dirAlmacenar);
			almacenarFavoritos.setAction(C.COM);
			context.sendBroadcast(almacenarFavoritos);

			progDailog = new ProgressDialog(AMapaUpVersion.this);

			progDailog.setTitle("ALMACENANDO EN FAVORITOS");
			progDailog.setMessage("ESPERE UN MOMENTO... ");
			progDailog.setCancelable(true);
			progDailog.show();

			EsperarRtaEncriptada();

		}else{
			Log.i(APPNAME, module +"No hay que almacenar Favoritos");
		}

	}
	//************************************************************************
	public static Bitmap getBitmapFromDrawable(Drawable drawable){
		Bitmap bitmap = null;
		try {
			BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
			bitmap = bitmapDrawable.getBitmap();
			return bitmap;

		} catch (Exception e) {
			Log.i("getBitmapFromDrawable", "%%%%%%%%%%%%%%%%%%%%%%NO CARGA NADA%%%%%%%%%%%%%%%%%%%%%%%%");
			e.printStackTrace();
			return null;
		}

	}
	//************************************************************************
	public static long getFreeMemory() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}
	//************************************************************************
	public void ColocarInfoTaxista(){
		if(mostrarFotoTaxista){
			Bitmap imagenMostrar;
			try{
				Log.i(APPNAME, module + "DENSIDAD TOMADA PARA LA FOTO: "+appState.getDensidad());
				if(appState.getDensidad() > 639){	//640
					//imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*8), (int)(imagenRecibida.getHeight()*8), true);
					imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*6), (int)(imagenRecibida.getHeight()*6), true);
				}else if(appState.getDensidad() > 479){	//480
					//imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*6), (int)(imagenRecibida.getHeight()*6), true);
					imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*4), (int)(imagenRecibida.getHeight()*4), true);
				}else if(appState.getDensidad() > 319){	//320
					//imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*4), (int)(imagenRecibida.getHeight()*4), true);
					imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*3), (int)(imagenRecibida.getHeight()*3), true);
				}else if(appState.getDensidad() > 239){	//240
					//imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*3), (int)(imagenRecibida.getHeight()*3), true);
					imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*2), (int)(imagenRecibida.getHeight()*2), true);
				}else{	//160
					//imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*2), (int)(imagenRecibida.getHeight()*2), true);
					imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*1.5), (int)(imagenRecibida.getHeight()*1.5), true);
				}

				//fotoTaxistaMapa.setVisibility(View.VISIBLE);
				fotoTaxistaMapa.setImageBitmap(imagenMostrar);
				//fotoTaxistaMapa.setVisibility(View.VISIBLE);
				//infoTaxista.setVisibility(View.VISIBLE);
			}catch (Exception ex) {
				Toast.makeText(context, "NO FUE POSIBLE CARGAR LA FOTO DEL TAXISTA...", Toast.LENGTH_LONG).show();
			}
			fotoTaxistaMapa.setVisibility(View.VISIBLE);
			
			Log.i(APPNAME, module +"NOMBRE: "+ appState.getNombreTaxista());
			Log.i(APPNAME, module +"PLACA: "+  appState.getPlaca());
			Log.i(APPNAME, module +"DISTANCIA: "+ appState.getMetros());
			
			nombreConductor.setText(appState.getNombreTaxista());
			placasTaxi.setText("Placa: " +  appState.getPlaca());
			distanciaTaxi.setText("Dis: " + appState.getMetros() + " mts");
									
			infoTaxista.setVisibility(View.VISIBLE);
			
		}else{
			Log.i(APPNAME, module +"mostrarFotoTaxista=false");
			
		}
	}
	//************************************************************************
	@Override
	public void onResume() {
		final Context context = getApplicationContext();
		super.onResume();
		Log.i(APPNAME, module + " Entra por onResume");
		registerReceiver(receiver, filter);
		mobileAppTracker.trackSession();
		appState.setActividad(module);
		com.facebook.AppEventsLogger.activateApp(context, "245686422277683");
		if(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE))	barraEnSolicitud.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//System.gc();
		//getFreeMemory();
		Log.i(APPNAME, module + " Entra por onPause");
		unregisterReceiver(receiver);
		
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
	
	/************************************************************************************************/
	public void MensajeCentral (){
		AvisoHablado("NUEVO MENSAJE DELACENTRAL");
		Log.i(APPNAME, module + "Verificar si el mensaje tiene comas...");
		String cadena = appState.getMensajeCentral();
		cadena=cadena.replace(",","");
		appState.setMensajeCentral(cadena);


		String adicionarMensaje = appState.getChatCentral()+"C|"+ appState.getMensajeCentral()+",";
		appState.setChatCentral(adicionarMensaje);

		Intent i = new Intent(AMapaUpVersion.this, AChatCentral.class);
		startActivityForResult(i, 1);
	}
	/************************************************************************************************/
	public void SolicitarNuevamente(){
		
		Log.i("Dialogos", "Nueva Solicitud de Servicio");
		String servicio= appState.getUltimoServicio();
		appState.setEstadoUnidad(EstadosUnidad.ESPERA);
		Intent newservice = new Intent();
		newservice.putExtra("CMD", C.SEND);
		newservice.putExtra("DATA",servicio);
		newservice.setAction(C.COM);
		context.sendBroadcast(newservice);

		ColocarEsperaServicio();
		
		EsperarRtaEncriptada();

		AvisoHablado("BUSCANDO TAXI MASCERCANO");
	
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
		/************************************************************************************************/		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(APPNAME, module + "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);

		Log.i(APPNAME, module + "_____________________________ " + requestCode +"    " + resultCode + "   " + data);

		try{
			if(data.getExtras().containsKey("pedirTaxi")){
				Log.i(APPNAME, module + data.getStringExtra("pedirTaxi"));
				String[] separaDir=data.getStringExtra("pedirTaxi").split("\\|");			    	 
				Log.i(APPNAME, module + "ESTE 1 " + separaDir[0] + " ESTE 2 " + separaDir[1]);
				appState.setDireccionUsuario(separaDir[0]);
				appState.setBarrio(separaDir[1]);

				barraEnSolicitud.setVisibility(View.VISIBLE);
				PedirElTaxi();

			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("nohacernada")){
				Log.i(APPNAME, "*******NO SE HACE ABSOLUTAMENTE NADA*******");
			}
			else if(data.getExtras().containsKey("cancela")){
				Log.i(APPNAME, "******************************************");

				barraEnSolicitud.setVisibility(View.VISIBLE);
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("cerrarApp")){
				CerrardesdeMapa();
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("completoDireccion")){
				Log.i(APPNAME, module + data.getStringExtra("completoDireccion"));
				/********************************SOLICITUD DE TAXI*******************/
				/*****************PARA GUARDAR LA DIRECCION Y NO SE BORRE*****************/
				ColocarInfoDireccion(data.getStringExtra("completoDireccion"));
								
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("confirmarsolicitud")){
				appState.setIntentosSolicitud(5);
				EnviarSolicitud();
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("llegoimagen")){
			//else if((data.getExtras().containsKey("llegoimagen"))||(data.getExtras().containsKey("noimagen"))){
				byte[] foto = data.getByteArrayExtra("llegoimagen");
				imagenRecibida=BitmapFactory.decodeByteArray(foto,0,foto.length);
				Log.i(APPNAME, module+"**********RECIBIO  IMAGEN DESDE LLEGO TAXI ********");
				new Handler().postDelayed(new Runnable(){
					public void run(){
						mostrarFotoTaxista=true;
						ColocarInfoTaxista();
					};
				}, 500);
				//ColocarInfoTaxista();
//				String informacion = "I|"+ appState.getIdServicio()+"\n";
//				Intent solimage = new Intent();
//				solimage.putExtra("CMD", C.SEND);
//				solimage.putExtra("DATA", informacion);
//				solimage.setAction(C.COM);
//				context.sendBroadcast(solimage);
//				Log.i(APPNAME, module+"**********SOLICITAR IMAGEN********"+ informacion);
				
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("noimagen")){
				imagenRecibida=BitmapFactory.decodeResource(getResources(), R.drawable.sin_foto);
				Log.i(APPNAME, module+"********** NO RECIBIO IMAGEN DESDE LLEGO TAXI********");
				mostrarFotoTaxista=true;
				ColocarInfoTaxista();
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("salidaalfrente")){
				flagaviso=7;
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("abordotaxi")){

				EnviaInfoAnalytics("Usuario Confirma Abordo del Taxi");
				
				appState.setFlagAnunciotaxi(0);
				ventanaAbordo=true;

				Toast.makeText(context, "USUARIO A BORDO DEL TAXI", Toast.LENGTH_LONG).show();
				/*******Se Coloca invisible los botones***************/
				appState.setFlagCerrarMapa(2);
				appState.setFlaginvisible(1);
				
				InvisibleBoton();
				
				appState.setFlagAnunciotaxi(0);
				appState.setFlagcancelado(1);
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("noabordotaxi")){
				ReconfirmarAbordo();
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("nuevaSolicitud")){
				EnviaInfoAnalytics("Nueva Solicitud x Cancelacion del Taxista");
				SolicitarNuevamente();
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("compartirServicio")){
				
				Log.i("COMPARTIR SERVICIO: ", "Compartir Datos del Servicio");

				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);

				if((appState.getFormaPago()== 1)||(appState.getFormaPago()== 3)){//Es pago con Vale
					EnviaInfoAnalytics("Usuario Comparte Info Servicio pago con Vale");
					sendIntent.putExtra(Intent.EXTRA_TEXT,"Yo estoy abordo de un #TaxiSeguro de @TaxisLibresCol con Vale Digital"+
							" Placa: "+appState.getPlaca()+ " ver más www.taxislibres.com.co");
				}else if(appState.getFormaPago()== 4){	//Es Pago con Tarjeta de Credito...
					EnviaInfoAnalytics("Usuario Comparte Info Servicio pago con T. Credito");
					sendIntent.putExtra(Intent.EXTRA_TEXT,"Yo estoy abordo de un #TaxiSeguro de @TaxisLibresCol pagado con Tarjeta de Crédito"+
							" Placa: "+appState.getPlaca()+ " ver más www.taxislibres.com.co");
				}
				else{
					EnviaInfoAnalytics("Usuario Comparte Info Servicio pago en Efectivo");
					sendIntent.putExtra(Intent.EXTRA_TEXT,"Tomé Taxi por la app TAXIS LIBRES Conductor: "+
							appState.getNombreTaxista()+" Placa: "+appState.getPlaca()+
							" Descarga aquí: http://goo.gl/aXdtll");
				}
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
			
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("reconfirmaNoAbordo")){
				EnviaInfoAnalytics("Usuario informa NO Abordo del Taxi");
				String tramaQrt = "T|"+ appState.getPinMovil() +"|"+appState.getIdServicio()+"|"+appState.getTramaGPSMap()+"\n";
				Intent sendqrt = new Intent();
				sendqrt.putExtra("CMD", C.SEND);
				sendqrt.putExtra("DATA", tramaQrt);
				sendqrt.setAction(C.COM);
				context.sendBroadcast(sendqrt);
				Log.i(APPNAME, module+"**********ENVIAR QRT********"+ tramaQrt);
				NuevoServicio();
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("cancelarServicio")){
				
				EnviaInfoAnalytics("Usuario Acepta Cancelar el Servicio");
				
				Log.i("Dialogos", "Confirmacion Aceptada.");
				appState.setEstadoUnidad(EstadosUnidad.CANCELADO);		       

				appState.setFlagcancelado(1);
				String cancelar_servicio = "D|" + appState.getIdServicio() +"\n";

				Intent canservice = new Intent();
				canservice.putExtra("CMD", C.SEND);
				canservice.putExtra("DATA", cancelar_servicio);
				canservice.setAction(C.COM);
				context.sendBroadcast(canservice);

				AvisoHablado("SERVICIO CANCELADO");

				map.clear();
				barraEnSolicitud.setVisibility(View.VISIBLE);
				barraEnServicio.setVisibility(View.INVISIBLE);
				fotoTaxistaMapa.setVisibility(View.INVISIBLE);
				infoTaxista.setVisibility(View.INVISIBLE);
				barraCalificar.setVisibility(View.INVISIBLE);
				avisomarket.setVisibility(View.VISIBLE);
				imgusuario.setVisibility(View.VISIBLE);

				Toast.makeText(context, "SERVICIO CANCELADO", Toast.LENGTH_LONG).show();
				
				if(noMostrarAsignacion){//CerrarApp entro forzado...
					
					new Handler().postDelayed(new Runnable(){
						public void run(){

							try {
								CerrardesdeMapa();
								
							} catch (Exception e) {
								e.printStackTrace();
							}	

						};
					}, 3000);
					
					//CerrardesdeMapa();
				}

			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("cafilicarServicio")){
								
				EnviaInfoAnalytics("Presiono Calificar Servicio");

				Log.i("Dialogos", "Confirmacion Aceptada.");
				//Nuevo almacenamiento... Se coloca forma de Pago

				String almacenarDb=""; 
				if(appState.getFormaPago()==0){	//Es Efectivo
					//almacenarDb = "DIRECCION: "+ appState.getDireccionUsuario() + " PLACA: "+ appState.getPlaca()+" CONDUCTOR: " + appState.getNombreTaxista()+" PAGO: EFECTIVO";
					almacenarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() + "\n"+"PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: EFECTIVO";
					GuardarInfoDataBase(almacenarDb,"\n"+"SERVICIO CUMPLIDO");
				}else if(appState.getFormaPago()==1){	//Es Vale Digital
					almacenarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() +"\n"+ "PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: VALE DIGITAL"+"\n"+"Valor del Servicio: "+appState.getValorPagar();
					GuardarInfoDataBase(almacenarDb,"\n"+"SERVICIO CUMPLIDO");
				}else{	//Es Vale Fisico
					almacenarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() +"\n"+ "PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: VALE FISICO" +"\n"+"Valor del Servicio: "+appState.getValorPagar();
					GuardarInfoDataBase(almacenarDb,"\n"+"SERVICIO CUMPLIDO");
				}

				appState.setFlagAnunciotaxi(0);
				Toast.makeText(context, "USUARIO A BORDO DEL TAXI ", Toast.LENGTH_LONG).show(); 			     			     											

				AvisoHablado("GRACIAS PORUTILIZAR TAXIS LIBRES... TAXIS LIBRES SILE RESPONDE");

				barraEnSolicitud.setVisibility(View.VISIBLE);	
				barraEnServicio.setVisibility(View.INVISIBLE);
				fotoTaxistaMapa.setVisibility(View.INVISIBLE);
				infoTaxista.setVisibility(View.INVISIBLE);

				Intent califi = new Intent();
				califi.setClass(AMapaUpVersion.this, AFormulario.class);		        		    								
				startActivity(califi);
				finish();
			
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("noCalificarServicio")){
				
				EnviaInfoAnalytics("Usuario Presiono No Calificar o no presiono nada");

				Log.i("Dialogos", "Confirmacion Cancelada.");
				appState.setEstadoUnidad(EstadosUnidad.LIBRE);

				String almacenarDb=""; 
				if(appState.getFormaPago()==0){	//Es Efectivo
					//almacenarDb = "DIRECCION: "+ appState.getDireccionUsuario() + " PLACA: "+ appState.getPlaca()+" CONDUCTOR: " + appState.getNombreTaxista()+" PAGO: EFECTIVO";
					almacenarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() + "\n"+"PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: EFECTIVO";
					GuardarInfoDataBase(almacenarDb,"\n"+"SERVICIO CUMPLIDO");
				}else if(appState.getFormaPago()==1){	//Es Vale Digital
					almacenarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() +"\n"+ "PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: VALE DIGITAL"+"\n"+"Valor del Servicio: "+appState.getValorPagar();
					GuardarInfoDataBase(almacenarDb,"\n"+"SERVICIO CUMPLIDO");
				}else{	//Es Vale Fisico
					almacenarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() +"\n"+ "PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: VALE FISICO" +"\n"+"Valor del Servicio: "+appState.getValorPagar();
					GuardarInfoDataBase(almacenarDb,"\n"+"SERVICIO CUMPLIDO");
				}

				String sinEncuesta = "E|" + appState.getNumberPhone() + "|" + "4" + "|" + "-1" + "|" + "-1" +
						"|" +"" +  "|" + 
						appState.getIdServicio() + "|" + appState.getPinMovil()+ "|" + appState.getCorreoUsuario()+
						"|" + appState.getNombreUsuario()+"\n";

				//appState.setFormutotal(sinEncuesta);

				try{							

					Log.i(APPNAME, module + ": Se Envia: " + sinEncuesta);

					Intent form = new Intent();
					form.putExtra("CMD", C.SEND);
					form.putExtra("DATA",sinEncuesta);
					form.setAction(C.COM);
					getApplicationContext().sendBroadcast(form);

					Intent talk = new Intent();
					talk.setAction(C.TEXT_TO_SPEECH);
					talk.putExtra("CMD", C.HABLAR);
					talk.putExtra("TEXTHABLA", "GRACIAS PORPREFERIRNOS      "  + appState.getNombreUsuario());
					sendBroadcast(talk);

				}catch(Exception e){
					Log.i(APPNAME, module + ": Se Reventooooooooo " +  e);
					e.printStackTrace();
				}

				Log.i(APPNAME, module + "Entro al Delay");
				new Handler().postDelayed(new Runnable(){
					public void run(){
						//Pasan 3 segundos
						Log.i(APPNAME, module + "Sale del Delay");
						appState.setEstadoUnidad(EstadosUnidad.LIBRE);	//Es efectivo
						Log.i(APPNAME, module + "PASA X CERRAR DESDE MAPA");
						CerrardesdeMapa();;
					};
				}, 3000);
			
			}
			
			//****************************************************************************************
			else if(data.getExtras().containsKey("aceptarPago")){

				String almacenarDb=""; 
				if(appState.getFormaPago()==0){	//Es Efectivo
					//almacenarDb = "DIRECCION: "+ appState.getDireccionUsuario() + " PLACA: "+ appState.getPlaca()+" CONDUCTOR: " + appState.getNombreTaxista()+" PAGO: EFECTIVO";
					almacenarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() + "\n"+"PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: EFECTIVO";
					GuardarInfoDataBase(almacenarDb,"\n"+"SERVICIO CUMPLIDO");
				}else if(appState.getFormaPago()==4){
					if(appState.isPagoTarjetaExitoso())	almacenarDb ="\n"+ "DIRECCION: "+ appState.getDireccionUsuario() + "\n"+"PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: TARJETA DE CREDITO";
					else almacenarDb ="\n"+ "DIRECCION: "+ appState.getDireccionUsuario() + "\n"+"PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: EN EFECTIVO, EL COBRO CON TARJETA NO PUDO REALIZARSE";
					GuardarInfoDataBase(almacenarDb,"\n"+"SERVICIO CUMPLIDO");
				}
				else if(appState.getFormaPago()==1){	//Es Vale Digital
					almacenarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() +"\n"+ "PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: VALE DIGITAL"+"\n"+"Valor del Servicio: "+appState.getValorPagar();
					GuardarInfoDataBase(almacenarDb,"\n"+"SERVICIO CUMPLIDO");
				}else{	//Es Vale Fisico
					almacenarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() +"\n"+ "PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: VALE FISICO" +"\n"+"Valor del Servicio: "+appState.getValorPagar();
					GuardarInfoDataBase(almacenarDb,"\n"+"SERVICIO CUMPLIDO");
				}

				appState.setFlagCerrarMapa(0);
				appState.setEstadoUnidad(EstadosUnidad.ENCUESTA);

				Intent califi = new Intent();
				califi.setClass(AMapaUpVersion.this, AFormulario.class);		        		    				
				startActivity(califi);
				finish();
				
			}
			//****************************************************************************************
//			else if(data.getExtras().containsKey("enviarMsgContact")){
//				String sendMsg = data.getStringExtra("enviarMsgContact");
//				String sendMsg1 = quitarCaracteresEsp(sendMsg);
//				Toast.makeText(getApplicationContext(), "SE ENVIA EL MENSAJE... ", Toast.LENGTH_LONG).show();
//				String Mensaje_Taxista = "F|" + "0" +  "|" + sendMsg1 +"|" + appState.getNumberPhone() + "|" + appState.getNombreUsuario() 
//						+"|"+ appState.getPinMovil() + "|"+appState.getPlaca() +  "|" +appState.getIdServicio() +  "|"+ appState.getDireccionServicio() + "\n";
//
//				Intent msg = new Intent();
//				msg.putExtra("CMD", C.SEND);
//				msg.putExtra("DATA", Mensaje_Taxista);
//				msg.setAction(C.COM);
//				getApplicationContext().sendBroadcast(msg);
//				
//			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("borrarHistorico")){
								
				EnviaInfoAnalytics("Usuario Acepto Borrar Historico de Servicios");
				
				final Handler_sqlite helper = new Handler_sqlite(this);
				Log.i(APPNAME, module + ": Borra el Historico de los Servicios"); 			    						    	
				String vector[]=helper.leer();
				helper.deleteAllMessages();
				Log.i(APPNAME, module + "Borro la base!!!");
				helper.cerrar();
				AvisoHablado("HISTORICO BORRADO CORRECTAMENTE");
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("pasarEncuesta")){
				Log.e(APPNAME, module + "PASA_ENCUESTA: Hay que pasar a la actividad del Formulario");
				appState.setFlagCerrarMapa(0);
				appState.setEstadoUnidad(EstadosUnidad.ENCUESTA);
				Intent califi = new Intent();
				califi.setClass(AMapaUpVersion.this, AFormulario.class);		        		    				
				startActivity(califi);
				finish();
			}
			//****************************************************************************************
//			else if(data.getExtras().containsKey("enviarMsgTaxista")){
//				String sendMsg = data.getStringExtra("enviarMsgTaxista");
//				String sendMsg1 = quitarCaracteresEsp(sendMsg);
//				Toast.makeText(context, "SE ENVIA EL MENSAJE... "  , Toast.LENGTH_LONG).show();
//				String Mensaje_Taxista = "F|" + "1" +  "|" + sendMsg1 +"|" + appState.getNumberPhone() + "|" + appState.getNombreUsuario() 
//						+"|"+ appState.getPinMovil() + "|"+appState.getPlaca() +  "|" +appState.getIdServicio() +  "|"+ appState.getDireccionServicio() + "\n";
//
//				Intent msg = new Intent();
//				msg.putExtra("CMD", C.SEND);
//				msg.putExtra("DATA", Mensaje_Taxista);
//				msg.setAction(C.COM);
//				context.sendBroadcast(msg);
//
//			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("consultarTarjetas")){
				ConsultarTarjetas();
			}
			//****************************************************************************************
			else if(data.getExtras().containsKey("NoverPromo")){
				//Enviar no mostrar mas promos... bloquearMsgPromo|pepe@gmail.com|Visa 5000
				EnviaInfoAnalytics("Usuario Bloquea Ver Promociones");
				String listar_promo = "bloquearMsgPromo|"+ appState.getCorreoUsuario()+"|"+appState.getNombrePromo();
				appState.setComandoPayu("BLOQUEARMSG_PROMO");
				Intent service = new Intent();
				service.putExtra("CMD", C.ENCRIPTADO);
				service.putExtra("DATA", listar_promo);
				service.setAction(C.COM);
				getApplicationContext().sendBroadcast(service);
			}
			else if(data.getExtras().containsKey("SeleccionoVale")){
				botonFormaPago.setImageResource(R.drawable.btnvaleok);
				botonFormaPago.setClickable(true);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//**********************************************************************************
		if (requestCode == 0) {

	        if (resultCode == RESULT_OK) {
	            String contents = data.getStringExtra("SCAN_RESULT");
	            Log.i( "AScanQr", "Info del QR: "+ contents);
	            appState.setUrlInfoTaxista(contents);
	            Intent scaner = new Intent();
	            scaner.setClass(context, AVerInfoScaner.class);
	    		startActivity(scaner);
	        }
	        if(resultCode == RESULT_CANCELED){
	            //handle cancel
	        	Log.i( "AScanQr", "Error: "+ RESULT_CANCELED);
	        }
	    }
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), "Entra al GPSSSSSSSSS", Toast.LENGTH_LONG).show();
		Log.i(APPNAME, module + "onLocationChanged");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onProviderDisabled");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onProviderEnabled");

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onStatusChanged");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i(APPNAME, module + "onSaveInstanceState");
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		//Guardar mis variables en servicio
	}

	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "dispatchGenericMotionEvent");
		movimientoIconoUsuario=true;
		if(appState.getEstadoUnidad().equals(EstadosUnidad.LLEGO_CARRO)&&(ventanaAbordo)){	//Estaba en la Ventana Usted esta abordo del taxi....
			ventanaAbordo=false;
			Intent nuevaVentana = new Intent();
			nuevaVentana.putExtra("CMD", C.VERIFICAR_TAXI);
			nuevaVentana.setAction(C.MAP);
			getApplicationContext().sendBroadcast(nuevaVentana);	

		}
		
		return super.dispatchGenericMotionEvent(ev);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "dispatchKeyEvent");
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "dispatchKeyShortcutEvent");
		return super.dispatchKeyShortcutEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "dispatchTouchEvent");
		movimientoIconoUsuario=true;
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean dispatchTrackballEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "dispatchTrackballEvent");
		return super.dispatchTrackballEvent(ev);
	}

	@Override
	public void onAttachFragment(android.app.Fragment fragment) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onAttachFragment");
		super.onAttachFragment(fragment);
	}

	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onKeyShortcut");
		return super.onKeyShortcut(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onKeyUp");
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onMenuOpened");
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onNavigateUp() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onNavigateUp");
		return super.onNavigateUp();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onTouchEvent");
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onTrackballEvent");
		return super.onTrackballEvent(event);
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onUserInteraction");
		super.onUserInteraction();
	}

}
