package regulador;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import common.Utils;

public class Regulador {

	//entrada por teclado
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	//puerto de lectura

	
	private static Registry registry  = null;
	
	private static ServicioDatosImpl datos = null;
	private static ServicioAutenticacionImpl autenticacion = null;
	private static ServicioMercanciasImpl mercancia = null;
	
	private static void iniciarRegistry(int RMIPuerto) throws RemoteException {
	     try {
	          registry = LocateRegistry.getRegistry(RMIPuerto);
	          registry.list(); 
	     } catch (RemoteException ex) {
	          registry = LocateRegistry.createRegistry(RMIPuerto);
	          System.out.println("RMI registry creado en puerto " + RMIPuerto);
	     }
	 }
	
	private static void detenerRegistry() throws RemoteException {
		try {
			if (registry != null) {
				UnicastRemoteObject.unexportObject(registry, true);
			}
		} catch (Exception e) { } 
	 }
	
	private static void mostrarMenu() throws Exception{
		int opcion = 0;
        boolean salir;
		while (opcion != 8) {
			salir=false;
			System.out.println("----- MENU REGULADOR -----");
			System.out.println("1 - Listar ofertas de distribuidores logueados");
			System.out.println("2 - Listar todas las ofertas almacenadas por tipos");
			System.out.println("3 - Listar demandas actuales");
			System.out.println("4 - Listar clientes (Registrados)");
			System.out.println("5 - Listar distribuidores (Registrados)");
			System.out.println("6 - Listar clientes (Logueados)");
			System.out.println("7 - Listar distribuidores (Logueados)");
			System.out.println("8 - Salir y guardar estado");
			System.out.println("Seleccionar una opción");
			try{opcion = Integer.parseInt(reader.readLine());}
			catch(Exception e) { opcion =0;}
			switch (opcion) {
				case 1:
					System.out.println("---- Lista de ofertas Distribuidores Logueados ----");
					mercancia.mostrarOfertas();
					System.out.println();
					opcion=0;
					break;					
				case 2:
					System.out.println("---- Lista de ofertas por tipo ----");
					while (!salir) {
						System.out.println("Elegir Producto: (introducir número)");
						System.out.println("1)Tomates 2)Limones 3)Naranjas 4)Fresas 5)Platanos 6)Melones 7)Sandias");
						try{ opcion = Integer.parseInt(reader.readLine());}
						catch(Exception e) { salir = false;}
						if (opcion>0 || opcion<8) {
							mercancia.mostrarOfertaTipo(opcion);
							System.out.println();
							
							salir=true;
						}
						else { System.out.println("Formato no válido");}
					}
					opcion=0;
					break;
				case 3:
					System.out.println("---- Lista de demandas ----");
							mercancia.mostrarDemandas();
							System.out.println();
					opcion=0;
					break;
				case 4:
					System.out.println("Lista de Usuarios Actualizada");
					datos.listaUsuarios(true);
					System.out.println();
					break;
				case 5:
					System.out.println("Lista de Distribuidores Actualizada");
					datos.listaUsuarios(false);
					System.out.println();
					break;
				case 6:
					System.out.println("Lista de Usuarios Conectados");
					datos.listaUsuariosLogueados(true);
					System.out.println();
					break;
				case 7:
					System.out.println("Lista de Distribuidores Conectados");
					datos.listaUsuariosLogueados(false);
					System.out.println();
					break;			
				case 8:
					datos.guardarUsuarios();
					datos.guardarOfertas();
					datos.guardarDemandas();
					datos.guardarVentas();
					Naming.unbind(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
					System.out.println("Servicio de Datos desmontado");
					Naming.unbind(Utils.getURL(Utils.direccionServidor, Utils.puerto,"autenticacion"));
					System.out.println("Servicio de Autenticacion desmontado");
					//System.exit(0);
					break;
				default:
					System.out.println("Opción incorrecta");
					break;
			}
		}
	}
	public static void main(String[] args) throws Exception {
		
		//se inicia el registro en el puerto
		iniciarRegistry(Utils.puerto);
		//se levanta el servicio de datos
		datos = new ServicioDatosImpl();
		Naming.rebind(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"),datos);
        System.out.println("Servicio de Datos preparado");
		
		//se levanta el servicio de autenticacion
		autenticacion = new ServicioAutenticacionImpl();
		Naming.rebind(Utils.getURL(Utils.direccionServidor, Utils.puerto,"autenticacion"),autenticacion);
        System.out.println("Servicio Autenticacion preparado");
        
      //se levanta el servicio de mercancia
      	mercancia = new ServicioMercanciasImpl();
      	Naming.rebind(Utils.getURL(Utils.direccionServidor, Utils.puerto,"mercancia"),mercancia);
             System.out.println("Servicio Mercancia preparado");

        mostrarMenu();
        detenerRegistry();
        System.exit(0);
        
	}
	
	
}
