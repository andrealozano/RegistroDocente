package com.unl.lapc.registrodocente.dto;

/**
 * Created by Usuario on 20/08/2016.
 */
public class ResumenParcialAcreditable {

    private int id;
    private int acreditableId;
    private double notaFinal;

    public ResumenParcialAcreditable(int id, int acreditableId, double notaFinal){
        this.setId(id);
        this.setNotaFinal(notaFinal);
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
}
