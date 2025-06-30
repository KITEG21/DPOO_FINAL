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
        Enfermedad encontrada = null;
        int i = 0;
        while (encontrada == null && i < enfermedades.size()) {
            Enfermedad e = enfermedades.get(i);
            if (e.obtenerCodigo().equals(codigo)) {
                encontrada = e;
            }
            i++;
        }
        return encontrada;
    }

    public List<Enfermedad> getAll() {
        return new ArrayList<>(enfermedades);
    }

    public boolean update(Enfermedad enfermedad) {
        boolean actualizada = false;
        int i = 0;
        while (!actualizada && i < enfermedades.size()) {
            if (enfermedades.get(i).obtenerCodigo().equals(enfermedad.obtenerCodigo())) {
                enfermedades.set(i, enfermedad);
                actualizada = true;
            }
            i++;
        }
        return actualizada;
    }

    public boolean delete(String codigo) {
        boolean eliminada = false;
        int i = 0;
        while (!eliminada && i < enfermedades.size()) {
            if (enfermedades.get(i).obtenerCodigo().equals(codigo)) {
                enfermedades.remove(i);
                eliminada = true;
            } else {
                i++;
            }
        }
        return eliminada;
    }
}