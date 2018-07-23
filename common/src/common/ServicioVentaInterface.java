package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioVentaInterface extends Remote{

	public void realizarCompra(Oferta compra, int numero, String id_cliente) throws RemoteException;

	public void guardarVenta(Oferta compra, String id_cliente) throws RemoteException;
	
	public void listarVentas (String id_dist) throws RemoteException;
}
