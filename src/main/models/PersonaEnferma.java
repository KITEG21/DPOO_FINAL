package main.models;

import java.util.Date;
import java.util.List;

public class PersonaEnferma extends Persona {
	
	private Date fechaDiagnostico;
    private String tratamiento;
    private boolean enfermoEnExtranjero;
    private String lugarDiagnostico;
    private List<VisitaPais> visitasPaises;
    
    public PersonaEnferma(String nombre, String apellidos, int edad,
			String sexo, String direccion, String carnetIdentidad) {
		super(nombre, apellidos, edad, sexo, direccion, carnetIdentidad);
    }
}
