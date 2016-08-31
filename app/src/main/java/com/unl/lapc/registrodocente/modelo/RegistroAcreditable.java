package com.unl.lapc.registrodocente.modelo;

/**
 * Created by Usuario on 26/07/2016.
 */
public class RegistroAcreditable {

    private int id;

    private Periodo periodo;
    private Clase clase;
    private Estudiante estudiante;
    private Acreditable acreditable;

    private int quimestre; //{1,2}
    private int parcial; //{1,2, 3}

    private double notaPromedio;
    private double notaFinal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Acreditable getAcreditable() {
        return acreditable;
    }

    public void setAcreditable(Acreditable acreditable) {
        this.acreditable = acreditable;
    }

    public int getQuimestre() {
        return quimestre;
    }

    public void setQuimestre(int quimestre) {
        this.quimestre = quimestre;
    }

    public int getParcial() {
        return parcial;
    }

    public void setParcial(int parcial) {
        this.parcial = parcial;
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
}
