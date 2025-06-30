package main.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private PacientePanel pacientePanel;
    private EnfermedadPanel enfermedadPanel;
    private EstadisticasPanel estadisticasPanel;
    private CargaDatosPanel cargaDatosPanel;

    private Color lightCyanBackground = new Color(230, 250, 250); 
    private Color mediumCyanAccent = new Color(0, 170, 170);    
    private Color darkCyanText = new Color(0, 80, 80);
    private Color lightGrayPanelBg = new Color(245, 250, 250); 
    private Color mediumGrayBorder = new Color(190, 200, 200);
    private Color headerTextColor = Color.WHITE;
    private Color tabSelectedTextColor = Color.WHITE;
    private Color tabUnselectedTextColor = darkCyanText;


    public MainFrame() {
        setTitle("CET-POO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 860);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());
        setResizable(false);

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        getContentPane().setBackground(lightCyanBackground);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));


        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        headerPanel.setBackground(mediumCyanAccent);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JLabel titleLabel = new JLabel("Sistema de Gestion Medica Avanzado (CET-POO)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(headerTextColor);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        pacientePanel = new PacientePanel(lightGrayPanelBg, mediumCyanAccent, darkCyanText, mediumGrayBorder);
        tabbedPane.addTab("Gestion de Pacientes", pacientePanel);

        enfermedadPanel = new EnfermedadPanel(lightGrayPanelBg, mediumCyanAccent, darkCyanText, mediumGrayBorder);
        tabbedPane.addTab("Gestion de Enfermedades", enfermedadPanel);

        estadisticasPanel = new EstadisticasPanel(lightGrayPanelBg, mediumCyanAccent, darkCyanText, mediumGrayBorder);
        tabbedPane.addTab("Analisis Estadistico", estadisticasPanel);
        
        cargaDatosPanel = new CargaDatosPanel(lightGrayPanelBg, mediumCyanAccent, darkCyanText, mediumGrayBorder);
        tabbedPane.addTab("Cargar Datos de Prueba", cargaDatosPanel);

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setBackgroundAt(i, lightCyanBackground); 
            tabbedPane.setForegroundAt(i, tabUnselectedTextColor);  
        }

        add(tabbedPane, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(mediumCyanAccent.darker()); 
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JLabel statusLabel = new JLabel("Copyright 2025.");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(headerTextColor);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }

    public PacientePanel getPacientePanel() {
        return pacientePanel;
    }

    public EnfermedadPanel getEnfermedadPanel() {
        return enfermedadPanel;
    }

    public EstadisticasPanel getEstadisticasPanel() {
        return estadisticasPanel;
    }

    public CargaDatosPanel getCargaDatosPanel() {
        return cargaDatosPanel;
    }
}