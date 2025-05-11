import main.controller.ConsoleController;
import main.repositories.EnfermedadRepository;
import main.repositories.PacienteRepository;
import main.services.EnfermedadService;
import main.services.EstadisticasService;
import main.services.PacienteService;

public class App {
    public static void main(String[] args) {
        EnfermedadRepository enfermedadRepo = new EnfermedadRepository();
        EnfermedadService enfermedadService = new EnfermedadService(enfermedadRepo);

        PacienteRepository pacienteRepo = new PacienteRepository();
        PacienteService pacienteService = new PacienteService(pacienteRepo);
        
        // Crear el servicio de estadísticas
        EstadisticasService estadisticasService = new EstadisticasService(pacienteService, enfermedadService);

        ConsoleController controller = new ConsoleController(estadisticasService, enfermedadService, pacienteService);

        System.out.println("Iniciando sistema de gestion medica...");
        controller.start();
        System.out.println("Sistema finalizado.");
    }
}