package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteInterface extends Remote{

	public String notificarme ( String mensaje ) throws RemoteException; 
	
}
