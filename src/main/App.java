package main;

import javax.swing.SwingUtilities;

import main.controller.SwingGuiController;
import main.gui.MainFrame;
import main.repositories.EnfermedadRepository;
import main.repositories.PacienteRepository;
import main.services.EnfermedadService;
import main.services.PacienteService;
import main.services.EstadisticasService;

public class App {
    public static void main(String[] args) {
    	
    	final PacienteRepository pacienteRepo = new PacienteRepository();
    	final EnfermedadRepository enfermedadRepo = new EnfermedadRepository();
    	
        final PacienteService pacienteService = new PacienteService(pacienteRepo);
        final EnfermedadService enfermedadService = new EnfermedadService(enfermedadRepo);
        final EstadisticasService estadisticasService = new EstadisticasService(pacienteService, enfermedadService);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                new SwingGuiController(mainFrame, pacienteService, enfermedadService, estadisticasService);
                mainFrame.setVisible(true);
            }
        });
    }
}