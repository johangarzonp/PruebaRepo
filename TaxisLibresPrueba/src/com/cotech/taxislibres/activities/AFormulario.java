package com.cotech.taxislibres.activities;


import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;







//import android.util.Log;
import com.cotech.taxislibres.activities.Log;

import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.EstadosUnidad;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.TaxisLi.TrackerName;
import com.cotech.taxislibres.services.ServiceTCP;
import com.cotech.taxislibres.services.ServiceTTS;
import com.cotech.taxislibres.services.ServiceTimer;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;



public class AFormulario extends Activity {
	
	private static final String TAG = "RLPlus";
	protected String module = "Formulario";	
	private TaxisLi appState;
	private TextView placa;
	private TextView nombre;
	private EditText comentarios;
	//Intent i = new Intent();
	private Context context;	
	
		
	private ImageButton enviar;

	private TextToSpeech tts;
	
	private boolean presionoBuena=false;
	private boolean presionoMala=false;
	
	private boolean pasadelay=false;
	
	private String comentario1;
	private String comentario2;
	private String comentario3;
	
	private int Densidad;
	
	private ImageView star1;
	private ImageView star2;
	private ImageView star3;
	private ImageView star4;
	private ImageView star5;
	
	private ImageView star6;
	private ImageView star7;
	private ImageView star8;
	private ImageView star9;
	private ImageView star10;
	
	private ImageView star11;
	private ImageView star12;
	private ImageView star13;
	private ImageView star14;
	private ImageView star15;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		appState = ((TaxisLi) context);	
		
		Densidad = getResources().getDisplayMetrics().densityDpi;
		
		if(Densidad<=240){
			setContentView(R.layout.formulario);
		}else{
			setContentView(R.layout.formulario_320);
		}	
		
		//************************************************************************
		
		star1 = (ImageView) findViewById(R.id.imagestar1costo);
		star2 = (ImageView) findViewById(R.id.imagestar2costo);
		star3 = (ImageView) findViewById(R.id.imagestar3costo);
		star4 = (ImageView) findViewById(R.id.imagestar4costo);
		star5 = (ImageView) findViewById(R.id.imagestar5costo);
		
		star6 = (ImageView) findViewById(R.id.imagestar1conducta);
		star7 = (ImageView) findViewById(R.id.imagestar2conducta);
		star8 = (ImageView) findViewById(R.id.imagestar3conducta);
		star9 = (ImageView) findViewById(R.id.imagestar4conducta);
		star10 = (ImageView) findViewById(R.id.imagestar5conducta);
		
		star11 = (ImageView) findViewById(R.id.imagestar1condiciones);
		star12 = (ImageView) findViewById(R.id.imagestar2condiciones);
		star13 = (ImageView) findViewById(R.id.imagestar3condiciones);
		star14 = (ImageView) findViewById(R.id.imagestar4condiciones);
		star15 = (ImageView) findViewById(R.id.imagestar5condiciones);

		comentarios=(EditText)findViewById(R.id.comentarios);
		comentarios.setMaxWidth(comentarios.getWidth());
		
		enviar= (ImageButton) findViewById(R.id.enviarencuesta);

		//************************************************************************
        appState.setEstadoUnidad(EstadosUnidad.ENCUESTA);

		
		Intent intentTcp = new Intent(AFormulario.this, ServiceTCP.class);
     	startService(intentTcp);	
     	
     	Intent intentTts = new Intent(AFormulario.this, ServiceTTS.class);
        startService(intentTts);	
        
        Intent intentTimer = new Intent(AFormulario.this, ServiceTimer.class);
        startService(intentTimer);
        
        comentario1="";
        comentario2="";
        comentario3="";
        
        presionoBuena=true;	//Por ahora para enviar comentarios...
//        comentario1="Costo del Servicio~";
		
        star1.setImageResource(R.drawable.estrellaseleccionada);
		star2.setImageResource(R.drawable.estrellaseleccionada);
		star3.setImageResource(R.drawable.estrellaseleccionada);
		star4.setImageResource(R.drawable.estrellaseleccionada);
		star5.setImageResource(R.drawable.estrellasinseleccionar);
		comentario1="Costo del Servicio=4~";
	}
	
	//************************************************************************
	public void Estrellascosto(View view){
		Log.i(TAG, module +": ENTRO POR : Estrellascosto");
				
		switch(view.getId()) {
		case R.id.imagestar1costo:
				star1.setImageResource(R.drawable.estrellaseleccionada);
				star2.setImageResource(R.drawable.estrellasinseleccionar);
				star3.setImageResource(R.drawable.estrellasinseleccionar);
				star4.setImageResource(R.drawable.estrellasinseleccionar);
				star5.setImageResource(R.drawable.estrellasinseleccionar);
				comentario1="Costo del Servicio=1~";
			break;
			
		case R.id.imagestar2costo:
			star1.setImageResource(R.drawable.estrellaseleccionada);
			star2.setImageResource(R.drawable.estrellaseleccionada);
			star3.setImageResource(R.drawable.estrellasinseleccionar);
			star4.setImageResource(R.drawable.estrellasinseleccionar);
			star5.setImageResource(R.drawable.estrellasinseleccionar);
			comentario1="Costo del Servicio=2~";
		break;
		
		case R.id.imagestar3costo:
			star1.setImageResource(R.drawable.estrellaseleccionada);
			star2.setImageResource(R.drawable.estrellaseleccionada);
			star3.setImageResource(R.drawable.estrellaseleccionada);
			star4.setImageResource(R.drawable.estrellasinseleccionar);
			star5.setImageResource(R.drawable.estrellasinseleccionar);
			comentario1="Costo del Servicio=3~";
		break;
		
		case R.id.imagestar4costo:
			star1.setImageResource(R.drawable.estrellaseleccionada);
			star2.setImageResource(R.drawable.estrellaseleccionada);
			star3.setImageResource(R.drawable.estrellaseleccionada);
			star4.setImageResource(R.drawable.estrellaseleccionada);
			star5.setImageResource(R.drawable.estrellasinseleccionar);
			comentario1="Costo del Servicio=4~";
		break;
		
		case R.id.imagestar5costo:
			star1.setImageResource(R.drawable.estrellaseleccionada);
			star2.setImageResource(R.drawable.estrellaseleccionada);
			star3.setImageResource(R.drawable.estrellaseleccionada);
			star4.setImageResource(R.drawable.estrellaseleccionada);
			star5.setImageResource(R.drawable.estrellaseleccionada);
			comentario1="Costo del Servicio=5~";
		break;
		}
	
	}
	
	//************************************************************************
	public void Estrellasconducta(View view){
		
		switch(view.getId()) {
		case R.id.imagestar1conducta:
			star6.setImageResource(R.drawable.estrellaseleccionada);
			star7.setImageResource(R.drawable.estrellasinseleccionar);
			star8.setImageResource(R.drawable.estrellasinseleccionar);
			star9.setImageResource(R.drawable.estrellasinseleccionar);
			star10.setImageResource(R.drawable.estrellasinseleccionar);
			comentario2="Conducta del Taxista=1~";
			break;
		
		case R.id.imagestar2conducta:
			star6.setImageResource(R.drawable.estrellaseleccionada);
			star7.setImageResource(R.drawable.estrellaseleccionada);
			star8.setImageResource(R.drawable.estrellasinseleccionar);
			star9.setImageResource(R.drawable.estrellasinseleccionar);
			star10.setImageResource(R.drawable.estrellasinseleccionar);
			comentario2="Conducta del Taxista=2~";
			break;
		
		case R.id.imagestar3conducta:
			star6.setImageResource(R.drawable.estrellaseleccionada);
			star7.setImageResource(R.drawable.estrellaseleccionada);
			star8.setImageResource(R.drawable.estrellaseleccionada);
			star9.setImageResource(R.drawable.estrellasinseleccionar);
			star10.setImageResource(R.drawable.estrellasinseleccionar);
			comentario2="Conducta del Taxista=3~";
			break;
			
		case R.id.imagestar4conducta:
			star6.setImageResource(R.drawable.estrellaseleccionada);
			star7.setImageResource(R.drawable.estrellaseleccionada);
			star8.setImageResource(R.drawable.estrellaseleccionada);
			star9.setImageResource(R.drawable.estrellaseleccionada);
			star10.setImageResource(R.drawable.estrellasinseleccionar);
			comentario2="Conducta del Taxista=4~";
			break;
			
		case R.id.imagestar5conducta:
			star6.setImageResource(R.drawable.estrellaseleccionada);
			star7.setImageResource(R.drawable.estrellaseleccionada);
			star8.setImageResource(R.drawable.estrellaseleccionada);
			star9.setImageResource(R.drawable.estrellaseleccionada);
			star10.setImageResource(R.drawable.estrellaseleccionada);
			comentario2="Conducta del Taxista=5~";
			break;
		}
	}
	
	//************************************************************************
	public void Estrellascondiciones(View view){
		
		switch(view.getId()) {
		case R.id.imagestar1condiciones:
			star11.setImageResource(R.drawable.estrellaseleccionada);
			star12.setImageResource(R.drawable.estrellasinseleccionar);
			star13.setImageResource(R.drawable.estrellasinseleccionar);
			star14.setImageResource(R.drawable.estrellasinseleccionar);
			star15.setImageResource(R.drawable.estrellasinseleccionar);
			comentario3="Condiciones del Vehiculo=1~";
			break;
			
		case R.id.imagestar2condiciones:
			star11.setImageResource(R.drawable.estrellaseleccionada);
			star12.setImageResource(R.drawable.estrellaseleccionada);
			star13.setImageResource(R.drawable.estrellasinseleccionar);
			star14.setImageResource(R.drawable.estrellasinseleccionar);
			star15.setImageResource(R.drawable.estrellasinseleccionar);
			comentario3="Condiciones del Vehiculo=2~";
			break;
			
		case R.id.imagestar3condiciones:
			star11.setImageResource(R.drawable.estrellaseleccionada);
			star12.setImageResource(R.drawable.estrellaseleccionada);
			star13.setImageResource(R.drawable.estrellaseleccionada);
			star14.setImageResource(R.drawable.estrellasinseleccionar);
			star15.setImageResource(R.drawable.estrellasinseleccionar);
			comentario3="Condiciones del Vehiculo=3~";
			break;
			
		case R.id.imagestar4condiciones:
			star11.setImageResource(R.drawable.estrellaseleccionada);
			star12.setImageResource(R.drawable.estrellaseleccionada);
			star13.setImageResource(R.drawable.estrellaseleccionada);
			star14.setImageResource(R.drawable.estrellaseleccionada);
			star15.setImageResource(R.drawable.estrellasinseleccionar);
			comentario3="Condiciones del Vehiculo=4~";
			break;
			
		case R.id.imagestar5condiciones:
			star11.setImageResource(R.drawable.estrellaseleccionada);
			star12.setImageResource(R.drawable.estrellaseleccionada);
			star13.setImageResource(R.drawable.estrellaseleccionada);
			star14.setImageResource(R.drawable.estrellaseleccionada);
			star15.setImageResource(R.drawable.estrellaseleccionada);
			comentario3="Condiciones del Vehiculo=5~";
			break;
		}
		
	}
	
	
		
	//*******************Escogio alguna de las opciones para mal servicio***********************
	public void EscogioOpciones(View view){

		//boolean checked = ((RadioButton) view).isChecked();
		
	    switch(view.getId()) {
	    
	    	
//	    	case R.id.imagecostoservicio:
//	    		
//	    		if(comentario1==""){
//	    			
//	    			comentario1="Costo del Servicio~";
//	    			Log.i(TAG, module + comentario1);
//	    		}else{
//	    			
//	    			comentario1="";
//	    			Log.i(TAG, module + "Sin Comentario");
//	    		}
//	    		
//
//	    	break;
//	        
//	        
//	        case R.id.imageconductataxista:
//	        	
//	        	if(comentario2==""){
//	        		
//	    			comentario2="Conducta del Taxista~";
//	    			Log.i(TAG, module + comentario2);
//	    		}else{
//	    			
//	    			comentario2="";
//	    			Log.i(TAG, module + "Sin Comentario");
//	    		}
//	            
//	        break;
//	        
//	        
//	        case R.id.imagecondicionesvehiculo:
//	        	
//	        	if(comentario3==""){
//	        		
//	    			comentario3="Condiciones del Vehiculo~";
//	    			Log.i(TAG, module + comentario3);
//	    		}else{
//	    			
//	    			comentario3="";
//	    			Log.i(TAG, module + "Sin Comentario");
//	    		}
//	        	
//	        break;
	        
	    }
		
	
	}
	
	//************************************************************************

	public void Enviar(View v){
		
		if((presionoMala)||(presionoBuena)){	//Presiono buena o mala experiencia
//			if((presionoMala)&&(comentario1=="")&&(comentario2=="")&&(comentario3=="")){	//No se selecciono ninguna opcion de mala experiencia
			if((presionoBuena)&&(comentario1=="")&&(comentario2=="")&&(comentario3=="")){	//No se selecciono ninguna opcion de mala experiencia

				final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(this);
			   	 alertaSimple.setTitle("    AYUDENOS A MEJORAR ");
			   	 //alertaSimple.setMessage("Porfavor seleccione una opcion para indicar su mala experiencia");
			   	alertaSimple.setMessage("Porfavor realice la calificación para indicar su experiencia");
			   	 alertaSimple.setPositiveButton("Aceptar",
			   	new DialogInterface.OnClickListener() {
			    	     public void onClick(DialogInterface dialog, int which) {
			    	     }
			    	    });
			    	  
			    	  alertaSimple.show();
			
				
			}else{
				
				try{
					Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
					t.setScreenName("Usuario envia Calificacion");
					t.send(new HitBuilders.AppViewBuilder().build());
				}catch(Exception e){
					e.printStackTrace();
				}

			
				Toast.makeText(AFormulario.this, "ENVIANDO ENCUESTA...", Toast.LENGTH_SHORT).show();
					Log.i(TAG, module +": DEBERIA ENVIAR : ");
					String comentariosusuario = comentarios.getText().toString();
					//appState.setFormucomentario(comentariosusuario);
					
					//String Total = "E|" + appState.getNumberPhone() + "|" + "1" + "|" + "1" + "|" + "5" +
					//		"|" + appState.getFormucomentario() +  "|" + appState.getIdServicio() + "|" + appState.getPlaca()+"\n";
					
					String experiencia="";
					if(presionoBuena)	experiencia="3";	//Buena experiencia=3
					else	experiencia="2";				//Mala experiencia=2
					String Total = "E|" + appState.getNumberPhone() + "|" + experiencia + "|" + "-1" + "|" + "-1" +
							"|" +comentario1+comentario2+comentario3+comentariosusuario +  "|" + 
							appState.getIdServicio() + "|" + appState.getPinMovil()+ "|" + appState.getCorreoUsuario()+
							"|" + appState.getNombreUsuario()+"\n";
					
					//appState.setFormutotal(Total);
					
					try{							
			        
			        	Log.e(TAG, module + ": Se Envia: " + Total);
			        	
			        	Intent form = new Intent();
			        	form.putExtra("CMD", C.SEND);
			        	form.putExtra("DATA", Total);
			        	form.setAction(C.COM);
						getApplicationContext().sendBroadcast(form);
			        	
								
						Intent talk = new Intent();
						talk.setAction(C.TEXT_TO_SPEECH);
						talk.putExtra("CMD", C.HABLAR);
						talk.putExtra("TEXTHABLA", "GRACIAS PORPREFERIRNOS      "  + appState.getNombreUsuario());
						sendBroadcast(talk);
						
		       		}catch(Exception e){
		       			Log.i(TAG, module + ": ERROR AL ENVIAR LA ENCUESTA" +  e);
		       			e.printStackTrace();
		       		}				
					
					//Entro al Delay
					Log.i(TAG, module + "Entro al Delay");
			        	new Handler().postDelayed(new Runnable(){
			        		public void run(){
			        			//Pasan 3 segundos
			        			Log.i(TAG, module + "Sale del Delay");
			        			pasadelay = true;
			        				appState.setEstadoUnidad(EstadosUnidad.LIBRE);	//Es efectivo
			        				Log.i(TAG, module + "PASA X CERRAR DESDE FORMULARIO");
			        				CerrardesdeForm();
			        		};
			        	}, 3000);
	        		
		}	
			        
			        
				}else{
					final AlertDialog.Builder alertaSimple = new AlertDialog.Builder(this);
				   	 alertaSimple.setTitle("    AYUDENOS A MEJORAR ");
				   	 alertaSimple.setMessage("Porfavor indique su experiencia con este servicio");
				   	 alertaSimple.setPositiveButton("Aceptar",
				   	new DialogInterface.OnClickListener() {
				    	     public void onClick(DialogInterface dialog, int which) {
				    	     }
				    	    });
				    	  
				    	  alertaSimple.show();
				}
				
	}
	
	//************************************************************************
	public void CerrardesdeForm(){
		
		try{
			Log.i(TAG, module + "--------------------------CIERRAR TODO");
			appState.setEstadoUnidad(EstadosUnidad.LIBRE);
	
	        Intent intentComunication = new Intent(AFormulario.this, ServiceTCP.class);
	 		stopService(intentComunication);
	 		
	 		Intent intentTts = new Intent(AFormulario.this, ServiceTTS.class);
	 		stopService(intentTts);	
	 		
	 		Intent intentTimer = new Intent(AFormulario.this, ServiceTimer.class);
	 		stopService(intentTimer);	
			
			
	 		finish();	
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}
	
	//************************************************************************	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
 
		switch(keyCode){
			case KeyEvent.KEYCODE_BACK:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				 
			    builder.setTitle("POR FAVOR CALIFIQUE EL SERVICIO");
			    builder.setMessage("¿Confirma que desea Salir de TaxisLibres?");
			    builder.setPositiveButton("Salir?", new OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			        Log.i("Dialogos", "Confirmacion Aceptada.");
			        appState.setEstadoUnidad(EstadosUnidad.LIBRE);
			        
			        try{
						Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
						t.setScreenName("Usuario No Califica, sale desde Formulario");
						t.send(new HitBuilders.AppViewBuilder().build());
					}catch(Exception e){
						e.printStackTrace();
					}

			        CerrardesdeForm();
			    }
			    });
			    builder.setNegativeButton("Cancelar", new OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			        Log.i("Dialogos", "Confirmacion Cancelada.");
			       
			    }
			    });
			 
			    builder.show();
				return true;
			
		}
		return super.onKeyDown(keyCode, event);
	}
	//************************************************************************
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(TAG, module + "ENTRO A onStart");
		super.onStart();
		GoogleAnalytics.getInstance(AFormulario.this).reportActivityStart(this);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(TAG, module + "ENTRO A onStop");
		super.onStop();
		GoogleAnalytics.getInstance(AFormulario.this).reportActivityStop(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//appState.setEstadoUnidad(EstadosUnidad.LIBRE);
		Log.i(TAG, module + "Se cerro la Aplicacion");
		
		finish();
	}


	
	 
	
}
