package regulador;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;


import common.Oferta;
import common.ServicioDatosInterface;
import common.Usuario;
import common.Utils;
import common.Venta;

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface{

	protected ServicioDatosImpl() throws RemoteException {
		super();
		this.cargarDatos();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4146743276753102773L;
	private Map<String, Usuario> lista_usuarios_conect = new HashMap<String, Usuario>();
	private Map<String, ArrayList<Oferta>> lista_ofertas = new HashMap<String, ArrayList<Oferta>>();
	private Map<String, ArrayList<Integer>> lista_demandas = new HashMap<String, ArrayList<Integer>>();
	private Map<String, ArrayList<Venta>> lista_ventas = new HashMap<String, ArrayList<Venta>>();
	private ArrayList <Usuario> lista_users = new ArrayList <Usuario>(); 
	
	

	
	@Override
	public synchronized boolean registrarUsuario(Usuario usuario, String id_unico) throws RemoteException {
		// se comprueba que el cliente/distribuidor no ha sido registrado antes
		//if (lista_usuarios_conect.containsKey(id_unico)) {
		boolean valido = true; 
		for (Usuario users : lista_users) {
			if (Utils.generarID(users.getDni()).equals(id_unico)) {
				valido = false;
			}
		}
		if (valido){
			lista_usuarios_conect.put(id_unico, usuario);
			lista_users.add(usuario);
			valido = true;
		}
		/*if (lista_users.contains(usuario)) {
			return false;
		}else { //se añade el usuario a la lista de usuarios registro y al de logueados
			lista_usuarios_conect.put(id_unico, usuario);
			lista_users.add(usuario);
			return true;
		}*/
		return valido;
	}

	
	//metodo que comprueba si el usuario cliente ya esta registrado
	@Override
	public synchronized String loguearUsuario(String nombre, String pass, Boolean tipo) throws RemoteException{
		String id_unico = null;
		for (Usuario users : lista_users) {
			if (users.getNombre().equals(nombre) && users.getPass().equals(pass) && users.getTipo()==tipo) {
				id_unico = Utils.generarID(users.getDni());
				lista_usuarios_conect.put(id_unico, users);
			}
		}
		return id_unico;
	}

	//metrodo que elimina el id_unico de usuario del lsitado de usuarios conectados 
	public synchronized void desloguearUsuario(String id_unico) throws RemoteException {
		lista_usuarios_conect.remove(id_unico);
		
	}
	
	//metrodo que elimina el usuario del lsitado de usuarios conectados y de los registrados
	public synchronized void bajaUsuario(String id_unico) throws RemoteException {
		lista_users.remove(buscarUsuario(id_unico));
		lista_usuarios_conect.remove(id_unico);
	}
	
	public Usuario buscarUsuario (String id_unico) throws RemoteException{
		return lista_usuarios_conect.get(id_unico);
	}
	
	//mostrar lista de clientes (true) o distribuidores (false)registrados en el sistema
	public void listaUsuarios (boolean tipo) throws RemoteException{
		if (!lista_users.isEmpty()) {
			String documento = "DNI";
			if (!tipo) {
				documento = "CIF";
			}
			int contador = 1;
			for (Usuario users : lista_users) {
				if (users.getTipo() == tipo) {
			    System.out.println (contador+"- ID: "+Utils.generarID(users.getDni())+" Nombre: "+users.getNombre()+" "+documento+": "+users.getDni());
			    contador++;
				}
			}
		}
		else {
			System.out.println("No hay ningún usuario registrado");
		}
	}
	
	//mostar lista de clientes (true) o distribuidores (false) logueados en el sistema
	public void listaUsuariosLogueados (boolean tipo) throws RemoteException{
		if (!lista_usuarios_conect.isEmpty()) {
			String documento = "DNI";
			if (!tipo) {
				documento = "CIF";
			}
			ArrayList <Usuario> lista_aux = new ArrayList <Usuario> (lista_usuarios_conect.values());
			int contador = 1;
			for (Usuario users : lista_aux) {
				if (users.getTipo() == tipo) {
			    System.out.println (contador+"- ID: "+Utils.generarID(users.getDni())+" Nombre: "+users.getNombre()+" "+documento+": "+users.getDni());
			    contador++;
				}
			}
		}
		else {
			System.out.println("No hay ningún usuario logueado");
		}
	}
	
	public String obtenerRuta() throws RemoteException{
		try {
			File ruta = new File ("datos");
			if (!ruta.exists()) {
				ruta.mkdir();
			}
			return ruta.getCanonicalPath();

		} catch (Exception e) {return null;}
	}
	
	public void guardarUsuarios() throws RemoteException {
		try {
			String fichero = obtenerRuta()+File.separator+"usuarios.dat";
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero));
			oos.writeObject(lista_users);
			oos.flush();
			oos.close();
			System.out.println("Usuarios guardados correctamente --> "+fichero);
		}catch (Exception e) {}
	}
	
	public void guardarOfertas() throws RemoteException {
		try {
			String fichero = obtenerRuta()+File.separator+"ofertas.dat";
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero));
			oos.writeObject(lista_ofertas);
			oos.flush();
			oos.close();
			System.out.println("Ofertas guardadas correctamente --> "+fichero);
		}catch (Exception e) {}
	}
	
	@Override
	public void guardarDemandas() throws RemoteException {
		try {
			String fichero = obtenerRuta()+File.separator+"demandas.dat";
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero));
			oos.writeObject(lista_demandas);
			oos.flush();
			oos.close();
			System.out.println("Demandas guardadas correctamente --> "+fichero);
		}catch (Exception e) {}
		
	}
	
	public void guardarVentas() throws RemoteException {
		try {
			String fichero = obtenerRuta()+File.separator+"ventas.dat";
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero));
			oos.writeObject(lista_ventas);
			oos.flush();
			oos.close();
			System.out.println("Ventas guardadas correctamente --> "+fichero);
		}catch (Exception e) {}
		
	}
	
	@SuppressWarnings("unchecked")
	public void cargarDatos() throws RemoteException {
		try {
			ObjectInputStream ois;
			String ruta = obtenerRuta()+File.separator+ "usuarios.dat";
			if(new File(ruta).exists()) {
				ois = new ObjectInputStream(new FileInputStream(ruta));
				lista_users = (ArrayList<Usuario>) ois.readObject();
				ois.close();
			}
			ruta = obtenerRuta()+File.separator+ "ofertas.dat";
			if(new File(ruta).exists()) {
				ois = new ObjectInputStream(new FileInputStream(ruta));
				lista_ofertas = (Map<String, ArrayList<Oferta>>) ois.readObject();
				ois.close();
			}
			ruta = obtenerRuta()+File.separator+ "demandas.dat";
			if(new File(ruta).exists()) {
				ois = new ObjectInputStream(new FileInputStream(ruta));
				lista_demandas = (Map<String, ArrayList<Integer>>) ois.readObject();
				ois.close();
			}
			ruta = obtenerRuta()+File.separator+ "ventas.dat";
			if(new File(ruta).exists()) {
				ois = new ObjectInputStream(new FileInputStream(ruta));
				lista_ventas= (Map<String, ArrayList<Venta>>) ois.readObject();
				ois.close();
			}
		}catch (Exception e) {}
		
	}

	//metodos de servicio de mercancias
	//almacenar las ofertas con clave el id del distribuidor
	@Override
	public synchronized void ingresarOferta(Oferta oferta) throws RemoteException{
		ArrayList <Oferta> lista_aux = new ArrayList <Oferta> ();
		if (lista_ofertas.containsKey(oferta.getId_unico())) {
			lista_aux = lista_ofertas.get(oferta.getId_unico());
		}
		lista_aux.add(oferta);
		//al añadir añ hashmap sobreescribe el valor contenido por el key anterior
		lista_ofertas.put(oferta.getId_unico(),lista_aux);
	}
	//quitar las ofertas con clave el id del distribuidor del tipo indicado y los kilos indicados
	@Override
	public synchronized void borrarOferta(String id_dist, int indice)throws RemoteException {
		ArrayList <Oferta> lista_aux = new ArrayList <Oferta> ();
		lista_aux =listarOfertaDistribuidor (id_dist);
		/*Iterator<Oferta> it = lista_aux.iterator();
		while (it.hasNext()) {
			int tipo= it.next().getTipo();
			if (tipo == oferta.getTipo()) {
				lista_aux.remove(indice);
			}
			it.hasNext();
		}*/
		lista_aux.remove(indice-1);
		lista_ofertas.put(id_dist, lista_aux);
	}
	//quitor las ofertas con clave el id del distribuidor del tipo indicado y los kilos indicados
/*	@Override
	public void borrarOferta(Oferta oferta)throws RemoteException {
		String id_dist = oferta.getId_unico();
		ArrayList <Oferta> lista_aux = new ArrayList <Oferta> ();
		if (lista_ofertas.containsKey(id_dist)) {
			lista_aux = lista_ofertas.get(id_dist);

			lista_aux.remove(oferta);
			if (!lista_aux.isEmpty()) {
				lista_ofertas.put(id_dist,lista_aux);
			}
		}
	}
	public void borrarOferta (Oferta oferta)throws RemoteException{

		ArrayList <Oferta> lista_aux = new ArrayList <Oferta> ();
		if (!lista_ofertas.containsKey(oferta.getId_unico())) {
			lista_aux = lista_ofertas.get(oferta.getId_unico());
			for (Oferta offers : lista_aux) {
				if (offers.getTipo() == oferta.getTipo() && offers.getPrecio() == oferta.getPrecio() && offers.getKilos() == oferta.getKilos()) {
					lista_aux.remove(offers);
				}	
			}
			lista_ofertas.replace(oferta.getId_unico(),lista_aux);
		}
	}
	*/
	
	
	public ArrayList<Oferta> listarOfertaTipo (int tipo)throws RemoteException{
		ArrayList <Oferta> lista_salida = new ArrayList<Oferta> ();
		if (!lista_ofertas.isEmpty()) {
			java.util.Iterator<ArrayList<Oferta>> it1 = lista_ofertas.values().iterator();
			while (it1.hasNext()) {
				ArrayList <Oferta> lista_aux = new ArrayList <Oferta> (it1.next());
				for (Oferta offers : lista_aux) {
					if (offers.getTipo() == tipo) {
						lista_salida.add(offers);
					}	
				}
			}
		}
		else {System.out.println("No hay ofertas almacenadas");}
			return lista_salida;
	}

	public ArrayList<Oferta> listarOfferTipoConectados (int tipo) throws RemoteException{
		ArrayList <Usuario> lista_aux_dis = new ArrayList <Usuario> (lista_usuarios_conect.values());
		ArrayList <Oferta> lista_off_aux = new ArrayList<Oferta>();
		ArrayList <Oferta> lista_off_salida = new ArrayList<Oferta>();
		for (Usuario users : lista_aux_dis) {
			if (!users.esCliente()) {
				lista_off_aux=(listarOfertaDistribuidor(Utils.generarID(users.getDni())));
				for (Oferta offers : lista_off_aux) {
					if (offers.getTipo() == tipo) {
						lista_off_salida.add(offers);
					}
				}				
			}
		}
		return lista_off_salida;
	}
	
	public ArrayList<Oferta> listarOfertaDistribuidor (String id_unico) throws RemoteException{
		ArrayList <Oferta> lista_salida = new ArrayList<Oferta> ();
		if (!lista_ofertas.isEmpty() && lista_ofertas.containsKey(id_unico)) {
			lista_salida =lista_ofertas.get(id_unico);
		}
		return lista_salida;
	}

	//muestra una lista de todas las ofertas de todos los distribuidores logueados
	public void listarOfertas () throws RemoteException{
		ArrayList <Usuario> lista_aux_cli = new ArrayList <Usuario> (lista_usuarios_conect.values());
		int contador1 = 1;
		for (Usuario users : lista_aux_cli) {
			if (!users.esCliente()) {
				ArrayList <Oferta> lista_off_aux = new ArrayList<Oferta>(listarOfertaDistribuidor(Utils.generarID(users.getDni())));
				int contador2 = 1;
				System.out.println();
				System.out.println(contador1+"- Distribuidor: "+users.getNombre()+" con ID: "+Utils.generarID(users.getDni())+" oferta:");
				for (Oferta offers : lista_off_aux) {
					System.out.println ("\t"+contador2+"- ID: "+offers.getId_unico()+" Tiene: "+offers.getKilos()+" kilos de "+Oferta.mostrarTipo(offers.getTipo())+" a "+offers.getPrecio()+" €");
				    contador2++;
				}
			contador1++;
			}
		}
	}
	
	//registra la demanda de la mercancia tipo al cliente id
	@Override
	public synchronized void ingresarDemanda(String id_cliente, int demanda) throws RemoteException{
		ArrayList <Integer> lista_aux = new ArrayList <Integer> ();
		if (lista_demandas.containsKey(id_cliente)) {
			lista_aux = lista_demandas.get(id_cliente);
		}
		if (!lista_aux.contains(demanda)){
			lista_aux.add(demanda);
			//al añadir añ hashmap sobreescribe el valor contenido por el key anterior
			lista_demandas.put(id_cliente,lista_aux);
		}
		
		
	}

	//borra la demanda de la mercancia tipo por el cliente id
	@Override
	public synchronized void borrarDemanda(String id_cliente, int demanda) throws RemoteException{
		ArrayList <Integer> lista_aux = new ArrayList <Integer> ();
		lista_aux = lista_demandas.get(id_cliente);
		lista_aux.remove(demanda-1);
		lista_demandas.put(id_cliente, lista_aux);
		
	}

	//devuelve el identificador de cliente que demanda una mercancia en concreto
	@Override
	public ArrayList<String> listarDemandasTipo(int tipo) throws RemoteException {
		ArrayList <String> lista_salida = new ArrayList<String> ();
		if (!lista_demandas.isEmpty()) {
			Iterator<Map.Entry<String, ArrayList<Integer>>> it = lista_demandas.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, ArrayList<Integer>> client = it.next();
				if (client.getValue().contains(tipo)) {
						lista_salida.add(client.getKey());
				}
			}
		}
		else {System.out.println("No hay ofertas almacenadas");}
			return lista_salida;
	}

	//muestra las demandas de un cliente logueado
	@Override
	public ArrayList<Integer> listarDemandasCliente(String id_unico) throws RemoteException {
		ArrayList <Integer> lista_salida = new ArrayList<Integer> ();
		if (!lista_demandas.isEmpty() && lista_demandas.containsKey(id_unico)) {
			lista_salida = lista_demandas.get(id_unico);
		}
		return lista_salida;
	}
	
	//muestra una lista de todas las demandas de todos los clientes logueados
	public void listarDemandas () throws RemoteException{
		ArrayList <Usuario> lista_aux_cli = new ArrayList <Usuario> (lista_usuarios_conect.values());
		int contador1 = 1;
		for (Usuario users : lista_aux_cli) {
			if (users.esCliente()) {
				ArrayList <Integer> list_dem_aux = new ArrayList<Integer>(listarDemandasCliente(Utils.generarID(users.getDni())));
				int contador2 = 1;
				System.out.println();
				System.out.println(contador1+"- Cliente "+users.getNombre()+" con ID: "+Utils.generarID(users.getDni())+" solicita:");
				for (Integer tipo : list_dem_aux) {
					System.out.println ("\t"+contador2+"- "+Oferta.mostrarTipo(tipo));
				    contador2++;
				}
			contador1++;
			}
		}
	}
	public synchronized void ingresarVenta(Venta venta) throws RemoteException{
		ArrayList <Venta> lista_aux = new ArrayList <Venta> ();
		if (lista_ventas.containsKey(venta.getId_unico())) {
			lista_aux = lista_ventas.get(venta.getId_unico());
		}
		lista_aux.add(venta);
		//al añadir añ hashmap sobreescribe el valor contenido por el key anterior
		lista_ventas.put(venta.getId_unico(),lista_aux);
	}
	public ArrayList<Venta> listarVentasDistribuidor (String id_unico) throws RemoteException{
		ArrayList <Venta> lista_salida = new ArrayList<Venta> ();
		if (!lista_ventas.isEmpty() && lista_ventas.containsKey(id_unico)) {
			lista_salida =lista_ventas.get(id_unico);
		}
		return lista_salida;
	}
}
