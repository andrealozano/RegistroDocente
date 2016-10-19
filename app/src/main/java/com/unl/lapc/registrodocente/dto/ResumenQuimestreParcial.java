package com.unl.lapc.registrodocente.dto;

/**
 * Modelo de presentación que representa el resumen de un parcial dentro de un quimestre.
 */
public class ResumenQuimestreParcial {

    private int id;
    private int parcial;
    private double notaFinal;

    public ResumenQuimestreParcial(int id, int parcial, double notaFinal){
        this.setId(id);
        this.setParcial(parcial);
        this.setNotaFinal(notaFinal);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParcial() {
        return parcial;
    }

    public void setParcial(int parcial) {
        this.parcial = parcial;
    }

    public double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(double notaFinal) {
        this.notaFinal = notaFinal;
    }
}
