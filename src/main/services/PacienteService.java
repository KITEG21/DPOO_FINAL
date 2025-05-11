package main.services;

import java.util.List;
import main.models.Persona;
import main.repositories.PacienteRepository;

public class PacienteService {
    private PacienteRepository repo;

    public PacienteService(PacienteRepository repo) {
        this.repo = repo;
    }

    public void registrarPaciente(Persona p) {
        if (repo.getByCodigo(p.obtenerCodigo()) == null) {
            repo.save(p);
        } else {
            throw new IllegalArgumentException("Paciente ya registrado");
        }
    }

    public List<Persona> getAll() {
        return repo.getAll();
    }

    public void updatePaciente(Persona p) {
        if (repo.getByCodigo(p.obtenerCodigo()) == null) {
            throw new IllegalArgumentException("Paciente no encontrado");
        }
        repo.update(p);
    }

    public void deletePaciente(String codigo) {
        if (repo.getByCodigo(codigo) == null) {
            throw new IllegalArgumentException("Paciente no encontrado");
        }
        repo.delete(codigo);
    }
}