package main.models;

import java.util.Date;

public class PersonaEnferma extends Persona {
    private boolean contagioExterior;
    private boolean enVigilancia;
    private boolean contactoExterior;
    private boolean enfermo;
    private String pais;
    private Date fechaContagio;
    private Enfermedad enfermedad;
    
    public PersonaEnferma(String nombre, String apellidos, String carnetIdentidad) {
        super(nombre, apellidos, carnetIdentidad);
    }
    
    // También puedes añadir un constructor que acepte una Persona existente
    public PersonaEnferma(Persona persona) {
        super(persona.getNombre(), persona.getApellidos(), persona.getCarnetIdentidad());
        // Copiar otros atributos de Persona si es necesario
        this.setEdad(persona.getEdad());
        this.setSexo(persona.getSexo());
        this.setDireccion(persona.getDireccion());
    }
    
    // Getters y setters
    public boolean isContagioExterior() {
        return contagioExterior;
    }

    public void setContagioExterior(boolean contagioExterior) {
        this.contagioExterior = contagioExterior;
    }

    public boolean isEnVigilancia() {
        return enVigilancia;
    }

    public void setEnVigilancia(boolean enVigilancia) {
        this.enVigilancia = enVigilancia;
    }

    public boolean isContactoExterior() {
        return contactoExterior;
    }

    public void setContactoExterior(boolean contactoExterior) {
        this.contactoExterior = contactoExterior;
    }

    public boolean isEnfermo() {
        return enfermo;
    }

    public void setEnfermo(boolean enfermo) {
        this.enfermo = enfermo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Date getFechaContagio() {
        return fechaContagio;
    }

    public void setFechaContagio(Date fechaContagio) {
        this.fechaContagio = fechaContagio;
    }

    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(Enfermedad enfermedad) {
        this.enfermedad = enfermedad;
    }
}