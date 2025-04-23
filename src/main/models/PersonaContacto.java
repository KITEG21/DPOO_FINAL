package main.models;

import java.util.Date;
import java.util.List;

public class PersonaContacto extends Persona {
	

	private Date fechaEntrevista;
    private List<String> analisisRequeridos;
    private boolean enVigilancia;
    private Date fechaFinVigilancia;
    
	public PersonaContacto(String nombre, String apellidos, int edad,
			String sexo, String direccion, String carnetIdentidad) {
		super(nombre, apellidos, edad, sexo, direccion, carnetIdentidad);
	}
}
