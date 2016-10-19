package com.unl.lapc.registrodocente.dto;

import java.util.HashMap;

/**
 * Modelo de presentación que representa el resumen general de notas para un estudiante.
 */
public class ResumenGeneral {

    private int id;
    private int numero;
    private String nombres;
    private double notaFinal;
    private double asistencias;

    private HashMap<Integer, ResumenQuimestre> quimestres = new HashMap<Integer, ResumenQuimestre>();

    public ResumenGeneral(int id, int numero, String nombres, double notaFinal, double asistencias){
        this.id = id;
        this.numero =  numero;
        this.nombres = nombres;
        this.notaFinal = notaFinal;
        this.asistencias = asistencias;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public double getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(double asistencias) {
        this.asistencias = asistencias;
    }

    public HashMap<Integer, ResumenQuimestre> getQuimestres() {
        return quimestres;
    }

    public void setQuimestres(HashMap<Integer, ResumenQuimestre> quimestres) {
        this.quimestres = quimestres;
    }
}
