package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServicioDatosInterface extends Remote{

	public boolean registrarUsuario(Usuario usuario, String id_unico) throws RemoteException;
	
	public String loguearUsuario(String nombre, String pass, Boolean tipo) throws RemoteException;
	
	public void desloguearUsuario(String id_unico) throws RemoteException;
	
	public void bajaUsuario(String id_unico) throws RemoteException;
	
	public void listaUsuarios (boolean tipo) throws RemoteException;
	
	public void listaUsuariosLogueados (boolean tipo) throws RemoteException;
	
	
	public void ingresarOferta(Oferta oferta) throws RemoteException;

	public void borrarOferta(String id_dist, int indice) throws RemoteException;
	
	public ArrayList<Oferta> listarOfertaTipo (int tipo) throws RemoteException;
	
	public ArrayList<Oferta> listarOfferTipoConectados (int tipo) throws RemoteException;
	
	public ArrayList<Oferta> listarOfertaDistribuidor (String id_unico) throws RemoteException;
	
	public void listarOfertas () throws RemoteException;
	
	public void guardarUsuarios() throws RemoteException;
	
	public void guardarOfertas() throws RemoteException;
	
	public void guardarDemandas() throws RemoteException;
	
	public void guardarVentas() throws RemoteException;
	
	public void cargarDatos() throws RemoteException;

	public void ingresarDemanda(String id_cliente, int demanda)throws RemoteException;

	public void borrarDemanda(String id_cliente, int demanda)throws RemoteException;

	public void listarDemandas () throws RemoteException;
	
	public ArrayList<String> listarDemandasTipo (int tipo) throws RemoteException;
	
	public ArrayList<Integer> listarDemandasCliente (String id_unico) throws RemoteException;
	
	public void ingresarVenta(Venta venta) throws RemoteException;
	
	public ArrayList<Venta> listarVentasDistribuidor (String id_unico) throws RemoteException;
	
}
