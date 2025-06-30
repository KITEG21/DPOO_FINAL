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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PacientePanel extends JPanel {
    private JTextField codigoField;
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField documentoField;
    private JTextField edadField;
    private JTextField fechaContagioField;
    private JComboBox<String> sexoComboBox;
    private JCheckBox contagioExteriorCheckBox;
    private JCheckBox enfermoCheckBox;
    private JTextField paisField;
    private JComboBox<String> enfermedadComboBox;
    private JLabel paisesVisitadosLabel;
    private JTextArea paisesVisitadosArea;
    private JScrollPane paisesVisitadosScrollPane;
    

    private JButton crearButton;
    private JButton actualizarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;

    private JLabel contactosLabel;
    private JTextArea contactosArea;
    private JScrollPane contactosScrollPane;

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
        setBackground(panelBackgroundColor);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(panelBackgroundColor); 
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
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

        // --- ROW 0: Codigo y Nombre ---
        gbc.gridx = 0; gbc.gridy = 0; JLabel codigoLabel = new JLabel("Codigo:"); codigoLabel.setFont(labelFont); codigoLabel.setForeground(textColor); formPanel.add(codigoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; codigoField = new JTextField(10); styleTextField(codigoField); formPanel.add(codigoField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; JLabel nombreLabel = new JLabel("Nombre:"); nombreLabel.setFont(labelFont); nombreLabel.setForeground(textColor); formPanel.add(nombreLabel, gbc);
        gbc.gridx = 3; gbc.gridy = 0; nombreField = new JTextField(10); styleTextField(nombreField); formPanel.add(nombreField, gbc);

        // --- ROW 1: Apellido y CI ---
        gbc.gridx = 0; gbc.gridy = 1; JLabel apellidoLabel = new JLabel("Apellido:"); apellidoLabel.setFont(labelFont); apellidoLabel.setForeground(textColor); formPanel.add(apellidoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; apellidoField = new JTextField(10); styleTextField(apellidoField); formPanel.add(apellidoField, gbc);

        gbc.gridx = 2; gbc.gridy = 1; JLabel documentoLabel = new JLabel("CI:"); documentoLabel.setFont(labelFont); documentoLabel.setForeground(textColor); formPanel.add(documentoLabel, gbc);
        gbc.gridx = 3; gbc.gridy = 1; documentoField = new JTextField(10); styleTextField(documentoField); formPanel.add(documentoField, gbc);
        
        // --- ROW 2: Edad y Sexo ---
        gbc.gridx = 0; gbc.gridy = 2; JLabel edadLabel = new JLabel("Edad:"); edadLabel.setFont(labelFont); edadLabel.setForeground(textColor); formPanel.add(edadLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; edadField = new JTextField(5); styleTextField(edadField); formPanel.add(edadField, gbc);

        gbc.gridx = 2; gbc.gridy = 2; JLabel sexoLabel = new JLabel("Sexo:"); sexoLabel.setFont(labelFont); sexoLabel.setForeground(textColor); formPanel.add(sexoLabel, gbc);
        gbc.gridx = 3; gbc.gridy = 2; sexoComboBox = new JComboBox<String>(new String[]{"M", "F"}); styleComboBox(sexoComboBox); formPanel.add(sexoComboBox, gbc);

        // --- ROW 3: Pais y Fecha Contagio ---
        gbc.gridx = 0; gbc.gridy = 3; JLabel paisLabel = new JLabel("Pais:"); paisLabel.setFont(labelFont); paisLabel.setForeground(textColor); formPanel.add(paisLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; paisField = new JTextField(10); styleTextField(paisField); formPanel.add(paisField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 3; JLabel fechaContagioLabel = new JLabel("Fecha Contagio:"); fechaContagioLabel.setFont(labelFont); fechaContagioLabel.setForeground(textColor); formPanel.add(fechaContagioLabel, gbc);
        gbc.gridx = 3; gbc.gridy = 3; fechaContagioField = new JTextField(10); styleTextField(fechaContagioField); fechaContagioField.setToolTipText("dd-MM-yyyy"); formPanel.add(fechaContagioField, gbc);

        // --- ROW 4: Enfermedad ---
        gbc.gridx = 0; gbc.gridy = 4; JLabel enfermedadLabel = new JLabel("Enfermedad:"); enfermedadLabel.setFont(labelFont); enfermedadLabel.setForeground(textColor); formPanel.add(enfermedadLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 3; enfermedadComboBox = new JComboBox<String>(); styleComboBox(enfermedadComboBox); formPanel.add(enfermedadComboBox, gbc); gbc.gridwidth = 1;

        // --- ROW 5: Checkboxes ---
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; contagioExteriorCheckBox = new JCheckBox("Contagio en el Exterior"); styleCheckBox(contagioExteriorCheckBox); formPanel.add(contagioExteriorCheckBox, gbc);
        gbc.gridx = 2; gbc.gridy = 5; gbc.gridwidth = 2; enfermoCheckBox = new JCheckBox("Actualmente Enfermo"); styleCheckBox(enfermoCheckBox); formPanel.add(enfermoCheckBox, gbc);
        gbc.gridwidth = 1;

        // --- ROW 6 & 7: Paises Visitados (Initially Hidden) ---
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 4;
        paisesVisitadosLabel = new JLabel("Paises visitados (pais,dias;...):");
        paisesVisitadosLabel.setFont(labelFont);
        paisesVisitadosLabel.setForeground(textColor);
        formPanel.add(paisesVisitadosLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 4;
        paisesVisitadosArea = new JTextArea(3, 20);
        styleTextArea(paisesVisitadosArea);
        paisesVisitadosScrollPane = new JScrollPane(paisesVisitadosArea);
        paisesVisitadosScrollPane.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        formPanel.add(paisesVisitadosScrollPane, gbc);
        gbc.gridwidth = 1;

         gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 4;
    contactosLabel = new JLabel("Contactos (nombre,apellido,ci,edad,sexo,fecha;...):");
    contactosLabel.setFont(labelFont);
    contactosLabel.setForeground(textColor);
    formPanel.add(contactosLabel, gbc);

    gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 4;
    contactosArea = new JTextArea(3, 20);
    styleTextArea(contactosArea);
    contactosScrollPane = new JScrollPane(contactosArea);
    contactosScrollPane.setBorder(BorderFactory.createLineBorder(borderColor, 1));
    formPanel.add(contactosScrollPane, gbc);
    gbc.gridwidth = 1;

    contactosLabel.setVisible(false);
    contactosScrollPane.setVisible(false);


        paisesVisitadosLabel.setVisible(false);
        paisesVisitadosScrollPane.setVisible(false);

        contagioExteriorCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boolean selected = contagioExteriorCheckBox.isSelected();
                paisesVisitadosLabel.setVisible(selected);
                paisesVisitadosScrollPane.setVisible(selected);
            }
        });
        enfermoCheckBox.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            boolean esEnfermo = enfermoCheckBox.isSelected();
            contactosLabel.setVisible(esEnfermo);
            contactosScrollPane.setVisible(esEnfermo);
        }
    });

        // --- ROW 8: Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(panelBackgroundColor);
        crearButton = new JButton("Crear"); styleButton(crearButton);
        actualizarButton = new JButton("Actualizar"); styleButton(actualizarButton);
        eliminarButton = new JButton("Eliminar"); styleButton(eliminarButton, new Color(220,90,90)); 
        limpiarButton = new JButton("Limpiar"); styleButton(limpiarButton, new Color(150,150,150)); 

        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(limpiarButton);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        String[] columnNames = {"Codigo", "Nombre", "Apellido", "CI", "Edad", "Sexo", "Pais", "Fecha Contagio", "Enfermo", "Extranjero", "Enfermedad"};
        tableModel = new DefaultTableModel(columnNames, 0){
            @Override 
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pacienteTable = new JTable(tableModel);
        styleTable(pacienteTable);
        
        JScrollPane scrollPane = new JScrollPane(pacienteTable);
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
    
    private void styleTextArea(JTextArea textArea) {
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(textColor);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    private void styleComboBox(JComboBox comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(textColor);
    }

    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        checkBox.setBackground(panelBackgroundColor);
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
    public JTextField getNombreField() { return nombreField; }
    public JTextField getApellidoField() { return apellidoField; }
    public JTextField getDocumentoField() { return documentoField; }
    public JTextField getEdadField() { return edadField; }
    public JComboBox<String> getSexoComboBox() { return sexoComboBox; }
    public JCheckBox getContagioExteriorCheckBox() { return contagioExteriorCheckBox; }
    public JCheckBox getEnfermoCheckBox() { return enfermoCheckBox; }
    public JTextField getPaisField() { return paisField; }
    public JComboBox<String> getEnfermedadComboBox() { return enfermedadComboBox; }
    public JTextArea getPaisesVisitadosAreal() { return paisesVisitadosArea; }
    public JButton getCrearButton() { return crearButton; }
    public JButton getActualizarButton() { return actualizarButton; }
    public JButton getEliminarButton() { return eliminarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JTable getPacienteTable() { return pacienteTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTextArea getPaisesVisitadosArea() { return paisesVisitadosArea; }
    public JLabel getPaisesVisitadosLabel() { return paisesVisitadosLabel; }
    public JScrollPane getPaisesVisitadosScrollPane() { return paisesVisitadosScrollPane; }
    public JTextField getFechaContagioField() { return fechaContagioField; }
     public JTextArea getContactosArea() { return contactosArea; }
    public JLabel getContactosLabel() { return contactosLabel; }
    public JScrollPane getContactosScrollPane() { return contactosScrollPane; }
}