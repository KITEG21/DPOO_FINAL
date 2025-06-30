package main.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class PersonaEnferma extends Persona {
    private boolean contagioExterior;
    private boolean enVigilancia;
    private boolean contactoExterior;
    private boolean enfermo;
    private String pais;
    private Date fechaContagio;
    private Enfermedad enfermedad;
    private List<VisitaPais> paisesVisitados;
    private List<PersonaContacto> contactos;


    public PersonaEnferma(String nombre, String apellidos, String carnetIdentidad) {
        super(nombre, apellidos, carnetIdentidad);
        paisesVisitados = new ArrayList<>();
        contactos = new ArrayList<PersonaContacto>();
    }

    public PersonaEnferma(Persona persona) {
        super(persona.getNombre(), persona.getApellidos(), persona.getCarnetIdentidad());
        this.setEdad(persona.getEdad());
        this.setSexo(persona.getSexo());
        this.setDireccion(persona.getDireccion());
        paisesVisitados = new ArrayList<>();
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

    public java.util.List<VisitaPais> getPaisesVisitados() {
        return paisesVisitados;
    }

    public void setPaisesVisitados(List<VisitaPais> paisesVisitados) {
        this.paisesVisitados = paisesVisitados;
    }

    public void limpiarPaisesVisitados() {
        if (this.paisesVisitados != null) {
            this.paisesVisitados.clear();
        }
    }

     public void setContactos(List<PersonaContacto> contactos) {
        this.contactos = contactos;
    }

    public List<PersonaContacto> getContactos() {
        return contactos;
    }

    public void agregarContacto(PersonaContacto contacto) {
        if (contactos == null) {
            contactos = new ArrayList<PersonaContacto>();
        }
        contactos.add(contacto);
    }

    public void eliminarContacto(String carnetContacto) {
        if (contactos != null) {
            contactos.removeIf(c -> c.getCarnetIdentidad().equals(carnetContacto));
        }
    }
}