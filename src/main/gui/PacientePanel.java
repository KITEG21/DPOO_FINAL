package main.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class PacientePanel extends JPanel {
    // Form fields
    private JTextField codigoField;
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField documentoField;
    private JTextField edadField;
    private JComboBox<String> sexoComboBox;
    private JCheckBox contagioExteriorCheckBox;
    private JCheckBox enfermoCheckBox;
    private JTextField paisField;
    private JComboBox<String> enfermedadComboBox;

    // Buttons
    private JButton crearButton;
    private JButton actualizarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;

    // Table
    private JTable pacienteTable;
    private DefaultTableModel tableModel;

    private Color panelBackgroundColor;
    private Color accentColor;
    private Color textColor;
    private Color borderColor;

    public PacientePanel(Color panelBg, Color accent, Color text, Color border) {
        this.panelBackgroundColor = panelBg;
        this.accentColor = accent;
        this.textColor = text;
        this.borderColor = border;

        setLayout(new BorderLayout(10, 10));
        setBackground(panelBackgroundColor); // Use the passed background color
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(panelBackgroundColor); // Form panel background
        formPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(borderColor, 1),
                "Datos del Paciente",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                accentColor // Title color
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 8, 8, 8); // Increased insets
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Styling JLabels
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

        gbc.gridx = 0; gbc.gridy = 0; JLabel codigoLabel = new JLabel("Codigo (para buscar/actualizar):"); codigoLabel.setFont(labelFont); codigoLabel.setForeground(textColor); formPanel.add(codigoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; codigoField = new JTextField(20); styleTextField(codigoField); formPanel.add(codigoField, gbc); gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1; JLabel nombreLabel = new JLabel("Nombre:"); nombreLabel.setFont(labelFont); nombreLabel.setForeground(textColor); formPanel.add(nombreLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; nombreField = new JTextField(20); styleTextField(nombreField); formPanel.add(nombreField, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; JLabel apellidoLabel = new JLabel("Apellido:"); apellidoLabel.setFont(labelFont); apellidoLabel.setForeground(textColor); formPanel.add(apellidoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; apellidoField = new JTextField(20); styleTextField(apellidoField); formPanel.add(apellidoField, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 3; JLabel documentoLabel = new JLabel("Documento:"); documentoLabel.setFont(labelFont); documentoLabel.setForeground(textColor); formPanel.add(documentoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; documentoField = new JTextField(20); styleTextField(documentoField); formPanel.add(documentoField, gbc); gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 4; JLabel edadLabel = new JLabel("Edad:"); edadLabel.setFont(labelFont); edadLabel.setForeground(textColor); formPanel.add(edadLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; edadField = new JTextField(5); styleTextField(edadField); formPanel.add(edadField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; JLabel sexoLabel = new JLabel("Sexo:"); sexoLabel.setFont(labelFont); sexoLabel.setForeground(textColor); formPanel.add(sexoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5; sexoComboBox = new JComboBox<String>(new String[]{"M", "F", "Otro"}); styleComboBox(sexoComboBox); formPanel.add(sexoComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 6; JLabel paisLabel = new JLabel("Pais:"); paisLabel.setFont(labelFont); paisLabel.setForeground(textColor); formPanel.add(paisLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 2; paisField = new JTextField(20); styleTextField(paisField); formPanel.add(paisField, gbc); gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 7; JLabel enfermedadLabel = new JLabel("Enfermedad Asignada:"); enfermedadLabel.setFont(labelFont); enfermedadLabel.setForeground(textColor); formPanel.add(enfermedadLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 7; gbc.gridwidth = 2; enfermedadComboBox = new JComboBox<String>(); styleComboBox(enfermedadComboBox); formPanel.add(enfermedadComboBox, gbc); gbc.gridwidth = 1;

        // Checkboxes on the right side or below
        gbc.gridx = 0; gbc.gridy = 8; contagioExteriorCheckBox = new JCheckBox("Contagio en el Exterior"); styleCheckBox(contagioExteriorCheckBox); formPanel.add(contagioExteriorCheckBox, gbc);
        gbc.gridx = 1; gbc.gridy = 8; enfermoCheckBox = new JCheckBox("Actualmente Enfermo"); styleCheckBox(enfermoCheckBox); formPanel.add(enfermoCheckBox, gbc);


        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(panelBackgroundColor);
        crearButton = new JButton("Crear Paciente"); styleButton(crearButton);
        actualizarButton = new JButton("Actualizar Datos"); styleButton(actualizarButton);
        eliminarButton = new JButton("Eliminar Paciente"); styleButton(eliminarButton, new Color(220,90,90)); // Reddish for delete
        limpiarButton = new JButton("Limpiar Campos"); styleButton(limpiarButton, new Color(150,150,150)); // Gray for clear

        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(limpiarButton);

        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Codigo", "Nombre", "Apellido", "Documento", "Edad", "Sexo", "Pais", "Enfermo", "Extranjero", "Enfermedad"};
        tableModel = new DefaultTableModel(columnNames, 0){
            @Override //Non-editable cells
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pacienteTable = new JTable(tableModel);
        styleTable(pacienteTable);
        
        JScrollPane scrollPane = new JScrollPane(pacienteTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        scrollPane.getViewport().setBackground(Color.WHITE); // Background for the viewport behind the table
        add(scrollPane, BorderLayout.CENTER);
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(new CompoundBorder(new LineBorder(borderColor, 1), new EmptyBorder(5, 5, 5, 5)));
        textField.setBackground(Color.WHITE);
        textField.setForeground(textColor);
    }
    
    private void styleComboBox(JComboBox comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(textColor);
        // For Nimbus, arrow button color might need UIManager properties
    }

    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        checkBox.setBackground(panelBackgroundColor); // Match panel background
        checkBox.setForeground(textColor);
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
        // Optional: Add hover effects if desired (requires MouseListener)
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


    // Getters for controller (no changes needed here)
    public JTextField getCodigoField() { return codigoField; }
    public JTextField getNombreField() { return nombreField; }
    public JTextField getApellidoField() { return apellidoField; }
    public JTextField getDocumentoField() { return documentoField; }
    public JTextField getEdadField() { return edadField; }
    public JComboBox<String> getSexoComboBox() { return sexoComboBox; }
    public JCheckBox getContagioExteriorCheckBox() { return contagioExteriorCheckBox; }
    public JCheckBox getEnfermoCheckBox() { return enfermoCheckBox; }
    public JTextField getPaisField() { return paisField; }
    public JComboBox<String> getEnfermedadComboBox() { return enfermedadComboBox; }
    public JButton getCrearButton() { return crearButton; }
    public JButton getActualizarButton() { return actualizarButton; }
    public JButton getEliminarButton() { return eliminarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JTable getPacienteTable() { return pacienteTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
}