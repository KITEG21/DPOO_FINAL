package main.repositories;

import java.util.ArrayList;
import java.util.List;
import main.models.Enfermedad;

public class EnfermedadRepository {
    private List<Enfermedad> enfermedades = new ArrayList<>();

    public Enfermedad save(Enfermedad enfermedad) {
        enfermedades.add(enfermedad);
        return enfermedad;
    }

    public Enfermedad getByCodigo(String codigo) {
        for (Enfermedad e : enfermedades) {
            if (e.obtenerCodigo().equals(codigo)) {
                return e;
            }
        }
        return null;
    }

    public List<Enfermedad> getAll() {
        return new ArrayList<>(enfermedades);
    }

    public boolean update(Enfermedad enfermedad) {
        for (int i = 0; i < enfermedades.size(); i++) {
            if (enfermedades.get(i).obtenerCodigo().equals(enfermedad.obtenerCodigo())) {
                enfermedades.set(i, enfermedad);
                return true;
            }
        }
        return false;
    }

    public boolean delete(String codigo) {
        for (int i = 0; i < enfermedades.size(); i++) {
            if (enfermedades.get(i).obtenerCodigo().equals(codigo)) {
                enfermedades.remove(i);
                return true;
            }
        }
        return false;
    }
}