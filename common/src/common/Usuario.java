package common;

import java.io.Serializable;

public class Usuario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6434454819428058040L;
	private String nombre, pass, dni;
	private boolean tipo; //si es true el usuario es un cliente, false usuario distribuidor
	
	public Usuario (String nombre, String pass, String dni, boolean tipo) {
		this.setNombre(nombre);
		this.setPass(pass);
		this.setDni(dni);
		this.setTipo(tipo);

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public boolean getTipo() {
		return tipo;
	}
	
	public void setTipo(boolean tipo) {
		this.tipo = tipo;		
	}
	
	public boolean esCliente() {
		return tipo;
	}
}
