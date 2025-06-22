package main.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class EstadisticasPanel extends JPanel {
    private JComboBox<String> estadisticaComboBox;
    private JButton mostrarButton;
    private JTextArea resultadoArea;
    private JPanel chartPanelPlaceholder; 

    private Color panelBackgroundColor;
    private Color accentColor;
    private Color textColor;
    private Color borderColor;
    private Color textAreaBgColor = new Color(250, 255, 255);

    public EstadisticasPanel(Color panelBg, Color accent, Color text, Color border) {
        this.panelBackgroundColor = panelBg;
        this.accentColor = accent;
        this.textColor = text;
        this.borderColor = border;

        setLayout(new BorderLayout(15, 15));
        setBackground(panelBackgroundColor);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(panelBackgroundColor);
        topPanel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(borderColor, 1),
            "Seleccion de Reporte Estadistico",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 14),
            accentColor
        ));

        JLabel selectLabel = new JLabel("Tipo de Estadistica:");
        selectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        selectLabel.setForeground(textColor);
        topPanel.add(selectLabel);

        estadisticaComboBox = new JComboBox<String>(new String[]{
                "Estadisticas Generales",
                "Pacientes por Sexo",
                "Enfermedades por Tipo de Transmision",
                "Contagios Nacionales vs. Extranjero",
                "Casos por Rango de Edad y Sexo",
                "Estadisticas por Enfermedad (curados/fallecidos/enfermos)",
                "Contactos de Enfermos en el Exterior",
                "Enfermos por Pais y Mes (requiere input)",
        });
        styleComboBox(estadisticaComboBox);
        topPanel.add(estadisticaComboBox);

        mostrarButton = new JButton("Generar Reporte");
        styleButton(mostrarButton);
        topPanel.add(mostrarButton);

        add(topPanel, BorderLayout.NORTH);

        resultadoArea = new JTextArea(20, 70);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultadoArea.setBackground(textAreaBgColor);
        resultadoArea.setForeground(textColor.darker());
        resultadoArea.setBorder(new CompoundBorder(
            new LineBorder(borderColor, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        scrollPane.setBorder(null);
///
        add(scrollPane, BorderLayout.CENTER);


        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, chartPanelPlaceholder);
        splitPane.setResizeWeight(0.85); 
        splitPane.setBorder(null);
        splitPane.setDividerSize(8);
        splitPane.setEnabled(false); 

        add(splitPane, BorderLayout.CENTER);
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(textColor);
    }
    
    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(accentColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(accentColor.darker(), 1), 
            new EmptyBorder(8, 18, 8, 18)
        ));
    }

    public JComboBox<String> getEstadisticaComboBox() { return estadisticaComboBox; }
    public JButton getMostrarButton() { return mostrarButton; }
    public JTextArea getResultadoArea() { return resultadoArea; }
    public JPanel getChartPanel() { return chartPanelPlaceholder; } 
}