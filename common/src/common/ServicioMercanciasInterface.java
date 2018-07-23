package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServicioMercanciasInterface extends Remote{
	
	public void registrarOferta (Oferta oferta) throws RemoteException;

	public void quitarOferta (String id_dist, int indice) throws RemoteException;
	
	public void mostrarOfertaTipo (int tipo) throws RemoteException;
	
	public ArrayList<Oferta> mostrarOfferTipo (int tipo) throws RemoteException;
	
	public ArrayList<Oferta> mostrarOfertaDistribuidor (String id_unico) throws RemoteException;
	
	public void mostrarOfertas () throws RemoteException;
	
	public void registrarDemanda (String id_cliente, int demanda) throws RemoteException;
	
	public void eliminarDemanda (String id_cliente, int demanda) throws RemoteException;
	
	public void mostrarDemandas () throws RemoteException;
	
	public ArrayList<Integer> mostrarDemandasCliente(String id_unico) throws RemoteException;
	
	public void registrarCallback (ClienteInterface objCallbackCliente) throws RemoteException;
	
	public void quitarrCallback (ClienteInterface objCallbackCliente) throws RemoteException;
}
