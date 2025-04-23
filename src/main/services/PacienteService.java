package main.services;

import main.models.Persona;
import main.repositories.PacienteRepository;

public class PacienteService {
	private PacienteRepository repo;
	
	public void registrarPaciente(Persona p){
		if(repo.getByCodigo(p.obtenerCodigo()) == null){
			repo.save(p);
		}else{
			throw new IllegalArgumentException("Persona ya registrada");
		}
	}
}
