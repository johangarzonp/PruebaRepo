package com.cotech.taxislibres;

public class C {	

	//INTERFAZ AMAP
	public static final String MAP = "MAP";
	public static final int CANCEL_PROCESSDIALOG = 1;
	public static final int TOAST = 2;
	public static final int INVISIBLEBOTON = 3;
	public static final int AVISO_TAXI = 4;
	public static final int UBICACION = 5;
	public static final int PREGUNTAMOVIL = 6;
	public static final int MSGTAXISTA = 7;
	public static final int SONIDOALFRENTE=8;
	public static final int CERRARAPP=9;	//COMUN PARA ASOLICITUD OJO...
	public static final int RECIBIOIMAGEN=10;

	public static final int NUM_FRECUENTES = 5;


	public static final int PEDIR_TAXI = 11;
	public static final int PAGO_EFECTIVO = 12;
	public static final int PASA_ENCUESTA = 13;
	public static final int PAGO_TARJETA = 14;
	public static final int VERIFICAR_TAXI = 15;
	public static final int NORECIBIOIMAGEN = 16;

	public static final int TOKENS_MAPA_OK = 17;
	public static final int TOKENS_MAPA_ERROR =18;
	public static final int ERROR_SOCKET =19;	//Compartido con Solicitud...
	public static final int VOLVER_A_PEDIR_TAXI =20;
	public static final int PAGO_CUPO_TARJETA=21;
	public static final int CANCELO_TAXISTA=22;
	public static final int CARGAR_PROPAGANDA=23;
	//------Para Rta de Consulta de Vale--------
	public static final int VALE_OK_SIN_CODIGO = 24;
	public static final int VALE_MAL_SIN_CODIGO =25;
	public static final int VALE_OK_CON_CODIGO=26;
	public static final int VALE_CODIGO_OK=27;
	public static final int VALE_OK_CODIGO_MAL=28;
	//------Para Rta de Consulta de Favoritos--------
	public static final int FAVORITOS_OK=29;
	public static final int NO_HAY_FAVORITOS=30;
	public static final int ERROR_CARGANDO_FAVORITOS=31;
	public static final int ALMACENAR_FAVORITOS_OK=32;
	public static final int ALMACENAR_FAVORITOS_ERROR=33;
	//-------Para T.C en caso que existan tarjetas hacer la consulta
	public static final int CONSULTAR_TARJETAS_REG=34;
	//-------Para Consultar la Promo
	public static final int VER_PROMO=35;
	public static final int ERROR_PROMO=36;
	//-------Para los msgs Push que llegan de Servicio
	public static final int FILRAR_SERVICIO=37;
	public static final int NO_MOVIL=38;
	public static final int MSGCONTACT = 39;

	// MODULO COMUNICACION
	public static final String COM = "COM";
	public static final int SEND = 1;
	public static final int DISCONECT = 2;
	public static final int ENCRIPTADO = 3;

	// MODULO DE TEXT TO SPEECH	
	public static final String TEXT_TO_SPEECH = "TEXT_TO_SPEECH";
	public static final int HABLAR = 1;

	// MODULO DATA BASE
	public static final int NUM_MENSAJES = 150;

	//EN LA SOLICITUD
	public static final String SOLICITUD = "SOLICITUD";

	//PARA REGISTRAR TARJETA
	public static final String REGISTRAR_TARJETA="ARegistrarTarjeta";
	public static final int RESPUESTA_OK = 1;
	public static final int RESPUESTA_MAL = 2;
	public static final int RESPUESTA_SOCKET = 3;
	public static final int RESPUESTA_FALLOPING = 4;
	public static final int RESPUESTA_DESCONOCIDA = 5;
	public static final int RESPUESTA_REGISTRO_MIEMBRO = 6;
	public static final int RESPUESTA_VERIFICACION_MIEMBRO = 7;


	//PARA RESPUESTA DE GET_TOKENS(En Solicitar)
	public static final int GET_TOKENS_OK = 41;
	public static final int GET_TOKENS_ERROR = 42;
	//public static final int VALE_OK_SIN_CODIGO = 3;
	//public static final int VALE_MAL_SIN_CODIGO = 4;
	//public static final int VALE_OK_CON_CODIGO=5;
	//public static final int VALE_CODIGO_OK=6;
	//public static final int VALE_OK_CODIGO_MAL=7;
	//public static final int FAVORITOS_OK=8;
	//NO USAR EL 9 COMUN CON AMAP
	//	public static final int NO_HAY_FAVORITOS=10;
	//public static final int ERROR_CARGANDO_FAVORITOS=11;
	//	public static final int ALMACENAR_FAVORITOS_OK=12;
	//	public static final int ALMACENAR_FAVORITOS_ERROR=13;
	//	public static final int FAVORITO_BORRADO_OK=14;
	//	public static final int FAVORITO_BORRADO_ERROR=15;

	//PARA LISTAR TARJETAS

	public static final String LISTAR_TARJETAS="AListarTarjetas";

	public static final int BORRAR_TARJETA = 1;
	public static final int BORRADO_OK = 2;
	public static final int BORRADO_ERROR = 3;

	//PARA PAGAR VALE Y TARJETA
	public static final String PAGO_NO_EFECTIVO="APagarVale";

	public static final int PAGO_OK = 1;
	public static final int PAGO_MAL = 2;

	//PARA ENTRADA

	public static final String ENTRADA="Entrada";
	public static final int RESPUESTA_REGISTRO = 1;
	public static final int RESPUESTA_ACCESAR_USUARIO = 2;
	public static final int RESPUESTA_OLVIDO_CLAVE = 3;

	//PARA FAVORITOS

	public static final String LISTAR_FAVORITOS="AListarFavoritos";
	public static final int FAVORITO_BORRADO_OK=1;
	public static final int FAVORITO_BORRADO_ERROR=2;

	//PARA ACTUALIZAR DATOS
	public static final String ACTUALIZAR_DATOS="AActualizarDatos";

	public static final int ACTUALIZACION_OK = 1;
	public static final int ACTUALIZACION_NADA = 2;
	public static final int ACTUALIZACION_MAL = 3;

	//EN LA COMPLETAR ADICIONALES
	public static final String ADICIONALES = "ADICIONALES";

	//EN LA COMPLETAR ADICIONALES
	public static final String CONFSOLICITUD = "CONFIRMARSOLICITUD";

	//PARA MOSTRAR INFO DEL TAXI QUE LLEGA...
	public static final String LLEGOTAXI = "LLEGOTAXI";

	//PARA MOSTRAR INFO DEL TAXI QUE LLEGA...
	public static final String VERALFRENTE = "VERALFRENTE";

	//PARA PREGUNTAR SI ESTA ABORDO DEL TAXI...
	public static final String ABORDOTAXI = "ABORDOTAXI";

	//PARA SALIR DE LA APP
	public static final String SALIRAPP = "SALIRAPP";

	//PARA NUEVA SOLICITUD DE SERVICIO
	public static final String NVASOLICITUD = "NVASOLICITUD";

	//PARA COMPARTIR DATOS DEL SERVICIO
	public static final String COMPARTIRSERVICIO = "COMPARTIRSERVICIO";

	//PARA RECONFIRMAR EL SERVICIO
	public static final String RECONFSERVICIO = "RECONFSERVICIO";

	//PARA CANCELAR EL SERVICIO
	public static final String CANCELARSERVICIO = "CANCELARSERVICIO";

	//PARA CALIFICAR EL SERVICIO
	public static final String CALIFICARSERVICIO = "CALIFICARSERVICIO";

	//PARA INFORMAR PAGO EL SERVICIO
	public static final String INFORMARPAGO = "INFORMARPAGO";

	//PARA ENVIAR MSG A CENTRAL
	public static final String MENSAJECENTRAL = "MENSAJECENTRAL";

	//PARA INFORMAR SIN INTERNET
	public static final String SININTERNET = "SININTERNET";

	//PARA PEDIR CLAVE MEDIOS ELECTRONICOS
	public static final String CLAVEELECTRONICOS = "CLAVEELECTRONICOS";

	//PARA PEDIR REGISTRO CLAVE MEDIOS ELECTRONICOS
	public static final String REGISTROCLAVEELECTRONICOS = "REGISTROCLAVEELECTRONICOS";

	//PARA BORRAR EL HISTORICO DE LOS SERVICIOS
	public static final String BORRARHISTORICO = "BORRARHISTORICO";

	//PARA ENVIAR MSG A CENTRAL
	public static final String MENSAJETAXISTA = "MENSAJETAXISTA";

	//PARA ENVIAR MSG A CENTRAL
	public static final String VERHISTORICO = "VERHISTORICO";

	//PARA CUANDO EL TAXISTA CANCELO EL SERVICIO
	public static final String TAXISTACANCELO = "TAXISTACANCELO";

	//PARA OPCION DE ALMACENAR FAVORITOS
	public static final String GUARDAFAVORITOS = "GUARDAFAVORITOS";

	//PARA OPCION DE MOSTRAR ADICIONALES
	public static final String MOSTRARADICIONALES = "MOSTRARADICIONALES";
	
	//PARA OPCION DE LISTAR LOS VALES
	public static final String LISTAR_VALES="AListarVales";
	public static final int LISTA_VALES_OK=1;
	public static final int LISTA_VALES_ERROR=2;
}
