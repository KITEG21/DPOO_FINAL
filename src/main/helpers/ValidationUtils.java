package main.helpers;

import java.awt.Component;
import java.util.Calendar;
import java.util.Date;

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
        if (!documento.matches("\\d{11}")) {
        throw new Exception("El documento debe tener exactamente 11 digitos numericos.");
    }
    }
    
    public static int validarEdad(String edadStr) throws Exception {
        if (edadStr == null || edadStr.trim().isEmpty()) {
            throw new Exception("La edad es obligatoria.");
        }
        
        try {
            int edad = Integer.parseInt(edadStr.trim());
            if (edad < 0 || edad > 150) {
                throw new Exception("La edad debe estar entre 0 y 150.");
            }
            return edad;
        } catch (NumberFormatException e) {
            throw new Exception("La edad debe ser un numero valido.");
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
    public static void validarRangoFecha(Date fecha, String nombreCampo) throws Exception {
        if (fecha == null) {
            throw new Exception("La fecha de " + nombreCampo + " es obligatoria.");
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        int anioFecha = calendar.get(Calendar.YEAR);
        int mesFecha = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH empieza en 0
        int diaFecha = calendar.get(Calendar.DAY_OF_MONTH);
        
        Calendar fechaActual = Calendar.getInstance();
        int anioActual = fechaActual.get(Calendar.YEAR);
        
        if (anioFecha <= 2000) {
            throw new Exception("La fecha de " + nombreCampo + " debe ser del siglo XXI (año mayor a 2000).");
        }
        
        if (anioFecha > anioActual) {
            throw new Exception("La fecha de " + nombreCampo + " no puede ser futura (" + anioActual + ").");
        }
        
        if (mesFecha < 1 || mesFecha > 12) {
            throw new Exception("El mes de " + nombreCampo + " debe estar entre 1 y 12.");
        }
        
        if (diaFecha < 1 || diaFecha > 31) {
            throw new Exception("El dia de " + nombreCampo + " debe estar entre 1 y 31.");
        }
        
        int diasEnMes = obtenerDiasEnMes(mesFecha, anioFecha);
        if (diaFecha > diasEnMes) {
            throw new Exception("El dia " + diaFecha + " no es valido para el mes " + mesFecha + " del " + anioFecha + ".");
        }
        
        if (fecha.after(fechaActual.getTime())) {
            throw new Exception("La fecha de " + nombreCampo + " no puede ser una fecha futura.");
        }
    }
    
    // Método para validar y parsear fechas
    public static Date validarYParsearFecha(String fechaStr, String nombreCampo) throws Exception {
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return null;
        }
        
        fechaStr = fechaStr.trim();
        
        // Validar formato básico con regex
        if (!fechaStr.matches("\\d{2}-\\d{2}-\\d{4}")) {
            throw new Exception("Formato de fecha invalido para " + nombreCampo + ". Use dd-MM-yyyy (ej: 15-06-2023).");
        }
        
        // Parsear los componentes manualmente
        String[] partes = fechaStr.split("-");
        int dia, mes, anio;
        
        try {
            dia = Integer.parseInt(partes[0]);
            mes = Integer.parseInt(partes[1]);
            anio = Integer.parseInt(partes[2]);
        } catch (NumberFormatException e) {
            throw new Exception("Formato de fecha invalido para " + nombreCampo + ". Use numeros validos.");
        }
        
        if (anio <= 2000) {
            throw new Exception("La fecha de " + nombreCampo + " debe ser del siglo XXI.");
        }
        
        Calendar fechaActual = Calendar.getInstance();
        int anioActual = fechaActual.get(Calendar.YEAR);
        
        if (anio > anioActual) {
            throw new Exception("La fecha de " + nombreCampo + " no puede ser mayor que " + anioActual + ".");
        }
        
        if (mes < 1 || mes > 12) {
            throw new Exception("El mes de " + nombreCampo + " debe estar entre 1 y 12.");
        }
        
        if (dia < 1 || dia > 31) {
            throw new Exception("El dia de " + nombreCampo + " debe estar entre 1 y 31.");
        }
        
        // Validar dias específicos por mes
        int diasEnMes = obtenerDiasEnMes(mes, anio);
        if (dia > diasEnMes) {
            throw new Exception("El dia " + dia + " no es valido para el mes " + mes + " del " + anio + ".");
        }
        
        // Crear la fecha usando Calendar para evitar problemas de parsing
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false); 
        calendar.set(Calendar.YEAR, anio);
        calendar.set(Calendar.MONTH, mes - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dia);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        Date fecha = calendar.getTime();
        
        // Validar que no sea futura
        if (fecha.after(fechaActual.getTime())) {
            throw new Exception("La fecha de " + nombreCampo + " no puede ser una fecha futura.");
        }
        
        return fecha;
    }

    private static int obtenerDiasEnMes(int mes, int anio) {
        switch (mes) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return esBisiesto(anio) ? 29 : 28;
            default:
                return 31; 
        }
    }
    
    private static boolean esBisiesto(int anio) {
        return (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0);
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