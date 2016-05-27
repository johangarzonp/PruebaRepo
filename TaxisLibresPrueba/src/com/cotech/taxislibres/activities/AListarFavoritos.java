package com.cotech.taxislibres.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;
import com.cotech.taxislibres.TaxisLi;
import com.cotech.taxislibres.TaxisLi.TrackerName;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AListarFavoritos extends Activity {

	protected String APPNAME = "TaxisLibres";
	protected String module = C.LISTAR_FAVORITOS;

	private TaxisLi appState;
	private Context context;
	private IntentFilter filter7;

	private int seleccionDireccion=0;
	private String listaFavoritos;
	private int copiaNumFrec;

	private LinearLayout informacionDireccion1;
	private LinearLayout informacionDireccion2;
	private LinearLayout informacionDireccion3;
	private LinearLayout informacionDireccion4;
	private LinearLayout informacionDireccion5;

	private TextView infoDir1;
	private TextView infoDir2;
	private TextView infoDir3;
	private TextView infoDir4;
	private TextView infoDir5;

	private RadioButton selectorDireccion1;
	private RadioButton selectorDireccion2;
	private RadioButton selectorDireccion3;
	private RadioButton selectorDireccion4;
	private RadioButton selectorDireccion5;
	
	private String direccionesFrecuentes[];
	
	private ProgressDialog ventanaEspera;

	//************************************************************************
	public final BroadcastReceiver receiver7 = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {									
			Integer cmd = intent.getIntExtra("CMD", 0);
			switch(cmd){
			case C.FAVORITO_BORRADO_OK:
				ventanaEspera.dismiss();
				appState.setListaFavoritos("NoHayFavoritos");
				Toast.makeText(context, "DIRECCION BORRADA CORRECTAMENTE", 5000).show();
				SalirFavoritos();
				break;
				
			case C.FAVORITO_BORRADO_ERROR:
				ventanaEspera.dismiss();
				Toast.makeText(context, "ERROR AL BORRAR DIRECCION", 5000).show();
				break;
			
			case C.ERROR_SOCKET:
				ventanaEspera.dismiss();
				Toast.makeText(context, "ERROR EN LA COMUNICACION, INTENTE NUEVAMENTE", 5000).show();
			break;
			
			case C.ERROR_CARGANDO_FAVORITOS:
				ventanaEspera.dismiss();
				Toast.makeText(context, "ERROR AL CARGAR SUS DIRECCIONES FAVORITAS", 5000).show();
				break;
			
			}
		}
	};

	//************************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listar_favoritos);

		context = getApplicationContext();
		appState = ((TaxisLi) context);

		informacionDireccion1=(LinearLayout) findViewById(R.id.layoutdireccion1);
		informacionDireccion1.setVisibility(View.INVISIBLE);
		selectorDireccion1= (RadioButton) findViewById(R.id.radioButtonDireccion1);

		informacionDireccion2=(LinearLayout) findViewById(R.id.layoutdireccion2);
		informacionDireccion2.setVisibility(View.INVISIBLE);
		selectorDireccion2= (RadioButton) findViewById(R.id.radioButtonDireccion2);

		informacionDireccion3=(LinearLayout) findViewById(R.id.layoutdireccion3);
		informacionDireccion3.setVisibility(View.INVISIBLE);
		selectorDireccion3= (RadioButton) findViewById(R.id.radioButtonDireccion3);

		informacionDireccion4=(LinearLayout) findViewById(R.id.layoutdireccion4);
		informacionDireccion4.setVisibility(View.INVISIBLE);
		selectorDireccion4= (RadioButton) findViewById(R.id.radioButtonDireccion4);

		informacionDireccion5=(LinearLayout) findViewById(R.id.layoutdireccion5);
		informacionDireccion5.setVisibility(View.INVISIBLE);
		selectorDireccion5= (RadioButton) findViewById(R.id.radioButtonDireccion5);

		listaFavoritos=appState.getListaFavoritos();
		Log.i(APPNAME, module + " ESTA ES LA INFO DE FAVORITOS: "+ listaFavoritos);

		direccionesFrecuentes=listaFavoritos.split("\\^");
		Log.i(APPNAME, module + "Longitud de Frecuentes: "+ direccionesFrecuentes.length);
		copiaNumFrec = direccionesFrecuentes.length;
		int cont=0;

		for(cont=0;cont< copiaNumFrec;cont++){
			Log.i(APPNAME, module + "ENTRO AL FOR");
			switch(cont){

			case(0):
				informacionDireccion1.setVisibility(View.VISIBLE);
				
				infoDir1=(TextView) findViewById(R.id.textodireccion1);
				infoDir1.setMaxWidth(infoDir1.getWidth());
				infoDir1.setText(TraerSoloDireccion(direccionesFrecuentes[0]));
				
			break;

			case(1):
				informacionDireccion2.setVisibility(View.VISIBLE);
				
				infoDir2=(TextView) findViewById(R.id.textodireccion2);
				infoDir2.setMaxWidth(infoDir2.getWidth());
				infoDir2.setText(TraerSoloDireccion(direccionesFrecuentes[1]));
				
			break;

			case(2):
				informacionDireccion3.setVisibility(View.VISIBLE);
				
				infoDir3=(TextView) findViewById(R.id.textodireccion3);
				infoDir3.setMaxWidth(infoDir3.getWidth());
				infoDir3.setText(TraerSoloDireccion(direccionesFrecuentes[2]));
				
			break;

			case(3):
				informacionDireccion4.setVisibility(View.VISIBLE);
				
				infoDir4=(TextView) findViewById(R.id.textodireccion4);
				infoDir4.setMaxWidth(infoDir4.getWidth());
				infoDir4.setText(TraerSoloDireccion(direccionesFrecuentes[3]));
				
			break;

			case(4):
				informacionDireccion5.setVisibility(View.VISIBLE);
				
				infoDir5=(TextView) findViewById(R.id.textodireccion5);
				infoDir5.setMaxWidth(infoDir5.getWidth());
				infoDir5.setText(TraerSoloDireccion(direccionesFrecuentes[4]));
				
			break;
			}
		}


		appState.setActividad(module);

		filter7 = new IntentFilter();
		filter7.addAction(module);
	}

	//************************************************************************

	public String TraerSoloDireccion(String info){

		String[] Data = info.split("\\|");

		//Log.i(APPNAME, module + Data[4]);
		//return Data[4];

		//		Log.i(APPNAME, module + Data[Data.length -6]);
		//		return Data[Data.length -6];

		Log.i(APPNAME, module + Data[Data.length -5]);	//Sacar la direccion que coloca el usuario en el Box
		if(Data[Data.length -5].contains("~")){	//Trae Adicionales...
			Log.i(APPNAME, module + "Entro por el contains ~");
			String[] partes = Data[Data.length -5].split("~");
			return partes[0];	//Retorna Unicamente la dirección
		}
		else	return Data[Data.length -5];
	}

	//************************************************************************
	
	public void SalirFavoritos(){
		String s = "SalidaFavoritos";
		Intent i = getIntent();
		i.putExtra("nohacernada", s);
		setResult(RESULT_OK, i);
		finish();
	}
	//************************************************************************
	public void EscogioDireccion(View view){

		boolean checked = ((RadioButton) view).isChecked();

		switch(view.getId()) {
		case R.id.radioButtonDireccion1:
			seleccionDireccion=1;
			appState.setDireccionFrecuente(direccionesFrecuentes[0]);
			selectorDireccion1.setBackgroundResource(R.drawable.fondoselectortrue);
			selectorDireccion2.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion3.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion4.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion5.setBackgroundResource(R.drawable.fondoselectorfalse);
			break;

		case R.id.radioButtonDireccion2:
			seleccionDireccion=2;
			appState.setDireccionFrecuente(direccionesFrecuentes[1]);
			selectorDireccion1.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion2.setBackgroundResource(R.drawable.fondoselectortrue);
			selectorDireccion3.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion4.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion5.setBackgroundResource(R.drawable.fondoselectorfalse);
			break;

		case R.id.radioButtonDireccion3:
			seleccionDireccion=3;
			appState.setDireccionFrecuente(direccionesFrecuentes[2]);
			selectorDireccion1.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion2.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion3.setBackgroundResource(R.drawable.fondoselectortrue);
			selectorDireccion4.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion5.setBackgroundResource(R.drawable.fondoselectorfalse);
			break;

		case R.id.radioButtonDireccion4:
			seleccionDireccion=4;
			appState.setDireccionFrecuente(direccionesFrecuentes[3]);
			selectorDireccion1.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion2.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion3.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion4.setBackgroundResource(R.drawable.fondoselectortrue);
			selectorDireccion5.setBackgroundResource(R.drawable.fondoselectorfalse);
			break;

		case R.id.radioButtonDireccion5:
			seleccionDireccion=5;
			appState.setDireccionFrecuente(direccionesFrecuentes[4]);
			selectorDireccion1.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion2.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion3.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion4.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion5.setBackgroundResource(R.drawable.fondoselectortrue);
			break;
		}
	}

	//************************************************************************
	
	public void PresionoDireccion(View view){

		switch(view.getId()) {
		case R.id.textodireccion1:
			seleccionDireccion=1;
			appState.setDireccionFrecuente(direccionesFrecuentes[0]);
			selectorDireccion1.setBackgroundResource(R.drawable.fondoselectortrue);
			selectorDireccion2.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion3.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion4.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion5.setBackgroundResource(R.drawable.fondoselectorfalse);
			break;

		case R.id.textodireccion2:
			seleccionDireccion=2;
			appState.setDireccionFrecuente(direccionesFrecuentes[1]);
			selectorDireccion1.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion2.setBackgroundResource(R.drawable.fondoselectortrue);
			selectorDireccion3.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion4.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion5.setBackgroundResource(R.drawable.fondoselectorfalse);
			break;

		case R.id.textodireccion3:
			seleccionDireccion=3;
			appState.setDireccionFrecuente(direccionesFrecuentes[2]);
			selectorDireccion1.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion2.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion3.setBackgroundResource(R.drawable.fondoselectortrue);
			selectorDireccion4.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion5.setBackgroundResource(R.drawable.fondoselectorfalse);
			break;

		case R.id.textodireccion4:
			seleccionDireccion=4;
			appState.setDireccionFrecuente(direccionesFrecuentes[3]);
			selectorDireccion1.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion2.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion3.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion4.setBackgroundResource(R.drawable.fondoselectortrue);
			selectorDireccion5.setBackgroundResource(R.drawable.fondoselectorfalse);
			break;

		case R.id.textodireccion5:
			seleccionDireccion=5;
			appState.setDireccionFrecuente(direccionesFrecuentes[4]);
			selectorDireccion1.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion2.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion3.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion4.setBackgroundResource(R.drawable.fondoselectorfalse);
			selectorDireccion5.setBackgroundResource(R.drawable.fondoselectortrue);
			break;
		}
	}

	//************************************************************************
	public void UsarDireccion(View view){
		Log.i(APPNAME, module + "ENTRO A UsarDireccion");
		if(seleccionDireccion==0){
			Toast.makeText(getApplicationContext(),"SELECCIONE UNA DIRECCION" ,Toast.LENGTH_LONG).show();
		}else{
			
			try{
				Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
				t.setScreenName("Usuario selecciono Favorito desde Lista de Favoritos");
				t.send(new HitBuilders.AppViewBuilder().build());
			}catch(Exception e){
				e.printStackTrace();
			}
			
			appState.setFlagfrecuentes(1);
			//Revisar donde se colocan las Indicaciones....
			String[] sacaInfo = appState.getDireccionFrecuente().split("\\|");
//			Log.i(APPNAME, module + copyInfo[copyInfo.length-1]);	//Index en el Servidor
//        	Log.i(APPNAME, module + copyInfo[copyInfo.length-2]);	//Longitud
//			Log.i(APPNAME, module + copyInfo[copyInfo.length-3]);	//Latitud
//			Log.i(APPNAME, module + copyInfo[copyInfo.length-4]);	//Barrio
//			Log.i(APPNAME, module + copyInfo[copyInfo.length-5]);	//Dir Usuario(Escrita en el Box)
//			Log.i(APPNAME, module + copyInfo[copyInfo.length-6]);	//Dir Servicio(Dir Google)
//			Log.i(APPNAME, module + copyInfo[copyInfo.length-7]);	//CiudadPais
		
			//Revisar si trae adicionales??
			
			String s ="";
			if(sacaInfo[sacaInfo.length-5].contains("~")){	//Trae Adicionales...
				String[] partes = sacaInfo[sacaInfo.length-5].split("~");
				s = partes[0] +"|" + sacaInfo[sacaInfo.length-4]+"|" + partes[1]+"|";
			}
			else	s = sacaInfo[sacaInfo.length-5] +"|" + sacaInfo[sacaInfo.length-4]+"|" + ""+"|";

			Intent i = getIntent();
			//i.putExtra("pedirTaxi", s);
			i.putExtra("completoDireccion", s);
			setResult(RESULT_OK, i);
			finish();
		}
	}

	//************************************************************************
	public void EliminarFavorito(View view){
		Log.i(APPNAME, module + "ENTRO A EliminarFavorito");
		if(seleccionDireccion==0){
			Toast.makeText(getApplicationContext(),"DEBE SELECCIONAR UNA DIRECCION PARA BORRAR" ,Toast.LENGTH_LONG).show();
		}else{
			
			try{
				Tracker t = ((TaxisLi) getApplication()).getTracker(TrackerName.APP_TRACKER);
				t.setScreenName("Usuario Presiono Eliminar Favorito");
				t.send(new HitBuilders.AppViewBuilder().build());
			}catch(Exception e){
				e.printStackTrace();
			}
			
			String[] sacaInfo = appState.getDireccionFrecuente().split("\\|");

        	//Se debe enviar Info a Juan C para borra ese Favorito...
        	String dirBorrar = "Z|2|"+appState.getNumberPhone()+"|"+appState.getCiudadPais().toUpperCase().trim() +"|" +sacaInfo[sacaInfo.length-1]+"|"+appState.getTramaGPSMap()+"\n"; 
        			
        	Intent borrarFavorito = new Intent();
        	borrarFavorito.putExtra("CMD", C.SEND);
        	borrarFavorito.putExtra("DATA", dirBorrar);
        	borrarFavorito.setAction(C.COM);
        	context.sendBroadcast(borrarFavorito);

        	ventanaEspera = new ProgressDialog(AListarFavoritos.this);

        	ventanaEspera.setTitle("BORRANDO DE FAVORITOS");
        	ventanaEspera.setMessage("ESPERE UN MOMENTO... ");
        	ventanaEspera.setCancelable(true);
        	ventanaEspera.show();

		}
	}

	//************************************************************************

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onPause");
		super.onPause();
		try {
			unregisterReceiver(receiver7);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onStart");
		super.onStart();
		registerReceiver(receiver7, filter7);
		GoogleAnalytics.getInstance(AListarFavoritos.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(APPNAME, module + "ENTRO A onStop");
		super.onStop();
		GoogleAnalytics.getInstance(AListarFavoritos.this).reportActivityStop(this);
	}


}
