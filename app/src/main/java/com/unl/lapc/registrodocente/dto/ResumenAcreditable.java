package com.unl.lapc.registrodocente.dto;

import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Utils;

import java.util.HashMap;

/**
 * Created by Usuario on 20/08/2016.
 */
public class ResumenAcreditable {

    private int id;
    private int estudianteId;
    private int numero;
    private String nombres;

    private double notaPromedio;
    private double notaFinal;

    private HashMap<Integer, ResumenParcialAcreditable> acreditables = new HashMap<Integer, ResumenParcialAcreditable>();

    public ResumenAcreditable(int id, int estudianteId, int numero, String nombres, double notaPromedio, double notaFinal){
        this.id = id;
        this.estudianteId = estudianteId;
        this.numero =  numero;
        this.nombres = nombres;
        this.notaPromedio = notaPromedio;
        this.notaFinal = notaFinal;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(int estudianteId) {
        this.estudianteId = estudianteId;
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

    public double getNotaPromedio() {
        return notaPromedio;
    }

    public void setNotaPromedio(double notaPromedio) {
        this.notaPromedio = notaPromedio;
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

    public void calcularPromedio(Acreditable acreditable, Periodo periodo){

        //Calculo del promedio
        double s = 0;
        for (ResumenParcialAcreditable r : this.getAcreditables().values()){
            s += r.getNotaFinal();
        }

        double pm = s / this.getAcreditables().size();
        notaPromedio = Utils.round(pm, 2);


        //Calculo nota final
        //      10   -> 2
        //      7.16 -> x
        double nf = (notaPromedio * acreditable.getEquivalencia()) / periodo.getEscala();
        notaFinal = Utils.round(nf, 2);
    }
}
