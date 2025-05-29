package main.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CargaDatosPanel extends JPanel {
    private JButton cargarDatosButton;
    private JTextArea logArea;

    private Color panelBackgroundColor;
    private Color accentColor;
    private Color textColor;
    private Color borderColor; 

    public CargaDatosPanel(Color panelBg, Color accent, Color text, Color border) {
        this.panelBackgroundColor = panelBg;
        this.accentColor = accent;
        this.textColor = text;
        this.borderColor = border; 

        setLayout(new BorderLayout(10, 10));
        setBackground(panelBackgroundColor);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topButtonPanel.setBackground(panelBackgroundColor);
        topButtonPanel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(borderColor, 1), 
            "Operaciones de Datos",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 14),
            accentColor
        ));

        cargarDatosButton = new JButton("Cargar Conjunto de Datos de Prueba");
        styleButton(cargarDatosButton);
        topButtonPanel.add(cargarDatosButton);
        
        add(topButtonPanel, BorderLayout.NORTH);

        logArea = new JTextArea(15, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 13)); 
        logArea.setBackground(new Color(250, 255, 255)); 
        logArea.setForeground(textColor.darker());
        logArea.setLineWrap(true); 
        logArea.setWrapStyleWord(true); 
        logArea.setBorder(new CompoundBorder(
            new LineBorder(borderColor, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(null);
        
        add(scrollPane, BorderLayout.CENTER);
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

    // Getters
    public JButton getCargarDatosButton() { return cargarDatosButton; }
    public JTextArea getLogArea() { return logArea; }
}