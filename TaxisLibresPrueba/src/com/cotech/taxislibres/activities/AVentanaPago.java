package com.cotech.taxislibres.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;

public class AVentanaPago extends Activity {
	protected String APPNAME = "TaxisLibres";
	protected String module = C.INFORMARPAGO;

	private TaxisLi appState;
	private Context context;

	private TextView colocarTipoPago;
	private TextView colocarCosto;
	private TextView valorCarrera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ventanapagonormal);

		context = getApplicationContext();
		appState = ((TaxisLi) context);

		String formaPago="Efectivo";
		if((appState.getFormaPago()==1))	formaPago="Vale Digital";
		else if((appState.getFormaPago()==3))	formaPago="Vale Fisico";
		else if(appState.getFormaPago()==4)	formaPago="Tarjeta de Crédito";

		colocarTipoPago=(TextView) findViewById(R.id.textotipopago);
		colocarTipoPago.setText(formaPago);

		colocarCosto= (TextView) findViewById(R.id.textocostoservicio);

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

		if(appState.getValorPagar().equals("0")){
			colocarCosto.setVisibility(View.INVISIBLE);
			valorCarrera.setVisibility(View.INVISIBLE);
		}else{
			valorCarrera=(TextView) findViewById(R.id.valorservicio);
			if((appState.getFormaPago()==4)&&(!appState.isPagoTarjetaExitoso())){
				valorCarrera.setText(MuestraPago+"\n\n PORFAVOR CANCELE EL \n VALOR DEL SERVICIO \n EN EFECTIVO");
			}else{
				valorCarrera.setText(MuestraPago);
			}
			valorCarrera.setGravity(Gravity.CENTER);
		}

	}

	//****************************************************************************************
	public void AceptarPago(View v){
		String s = "Acepataciondel Pago";

		Intent i = getIntent();
		i.putExtra("aceptarPago", s);
		setResult(RESULT_OK, i);
		finish();

	}

	//****************************************************************************************
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(APPNAME, module + "onKeyDown");
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:

			break;	

		}
		return true;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


}
