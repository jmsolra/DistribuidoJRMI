package cliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.ClienteInterface;

public class ClienteImpl extends UnicastRemoteObject implements ClienteInterface{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2331979605140998990L;
	public ClienteImpl() throws RemoteException	{
		super( );
	}
	
	public String notificarme ( String mensaje ) {
		String returnMessage = "Hay una nueva oferta: \n" + mensaje;
		System.out.println(returnMessage);
		return returnMessage;
	}
	 
}
