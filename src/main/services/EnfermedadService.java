package main.services;

import main.models.Enfermedad;
import main.repositories.EnfermedadRepository;
import java.util.List;

public class EnfermedadService {
    private EnfermedadRepository repo;

    public EnfermedadService(EnfermedadRepository repo) {
        this.repo = repo;
    }

    public void registrarEnfermedad(Enfermedad e) {
        if (e == null) {
            throw new IllegalArgumentException("Enfermedad no puede ser null");
        }
        if (e.getNombreComun() == null || e.getNombreComun().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la enfermedad no puede estar vacío");
        }
        if (repo.getByCodigo(e.obtenerCodigo()) == null) {
            repo.save(e);
        } else {
            throw new IllegalArgumentException("Enfermedad ya registrada");
        }
    }
    public Enfermedad getByCodigo(String codigo) {
        return repo.getByCodigo(codigo);
    }

    public List<Enfermedad> getAll() {
        return repo.getAll();
    }

    public void actualizarEnfermedad(Enfermedad e) {
        if (repo.getByCodigo(e.obtenerCodigo()) == null) {
            throw new IllegalArgumentException("Enfermedad no encontrada");
        }
        repo.update(e);
    }

    public void eliminarEnfermedad(String codigo) {
        if (repo.getByCodigo(codigo) == null) {
            throw new IllegalArgumentException("Enfermedad no encontrada");
        }
        repo.delete(codigo);
    }
}