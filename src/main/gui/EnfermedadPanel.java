package main.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class EnfermedadPanel extends JPanel {
    // Form fields
    private JTextField codigoField;
    private JTextField nombreComunField;
    private JTextField viaTransmisionField;
    private JTextField nombreCientificoField;
    private JTextField curadosField;
    private JTextField muertosField;
    private JTextField enfermosActivosField;

    // Buttons
    private JButton crearButton;
    private JButton actualizarButton;
    private JButton limpiarButton;

    // Table
    private JTable enfermedadTable;
    private DefaultTableModel tableModel;

    private Color panelBackgroundColor;
    private Color accentColor;
    private Color textColor;
    private Color borderColor;

    public EnfermedadPanel(Color panelBg, Color accent, Color text, Color border) {
        this.panelBackgroundColor = panelBg;
        this.accentColor = accent;
        this.textColor = text;
        this.borderColor = border;

        setLayout(new BorderLayout(10, 10));
        setBackground(panelBackgroundColor);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(panelBackgroundColor);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(borderColor, 1),
                "Datos de la Enfermedad",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                accentColor
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

        gbc.gridx = 0; gbc.gridy = 0; JLabel codigoLabel = new JLabel("Codigo (para buscar/actualizar):"); codigoLabel.setFont(labelFont); codigoLabel.setForeground(textColor); formPanel.add(codigoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; codigoField = new JTextField(20); styleTextField(codigoField); formPanel.add(codigoField, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; JLabel nombreComunLabel = new JLabel("Nombre Comun:"); nombreComunLabel.setFont(labelFont); nombreComunLabel.setForeground(textColor); formPanel.add(nombreComunLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; nombreComunField = new JTextField(20); styleTextField(nombreComunField); formPanel.add(nombreComunField, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; JLabel viaTransmisionLabel = new JLabel("Via Transmision (Tipo):"); viaTransmisionLabel.setFont(labelFont); viaTransmisionLabel.setForeground(textColor); formPanel.add(viaTransmisionLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; viaTransmisionField = new JTextField(20); styleTextField(viaTransmisionField); formPanel.add(viaTransmisionField, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 3; JLabel nombreCientificoLabel = new JLabel("Nombre Cientifico:"); nombreCientificoLabel.setFont(labelFont); nombreCientificoLabel.setForeground(textColor); formPanel.add(nombreCientificoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; nombreCientificoField = new JTextField(20); styleTextField(nombreCientificoField); formPanel.add(nombreCientificoField, gbc); gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 4; JLabel curadosLabel = new JLabel("Pacientes Curados:"); curadosLabel.setFont(labelFont); curadosLabel.setForeground(textColor); formPanel.add(curadosLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; curadosField = new JTextField(7); styleTextField(curadosField); formPanel.add(curadosField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; JLabel muertosLabel = new JLabel("Pacientes Fallecidos:"); muertosLabel.setFont(labelFont); muertosLabel.setForeground(textColor); formPanel.add(muertosLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5; muertosField = new JTextField(7); styleTextField(muertosField); formPanel.add(muertosField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; JLabel activosLabel = new JLabel("Pacientes Activos:"); activosLabel.setFont(labelFont); activosLabel.setForeground(textColor); formPanel.add(activosLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 6; enfermosActivosField = new JTextField(7); styleTextField(enfermosActivosField); formPanel.add(enfermosActivosField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(panelBackgroundColor);
        crearButton = new JButton("Registrar Enfermedad"); styleButton(crearButton);
        actualizarButton = new JButton("Actualizar Informacion"); styleButton(actualizarButton);
        limpiarButton = new JButton("Limpiar Formulario"); styleButton(limpiarButton, new Color(150,150,150)); // Gray

        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(limpiarButton);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        String[] columnNames = {"Codigo", "Nombre Comun", "Via Transmision", "Nombre Cientifico", "Curados", "Muertos", "Activos"};
        tableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        enfermedadTable = new JTable(tableModel);
        styleTable(enfermedadTable);
        
        JScrollPane scrollPane = new JScrollPane(enfermedadTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(new CompoundBorder(new LineBorder(borderColor, 1), new EmptyBorder(5, 5, 5, 5)));
        textField.setBackground(Color.WHITE);
        textField.setForeground(textColor);
    }
    
    private void styleButton(JButton button) {
        styleButton(button, accentColor);
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(bgColor.darker(), 1), 
            new EmptyBorder(8, 18, 8, 18)
        ));
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.setGridColor(borderColor.brighter());
        table.setSelectionBackground(accentColor.brighter());
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(accentColor);
        header.setForeground(Color.WHITE);
        header.setBorder(new LineBorder(accentColor.darker(),1));
        header.setReorderingAllowed(false);
    }

    public JTextField getCodigoField() { return codigoField; }
    public JTextField getNombreComunField() { return nombreComunField; }
    public JTextField getViaTransmisionField() { return viaTransmisionField; }
    public JTextField getNombreCientificoField() { return nombreCientificoField; }
    public JTextField getCuradosField() { return curadosField; }
    public JTextField getMuertosField() { return muertosField; }
    public JTextField getEnfermosActivosField() { return enfermosActivosField; }
    public JButton getCrearButton() { return crearButton; }
    public JButton getActualizarButton() { return actualizarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JTable getEnfermedadTable() { return enfermedadTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
}