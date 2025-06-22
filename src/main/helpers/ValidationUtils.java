package main.helpers;

import java.awt.Component;
import javax.swing.JOptionPane;


public class ValidationUtils {
    
    public static void validarNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre es obligatorio.");
        }
        if (nombre.trim().length() < 2) {
            throw new Exception("El nombre debe tener al menos 2 caracteres.");
        }
    }
    
    public static void validarApellido(String apellido) throws Exception {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new Exception("El apellido es obligatorio.");
        }
        if (apellido.trim().length() < 2) {
            throw new Exception("El apellido debe tener al menos 2 caracteres.");
        }
    }
    
    public static void validarDocumento(String documento) throws Exception {
        if (documento == null || documento.trim().isEmpty()) {
            throw new Exception("El documento es obligatorio.");
        }
        if (!documento.matches("\\d{8}[A-Za-z]")) {
            throw new Exception("Formato de documento inválido. Use: 12345678A");
        }
    }
    
    public static int validarEdad(String edadStr) throws Exception {
        if (edadStr == null || edadStr.trim().isEmpty()) {
            throw new Exception("La edad es obligatoria.");
        }
        
        try {
            int edad = Integer.parseInt(edadStr.trim());
            if (edad < 0 || edad > 150) {
                throw new Exception("La edad debe estar entre 0 y 150 años.");
            }
            return edad;
        } catch (NumberFormatException e) {
            throw new Exception("La edad debe ser un número válido.");
        }
    }
    
    public static void validarEnfermedadNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre de la enfermedad es obligatorio.");
        }
        if (nombre.trim().length() < 3) {
            throw new Exception("El nombre de la enfermedad debe tener al menos 3 caracteres.");
        }
    }
    
    public static int validarNumeroEntero(String numeroStr, String nombreCampo) throws Exception {
        if (numeroStr == null || numeroStr.trim().isEmpty()) {
            throw new Exception("El campo " + nombreCampo + " es obligatorio.");
        }
        
        try {
            int numero = Integer.parseInt(numeroStr.trim());
            if (numero < 0) {
                throw new Exception("El campo " + nombreCampo + " no puede ser negativo.");
            }
            return numero;
        } catch (NumberFormatException e) {
            throw new Exception("El campo " + nombreCampo + " debe ser un numero válido.");
        }
    }
    
    public static void validarCampoObligatorio(String valor, String nombreCampo) throws Exception {
        if (valor == null || valor.trim().isEmpty()) {
            throw new Exception("El campo " + nombreCampo + " es obligatorio.");
        }
    }
    
    public static void mostrarError(String mensaje, Component parent) {
        JOptionPane.showMessageDialog(parent, mensaje, "Error de Validacion", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void mostrarExito(String mensaje, Component parent) {
        JOptionPane.showMessageDialog(parent, mensaje, "Exito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void mostrarAdvertencia(String mensaje, Component parent) {
        JOptionPane.showMessageDialog(parent, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}