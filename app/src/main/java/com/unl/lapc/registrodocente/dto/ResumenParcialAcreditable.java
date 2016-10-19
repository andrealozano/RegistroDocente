package com.unl.lapc.registrodocente.dto;

/**
 * Modelo de presentación que representa el resumen de un acreditable dentro de un parcial.
 */
public class ResumenParcialAcreditable {

    private int id;
    private int estudianteId;
    private int acreditableId;
    private double notaFinal;

    public ResumenParcialAcreditable(int id, int estudianteId, int acreditableId, double notaFinal){
        this.setId(id);
        this.setNotaFinal(notaFinal);
        this.setEstudianteId(estudianteId);
        this.setAcreditableId(acreditableId);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public int getAcreditableId() {
        return acreditableId;
    }

    public void setAcreditableId(int acreditableId) {
        this.acreditableId = acreditableId;
    }

    public void setEstudianteId(int estudianteId) {
        this.estudianteId = estudianteId;
    }

    public int getEstudianteId() {
        return estudianteId;
    }

}
