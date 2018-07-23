package common;

import java.util.Date;

public class Venta extends Oferta {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6469513882223378625L;
	private Date fecha;
	private String id_cliente;
	
	public Venta(String id_unico, int tipo, int kilos, double precio,String id_cliente) {
		super(id_unico, tipo, kilos, precio);
		this.setFecha(new Date());
		this.setId_cliente(id_cliente);
	}

	public Venta(Oferta compra,String id_cliente) {
		super(compra.getId_unico(),compra.getTipo(),compra.getKilos(),compra.getPrecio());
		this.setFecha(new Date());
		this.setId_cliente(id_cliente);
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}
	
	
}
