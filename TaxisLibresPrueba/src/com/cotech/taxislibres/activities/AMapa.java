package com.cotech.taxislibres.activities;

import com.mobileapptracker.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
//import android.util.Log;
import com.cotech.taxislibres.activities.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.EstadosUnidad;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.database.Handler_sqlite;
import com.cotech.taxislibres.services.ServiceTCP;
import com.cotech.taxislibres.services.ServiceTTS;
import com.cotech.taxislibres.services.ServiceTimer;
import com.cotech.taxislibres.view.viewgroup.FlyOutContainer;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobileapptracker.MobileAppTracker;


@SuppressLint("NewApi")
public class AMapa extends FragmentActivity implements  LocationListener{
	
	public MobileAppTracker mobileAppTracker = null;
	
	protected String APPNAME = "TaxisLibres";
    protected String module = C.MAP;
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
	
	private EditText direccionusuario;
	private EditText direccion;
	
	
	private ImageButton calificar;
	private ImageButton trabajo;
		
	private ImageButton cancelar;
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
	
	
	MarkerOptions markerOtions;
	LatLng latlng;
	
	
	
	private int flagMenu;
	private ListView list;
    FlyOutContainer root;			//pARA EL SlidingMenu
    
	
    /*************Nov29/2013****************/
    private TextView avisousuario;
    //private TextView barriousuario;
    private TextView avisodireccion;
    
    private ImageView imgusuario;
    private LinearLayout avisomarket;
    /**************************************************************/
	private String newnombre;
	private String newtelefono;
	/**************************************************************/
	public MediaPlayer mp;
	static final int READ_BLOCK_SIZE = 200;
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
	private int contaviso = 0;
	private int flagaviso = 0;
	
    public final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {									
			Integer cmd = intent.getIntExtra("CMD", 0);
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
				if(appState.getFlaginvisible()==1){
					map.clear();
					trabajo.setVisibility(View.VISIBLE);
					cancelar.setVisibility(View.INVISIBLE);
					calificar.setVisibility(View.INVISIBLE);
				}else{
					imgusuario.setVisibility(View.INVISIBLE);
					avisomarket.setVisibility(View.INVISIBLE);
					trabajo.setVisibility(View.INVISIBLE);
					cancelar.setVisibility(View.VISIBLE);		
					calificar.setVisibility(View.VISIBLE);
				}
				if(appState.getFlagCerrarMapa()== 2){
					Log.e(APPNAME, module + "Pasa a Finish Mapa");
					appState.setFlagCerrarMapa(0);
					Intent califi = new Intent();
					califi.setClass(getApplicationContext(), AFormulario.class);		        		    				
			        startActivity(califi);
				    finish();
				}
				
				break;
			case C.AVISO_TAXI:	
					AvisoTaxiAsignado();
					String informacion= "DIRECCION: "+ appState.getDireccionUsuario() + " PLACA: "+ appState.getPlaca()+" CONDUCTOR: " + appState.getNombreTaxista();
					GuardarInfoDataBase(informacion,"SERVICIO ASIGNADO");
					informacion = "I|"+ appState.getIdServicio()+"\n";
					Intent solimage = new Intent();
					solimage.putExtra("CMD", C.SEND);
					solimage.putExtra("DATA", informacion);
					solimage.setAction(C.COM);
					getApplicationContext().sendBroadcast(solimage);
					Log.i(APPNAME, module+"**********SOLICITAR IMAGEN********"+ informacion);
				break;
			case C.UBICACION:
				ubicacionGoogle();
				break;
			case C.PREGUNTAMOVIL:
				PreguntaMovil();
				break;
			case C.MSGTAXISTA:
					MensajeTaxista();
				break;
			case C.SONIDOALFRENTE:
					play_mp();
				break;
				
			case C.CERRARAPP:
				CerrardesdeMapa();
			break;
			
			case C.RECIBIOIMAGEN:
				final byte[] data = intent.getByteArrayExtra("EXTRA");
				imagenRecibida=BitmapFactory.decodeByteArray(data,0,data.length);
				Log.i(APPNAME, module+"**********RECIBIO  IMAGEN EN EL MAPA********");
			break;
			}
		}
	}; 
    	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);    //Para el tietle bar
		final Context context = getApplicationContext();
		appState = ((TaxisLi) context);
		//setContentView(R.layout.mapa);
		this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.mapa, null);		
		setContentView(root);
				
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);  //Para el tietle bar
		final PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
		this.wakelock=pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "etiqueta");
        wakelock.acquire();
		
     // Initialize MAT
        MobileAppTracker.init(getApplicationContext(), "12116", "6b69c1b7197136782c62cf508d25c98b");
        mobileAppTracker = MobileAppTracker.getInstance();
        mobileAppTracker.setReferralSources(this);

        mobileAppTracker.setExistingUser(true);
        
        new Thread(new Runnable() {
            @Override public void run() {
                // See sample code at http://developer.android.com/google/play-services/id.html
                try {
                    Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
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
		
		cancelar=(ImageButton) findViewById(R.id.cancelar);			
		calificar=(ImageButton)findViewById(R.id.calificar);
		trabajo=(ImageButton)findViewById(R.id.solicitar);
		avisousuario=(TextView) findViewById(R.id.usuario);
//		barriousuario=(TextView) findViewById(R.id.barriousuario);
		avisodireccion=(TextView) findViewById(R.id.direccionusuario);
		imgusuario=(ImageView)findViewById(R.id.yo);
		avisomarket= (LinearLayout)findViewById(R.id.avisomarket);
		
	
		
		if(appState.getEstadoUnidad().equals(EstadosUnidad.ENCUESTA))appState.setEstadoUnidad(EstadosUnidad.LIBRE);
		
		LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		//Colocar verificar Menu de Gps...
		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			//Colocar Aviso de Habilitar los Servicios del gps..
			Log.i(APPNAME, module + ": HAY QUE HABILITAR EL GPS!!!");
			
			final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(this);
			   alertaSimple.setTitle("HABILITAR SERVICIOS DE UBICACION");
			   alertaSimple.setMessage("SE DEBEN HABILITAR LOS SERVICIOS DE UBICACION PARA UN OPTIMO FUNCIONAMIENTO");
			   alertaSimple.setPositiveButton("Aceptar",
			   new DialogInterface.OnClickListener() {
			     public void onClick(DialogInterface dialog, int which) {
			    	
			    	 Intent in = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);   
					 startActivity(in);
					//Entra a mostrar el menu de Gps					    	    	 					    	    	 					    	    	
			    	    	 
			    	}
			    });  
			    alertaSimple.show();
			
			
			
	    }
		
										
		try{
			InitMapa();
			handler = new Handler(); 
		}catch(Exception e){
			e.printStackTrace();
			Log.e(APPNAME, module + ": Erro en Init Mapa..." + e); 		
		}

		try{
			map.setOnCameraChangeListener(new OnCameraChangeListener() {
				@Override
				public void onCameraChange(final CameraPosition position) {
					
				if(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE))	{
						try{
							
							Latsave = position.target.latitude;
							Lonsave = position.target.longitude;
							
							appState.setLatitudUsuario(Latsave);
							appState.setLongitudUsuario(Lonsave);
							
							UBICACION = new LatLng(position.target.latitude, position.target.longitude);
							LatLng= new LatLng(position.target.latitude, position.target.longitude);
							
							if((position.target.latitude != 0)||(position.target.longitude != 0)){
								appState.setTramaGPSMap(position.target.latitude+ "|" + position.target.longitude);
								trabajo.setVisibility(View.VISIBLE);
							}

							Thread thrd = new Thread(){
								public void run(){
											

									
									   JSONObject ret = getLocationInfo(Latsave, Lonsave); 
									   JSONObject location;
									   JSONObject bar=ret;
									   String location_string;
									   String barrio_string;
									   try {
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
									    	   pais= quitarCaracteresEsp(addressData[3]);

									    	   barrio= quitarCaracteresEsp(sacaBarrio[0]);
									    	   barrio=barrio.substring(3);
									    	   
									    	   appState.setBarrio(barrio);

									    	   //Log.i(module,"EL RESTO DE INFO: "+ addressData[1] + addressData[2] + addressData[3]);
									    	   Log.i(module,"EL RESTO DE INFO: "+ barrio + ciudad + departamento + pais);
									    	   ciudadPais=ciudad+","+pais;
									       }catch(Exception e){
									    	   e.printStackTrace();
									    	   barrio="No existe";
										       ciudad="No existe";
										       pais="No existe";
										       ciudadPais="No existe";
									       }
									       
									   } catch (JSONException e1) {
									       e1.printStackTrace();

									   }
									   handler.post(new Runnable() {
							                public void run() {							                								                								                
							                	appState.setFlagrecordardireccion(0);
							                	
							                	if (appState.getDireccionGoogle().indexOf("#")>0 ){
							                		String [] separanumeral = appState.getDireccionGoogle().split("#");
								                	
								                	if (separanumeral[1].indexOf("a")>0 ){								                	
								                		avisodireccion.setText("POR FAVOR REUBIQUESE");
								                	}else{
								                		avisodireccion.setText(appState.getDireccionGoogle());
								                	}
							                	}else{
							                		avisodireccion.setText(appState.getDireccionGoogle());
							                	}
							                	
							                	
							                	
							                	
							                	avisousuario.setText(appState.getNombreUsuario());
//							                	avisodireccion.setText(appState.getDireccionGoogle());
							                	//barriousuario.setText(appState.getBarrio());
							                }
							            });

								   }

							};
							
							thrd.start();	
						}catch (Exception e){
							Log.e(APPNAME, module + ": Erro en el Marker..." + e); 		
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
		
		
	}
	
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
	
	public JSONObject getLocationInfo( double lat, double lng) {
		String urlJson =("http://maps.google.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=false");
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

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

	private void AvisoTaxiAsignado(){
		final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(this);
	    alertaSimple.setTitle("        TAXI ASIGNADO !!!!");
	   	alertaSimple.setMessage("En Breve el Taxi de Placas: "  + appState.getPlaca() + " \nNombre Conductor: " + appState.getNombreTaxista());
	    alertaSimple.setPositiveButton("Aceptar",
	   	new DialogInterface.OnClickListener() {
	    	 public void onClick(DialogInterface dialog, int which) {
	    	    	 					    	    	 					    	    	 					    	    	
	    	    	 
	    	 }
	    });  
	    try{
	    	alertaSimple.show();
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	
		
	 /********************************************************************************************/
	 /****************************PARA EL SONIDO DEL ALFRENTE***************************************/       
	private void play_mp() {	    	      
	   contaviso++;
	   if((contaviso==1)||(flagaviso==7)){
		   flagaviso=3;
		   mp= MediaPlayer.create(getApplicationContext(), R.raw.sonido);
		   mp.start();
		   final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(this);
		   alertaSimple.setTitle("        TAXIS LIBRES ");
		   alertaSimple.setMessage("SU TAXI DE PLACAS: " +  appState.getPlaca() + " SE ENCUENTRA ALFRENTE\n"
		   		+ "CLAVE:"+ appState.getNumberPhone().charAt(8)+appState.getNumberPhone().charAt(9));
		   alertaSimple.setPositiveButton("Aceptar",
		   new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog, int which) {
		    	 flagaviso=7;	 					    	    	 					    	    	 					    	    	
		    	}
		    });  
		    alertaSimple.show();	
	   }
	}   
	
	/********************************************************************************************/
	
	public void cambioMapa(View v){			
		flagmapa++;
		if(flagmapa==1){			
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		}else if(flagmapa>1){
			flagmapa=0;		
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		}	
		
	}
			
	public void MensajeTaxista(){
		try{
  
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Mensajes del Taxista");
			alert.setMessage(appState.getMsgTaxista());
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				// here you can add functions

				
				}
			});
			alert.setIcon(R.drawable.barra);
			alert.show();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void Solicita(View v){	
		
		if(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){					
		LayoutInflater factory = LayoutInflater.from(this);  
		//final View textEntryView = factory.inflate(R.layout.dialog_signin, null);
		final View textEntryView = factory.inflate(R.layout.window_dir_barrio, null);
					
		 direccionInput = (EditText) textEntryView.findViewById(R.id.compdireccion);  
		 direccionInput.setTextColor(Color.RED);
		 
		//Preguntar Pais para colocar titulo...
		 final TextView InfoBarr = (TextView) textEntryView.findViewById(R.id.infobarr);
		 if((ciudadPais.contains("MEXICO"))||(ciudadPais.contains("Mexico"))){
			 InfoBarr.setText("COLONIA");
		 }else{
			 InfoBarr.setText("BARRIO");
		 }
		 
		 InfoBarr.setTextColor(Color.WHITE);
		 
		 barrioInput = (EditText) textEntryView.findViewById(R.id.compbarrio);  
		 barrioInput.setTextColor(Color.RED);
		 barrioInput.setText(barrio);
		 
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
			 
			 direccionInput.setText(s);
			 
			 //Toast.makeText(getApplicationContext(), "CARGANDOOOOO   " + s, Toast.LENGTH_LONG).show();
			 
			 isr.close();
			 
		 }catch(IOException e){
			 e.printStackTrace();
		 }
		 
		 
		 /****************************************************************************************/
		 
		 
		 if(appState.getFlagrecordardireccion()==1){
			 
		 }else{
			 direccionInput.setText(appState.getDireccionGoogle());
			 direccionInput.setSelection(appState.getDireccionGoogle().length());
		 }
		 
		 
//		 direccionInput.setText(appState.getDireccionGoogle());
//		 direccionInput.setSelection(appState.getDireccionGoogle().length());
		 
			
		builderDir = new AlertDialog.Builder(this);
	    // Get the layout inflater				
	    LayoutInflater inflater = this.getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    //builder.setView(inflater.inflate(R.layout.dialog_signin, null)).setTitle("POR FAVOR VERIFIQUE Y COMPLETE SU DIRECCIÃ“N!!!")
	    //.setView(textEntryView)
	    
	    builderDir.setView(inflater.inflate(R.layout.window_dir_barrio, null)).setTitle("POR FAVOR VERIFIQUE Y COMPLETE SU DIRECCION!!!")
	    .setView(textEntryView);
		    
	    // Add action buttons
	           //.setNegativeButton(R.string.confirmat, new DialogInterface.OnClickListener() {
	    builderDir.setPositiveButton("Solicitar Taxi", new OnClickListener() {
               @SuppressWarnings("deprecation")
			public void onClick(DialogInterface dialog, int id) {
            	   try{
            		   compldireccion =direccionInput.getText().toString();
            		   Log.d(APPNAME, "+++++++++++++++ " + compldireccion.length() + "******************** " + appState.getDireccionServicio().length());
            		   Log.d(APPNAME, "***************" + compldireccion + "++++++++++++++++ " + appState.getDireccionServicio());

            		   int usuario =compldireccion.length();
            		   int servidor = appState.getDireccionGoogle().length() + 1;
	            	   //if(usuario <= servidor){
            		   if(usuario <= 5){
	            	    	 AlertDialog alertDialog1 = new AlertDialog.Builder(AMapa.this).create();	        
	            	    	 // Setting Dialog Title
	            	    	 alertDialog1.setTitle("Verifique la direccion!!! ");	        
	            	    	 // Setting Dialog Message
	            	    	 alertDialog1.setMessage("Complete su direccion (Calle, Carrera, Apto, etc)");	        
	            	    	 // Setting Icon to Dialog
	            	    	 alertDialog1.setIcon(R.drawable.barra);	        
	            	    	 // Setting OK Button
	            	    	 alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {	        
	            	    		 public void onClick(DialogInterface dialog, int which) {
	                           // Write your code here to execute after dialog
	                           // closed
	            	    			 Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
	            	    		 }
	            	    	 });	        
	                   // Showing Alert Message
	            	    	 alertDialog1.show();
	            	  }else
            		   if((appState.getBarrio().equals("0"))||(appState.getBarrio()==null)){				    					    		
	  						
	            		  	Log.i(APPNAME, module +  "---------------------------- " + appState.getDireccionServicio());
	  						Intent talk = new Intent();
	  						talk.setAction(C.TEXT_TO_SPEECH);
	  						talk.putExtra("CMD", C.HABLAR);
	  						talk.putExtra("TEXTHABLA", "PORFAVOR ESPERE LAUBICACION DELSERVIDOR");
                			sendBroadcast(talk);
	  						Toast.makeText(getApplicationContext(), "POR FAVOR ESPERE LA UBICACION DEL SERVIDOR", Toast.LENGTH_LONG).show();
	  			    		
	  			      }else{
	  			    	  
	  			    	if(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){
			    			appState.setDireccionUsuario(compldireccion);
			    			appState.setFlagfrecuentes(0);
			    			PedirElTaxi();
			    		}
	  			      }
	      			   	
            	   }catch (Exception e){
            		   e.printStackTrace();
            	   }
               }
           });  
	    
	    //builderDir.setNegativeButton("Frecuentes", new OnClickListener() {
	    builderDir.setNegativeButton("Favoritos", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(APPNAME, module + "Entro a Frecuentes");
				LeerFrec();
				dialog.cancel();
			}
		});
	    
	    builderDir.show();
	   
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void PedirElTaxi (){

		if(appState.getFlagfrecuentes()==1){	//Se deben reemplazar todos los valores...
			String[] copyInfo = appState.getDireccionFrecuente().split("\\|");
    		ciudadPais= copyInfo[2];
    		appState.setDireccionServicio(copyInfo[3]);
    		appState.setDireccionUsuario(copyInfo[4]);
    		barrio=copyInfo[6];
    		double Lat1 = Double.parseDouble(copyInfo[8]);
    		Latsave = Lat1;
    		double Lon1 = Double.parseDouble(copyInfo[9]);
    		Lonsave = Lon1;
    		appState.setTramaGPSMap(copyInfo[8]+"|"+copyInfo[9]);
		}
		
		AlertDialog alertDialog2 = new AlertDialog.Builder(AMapa.this).create();	        
   	 // Setting Dialog Title
   	 alertDialog2.setTitle("Confirmacion!!! ");	        
   	 // Setting Dialog Message
   	 //alertDialog2.setMessage("Â¿Confirma Solicitud de Taxi a:  "  + compldireccion + "?");
   	alertDialog2.setMessage("¿Confirma Solicitud de Taxi a:  "  + appState.getDireccionUsuario() + "?");
   	 // Setting Icon to Dialog
   	 alertDialog2.setIcon(R.drawable.barra);	        
   	 // Setting OK Button
   	 alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {	        
   	public void onClick(DialogInterface dialog, int which) {
          // Write your code here to execute after dialog
          // closed
          Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
          
          if(appState.getDireccionServicio().equals("0")){				    		        	
				
       	   Log.i(APPNAME, module +  "++++++++++++++++++++++ " + appState.getDireccionServicio());
				Intent talk = new Intent();
					talk.setAction(C.TEXT_TO_SPEECH);
					talk.putExtra("CMD", C.HABLAR);
					talk.putExtra("TEXTHABLA", "PORFAVOR ESPERE LAUBICACION DELSERVIDOR");
   			sendBroadcast(talk);
				
				Toast.makeText(getApplicationContext(), "POR FAVOR ESPERE LA UBICACION DEL SERVIDOR", Toast.LENGTH_LONG).show();
	        }else{
   			Log.i(APPNAME, module + ": SeSolicita un taxiiii"); 			    						    	
   			appState.setFlagsolicitud(4);
		    	progDailog = new ProgressDialog(AMapa.this);
		    	progDailog.setTitle("SOLICITUD DE TAXI...");
		    	progDailog.setMessage("Buscando el taxi mas cercano... ");
		    	progDailog.setCancelable(false);
		    	progDailog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar Servicio", new DialogInterface.OnClickListener() {
		    	    @Override
		    	    public void onClick(DialogInterface dialog, int which) {
		    	        dialog.dismiss();
		    	        appState.setEstadoUnidad(EstadosUnidad.CANCELADO);		       
				        appState.setFlagcancelado(1);
				        String cancelar_servicio = "D|" + appState.getIdServicio() +"\n";
				        
				        Intent canservice = new Intent();
				        canservice.putExtra("CMD", C.SEND);
				        canservice.putExtra("DATA", cancelar_servicio);
				        canservice.setAction(C.COM);
						getApplicationContext().sendBroadcast(canservice);

						Intent talk = new Intent();
						talk.setAction(C.TEXT_TO_SPEECH);
						talk.putExtra("CMD", C.HABLAR);
						talk.putExtra("TEXTHABLA", "SERVICIO CANCELADO");
						sendBroadcast(talk);

						map.clear();
						trabajo.setVisibility(View.VISIBLE);
						cancelar.setVisibility(View.INVISIBLE);
						calificar.setVisibility(View.INVISIBLE);
						avisomarket.setVisibility(View.VISIBLE);
						imgusuario.setVisibility(View.VISIBLE);
						Toast.makeText(getApplicationContext(), "SERVICIO CANCELADO", Toast.LENGTH_LONG).show();
		    	    }
		    	});
		    	progDailog.show();
		    	
		    	/********************************SOLICITUD DE TAXI*******************/
		    	/*****************PARA GUARDAR LA DIRECCION Y NO SE BORRE*****************/
		    	String Savedireccion =  appState.getDireccionUsuario();
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
		    	/****************************************************************************/
   	
//		    	String solicitud_servicio = "B|" + appState.getNumberPhone() +  "|" + appState.getCodciudad() +"|" + appState.getDireccionServicio() + "|" + appState.getDireccionUsuario()+"|" + appState.getNombreUsuario()
//			    		+ "|" + appState.getBarrio() + "|" + appState.getCodzona() + "|" +  appState.getTramaGPSMap() +"\n";

		    	barrio= barrioInput.getText().toString();
		    	String solicitud_servicio="";
		    	if(appState.getFlagfrecuentes()==0){	//Se toma la que el usuario coloco...
		    	solicitud_servicio = "B|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + appState.getDireccionUsuario()+"|" + appState.getNombreUsuario()
			    		+ "|" + barrio.toUpperCase().trim() + "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
		    	}else{		//Se toma de las frecuentes...
		    		appState.setFlagfrecuentes(0);
		    		solicitud_servicio=appState.getDireccionFrecuente();
		    		builderDir.setCancelable(true);	//Cerrar ventana de Direccion...
		    	}
		    	
		    	//String solicitud_servicio = "B|" + appState.getNumberPhone() +  "|" + "11001" +"|" + appState.getDireccionServicio() + "|" + appState.getDireccionUsuario()+"|" + appState.getNombreUsuario()
			    //		+ "|" + "Castilla" + "|" + "399" + "|" +  appState.getTramaGPSMap() +"\n";
				
		    	Intent service = new Intent();
				service.putExtra("CMD", C.SEND);
				service.putExtra("DATA", solicitud_servicio);
				service.setAction(C.COM);
				getApplicationContext().sendBroadcast(service);

				appState.setFlagrecordardireccion(1);
			    appState.setEstadoUnidad(EstadosUnidad.ESPERA);									    									   
				
				Intent talk = new Intent();
					talk.setAction(C.TEXT_TO_SPEECH);
					talk.putExtra("CMD", C.HABLAR);
					talk.putExtra("TEXTHABLA", "BUSCANDO TAXI MASCERCANO");
   			sendBroadcast(talk);
				
				Toast.makeText(getApplicationContext(), "BUSCANDO TAXI MAS CERCANO... ", Toast.LENGTH_LONG).show();
	        }
          
   	   }
   	});	        
  // Showing Alert Message
  alertDialog2.show();
	}
	
	public void StreetView (View v){		
	
		try{			
			Intent streetViewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse 
			("google.streetview:cbll=" + Latsave + "," +  Lonsave + "&cbp=12,222.95,,0,5.39&mz=7")); 
			startActivity(streetViewIntent); 
			Toast.makeText(getApplicationContext(), "Descargando La Imagen de la Direccion...", Toast.LENGTH_LONG).show();
		}catch (Exception e){
			Toast.makeText(getApplicationContext(), "Imagen no Disponible...", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	//Cuando Presionan Leer Frecuentes
		//public void LeerFrec(View v){
		public void LeerFrec(){
			Intent talk = new Intent();
			talk.setAction(C.TEXT_TO_SPEECH);
			talk.putExtra("CMD", C.HABLAR);
			//talk.putExtra("TEXTHABLA", "ENTRO A LEER FRECUENTES");
			talk.putExtra("TEXTHABLA", "ENTRO A LEER FAVORITOS");
			sendBroadcast(talk);
			final ArrayList<String> copiaFrecuentes = new ArrayList<String>();
			copiaNumFrec = appState.getNumFrecuentes();

			int cont=0;
			//for(int cont=0;cont< C.NUM_FRECUENTES;cont++){
			for(cont=0;cont< copiaNumFrec;cont++){
				switch(cont){
					
					case(0):
						copiaFrecuentes.add(cont, appState.getDirFrec1());
						//copiaFrecuentes.add(cont, TraerSoloDireccion(appState.getDirFrec1()));
					break;
					
					case(1):
						copiaFrecuentes.add(cont, appState.getDirFrec2());
						//copiaFrecuentes.add(cont, TraerSoloDireccion(appState.getDirFrec2()));
					break;
					
					case(2):
						copiaFrecuentes.add(cont, appState.getDirFrec3());
						//copiaFrecuentes.add(cont, TraerSoloDireccion(appState.getDirFrec3()));
					break;
					
					case(3):
						copiaFrecuentes.add(cont, appState.getDirFrec4());
						//copiaFrecuentes.add(cont, TraerSoloDireccion(appState.getDirFrec4()));
					break;
					
					case(4):
						copiaFrecuentes.add(cont, appState.getDirFrec5());
						//copiaFrecuentes.add(cont, TraerSoloDireccion(appState.getDirFrec5()));
					break;
					
					case(5):
						copiaFrecuentes.add(cont, appState.getDirFrec6());
						//copiaFrecuentes.add(cont, TraerSoloDireccion(appState.getDirFrec6()));
					break;
					
					case(6):
						copiaFrecuentes.add(cont, appState.getDirFrec7());
						//copiaFrecuentes.add(cont, TraerSoloDireccion(appState.getDirFrec7()));
					break;
					
					case(7):
						copiaFrecuentes.add(cont, appState.getDirFrec8());
						//copiaFrecuentes.add(cont, TraerSoloDireccion(appState.getDirFrec8()));
					break;
					
					case(8):
						copiaFrecuentes.add(cont, appState.getDirFrec9());
						//copiaFrecuentes.add(cont, TraerSoloDireccion(appState.getDirFrec9()));
					break;
					
					case(9):
						copiaFrecuentes.add(cont, appState.getDirFrec10());
						//copiaFrecuentes.add(cont, TraerSoloDireccion(appState.getDirFrec10()));
					break;
					
				
				}

				copiaFrecuentes.get(cont);
				Log.i(APPNAME, module + "INFO"+ copiaFrecuentes.get(cont)+ "este es index" + cont);
			}
			
			//Aca se muestra la lista
			Dialog dialog = new Dialog(AMapa.this);
			dialog.setContentView(R.layout.list);
			
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					AMapa.this);
	        //builderSingle.setIcon(R.drawable.ic_launcher);
	        //builderSingle.setTitle("Direcciones Frecuentes");
			builderSingle.setTitle("Direcciones Favoritas");
	        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	        		AMapa.this,
	                android.R.layout.select_dialog_singlechoice);
	        cont=copiaFrecuentes.size();
			 int contadorDir=0;
			 appState.setFlagfrecuentes(0);
			 while(contadorDir < cont){
				 arrayAdapter.add(TraerSoloDireccion(copiaFrecuentes.get(contadorDir)));
				 contadorDir++;
			 }
//	        arrayAdapter.add("Hardik");
//	        arrayAdapter.add("Archit");
//	        arrayAdapter.add("Jignesh");
//	        arrayAdapter.add("Umang");
//	        arrayAdapter.add("Gatti");
	        builderSingle.setNegativeButton("Salir",
	                new DialogInterface.OnClickListener() {

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
	                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
	                        		AMapa.this);
	                        builderInner.setMessage(strName);
	                        builderInner.setTitle("Selecciono la Direccion");
	                        builderInner.setPositiveButton("Ok",
	                                new DialogInterface.OnClickListener() {

	                                    @Override
	                                    public void onClick(
	                                            DialogInterface dialog,
	                                            int which) {
	                                    	appState.setDireccionFrecuente(copiaFrecuentes.get(which1));
	                		    	    	appState.setFlagfrecuentes(1);
	                		    	    	
	                		    	    	PedirElTaxi();
	                                        dialog.dismiss();
	                                    }
	                                });
	                        builderInner.show();
	                    }
	                });
	        builderSingle.show();
			    
		}
		
		public String TraerSoloDireccion(String info){
			
			String[] Data = info.split("\\|");
			
			Log.i(APPNAME, module + Data[4]);
			return Data[4];
		}
		
		//Cuando Presionan Guardar Frecuentes
		public void GuardarFrec(View v){ 
			
			copiaNumFrec = appState.getNumFrecuentes();
			Log.i(APPNAME, module + "Numero de Direcciones Almacenadas: "+ copiaNumFrec);
			//Preguntar si ya llego al maximo de frecuentes...
			if(copiaNumFrec < C.NUM_FRECUENTES){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				 
				builder.setTitle("GUARDAR DIRECCION");
				builder.setMessage("¿Confirma que desea guardar esta direccion?");
				builder.setPositiveButton("Aceptar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						compldireccion=direccionInput.getText().toString();
						barrio= barrioInput.getText().toString();
						
						String dirAlmacenar = "B|" + appState.getNumberPhone() +  "|" + ciudadPais.toUpperCase().trim() +"|" + appState.getDireccionServicio() + "|" + compldireccion +"|" + appState.getNombreUsuario()
				    		+ "|" + barrio.toUpperCase().trim() + "|" + "0" + "|" +  appState.getTramaGPSMap() +"\n";
						
						
						
						copiaNumFrec++;	//Se aumenta en 1 el num de frecuentes
						appState.setNumFrecuentes(copiaNumFrec);
						//Se debe almacenar la direccion frecuente n
						switch(copiaNumFrec){
							case(1):
								appState.setDirFrec1(dirAlmacenar);
							break;
							
							case(2):
								appState.setDirFrec2(dirAlmacenar);
							break;
							
							case(3):
								appState.setDirFrec3(dirAlmacenar);
							break;
							
							case(4):
								appState.setDirFrec4(dirAlmacenar);
							break;
							
							case(5):
								appState.setDirFrec5(dirAlmacenar);
							break;
							
							case(6):
								appState.setDirFrec6(dirAlmacenar);
							break;
							
							case(7):
								appState.setDirFrec7(dirAlmacenar);
							break;
							
							case(8):
								appState.setDirFrec8(dirAlmacenar);
							break;
							
							case(9):
								appState.setDirFrec9(dirAlmacenar);
							break;
							
							case(10):
								appState.setDirFrec10(dirAlmacenar);
							break;
						}
						Intent talk = new Intent();
						talk.setAction(C.TEXT_TO_SPEECH);
						talk.putExtra("CMD", C.HABLAR);
						//talk.putExtra("TEXTHABLA", "DIRECCION ALMACENADA ENFRECUENTES");
						talk.putExtra("TEXTHABLA", "DIRECCION ALMACENADA ENFAVORITOS");
						sendBroadcast(talk);
						Log.i(APPNAME, module + "Ahora las Direcciones Almacenadas son: "+ copiaNumFrec);
						
						
					}
				});
	    
				builder.setNegativeButton("Cancelar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i(APPNAME, module + "No guardar Direccion");
						dialog.cancel();
					}
				});
	 
				builder.show();
			}else{
				//Se debe informar que llego al limite de almacenamiento de frecuentes
				Intent talk2 = new Intent();
				talk2.setAction(C.TEXT_TO_SPEECH);
				talk2.putExtra("CMD", C.HABLAR);
				//talk2.putExtra("TEXTHABLA", "NOES POSIBLE ALMACENAR COMO DIRECCION FRECUENTE, ALLEGADO ALLIMITE DE ALMACENAMIENTO");
				talk2.putExtra("TEXTHABLA", "NOES POSIBLE ALMACENAR COMO DIRECCION FAVORITA, ALLEGADO ALLIMITE DE ALMACENAMIENTO");
				sendBroadcast(talk2);
			}
		}

	public void Cancela(View v){	
		
		if(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){
			
			//Cambio para que funcione		
			
		}else if(appState.getEstadoUnidad().equals(EstadosUnidad.SOLICITUD_TAXI)){
		
		}else if(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 
		    builder.setTitle("CANCELACION SERVICIO");
		    builder.setMessage("¿Confirma que desea Cancelar el servicio?");
		    builder.setPositiveButton("Aceptar", new OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        Log.i("Dialogos", "Confirmacion Aceptada.");
		        appState.setEstadoUnidad(EstadosUnidad.CANCELADO);		       
		       
		        appState.setFlagcancelado(1);
		        String cancelar_servicio = "D|" + appState.getIdServicio() +"\n";
		        
		        Intent canservice = new Intent();
		        canservice.putExtra("CMD", C.SEND);
		        canservice.putExtra("DATA", cancelar_servicio);
		        canservice.setAction(C.COM);
				getApplicationContext().sendBroadcast(canservice);
		              				        
				Intent talk = new Intent();
				talk.setAction(C.TEXT_TO_SPEECH);
				talk.putExtra("CMD", C.HABLAR);
				talk.putExtra("TEXTHABLA", "SERVICIO CANCELADO");
				sendBroadcast(talk);

				map.clear();
				trabajo.setVisibility(View.VISIBLE);
				cancelar.setVisibility(View.INVISIBLE);
				calificar.setVisibility(View.INVISIBLE);
				avisomarket.setVisibility(View.VISIBLE);
				imgusuario.setVisibility(View.VISIBLE);
				
				Toast.makeText(getApplicationContext(), "SERVICIO CANCELADO", Toast.LENGTH_LONG).show();
				
		        dialog.cancel();
		     }
		    });
		    builder.setNegativeButton("Cancelar", new OnClickListener() {
		    	public void onClick(DialogInterface dialog, int which) {
		    		Log.i("Dialogos", "Confirmacion Cancelada.");
		    		dialog.cancel();
		    	}
		    });
		    builder.show();
		}
		
	}
			
	private void InitMapa() {
		// TODO Auto-generated method stub		
		UBICACION = new LatLng(4.0897222222222, -72.961944444444); //Centro de Colombia
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
		map.setTrafficEnabled(true);	
	}
	
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
					
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(INICIO, 16));
					Log.e(APPNAME, module + " SE CUADRA EL ZOOM   ");
					if(appState.getInicializar()==0){		
						
						this.root.toggleMenu();
						Log.e(APPNAME, module + " ++++++++++++++++EL SALUDO++++++++++   ");
						
						Intent talk = new Intent();
						talk.setAction(C.TEXT_TO_SPEECH);
						talk.putExtra("CMD", C.HABLAR);
						talk.putExtra("TEXTHABLA", "BIENVENIDO" + appState.getNombreUsuario() + " A TAXIS LIBRES");
						sendBroadcast(talk);			
						
						LayoutInflater inflater = getLayoutInflater();
		 
						View layout = inflater.inflate(R.layout.custom_toast,
						  (ViewGroup) findViewById(R.id.custom_toast_layout_id));
		 
						// set a dummy image
						ImageView image = (ImageView) layout.findViewById(R.id.image);
						image.setImageResource(R.drawable.iconousuario);
		 
						// set a message
						TextView text = (TextView) layout.findViewById(R.id.text);
						text.setText("BIENVENIDO A TAXIS LIBRES: \n"  + "     "+appState.getNombreUsuario());
		 
						// Toast...
						Toast toast = new Toast(getApplicationContext());					
						toast.setDuration(Toast.LENGTH_LONG);
						toast.setView(layout);
						toast.show();
						this.root.toggleMenu();			
						
						
					}
				}								
				/*************************************CUANDO SE ASIGNA TAXI*****************************************/					
				if(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){
					flagTaxiinicio++;	
					TaxiAsignado();	
					
				}
				/*****************************************************************************************************/							
			}catch (Exception e){
				e.printStackTrace();
				Log.e(APPNAME, module + "pailaaaaaaa de gps" + e);
				Toast.makeText(getApplicationContext(), "      BUSCANDO SENAL DE GPS\n"   +  "Por favor ubiquese en un lugar despejado", Toast.LENGTH_LONG).show();
				
			}
		}else{
			Log.e(APPNAME, module + "++++++++++++++++GPS PICHO++++++++++++++");
			Toast.makeText(getApplicationContext(), " POR FAVOR ESPERE LOS SERVICIOS DE UBICACION...", Toast.LENGTH_LONG).show();
			trabajo.setVisibility(View.INVISIBLE);	//NO MOSTRAR BOTON DE SOLICITAR TAXI...
		}
	 }catch(Exception e){
		 e.printStackTrace();
	 }
	
}
	
	public void TaxiAsignado(){
		try{
			
			if(appState.getNombreTaxista().equals(null))appState.setNombreTaxista(appState.getPlaca());
			map.clear();
			movillat = Double.parseDouble(appState.getLatitudMovil());
			movillon = Double.parseDouble(appState.getLongitudMovil());
			double LatitudTaxi = movillat;
			double LongitudTaxi = movillon;
			SERVICIO = new LatLng(LatitudTaxi, LongitudTaxi);
			Marker carro = map.addMarker(new MarkerOptions().position(SERVICIO).title(appState.getNombreUsuario()).snippet("Barrio: "  + appState.getBarrio()  + "\n" + "jaime")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.botontaxiasignado)));
			
			if(flagTaxiinicio<3){											

				map.moveCamera(CameraUpdateFactory.newLatLngZoom(SERVICIO , 14));										
			}
			
			usuario = map.addMarker(new MarkerOptions().position(new LatLng(Latsave, Lonsave)).title(appState.getNombreUsuario()).snippet("Barrio: "  + appState.getBarrio()  + "\n" + "jaime")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconousuario)));
				
			Lat = Double.parseDouble(appState.getLatitudMovil());
			Lon = Double.parseDouble(appState.getLongitudMovil());			
			
			map.setInfoWindowAdapter(new InfoWindowAdapter() {    	
 		        // Use default InfoWindow frame
 		        @Override
 		        public View getInfoWindow(Marker arg0) {
 		            return null;
 		        }    	
 		        // Defines the contents of the InfoWindow
 		        @Override
 		        public View getInfoContents(Marker arg0) {
 		            // Getting view from the layout file info_window_layout
 		            View v = getLayoutInflater().inflate(R.layout.custom_infowindow, null);				 	
 		            // Getting the position from the marker
 		            	           				 	            
 		            TextView nombreWindow = (TextView) v.findViewById(R.id.windownombre);
 		            TextView barrioWindow = (TextView) v.findViewById(R.id.placataxi);
 		            TextView direccionWindow = (TextView) v.findViewById(R.id.windowdireccion);
 		            ImageView fotoTaxista=(ImageView) v.findViewById(R.id.imageView1);
		            Bitmap imagenMostrar;
		            if(appState.getDensidad() > 320){
		            	imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*3), (int)(imagenRecibida.getHeight()*3), true);
		            }else if((321 > appState.getDensidad())&&(appState.getDensidad() > 160)){
		            	imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*2), (int)(imagenRecibida.getHeight()*2), true);
		            }else{
		            	imagenMostrar = Bitmap.createScaledBitmap(imagenRecibida,(int)(imagenRecibida.getWidth()*1), (int)(imagenRecibida.getHeight()*1), true);
		            }
		            fotoTaxista.setImageBitmap(imagenMostrar);
 		            
 		            nombreWindow.setText(appState.getNombreTaxista());
 		            barrioWindow.setText("Placa: " +  appState.getPlaca());
 		            direccionWindow.setText("Distancia: " + appState.getMetros() + " mts");
 		           //direccionWindow.setText(appState.getDireccionGoogle());
 		            return v;    	
 		        }        				
 		    });    
			carro.showInfoWindow();	
				
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
				        
				    if(appState.getMetros()<15){						    	
				    	appState.setFlagAnunciotaxi(0);
				       	appState.setEstadoUnidad(EstadosUnidad.ENCUESTA);						     
				        Toast.makeText(getApplicationContext(), "USUARIO A BORDO DEL TAXI ", Toast.LENGTH_LONG).show(); 						      								
									
						trabajo.setVisibility(View.VISIBLE);	
						cancelar.setVisibility(View.INVISIBLE);								
						calificar.setVisibility(View.INVISIBLE);

						appState.setFlagAnunciotaxi(0);
					    Toast.makeText(getApplicationContext(), "USUARIO A BORDO DEL TAXI ", Toast.LENGTH_LONG).show();
					    
					    appState.setFlagcancelado(1);
					    Intent califi = new Intent();
						califi.setClass(getApplicationContext(), AFormulario.class);		        		    				
				        startActivity(califi);
					    finish(); 
				     }
				}

		}catch (Exception e) {
			e.printStackTrace();
			Log.e(APPNAME, module + ": UPDATE LOCATION STATUS2 "+ e.getMessage());
		}
	}
	
	public void MostrarUsuario(){
		usuario = map.addMarker(new MarkerOptions().position(new LatLng(Latsave, Lonsave)).title(appState.getNombreUsuario()).snippet("Barrio: "  + appState.getBarrio()  + "\n" + "jaime")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconousuario)));
	}
	
	public void PreguntaMovil(){
			
		Log.i(APPNAME, module + ": ******************* ESTADO *******************************: " + appState.getEstadoUnidad());
		switch (appState.getEstadoUnidad()){				
			
		case SOLICITUD_TAXI:
		case TAXI_ASIGNADO:
				
				try{
					
					Log.i(APPNAME, module + ": ++++++++++++: " + appState.getIdServicio() + "--------------------" + appState.getNumberPhone());	
					
					String consultar_movil = "C|" + appState.getNumberPhone() +  "|"+ appState.getIdServicio() +"\n";
					
					Intent cons = new Intent();
					cons.putExtra("CMD", C.SEND);
					cons.putExtra("DATA", consultar_movil);
					cons.setAction(C.COM);
					getApplicationContext().sendBroadcast(cons);

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
					getApplicationContext().sendBroadcast(cancel);
				}catch (Exception e){	
					e.printStackTrace();
					Log.e(APPNAME, module + ": Se totea en consultar cancelado: " + e);
				}
				
				break;
		default:
			
			break;
		}
		
		
	}
		
	public Integer restarCoordenadas(double latSrv, double lonSrv, double latTaxi, double lonTaxi) {
        double lon = lonTaxi - lonSrv;
        double res = Math.sin(latSrv * 0.01745329D) * Math.sin(latTaxi * 0.01745329D) + Math.cos(latSrv * 0.01745329D) * Math.cos(latTaxi * 0.01745329D) * Math.cos(lon * 0.01745329D);

        double res1 = Math.acos(res) * 57.295779510000003D;
        Double metros = res1 * 111.30200000000001D * 1000.0D;
        return metros.intValue();
    }
		
	public void Calificar(View v){
					
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
	    builder.setTitle("  CALIFICAR SERVICIO!!!");
	    builder.setMessage("¿Al Calificar Finaliza el Servicio, esta Seguro ? " );
	    builder.setPositiveButton("SI", new OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
	        Log.i("Dialogos", "Confirmacion Aceptada.");
		
				appState.setFlagAnunciotaxi(0);
			    Toast.makeText(getApplicationContext(), "USUARIO A BORDO DEL TAXI ", Toast.LENGTH_LONG).show(); 			     			     											
			
				Intent talk = new Intent();
				talk.setAction(C.TEXT_TO_SPEECH);
				talk.putExtra("CMD", C.HABLAR);
				talk.putExtra("TEXTHABLA", "GRACIAS PORUTILIZAR TAXIS LIBRES... TAXIS LIBRES SILE RESPONDE");
				sendBroadcast(talk);
				
				trabajo.setVisibility(View.VISIBLE);	
				cancelar.setVisibility(View.INVISIBLE);
				calificar.setVisibility(View.INVISIBLE);
				
				Intent califi = new Intent();
				califi.setClass(getApplicationContext(), AFormulario.class);		        		    								
		        startActivity(califi);
		        finish();
	    	}
	    });
	    builder.setNegativeButton("NO", new OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
	        Log.i("Dialogos", "Confirmacion Cancelada.");
	        dialog.cancel();
	    }
	    });
	 
	    builder.show();
			
		
	}
	
	public void CerrardesdeMapa(){
		appState.setEstadoUnidad(EstadosUnidad.LIBRE);

        Intent intentComunication = new Intent(AMapa.this, ServiceTCP.class);
 		stopService(intentComunication);
 		
 		Intent intentTts = new Intent(AMapa.this, ServiceTTS.class);
 		stopService(intentTts);	
 		
 		Intent intentTimer = new Intent(AMapa.this, ServiceTimer.class);
 		stopService(intentTimer);			
	    finish();
	}
			 	
	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
			switch(keyCode){
				case KeyEvent.KEYCODE_BACK:
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
				    builder.setTitle("CERRAR APLICACION...");
				    builder.setMessage("¿Confirma que desea Salir de TaxisLibres?");
				    builder.setPositiveButton("SI", new OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				        Log.i("Dialogos", "Confirmacion Aceptada.");		
				        CerrardesdeMapa();
				    }
				    });
				    builder.setNegativeButton("NO", new OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				        Log.i("Dialogos", "Confirmacion Cancelada.");
				        dialog.cancel();
				    }
				    });
				 
				    builder.show();
					return true;
				
			}
			return super.onKeyDown(keyCode, event);
		}
	 
	 
	 /********************************BOTONES DEL MENU *******************************************/
	 /********************************************************************************************/
	 /********************************************************************************************/
	 public void MenuLeft(View V){
		 this.root.toggleMenu(); 
	 }
	 public void ActualizarDatos(View v){
		 
		 LayoutInflater factory = LayoutInflater.from(this);  
			final View textEntryView = factory.inflate(R.layout.actualizardatos, null);
						
			 final EditText actnombre = (EditText) textEntryView.findViewById(R.id.actnombre);  
			 actnombre.setTextColor(Color.RED);				 
			 actnombre.setText(appState.getNombreUsuario());
			
			 final EditText acttelefono = (EditText) textEntryView.findViewById(R.id.acttelefono);  
			 acttelefono.setTextColor(Color.RED);				 
			 acttelefono.setText(appState.getNumberPhone());
				
			 
			 
			 
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    // Get the layout inflater				
		    LayoutInflater inflater = this.getLayoutInflater();
		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(R.layout.actualizardatos, null)).setTitle("ACTUALIZACION DE DATOS!!!")
		    .setView(textEntryView)
		    .setNegativeButton("GUARDAR", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					newnombre =actnombre.getText().toString();
					newtelefono =acttelefono.getText().toString();
			
					appState.setNombreUsuario(newnombre);
					appState.setNumberPhone(newtelefono);
					avisousuario.setText(appState.getNombreUsuario());
					
				}
		    	
		    });
			    
		    // Add action buttons
		          
		    
		    builder.show();
	 }
	 
	 public void ListDirecciones(View v){
		 Intent datos = new Intent();
		 datos.setClass(getApplicationContext(), AFormulario.class);	    				
	     startActivity(datos);
		 finish();
		 this.root.toggleMenu();
	 }
	 
	 public void CompartirApp(View v){
		 
		// Reemplazamos el email por algun otro real
	        String[] to = {  };
	        
	        enviar(to, "Aplicacion Pedir Taxi","Esto es un email para compartir esta super Aplicacion para pedir Taxi \n" +
	        "https://itunes.apple.com/us/app/taxis-libres/id686270238?ls=1&mt=8  ---->  IPHONE \n" +
	        		"https://play.google.com/store/apps/details?id=com.cotech.taxislibres&hl=es ----> ANDROID");
	        
	      
		 this.root.toggleMenu();
	 }

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
	 
	 
	 
	 public void RastreoTaxi(View v){
		 this.root.toggleMenu();
	 }

	 
	 
	public void SendMsgTaxista(View v) {
			// TODO Auto-generated method stub	
		
		if(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){				
		
			LayoutInflater factory = LayoutInflater.from(this);  
			 View textEntryView = factory.inflate(R.layout.dialog_msg, null);
								
			 final EditText msgtaxista = (EditText) textEntryView.findViewById(R.id.msgtaxista);  
			
			 
			 
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    // Get the layout inflater				
		    LayoutInflater inflater = this.getLayoutInflater();
		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(R.layout.dialog_signin, null)).setTitle("DIGITE EL MENSAJE QUE DESEA ENVIAR AL TAXISTA!!!")
		    .setView(textEntryView)			   
		    // Add action buttons
		           .setNegativeButton(R.string.Mensaje, new DialogInterface.OnClickListener() {
	         
				public void onClick(DialogInterface dialog, int id) {
	            	   	
					try{						
						
	            	   String sendMsg = msgtaxista.getText().toString();
	            	   Toast.makeText(getApplicationContext(), "SE ENVIA EL MENSAJE... "  , Toast.LENGTH_LONG).show();
	            	   String Mensaje_Taxista = "F|" + "1" +  "|" + sendMsg +"|" + appState.getNumberPhone() + "|" + appState.getNombreUsuario() 
	            			 +"|"+ appState.getPinMovil() + "|"+appState.getPlaca() +  "|" +appState.getIdServicio() +  "|"+ appState.getDireccionServicio() + "\n";
					   
					   Intent msg = new Intent();
					   msg.putExtra("CMD", C.SEND);
					   msg.putExtra("DATA", Mensaje_Taxista);
					   msg.setAction(C.COM);
				       getApplicationContext().sendBroadcast(msg);
				       QuitaMenu();
				       
					}catch(Exception e){
						e.printStackTrace();
					}

	               }

	               
		   });
		   builder.show();
		}else{
			
			Toast.makeText(getApplicationContext(), "EN ESTE MOMENTO USTED NO TIENE TAXI ASIGNADO... "  , Toast.LENGTH_LONG).show();
			
			Intent talk = new Intent();
			talk.setAction(C.TEXT_TO_SPEECH);
			talk.putExtra("CMD", C.HABLAR);
			talk.putExtra("TEXTHABLA", "ENESTE MOMENTO USTED NOTIENE TAXI ASIGNADO  ");
			sendBroadcast(talk);
			
			this.root.toggleMenu();
		}
	 }
	
	public void QuitaMenu(){
		Toast.makeText(getApplicationContext(), "MENSAJE ENVIADO CORRECTAMENTE", Toast.LENGTH_LONG).show();
		this.root.toggleMenu();
	}
	public void SendMsgCentral(View v ) {
		// TODO Auto-generated method stub					
	 if(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){		
		LayoutInflater factory = LayoutInflater.from(this);  
		final View textEntryView = factory.inflate(R.layout.dialog_msg, null);
			
		
		final EditText msgcentral = (EditText) textEntryView.findViewById(R.id.msgtaxista);  		 
			
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater				
	    LayoutInflater inflater = this.getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.dialog_signin, null)).setTitle("!!!DIGITE EL MENSAJE QUE DESEA ENVIAR AL CONTACT CENTER!!!")
	    .setView(textEntryView)			   
	    // Add action buttons
	           .setNegativeButton(R.string.Mensaje, new DialogInterface.OnClickListener() {
               @SuppressWarnings("deprecation")
			public void onClick(DialogInterface dialog, int id) {
            	   
            	   String sendMsg = msgcentral.getText().toString();
            	   Toast.makeText(getApplicationContext(), "SE ENVIA EL MENSAJE... ", Toast.LENGTH_LONG).show();
            	   String Mensaje_Taxista = "F|" + "0" +  "|" + sendMsg +"|" + appState.getNumberPhone() + "|" + appState.getNombreUsuario() 
	            			 +"|"+ appState.getPinMovil() + "|"+appState.getPlaca() +  "|" +appState.getIdServicio() +  "|"+ appState.getDireccionServicio() + "\n";
				   
				    Intent msg = new Intent();
					msg.putExtra("CMD", C.SEND);
					msg.putExtra("DATA", Mensaje_Taxista);
					msg.setAction(C.COM);
				    getApplicationContext().sendBroadcast(msg);
				    
				    QuitaMenu();
	
				    
               }
               
	   });
	   builder.show();
	 }else{
		 Toast.makeText(getApplicationContext(), "EN ESTE MOMENTO USTED NO TIENE TAXI ASIGNADO... "  , Toast.LENGTH_LONG).show();
		 
		Intent talk = new Intent();
		talk.setAction(C.TEXT_TO_SPEECH);
		talk.putExtra("CMD", C.HABLAR);
		talk.putExtra("TEXTHABLA", "ENESTE MOMENTO USTED NOTIENE TAXI ASIGNADO  ");
		sendBroadcast(talk);
		
		 this.root.toggleMenu();
	 }
	 
 }

	public void HistoricoServicios(View v){
		Log.i(APPNAME, module +  "ENTRO A VER HISTORICO");
		 Toast.makeText(getApplicationContext(), "PASANDO AL HISTORICO"  , Toast.LENGTH_LONG).show();
		 
//		 Intent verserv = new Intent();
//		 verserv.putExtra("CMD", C.ENVIARAHISTORICO);
//		 verserv.setAction(C.MAP);
//		 sendBroadcast(verserv);
		 Intent talk = new Intent();
		 talk.setAction(C.TEXT_TO_SPEECH);
		 talk.putExtra("CMD", C.HABLAR);
		 talk.putExtra("TEXTHABLA", "ENTROAVER HISTORICO");
		 sendBroadcast(talk); 
		 
       
		 
		 LayoutInflater factory = LayoutInflater.from(this);
		 final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 
		 final View Msgs = factory.inflate(R.layout.lista_servicios, null);
		 final LinearLayout listaMensajes = (LinearLayout)Msgs.findViewById(R.id.listaServicios);
		 final TextView Titulo = (TextView) Msgs.findViewById(R.id.textnombre);
		 Titulo.setText("SERVICIOS SOLICITADOS");
		 //Titulo.setTextColor(Color.BLUE);
		 //Titulo.setTextAlignment(4);
		 Titulo.setTextSize(22);

		 
		 builder.setView(Msgs);
		 
		 Handler_sqlite helper = new Handler_sqlite(this);
		    Log.i(APPNAME, module + ": Crea la base");
		    String vector[]=helper.leer();
		    int campos=0;
			int size=0;
			int contadorMsgs=0;
			
			while(campos < vector.length){
		    	if(vector[campos]!=null){
		    		campos++;
		    	}
		    	else{
		    		size=campos-1;
		    		campos=1000;
		    	}
		    }
		    Log.i(APPNAME, module + "La base tiene: "+ vector.length + "Registros");

		    while(size > -1){
		    	
		    	TextView text = new TextView(this);
		    	if(vector[size]!=null){
		    		text.setText((contadorMsgs+1)+". "+ vector[size]);
		    	}else{
		    		text.setText(vector[size]);
		    	}
		    		text.setTextSize(18);
		    		text.setHighlightColor(Color.BLACK);
			    	listaMensajes.addView(text);
		    	Log.i(APPNAME, module + ": Registro #"+contadorMsgs+"."+ vector[size]);
		    	size--;
		    	contadorMsgs++;
		    }
		    helper.cerrar();
		    
		    builder.show();
		this.root.toggleMenu();
	}
	
	public void BorrarHistorico(View v){
		Intent talk = new Intent();
		 talk.setAction(C.TEXT_TO_SPEECH);
		 talk.putExtra("CMD", C.HABLAR);
		 talk.putExtra("TEXTHABLA", "ENTRO A BORRAR HISTORICO");
		 sendBroadcast(talk);
		 
		 final Handler_sqlite helper = new Handler_sqlite(this);
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 
			builder.setTitle(" BORRAR HISTORICO ");
			builder.setMessage("¿Confirma que desea borrar el Historico?");
			builder.setPositiveButton("Aceptar", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
       	
					Log.i(APPNAME, module + ": Borra el Historico de los Servicios"); 			    						    	
					String vector[]=helper.leer();
					helper.deleteAllMessages();
		        	Log.i(APPNAME, module + "Borro la base!!!");
		        	helper.cerrar();
		        	
		        	Intent talk = new Intent();
					talk.setAction(C.TEXT_TO_SPEECH);
					talk.putExtra("CMD", C.HABLAR);
					talk.putExtra("TEXTHABLA", "HISTORICO BORRADO CORRECTAMENTE");
					sendBroadcast(talk);
					
				}
			});
   
			builder.setNegativeButton("Cancelar", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Log.i(APPNAME, module + "No borra Historico.");
					dialog.cancel();
				}
			});

			builder.show();	
		
		this.root.toggleMenu();
	}
	 //************************************************************************
	 
	 public void GuardarInfoDataBase(String asunto, String label){
			String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
		    String asunto2 = mydate + asunto;
			Handler_sqlite helper = new Handler_sqlite(this);
			helper.abrir();
			helper.insertarReg(asunto2, label);
	        helper.cerrar();
	 }
	/************************************************************************************************/    
	 	 
	 
	    @Override
		public void onDestroy() {
			super.onDestroy();
			Log.i(APPNAME, module + "Se destruye");
			finish();		
//		    System.gc();
//			registerReceiver(receiver, filter);
		}
		
		
		
		@Override
		public void onStart() {
			super.onStart();
			Log.i(APPNAME, module + "Inicio On Create");
			registerReceiver(receiver, filter);
		}
		
		@Override
		public void onResume() {
			super.onResume();
			Log.i(APPNAME, module + "resumeeeen");
			registerReceiver(receiver, filter);
			mobileAppTracker.trackSession();
		}

		@Override
		protected void onPause() {
			super.onPause();
			System.gc();
			Log.i(APPNAME, module + "Pausaaaaaaa");
			unregisterReceiver(receiver);
			
		}


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), "Entra al GPSSSSSSSSS", Toast.LENGTH_LONG).show();
	}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}


}
