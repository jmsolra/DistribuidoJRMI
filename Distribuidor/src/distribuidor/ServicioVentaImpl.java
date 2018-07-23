package distribuidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import common.Oferta;
import common.ServicioDatosInterface;
import common.ServicioMercanciasInterface;
import common.ServicioVentaInterface;
import common.Usuario;
import common.Utils;
import common.Venta;

public class ServicioVentaImpl extends UnicastRemoteObject implements ServicioVentaInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ServicioMercanciasInterface mercancia;
	private static ServicioDatosInterface datos;
	
	
	protected ServicioVentaImpl() throws RemoteException {
		super();
	}
	@Override
	public synchronized void realizarCompra(Oferta compra, int numero, String id_cliente) throws RemoteException {
		try {
			mercancia = (ServicioMercanciasInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"mercancia"));
		}
		catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("\t- El cliente: "+id_cliente+" ha comprado "+compra.getKilos()+"  kilos de "+Oferta.mostrarTipo(compra.getTipo())+" a "+compra.getPrecio()+" €");
		guardarVenta(compra,id_cliente);
		mercancia.quitarOferta(compra.getId_unico(), numero);
	}
	@Override
	public synchronized void guardarVenta(Oferta compra, String id_cliente) throws RemoteException {
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
			datos.ingresarVenta(new Venta(compra,id_cliente));
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void listarVentas (String id_dist) throws RemoteException{
		try {
			datos = (ServicioDatosInterface) Naming.lookup(Utils.getURL(Utils.direccionServidor, Utils.puerto,"datos"));
			ArrayList <Venta> lista_ventas = new ArrayList<Venta> (datos.listarVentasDistribuidor(id_dist));
			int contador = 1;
			System.out.println("- Has realizado "+lista_ventas.size()+" ventas");
			for (Venta venta : lista_ventas) {
					System.out.println ("\t"+contador+"- Vendió a: "+venta.getId_cliente()+" -> "+venta.getKilos()+" kilos de "+Venta.mostrarTipo(venta.getTipo())+" a "+venta.getPrecio()+" € el "+venta.getFecha());
					contador++;
			}
			System.out.println();
		
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		
	}
}