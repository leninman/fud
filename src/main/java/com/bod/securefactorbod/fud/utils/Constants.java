package com.bod.securefactorbod.fud.utils;

public class Constants {
	
	public static int NOTVALIDPARAMETER_CODE=96;
	
	public static int NOTVALIDCLIENT_CODE=97;
	
	public static int VALIDCLIENT_CODE=98;
	
	public static int SERVERERROR_CODE=99;
	
	public static int FINGERPRINTOK_CODE=100;
	
	public static int FINGERPRINTBAD_CODE=101;
       
	public static int MACHINESAVED_CODE=102;
        
	public static int BADUSER_CODE=103;
        
        public static int CONFFINGERSAFE_CODE=104;
        
        
	
	public static String NOTVALIDPARAMETER_DESC="PARÁMETRO NO VALIDO";
	
	public static String NOTVALIDCLIENT_DESC="EL CLIENTE REPRESENTADO POR EL SHAREDKEY SUMINISTRADO NO EXISTE";
	
	public static String VALIDCLIENT_DESC="EL CLIENTE REPRESENTADO POR EL SHAREDKEY SUMINISTRADO EXISTE";
	
	
	public static String SERVERERROR_DESC="ERROR EN EL SERVIDOR O EN LA COMUNICACION ENTRE LA APLICACION Y EL SERVIDOR";
	
	
	public static String FINGERPRINTOK_DESC="LA HUELLA DEL DISPOSITIVO COINCIDE CON UNO DE SU LISTA BLANCA, POR LO TANTO EL EQUIPO ES DE USO FRECUENTE";
	

	public static String FINGERPRINTBAD_DESC="LA HUELLA SUMINISTRADA NO COINCIDE CON NINGUNA DE LAS REGISTRADAS, POR LO TANTO EL EQUIPO NO ES DE USO FRECUENTE";
	
	public static String MACHINESAVED_DESC="EL EQUIPO Y SU HUELLA HAN SIDO REGISTRADOS";
       
	public static String BADUSER_DESC="LA HUELLA DEL EQUIPO NO CORRESPONDE AL CLIENTE SUMNISTRADO POR EL SHAREDKEY";
	
        public static String CONFFINGERSAFE_DESC="LA CONFIGURACION DE LOS PARAMETROS PARA LA HUELLA SE HA GUARDADO EXITOSAMENTE";
	
	
	
	
	
	
	
	
	
	
	
	
	
}
	
	
	
	
