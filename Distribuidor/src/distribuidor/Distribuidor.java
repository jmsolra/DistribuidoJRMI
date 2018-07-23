package distribuidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.util.ArrayList;

import common.Oferta;
import common.ServicioAutenticacionInterface;
import common.ServicioMercanciasInterface;
import common.Usuario;
import common.Utils;


public class Distribuidor {
	
	private static ServicioAutenticacionInterface autenticacion;
	private static ServicioMercanciasInterface mercancia;
	private static ServicioVentaImpl venta = null;

	private static String id_unico = null;
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
public static void main(String[] args) throws Exception {
		
		autenticacion = (ServicioAutenticacionInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"autenticacion"));
		mercancia = (ServicioMercanciasInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"mercancia"));
		boolean valido= false;
		while (id_unico==null) {
			valido = menuLogin();
			if(valido) {
			//Arrancamos el id_unico del distribuidor
				//se levanta el servicio de venta del distribuidor
				venta = new ServicioVentaImpl();
				Naming.rebind(Utils.getURL(Utils.direccionServidor, Utils.puerto,"venta",id_unico),venta);
			    System.out.println("Servicio de Venta preparado");
				menu();
				
			}
		}
		System.exit(0);
}
private static void mostrarMenu () throws Exception{
	System.out.println("----- ID: "+id_unico+" -----");
	System.out.println("----- MENU DISTRIBUIDOR -----");
	System.out.println("1 - Introducir oferta");
	System.out.println("2 - Quitar oferta");
	System.out.println("3 - Mostrar ventas");
	System.out.println("4 - Darse de baja del sistema");
	System.out.println("5 - Salir");
	System.out.println("Seleccionar una opción");
}

private static void menu() throws Exception{
	//Arrancamos el id_unico del distribuidor

	int opcion = 0;
	while (opcion != 5) {
		mostrarMenu();
		try{opcion= Integer.parseInt(reader.readLine());}
		catch(Exception e) { opcion =0;}
		switch (opcion) {
			case 1:
				introducirOferta();
				break;
			case 2:
				quitarOferta();
				break;
			case 3:
				venta.listarVentas(id_unico);
				break;
			case 4://al dar de baja tambien se cierra el servicio de venta
				autenticacion.bajaUser(id_unico);
				System.out.println("Baja de usuario realizada");
				Naming.unbind(Utils.getURL(Utils.direccionServidor, Utils.puerto,"venta",id_unico));
				System.out.println("Servicio de Venta desmontado");
				opcion = 5;
				id_unico = null;
				break;
			case 5://cerramos el id_unico del cliente del regulador
				autenticacion.desloguearUser(id_unico);
				Naming.unbind(Utils.getURL(Utils.direccionServidor, Utils.puerto,"venta",id_unico));
				System.out.println("Servicio de Venta desmontado");
				id_unico = null;
				break;
			default:
				System.out.println("Opción incorrecta");
				break;
		}
	}
}
			

private static boolean menuLogin() throws Exception{
	boolean valido = false;
	int opcion = 0;
	while (id_unico==null) {
		System.out.println("----- MENU LOGIN -----");
		System.out.println("1 - Registrar un nuevo usuario");
		System.out.println("2 - Autenticarse en el sistema");
		System.out.println("3 - Salir");
		System.out.println("Seleccionar una opción");
		try{opcion = Integer.parseInt(reader.readLine());}
		catch(Exception e) { opcion =0;}
		switch (opcion) {
			case 1:
				id_unico = solicitarDatosRegistro();
				if(id_unico!=null) {
					valido = true;
				}
				break;
			case 2:
				id_unico = solicitarlogin();
				if(id_unico!=null) {
					valido = true;
				}
				break;
			case 3:
				id_unico="salir";
				break;
			default:
				System.out.println("Opción incorrecta");
				break;
		}
		if (id_unico == null) {
			System.out.println("Usuario no válido");
		}
	}
	return valido;
}

	private static String solicitarlogin() throws Exception {
		String id_aux = null;
		//do {
			System.out.println("Introducir nombre de distribuidor");
			String nombre = reader.readLine();
			nombre = Utils.letraMayuscula(nombre);
			System.out.println("Introducir contraseña");
			String pass = reader.readLine();
			id_aux = autenticacion.loguearUser(nombre, pass,false);
			//if (id_aux == null) {
			//	System.out.println("Usuario no válido");
			//}
		//} while (id_aux == null);
	
		return id_aux;
	}

	private static String solicitarDatosRegistro() throws Exception {
		String id_aux = null;
		
		//do {
			System.out.println("Introducir nombre de distribuidor");
			String nombre = reader.readLine();
			nombre = Utils.letraMayuscula(nombre);
			System.out.println("Introducir contraseña");
			String pass = reader.readLine();
			String dni = null;
			//boolean valido = false;
			do {
				System.out.println("Introducir CIF (tipo X1234567X)");
				dni = reader.readLine();
				if ((dni.length() != 9) || (!Character.isLetter(dni.charAt(0))) || (!Character.isLetter(dni.charAt(8))) ){
					dni = null;// = true;
					System.out.println("Formato de CIF no válido");
				}
			}while (dni == null); //}while (!valido);
			Usuario user = new Usuario (nombre, pass, dni, false);
			id_aux = autenticacion.autenticarUser(user);
		//} while (id_aux == null);
		return id_aux;
	}
	
	private static void introducirOferta () throws Exception{
		boolean salir = false;
		int tipo = 0;
		double precio=0;
		int kilos = 0;
		System.out.println("Nueva oferta");
		while (!salir) {
			System.out.println("Elegir Producto: (introducir número)");
			System.out.println("1)Tomates 2)Limones 3)Naranjas 4)Fresas 5)Platanos 6)Melones 7)Sandias");
			try{ tipo = Integer.parseInt(reader.readLine());}
			catch(Exception e) { salir = false;}
			if (tipo>0 || tipo<8) {
				System.out.print("Introducir Precio (0.00): ");
				try{precio = Double.parseDouble(reader.readLine());}
				catch(Exception e) {System.out.println("Formato no valido");salir = false; }
				System.out.print("Introducir kilos: ");
				try{kilos = Integer.parseInt(reader.readLine());}
				catch(Exception e) {System.out.println("Formato no valido");salir = false; }
				salir=true;
				System.out.println("Oferta introducida");
				System.out.println();
			}
			else { System.out.println("Formato no válido");}
		}
		Oferta ofe= new Oferta(id_unico,tipo,kilos,precio);
		mercancia.registrarOferta (ofe);
	}
	
	private static void quitarOferta () throws Exception{
		boolean salir = false;
		int numero = 0;
		ArrayList<Oferta> lista_ofertas = new ArrayList<Oferta> (mercancia.mostrarOfertaDistribuidor (id_unico));
		System.out.println("Estas son tus ofertas actuales:");
		int contador = 1;
		String producto=null;
		for (Oferta offers : lista_ofertas) {
			producto = Oferta.mostrarTipo(offers.getTipo());
			System.out.println (contador+"- ID: "+offers.getId_unico()+" Tiene: "+offers.getKilos()+" kilos de "+producto+" a "+offers.getPrecio()+" €");
		    contador++;
		}
		System.out.println();
		System.out.println("Eliminando Oferta");
		while (!salir) {
			System.out.print("Elige número de oferta: ");
			try{ numero = Integer.parseInt(reader.readLine());}
			catch(Exception e) { salir = false;}
			if (numero>0 && numero<=lista_ofertas.size()) {
				mercancia.quitarOferta (id_unico,numero);
				System.out.println("Oferta Borrada");
				System.out.println();
				salir=true;
			}
			else { System.out.println("Opcion no válida");}
		}		
	}

}
