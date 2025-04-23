package main.repositories;

import java.util.ArrayList;
import java.util.List;
import main.models.Persona;

public class PacienteRepository {
	
	private List<Persona> pacientes = new ArrayList<>();

	public void save(Persona persona) {
        pacientes.add(persona);
    }

    public Persona getByCodigo(final String codigo) {
    	return pacientes.stream()
                .filter(new java.util.function.Predicate<Persona>() {
                    @Override
                    public boolean test(Persona e) {
                        return e.obtenerCodigo().equals(codigo);
                    }
                })
                .findFirst()
                .orElse(null);
    }
    
    //Add CREATE, POST & DELETE
}
