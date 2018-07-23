package cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import common.ClienteInterface;
import common.Oferta;
import common.ServicioAutenticacionInterface;
import common.ServicioMercanciasInterface;
import common.ServicioVentaInterface;
import common.Usuario;
import common.Utils;

public class Cliente {
	
	private static ServicioAutenticacionInterface autenticacion;
	private static ServicioMercanciasInterface mercancia;
	private static ServicioVentaInterface venta;
	private static ClienteInterface callbackCliente = null;

	private static String id_unico = null;
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws Exception {
		
		autenticacion = (ServicioAutenticacionInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"autenticacion"));
		mercancia = (ServicioMercanciasInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"mercancia"));
		
		//se levanta el callback del cliente registrandolo
		callbackCliente = new ClienteImpl();
		mercancia.registrarCallback(callbackCliente);
		
		boolean valido= false;
		while (id_unico==null) {
			valido = menuLogin();
			if(valido) {
			//Arrancamos el id_unico del cliente
				menu();
			}
		}
		System.exit(0);
	}
	
	private static void mostrarMenu () throws Exception{
		System.out.println("----- ID: "+id_unico+" -----");
		System.out.println("----- MENU CLIENTE -----");
		System.out.println("1 - Introducir demanda");
		System.out.println("2 - Quitar demanda");
		System.out.println("3 - Recibir oferta");
		System.out.println("4 - Comprar Mercancia");
		System.out.println("5 - Darse de baja del sistema");
		System.out.println("6 - Salir");
		System.out.println("Seleccionar una opción");
	}
	
	private static void menu() throws Exception{
		int opcion = 0;
		
		while (opcion != 6) {
			mostrarMenu();
			try{ opcion = Integer.parseInt(reader.readLine());}
			catch(Exception e) { opcion =0;}
			switch (opcion) {
				case 1:
					introducirDemanda();
					break;
				case 2:
					quitarDemanda();
					break;
				case 3:
					recibirOferta();
					break;
				case 4:
					comprarMercancia();
					break;
				case 5:
					autenticacion.bajaUser(id_unico);
					mercancia.quitarrCallback(callbackCliente);
					System.out.println("Baja de usuario realizada");
					id_unico = null;
					opcion = 6;
					break;
				case 6://cerramos el id_unico del cliente del regulador
					mercancia.quitarrCallback(callbackCliente);
					autenticacion.desloguearUser(id_unico);
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
					mercancia.quitarrCallback(callbackCliente);
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
			System.out.println("Introducir nombre de usuario");
			String nombre = reader.readLine();
			nombre = Utils.letraMayuscula(nombre);
			System.out.println("Introducir contraseña");
			String pass = reader.readLine();
			id_aux = autenticacion.loguearUser(nombre, pass, true);
			//if (id_aux == null) {
			//	System.out.println("Usuario no válido");
			//}
		//} while (id_aux == null);
	
		return id_aux;
	}

	private static String solicitarDatosRegistro() throws Exception {
		String id_aux = null;
		
		//do {
			System.out.println("Introducir nombre de usuario");
			String nombre = reader.readLine();
			nombre = Utils.letraMayuscula(nombre);
			System.out.println("Introducir contraseña");
			String pass = reader.readLine();
			String dni = null;
			//boolean valido = false;
			do {
				System.out.println("Introducir DNI (tipo 12345678X)");
				dni = reader.readLine();
				if ((dni.length() != 9) || (!Character.isLetter(dni.charAt(8)))){
					dni = null;// = true;
					System.out.println("Formato de DNI no válido");
				}
			}while (dni == null); //}while (!valido);
			Usuario user = new Usuario (nombre, pass, dni, true);
			id_aux = autenticacion.autenticarUser(user);
		//} while (id_aux == null);
		return id_aux;
	}
	
	private static void introducirDemanda () throws Exception{
		int tipo = 0;
		System.out.println("Nueva demanda");
		while (tipo == 0) {
			System.out.println("Elegir Producto: (introducir número)");
			System.out.println("1)Tomates 2)Limones 3)Naranjas 4)Fresas 5)Platanos 6)Melones 7)Sandias");
			try{ tipo = Integer.parseInt(reader.readLine());}
			catch(Exception e) { tipo = 0;}
			if (tipo>0 || tipo<8) {
				System.out.println("Producto solicitado");
				System.out.println();
			}
			else { 
				System.out.println("Formato no válido");
				tipo = 0;
				}
		
		}
		mercancia.registrarDemanda(id_unico, tipo);;
	}
	
	private static void quitarDemanda () throws Exception{
		boolean salir = false;
		int numero = 0;
		ArrayList<Integer> lista_dem_aux = new ArrayList<Integer> (mercancia.mostrarDemandasCliente(id_unico));
		System.out.println("Estas son tus demandas actuales:");
		int contador = 1;
		for (Integer tipo : lista_dem_aux) {
			System.out.println (contador+"- "+Oferta.mostrarTipo(tipo));
		    contador++;
		}
		System.out.println();
		System.out.println("Eliminando Demanda");
		while (!salir) {
			System.out.print("Elige número de demanda: ");
			try{ numero = Integer.parseInt(reader.readLine());}
			catch(Exception e) { salir = false;}
			if (numero>0 && numero<=lista_dem_aux.size()) {
				mercancia.eliminarDemanda(id_unico, numero);
				System.out.println("Oferta Borrada");
				System.out.println();
				salir=true;
			}
			else { System.out.println("Opcion no válida");}
		}		
	}
	private static int numeroDemanda(int tipo) throws Exception{
		int numero = 0;
		ArrayList<Integer> lista_dem_aux = new ArrayList<Integer> (mercancia.mostrarDemandasCliente(id_unico));
		java.util.Iterator<Integer> it1 = lista_dem_aux.iterator();
		int contador = 0;
		while (it1.hasNext() && numero == 0) {
			contador++;
			if (it1.next()==tipo) {
				numero = contador;
			}
		}
		return numero;
	}
	
	private static void recibirOferta() throws Exception{
		ArrayList<Integer> lista_dem_aux = new ArrayList<Integer> (mercancia.mostrarDemandasCliente(id_unico));
		if(lista_dem_aux.isEmpty()) {
			System.out.println("Actualmente no tienes ninguna demanda");
		}
		else {
			Iterator<Integer> it = lista_dem_aux.iterator();
			ArrayList<Oferta> lista_offer_tipo = new ArrayList<Oferta>();
			int contador = 1;
			while (it.hasNext()) {
				Integer producto = it.next().intValue();
				lista_offer_tipo = mercancia.mostrarOfferTipo(producto);
				if(!lista_offer_tipo.isEmpty()) {
					for (Oferta offers : lista_offer_tipo) {
						System.out.println ("\t"+contador+"- ID: "+offers.getId_unico()+" Tiene: "+offers.getKilos()+"  kilos de "+Oferta.mostrarTipo(offers.getTipo())+" a "+offers.getPrecio()+" €");
						contador++;
					}
				}
				
			}
			System.out.println();
		}
	}
	
	private static void comprarMercancia() throws Exception{
		System.out.println("---- Comprando Mercancia ----");
		boolean salir = false;
		boolean comprado = false;
		int tipo = 0;
		while (!salir) {
			System.out.println("Elegir Producto: (introducir número)");
			System.out.println("1)Tomates 2)Limones 3)Naranjas 4)Fresas 5)Platanos 6)Melones 7)Sandias");
			try{ tipo = Integer.parseInt(reader.readLine());}
			catch(Exception e) { salir = false;}
			if (tipo>0 && tipo<8) {
				//recuperamos las ofertas de ese producto para comprobar que existe esa oferta
				ArrayList<Oferta> lista_offer_dist = new ArrayList<Oferta>();
				ArrayList<Oferta> lista_offer_dist_tipo = new ArrayList<Oferta>();
				String id = null;
				do {
					System.out.println("Introducir Identificador del Distribuidor:  (tipo XX1234567)");
					id = reader.readLine();
					id = Utils.todoMayusculas(id);
					if ((id.length() != 9) || (!Character.isLetter(id.charAt(0))) || (!Character.isLetter(id.charAt(1)))){
						id = null;
						System.out.println("Formato de ID no válido");
					}
				}while (id == null); 
				lista_offer_dist = mercancia.mostrarOfertaDistribuidor(id);
				int contador = 0;
				if(!lista_offer_dist.isEmpty()) {
					
					for (Oferta offers : lista_offer_dist) {
						if (offers.getTipo()==tipo) {
							contador++;
							System.out.println ("\t"+contador+"- ID: "+offers.getId_unico()+" Tiene: "+offers.getKilos()+"  kilos de "+Oferta.mostrarTipo(offers.getTipo())+" a "+offers.getPrecio()+" €");
							lista_offer_dist_tipo.add(offers);
						}
					}
				}
				int numero_oferta = 0;
				if (contador>0) {
					System.out.println("Elegir número de oferta: (introducir número)");
					try{ numero_oferta = Integer.parseInt(reader.readLine());}
					catch(Exception e) { salir = false;}
				}
				else {
					System.out.println("No hay ofertas de ese producto");
				}
				if (numero_oferta>0 || numero_oferta<=contador) {
					try {
						venta = (ServicioVentaInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"venta",id));
					} catch (NotBoundException e) {
						e.printStackTrace();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					
					venta.realizarCompra(lista_offer_dist_tipo.get(numero_oferta-1),numero_oferta,id_unico);
					comprado=true;
					salir=true;
				}
				else {
					System.out.println("Oferta no válida");
				}			
			}
			else { System.out.println("Formato no válido");}
		}

			if(comprado) {
				int numero = numeroDemanda(tipo);
				if(numero!=0) {
					mercancia.eliminarDemanda(id_unico, numero);
				}
				System.out.println("Compra de "+Oferta.mostrarTipo(tipo)+" realizada");
			}
			
	}
}
