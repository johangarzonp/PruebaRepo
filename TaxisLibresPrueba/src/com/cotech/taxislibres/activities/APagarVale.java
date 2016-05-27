package com.cotech.taxislibres.activities;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;







//import android.util.Log;
import com.cotech.taxislibres.activities.Log;
import com.cotech.taxislibres.database.Handler_sqlite;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.EstadosUnidad;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.rta.main.JsonAuth;
import com.rta.structure.payments.confirmMemberPayment.ConfirmMemberPaymentOutput;
import com.rta.structure.payments.memberPayment.MemberPaymentInputData;
import com.rta.structure.payments.memberPayment.MemberPaymentOutput;

public class APagarVale extends Activity {
	

	protected String APPNAME = "TaxisLibres";
	protected String module = C.PAGO_NO_EFECTIVO;
	private TaxisLi appState;
	private Context context;
	
	
	private EditText claveModipay;
	private TextView placaTaxi;
	private TextView nombreTaxista;
	private TextView valorPagar;
	private TextView claveServicio;
	private boolean yaPago=false;
	
	private TextView titulo;//titulotextopagos
	private TextView tituloPedirCvv;//avisocontrasenacvv
	private ProgressDialog progDailog7 = null;
	private IntentFilter filter7;
	
	public final BroadcastReceiver receiver7 = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Integer cmd = intent.getIntExtra("CMD", 0);
			String infoRta = intent.getStringExtra("DATA");
			switch(cmd){
			
				case C.ERROR_SOCKET:
					progDailog7.dismiss();
					Toast.makeText(getApplicationContext(), "ERROR EN LA COMUNICACION, INTENTE NUEVAMENTE", 5000).show();
				break;
				
				case C.PAGO_OK:
					progDailog7.dismiss();
					String guardarDb="";
					guardarDb ="\n"+ "DIRECCION: "+ appState.getDireccionUsuario() + "\n"+"PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: TARJETA DE CREDITO";
					GuardarInfoDataBase(guardarDb,"\n"+"SERVICIO CUMPLIDO");
					Toast.makeText(getApplicationContext(), "PAGO EXITOSO", 5000).show();
					PagoExitoso();
//					new Handler().postDelayed(new Runnable(){
//						public void run(){
//
//							try {
//								finish();
//								
//							} catch (Exception e) {
//								e.printStackTrace();
//							}	
//
//						};
//					}, 2000);
//					
//					String s = "SeReconfirma";

					PasarEncuesta();
					
				break;
				
				case C.PAGO_MAL:
					progDailog7.dismiss();
					
					appState.setIntentosdepago(appState.getIntentosdepago()+1);	//Sumamos 1 intento

					if(appState.getIntentosdepago()>2){
						
						guardarDb = "\n"+"DIRECCION: "+ appState.getDireccionUsuario() + "\n"+"PLACA: "+ appState.getPlaca()+"\n"+"CONDUCTOR: " + appState.getNombreTaxista()+"\n"+"PAGO: EFECTIVO";
						GuardarInfoDataBase(guardarDb,"\n"+"SERVICIO CUMPLIDO");
						
						AlertDialog alertDialog = new AlertDialog.Builder(APagarVale.this).create();
						alertDialog.setTitle("PAGO FALLIDO!!!");
						alertDialog.setMessage("POR FAVOR REALICE EL PAGO EN EFECTIVO");
						alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
						    new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) {
						        	PagoFallido();
//						        	new Handler().postDelayed(new Runnable(){
//										public void run(){
//
//											try {
//												finish();
//												
//											} catch (Exception e) {
//												e.printStackTrace();
//											}	
//
//										};
//									}, 2000);
						        	PasarEncuesta();
						        }
						    });
						alertDialog.show();
						
						
						
						
//						PagoFallido();
//						finish();
					}
					else{
						claveModipay.setText("");
						
						String[] Error = appState.getRtaError().split("\\|");
						
						claveModipay.setError(Error[1]);
						
//						Toast.makeText(getApplicationContext(), "ERROR AL REALIZAR EL PAGO, INTENTE NUEVAMENTE", 5000).show();
//						Intent intentar = new Intent();
//						intentar.setClass(getApplicationContext(), APagarVale.class);		        		    								
//						startActivity(intentar);
//						finish();
					}

				break;
				
				case C.RESPUESTA_OLVIDO_CLAVE:
					progDailog7.dismiss();
					try {
						String[] Separa = infoRta.split("\\|");				
						AlertDialog alertDialog = new AlertDialog.Builder(APagarVale.this).create();
						alertDialog.setTitle("RESPUESTA CLAVE OLVIDADA!!!");
						alertDialog.setMessage(Separa[2]);
						alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
						    new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) {
						        	
						        }
						    });
						alertDialog.show();
					} catch (Exception e) {
						
					}
					
					break;
			}
			
		}
	
	};
	//************************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.pagar_vale);
		setContentView(R.layout.pagarconclave);
		context = getApplicationContext();
		appState = ((TaxisLi) context);
		try{
			claveModipay = (EditText) findViewById(R.id.contrasenapago);
			placaTaxi = (TextView) findViewById(R.id.placataxi);
			nombreTaxista = (TextView) findViewById(R.id.nombretaxista);
			valorPagar = (TextView) findViewById(R.id.valorpagar);
			claveServicio = (TextView) findViewById(R.id.claveservicio);
			
			if(appState.getFormaPago()== 4){
				titulo=(TextView) findViewById(R.id.titulotextopagos);
				titulo.setText(" PAGO CON TARJETA DE CREDITO ");
				tituloPedirCvv=(TextView) findViewById(R.id.avisocontrasenacvv);
				if(appState.getCiudadPais().contains("COLOMBIA")){
					tituloPedirCvv.setText(" Ingrese clave para pagos electrónicos ");
					claveModipay.setHint("Clave para pagos");
				}
				else{ 
					tituloPedirCvv.setText(" Ingrese el CVV de su tarjeta ");
					claveModipay.setHint("CVV");
				}
				//tituloPedirCvv.setText(" Ingrese clave para pagos electrónicos ");
				//claveModipay.setHint("Clave para pagos");
			}
		
			Log.i(APPNAME, module + appState.getIdModiPayConductor() + "         " +appState.getValorPagar() );
			
			String MuestraPago = null;
			if(appState.getValorPagar().contains(",")){
				String[] separavalor = appState.getValorPagar().split("\\,");
				appState.setValorPagar(separavalor[0]);
				if(separavalor.length==2){
					MuestraPago = "$" + separavalor[0] + "\n Incluye: \n" + separavalor[1];
				}else if(separavalor.length==3){
					MuestraPago = "$" + separavalor[0] + "\n Incluye: \n" + separavalor[1] + "\n" + separavalor[2];
				}else if(separavalor.length>=4){
					MuestraPago = "$" + separavalor[0] + "\n Incluye: \n" + separavalor[1] + "\n" + separavalor[2] + "\n" + separavalor[3] ;
				}
				
			}else{
				MuestraPago=appState.getValorPagar();
			}
			
			placaTaxi.setText(appState.getPlaca());
			nombreTaxista.setText(appState.getNombreTaxista());
			//valorPagar.setText(appState.getValorPagar());
			valorPagar.setText(MuestraPago);
			//claveservicio.setText(appState.getNumberPhone().charAt(8)+appState.getNumberPhone().charAt(9));
			String numPhone=appState.getNumberPhone();
			String hr = numPhone.substring(numPhone.length()-2,numPhone.length());
			//claveServicio.setText("80");
			claveServicio.setText(hr);
			
		}catch(Exception e){
			appState.setEstadoUnidad(EstadosUnidad.LIBRE);
			e.printStackTrace();
		}
		
		appState.setEstadoUnidad(EstadosUnidad.PAGANDO_VALE);
		
		filter7 = new IntentFilter();
		filter7.addAction(module);
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
			//AvisoHablado("REGISTRO NOFUE ALMACENADO SEBORRO SUHISTORICO DESERVICIOS");
			Log.i(APPNAME, module + "=================== BORRANDO MENSAJES =================");
		}

		helper.cerrar();
	}
	//************************************************************************
	
	public void PasarEncuesta(){
		Intent i = getIntent();
		i.putExtra("pasarEncuesta", "Nada");
		setResult(RESULT_OK, i);
		finish();
	}
	//************************************************************************
	
	public void RecordarClave(View v){
		String recordar ="olvidoPasswd|" + appState.getCorreoUsuario();
		Intent forgot = new Intent();
		forgot.putExtra("CMD", C.ENCRIPTADO);
		forgot.putExtra("DATA", recordar);
		forgot.setAction(C.COM);
		getApplicationContext().sendBroadcast(forgot);
		
   		 
		progDailog7 = new ProgressDialog(APagarVale.this);
		progDailog7.setTitle("ENVIANDO INFORMACION DE USUARIO");
		progDailog7.setMessage("ESPERE UN MOMENTO... ");
		progDailog7.setCancelable(true);
		progDailog7.show();
	
	}
	//************************************************************************
	public void ConfirmaPago(View v){
		if(appState.getFormaPago()== 4){	//Fue Tarjeta de Credito
			//Se debe realizar el respectivo pago...
			Log.i(APPNAME, module +"Longitud de lo ingresado"+claveModipay.getText().toString().length());
			String infoTarjeta=appState.getTarjetaPago();//tokenId,nombre,identNumber,franquicia de la tarjeta,numero de la tarjeta
			String[] Data = infoTarjeta.split("\\|");
			//String prueba_pago = "{\"language\":\"es\",\"command\":\"SUBMIT_TRANSACTION\",\"merchant\":{\"apiLogin\":\"APILOGIN\",\"apiKey\":\"APIKEY\"},\"transaction\":{\"order\":{\"accountId\":\"ACCOUNTID\",\"referenceCode\":\"pruebage12\",\"description\":\"123|Test order Colombia\",\"language\":\"es\",\"signature\":\"SIGNATURE\",\"shippingAddress\":{\"country\":\"CO\",\"phone\":\"5517517105\"},\"buyer\":{\"fullName\":\"full name\",\"emailAddress\":\"marith20@gmail.com\",\"dniNumber\":\"32144457\",\"shippingAddress\":{\"country\":\"MX\",\"phone\":\"5517517105\"}},\"additionalValues\":{\"TX_VALUE\":{\"value\":\"39\",\"currency\":\"USD\"}}},\"creditCardTokenId\":\"aca09507-7485-45e0-8fb4-f7d3d7a905c2\",\"creditCard\":{\"securityCode\":\"123\"},\"type\":\"AUTHORIZATION_AND_CAPTURE\",\"paymentMethod\":\"VISA\",\"paymentCountry\":\"CO\",\"payer\":{\"fullName\":\"full name\",\"emailAddress\":\"marith20@gmail.com\"},\"extraParameters\":{\"INSTALLMENTS_NUMBER\":1},\"ipAddress\":\"201.217.202.179\"},\"test\":true}";
//			String prueba_pago = "{\"language\":\"es\",\"command\":\"SUBMIT_TRANSACTION\",\"merchant\":{\"apiLogin\":\"APILOGIN\",\"apiKey\":\"APIKEY\"},\"transaction\":{\"order\":{\"accountId\":\"ACCOUNTID\",\"referenceCode\":\"pruebage12\",\"description\":\""
//			+appState.getPinMovil() +"|Servicio de Taxi\",\"language\":\"es\",\"signature\":\"SIGNATURE\",\"shippingAddress\":{\"country\":\"CO\",\"phone\":\"5517517105\"},\"buyer\":{\"fullName\":\"full name\",\"emailAddress\":\"marith20@gmail.com\",\"dniNumber\":\""+Data[2]+"\",\"shippingAddress\":{\"country\":\"MX\",\"phone\":\"5517517105\"}},\"additionalValues\":{\"TX_VALUE\":{\"value\":\"39\",\"currency\":\"USD\"}}},\"creditCardTokenId\":\""
//			+ Data[0]+"\",\"creditCard\":{\"securityCode\":\""+claveModipay.getText().toString()+"\"},\"type\":\"AUTHORIZATION_AND_CAPTURE\",\"paymentMethod\":\""+Data[3]+"\",\"paymentCountry\":\"CO\",\"payer\":{\"fullName\":\"full name\",\"emailAddress\":\"marith20@gmail.com\"},\"extraParameters\":{\"INSTALLMENTS_NUMBER\":1},\"ipAddress\":\"201.217.202.179\"},\"test\":true}";
			
						
			
//			String prueba_pago = "{\"language\":\"es\",\"command\":\"SUBMIT_TRANSACTION\",\"merchant\":{\"apiLogin\":\"APILOGIN\",\"apiKey\":\"APIKEY\"},\"transaction\":{\"order\":{\"accountId\":\"ACCOUNTID\",\"referenceCode\":\""+appState.getIdModiPayConductor()+"\",\"description\":\""
//			+appState.getIdModiPayConductor() +"|Servicio de Taxis Libres\",\"language\":\"es\",\"signature\":\"SIGNATURE\",\"shippingAddress\":{\"country\":\"MX\",\"phone\":\"5517517105\"},\"buyer\":{\"fullName\":\"MARITZA HERNANDEZ H\",\"emailAddress\":\"marith20@gmail.com\",\"dniNumber\":\""+appState.getNumberPhone()+"\",\"shippingAddress\":{\"street1\":\"Holbein 37 San Juan Benito Juarez Mexico DF\",\"country\":\"MX\",\"phone\":\"5517517105\",\"city\":\"Mexico D.F.\"}},\"additionalValues\":{\"TX_VALUE\":{\"value\":\"39\",\"currency\":\"MXN\"}}},\"creditCardTokenId\":\""
//			+ Data[0]+"\",\"creditCard\":{\"securityCode\":\""+claveModipay.getText().toString()+"\"},\"type\":\"AUTHORIZATION_AND_CAPTURE\",\"paymentMethod\":\""+Data[3]+"\",\"paymentCountry\":\"MX\",\"payer\":{\"fullName\":\"MARITZA HERNANDEZ H\",\"emailAddress\":\"marith20@gmail.com\",\"contactPhone\":\"5517517105\",\"billingAddress\":{\"street1\":\"Holbein 37 San Juan Benito Juarez Mexico DF\",\"country\":\"MX\",\"phone\":\"5517517105\",\"city\":\"Mexico D.F.\"}},\"extraParameters\":{\"INSTALLMENTS_NUMBER\":1},\"ipAddress\":\"201.217.202.179\",\"cookie\":\"cookie 1\",\"userAgent\": \"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0\"},\"test\":false}";
			
			String prueba_pago="";
			
			if(appState.getCiudadPais().contains("COLOMBIA")){
				prueba_pago ="realizarPagoTarjetaCredito|CO|"+appState.getCorreoUsuario()+"|{\"referenceCode\":\"\",\"description\":\"Test order Colombia\",\"phone\":\""+appState.getNumberPhone()+
				"\",\"fullName\":\""+appState.getNombreUsuario()+"\",\"emailAddress\":\""+appState.getCorreoUsuario()+"\",\"dniNumber\":\""+appState.getNumberPhone()+"\",\"street1\":\""+appState.getDireccionUsuario()+
				"\",\"city\":\"Bogotá\",\"value\":\""+appState.getValorPagar()+"\",\"currency\":\"COP\",\"creditCardTokenId\":\""+ Data[0]+"\",\"securityCode\":\"\",\"paymentMethod\":\""+Data[3]+
				"\",\"paymentCountry\":\"CO\",\"ipAddress\":\"IP_DEL_MOVIL\",\"payerIdTaxista\":\""+appState.getIdModiPayConductor()+"\",\"installmentsNumber\":\"1\"}|"+ claveModipay.getText().toString();
			}else{
				prueba_pago ="realizarPagoTarjetaCredito|MX|"+appState.getCorreoUsuario()+"|{\"referenceCode\":\"MX_PRU_001\",\"description\":\"Test order Mexico\",\"phone\":\""+appState.getNumberPhone()+
				"\",\"fullName\":\""+appState.getNombreUsuario()+"\",\"emailAddress\":\""+appState.getCorreoUsuario()+"\",\"dniNumber\":\""+appState.getNumberPhone()+"\",\"street1\":\""+appState.getDireccionUsuario()+
				"\",\"city\":\"Mexico D.F.\",\"value\":\""+appState.getValorPagar()+"\",\"currency\":\"MXN\",\"creditCardTokenId\":\""+ Data[0]+"\",\"securityCode\":\""+claveModipay.getText().toString()+"\",\"paymentMethod\":\""+Data[3]+
				"\",\"paymentCountry\":\"MX\",\"ipAddress\":\"IP_DEL_MOVIL\",\"payerIdTaxista\":\""+appState.getIdModiPayConductor()+"\",\"installmentsNumber\":\"1\"}|"+"NA";
			}
			
			//appState.setComandoPayu("SUBMIT_TRANSACTION");
			appState.setComandoPayu("REALIZAR_PAGO");

			Intent service = new Intent();
			service.putExtra("CMD", C.ENCRIPTADO);
			service.putExtra("DATA", prueba_pago);
			service.setAction(C.COM);
			getApplicationContext().sendBroadcast(service);
			
			progDailog7 = new ProgressDialog(APagarVale.this);
	   		progDailog7.setTitle("REALIZANDO SU PAGO");
	   		progDailog7.setMessage("ESPERE UN MOMENTO PORFAVOR... ");
	   		progDailog7.setCancelable(false);
	   		progDailog7.show();
			
			//Si el Pago fue Exitoso...
			//PagoExitoso();
			//finish();
		}
		else{
			Log.i(APPNAME, module + appState.getIdModiPayConductor() + "         " +appState.getValorPagar() );
			appState.setEstadoUnidad(EstadosUnidad.LIBRE);
			MemberPaymentInputData memberPaymentInputData = new MemberPaymentInputData();
			//OJO verificar info que llega.... Datos del id del Conductor
			memberPaymentInputData.setToMemberPrincipal(appState.getIdModiPayConductor());
			//memberPaymentInputData.setToMemberPrincipal("abarbosa@digipay.com.co");
			memberPaymentInputData.setAmount(appState.getValorPagar());
			//*****************************************************|
			//Verificar Metodo de Pago TransferTypeId				|
			//*****************************************************|
			//       MEXICO         |	COLOMBIA 	| Pruebas		|
			//*****************************************************|
			//53 Efectivo Conductor |53 Efectivo	|53 Efectivo	|
			//61 Vales Conductor    |61 Vales		|42 Vales		|
			//68 eWallet            |N.A			|63 eWallet		|
			//*****************************************************
			if((appState.getCiudadPais().contains("MEXICO"))||(appState.getCiudadPais().contains("Mexico"))){
				memberPaymentInputData.setTransferTypeId(61);
			}else{
				//memberPaymentInputData.setTransferTypeId(42);	//Pruebas
				memberPaymentInputData.setTransferTypeId(61);	//Produccion
			}
			//*****************************************************
			memberPaymentInputData.setDescription("Cualquier dato que se dese poner de descripción");

			//String userCode="tecnologia@taxislosunos.com";
			String userCode=appState.getUserModipay();
			//String userPasswd="h0lb31N604a";
			String userPasswd= claveModipay.getText().toString();

			try{
				//ConfirmMemberPaymentOutput payment = JsonAuth.confirmMemberPayment("3935", "1234", memberPaymentInputData);
				//JsonAuth.setURL("http://200.91.204.38:9010/Modipay/rest");	//Para enviar info a....
				//MemberPaymentOutput memberPaymentOutput = JsonAuth.memberPayment("256637", "1234", memberPaymentInputData);
				//ConfirmMemberPaymentOutput payment = JsonAuth.confirmMemberPayment("256637", "1234", memberPaymentInputData);
				//Log.i(APPNAME, module + " --------------------------- Todo bn " + payment);


				final MemberPaymentOutput memberPaymentOutputData = JsonAuth.memberPayment(userCode, userPasswd, memberPaymentInputData);

				Log.i("PAGOOOOOOOO", "----------------" + memberPaymentInputData +"---------------------------------");
				ConfirmMemberPaymentOutput payment = JsonAuth.confirmMemberPayment(userCode, userPasswd, memberPaymentInputData);
				Log.i(getClass().getSimpleName(), "lo del RESRT: " + payment.getId());
				if (payment.getId() != 0) {

					AlertDialog.Builder codierror = new AlertDialog.Builder(APagarVale.this);
					//codierror.setIcon(R.drawable.ic_launcher);
					codierror.setTitle("RESPUESTA DE PAGO");
					// set the message to display
					codierror.setMessage("EL PAGO SE REALIZO CON EXITO!!!");

					// set a negative/no button and create a listener
					codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {

						// do something when the button is clicked
						public void onClick(DialogInterface arg0, int arg1) {
							
							PagoExitoso();
							finish();

						}
					});

					codierror.show();
					//				} else if (payment.getErrorCode().equals("NOT_ENOUGH_FUNDS")) {
					//					
					//				} else if (payment.getErrorCode().equals("INVALID_CREDENTIALS")) {
					//					
					//				} else if (payment.getErrorCode().equals("NOT_FOUND")) {

				} else {

					AlertDialog.Builder codierror = new AlertDialog.Builder(APagarVale.this);
					//codierror.setIcon(R.drawable.ic_launcher);
					codierror.setTitle("RESPUESTA DE PAGO");
					// set the message to display
					codierror.setMessage("NO FUE POSIBLE REALIZAR SU PAGO CON VALE ELECTRONICO");

					// set a negative/no button and create a listener
					codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {

						// do something when the button is clicked
						public void onClick(DialogInterface arg0, int arg1) {
							appState.setIntentosdepago(appState.getIntentosdepago()+1);	//Sumamos 1 intento

							if(appState.getIntentosdepago()>2){
								
								AlertDialog alertDialog = new AlertDialog.Builder(APagarVale.this).create();
								alertDialog.setTitle("PAGO FALLIDO!!!");
								alertDialog.setMessage("POR FAVOR REALICE EL PAGO EN EFECTIVO");
								alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
								    new DialogInterface.OnClickListener() {
								        public void onClick(DialogInterface dialog, int which) {
								        	PagoFallido();
								        	finish();
								        }
								    });
								alertDialog.show();
								//PagoFallido();
								//finish();
							}
							else{
								Toast.makeText(getApplicationContext(), "Intente Nuevamente ingresando su clave", Toast.LENGTH_LONG).show();
								Intent intentar = new Intent();
								intentar.setClass(getApplicationContext(), APagarVale.class);		        		    								
								startActivity(intentar);
								finish();
							}


						}
					});

					codierror.show();
				}

			}catch(Exception e){
				Log.i(APPNAME, module + " ESTE ES EL ERROR: "+ e);
			}
		}
	}
	
	//************************************************************************
	//El Pago fue realizado Exitosamente...
	public void PagoExitoso(){
		//Enviar Funcion "L"
		//appState.setEstadoUnidad(EstadosUnidad.ENCUESTA);
		String confirmapago = "L|" + appState.getNumberPhone() + "|"+ appState.getPinMovil() + "|" + appState.getNombreUsuario()+"|" +
		"Pago realizado correctamente por un valor de "+appState.getValorPagar()+" pesos." +"|" +appState.getIdServicio()+"|" +"1"+"\n";
		Intent pagarcarrera = new Intent();
		pagarcarrera.putExtra("CMD", C.SEND);
		pagarcarrera.putExtra("DATA", confirmapago);
		pagarcarrera.setAction(C.COM);
		getApplicationContext().sendBroadcast(pagarcarrera);
		
		Intent pasarformulario = new Intent();
		pasarformulario.putExtra("CMD", C.PASA_ENCUESTA);
		pasarformulario.setAction(C.MAP);
		context.sendBroadcast(pasarformulario);

		//							if((appState.getCiudadPais().contains("COLOMBIA"))||(appState.getCiudadPais().contains("Colombia"))){
		//								appState.setEstadoUnidad(EstadosUnidad.LIBRE);
		//								Intent cerrarapp = new Intent();
		//								cerrarapp.putExtra("CMD", C.CERRARAPP);
		//								cerrarapp.setAction(C.MAP);
		//								context.sendBroadcast(cerrarapp);
		//							}else{	//Es MEXICO
//		Intent pasarformulario = new Intent();
//		pasarformulario.putExtra("CMD", C.PASA_ENCUESTA);
//		pasarformulario.setAction(C.MAP);
//		context.sendBroadcast(pasarformulario);
		//}
	}
	
	//************************************************************************
	//El Pago fallo...
	public void PagoFallido(){
		//Enviar Funcion "L"
		//appState.setEstadoUnidad(EstadosUnidad.ENCUESTA);
		appState.setFormaPago(0);//Debe quedar pagado como efectivo....
		Toast.makeText(getApplicationContext(), "POR FAVOR REALICE EL PAGO EN EFECTIVO", Toast.LENGTH_LONG).show();
		String confirmapago = "L|" + appState.getNumberPhone() + "|"+ appState.getPinMovil() + "|" + appState.getNombreUsuario()+"|" +"Su Pago no pudo realizarse correctamente por un valor de "+appState.getValorPagar()+" pesos, "
				+ "el usuario debe cancelar este valor en EFECTIVO" + "|" +appState.getIdServicio()+"|" +"0"+"\n";
		Intent pagarcarrera = new Intent();
		pagarcarrera.putExtra("CMD", C.SEND);
		pagarcarrera.putExtra("DATA", confirmapago);
		pagarcarrera.setAction(C.COM);
		getApplicationContext().sendBroadcast(pagarcarrera);
		
		Intent pasarformulario = new Intent();
		pasarformulario.putExtra("CMD", C.PASA_ENCUESTA);
		pasarformulario.setAction(C.MAP);
		context.sendBroadcast(pasarformulario);

		
//		AlertDialog alertDialog = new AlertDialog.Builder(APagarVale.this).create();
//		alertDialog.setTitle("PAGO FALLIDO!!!");
//		alertDialog.setMessage("POR FAVOR REALICE EL PAGO EN EFECTIVO");
//		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//		    new DialogInterface.OnClickListener() {
//		        public void onClick(DialogInterface dialog, int which) {
//		            
//		        	dialog.dismiss();
//		        	Intent pasarformulario = new Intent();
//		    		pasarformulario.putExtra("CMD", C.PASA_ENCUESTA);
//		    		pasarformulario.setAction(C.MAP);
//		    		context.sendBroadcast(pasarformulario);
//		        	finish();
//		        }
//		    });
//		alertDialog.show();
		
		
		
		

		//								if((appState.getCiudadPais().contains("COLOMBIA"))||(appState.getCiudadPais().contains("Colombia"))){
		//									appState.setEstadoUnidad(EstadosUnidad.LIBRE);
		//									Intent cerrarapp = new Intent();
		//									cerrarapp.putExtra("CMD", C.CERRARAPP);
		//									cerrarapp.setAction(C.MAP);
		//									context.sendBroadcast(cerrarapp);
		//								}else{	//Es MEXICO
//		Intent pasarformulario = new Intent();
//		pasarformulario.putExtra("CMD", C.PASA_ENCUESTA);
//		pasarformulario.setAction(C.MAP);
//		context.sendBroadcast(pasarformulario);
		//}
	}
	
	//************************************************************************
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 Log.i(APPNAME, module + "onKeyDown");
			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("PAGO PENDIENTE");
				builder.setMessage("Usted tiene un pago Pendiente");
				builder.setPositiveButton("ACEPTAR", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i("Dialogos", "JAIME USUARIO PRESIONO ACEPTAR");
						dialog.cancel();
					}
				});
				builder.show();
			 return super.onKeyDown(keyCode, event);
	 }
	 
	//************************************************************************

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onDestroy");
		super.onDestroy();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onPause");
		super.onPause();
		//unregisterReceiver(receiver7);
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onResume");
		super.onResume();
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onStart");
		super.onStart();
		try {
			registerReceiver(receiver7, filter7);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "onStop");
		super.onStop();
	}
}
