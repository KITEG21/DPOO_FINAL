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
    private JTextField codigoField;
    private JTextField nombreComunField;
    private JTextField viaTransmisionField;
    private JTextField nombreCientificoField;
    private JTextField curadosField;
    private JTextField muertosField;
    private JTextField enfermosActivosField;

    private JButton crearButton;
    private JButton actualizarButton;
    private JButton limpiarButton;

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
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

        // --- ROW 0: Codigo y Nombre Comun ---
        gbc.gridx = 0; gbc.gridy = 0; JLabel codigoLabel = new JLabel("Codigo:"); codigoLabel.setFont(labelFont); codigoLabel.setForeground(textColor); formPanel.add(codigoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; codigoField = new JTextField(10); styleTextField(codigoField); formPanel.add(codigoField, gbc);

        gbc.gridx = 2; gbc.gridy = 0; JLabel nombreComunLabel = new JLabel("Nombre Comun:"); nombreComunLabel.setFont(labelFont); nombreComunLabel.setForeground(textColor); formPanel.add(nombreComunLabel, gbc);
        gbc.gridx = 3; gbc.gridy = 0; nombreComunField = new JTextField(10); styleTextField(nombreComunField); formPanel.add(nombreComunField, gbc);

        // --- ROW 1: Via Transmision y Nombre Cientifico ---
        gbc.gridx = 0; gbc.gridy = 1; JLabel viaTransmisionLabel = new JLabel("Via Transmision:"); viaTransmisionLabel.setFont(labelFont); viaTransmisionLabel.setForeground(textColor); formPanel.add(viaTransmisionLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; viaTransmisionField = new JTextField(10); styleTextField(viaTransmisionField); formPanel.add(viaTransmisionField, gbc);

        gbc.gridx = 2; gbc.gridy = 1; JLabel nombreCientificoLabel = new JLabel("Nombre Cientifico:"); nombreCientificoLabel.setFont(labelFont); nombreCientificoLabel.setForeground(textColor); formPanel.add(nombreCientificoLabel, gbc);
        gbc.gridx = 3; gbc.gridy = 1; nombreCientificoField = new JTextField(10); styleTextField(nombreCientificoField); formPanel.add(nombreCientificoField, gbc);

        // --- ROW 2: Curados y Muertos ---
        gbc.gridx = 0; gbc.gridy = 2; JLabel curadosLabel = new JLabel("Curados:"); curadosLabel.setFont(labelFont); curadosLabel.setForeground(textColor); formPanel.add(curadosLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; curadosField = new JTextField(5); styleTextField(curadosField); formPanel.add(curadosField, gbc);

        gbc.gridx = 2; gbc.gridy = 2; JLabel muertosLabel = new JLabel("Muertos:"); muertosLabel.setFont(labelFont); muertosLabel.setForeground(textColor); formPanel.add(muertosLabel, gbc);
        gbc.gridx = 3; gbc.gridy = 2; muertosField = new JTextField(5); styleTextField(muertosField); formPanel.add(muertosField, gbc);

        // --- ROW 3: Enfermos Activos ---
        gbc.gridx = 0; gbc.gridy = 3; JLabel enfermosActivosLabel = new JLabel("Enfermos Activos:"); enfermosActivosLabel.setFont(labelFont); enfermosActivosLabel.setForeground(textColor); formPanel.add(enfermosActivosLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 3; enfermosActivosField = new JTextField(5); styleTextField(enfermosActivosField); formPanel.add(enfermosActivosField, gbc);
        gbc.gridwidth = 1;

        // --- ROW 4: Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(panelBackgroundColor);
        crearButton = new JButton("Crear"); styleButton(crearButton);
        actualizarButton = new JButton("Actualizar"); styleButton(actualizarButton);
        limpiarButton = new JButton("Limpiar"); styleButton(limpiarButton, new Color(150, 150, 150));

        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(limpiarButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        String[] columnNames = {"Codigo", "Nombre Comun", "Via Transmision", "Nombre Cientifico", "Curados", "Muertos", "Activos"};
        tableModel = new DefaultTableModel(columnNames, 0) {
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