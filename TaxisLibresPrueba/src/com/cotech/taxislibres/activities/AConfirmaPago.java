package com.cotech.taxislibres.activities;

import com.cotech.taxislibres.EstadosUnidad;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.rta.main.JsonAuth;
import com.rta.structure.payments.confirmMemberPayment.ConfirmMemberPaymentOutput;
import com.rta.structure.payments.memberPayment.MemberPaymentInputData;
import com.rta.structure.payments.memberPayment.MemberPaymentOutput;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
//import android.util.Log;
import com.cotech.taxislibres.activities.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AConfirmaPago extends Activity {
//	
//	protected String APPNAME = "TaxisLibres";
//	protected String module = "ConfirmaPago";
//	private TaxisLi appState;
//	private Context context;
//	
//	
//	private EditText contrasena;
//	private TextView placataxi;
//	private TextView nombretaxista;
//	private TextView valorpagar;
//	private TextView claveservicio;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.confirmapago);
//		context = getApplicationContext();
//		appState = ((TaxisLi) context);
//		try{
//			
//			contrasena = (EditText) findViewById(R.id.contrasenapago);
//			placataxi = (TextView) findViewById(R.id.placataxi);
//			nombretaxista = (TextView) findViewById(R.id.nombretaxista);
//			valorpagar = (TextView) findViewById(R.id.valorpagar);
//			claveservicio = (TextView) findViewById(R.id.claveservicio);
//		
//		
//		
//			Log.i(APPNAME, module + appState.getIdModiPayConductor() + "         " +appState.getValorPagar() );
//			
//			placataxi.setText(appState.getPlaca());
//			nombretaxista.setText(appState.getNombreTaxista());
//			valorpagar.setText(appState.getValorPagar());
//			//claveservicio.setText(appState.getNumberPhone().charAt(8)+appState.getNumberPhone().charAt(9));
//			claveservicio.setText("80");
//			
//			appState.setEstadoUnidad(EstadosUnidad.LIBRE);
//		}catch(Exception e){
//			appState.setEstadoUnidad(EstadosUnidad.LIBRE);
//			e.printStackTrace();
//		}
//		
//	}
//	
//	
//	public void ConfirmaPago(View v){
//		
//		Log.i(APPNAME, module + appState.getIdModiPayConductor() + "         " +appState.getValorPagar() );
//		appState.setEstadoUnidad(EstadosUnidad.LIBRE);
//		 MemberPaymentInputData memberPaymentInputData = new MemberPaymentInputData();
//		 //OJO verificar info que llega.... Datos del id del Conductor
//		 //memberPaymentInputData.setToMemberPrincipal(appState.getIdModiPayConductor());
//		 memberPaymentInputData.setToMemberPrincipal("abarbosa@digipay.com.co");
//		 memberPaymentInputData.setAmount(appState.getValorPagar());
//		 //Verificar Metodo de Pago: MEXICO
//		 //53 Efectivo Conductor
//		 //61 Vales Conductor
//		 //68 eWallet
//		 memberPaymentInputData.setTransferTypeId(61);
//		 memberPaymentInputData.setDescription("Cualquier dato que se dese poner de descripción");
//
//		 String userCode="tecnologia@taxislosunos.com";
//		 String userPasswd="h0lb31N604a";
//
//		 try{
//			 //ConfirmMemberPaymentOutput payment = JsonAuth.confirmMemberPayment("3935", "1234", memberPaymentInputData);
//			 //JsonAuth.setURL("http://200.91.204.38:9010/Modipay/rest");	//Para enviar info a....
//			 //MemberPaymentOutput memberPaymentOutput = JsonAuth.memberPayment("256637", "1234", memberPaymentInputData);
//			 //ConfirmMemberPaymentOutput payment = JsonAuth.confirmMemberPayment("256637", "1234", memberPaymentInputData);
//			 //Log.i(APPNAME, module + " --------------------------- Todo bn " + payment);
//			 
//			 
//			 final MemberPaymentOutput memberPaymentOutputData = JsonAuth.memberPayment(userCode, userPasswd, memberPaymentInputData);
//				
//				Log.i("PAGOOOOOOOO", "----------------" + memberPaymentInputData +"---------------------------------");
//				ConfirmMemberPaymentOutput payment = JsonAuth.confirmMemberPayment(userCode, userPasswd, memberPaymentInputData);
//				Log.i(getClass().getSimpleName(), "lo del RESRT: " + payment.getId());
//				if (payment.getId() != 0) {
//
//					AlertDialog.Builder codierror = new AlertDialog.Builder(AConfirmaPago.this);
//					//codierror.setIcon(R.drawable.ic_launcher);
//					codierror.setTitle("RESPUESTA DE PAGO");
//					// set the message to display
//					codierror.setMessage("EL PAGO SE REALIZO CON EXITO!!!");
//
//					// set a negative/no button and create a listener
//					codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
//
//						// do something when the button is clicked
//						public void onClick(DialogInterface arg0, int arg1) {
//							finish();
//
//
//						}
//					});
//
//					codierror.show();
////				} else if (payment.getErrorCode().equals("NOT_ENOUGH_FUNDS")) {
////					
////				} else if (payment.getErrorCode().equals("INVALID_CREDENTIALS")) {
////					
////				} else if (payment.getErrorCode().equals("NOT_FOUND")) {
//					
//				} else {
//
//					AlertDialog.Builder codierror = new AlertDialog.Builder(AConfirmaPago.this);
//					//codierror.setIcon(R.drawable.ic_launcher);
//					codierror.setTitle("RESPUESTA DE PAGO");
//					// set the message to display
//					codierror.setMessage("NO FUE POSIBLE REALIZAR SU PAGO");
//
//					// set a negative/no button and create a listener
//					codierror.setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
//
//						// do something when the button is clicked
//						public void onClick(DialogInterface arg0, int arg1) {
//							finish();
//
//
//						}
//					});
//
//					codierror.show();
//					
//				}
//
//			 
//		 }catch(Exception e){
//			 Log.i(APPNAME, module + " ESTE ES EL ERROR: "+ e);
//		 }
//	}
//
}
