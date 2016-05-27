package com.cotech.taxislibres.activities;

import java.util.Calendar;
import java.util.List;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;



import com.rta.main.JsonAuth;
import com.rta.main.ModipayServicesWSDL;
import com.rta.structure.access.initialData.CustomValue;
import com.rta.structure.access.initialData.InitialDataOutput;
import com.rta.structure.info.InfoResponse;
import com.rta.structure.modipay.FieldValueTL;
import com.rta.structure.modipay.MemberTL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.util.Log;
import com.cotech.taxislibres.activities.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AFormPagos extends Activity{
//	
//	protected String APPNAME = "TaxisLibres";
//	protected String module = "FormPagoVale";
//	private TaxisLi appState;
//	private Context context;
//	private View RegisterVale;
//	//private View RegisterTarjeta;
//	//private Button fecha;
//	
//	//private EditText nombretarjeta;
//	//private EditText numerotarjeta;
//	//private EditText codigotarjeta;
//	private EditText usercodevale;
//	private EditText passwordvale;
//	
//	//DatePickerDialog mDatePicker;
//	//private int day;
//	//private int month;
//	//private int year;
//	
//
//	//private ProgressBar pensando;
//	 Handler Handler = new Handler();
//	 private ProgressDialog dialogEspera = null;
//	 private boolean cancelaRegistro=false;
//	 private boolean fueBoton=false;
//	 
//	 private String userCode="";
//	 private String userPasswd="";
//	 
//	 private double Saldo;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.registrar_vale);
//		context = getApplicationContext();
//		appState = ((TaxisLi) context);
//		
//		RegisterVale = findViewById(R.id.layoutRegisterVale);
//		//RegisterTarjeta = findViewById(R.id.layoutRegisterTarjeta);
//		
//		//nombretarjeta= (EditText) findViewById(R.id.nombretarjeta);
//		//numerotarjeta= (EditText) findViewById(R.id.numerotarjeta);
//		//codigotarjeta= (EditText) findViewById(R.id.codigoseguridad);
//		
//		usercodevale= (EditText) findViewById(R.id.usercodevale);
//		passwordvale= (EditText) findViewById(R.id.passwordvale);
//		
//		appState.setVale(20);	//VALOR X DEFECTO
//		
//		//pensando= (ProgressBar) findViewById(R.id.progress);
//		
//		//fecha=(Button) findViewById(R.id.fechaVencimieto);
//		
//		
//		
//		
////		if(appState.getFlagRegistro().equals("vale")){
////			RegisterVale.setVisibility(View.VISIBLE);
////			RegisterTarjeta.setVisibility(View.GONE);
////		}else if(appState.getFlagRegistro().equals("tarjeta")){
////			RegisterVale.setVisibility(View.GONE);
////			RegisterTarjeta.setVisibility(View.VISIBLE);
////		}
////		
////		/****************************************************************************/		
////		/******************BOTON DE LA FECHA DE VENCIMIENTO *************************/
////		/****************************************************************************/	
////		fecha.setOnClickListener(new View.OnClickListener() {		
////			@SuppressWarnings("deprecation")
////			@Override
////			public void onClick(View v) {
////				showDialog(0);
////			}
////		});
////		/****************************************************************************/	
//		
//		//pensando.setVisibility(View.INVISIBLE);
//		
//		if(!(appState.getUserModipay().equals("Defecto"))){	//Existe un usuario de Modipay Correcto...
//			RegistroModipay();
//		}
//	}
//	
////	
////	@Override
////	 @Deprecated
////	 protected Dialog onCreateDialog(int id) {
////	  return new DatePickerDialog(this, datePickerListener, year, month, day);
////	 }
////
////	 private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
////	  public void onDateSet(DatePicker view, int selectedYear,
////	    int selectedMonth, int selectedDay) {
////	   fecha.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
////	     + selectedYear);
////	  }
////	 };
////	
////	
////	public void GuardarTC(View v){
////		String datosTC = nombretarjeta.getText().toString() + "|" + numerotarjeta.getText().toString() + "|" + codigotarjeta.getText().toString();
////		appState.setDatosTarjeta(datosTC);
////	}
//	
//	public void RegistroVale(View v){
//		fueBoton=true;
//		RegistroModipay();
//	}
//	
//	public void RegistroModipay(){
//		//ACA HACER LA COMICACION CON DIGIPAY
//		//pensando.setVisibility(View.VISIBLE);
//		dialogEspera= new ProgressDialog(AFormPagos.this);
//		 
//		 dialogEspera.setTitle("REALIZANDO REGISTRO");
//		 dialogEspera.setMessage("ENVIANDO INFO PARA VALIDACION");
//		 dialogEspera.setCancelable(false);
//		 dialogEspera.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar Registro", new DialogInterface.OnClickListener() {
//	    	    @Override
//	    	    public void onClick(DialogInterface dialog, int which) {
//	    	    	dialog.dismiss();
//	    	    	cancelaRegistro=true;
//	    	    }
//	    	});
//		 dialogEspera.show();
//
//		Handler.postDelayed(new Runnable(){	
//            public void run(){
//
//				try{
//					
//					if(fueBoton){
//						userCode= usercodevale.getText().toString();
//						userPasswd= passwordvale.getText().toString();
//					}else{
//						userCode= appState.getUserModipay();
//						userPasswd=appState.getPwdModipay();	
//					}
//					//Object Resultado = JsonAuth.initialData("91716", "1234");
//					//if(JsonAuth.initialData("3935", "1234").toString() != null){
//					
//					String URL="";
//					//OJO VALIDAR PAIS
//					if((appState.getCiudadPais().contains("MEXICO"))||(appState.getCiudadPais().contains("Mexico"))){
//						URL="http://200.91.204.38:9020/Modipay/rest";	//Mexico Desarrollo
//					}
//					else{
//						//URL="http://200.91.204.38:9010/Modipay/rest"; //Bogotá Desarrollo
//						URL="http://200.91.204.38:8080/Modipay/rest"; //Bogotá Produccion
//					
//					}
//					
//					JsonAuth.setURL(URL);	//Para enviar info a....
//					
////					if(JsonAuth.initialData(codigovale, passworvale).toString() != null){
////						appState.setVale(1);  //ESTO ES PARA SABER QUE YA REGISTRO EL VALE
////						Log.i(APPNAME, module + "La Respuesta de la Validacien es: OK" );
////						
////						MuestraDialog();
////					}else{
////						appState.setVale(0);  //ESTO ES PARA SABER QUE YA REGISTRO EL VALE
////						Log.i(APPNAME, module + "La Respuesta de la Validacien es: null" );
////						//pensando.setVisibility(View.INVISIBLE);
////						MuestraDialog();
////						
////					}
//					
//					 
//					
//					if (JsonAuth.initialData(userCode, userPasswd).getErrorCode().equals("INVALID_CREDENTIALS")) {
//						
//						appState.setVale(0);  //ESTO ES PARA SABER QUE FUERON CREDENCIALES INVALIDAS
//
//					}else if (JsonAuth.initialData(userCode, userPasswd).equals("Invalid username / password")) {
//											
//						appState.setVale(1);  //ESTO ES PARA SABER QUE FUE USUARIO O CLAVE INCORRECTOS
//
//					}else  if (JsonAuth.initialData(userCode, userPasswd).getErrorCode().equals("BLOCKED_CREDENTIALS")) {
//					
//						appState.setVale(2);  //ESTO ES PARA SABER QUE FUE CREDENCIALES BLOQUEADAS
//						
//					}else  if (JsonAuth.initialData(userCode, userPasswd).getErrorCode().equals("CHANNEL_DISABLED")) {
//						appState.setVale(5);  //ESTO ES PARA SABER QUE FUE CHANNEL_DISABLED
//						
//					}else if (JsonAuth.initialData(userCode, userPasswd).getErrorCode().toString() == "") {	//Esta Registrado!!!
//						//Sacar el Valor si es Taximetro o PreLiquidado(Pre-establecido)
//						InitialDataOutput initialDataOutput = JsonAuth.initialData(userCode, userPasswd);
//						boolean user_tarifa=false;
//						for(CustomValue customValue :initialDataOutput.getProfile().getCustomValues()) {
//				            if(customValue.getInternalName().equals("Convenio_Empresarial")) {
//				            	//if(customValue.getValue().equals("Pre-establecido"))	appState.setTarificacionVale("Pre-establecido");
//				            	//else	appState.setTarificacionVale("Taximetro");
//				            	appState.setTarificacionVale(customValue.getValue());
//				            	Log.i("DigiPay","Value = " + appState.getTarificacionVale());
//				            	user_tarifa=true;
//				            }
//				        }
//						if(!user_tarifa){
//				            	//Se deja x defecto Taximetro
//				            	appState.setTarificacionVale("Taximetro");
//				            	
//				            	Intent talk = new Intent();
//								talk.setAction(C.TEXT_TO_SPEECH);
//								talk.putExtra("CMD", C.HABLAR);
//								talk.putExtra("TEXTHABLA","TARIFA PORDEFECTO CONTAXIMETRO REVISE SUPERFIL ENMODIPAY");
//								sendBroadcast(talk);
//				            
//						}
//							
//						//SE DEBE REALIZAR LA CONSULTA DEL SALDO PARA AUTORIZAR EL SERVICIO CON VALE!!!
//						
//						Log.i("DIGIPAY", "------------------------CONSULTANDO SALDO---------------------------------  ");
//						
//						List<InfoResponse> cuentasUsuarioLst = JsonAuth.info(userCode, userPasswd);;
//						
//						Log.i("DIGIPAY", "---------------------------------------------------------  " +  cuentasUsuarioLst.size());
//						
//						for(InfoResponse infoResponse : cuentasUsuarioLst){
//							Log.i("DigiPay", "infoResponse.getAccount.getType.getName= " + infoResponse.getAccount().getType().getName());
//						}
//							
//						Saldo = JsonAuth.consultarSaldoCuenta(userCode, userPasswd, "Vales");
//						
//						Log.i("DIGIPAY", "-----------------------EL SALDO----------------------------------  " +  Saldo);
//						double saldoPais;
//						if((appState.getCiudadPais().contains("MEXICO"))||(appState.getCiudadPais().contains("Mexico"))){
//							saldoPais=100;
//						}else{
//							saldoPais=10000;
//						}
//						
//						//Establecer El Valor minimo de Saldo para solicitar el Taxi
//						if(saldoPais < Saldo){	//Saldo Suficiente...
//							appState.setVale(3);  //ESTO ES PARA SABER QUE FUE EXITOSO EL REGISTRO
//							//Validar Tarificacion
////							MemberTL result= ModipayServicesWSDL.buscarPorUsername(userCode);
////							for(FieldValueTL campos: result.getFields()){
////								if(campos.getInternalName().equals("Convenio_Empresarial")){
////									String tarifa = campos.getValue();
////									Log.i("DigiPay", "TARIFA: " + tarifa + "\n");
////								}
////							}
//						}else{	//SALDO INSUFICIENTE...
//							appState.setFormaPago(0);	//Dejar x defecto efectivo porque el saldo es insuficiente!!!
//							appState.setVale(4);	
//						}
//							
//					}else{	//Error no Definido...
//						appState.setVale(6);
//					}
//					
//					if(cancelaRegistro){
//						
//						AlertDialog.Builder codierror = new AlertDialog.Builder(AFormPagos.this);
//						//codierror.setIcon(R.drawable.ic_launcher);
//						codierror.setTitle("CANCELACION DE REGISTRO!!!");
//				        // set the message to display
//						codierror.setMessage("REGISTRO CANCELADO");
//				
//				        // set a negative/no button and create a listener
//						codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
//				
//				            // do something when the button is clicked
//				            public void onClick(DialogInterface arg0, int arg1) {
//				            	                                
//				        		         		
//				            }
//				        });
//						
//						codierror.show();
//						appState.setVale(20);	//VALOR X DEFECTO
//						
//					}else{
//						dialogEspera.dismiss();
//						//Para todos los casos debe mostrar Dialog...
//						MuestraDialog();
//					}
//					
//				}catch (Exception e){
//					appState.setVale(6); 
//					Log.i(APPNAME, module + "La Respuesta de la Validacien es: null" );
//					//pensando.setVisibility(View.INVISIBLE);
//					MuestraDialog();
//					e.printStackTrace();
//				}
//            };
//        }, 5000);		
//
//	}
//	
//	public void MuestraDialog(){
//		
////		handler.post(new Runnable() {
////            public void run() {			
//		
//		
//			try{
//				if(appState.getVale()==0){
//					
//					AlertDialog.Builder codierror = new AlertDialog.Builder(AFormPagos.this);
//					//codierror.setIcon(R.drawable.ic_launcher);
//					codierror.setTitle("RESPUESTA DE REGISTRO!!!");
//			        // set the message to display
//					codierror.setMessage("CREDENCIALES INVALIDAS");
//			
//			        // set a negative/no button and create a listener
//					codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
//			
//			            // do something when the button is clicked
//			            public void onClick(DialogInterface arg0, int arg1) {
//			            	                                
//			        		         		
//			            }
//			        });
//					
//					codierror.show();
//					appState.setVale(20);	//VALOR X DEFECTO
//					
//				}else if(appState.getVale()==1){
//					
//					AlertDialog.Builder codierror = new AlertDialog.Builder(AFormPagos.this);
//					//codierror.setIcon(R.drawable.ic_launcher);
//					codierror.setTitle("RESPUESTA DE REGISTRO!!!");
//			        // set the message to display
//					codierror.setMessage("POR FAVOR VERIFIQUE SU CONTRASEÑA!!!");
//			
//			        // set a negative/no button and create a listener
//					codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
//			
//			            // do something when the button is clicked
//			            public void onClick(DialogInterface arg0, int arg1) {
//			            	                                
//			        		         		
//			            }
//			        });
//					
//					codierror.show();
//					appState.setVale(20);	//VALOR X DEFECTO
//					
//				}else if(appState.getVale()==2){
//					
//					AlertDialog.Builder codierror = new AlertDialog.Builder(AFormPagos.this);
//					//codierror.setIcon(R.drawable.ic_launcher);
//					codierror.setTitle("RESPUESTA DE REGISTRO!!!");
//			        // set the message to display
//					codierror.setMessage("CREDENCIALES BLOQUEADAS");
//			
//			        // set a negative/no button and create a listener
//					codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
//			
//			            // do something when the button is clicked
//			            public void onClick(DialogInterface arg0, int arg1) {
//			            	                                
//			        		         		
//			            }
//			        });
//					
//					codierror.show();
//					appState.setVale(20);	//VALOR X DEFECTO
//					
//				}else if(appState.getVale()==3){
//					
//					AlertDialog.Builder codierror = new AlertDialog.Builder(AFormPagos.this);
//					codierror.setIcon(R.drawable.msg);
//					
//					if(appState.getTarificacionVale().equals("Pre-establecido")){
//						Intent talk = new Intent();
//						talk.setAction(C.TEXT_TO_SPEECH);
//						talk.putExtra("CMD", C.HABLAR);
//						talk.putExtra("TEXTHABLA","ESOBLIGATORIO INGRESARSU DESTINO PARASOLICITAR SERVICIO CONVALE ELECTRONICO");
//						sendBroadcast(talk);
//					}
//					
//					if(!fueBoton){
////						Intent talk = new Intent();
////						talk.setAction(C.TEXT_TO_SPEECH);
////						talk.putExtra("CMD", C.HABLAR);
////						talk.putExtra("TEXTHABLA","ESOBLIGATORIO INGRESARSU DESTINO PARASOLICITAR SERVICIO CONVALE ELECTRONICO");
////						sendBroadcast(talk);
//						codierror.setTitle("RESPUESTA DE REGISTRO!!!");
//						// set the message to display
//						codierror.setMessage("REGISTRADO CORRECTAMENTE SU SALDO ES DE: "+Saldo+"Pesos");
//
//						// set a negative/no button and create a listener
//						codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
//
//							// do something when the button is clicked
//							public void onClick(DialogInterface arg0, int arg1) {
//								finish();                               
//
//							}
//						});
//					}else{
//						codierror.setTitle("RESPUESTA DE REGISTRO CORRECTO");
//						// set the message to display
//						codierror.setMessage("DESEA ALMACENAR SUS CREDENCIALES?");
//
//						// set a negative/no button and create a listener
//						codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
//
//							// do something when the button is clicked
//							public void onClick(DialogInterface arg0, int arg1) {
//								appState.setUserModipay(userCode);
//								appState.setPwdModipay(userPasswd);
//								finish();                               
//
//							}
//						});
//						
//						codierror.setPositiveButton("CANCELAR", new DialogInterface.OnClickListener() {
//
//							// do something when the button is clicked
//							public void onClick(DialogInterface arg0, int arg1) {
//								appState.setUserModipay(userCode);
//								finish();                               
//
//							}
//						});
//					}
//					codierror.show();
//					
//				}else if(appState.getVale()==4){
//					
//					AlertDialog.Builder codierror = new AlertDialog.Builder(AFormPagos.this);
//					codierror.setIcon(R.drawable.msg);
//					codierror.setTitle("CONSULTA DE SALDO");
//			        // set the message to display
//					codierror.setMessage("SALDO INSUFICIENTE SU SALDO ES:"+Saldo+"Pesos");
//		
//			        // set a negative/no button and create a listener
//					codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
//		
//			            // do something when the button is clicked
//			            public void onClick(DialogInterface arg0, int arg1) {
//			            	  finish();                               
//			        		         		
//			            }
//			        });
//					
//					codierror.show();
//					appState.setVale(20);	//VALOR X DEFECTO
//					
//				}else{
//					
//					AlertDialog.Builder codierror = new AlertDialog.Builder(AFormPagos.this);
//					//codierror.setIcon(R.drawable.ic_launcher);
//					codierror.setTitle("RESPUESTA DE REGISTRO!!!");
//			        // set the message to display
//					codierror.setMessage("ERROR INESPERADO");
//			
//			        // set a negative/no button and create a listener
//					codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
//			
//			            // do something when the button is clicked
//			            public void onClick(DialogInterface arg0, int arg1) {
//			            	                                
//			        		         		
//			            }
//			        });
//					
//					codierror.show();
//					appState.setVale(20);	//VALOR X DEFECTO
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		
////            }
////        });
//	}
//	
//	public void Cancelar(View v){
//		
//		finish();
//	}
//
////	@Override
////	public void onBackPressed() {
////		// TODO Auto-generated method stub
////		Log.i(APPNAME, module + " Entra por onBackPressed");
////		super.onBackPressed();
////	}
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		Log.i(APPNAME, module + " Entra por onDestroy");
//		super.onDestroy();
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		Log.i(APPNAME, module + " Entra por onPause");
//		super.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		Log.i(APPNAME, module + " Entra por onResume");
//		super.onResume();
//	}
//
//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		Log.i(APPNAME, module + " Entra por onStart");
//		super.onStart();
//	}
//
//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		Log.i(APPNAME, module + " Entra por onStop");
//		Log.i(APPNAME, module + " VALOR DE appState.setVale ES : "+appState.getVale());
//		super.onStop();
//	}
//	
//	
//
}
