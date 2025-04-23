package main.services;

import main.models.Enfermedad;
import main.repositories.EnfermedadRepository;

public class EnfermedadService {
	private EnfermedadRepository repo;
	
	public void registrarEnfermedad(Enfermedad e){
		if(repo.getByCodigo(e.obtenerCodigo()) == null){
			repo.save(e);
		}else{
			throw new IllegalArgumentException("Enfermedad ya registrada");
		}
	}
	
	//Add CREATE, POST and DELETE
}
