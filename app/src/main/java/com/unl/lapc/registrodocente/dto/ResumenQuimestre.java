package com.unl.lapc.registrodocente.dto;

import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Modelo de presentación que representa el resumen de un quimestre para un estudiante.
 */
public class ResumenQuimestre {

    private int id;
    private int numero;
    private String nombres;
    private double notaParciales;
    private double notaExamenes;
    private double notaFinal;

    private HashMap<Integer, Double> parciales = new HashMap<Integer, Double>();

    public ResumenQuimestre(int id, int numero, String nombres, double notaParciales, double notaExamenes, double notaFinal){
        this.id = id;
        this.numero =  numero;
        this.nombres = nombres;
        this.notaParciales = notaParciales;
        this.notaExamenes = notaExamenes;
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

    public double getNotaParciales() {
        return notaParciales;
    }

    public void setNotaParciales(double notaParciales) {
        this.notaParciales = notaParciales;
    }

    public double getNotaExamenes() {
        return notaExamenes;
    }

    public void setNotaExamenes(double notaExamenes) {
        this.notaExamenes = notaExamenes;
    }

    public double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public HashMap<Integer, Double> getParciales() {
        return parciales;
    }

    public void setParciales(HashMap<Integer, Double> parciales) {
        this.parciales = parciales;
    }
}
