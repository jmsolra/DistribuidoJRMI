package regulador;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.Usuario;
import common.Utils;
import common.ServicioAutenticacionInterface;
import common.ServicioDatosInterface;

public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface{



	/**
	 * 
	 */
	private static final long serialVersionUID = -6218206623813709625L;
	private static ServicioDatosInterface datos;
	
	protected ServicioAutenticacionImpl() throws RemoteException {
		super();
	}
	@Override
	public synchronized String autenticarUser(Usuario usuario) throws RemoteException {
		String id_unico= Utils.generarID(usuario.getDni());
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
			
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (datos.registrarUsuario(usuario,id_unico)){
			return id_unico;
		}
		else {
			return null;
		}
	}

	@Override
	public synchronized String loguearUser(String nombre, String pass, Boolean tipo) throws RemoteException {
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		String id_unico = datos.loguearUsuario(nombre,pass,tipo);
			return id_unico;
	}
	@Override
	public synchronized void desloguearUser(String id_unico) throws RemoteException {
		datos.desloguearUsuario(id_unico);
	}
	@Override
	public synchronized void bajaUser(String id_unico) throws RemoteException {
		datos.bajaUsuario(id_unico);
	}

}
