package common;

import java.io.Serializable;

public class Oferta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8820986259850927941L;
	
	private String id_unico;
	private int tipo, kilos;
	private double precio;
	
	public Oferta(String id_unico, int tipo, int kilos, double precio) {
		super();
		this.setId_unico(id_unico);
		this.tipo = tipo;
		this.kilos = kilos;
		this.precio = precio;
	}
	
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public int getKilos() {
		return kilos;
	}
	public void setKilos(int kilos) {
		this.kilos = kilos;
	}

	public String getId_unico() {
		return id_unico;
	}

	public void setId_unico(String id_unico) {
		this.id_unico = id_unico;
	}
	
	public static String mostrarTipo (int tipo) {
		String producto = null;
		switch (tipo) {
		case 1:
			producto = "Tomates";
			break;
		case 2:
			producto = "Limones";
			break;
		case 3:
			producto = "Naranjas";
			break;
		case 4:
			producto = "Fresas";
			break;
		case 5:
			producto = "Platanos";
			break;
		case 6:
			producto = "Melones";
			break;
		case 7:
			producto = "Sandias";
			break;
		}
		return producto;
	}

}
