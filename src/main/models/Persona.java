package main.models;

import main.helpers.Codificador;


public class Persona implements Codificador {
	private String nombre;
	private String apellidos;
	private int edad;
	private String sexo;
	private String direccion;
	private String carnetIdentidad;
	
	// Constructor
	public Persona(String nombre, String apellidos, int edad, String sexo, String direccion, String carnetIdentidad){
		setNombre(nombre);
		setApellidos(apellidos);
		setEdad(edad);
		setSexo(sexo);
		setDireccion(direccion);
		setCarnetIdentidad(carnetIdentidad);
	}
	public Persona(String nombre, String apellido, String carnetIdentidad) {
		setNombre(nombre);
		setApellidos(apellidos);
		setCarnetIdentidad(carnetIdentidad);

    }
	
	// Getters y Setters
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCarnetIdentidad() {
		return carnetIdentidad;
	}

	public void setCarnetIdentidad(String carnetIdentidad) {
		this.carnetIdentidad = carnetIdentidad;
	}

	@Override
	public String obtenerCodigo() {
		return carnetIdentidad;
	}
	
	
	
}
