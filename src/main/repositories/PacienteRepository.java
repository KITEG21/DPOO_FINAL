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
        Persona resultado = null;
        int i = 0;
        while (i < pacientes.size() && resultado == null) {
            if (pacientes.get(i).obtenerCodigo().equals(codigo)) {
                resultado = pacientes.get(i);
            }
            i++;
        }
        return resultado;
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
        boolean actualizado = false;
        int i = 0;
        while (i < pacientes.size() && !actualizado) {
            if (pacientes.get(i).obtenerCodigo().equals(persona.obtenerCodigo())) {
                pacientes.set(i, persona);
                actualizado = true;
            }
            i++;
        }
        return actualizado;
    }

    public boolean delete(String codigo) {
        boolean eliminado = false;
        int i = 0;
        while (i < pacientes.size() && !eliminado) {
            if (pacientes.get(i).obtenerCodigo().equals(codigo)) {
                pacientes.remove(i);
                eliminado = true;
            } else {
                i++;
            }
        }
        return eliminado;
    }
}