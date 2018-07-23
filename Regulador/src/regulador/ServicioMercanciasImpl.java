package regulador;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Vector;

import common.ClienteInterface;
import common.Oferta;
import common.ServicioDatosInterface;
import common.ServicioMercanciasInterface;
import common.Usuario;
import common.Utils;



public class ServicioMercanciasImpl extends UnicastRemoteObject implements ServicioMercanciasInterface{

	private static final long serialVersionUID = -35508931859385128L;
	private static ServicioDatosInterface datos;
	private Vector clientList;
	
	protected ServicioMercanciasImpl() throws RemoteException {
		super();
		clientList = new Vector();
		// TODO Apéndice de constructor generado automáticamente
	}

	@Override
	public synchronized void registrarOferta(Oferta oferta) throws RemoteException {
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		datos.ingresarOferta(oferta);
		hacerCallback(oferta);
	}
	@Override
	public synchronized void quitarOferta(String id_dist, int indice) throws RemoteException {
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		datos.borrarOferta(id_dist,indice);
	}


	
	public void mostrarOfertaTipo(int tipo) throws RemoteException {
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
			ArrayList<Oferta> lista_ofertas = new ArrayList<Oferta> (datos.listarOfertaTipo(tipo));
			int contador = 1;
			for (Oferta offers : lista_ofertas) {
				System.out.println ("\t"+contador+"- ID: "+offers.getId_unico()+" Tiene: "+offers.getKilos()+" kilos de "+Oferta.mostrarTipo(offers.getTipo())+" a "+offers.getPrecio()+" €");
			    contador++;
			}
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Oferta> mostrarOfferTipo (int tipo) throws RemoteException{
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ArrayList<Oferta> lista_ofertas = new ArrayList<Oferta> (datos.listarOfferTipoConectados(tipo));
		return lista_ofertas;
	}

	@Override
	public ArrayList<Oferta> mostrarOfertaDistribuidor(String id_unico) throws RemoteException {
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ArrayList<Oferta> lista_ofertas = new ArrayList<Oferta> (datos.listarOfertaDistribuidor(id_unico));
		return lista_ofertas;
	}
	
	public void mostrarOfertas() throws RemoteException{
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
			datos.listarOfertas();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void registrarDemanda (String id_cliente, int demanda) throws RemoteException{
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		datos.ingresarDemanda(id_cliente,demanda);
	}
	
	public synchronized void eliminarDemanda (String id_cliente, int demanda) throws RemoteException{
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		datos.borrarDemanda(id_cliente,demanda);
	}

	public void mostrarDemandas() throws RemoteException{
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
			datos.listarDemandas();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
	public ArrayList<Integer> mostrarDemandasCliente(String id_unico) throws RemoteException{
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));

		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ArrayList<Integer> lista_dem_cli = new ArrayList<Integer> (datos.listarDemandasCliente(id_unico));
		return lista_dem_cli;
		
	}
	
	public synchronized void registrarCallback(ClienteInterface objCallbackCliente) throws RemoteException{
		if (!(clientList.contains(objCallbackCliente)))
		 {
		 clientList.addElement(objCallbackCliente);
		 } 
	}

	@Override
	public synchronized void quitarrCallback(ClienteInterface objCallbackCliente) throws RemoteException {
		clientList.removeElement(objCallbackCliente);
		/*if (clientList.removeElement(objCallbackCliente)) {
			System.out.println("quitando callback de cliente");
		} 
		else {
			System.out.println("cliente no registrado en callback");
		} */
		
	}
	
	private synchronized void hacerCallback(Oferta oferta ) throws RemoteException{
		for (int i = 0; i < clientList.size(); i++){
			//System.out.println("Enviando nueva oferta");
			ClienteInterface nextClient = (ClienteInterface) clientList.elementAt(i);
			nextClient.notificarme("- ID: "+oferta.getId_unico()+" Tiene: "+oferta.getKilos()+"  kilos de "+Oferta.mostrarTipo(oferta.getTipo())+" a "+oferta.getPrecio()+" €");
		}
	} 
	

}
