package com.cotech.taxislibres.activities;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.services.ServiceTCP;
import com.cotech.taxislibres.services.ServiceTTS;
import com.cotech.taxislibres.services.ServiceTimer;
import com.cotech.taxislibres.TaxisLi.TrackerName;
import com.cotech.taxislibres.activities.Log;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Base64;
//import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ASplash extends Activity {
 
    
    private final int DURACION_SPLASH = 4000; // 3 segundos
    private static final String TAG = "TaxisLibres";
	private static final String module = "SPLASH";	
	private boolean initialized = false;
	private TaxisLi appState;
	
	
	/*******PARA CA CONEXION*********/
	
	String cadenaObtenida;
	final Handler mHandler= new Handler();
	private Socket socket=null;
	private String Reinicio;
	protected PrintWriter dataOutputStream;
	protected InputStreamReader dataInputStream;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
                        
        Context context = getApplicationContext();
	    appState = ((TaxisLi) context);
        
        setContentView(R.layout.splash);
                        
      
        if (!verificaConexion(this)) {	//Se verifica la Conexion de Datos del Usuario...
    	   
        	Intent i = new Intent(ASplash.this, ASinInternet.class);
			startActivityForResult(i, 1);
    	}else{	               
	        new Handler().postDelayed(new Runnable(){
	            public void run(){
			
	            //Dispara los Servicios de Comunicacion, Voz y Tiempo
	            	
	            Intent intentTcp = new Intent(ASplash.this, ServiceTCP.class);
	         	startService(intentTcp);	
	         	
	         	Intent intentTts = new Intent(ASplash.this, ServiceTTS.class);
		        startService(intentTts);	
		        
		        Intent intentTimer = new Intent(ASplash.this, ServiceTimer.class);
		        startService(intentTimer);	
	            	
	            //Se pasa a verificar Datos del Usuario
		        
		        try{
					Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
					t.setScreenName("Paso x el Splash");
					t.send(new HitBuilders.AppViewBuilder().build());
				}catch(Exception e){
					e.printStackTrace();
				}
		        
	            Intent intent = new Intent(ASplash.this, AEntrada.class);
	        	startActivity(intent);
	        	finish();
	            };
	        }, DURACION_SPLASH);
    		
//    		 /*********************PARA SACAR EL IMEI********************************************/
//    		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//    		String imei = tm.getDeviceId(); // getDeviceId() Obtiene el imei	
//    	    appState.setImei(imei); 
//    	    Log.i(TAG, module+": ESTE ES EL iMEI DEL CELULAR : " + appState.getImei());	
//            Reinicio = "identificador|" + appState.getImei(); 
//            ConectarTcp();
    	}
        
      
       
	    
        
    }
 
    public static boolean verificaConexion(Context ctx) {
        
    	Log.i("taxislibres","********************* " + "Comprueba Conexion a Internet");
    	boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        
        NetworkInfo[] redes = connec.getAllNetworkInfo();
       
        for (int i = 0; i < 2; i++) {
            
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        
        Log.i("taxislibres","Conexion es: " + bConectado);
        return bConectado;
    }
    
    public void onResume() {
		final Context context = getApplicationContext();
		super.onResume();
		appState.setActividad(module);
		com.facebook.AppEventsLogger.activateApp(context, "245686422277683");
		Log.i("SPLASH","Paso x On Resume del Splash");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try{
			if(data.getExtras().containsKey("cerrarApp")){
				finish();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    

}
