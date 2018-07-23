package common;

public class Utils {

	public static final String CODEBASE = "java.rmi.server.codebase";
	public static int puerto = 7777;
	public static final String direccionServidor = "localhost";
	
	public static void setCodeBase(Class<?> c) {
		String ruta = c.getProtectionDomain().getCodeSource().getLocation().toString();
		
		String path = System.getProperty(CODEBASE);
		if (path != null && !path.isEmpty()) {
			ruta = path + " " + ruta;
		}
		System.setProperty(CODEBASE, ruta);
	}
	//generar url de objetos remotos
	public static String getURL(String ip, int rmipuerto, String nombre_servicio){
		return "rmi://" + ip + ":" + rmipuerto + "/" + nombre_servicio;
	}

	public static String getURL(String ip, int rmipuerto, String nombre_servicio, String id_unico){
		return "rmi://" + ip + ":" + rmipuerto + "/" + nombre_servicio + "/" + id_unico;
	}

	public static String letraMayuscula(String str) {
	    if (str == null || str.isEmpty()) {
	        return str;            
	    } else {
	        return Character.toUpperCase(str.charAt(0)) + str.substring(1); 
	    }
	}
	
	public static String todoMayusculas (String str) {
		if (str == null || str.isEmpty()) {
	        return str;            
	    } else {
	        return str.toUpperCase(); 
	    }
	}
	
	//metodo que toma la letra del dni y la coloca delante
	public static String generarID(String dni) {
		String id_unico = dni.substring(8, 9).concat(dni.substring(0, 8));
		id_unico = todoMayusculas(id_unico);
		return id_unico;
	}
	
}
