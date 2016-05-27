package com.cotech.taxislibres;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;

import com.cotech.taxislibres.R;
import com.cotech.taxislibres.activities.AMapaUpVersion;
import com.cotech.taxislibres.activities.Log;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	// Use your PROJECT ID from Google API into SENDER_ID
	public static final String SENDER_ID = "717193394459";

	private Context context;
	private TaxisLi appState;

	public GCMIntentService() {

		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		context = getApplicationContext();
		appState = ((TaxisLi) context);
		Log.i(TAG, "onRegistered: registrationId=" + registrationId);
		appState.setKeyGcm(registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {

		Log.i(TAG, "onUnregistered: registrationId=" + registrationId);
	}

	@Override
	protected void onMessage(Context context, Intent data) {
		context = getApplicationContext();
		appState = ((TaxisLi) context);
		Log.i(TAG,"Entro por onMessage");
		String message;
		// Message from PHP server
		message = data.getStringExtra("message");
		Log.i(TAG,"Mensaje Push recibido: "+ message);
		// Open a new activity called GCMMessageView

		/**********************SE VERIFICAN LOS PUSH QUE SE MUESTRAN EN PRIMER PLANO****************************/
		if(appState.isAppActiva()&&appState.isAppPrimerPlano()){	//Estamos con la App Abierta
	
			Log.i(TAG," ACA SE DEBEN PROCESAR LOS MENSAJES QUE INGRESAN CUANDO LA APP ESTA ABIERTA...");
			String[] partes = message.split("\\|");
			/**********************PREGUNTAR POR SERVICIO****************************/
			if(partes[0].equals("{c")){

				int estado = Integer.parseInt(partes[1]);
				switch (estado){
				
				case 1:		//Llega el Servicio
				
					Log.i(TAG,"Entro por c|1");
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
					Intent closedialog = new Intent();
					closedialog.putExtra("CMD", C.FILRAR_SERVICIO);
					closedialog.setAction(C.MAP);
					closedialog.putExtra("DATA", message);
					context.sendBroadcast(closedialog);
					Log.i(TAG,"Pasa a quitar el Process dialog");
					break;
				case 2:		//No hay movil...

					Log.i(TAG,"Entro por c|2");
					if(appState.getEstadoUnidad().equals(EstadosUnidad.SOLICITUD_TAXI)){
						appState.setFlagrecordardireccion(1);
						appState.setFlagsolicitud(1);
						
						Intent nomovil = new Intent();
						nomovil.putExtra("CMD", C.NO_MOVIL);
						nomovil.setAction(C.MAP);
						nomovil.putExtra("DATA", message);
						context.sendBroadcast(nomovil);
						Log.i(TAG,"Pasa a quitar el Process dialog");
						
						appState.setFlaginvisible(1);
						appState.setIdServicio("0");
					
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
			/**********************PREGUNTAR MENSAJES DEL TAXISTA****************************/
			else if(partes[0].equals("{CT")){	//Mensaje del Taxista...
				appState.setMsgTaxista(partes[1]);	//En esa posición viene el Msg...
				
				Intent msgt = new Intent();
				msgt.putExtra("CMD", C.MSGTAXISTA);
				//msgt.setAction(C.MAP);
				msgt.setAction(appState.getActividad());
				context.sendBroadcast(msgt);
			}
			/**********************PREGUNTAR MENSAJES DEL CONTACT****************************/
			else if(partes[0].equals("{CC")){	//Mensaje del Contact Center...
				appState.setMensajeCentral(partes[1]);	//En esa posición viene el Msg...
				
				Intent msgt = new Intent();
				msgt.putExtra("CMD", C.MSGCONTACT);
				//msgt.setAction(C.MAP);
				msgt.setAction(appState.getActividad());
				context.sendBroadcast(msgt);
			}
			/**********************PREGUNTAR POR MOVIL AL FRENTE****************************/
			else if(partes[0].equals("{MF")){	//Movil al frente...
				Intent sonido = new Intent();
				sonido.putExtra("CMD", C.SONIDOALFRENTE);
				sonido.setAction(appState.getActividad());
				context.sendBroadcast(sonido);
				
			}
			/**********************FINALIZACION DEL SERVICIO****************************/
			else if(partes[0].equals("{j")){	//Llego el Pago...
				Log.i(TAG," LLEGO EL PAGO POR PUSH...");
				int estado = Integer.parseInt(partes[1]);
				if(estado==0){
					
				}else{
					if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERADEPAGO)){

						if((estado==-1)&&(partes[2].contains("-1"))){
							//Ocurrio Error al pagar... Hay que pasar directamente a la encuesta...
							Intent calificar = new Intent();
							calificar.putExtra("CMD", C.PASA_ENCUESTA);
							calificar.setAction(C.MAP);
							context.sendBroadcast(calificar);
						}else{
							appState.setFlagConfPago(7);
							appState.setIdModiPayConductor(partes[1]);
							appState.setValorPagar(partes[2]); //vapag|cedula|lat|lon|ex o no
							//Hay que verificar si el pago se realizo Satisfactoriamente o No
							if(partes[6].contains("0"))	appState.setPagoTarjetaExitoso(true);
							else	appState.setPagoTarjetaExitoso(false);	
						}
					}
				}
			}
			else{
				
			}
			appState.setPushPendiente(false);
			appState.setMsgPushPendiente("");
		}else{ //CUANDO NO ESTA EN PRIMER PLANO....
			
			MediaPlayer mp = MediaPlayer.create(context, R.raw.sonido);
			mp.start();
			
			appState.setPushPendiente(true);
			appState.setMsgPushPendiente(message);
			
			@SuppressWarnings("rawtypes")
			Class actividad= GCMMessageView.class;
			Intent intent;
			String msgNotificacion="Mensaje App Taxis Libres";
			String[] partes = message.split("\\|");
			/**********************PREGUNTAR POR SERVICIO****************************/
			if(partes[0].equals("{c")){

				int estado = Integer.parseInt(partes[1]);
				switch (estado){
				
				case 1:		//Llega el Servicio
					msgNotificacion="SERVICIO ASIGNADO";
					Log.i(TAG,"Entro por c|1");
					
					break;
				case 2:		//No hay movil...
					msgNotificacion="NO SE LOGRO UBICAR TAXI";
					Log.i(TAG,"Entro por c|2");
									
					break;
				
				}
			}
			/**********************PREGUNTAR MENSAJES DEL TAXISTA****************************/
			else if(partes[0].equals("{CT")){	//Mensaje del Taxista...
				msgNotificacion="MENSAJE DEL TAXISTA";
				appState.setMsgTaxista(partes[1]);	//En esa posición viene el Msg...
				Log.i(TAG,"Entro por Mensaje del Taxista");
			}
			/**********************PREGUNTAR MENSAJES DEL CONTACT****************************/
			else if(partes[0].equals("{CC")){	//Mensaje del Contact Center...
				msgNotificacion="MENSAJE DE LA CENTRAL";
				appState.setMensajeCentral(partes[1]);	//En esa posición viene el Msg...
				Log.i(TAG,"Entro por Mensaje de la Central");
			}
			/**********************PREGUNTAR POR MOVIL AL FRENTE****************************/
			else if(partes[0].equals("{MF")){	//Movil al frente...
				msgNotificacion="SU MOVIL ESTA ALFRENTE";
				Log.i(TAG,"Entro por MF...");
				
			}
			/**********************FINALIZACION DEL SERVICIO****************************/
			else if(partes[0].equals("{j")){	//Llego el Pago...
				msgNotificacion="FIN DEL SERVICIO";
				Log.i(TAG,"Entro por Fin del Servicio");
				int estado = Integer.parseInt(partes[1]);
				if(estado==0){
					
				}else{
					if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERADEPAGO)){

						if((estado==-1)&&(partes[2].contains("-1"))){
							//Ocurrio Error al pagar... Hay que pasar directamente a la encuesta...
//							Intent calificar = new Intent();
//							calificar.putExtra("CMD", C.PASA_ENCUESTA);
//							calificar.setAction(C.MAP);
//							context.sendBroadcast(calificar);
						}else{
							
							appState.setIdModiPayConductor(partes[1]);
							appState.setValorPagar(partes[2]);
							//Hay que verificar si el pago se realizo Satisfactoriamente o No
							if(partes[6].contains("0"))	appState.setPagoTarjetaExitoso(true);
							else	appState.setPagoTarjetaExitoso(false);
						}
					}
				}
			}
			
			else{
				mp.stop();
				appState.setPushPendiente(false);
				appState.setMsgPushPendiente("");
			}
			
			intent = new Intent(this,actividad);
			// Pass data to the new activity
			//intent.putExtra("message", message);
			// Starts the activity on notification click
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			// Create the notification with a notification builder
			Notification notification = new Notification.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher)
			.setWhen(System.currentTimeMillis())
			.setContentTitle(msgNotificacion)
			.setContentText("").setContentIntent(pIntent)
			.getNotification();
			// Remove the notification on click
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			manager.notify(R.string.app_name, notification);

			{
				// Wake Android Device when notification received
				PowerManager pm = (PowerManager) context
						.getSystemService(Context.POWER_SERVICE);
				final PowerManager.WakeLock mWakelock = pm.newWakeLock(
						PowerManager.FULL_WAKE_LOCK
						| PowerManager.ACQUIRE_CAUSES_WAKEUP, "GCM_PUSH");
				mWakelock.acquire();

				// Timer before putting Android Device to sleep mode.
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					public void run() {
						mWakelock.release();
					}
				};
				timer.schedule(task, 5000);
			}
		}

	}

	@Override
	protected void onError(Context arg0, String errorId) {

		Log.e(TAG, "onError: errorId=" + errorId);
	}
	//******************************************************************************************
	public void ProcessDatosAppAbierta (String data){
		Log.i(TAG," ACA SE DEBEN PROCESAR LOS MENSAJES QUE INGRESAN CUANDO LA APP ESTA ABIERTA...");
		String[] partes = data.split("\\|");
		/**********************SOLICITUD DE SERVICIO****************************/
		if(partes[0].equals("{B")){			

			appState.setIdServicio(partes[1]);										
			appState.setEstadoUnidad(EstadosUnidad.SOLICITUD_TAXI);
			appState.setFlagCerrarMapa(1);
		}
		/**********************PREGUNTAR POR SERVICIO****************************/
		else if(partes[0].equals("{c")){

			int estado = Integer.parseInt(partes[1]);
			switch (estado){
			case 0:
				break;
			case 1:
				Log.i(TAG,"Entro por c|1");
				Intent toast = new Intent();
				toast.setAction(C.MAP);
				toast.putExtra("CMD", C.TOAST);
				toast.putExtra("TOAST",data);
				context.sendBroadcast(toast);
				break;
			}
		}
	}
	//******************************************************************************************
	public void PregMovil(){}
}