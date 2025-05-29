package main.repositories;

import java.util.ArrayList;
import java.util.List;

import main.models.Persona;
import main.models.PersonaEnferma;

public class PacienteRepository {
    private List<Persona> pacientes = new ArrayList<>();

    public Persona save(Persona persona) {
        pacientes.add(persona);
        return persona;
    }

    public Persona getByCodigo(String codigo) {
        for (Persona p : pacientes) {
            if (p.obtenerCodigo().equals(codigo)) {
                return p;
            }
        }
        return null;
    }

    public List<Persona> getAll() {
        return new ArrayList<>(pacientes);
    }
    
    public List<Persona> getAllSick(){
    	ArrayList<Persona> pacientesEnfermos = new ArrayList<>();
    	for(Persona p : pacientes){
    		if(p instanceof PersonaEnferma){
    			pacientesEnfermos.add(p);
    		}
    	}
    	return pacientesEnfermos;

    }
    
    public boolean update(Persona persona) {
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).obtenerCodigo().equals(persona.obtenerCodigo())) {
                pacientes.set(i, persona);
                return true;
            }
        }
        return false;
    }

    public boolean delete(String codigo) {
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).obtenerCodigo().equals(codigo)) {
                pacientes.remove(i);
                return true;
            }
        }
        return false;
    }
}