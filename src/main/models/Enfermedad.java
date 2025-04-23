package main.models;

import java.util.Map;

import main.helpers.Codificador;


public class Enfermedad implements Codificador {
	private String nombreComun;
	private String nombreCientifico;
	private String viaTransmision;
	private int periodoIncubacion;
	private int cantPacientes;
    private int curados;
    private int muertos;
    private int enfermosActivos;
    
    private Map<String, Integer> pacientesPorSexo;
    private Map<String, Integer> pacientesPorRangoEdad;
	
	// Constructor
	public Enfermedad(String nombreComun, String nombreCientifico, String viaTransmision, int periodoIncubacion, int cantPacientes){
		setNombreComun(nombreComun);
		setNombreCientifico(nombreCientifico);
		setViaTransmision(viaTransmision);
		setPeriodoIncubacion(periodoIncubacion);
		setCantPacientes(cantPacientes);
	}

	// Getters y Setters
	public String getNombreComun() {
		return nombreComun;
	}

	public void setNombreComun(String nombreComun) {
		this.nombreComun = nombreComun;
	}

	public String getNombreCientifico() {
		return nombreCientifico;
	}

	public void setNombreCientifico(String nombreCientifico) {
		this.nombreCientifico = nombreCientifico;
	}

	public String getViaTransmision() {
		return viaTransmision;
	}

	public void setViaTransmision(String viaTransmision) {
		this.viaTransmision = viaTransmision;
	}

	public int getPeriodoIncubacion() {
		return periodoIncubacion;
	}

	public void setPeriodoIncubacion(int periodoIncubacion) {
		this.periodoIncubacion = periodoIncubacion;
	}

	public int getCantPacientes() {
		return cantPacientes;
	}

	public void setCantPacientes(int cantPacientes) {
		this.cantPacientes = cantPacientes;
	}

	public int getCurados() {
		return curados;
	}

	public void setCurados(int curados) {
		this.curados = curados;
	}

	public int getMuertos() {
		return muertos;
	}

	public void setMuertos(int muertos) {
		this.muertos = muertos;
	}

	public int getEnfermosActivos() {
		return enfermosActivos;
	}

	public void setEnfermosActivos(int enfermosActivos) {
		this.enfermosActivos = enfermosActivos;
	}

	@Override
	public String obtenerCodigo() {
		return nombreCientifico;
	}
	
	
	
}
