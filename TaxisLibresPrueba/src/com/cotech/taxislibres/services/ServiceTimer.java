package com.cotech.taxislibres.services;

import java.util.Timer;
import java.util.TimerTask;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.EstadosUnidad;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.activities.AMapa;
import com.cotech.taxislibres.activities.AMapaUpVersion;
import com.cotech.taxislibres.activities.APagarVale;
import com.cotech.taxislibres.activities.ASolicitud;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
//import android.util.Log;
import com.cotech.taxislibres.activities.Log;

public class ServiceTimer extends Service {

	private String TAG="RLPlusSTempo";
	private Timer timer = new Timer(); 
	private TaxisLi appState;
	private Context context;
    private int reporteTiempo;
    private int contsolicitud;
    private int timerEspera;
    private int conttaxicerca;
    private int metrosservice;
    private int contconexion;
    private int timerInactividad;
    private int contesperapago;
    private int contLlegoMsgPush=0;
    
    private MediaPlayer mp; 

    public IBinder onBind(Intent arg0) {
          return null;
    }

    public void onCreate() {
    	Log.i(TAG, "Se Inicializa OnCreate.......: ");
        super.onCreate();
        startService();
    }

    private void startService(){           
    	context = getApplicationContext();
		appState = (TaxisLi) getApplicationContext();
		
        timer.scheduleAtFixedRate(new mainTask(), 0, 1000);
    }

    private class mainTask extends TimerTask{ 
        public void run() {
        	CadaSegundo();
        }
    }    

    
    private void CadaSegundo() {
    	try{				
			reporteTiempo++;
			
	    	//Log.i(TAG, module + ": Contador de segundos  " + reporteTiempo);	 
			
			/**********************************************************************************************/

			if(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){
				timerInactividad++;
				//if(timerInactividad > 300){	//Se debe cerrar la app x inactividad...
				if(timerInactividad > 480){	//Se debe cerrar la app x inactividad...
					//Enviar Intent para cerrar app desde el mapa...
					timerInactividad=0;
					Log.i(TAG,": PASA A CERRAR EL MAPA");
					Intent cerrarapp = new Intent();
					cerrarapp.putExtra("CMD", C.CERRARAPP);

					if(appState.getActividad().equals(C.MAP)){
						cerrarapp.setAction(C.MAP);
						
					}else{
						cerrarapp.setAction(C.SOLICITUD);
					}
					context.sendBroadcast(cerrarapp);
					
				}
    			
			}else{
				timerInactividad=0;
			}
			
			//Log.i(TAG,": CONTADOR DE INACTIVIDAD  " + timerInactividad);
				
			
	    	/**********************************************************************************************/
	    	/**********************************Se revisa cada 5 segundos GPS*********************/
	    	if(reporteTiempo>3){ 
	    		reporteTiempo=0;		    		    			   
	    		try{		    					    					    			
					/*******Se Revisa Ubicacion***************/
	    			Intent ubicacion = new Intent();
	    			ubicacion.putExtra("CMD", C.UBICACION);
	    			ubicacion.setAction(C.MAP);
	    			context.sendBroadcast(ubicacion);
				}catch(Exception e){
					e.printStackTrace();
					Log.e(TAG,": Se totea en consultar ubicacion: " + e);
				}		    		
	    	}		    	
	    	/**********************************************************************************************/   
	    	/**********************************Se envia cada 3 segundos al Servidor************************/	        			        		    	
	    	
	    	if((appState.getEstadoUnidad().equals(EstadosUnidad.SOLICITUD_TAXI))||(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO))
	    			||(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERA))||(appState.getEstadoUnidad().equals(EstadosUnidad.CANCELADO))){		    		
	    			appState.setContador(appState.getContador() + 1);
	    			if(appState.getContador() >= 3){
	    				Log.e(TAG,": ++++++++++++++++++++++++++++++++++++++++++++++++ " );
	    				appState.setContador(0);
	    				/*******Se Pregunta por el Movil**************/
		    			Intent pregunta = new Intent();
		    			pregunta.putExtra("CMD", C.PREGUNTAMOVIL);
		    			pregunta.setAction(C.MAP);
		    			context.sendBroadcast(pregunta);
	   				}		  			    		
	    	}
	    			    			    			    			    
	        /**********************************************************************************************/  
	        /***********************Si no hay respuesta de taxi se quita el processdialog******************/	
		     
	    	if(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){
	    		contsolicitud=0;
	    		
	    	}
	        
	    	if(appState.getFlagsolicitud()==1){
	        	contsolicitud=0;

	        }	        		        		
	        
	    	else if(appState.getFlagsolicitud()==4){
	        Log.i(TAG, ": Contador processdialo:  " + contsolicitud);
	       
	        //if(contsolicitud>110){
	        	
	        		contsolicitud++;
	        		if(contsolicitud>180){
		        		appState.setFlagrecordardireccion(1);
		        		appState.setFlagsolicitud(0);
		        		contsolicitud=0;;
		        		appState.setEstadoUnidad(EstadosUnidad.LIBRE);    		
			    		try{
			    			
			    			/*******Se cierra el ProcessDialog***************/
			    			Intent closedialog = new Intent();
			    			closedialog.putExtra("CMD", C.CANCEL_PROCESSDIALOG);
			    			closedialog.setAction(C.MAP);
			    			context.sendBroadcast(closedialog);
							
							appState.setEstadoUnidad(EstadosUnidad.LIBRE);												
							
							Intent talk = new Intent();
							talk.setAction(C.TEXT_TO_SPEECH);
							talk.putExtra("CMD", C.HABLAR);
							talk.putExtra("TEXTHABLA", "PORFAVOR INTENTE DENUEVO");
							sendBroadcast(talk);
							
							Intent toast = new Intent();
							toast.setAction(C.MAP);
							toast.putExtra("CMD", C.TOAST);
							toast.putExtra("TOAST", "POR FAVOR INTENTE DE NUEVO");
							context.sendBroadcast(toast);
							
							
						}catch(Exception e){
							Log.e(TAG,": Se totea en cancelar process dialog: " + e);
						}
		        	}
	        }
	        		
	        	
	        /**********************************************************************************************/  
	        /****************Si el taxi se encuentra a menos de 150 metros cada 15 segundos ***************/
		        //if(appState.getFlagAnunciotaxi()==7){
		        if((appState.getFlagAnunciotaxi()==7)&&(appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO))){	
		        	conttaxicerca++;
		        	if(conttaxicerca>5){
		        		conttaxicerca=0;	  
		        		if(appState.getMetros()==metrosservice){
		        			
		        		}else{

		        			Intent talk = new Intent();
							talk.setAction(C.TEXT_TO_SPEECH);
							talk.putExtra("CMD", C.HABLAR);
							talk.putExtra("TEXTHABLA", "SUTAXI SEENCUENTRA A" + appState.getMetros() + "metros");
							sendBroadcast(talk);
		        			try{
								
								Intent toast = new Intent();
								toast.setAction(C.MAP);
								toast.putExtra("CMD", C.TOAST);
								toast.putExtra("TOAST", "SU TAXI SEENCUENTRA A:  \n" + appState.getMetros() + "  METROS ");
								metrosservice= appState.getMetros();
							}catch(Exception e){
								Log.e(TAG, ": Se totea en mostrar toast: " + e);
							}
		        		}
		        	}
		        	
		        }
		   /**********************************************************************************************/ 
//		        Log.i(TAG, "JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ" + contesperapago );
		        if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERADEPAGO)){
		        	contesperapago++;
		        	Log.i(TAG, "+++++++++++" + contesperapago +  appState.getFlagConfPago() );
		        	if(contesperapago>=5){
			        		
		        		contesperapago=0;
				        //String consultar = "J|" + appState.getPinMovil() +"|"+appState.getTramaGPSMap()+"\n";
				        String consultar = "J|" + appState.getPinMovil() +"|"+appState.getIdServicio()+"|"+appState.getTramaGPSMap()+"\n";
						
						Intent cons = new Intent();
						cons.putExtra("CMD", C.SEND);
						cons.putExtra("DATA", consultar);
						cons.setAction(C.COM);
						getApplicationContext().sendBroadcast(cons);
						
						Intent toast = new Intent();
						toast.setAction(C.MAP);
						toast.putExtra("CMD", C.TOAST);
						toast.putExtra("TOAST", "ESPERANDO FINALIZAR SERVICIO");
						context.sendBroadcast(toast);
						
//						Intent ubicacion = new Intent();
//		    			ubicacion.putExtra("CMD", C.UBICACION);
//		    			ubicacion.setAction(C.MAP);
//		    			context.sendBroadcast(ubicacion);
						
		        	}
		        	
		        	if(appState.getFlagConfPago()==7){
			        	Log.i(TAG, "+++++vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv++++++");
			        	mp = MediaPlayer.create(getApplicationContext(), R.raw.sonido);
						mp.start();
			        	appState.setFlagConfPago(0);
			        	
			        	//Preguntar Metodo de Pago...
			        	Intent infopago = new Intent();
			        	//if(appState.getFormaPago()== 2){	//FUE VALE ELECTRONICO...
			        	if((appState.getFormaPago()== 2)||(appState.getFormaPago()== 4)){	//FUE VALE ELECTRONICO...
			        		infopago.putExtra("CMD", C.PAGO_CUPO_TARJETA);
			        	}

			        	else{		//DEBE SER EFECTIVO... O VALE CONVENCIONAL
			        		infopago.putExtra("CMD", C.PAGO_EFECTIVO);
			        	}
			        	infopago.setAction(C.MAP);
		    			context.sendBroadcast(infopago);

			        }
		        
		        }
		     //********************VERIFICAR PUSH PENDIENTES POR VER********************************   
		        if((appState.isPushPendiente())&&(appState.getEstadoUnidad().equals(EstadosUnidad.SOLICITUD_TAXI))){
		        	
		        	if(contLlegoMsgPush > 10){
		        		Log.i(TAG, " HAY PUSH PENDIENTE EL USUARIO AUN NO LO VE...");
		        		contLlegoMsgPush=0;
		        		mp = MediaPlayer.create(getApplicationContext(), R.raw.sonido);
						mp.start();
		        	}else{
		        		contLlegoMsgPush++;
		        	}
		        }else{
		        	contLlegoMsgPush=0;
		        }
		      //****************************************************************************
		        //POSIBLE SALIDA SINO PROCESA ENVIO A JUAN D...
		        
		        
	    	
		}catch (Exception e){
			Log.e(TAG, ": Paila  " + e);
		}
	}
    
   
   
    
    public void onDestroy() {	  
    	Log.i(TAG, "Se Cancela el Timer.......: " );	
        super.onDestroy();
        timer.cancel();   
    }

    

}
