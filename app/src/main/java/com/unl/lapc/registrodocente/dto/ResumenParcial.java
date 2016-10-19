package com.unl.lapc.registrodocente.dto;

import java.util.HashMap;

/**
 * Modelo de presentación que representa el resumen de un parcial para un estudiante.
 */
public class ResumenParcial {

    private int id;
    private int numero;
    private String nombres;
    private double notaFinal;

    private HashMap<Integer, ResumenParcialAcreditable> acreditables = new HashMap<Integer, ResumenParcialAcreditable>();

    public ResumenParcial(int id, int numero, String nombres, double notaFinal){
        this.id = id;
        this.numero =  numero;
        this.nombres = nombres;
        this.notaFinal = notaFinal;

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

    public HashMap<Integer, ResumenParcialAcreditable> getAcreditables() {
        return acreditables;
    }

    public void setAcreditables(HashMap<Integer, ResumenParcialAcreditable> acreditables) {
        this.acreditables = acreditables;
    }
}
