package com.cotech.taxislibres.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.conn.util.InetAddressUtils;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.EstadosUnidad;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.activities.AFormulario;
import com.cotech.taxislibres.activities.AMapaUpVersion;
import com.cotech.taxislibres.activities.EncriptadorTexto;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;

//import android.util.Log;
import com.cotech.taxislibres.activities.Log;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint({ "NewApi", "ShowToast" })
public class ServiceTCP extends Service{
	
	
	private String TAG = "ServiceTCP";
	protected String module = C.COM;
	public SocketChannel _socket;
	Handler handlerTimer = new Handler();
	private int port;
	private String host;
	private TaxisLi appState;
	private Context context;
	private boolean reinicio = false;
	private IntentFilter filter;
	ByteBuffer buf2;
	
	 
	 private boolean isconect=false;
	 private String IP;
	
	
	 private String Barrio; 
	 private int contador; 
	 private String mensajeTaxista="taxislibres";
	 private String msgEntrante;
	 
	 private AlertDialog alert1;
	
	 
	public final BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Integer cmd = intent.getIntExtra("CMD", 0);
			switch(cmd){
				case(C.SEND):	
					final String data = intent.getStringExtra("DATA");
					try {
						
						Thread thrd = new Thread(){
							public void run(){
								NuevoEnvio(data);
							}
						};
						thrd.start();
					} catch (Exception e) {
						
						Log.i(TAG, module +"EL ERROR OCURRE EN C.SEND :"+ e);
						e.printStackTrace();
					}
					
					break;
					
				case(C.ENCRIPTADO):	
					final String infoEnviar = intent.getStringExtra("DATA");
					try {
						
						Thread thrd = new Thread(){
							public void run(){
								EnvioEncriptado(infoEnviar);
							}
						};
						thrd.start();
					} catch (Exception e) {
						
						Log.i(TAG, module +"EL ERROR OCURRE EN C.ENCRIPTADO :"+ e);
						e.printStackTrace();
					}
					
					break;
					
					
				case(C.DISCONECT):
					
				try {
					Log.i(TAG, "SE DESCONECTA EL SOCKET++++++++++++++++++++++++++++++++++++++++");
					
					_socket.close();
					isconect=false;
				} catch (Exception e) {
					e.printStackTrace();
				}
					
					break;
			}

			
		}
	};
	
	//**************************************************************************************
	public void NuevoEnvio(String message){
		port = 11012;		//Produccion
		//port = 11014;		//Desarrollo
		//port = 11013;		//Aero
		host = "200.91.204.38";
		//host = "190.131.240.58";
		try{
			Socket sck = new Socket(host, port);
			Log.i(TAG, module+" Abriendo Socket..."  );
			sck.setSoTimeout(30000);
//			IP = getLocalIpAddress();
//			Log.i(TAG, module+": LA IP DE LA UNIDAD ES: ++++++++ " + IP + " +++++++++++");
			BufferedWriter bufferSalida = new BufferedWriter(new OutputStreamWriter(sck.getOutputStream(), "LATIN1"));
			boolean esimagen=false;
			if(message.contains("I|"))	esimagen=true;
			bufferSalida.write(message);
			bufferSalida.flush();
			Log.i(TAG, module+"ENVIANDO TRAMA:"+message);
			if(esimagen){
				esimagen=false;
				Intent recimage = new Intent();
				try {

					byte[] bytes = new byte[65536];
					InputStream entrada = sck.getInputStream();
					int a = entrada.read(bytes);
					Log.i(TAG, module+"-----------Numero de Bytes-----: "+ a);
					Log.i(TAG, module+"**********IMPRIME IMAGEN********"); 
		            for(int cont=0;cont<a;cont++){
		            	Log.i(TAG, module+":"+cont+":"+bytes[cont]);
		            }
					sck.close();
					Log.i(TAG, module+" Cerrando Socket..."  );
					//Bitmap bmp=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
					//Intent recimage = new Intent();
					if(a== -1){
						recimage.putExtra("CMD", C.NORECIBIOIMAGEN);
						Log.i(TAG, module+"**********NO HAY IMAGEN********");
					}else{
						recimage.putExtra("CMD", C.RECIBIOIMAGEN);
						Log.i(TAG, module+"**********LEYO IMAGEN********");
					}
					recimage.putExtra("EXTRA", bytes);
					//Hay que preguntar a donde se envia la foto al mapa o al llegotaxi
					if(appState.getActividad().equals(C.LLEGOTAXI))	recimage.setAction(C.LLEGOTAXI);
					else recimage.setAction(C.MAP);
					
					context.sendBroadcast(recimage);
					
										
				} catch (Exception ex) {
//		            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
					recimage.putExtra("CMD", C.NORECIBIOIMAGEN);
					Log.i(TAG, module+"**********ERROR AL CARGAR LA IMAGEN********");
					//recimage.putExtra("EXTRA", bytes);
					//Hay que preguntar a donde se envia la foto al mapa o al llegotaxi
					if(appState.getActividad().equals(C.LLEGOTAXI))	recimage.setAction(C.LLEGOTAXI);
					else recimage.setAction(C.MAP);
		        }
				
			}else{
			BufferedReader bufferEntrada = new BufferedReader(new InputStreamReader(sck.getInputStream()));
			msgEntrante = bufferEntrada.readLine();
			Log.i(TAG, module + ": SE RECIBE :  "+ msgEntrante);
			bufferSalida.close();
			sck.close();
			Log.i(TAG, module+" Cerrando Socket..."  );
			ProcessDatosRx(msgEntrante);
			}
		}catch (Exception ex){
			Log.i(TAG, module + appState.getEstadoUnidad()+": SE RECIBE :Tiempo expirado para recibir respuesta..."+ ex);
			if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERA)){ //Indica que es una Funcion B
				appState.setFlagrecordardireccion(1);
				appState.setFlagsolicitud(1);
				/*******Se cierra el ProcessDialog***************/
				Intent closedialog = new Intent();
				closedialog.putExtra("CMD", C.CANCEL_PROCESSDIALOG);
				closedialog.setAction(C.MAP);
				context.sendBroadcast(closedialog);
				appState.setEstadoUnidad(EstadosUnidad.LIBRE);

				appState.setIdServicio("0");
				
				Intent error = new Intent();
				error.putExtra("CMD", C.ERROR_SOCKET);
				error.setAction(C.MAP);
				context.sendBroadcast(error);
				
			}else if((message.contains("Y|"))){	//Tramas pendientes en Solicitud con Ventana en progreso
				Intent closedialog = new Intent();
				closedialog.putExtra("CMD", C.ERROR_SOCKET);
				//closedialog.setAction(C.SOLICITUD);
				closedialog.setAction(appState.getActividad());
				context.sendBroadcast(closedialog);
			}else if((message.contains("Z|"))){	//Tramas pendientes en Solicitud con Ventana en progreso
				Intent closedialog = new Intent();
				closedialog.putExtra("CMD", C.ERROR_SOCKET);
				//closedialog.setAction(C.SOLICITUD);
				if(appState.getActividad().contains("MAP"))	closedialog.setAction(C.MAP);
				context.sendBroadcast(closedialog);
			}
			else{
				//SendToast("LO SENTIMOS NO HAY COMUNICACION CON EL SERVIDOR!!!" +"\n POR FAVOR INTENTE DE NUEVO");
			}
			
	      }

	}
	
	
	//***************************************************************************************
	
	public void EnvioEncriptado(String message){
		
		int portEncrip = 11020;				//Puerto Desarrollo
		
		//String hostEncrip = "192.168.34.15";	//IP Interna
		String hostEncrip = "200.91.204.38";	//IP Externa
		
		
		try{
			Socket sck = new Socket(hostEncrip, portEncrip);
			Log.i(TAG, module+" Abriendo Socket para Envio info Tarjeta de Credito..."  );
			sck.setSoTimeout(180000);
//			String ipEncrip = sck.getLocalAddress().toString()+":"+sck.getLocalPort();
			String ipEncrip = "";
			
//			if(sck.getLocalAddress().toString().contains("/192.")){
//				//ipEncrip = "/"+getWifiApIpAddress()+":"+sck.getLocalPort();
//				ipEncrip = "/"+getWifiApIpAddress();
//				Log.i(TAG, module+"SE CONECTA POR WIFI"  );
//			}else{
////				ipEncrip = sck.getLocalAddress().toString()+":"+sck.getLocalPort();
//				ipEncrip = sck.getLocalAddress().toString();
//				Log.i(TAG, module+"SE CONECTA POR MOVIL"  );
//			}
			if(message.contains("realizarPagoTarjetaCredito")){
				message= message.replace("IP_DEL_MOVIL", getWifiApIpAddress());
				message= message.replace("MX_PRU_001", getWifiApIpAddress()+"_"+appState.getIdModiPayConductor());
				//
				Log.i(TAG, module+"TRAMA DE REALIZAR PAGO: "+ message);
				Log.i(TAG, module+"------------------------------------------------------");
			}
			
			
			
			
//			WifiManager wifi =(WifiManager)getSystemService(Context.WIFI_SERVICE); 
//			if(wifi.isWifiEnabled()){
//							
//				Log.i(TAG, module +  "---------------------------POR WIFI-----------------------------------");
//				ipEncrip = "/"+getWifiApIpAddress();
//				
//			}else{
//				Log.i(TAG, module +  "---------------------------POR RED-----------------------------------");
//				IP= getLocalIpAddress();
//				ipEncrip = "/"+IP;
//				
//			}

			
			ipEncrip = "VDR4MXNMaWJyM3NFcyBsYSAzbXByZVM0IG51bTNyMCB1bm8gM24gQzBsMG1iMTQgeSBNM1hpYzAsIDIxMTExMTEsIHNpM25kMCBsNCBtw4FzIGdyQW5kMyBkM2wgUGExcyEkJSY=";
			
			Log.i(TAG, module + "Resultado de getWifiApIpAddress =  " + ipEncrip);
			
			
			Log.i(TAG, module+": LA IP DE CONEXION ES: ++++++++ " + ipEncrip + " +++++++++++");
			BufferedWriter bufferEncrip = new BufferedWriter(new OutputStreamWriter(sck.getOutputStream(), "LATIN1"));
			//Se encripta la información
			//EncriptadorTexto encriptadorTexto = new EncriptadorTexto(ipEncrip);
			//Log.i(TAG, module+": LA IP DE CONEXION ES: ++++++++ " + ipEncrip +":TaxisLibres" + " +++++++++++");
			//EncriptadorTexto encriptadorTexto = new EncriptadorTexto(ipEncrip+":TaxisLibres");
			EncriptadorTexto encriptadorTexto = new EncriptadorTexto(ipEncrip);
			String mensajeEncriptado = encriptadorTexto.encriptar(message)+"\n";
			//Se envia Msg Encriptado
			Log.i(TAG, module + " --------------------------    Enviando Texto Encriptado   =  " + mensajeEncriptado);
			bufferEncrip.write(mensajeEncriptado);
			bufferEncrip.flush();
			Log.i(TAG, module+"ENVIANDO TRAMA:"+message);
			Log.i(TAG, module+"ENCRIPTADA:"+mensajeEncriptado);
			
			BufferedReader bufferInEncrip = new BufferedReader(new InputStreamReader(sck.getInputStream()));
			String msgInEncrip = bufferInEncrip.readLine();
			Log.i(TAG, module + ": SE RECIBE ENCRIPTADO :  "+ msgInEncrip+"\n");
			bufferEncrip.close();
			sck.close();
			Log.i(TAG, module+" Cerrando Socket..."  );
			String mensajeDesencriptado = encriptadorTexto.desencriptar(msgInEncrip);
			Log.i(TAG, module+" SE RECIBE DESENCRIPTADO:"+ mensajeDesencriptado+"\n");
			appState.setRtaError(mensajeDesencriptado);
			ProcessDatosInEncrip(mensajeDesencriptado);
			
		}catch (Exception ex) {
			ex.printStackTrace();
			Log.i(TAG, module + ": SE RECIBE Socket Exception Encriptado:"+ ex);
//			if(appState.getComandoPayu().equals("CREATE_TOKEN")){
//				Intent respuetaPayu = new Intent();
//				respuetaPayu.putExtra("CMD", C.RESPUESTA_SOCKET);
//				respuetaPayu.setAction(C.REGISTRAR_TARJETA);
//				context.sendBroadcast(respuetaPayu);
//			}
//			else if(appState.getComandoPayu().equals("GET_TOKENS")){
			if(appState.getComandoPayu().equals("GET_TOKENS")){
				Intent respuetaPayu = new Intent();
				respuetaPayu.putExtra("CMD", C.ERROR_SOCKET);
				//Hay que preguntar donde se hizo el get Tokens para enviar la rta a esa actividad...
				Log.i(TAG, module + "Viene de: " + appState.getActividad());
				if(appState.getActividad().contains("MAP"))	respuetaPayu.setAction(C.MAP);
				else	respuetaPayu.setAction(C.SOLICITUD);
				context.sendBroadcast(respuetaPayu);
			}
			else if((appState.getComandoPayu().equals("REMOVE_TOKEN"))||
					(appState.getComandoPayu().equals("ELIMINAR_TARJETA"))){
				Intent respuetaPayu = new Intent();
				respuetaPayu.putExtra("CMD", C.ERROR_SOCKET);
				respuetaPayu.setAction(C.LISTAR_TARJETAS);
				context.sendBroadcast(respuetaPayu);
			}
			else if((appState.getComandoPayu().equals("SUBMIT_TRANSACTION"))||
					(appState.getComandoPayu().equals("REALIZAR_PAGO"))||(message.contains("olvidoPasswd"))){
				Intent respuetaPayu = new Intent();
				respuetaPayu.putExtra("CMD", C.ERROR_SOCKET);
				respuetaPayu.setAction(C.PAGO_NO_EFECTIVO);
				context.sendBroadcast(respuetaPayu);
			}else if(appState.getComandoPayu().equals("REGISTAR_USUARIO")){
				Intent respuestaPayu = new Intent();
				respuestaPayu.putExtra("CMD", C.ERROR_SOCKET);
				respuestaPayu.setAction("Entrada");
				context.sendBroadcast(respuestaPayu);
			}else if((appState.getComandoPayu().equals("REGISTAR_MIEMBRO_CLAVE"))||
					(appState.getComandoPayu().equals("INSERTAR_TARJETA"))||
					(appState.getComandoPayu().equals("ACCESAR_USUARIO_REGISTRO"))){
				Intent respuetaPayu = new Intent();
				respuetaPayu.putExtra("CMD", C.RESPUESTA_SOCKET);
				respuetaPayu.setAction(C.REGISTRAR_TARJETA);
				context.sendBroadcast(respuetaPayu);
			}else if(appState.getComandoPayu().equals("ACTUALIZAR_USUARIO")){
				Intent respuestaPayu = new Intent();
				respuestaPayu.putExtra("CMD", C.ERROR_SOCKET);
				respuestaPayu.setAction("AActualizarDatos");
				context.sendBroadcast(respuestaPayu);
			}else if((appState.getComandoPayu().equals("CONSULTA_PROMO"))||
			(appState.getComandoPayu().equals("LISTAR_PROMO"))){
				Intent respuestaPromo = new Intent();
				respuestaPromo.putExtra("CMD", C.ERROR_PROMO);
				respuestaPromo.setAction(C.MAP);
				context.sendBroadcast(respuestaPromo);
			}
			
			
		}
	}
	
	//****************************************************************************************
	
	public String getWifiApIpAddress() {
		URL ipAdress;
		 try {
	            ipAdress = new URL("http://myexternalip.com/raw");
	            BufferedReader in = new BufferedReader(new InputStreamReader(ipAdress.openStream()));
	            
	            String ip = in.readLine();
	            Log.d(TAG, module + " ----------------------getWifiApIpAddress-----------------------------------  "   + ip);
	            return ip;
	        } //catch (IOException e) {
		 	catch (Exception e){
	            e.printStackTrace();
	        }
		 return null;
	}
	
	//****************************************************************************************

	public String getLocalIpAddress() {
    	try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    

          String ipv4;
		
          if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {

                        String ip = inetAddress.getHostAddress().toString();                
                        IP = ip;
                        return ipv4;
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    
    }
	
		
	//****************************************************************************************
	//	PROCESAR DATOS QUE LLEGAN DE JUAN C
	//****************************************************************************************
	
	public String ProcessDatosRx (String data){
		
		String[] partes = data.split("\\|");	
		/***********************ZONIFICAR**********************************/
		Log.i(TAG, module+"Llego el Codigo: "+ partes[0]);

		/**********************SOLICITUD DE SERVICIO****************************/
		if(partes[0].equals("{B")){			
			contador=0;								
			appState.setIdServicio(partes[1]);										
			appState.setEstadoUnidad(EstadosUnidad.SOLICITUD_TAXI);
			appState.setFlagCerrarMapa(1);
		}
		
		/**********************PREGUNTAR POR SERVICIO****************************/
		else if(partes[0].equals("{C")){
			
			int estado = Integer.parseInt(partes[1]);
			switch (estado){
				case 0:
					break;
				case 1:
					appState.setFlagrecordardireccion(0);
					
					appState.setPlaca(partes[2]);
					appState.setPinMovil(partes[3]);
					appState.setNombreTaxista(partes[4]);
					appState.setLatitudMovil(partes[5]);
					appState.setLongitudMovil(partes[6]);
					
					if(partes.length>7){
						appState.setMsgTaxista(partes[7]);
						
						

						
							if(mensajeTaxista.equals(appState.getMsgTaxista())){
								
							}else{
								
								if(appState.getMsgTaxista().equals("FFTTEE0")){
									
									/*************Movil Alfrente**********************/
//									Intent sonido = new Intent();
//									sonido.putExtra("CMD", C.SONIDOALFRENTE);
//									sonido.setAction(C.MAP);
//									context.sendBroadcast(sonido);

								}
								
								else{
								
//								/*******Se Recibe Mensaje del Taxista***************/
//								Intent msgt = new Intent();
//								msgt.putExtra("CMD", C.MSGTAXISTA);
//								//msgt.setAction(C.MAP);
//								msgt.setAction(appState.getActividad());
//								context.sendBroadcast(msgt);
//								
//								Intent k = new Intent();
//								k.putExtra("CMD", C.HABLAR);
//								k.putExtra("TEXTHABLA", "NUEVO MENSAJE DELTAXISTA: ");	
//								k.setAction(C.TEXT_TO_SPEECH);
//								context.sendBroadcast(k);
								
							}
						}
						
						mensajeTaxista = appState.getMsgTaxista();
					}

					//ConsultarMovil();
					break;
				case 2:		
					SinMovil();
					break;	
				
				case 3:
					//Llega la cancelación del Servicio x parte del Taxista...
					Intent closedialog = new Intent();
					closedialog.putExtra("CMD", C.CANCELO_TAXISTA);
					closedialog.setAction(C.MAP);
					context.sendBroadcast(closedialog);
					break;
			}
			
			
	
		}
		
		/***********************RESPUESTA DE ERROR**************************************************************/
		else if(partes[0].equals("{D")){
			
			if(partes[1].equals("N-ACK")){
				Log.i(TAG, module+ "ENTRO A N-ACK");
				try{
				if(partes[2].equals("5")){	//Celular
					Log.i(TAG, module+ "ERROR 5");
					CloseDialogMap();
					LeerError("ERROR NUMERODE CELULAR INVALIDO,PORFAVOR VERIFIQUELO");
					//Toast.makeText(getApplicationContext(), "ERROR NUMERO DE CELULAR INVALIDO", 5000).show();
					SendToast("ERROR NUMERO DE CELULAR INVALIDO");
					appState.setEstadoUnidad(EstadosUnidad.LIBRE);
				}else if(partes[2].equals("6")){	//Ciudad o Pais...
					Log.i(TAG, module+ "ERROR 6");
					CloseDialogMap();
					LeerError("ERROR ENEL MOMENTONO CONTAMOS CONESTE SERVICIO ENESTA CIUDAD");
					//Toast.makeText(getApplicationContext(), "ERROR EN EL MOMENTO NO CONTAMOS CON ESTE SERVICIO EN ESTA CIUDAD", 5000).show();
					SendToast("ERROR EN EL MOMENTO NO CONTAMOS CON ESTE SERVICIO EN ESTA CIUDAD");
					appState.setEstadoUnidad(EstadosUnidad.LIBRE);
					
				}else if(partes[2].equals("1")){
					Log.i(TAG, module+ "ERROR 1");
					CloseDialogMap();
					LeerError("ERROR DATOSGPS INVALIDOS NOES POSIBLE CREAREL SERVICIO");
					//Toast.makeText(getApplicationContext(), "ERROR DATOS GPS INVALIDOS NO ES POSIBLE CREAR EL SERVICIO", 8000).show();
					SendToast("ERROR DATOS GPS INVALIDOS NO ES POSIBLE CREAR EL SERVICIO");
					appState.setEstadoUnidad(EstadosUnidad.LIBRE);
					
				}else if(partes[2].equals("7")){	//Ciudad o Pais...
					Log.i(TAG, module+ "ERROR 7");
					CloseDialogMap();
					if((appState.getCiudadPais().contains("COLOMBIA"))||(appState.getCiudadPais().contains("Colombia"))){
						LeerError("ENEL MOMENTONO ESPOSIBLE PROCESARSU SOLICITUD,COMUNIQUESE CONNUESTRA LINEA 2 11 11 11 O 3 11 11 11");
						//Toast.makeText(getApplicationContext(), "ERROR EN EL MOMENTO NO CONTAMOS CON ESTE SERVICIO EN ESTA CIUDAD", 5000).show();
						SendToast("NO ES POSIBLE PROCESAR SU SOLICITUD, COMUNIQUESE CON NUESTRA LINEA 2111111 3111111");
					}else if((appState.getCiudadPais().contains("MEXICO"))||(appState.getCiudadPais().contains("Mexico"))){
						LeerError("ENEL MOMENTONO ESPOSIBLE PROCESARSU SOLICITUD,COMUNIQUESE CONNUESTRA LINEA 51 11 11 11");
						//Toast.makeText(getApplicationContext(), "ERROR EN EL MOMENTO NO CONTAMOS CON ESTE SERVICIO EN ESTA CIUDAD", 5000).show();
						SendToast("NO ES POSIBLE PROCESAR SU SOLICITUD, COMUNIQUESE CON NUESTRA LINEA 51 11 11 11");
					}else{
						LeerError("ENEL MOMENTONO ESPOSIBLE PROCESARSU SOLICITUD");
						SendToast("NO ES POSIBLE PROCESAR SU SOLICITUD");
					}
					appState.setEstadoUnidad(EstadosUnidad.LIBRE);
					
				}else {
					Log.i(TAG, module+ "ERROR SIN DEFINIR");
					CloseDialogMap();
					LeerError("ERROR SINDEFINIR");
					//Toast.makeText(getApplicationContext(), "ERROR SIN DEFINIR", 5000).show();
					SendToast("ERROR SIN DEFINIR");
					appState.setEstadoUnidad(EstadosUnidad.LIBRE);
					
				}
				
				}catch(Exception e){
					Log.i(TAG, module+ "ERROR SIN DEFINIR"+e);
					CloseDialogMap();
					
				}			
	
				
			}
				
				
			
		}
		
		/****************PARA SABER SI HAY QUE PAGAR YA *************************************/
		else if(partes[0].equals("{J")){
			int estado = Integer.parseInt(partes[1]);
			
			if(estado==0){
				if(partes.length==6){
					Log.i(TAG, module +"Llega Latitud del Taxi: "+partes[4]);
					Log.i(TAG, module +"Llega Longitud del Taxi: "+partes[5]);
					appState.setLatitudMovil(partes[4]);
					appState.setLongitudMovil(partes[5]);
					Intent ubicacion = new Intent();
	    			ubicacion.putExtra("CMD", C.UBICACION);
	    			ubicacion.setAction(C.MAP);
	    			context.sendBroadcast(ubicacion);
				}
			}else{
				//if(!appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){
//				if(appState.getEstadoUnidad().equals(EstadosUnidad.ESPERADEPAGO)){
//					
//					if((estado==-1)&&(partes[2].contains("-1"))){
//						//Ocurrio Error al pagar... Hay que pasar directamente a la encuesta...
//						Intent calificar = new Intent();
//						calificar.putExtra("CMD", C.PASA_ENCUESTA);
//						calificar.setAction(C.MAP);
//		    			context.sendBroadcast(calificar);
//					}else{
//						appState.setFlagConfPago(7);
//						appState.setIdModiPayConductor(partes[1]);
//						appState.setValorPagar(partes[2]);
//					}
//					Log.i(TAG, module + "***** Entra a el pago Parce*******************************************");
//					
//				}
			    
			}
			
		}
		
		/***************************RESPUESTA OK**********************************************************/
		else if(partes[0].equals("{S-ACK")){
			
			
			if(!appState.getEstadoUnidad().equals(EstadosUnidad.TAXI_ASIGNADO)){
				//appState.setEstadoUnidad(EstadosUnidad.LIBRE);
				Log.i(TAG, module+": EN LA dssssssss: "  +  appState.getEstadoUnidad());

				/*******Se Coloca invisible los botones***************/
				
				appState.setFlaginvisible(1);
				Intent invisible = new Intent();
				invisible.putExtra("CMD", C.INVISIBLEBOTON);
				invisible.setAction(C.MAP);
				context.sendBroadcast(invisible);
				
			}
				
				SendToast("PETICION RECIBIDA ");
			
			
			
		}
		
		/*****************************PRUEBA DE RED********************************************************/
		else if(partes[0].equals("{O")){	
			if(appState.getEstadoUnidad().equals(EstadosUnidad.PRUEBA_RED))	appState.setEstadoUnidad(EstadosUnidad.LIBRE);
			Log.i(TAG, module+"RRRRR---- COMUNICACION OK ----RRRRR");
		}
		
		/************LLEGA INFO DEL DESTINO PARA CALCULAR TARIFA*******************************/
		else if(partes[0].equals("{G")){
			if(partes[1].equals("0")){
				//HAY QUE AVISAR AL USUARIO QUE ESTA SUCURSAL NO CUENTA CON ESTE SERVICIO
				SendToast("ESTA SUCURSAL NO CUENTA CON ESTE SERVICIO");
				LeerError("ESTA SUCURSAL NOCUENTA CONESTE SERVICIO");
			}
			else{
				if(partes[5].equals("0.0")){
					//HAY QUE AVISAR AL USUARIO QUE COLOQUE NUEVAMENTE EL DESTINO
					SendToast("SR USUARIO PORFAVOR CORREGIR EL DESTINO");
					LeerError("SEÑOR USUARIO PORFAVOR CORREGIR EL DESTINO");
				}else{
					String tarificar = "Q|" + appState.getNumberPhone() + "|"+ partes[1] + "|" +
				    appState.getTramaGPSMap() + "|" + partes[5]+"|" +partes[6]+"\n";
					Intent consultarifa = new Intent();
					consultarifa.putExtra("CMD", C.SEND);
					consultarifa.putExtra("DATA", tarificar);
					consultarifa.setAction(C.COM);
		    		getApplicationContext().sendBroadcast(consultarifa);
				}
			}
		}
		
		/******************SE RECIBE INFO DE LA TARIFA PRELIQUIDADA****************************/			
		else if(partes[0].equals("{Q")){
			//Hay que guardar el precio y la distancia Aproximada
			SendToast("Valor Carrera:"+partes[1]+"\n"+"Distancia Aprox:"+partes[3]);
			appState.setValorCarrera(partes[1]);
		}
		
		/******************SE RECIBE INFO DEL VALE****************************/			
		else if(partes[0].equals("{Y")){
			//Hay que revisar Info adicional si el Vale es correcto o no...
			if(partes[1].equals("0")){
				if(partes[2].equals("OK")){
					Intent rta_vale = new Intent();
					rta_vale.putExtra("CMD",C.VALE_OK_SIN_CODIGO);
					rta_vale.putExtra("DATA",data);
					//rta_vale.setAction(C.SOLICITUD);
					rta_vale.setAction(C.MAP);
					getApplicationContext().sendBroadcast(rta_vale);
				}
				else{
					Intent rta_vale = new Intent();
					rta_vale.putExtra("CMD",C.VALE_MAL_SIN_CODIGO);
					rta_vale.putExtra("DATA",data);
					//rta_vale.setAction(C.SOLICITUD);
					rta_vale.setAction(C.MAP);
					getApplicationContext().sendBroadcast(rta_vale);
					
				}
			}

			else if(partes[1].equals("1")){	//Vale en la Base Nueva
				String add="";
				if(partes[2].equals("0")){	//Supone que es Fisico en la Nueva Base
					add="0";
				}else{	//Supone que es Digital en la Nueva Base
					add="1";
				}
				Intent rta_vale = new Intent();
				//rta_vale.putExtra("CMD",C.VALE_OK_SIN_CODIGO);
				rta_vale.putExtra("CMD",C.VALE_OK_CON_CODIGO);
				rta_vale.putExtra("DATA",add);
				//rta_vale.setAction(C.SOLICITUD);
				rta_vale.setAction(C.MAP);
				getApplicationContext().sendBroadcast(rta_vale);
				
			}
			else if(partes[1].equals("2")){		//Vale no Disponible sea nueva o antigua Base
				Intent rta_vale = new Intent();
				rta_vale.putExtra("CMD",C.VALE_MAL_SIN_CODIGO);
				rta_vale.putExtra("DATA",data);
				//rta_vale.setAction(C.SOLICITUD);
				rta_vale.setAction(C.MAP);
				getApplicationContext().sendBroadcast(rta_vale);
			}
			else if(partes[1].equals("3")&&(partes[2].equals("OK"))){	//Codigo Correcto, Se puede usar el Vale
				Intent rta_vale = new Intent();
				//rta_vale.putExtra("CMD",C.VALE_OK_SIN_CODIGO);
				rta_vale.putExtra("CMD",C.VALE_CODIGO_OK);
				rta_vale.putExtra("DATA",data);
				//rta_vale.setAction(C.SOLICITUD);
				rta_vale.setAction(C.MAP);
				getApplicationContext().sendBroadcast(rta_vale);
			}
			else if(partes[1].equals("4")){	//Error de Codigo para la base nueva
				Intent rta_vale = new Intent();
				rta_vale.putExtra("CMD",C.VALE_OK_CODIGO_MAL);
				rta_vale.putExtra("DATA",data);
				//rta_vale.setAction(C.SOLICITUD);
				rta_vale.setAction(C.MAP);
				getApplicationContext().sendBroadcast(rta_vale);
			}
			//Para lista de Vales
			else if(partes[1].contains("\"error\":\"ok\"")){
				Log.i(TAG, module+ " LLEGO LISTA DE VALES OK");
				Intent rta_vale = new Intent();
				rta_vale.putExtra("CMD",C.LISTA_VALES_OK);
				rta_vale.putExtra("DATA",data);
				rta_vale.setAction(C.LISTAR_VALES);
				getApplicationContext().sendBroadcast(rta_vale);
			}
			else if(partes[1].contains("\"error\":")){
				Log.i(TAG, module+ " NO LLEGO LISTA DE VALES, SE PRODUJO ERROR AL CONSULTAR");
				Intent rta_vale = new Intent();
				rta_vale.putExtra("CMD",C.LISTA_VALES_ERROR);
				rta_vale.putExtra("DATA",data);
				rta_vale.setAction(C.LISTAR_VALES);
				getApplicationContext().sendBroadcast(rta_vale);
			}
			
			else{
				Intent rta_vale = new Intent();
				rta_vale.putExtra("CMD",C.VALE_MAL_SIN_CODIGO);
				rta_vale.putExtra("DATA",data);
				//rta_vale.setAction(C.SOLICITUD);
				rta_vale.setAction(C.MAP);
				getApplicationContext().sendBroadcast(rta_vale);
			}
		}
		
		/******************SE RECIBE INFO DE LOS FAVORITOS****************************/
		else if(partes[0].equals("{Z")){
			Intent rta_favoritos = new Intent();
			if(partes[1].equals("0"))	rta_favoritos.putExtra("CMD",C.NO_HAY_FAVORITOS);
			else if(partes[1].equals("1"))	rta_favoritos.putExtra("CMD",C.FAVORITOS_OK);
			else if(partes[1].equals("2")){
				if(partes[2].equals("true"))	rta_favoritos.putExtra("CMD",C.ALMACENAR_FAVORITOS_OK);
				else	rta_favoritos.putExtra("CMD",C.ALMACENAR_FAVORITOS_ERROR);
			}else if(partes[1].equals("3")){
				if(partes[2].equals("true"))	rta_favoritos.putExtra("CMD",C.FAVORITO_BORRADO_OK);
				else	rta_favoritos.putExtra("CMD",C.FAVORITO_BORRADO_ERROR);
			}
			else	rta_favoritos.putExtra("CMD",C.ERROR_CARGANDO_FAVORITOS); 
			rta_favoritos.putExtra("DATA",data);
			//Preguntar de que actividad viene
			if(appState.getActividad().equals(C.MAP))	rta_favoritos.setAction(C.MAP);
			else if(appState.getActividad().equals(C.LISTAR_FAVORITOS))	rta_favoritos.setAction(C.LISTAR_FAVORITOS);
			getApplicationContext().sendBroadcast(rta_favoritos);
		}
		return "";
	
	}
	
	//************************************************************************************
	// Revisa lo que viene de PAYU o del Socket encriptado....
	//************************************************************************************
	
	public String ProcessDatosInEncrip (String data){
		Log.i(TAG, module+ "COMANDO: "+appState.getComandoPayu()+"\n");
		
		if(data.contains("ERROR: PING - No hay comunicación con PayU")){
			if(appState.getComandoPayu().equals("CREATE_TOKEN")){
				Intent respuetaPayu = new Intent();
				respuetaPayu.putExtra("CMD", C.RESPUESTA_FALLOPING);
				respuetaPayu.setAction(C.REGISTRAR_TARJETA);
				context.sendBroadcast(respuetaPayu);
			}
			
		}else if(data.contains("olvidoPasswd")){

			Intent olvido = new Intent();
			olvido.putExtra("CMD", C.RESPUESTA_OLVIDO_CLAVE);
			olvido.putExtra("DATA",data);	//Pasa info a la Actividad
			if(appState.getEstadoUnidad().equals(EstadosUnidad.PAGANDO_VALE))	olvido.setAction(C.PAGO_NO_EFECTIVO);
			else olvido.setAction(C.ENTRADA);
			context.sendBroadcast(olvido);
		} else{

			if(appState.getComandoPayu().equals("GET_TOKENS")){
				Log.i(TAG, module+"*ES GET_TOKENS*");
				if(data.contains("0|")){	//Debe ser 0| -> Indica Lectura de Tarjetas OK
					String[] buscar = data.split("\"");	

					int cont3=0;
					while(cont3 < buscar.length){
						Log.i(TAG, module+"Posicion["+cont3+"]: "+ buscar[cont3]+"\n");
						cont3++;
					}

					//if(buscar[3].equals("SUCCESS")){	//Se hizo GET_TOKENS Correctamente...
					if((buscar[3].equals("SUCCESS"))||(buscar[3].equals(""))){	//Se hizo GET_TOKENS Correctamente...
						Log.i(TAG, module+" FUE SUCCESS... HAY QUE LEER LAS TARJETAS");
						String[] tarjetas = data.split("creditCardTokenId");
						int numeroTarjetas = (tarjetas.length - 1);
						appState.setNumTarjetas(numeroTarjetas);	//Guardar el # de Tarjetas
						Log.i(TAG, module+"El cliente tiene: "+ numeroTarjetas +" tarjetas"+"\n");
						String[] posiciones;
						int cont=0;
						while(cont<numeroTarjetas){
							posiciones = tarjetas[cont+1].split("\"");
							int cont2=0;
							while(cont2 < posiciones.length){
								Log.i(TAG, module+"Posicion["+cont2+"] Tarjeta"+(cont+1)+": "+ posiciones[cont2]+"\n");
								cont2++;
							}
							cont++;
							//Aca se debe guardar la info de cada tarjeta de credito...
							//PARA MEXICO PAYU
							//2 tokenId
							//6 nombre
							//14 identNumber"Cedula"
							//18 franquicia de la tarjeta
							//30 numero de la tarjeta
							//PARA COLOMBIA MODIPAY
							//2 Id Tarjeta Modipay
							//18 Nombre Vacio
							//22 Cedula Vacio
							//26 franquicia de la tarjeta
							//10 numero de la tarjeta
							String infoTdC="";
							if(appState.getCiudadPais().contains("COLOMBIA")){
								//infoTdC=posiciones[14]+"|"+posiciones[18]+"|"+posiciones[22]+"|"+posiciones[26]+"|"+posiciones[10];
								//Para no dejar el Nombre Vacio...
								infoTdC=posiciones[2]+"|"+appState.getNombreUsuario()+"|"+posiciones[22]+"|"+posiciones[26]+"|"+posiciones[10];

							}else{
								infoTdC=posiciones[2]+"|"+posiciones[6]+"|"+posiciones[14]+"|"+posiciones[18]+"|"+posiciones[30];
							}
							Log.i(TAG, module+"Tarjeta "+ cont +": "+infoTdC+"\n");
							if(cont==1){
								appState.setInfoTarjeta1(infoTdC);
								Log.i(TAG, module+" GUARDA INFO DE LA TARJETA 1");
							}else if(cont==2){
								appState.setInfoTarjeta2(infoTdC);
								Log.i(TAG, module+" GUARDA INFO DE LA TARJETA 2");
							}else if(cont==3){
								appState.setInfoTarjeta3(infoTdC);
								Log.i(TAG, module+" GUARDA INFO DE LA TARJETA 3");
							}
						}
						//Enviar Intent a donde se envio el GET_TOKENS
						Intent rta_gettoken = new Intent();
						rta_gettoken.putExtra("DATA",data);
						if(appState.getActividad().equals(C.SOLICITUD)){	//Se envio desde Solicitud
							rta_gettoken.putExtra("CMD",C.GET_TOKENS_OK );
							rta_gettoken.setAction(C.SOLICITUD);
						}else{	//Se envio desde el Mapa
							rta_gettoken.putExtra("CMD",C.TOKENS_MAPA_OK);
							rta_gettoken.setAction(C.MAP);
						}

						getApplicationContext().sendBroadcast(rta_gettoken);

					}else{
						//Validar Errores que se pueden presentar
						Log.i(TAG, module+" NO FUE SUCCESS... HAY QUE REVISAR");
						Intent rta_gettoken = new Intent();
						rta_gettoken.putExtra("DATA",data);
						if(appState.getActividad().equals(C.SOLICITUD)){	//Se envio desde Solicitud
							rta_gettoken.putExtra("CMD",C.GET_TOKENS_ERROR );
							rta_gettoken.setAction(C.SOLICITUD);
						}else{	//Se envio desde el Mapa
							rta_gettoken.putExtra("CMD",C.TOKENS_MAPA_ERROR);
							rta_gettoken.setAction(C.MAP);
						}
						getApplicationContext().sendBroadcast(rta_gettoken);
					}
				}else{	//Debe ser 1| -> Indica Error
					//Validar Errores que se pueden presentar
					Log.i(TAG, module+" NO FUE SUCCESS... HAY QUE REVISAR");
					Intent rta_gettoken = new Intent();
					rta_gettoken.putExtra("DATA",data);
					if(appState.getActividad().equals(C.SOLICITUD)){	//Se envio desde Solicitud
						rta_gettoken.putExtra("CMD",C.GET_TOKENS_ERROR );
						rta_gettoken.setAction(C.SOLICITUD);
					}else{	//Se envio desde el Mapa
						rta_gettoken.putExtra("CMD",C.TOKENS_MAPA_ERROR);
						rta_gettoken.setAction(C.MAP);
					}
					getApplicationContext().sendBroadcast(rta_gettoken);
				}

			}else{
				String[] posiciones = data.split("\"");	

				int cont2=0;
				while(cont2 < posiciones.length){
					Log.i(TAG, module+"Posicion["+cont2+"]: "+ posiciones[cont2]+"\n");
					cont2++;
				}

				//***************************ES CREAR TOKEN

				if(appState.getComandoPayu().equals("CREATE_TOKEN")){

					Log.i(TAG, module+"*ES CREATE_TOKEN*");
					Intent respuetaPayu = new Intent();

					if(posiciones[3].equals("SUCCESS")){	//Se Creo Token Correctamente...
						respuetaPayu.putExtra("CMD", C.RESPUESTA_OK);
						appState.setNumTarjetas(appState.getNumTarjetas()+1);	//Guardar el # de Tarjetas
					}
					else if(posiciones[3].equals("ERROR")){	//Se genero un error al Crear Token...
						respuetaPayu.putExtra("CMD", C.RESPUESTA_MAL);
					}
					else{	//Errores no definidos...
						respuetaPayu.putExtra("CMD", C.RESPUESTA_DESCONOCIDA);
					}

					respuetaPayu.setAction(C.REGISTRAR_TARJETA);
					context.sendBroadcast(respuetaPayu);
				}
				
				//***************************ES SUBMIT_TRANSACTION
				
				else if(appState.getComandoPayu().equals("SUBMIT_TRANSACTION")){
					
					Log.i(TAG, module+"*ES SUBMIT_TRANSACTION*");
					if(posiciones[17].equals("APPROVED")){	//Se Realizo el pago Correctamente...
						Log.i(TAG, module+"*EL PAGO SE REALIZO CORRECTAMENTE*");
						Intent respuestaPayu = new Intent();
						respuestaPayu.putExtra("CMD", C.PAGO_OK);
						respuestaPayu.setAction(C.PAGO_NO_EFECTIVO);
						context.sendBroadcast(respuestaPayu);
						
					}else{
						Log.i(TAG, module+"*EL PAGO FALLO HAY QUE INTENTAR NUEVAMENTE*");
						Intent respuestaPayu = new Intent();
						respuestaPayu.putExtra("CMD", C.PAGO_MAL);
						respuestaPayu.setAction(C.PAGO_NO_EFECTIVO);
						context.sendBroadcast(respuestaPayu);
					}
				}
				
				//***************************ES REMOVE_TOKEN
				
				else if(appState.getComandoPayu().equals("REMOVE_TOKEN")){
					Log.i(TAG, module+"*ES REMOVE_TOKEN*");
					Intent respuestaRemover = new Intent();
					if(posiciones[3].equals("SUCCESS")){
						Log.i(TAG, module+"TARJETA BORRADA");
						appState.setNumTarjetas(appState.getNumTarjetas()-1);	//Actualizar el # de Tarjetas
						//respuestaRemover.putExtra("CMD", C.TOKENS_MAPA_OK);
						respuestaRemover.putExtra("CMD", C.BORRADO_OK);
					}
					else if(posiciones[3].equals("ERROR")){	//Se genero un error al Borrar Token...
						//respuestaRemover.putExtra("CMD", C.TOKENS_MAPA_ERROR);
						respuestaRemover.putExtra("CMD", C.BORRADO_ERROR);
					}
					//respuestaRemover.setAction(C.MAP);
					respuestaRemover.setAction(C.LISTAR_TARJETAS);
					context.sendBroadcast(respuestaRemover);
				}
				
				//***************************ES REGISTAR_USUARIO
				
				else if(appState.getComandoPayu().equals("REGISTAR_USUARIO")){	//Registrar Usuario en la base Nva
					Log.i(TAG, module+"*LLEGO RESPUESTA DE REGISTRO DE USUARIO*");
					Intent usuario = new Intent();
					usuario.putExtra("CMD", C.RESPUESTA_REGISTRO);
					usuario.putExtra("DATA",data);	//Pasa info a la Actividad
					usuario.setAction(C.ENTRADA);
					context.sendBroadcast(usuario);
				}
				
				//***************************ES REGISTAR_MIEMBRO_CLAVE
				
				else if(appState.getComandoPayu().equals("REGISTAR_MIEMBRO_CLAVE")){
					Log.i(TAG, module+"*LLEGO RESPUESTA DE REGISTRAR MIEMBRO CLAVE*");
					Intent respuestaPayu = new Intent();
					respuestaPayu.putExtra("CMD", C.RESPUESTA_REGISTRO_MIEMBRO);
					respuestaPayu.putExtra("DATA",data);
					respuestaPayu.setAction(C.REGISTRAR_TARJETA);
					context.sendBroadcast(respuestaPayu);
				}
				
				//***************************ES ACCESAR_USUARIO_ENTRADA
				
				else if(appState.getComandoPayu().equals("ACCESAR_USUARIO_ENTRADA")){
					Log.i(TAG, module+"*LLEGO RESPUESTA DE ACCESAR USUARIO*");
					Intent respuestaPayu = new Intent();
					respuestaPayu.putExtra("CMD", C.RESPUESTA_ACCESAR_USUARIO);
					respuestaPayu.putExtra("DATA",data);
					respuestaPayu.setAction("Entrada");
					context.sendBroadcast(respuestaPayu);
				}
				
				//***************************ES INSERTAR_TARJETA
				
				else if(appState.getComandoPayu().equals("INSERTAR_TARJETA")){
					Log.i(TAG, module+"*LLEGO RESPUESTA DE INSERTAR_TARJETA*");
					Intent respuestaPayu = new Intent();
					if(data.contains("0|")){
						respuestaPayu.putExtra("CMD", C.RESPUESTA_OK);
						appState.setNumTarjetas(appState.getNumTarjetas()+1);	//Guardar el # de Tarjetas
					}else if(data.contains("1|")){
						respuestaPayu.putExtra("CMD", C.RESPUESTA_MAL);
						respuestaPayu.putExtra("DATA",data);	//Para Mostrar el error...
					}else{
						respuestaPayu.putExtra("CMD", C.RESPUESTA_DESCONOCIDA);
					}
					
					respuestaPayu.setAction(C.REGISTRAR_TARJETA);
					context.sendBroadcast(respuestaPayu);
				}
				
				//***************************ES ELIMINAR_TARJETA
				
				else if(appState.getComandoPayu().equals("ELIMINAR_TARJETA")){
					Log.i(TAG, module+"*ELIMINAR_TARJETA*");
					Intent respuestaRemover = new Intent();
					if(data.contains("0|")){	//Se Borro Correctamente
						Log.i(TAG, module+"TARJETA BORRADA");
						appState.setNumTarjetas(appState.getNumTarjetas()-1);	//Actualizar el # de Tarjetas
						respuestaRemover.putExtra("CMD", C.BORRADO_OK);
					}
					else if(data.contains("1|")){	//Se genero un error al Borrar Tarjeta
						respuestaRemover.putExtra("CMD", C.BORRADO_ERROR);
					}
					//respuestaRemover.setAction(C.MAP);
					respuestaRemover.setAction(C.LISTAR_TARJETAS);
					context.sendBroadcast(respuestaRemover);
				}
				
				//***************************ES ACCESAR_USUARIO_REGISTRO
				
				else if(appState.getComandoPayu().equals("ACCESAR_USUARIO_REGISTRO")){
					Log.i(TAG, module+"*ACCESAR_USUARIO_REGISTRO*");
					Intent respuestaPayu = new Intent();
					respuestaPayu.putExtra("CMD", C.RESPUESTA_VERIFICACION_MIEMBRO);
					respuestaPayu.putExtra("DATA",data);	//Para Mostrar el error...
					respuestaPayu.setAction(C.REGISTRAR_TARJETA);
					context.sendBroadcast(respuestaPayu);
				}
				
				//***************************ES REALIZAR_PAGO
				
				else if(appState.getComandoPayu().equals("REALIZAR_PAGO")){
					Log.i(TAG, module+"*REALIZAR_PAGO*");
					Intent respuestaPago = new Intent();
					if(data.contains("0|")){	//Se Realizo el Pago Correctamente
						Log.i(TAG, module+"PAGO REALIZADO CORRECTAMENTE");
						respuestaPago.putExtra("CMD", C.PAGO_OK);
					}
					else if(data.contains("1|")){	//Se genero un error al Pagar
						respuestaPago.putExtra("CMD", C.PAGO_MAL);
						respuestaPago.putExtra("DATA",data);	//Para Filtrar el error...
						//Se debe colocar filtro de errores...
//						1|Negada,CVV2 invalido
//						1|Contraseña Inválida!
//						1|Error de timeout con plataforma
//						1|Contrase?a Inv?lida
//						1|null
//						1|Negada, transacci??n no permitida a esta tarjeta
//						1|Negada, track 2 errado
//						1|Negada, estado de la tarjeta inv??lido
//						1|Error, transacci??n no autorizada debido a reglas pre-autorizaci??n
//						1|Usuario y/o clave incorrectos
//						-----------------------------------------------------------------------------------------
//						1|El valor de la ??rden es inferior al m??nimo permitido. Valor m??nimo permitido 39 MXN
//						1|RESTRICTED_CARD
//						1|La longitud del c??digo de seguridad de la tarjeta de cr??dito debe ser de 4 d??gitos
//						1|PAYMENT_NETWORK_REJECTED
//						1|PENDING_TRANSACTION_REVIEW
//						1|INVALID_CARD
//						1|A JSONObject must begin with '{' at character 1 of 
						
					}
					respuestaPago.setAction(C.PAGO_NO_EFECTIVO);
					context.sendBroadcast(respuestaPago);
				}
				
				//***************************ES ACTUALIZAR_USUARIO
				else if(appState.getComandoPayu().equals("ACTUALIZAR_USUARIO")){
					Log.i(TAG, module+"*ACTUALIZAR_USUARIO*");
					Intent respuestaActualizar = new Intent();
					if(data.contains("0|")){	//Se Realizo Actualizacion Correctamente
						Log.i(TAG, module+"ACTUALIZADO CORRECTAMENTE");
						respuestaActualizar.putExtra("CMD", C.ACTUALIZACION_OK);
					}
					
					else if(data.contains("1|Nada que actualizar")){	//No hay nada que actualizar
						respuestaActualizar.putExtra("CMD", C.ACTUALIZACION_NADA);
					}
					
					else if(data.contains("1|")){	//Error al Actualizar
						respuestaActualizar.putExtra("CMD", C.ACTUALIZACION_MAL);
					}
					respuestaActualizar.setAction("AActualizarDatos");
					context.sendBroadcast(respuestaActualizar);
				}
				
				//***********************ES LISTAR_PROMO
				else if(appState.getComandoPayu().equals("LISTAR_PROMO")){
					if(data.contains("0|")){	//Sacar Nombre promo para validar Info...
						Log.i(TAG, module+"*LISTAR_PROMO*");
						
						String buscarComillas[]=data.split("proNombre");
						String buscarNombre[]=buscarComillas[1].split("\"");
						Log.i(TAG, module+"El nombre es: "+buscarNombre[2]);
						appState.setNombrePromo(buscarNombre[2]);
						String consulta_promo = "validarPromo|"+appState.getCorreoUsuario()+"|"+buscarNombre[2];
						appState.setComandoPayu("CONSULTA_PROMO");
						Intent service = new Intent();
						service.putExtra("CMD", C.ENCRIPTADO);
						service.putExtra("DATA", consulta_promo);
						service.setAction(C.COM);
						getApplicationContext().sendBroadcast(service);
					}
				}
				
				//***********************ES CONSULTA_PROMO
				else if(appState.getComandoPayu().equals("CONSULTA_PROMO")){
					Log.i(TAG, module+"*CONSULTA_PROMO*");
					Intent respuestaPromo = new Intent();
					if(data.contains("0|")&&data.contains("|true")){	//Hay que mostrar la Promo...
						respuestaPromo.putExtra("CMD", C.VER_PROMO);
						respuestaPromo.putExtra("DATA",data);	//Para Mostrar la Promo...
					}else{
						
					}
					respuestaPromo.setAction(C.MAP);
					context.sendBroadcast(respuestaPromo);
				}
				//***************************ES OTRO COMANDO....
				
				else{
					Log.i(TAG, module+"ES OTRO COMANDO... HAY QUE VALIDARLO");
				}

				
			}
		}
		return "";
		
	}
	
	//****************************************************************************************
	public void CloseDialogMap(){
		/*******Se cierra el ProcessDialog***************/
		Intent closedialog = new Intent();
		closedialog.putExtra("CMD", C.CANCEL_PROCESSDIALOG);
		closedialog.setAction(C.MAP);
		context.sendBroadcast(closedialog);

	}
	//****************************************************************************************
	public void LeerError(String error){
		Intent senderror = new Intent();
		senderror.putExtra("CMD", C.HABLAR);
		senderror.putExtra("TEXTHABLA", error);	
		senderror.setAction(C.TEXT_TO_SPEECH);
		context.sendBroadcast(senderror);
	}
	//****************************************************************************************
	public void SendToast(String mensaje){
		Intent toast = new Intent();
		toast.setAction(C.MAP);
		toast.putExtra("CMD", C.TOAST);
		toast.putExtra("TOAST",mensaje );
		context.sendBroadcast(toast);
	}
	//****************************************************************************************
	public void ConsultarMovil(){
		if(!appState.getEstadoUnidad().equals(EstadosUnidad.CANCELADO)){
			Log.i(TAG, module+ "+++ESTADO AL ENTRAR A ConsultarMovil: "+appState.getEstadoUnidad()+" +++");
			contador++;
						
			/*******Se Coloca invisible los botones***************/
			appState.setFlaginvisible(2);
			Intent invisible = new Intent();
			invisible.putExtra("CMD", C.INVISIBLEBOTON);
			invisible.setAction(C.MAP);
			context.sendBroadcast(invisible);
			
			
			appState.setFlagsolicitud(1);
			/*******Se cierra el ProcessDialog***************/
			Intent closedialog = new Intent();
			closedialog.putExtra("CMD", C.CANCEL_PROCESSDIALOG);
			closedialog.setAction(C.MAP);
			context.sendBroadcast(closedialog);
						
			appState.setContador(0);

			if(appState.getFlagcancelado()==1){
				
			}else{
				
				//appState.setEstadoUnidad(EstadosUnidad.TAXI_ASIGNADO);
				appState.setFlagcancelado(0);
			}
			
			//if(contador<2){																								
			if(appState.getEstadoUnidad().equals(EstadosUnidad.SOLICITUD_TAXI)){
				Log.i(TAG, module+ "eN bREVE.....");
				Log.i(TAG, module+ "***ESTADO ACTUAL: "+appState.getEstadoUnidad()+" ***");
				
				Intent k = new Intent();
				k.putExtra("CMD", C.HABLAR);
				k.putExtra("TEXTHABLA", "ENBREVE ELTAXI DEPLACAS "  + appState.getPlaca() + "loatendera");	
				k.setAction(C.TEXT_TO_SPEECH);
				context.sendBroadcast(k);
				
				appState.setEstadoUnidad(EstadosUnidad.TAXI_ASIGNADO);
				Log.i(TAG, module+ "***ESTADO CAMBIO A: "+appState.getEstadoUnidad()+" ***");
										
				/*******Se Coloca Aviso de  Taxi***************/
				Intent taxi = new Intent();
				taxi.putExtra("CMD", C.AVISO_TAXI);
				taxi.setAction(C.MAP);
				context.sendBroadcast(taxi);
				
//				/*******Se Mira las cordenadas del Taxi***************/
//				Intent taxiasig = new Intent();
//				taxiasig.putExtra("CMD", C.TAXIASIGNADO);
//				taxiasig.setAction(C.MAP);
//				context.sendBroadcast(taxiasig);
			
			}else if(appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){ //Por si se Cruzo una cancelacion(D) con una C|1
				Log.i(TAG, module+ "***VOLVER VISIBLE LA BARRA DE SOLICITUD***"+appState.getEstadoUnidad()+" ***");
				appState.setFlaginvisible(1);
				invisible.putExtra("CMD", C.INVISIBLEBOTON);
				invisible.setAction(C.MAP);
				context.sendBroadcast(invisible);
			}
			
		}
	}
	//****************************************************************************************
	public void SinMovil(){
		if(appState.getEstadoUnidad().equals(EstadosUnidad.SOLICITUD_TAXI)){
			appState.setFlagrecordardireccion(1);
			appState.setFlagsolicitud(1);
			/*******Se cierra el ProcessDialog***************/
			Intent closedialog = new Intent();
			closedialog.putExtra("CMD", C.CANCEL_PROCESSDIALOG);
			closedialog.setAction(C.MAP);
			context.sendBroadcast(closedialog);
			appState.setEstadoUnidad(EstadosUnidad.LIBRE);
							
			/*******Se Coloca invisible los botones***************/
			appState.setFlaginvisible(1);
			Intent invisible = new Intent();
			invisible.putExtra("CMD", C.INVISIBLEBOTON);
			invisible.setAction(C.MAP);
			context.sendBroadcast(invisible);
			
			Intent intento_taxi = new Intent();
			intento_taxi.putExtra("CMD", C.VOLVER_A_PEDIR_TAXI);
			intento_taxi.setAction(C.MAP);
			context.sendBroadcast(intento_taxi);

			Intent k = new Intent();
			k.putExtra("CMD", C.HABLAR);
			k.putExtra("TEXTHABLA", "LOSENTIMOS NOHAY TAXI DISPONIBLE ENEL MOMENTO PORFAVOR INTENTE DENUEVO ");	
			k.setAction(C.TEXT_TO_SPEECH);
			context.sendBroadcast(k);
			
			appState.setIdServicio("0");
			
			SendToast("LO SENTIMOS NO HAY TAXI DISPONIBLE EN EL MOMENTO!!!" +"\n POR FAVOR INTENTE DE NUEVO");
		
		}
		
		
		else{

			if(!appState.getEstadoUnidad().equals(EstadosUnidad.LIBRE)){
				Intent preguntaTaxi = new Intent();
				preguntaTaxi.putExtra("CMD", C.VERIFICAR_TAXI);
				preguntaTaxi.setAction(C.MAP);
				context.sendBroadcast(preguntaTaxi);
			}
	        
		}
	}
	
	/**************************METODOS CON OVERRIDE****************************/
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	//****************************************************************************************
	
	@Override
	public void onCreate()
	{
	    Log.i(TAG, "onCreate");
	    
	    context = getApplicationContext();
		appState = (TaxisLi)context;
	    
		filter = new IntentFilter();
		filter.addAction(module);
		try{
			registerReceiver(receiver, filter);
		}catch (Exception e){
			Log.i(TAG, module +"EL ERROR OCURRE AL registerReceiver :"+ e);
			e.printStackTrace();
		}		
		
		
		if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.FROYO) {
		     Log.i(TAG, module + "VVVVVVVVVVVVVVVVVVVVVVVVV ES MENOSR A 2.2");
		}else{
			 Log.i(TAG, module + "VVVVVVVVVVVVVVVVVVVVVVVVV ES MAYOR A 2.2");
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);   
		}
		

	}
	
	//****************************************************************************************
	
	@Override
	public void onDestroy(){
	    Log.i(TAG, "onDestroy");
	    super.onDestroy();
	    unregisterReceiver(receiver);
	    isconect=false;
	   
	} 
	
	
}
