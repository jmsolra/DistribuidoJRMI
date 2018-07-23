package common;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ServicioAutenticacionInterface extends Remote{

	public String autenticarUser (Usuario usuario) throws RemoteException;
		
	public String loguearUser (String nombre, String pass, Boolean tipo) throws RemoteException;
	
	public void desloguearUser (String id_unico) throws RemoteException;
	
	public void bajaUser (String id_unico) throws RemoteException;
}
