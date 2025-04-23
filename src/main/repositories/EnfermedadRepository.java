package main.repositories;

import java.util.ArrayList;
import java.util.List;

import main.models.Enfermedad;

public class EnfermedadRepository {
	
	private List<Enfermedad> enfermedades = new ArrayList<>();

    public void save(Enfermedad enfermedad) {
        enfermedades.add(enfermedad);
    }

    public Enfermedad getByCodigo(final String codigo) {
    	return enfermedades.stream()
                .filter(new java.util.function.Predicate<Enfermedad>() {
                    @Override
                    public boolean test(Enfermedad e) {
                        return e.obtenerCodigo().equals(codigo);
                    }
                })
                .findFirst()
                .orElse(null);
    }
    
    //add CREATE, POST and DELETE
    
    
    
    
}
