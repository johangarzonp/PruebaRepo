package com.cotech.taxislibres;
import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//import android.util.Log;
import com.cotech.taxislibres.activities.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class TaxisLi extends Application{
	
	protected String appName = "TaxisLi";
	protected String module = "TaxisLi";
	
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;	
	private EstadosUnidad estadoUnidad;		
	private String idServicio = "0";
	
	private String direccionServicio= "0";
	private String direccionUsuario= "0";
	
	private String nombreUsuario="0";
	private String nombreTaxista="0";
	private String correoUsuario="0";
	private String barrio= "0";
	private String placa = "0";
	private String pinMovil= "0";
	private String LatitudMovil;
	private String LongitudMovil;
	
	private String numberPhone= "0";	
	private int flagGps;	
	private int contador=0;
		
	private int flagsolicitud;
	private int flagAnunciotaxi;
	private int metros;
	
	private String TramaGPSMap;

	private int inicializar;
	private String trama;
	private String resultado;
	private int AnchoPantalla;
	private int AltoPantalla;
	private int Densidad;
			
	private int Anchsolicitar;
	private int Altosolicitar;
	
	private int Anchocancelar;
	private int Altocancelar;
	
	private int Anchocalificar;
	private int Altocalificar;
	
	private int Anchoenviar;
	private int Altoenviar;
	
	private int Anchoentrar;
	private int Altoentrar;
	
	private String direccionGoogle= "0";
	private int flagActDatos;
	private int flagcancelado;
	
	private int flaginvisible;
	private String Serviciosave;
	private String MsgTaxista;
	private int flagconexion;
	
	private int flagrecordardireccion;
	
	private int flagCerrarMapa;
	
	private int numFrecuentes;
	
	private String dirFrec1;
	private String dirFrec2;
	private String dirFrec3;
	private String dirFrec4;
	private String dirFrec5;
	private String dirFrec6;
	private String dirFrec7;
	private String dirFrec8;
	private String dirFrec9;
	private String dirFrec10;
		
	/*****17-03-14**********/
	private double LatitudUsuario;
	private double LongitudUsuario;
	private String ciudadPais;
	private int flagfrecuentes;
	private String direccionFrecuente;
	/*****18-03-14**********/
	private String flagRegistro;
	private String datosTarjeta;
	/*****19-03-14**********/
	private int vale;
	private String Sepagacon;
	private String IdModiPayConductor;
	private String ValorPagar;
	/*****19-03-14**********/
	private int flagConfPago;
	
	private double UltimaLatitud;
	private double UltimaLongitud;
	private String UltimaLatitudString;
	private String UltimaLongitudString;
	
	private int formaPago;
	private boolean escogioPago;
	private String destinoUsuario;
	private String actividad;
	private boolean quieroEscuchar;
	
	private String userModipay;
	private String pwdModipay;
	
	private String comandoPayu;
	
	private String tarificacionVale;
	private String valorCarrera;
	private String incentivoTaxista;
	private int intentosdepago;
	private String payerIdTc;
	private String infoTarjeta1;
	private String infoTarjeta2;
	private String infoTarjeta3;
	private int numTarjetas;
	private String tarjetaPago;
	private String valeUsuario;
	private Long ultimahora;
	private String ultimoServicio;
	private boolean tengoClaveElectronicos;
	private boolean pedirClaveElectronicos;
	
	private int PaisSeleccionado;
	private String rtaError;
	private String Imei;
	
	private boolean borrarTarjeta;
	private String listaFavoritos;
	private boolean guardarFavorito;
	private int intentosSolicitud;
	private String adicionalesServicio="";
	private String chatTaxista;
	private String chatCentral;
	private String urlPromo="";
	private String keyGcm="No Hay";
	private boolean appPrimerPlano;
	private boolean appActiva;
	private boolean pushPendiente;
	private String msgPushPendiente;
	private String nombrePromo="";
	private String mensajeCentral="";
	private boolean pagoTarjetaExitoso=false;
	private String usuarioVales;
	private String claveVales;
	private String urlInfoTaxista;
//********************************************************************


	// change the following line 
	private static final String PROPERTY_ID = "UA-43520448-2";

	public static int GENERAL_TRACKER = 0;

	public enum TrackerName {
		APP_TRACKER, GLOBAL_TRACKER, ECOMMERCE_TRACKER,
	}

	public HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	public TaxisLi() {
		super();
	}

	public synchronized Tracker getTracker(TrackerName appTracker) {
		if (!mTrackers.containsKey(appTracker)) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = (appTracker == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) : (appTracker == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker) : analytics.newTracker(R.xml.ecommerce_tracker);
			mTrackers.put(appTracker, t);
		}
		return mTrackers.get(appTracker);
	}

//********************************************************************	
	@Override
	public void onCreate() {
		super.onCreate();
		try {
			Context context = getBaseContext();
			prefs = PreferenceManager.getDefaultSharedPreferences(context);
			editor = prefs.edit();
		} catch (Exception e) {
			Log.e(appName, module + e.getMessage());
			e.printStackTrace();
		}
	}
	
//********************************************************************
	public EstadosUnidad getEstadoUnidad() {
		String estado = prefs.getString("estadoUnidad", "LIBRE");
		estadoUnidad = EstadosUnidad.valueOf(estado);
		return estadoUnidad;
	}

	public void setEstadoUnidad(EstadosUnidad estadoUnidad) {
		editor.putString("estadoUnidad", estadoUnidad.name());
		editor.commit();
		this.estadoUnidad = estadoUnidad;
	}
//********************************************************************
	public String getIdServicio() {
		idServicio = prefs.getString("idServicio", "0000");
		return idServicio;
	}

	public void setIdServicio(String _idServicio) {
		editor.putString("idServicio", _idServicio);
		editor.commit();	
		idServicio = _idServicio;
	}
//********************************************************************	
	public String getDireccionServicio() {
		
		return direccionServicio;
	}

	public void setDireccionServicio(String _direccionServicio) {
		editor.putString("direccionServicio", _direccionServicio);
		editor.commit();		
		direccionServicio = _direccionServicio;
	}
//********************************************************************	
	public String getDireccionUsuario() {
		direccionUsuario = prefs.getString("direccionUsuario", "Direccion");
		return direccionUsuario;
	}

	public void setDireccionUsuario(String _direccionUsuario) {
		editor.putString("direccionUsuario", _direccionUsuario);
		editor.commit();
		direccionUsuario = _direccionUsuario;
	}
//********************************************************************	
	public String getNombreUsuario() {
		nombreUsuario = prefs.getString("nombreUsuario", "Jaime Lombana");
		return nombreUsuario;
	}

	public void setNombreUsuario(String _nombreUsuario) {
		editor.putString("nombreUsuario", _nombreUsuario);
		editor.commit();
		nombreUsuario = _nombreUsuario;
	}
//********************************************************************	
	public String getCorreoUsuario() {
		correoUsuario = prefs.getString("correoUsuario", "alombana24");
		return correoUsuario;
	}

	public void setCorreoUsuario(String _correoUsuario) {
		editor.putString("correoUsuario", _correoUsuario);
		editor.commit();
		nombreUsuario = _correoUsuario;
	}
//********************************************************************	
	public String getNombreTaxista() {
		nombreTaxista =prefs.getString("nombreTaxista", "Sin nombre");
		return nombreTaxista;
	}

	public void setNombreTaxista(String _nombreTaxista) {
		editor.putString("nombreTaxista", _nombreTaxista);
		editor.commit();
		nombreTaxista = _nombreTaxista;
	}
//********************************************************************	
	public String getBarrio() {
		barrio =prefs.getString("barrio", "Buscando!!!");
		return barrio;
	}

	public void setBarrio(String _barrio) {
		editor.putString("barrio", _barrio);
		editor.commit();
		barrio = _barrio;
	}
//********************************************************************	
	public String getPlaca() {
		placa =prefs.getString("placa", "ABC123");
		return placa;
	}

	public void setPlaca(String _placa) {
		editor.putString("placa", _placa);
		editor.commit();
		placa = _placa;
	}
//********************************************************************	
	public String getPinMovil() {
		pinMovil =prefs.getString("pinMovil", "000000");
		return pinMovil;
	}

	public void setPinMovil(String _pinMovil) {
		editor.putString("pinMovil", _pinMovil);
		editor.commit();
		pinMovil = _pinMovil;
	}
//********************************************************************
	public String getLatitudMovil() {
		LatitudMovil=prefs.getString("LatitudMovil", "000000");
		return LatitudMovil;
	}

	public void setLatitudMovil(String _latitudMovil) {
		editor.putString("LatitudMovil", _latitudMovil);
		editor.commit();
		LatitudMovil = _latitudMovil;
	}
//********************************************************************
	public String getLongitudMovil() {
		LongitudMovil=prefs.getString("LongitudMovil", "000000");
		return LongitudMovil;
	}

	public void setLongitudMovil(String _longitudMovil) {
		editor.putString("LongitudMovil", _longitudMovil);
		editor.commit();
		LongitudMovil = _longitudMovil;
	}
//********************************************************************	
	public String getNumberPhone() {
		numberPhone = prefs.getString("numberPhone", "3111111");
		return numberPhone;
	}

	public void setNumberPhone(String _numberPhone) {
		editor.putString("numberPhone", _numberPhone);
		editor.commit();
		numberPhone = _numberPhone;
	}
//********************************************************************	
	public int getFlagGps() {
		return flagGps;
	}

	public void setFlagGps(int flagGps) {
		this.flagGps = flagGps;
	}
//********************************************************************
	public int getContador() {
		return contador;
	}

	public void setContador(int contador) {
		this.contador = contador;
	}
//********************************************************************
	public int getFlagsolicitud() {
		return flagsolicitud;
	}

	public void setFlagsolicitud(int flagsolicitud) {
		this.flagsolicitud = flagsolicitud;
	}
//********************************************************************
	public int getFlagAnunciotaxi() {
		return flagAnunciotaxi;
	}

	public void setFlagAnunciotaxi(int flagAnunciotaxi) {
		this.flagAnunciotaxi = flagAnunciotaxi;
	}
//********************************************************************
	public int getMetros() {
		return metros;
	}

	public void setMetros(int metros) {
		this.metros = metros;
	}
//********************************************************************
	public String getTramaGPSMap() {
		return TramaGPSMap;
	}

	public void setTramaGPSMap(String tramaGPSMap) {
		TramaGPSMap = tramaGPSMap;
	}
//********************************************************************	
	public int getInicializar() {
		return inicializar;
	}

	public void setInicializar(int inicializar) {
		this.inicializar = inicializar;
	}
//********************************************************************
	public String getTrama() {
		return trama;
	}

	public void setTrama(String trama) {
		this.trama = trama;
	}
//********************************************************************
	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
//********************************************************************
	public int getAnchoPantalla() {
		return AnchoPantalla;
	}

	public void setAnchoPantalla(int anchoPantalla) {
		AnchoPantalla = anchoPantalla;
	}
//********************************************************************
	public int getAltoPantalla() {
		return AltoPantalla;
	}

	public void setAltoPantalla(int altoPantalla) {
		AltoPantalla = altoPantalla;
	}
//********************************************************************
	public int getDensidad() {
		return Densidad;
	}

	public void setDensidad(int densidad) {
		Densidad = densidad;
	}
//********************************************************************
	public int getAnchsolicitar() {
		Anchsolicitar= prefs.getInt("Anchsolicitar", 1);
		return Anchsolicitar;
	}

	public void setAnchsolicitar(int _anchsolicitar) {
		editor.putInt("anchsolicitar", _anchsolicitar);
		editor.commit();
		this.Anchsolicitar = _anchsolicitar;
						
	}
//********************************************************************
	public int getAltosolicitar() {
		Altosolicitar= prefs.getInt("Altosolicitar", 1);
		return Altosolicitar;
	}

	public void setAltosolicitar(int _altosolicitar) {
		editor.putInt("altosolicitar", _altosolicitar);
		editor.commit();
		this.Altosolicitar = _altosolicitar;
		
	}
//********************************************************************
	public int getAltocancelar() {
		return Altocancelar;
	}

	public void setAltocancelar(int altocancelar) {
		Altocancelar = altocancelar;
	}
//********************************************************************
	public int getAnchocancelar() {
		return Anchocancelar;
	}

	public void setAnchocancelar(int anchocancelar) {
		Anchocancelar = anchocancelar;
	}
//********************************************************************
	public int getAltocalificar() {
		return Altocalificar;
	}

	public void setAltocalificar(int altocalificar) {
		Altocalificar = altocalificar;
	}
//********************************************************************
	public int getAnchocalificar() {
		return Anchocalificar;
	}

	public void setAnchocalificar(int anchocalificar) {
		Anchocalificar = anchocalificar;
	}
//********************************************************************
	public int getAnchoenviar() {
		return Anchoenviar;
	}

	public void setAnchoenviar(int anchoenviar) {
		Anchoenviar = anchoenviar;
	}
//********************************************************************
	public int getAltoenviar() {
		return Altoenviar;
	}

	public void setAltoenviar(int altoenviar) {
		Altoenviar = altoenviar;
	}
//********************************************************************
	public int getAnchoentrar() {
		return Anchoentrar;
	}

	public void setAnchoentrar(int anchoentrar) {
		Anchoentrar = anchoentrar;
	}
//********************************************************************
	public int getAltoentrar() {
		return Altoentrar;
	}

	public void setAltoentrar(int altoentrar) {
		Altoentrar = altoentrar;
	}
//********************************************************************
	public String getDireccionGoogle() {
		return direccionGoogle;
	}

	public void setDireccionGoogle(String _direccionGoogle) {
		editor.putString("direccionGoogle", _direccionGoogle);
		editor.commit();
		direccionGoogle = _direccionGoogle;
	}
//********************************************************************
	public int getFlagActDatos() {
		flagActDatos= prefs.getInt("flagActDatos", 0);
		return flagActDatos;
	}

	public void setFlagActDatos(int _flagActDatos) {
		editor.putInt("flagActDatos", _flagActDatos);
		editor.commit();
		flagActDatos = _flagActDatos;
	}
//********************************************************************
	public int getFlagcancelado() {
		return flagcancelado;
	}

	public void setFlagcancelado(int flagcancelado) {
		this.flagcancelado = flagcancelado;
	}
//********************************************************************
	public int getFlaginvisible() {
		return flaginvisible;
	}

	public void setFlaginvisible(int flaginvisible) {
		this.flaginvisible = flaginvisible;
	}
//********************************************************************
	public String getServiciosave() {
		return Serviciosave;
	}

	public void setServiciosave(String serviciosave) {
		Serviciosave = serviciosave;
	}
//********************************************************************
	public String getMsgTaxista() {
		return MsgTaxista;
	}

	public void setMsgTaxista(String msgTaxista) {
		MsgTaxista = msgTaxista;
	}
//********************************************************************
	public int getFlagconexion() {
		return flagconexion;
	}

	public void setFlagconexion(int flagconexion) {
		this.flagconexion = flagconexion;
	}
//********************************************************************
	public int getFlagrecordardireccion() {
		return flagrecordardireccion;
	}

	public void setFlagrecordardireccion(int flagrecordardireccion) {
		this.flagrecordardireccion = flagrecordardireccion;
	}
//********************************************************************	
	public int getFlagCerrarMapa() {
		return flagCerrarMapa;
	}

	public void setFlagCerrarMapa(int flagCerrarMapa) {
		this.flagCerrarMapa = flagCerrarMapa;
	}
//*********************************************************************
//	Para Direcciones Frecuentes
//*********************************************************************
	public int getNumFrecuentes() {
		numFrecuentes = prefs.getInt("numFrecuentes", 0);
		return numFrecuentes;
	}

	public void setNumFrecuentes(int _numFrecuentes) {
		editor.putInt("numFrecuentes", _numFrecuentes);
		editor.commit();	
		this.numFrecuentes = _numFrecuentes;
	}
//********************************************************************
	public String getDirFrec1() {
		dirFrec1 = prefs.getString("dirFrec1","");
		return dirFrec1;
	}

	public void setDirFrec1(String _dirFrec1) {
		editor.putString("dirFrec1", _dirFrec1);
		editor.commit();
		this.dirFrec1 = _dirFrec1;
	}

	public String getDirFrec2() {
		dirFrec2 = prefs.getString("dirFrec2","");
		return dirFrec2;
	}

	public void setDirFrec2(String _dirFrec2) {
		editor.putString("dirFrec2", _dirFrec2);
		editor.commit();
		this.dirFrec2 = _dirFrec2;
	}

	public String getDirFrec3() {
		dirFrec3 = prefs.getString("dirFrec3","");
		return dirFrec3;
	}

	public void setDirFrec3(String _dirFrec3) {
		editor.putString("dirFrec3", _dirFrec3);
		editor.commit();
		this.dirFrec3 = _dirFrec3;
	}

	public String getDirFrec4() {
		dirFrec4 = prefs.getString("dirFrec4","");
		return dirFrec4;
	}

	public void setDirFrec4(String _dirFrec4) {
		editor.putString("dirFrec4", _dirFrec4);
		editor.commit();
		this.dirFrec4 = _dirFrec4;
	}

	public String getDirFrec5() {
		dirFrec5 = prefs.getString("dirFrec5","");
		return dirFrec5;
	}

	public void setDirFrec5(String _dirFrec5) {
		editor.putString("dirFrec5", _dirFrec5);
		editor.commit();
		this.dirFrec5 = _dirFrec5;
	}

	public String getDirFrec6() {
		dirFrec6 = prefs.getString("dirFrec6","");
		return dirFrec6;
	}

	public void setDirFrec6(String _dirFrec6) {
		editor.putString("dirFrec6", _dirFrec6);
		editor.commit();
		this.dirFrec6 = _dirFrec6;
	}

	public String getDirFrec7() {
		dirFrec7 = prefs.getString("dirFrec7","");
		return dirFrec7;
	}

	public void setDirFrec7(String _dirFrec7) {
		editor.putString("dirFrec7", _dirFrec7);
		editor.commit();
		this.dirFrec7 = _dirFrec7;
	}

	public String getDirFrec8() {
		dirFrec8 = prefs.getString("dirFrec8","");
		return dirFrec8;
	}

	public void setDirFrec8(String _dirFrec8) {
		editor.putString("dirFrec8", _dirFrec8);
		editor.commit();
		this.dirFrec8 = _dirFrec8;
	}

	public String getDirFrec9() {
		dirFrec9 = prefs.getString("dirFrec9","");
		return dirFrec9;
	}

	public void setDirFrec9(String _dirFrec9) {
		editor.putString("dirFrec9", _dirFrec9);
		editor.commit();
		this.dirFrec9 = _dirFrec9;
	}

	public String getDirFrec10() {
		dirFrec10 = prefs.getString("dirFrec10","");
		return dirFrec10;
	}

	public void setDirFrec10(String _dirFrec10) {
		editor.putString("dirFrec10", _dirFrec10);
		editor.commit();
		this.dirFrec10 = _dirFrec10;
	}
//***********************************************************************************
	public double getLatitudUsuario() {
		return LatitudUsuario;
	}

	public void setLatitudUsuario(double latitudUsuario) {
		LatitudUsuario = latitudUsuario;
	}
//********************************************************************
	public double getLongitudUsuario() {
		return LongitudUsuario;
	}

	public void setLongitudUsuario(double longitudUsuario) {
		LongitudUsuario = longitudUsuario;
	}
//********************************************************************
	public String getCiudadPais() {
		ciudadPais = prefs.getString("ciudadPais", "COLOMBIA");
		return ciudadPais;
	}

	public void setCiudadPais(String _ciudadPais) {
		editor.putString("ciudadPais", _ciudadPais);
		editor.commit();
		ciudadPais = _ciudadPais;
	}
//********************************************************************
	public int getFlagfrecuentes() {
		return flagfrecuentes;
	}

	public void setFlagfrecuentes(int flagfrecuentes) {
		this.flagfrecuentes = flagfrecuentes;
	}
//********************************************************************
	public String getDireccionFrecuente() {
		return direccionFrecuente;
	}

	public void setDireccionFrecuente(String direccionFrecuente) {
		this.direccionFrecuente = direccionFrecuente;
	}
//********************************************************************
	public String getFlagRegistro() {
		return flagRegistro;
	}

	public void setFlagRegistro(String flagRegistro) {
		this.flagRegistro = flagRegistro;
	}
//********************************************************************
	public String getDatosTarjeta() {
		return datosTarjeta;
	}

	public void setDatosTarjeta(String datosTarjeta) {
		this.datosTarjeta = datosTarjeta;
	}
//********************************************************************
	public int getVale() {
		return vale;
	}

	public void setVale(int vale) {
		this.vale = vale;
	}
//********************************************************************
	public String getSepagacon() {
		return Sepagacon;
	}

	public void setSepagacon(String sepagacon) {
		Sepagacon = sepagacon;
	}
//********************************************************************
	public String getIdModiPayConductor() {
		return IdModiPayConductor;
	}

	public void setIdModiPayConductor(String idModiPayConductor) {
		IdModiPayConductor = idModiPayConductor;
	}
//********************************************************************
	public String getValorPagar() {
		return ValorPagar;
	}

	public void setValorPagar(String valorPagar) {
		ValorPagar = valorPagar;
	}
//********************************************************************
	public int getFlagConfPago() {
		return flagConfPago;
	}

	public void setFlagConfPago(int flagConfPago) {
		this.flagConfPago = flagConfPago;
	}
//********************************************************************
	public double getUltimaLatitud() {
		UltimaLatitudString = prefs.getString("UltimaLatitudString", "4.0897222222222");
		UltimaLatitud=Double.parseDouble(UltimaLatitudString);
		return UltimaLatitud;
	}

	public void setUltimaLatitud(double _ultimaLatitud) {
		UltimaLatitud = _ultimaLatitud;
		UltimaLatitudString= new Double(UltimaLatitud).toString();
		editor.putString("UltimaLatitudString", UltimaLatitudString);
		editor.commit();
	}
//********************************************************************
	public double getUltimaLongitud() {
		UltimaLongitudString = prefs.getString("UltimaLongitudString", "-72.961944444444");
		UltimaLongitud=Double.parseDouble(UltimaLongitudString);
		return UltimaLongitud;
	}

	public void setUltimaLongitud(double _ultimaLongitud) {
		UltimaLongitud = _ultimaLongitud;
		UltimaLongitudString= new Double(UltimaLongitud).toString();
		editor.putString("UltimaLongitudString", UltimaLongitudString);
		editor.commit();

	}
//********************************************************************
	public int getFormaPago() {
		formaPago = prefs.getInt("formaPago", 0);
		return formaPago;
	}

	public void setFormaPago(int _formaPago) {
		editor.putInt("formaPago", _formaPago);
		editor.commit();
		formaPago = _formaPago;
	}
//********************************************************************
	public boolean isEscogioPago() {
		return escogioPago;
	}

	public void setEscogioPago(boolean escogioPago) {
		this.escogioPago = escogioPago;
	}
//********************************************************************
	public String getDestinoUsuario() {
		return destinoUsuario;
	}

	public void setDestinoUsuario(String destinoUsuario) {
		this.destinoUsuario = destinoUsuario;
	}
//********************************************************************
	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}
//********************************************************************
	public boolean isQuieroEscuchar() {
		quieroEscuchar = prefs.getBoolean("quieroEscuchar", false);
		return quieroEscuchar;
	}

	public void setQuieroEscuchar(boolean _quieroEscuchar) {
		editor.putBoolean("quieroEscuchar", _quieroEscuchar);
		editor.commit();
		this.quieroEscuchar = _quieroEscuchar;
	}
//********************************************************************
	public String getUserModipay() {
		userModipay = prefs.getString("userModipay", "Defecto");
		return userModipay;
	}

	public void setUserModipay(String _userModipay) {
		editor.putString("userModipay", _userModipay);
		editor.commit();
		userModipay = _userModipay;
	}
//********************************************************************
	public String getPwdModipay() {
		pwdModipay = prefs.getString("pwdModipay", "Defecto");
		return pwdModipay;
	}

	public void setPwdModipay(String _pwdModipay) {
		editor.putString("pwdModipay", _pwdModipay);
		editor.commit();
		pwdModipay = _pwdModipay;
	}
//********************************************************************
	public String getComandoPayu() {
		return comandoPayu;
	}

	public void setComandoPayu(String comandoPayu) {
		this.comandoPayu = comandoPayu;
	}
//********************************************************************
	public String getTarificacionVale() {
		return tarificacionVale;
	}

	public void setTarificacionVale(String tarificacionVale) {
		this.tarificacionVale = tarificacionVale;
	}
//********************************************************************
	public String getValorCarrera() {
		return valorCarrera;
	}

	public void setValorCarrera(String valorCarrera) {
		this.valorCarrera = valorCarrera;
	}
//********************************************************************
	public String getIncentivoTaxista() {
		return incentivoTaxista;
	}

	public void setIncentivoTaxista(String incentivoTaxista) {
		this.incentivoTaxista = incentivoTaxista;
	}
//********************************************************************
	public int getIntentosdepago() {
		return intentosdepago;
	}

	public void setIntentosdepago(int intentosdepago) {
		this.intentosdepago = intentosdepago;
	}
//********************************************************************
	public String getPayerIdTc() {
		payerIdTc = prefs.getString("payerIdTc", "NoHay");
		return payerIdTc;
	}

	public void setPayerIdTc(String _payerIdTc) {
		editor.putString("payerIdTc", _payerIdTc);
		editor.commit();
		payerIdTc = _payerIdTc;
	}
//********************************************************************	
	public String getInfoTarjeta1() {
		return infoTarjeta1;
	}

	public void setInfoTarjeta1(String infoTarjeta1) {
		this.infoTarjeta1 = infoTarjeta1;
	}
//********************************************************************
	public String getInfoTarjeta2() {
		return infoTarjeta2;
	}

	public void setInfoTarjeta2(String infoTarjeta2) {
		this.infoTarjeta2 = infoTarjeta2;
	}
//********************************************************************
	public String getInfoTarjeta3() {
		return infoTarjeta3;
	}

	public void setInfoTarjeta3(String infoTarjeta3) {
		this.infoTarjeta3 = infoTarjeta3;
	}
//********************************************************************
	public int getNumTarjetas() {
		numTarjetas = prefs.getInt("NumTarjetas",0);
		return numTarjetas;
	}

	public void setNumTarjetas(int _numTarjetas) {
		editor.putInt("NumTarjetas",_numTarjetas);
		editor.commit();
		numTarjetas = _numTarjetas;
	}
//********************************************************************
	public String getTarjetaPago() {
		tarjetaPago = prefs.getString("tarjetaPago", "NoHay");
		return tarjetaPago;
	}

	public void setTarjetaPago(String _tarjetaPago) {
		editor.putString("tarjetaPago", _tarjetaPago);
		editor.commit();
		tarjetaPago = _tarjetaPago;
	}
//********************************************************************
	public String getValeUsuario() {
		return valeUsuario;
	}

	public void setValeUsuario(String valeUsuario) {
		this.valeUsuario = valeUsuario;
	}
//********************************************************************
	public Long getUltimahora() {
		ultimahora = prefs.getLong("ultimaHora",123);
		return ultimahora;
	}

	public void setUltimahora(Long _ultimahora) {
		editor.putLong("ultimaHora", _ultimahora);
		editor.commit();
		ultimahora = _ultimahora;
	}
//********************************************************************
	public String getUltimoServicio() {
		return ultimoServicio;
	}

	public void setUltimoServicio(String ultimoServicio) {
		this.ultimoServicio = ultimoServicio;
	}
//********************************************************************
	public boolean isTengoClaveElectronicos() {
		tengoClaveElectronicos = prefs.getBoolean("claveelectronicos", false);
		return tengoClaveElectronicos;
	}

	public void setTengoClaveElectronicos(boolean _tengoClaveElectronicos) {
		editor.putBoolean("claveelectronicos", _tengoClaveElectronicos);
		editor.commit();
		tengoClaveElectronicos = _tengoClaveElectronicos;
	}
//********************************************************************
	public boolean isPedirClaveElectronicos() {
		pedirClaveElectronicos = prefs.getBoolean("pedirclaveelectronicos", false);
		return pedirClaveElectronicos;
	}

	public void setPedirClaveElectronicos(boolean _pedirClaveElectronicos) {
		editor.putBoolean("pedirclaveelectronicos", _pedirClaveElectronicos);
		editor.commit();
		pedirClaveElectronicos = _pedirClaveElectronicos;
	}
//********************************************************************
	public int getPaisSeleccionado() {
		PaisSeleccionado = prefs.getInt("PaisSeleccionado", 0);
		return PaisSeleccionado;
	}

	public void setPaisSeleccionado(int _paisSeleccionado) {
		editor.putInt("PaisSeleccionado", _paisSeleccionado);
		editor.commit();
		PaisSeleccionado = _paisSeleccionado;
	}
//********************************************************************
	public String getRtaError() {
		return rtaError;
	}

	public void setRtaError(String rtaError) {
		this.rtaError = rtaError;
	}
//********************************************************************
	public String getImei() {
		return Imei;
	}

	public void setImei(String imei) {
		Imei = imei;
	}
//********************************************************************
	public boolean isBorrarTarjeta() {
		return borrarTarjeta;
	}

	public void setBorrarTarjeta(boolean borrarTarjeta) {
		this.borrarTarjeta = borrarTarjeta;
	}
//********************************************************************
	public String getListaFavoritos() {
		listaFavoritos=prefs.getString("listaFavoritos", "NoHayFavoritos");
		return listaFavoritos;
	}

	public void setListaFavoritos(String _listaFavoritos) {
		editor.putString("listaFavoritos", _listaFavoritos);
		editor.commit();
		listaFavoritos = _listaFavoritos;
	}
//********************************************************************
	public boolean isGuardarFavorito() {
		return guardarFavorito;
	}

	public void setGuardarFavorito(boolean guardarFavorito) {
		this.guardarFavorito = guardarFavorito;
	}
//********************************************************************
	public int getIntentosSolicitud() {
		return intentosSolicitud;
	}

	public void setIntentosSolicitud(int intentosSolicitud) {
		this.intentosSolicitud = intentosSolicitud;
	}
//********************************************************************
	public String getAdicionalesServicio() {
		return adicionalesServicio;
	}

	public void setAdicionalesServicio(String adicionalesServicio) {
		this.adicionalesServicio = adicionalesServicio;
	}
//********************************************************************
	public String getChatTaxista() {
		chatTaxista=prefs.getString("chatTaxista", "");
		return chatTaxista;
	}

	public void setChatTaxista(String _chatTaxista) {
		editor.putString("chatTaxista", _chatTaxista);
		editor.commit();
		chatTaxista = _chatTaxista;
	}
//********************************************************************
	public String getChatCentral() {
		chatCentral=prefs.getString("chatCentral", "");
		return chatCentral;
	}

	public void setChatCentral(String _chatCentral) {
		editor.putString("chatCentral", _chatCentral);
		editor.commit();
		chatCentral = _chatCentral;
	}
//********************************************************************
	public String getUrlPromo() {
		return urlPromo;
	}

	public void setUrlPromo(String urlPromo) {
		this.urlPromo = urlPromo;
	}
//********************************************************************
	public String getKeyGcm() {
		return keyGcm;
	}

	public void setKeyGcm(String keyGcm) {
		this.keyGcm = keyGcm;
	}
//********************************************************************
	public boolean isAppPrimerPlano() {
		appPrimerPlano = prefs.getBoolean("appPrimerPlano", false);
		return appPrimerPlano;
	}

	public void setAppPrimerPlano(boolean _appPrimerPlano) {
		editor.putBoolean("appPrimerPlano", _appPrimerPlano);
		editor.commit();
		appPrimerPlano = _appPrimerPlano;
		
	}
//********************************************************************
	public boolean isAppActiva() {
		appActiva = prefs.getBoolean("appActiva", false);
		return appActiva;
	}

	public void setAppActiva(boolean _appActiva) {
		editor.putBoolean("appActiva", _appActiva);
		editor.commit();
		appActiva = _appActiva;

	}
//********************************************************************
	public boolean isPushPendiente() {
		pushPendiente= prefs.getBoolean("pushPendiente", false);
		return pushPendiente;
	}

	public void setPushPendiente(boolean _pushPendiente) {
		editor.putBoolean("pushPendiente", _pushPendiente);
		editor.commit();
		pushPendiente = _pushPendiente;
	}
//********************************************************************

	public String getMsgPushPendiente() {
		msgPushPendiente=prefs.getString("msgPushPendiente", "");
		return msgPushPendiente;
	}

	public void setMsgPushPendiente(String _msgPushPendiente) {
		editor.putString("msgPushPendiente", _msgPushPendiente);
		editor.commit();
		msgPushPendiente = _msgPushPendiente;
	}
//********************************************************************
	public String getNombrePromo() {
		return nombrePromo;
	}

	public void setNombrePromo(String nombrePromo) {
		this.nombrePromo = nombrePromo;
	}
//*******************************************************************
	public String getMensajeCentral() {
		return mensajeCentral;
	}

	public void setMensajeCentral(String mensajeCentral) {
		this.mensajeCentral = mensajeCentral;
	}
//*******************************************************************
	public boolean isPagoTarjetaExitoso() {
		return pagoTarjetaExitoso;
	}

	public void setPagoTarjetaExitoso(boolean pagoTarjetaExitoso) {
		this.pagoTarjetaExitoso = pagoTarjetaExitoso;
	}
//*******************************************************************
	public String getUsuarioVales() {
		usuarioVales=prefs.getString("usuarioVales", "");
		return usuarioVales;
	}

	public void setUsuarioVales(String _usuarioVales) {
		editor.putString("usuarioVales", _usuarioVales);
		editor.commit();
		usuarioVales = _usuarioVales;
	}
//*******************************************************************
	public String getClaveVales() {
		claveVales=prefs.getString("claveVales", "");
		return claveVales;
	}

	public void setClaveVales(String _claveVales) {
		editor.putString("claveVales", _claveVales);
		editor.commit();
		claveVales = _claveVales;
	}
//*******************************************************************
	public String getUrlInfoTaxista() {
		return urlInfoTaxista;
	}

	public void setUrlInfoTaxista(String urlInfoTaxista) {
		this.urlInfoTaxista = urlInfoTaxista;
	}
	
}