package com.unl.lapc.registrodocente.modelo;

/**
 * Clase de entidad que representa la nota de un quimestre para un estudiante.
 */
public class RegistroQuimestral {

    private int id;

    private Periodo periodo;
    private Clase clase;
    private Estudiante estudiante;

    private int quimestre; //{1,2}

    private double notaParciales; //Promedio de los 3 parciales
    private double notaExamenes; // Nota examen quimestral
    private double notaFinal; //Nota final (parciales * 0.8 + examen * 0.2)


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

    public int getQuimestre() {
        return quimestre;
    }

    public void setQuimestre(int quimestre) {
        this.quimestre = quimestre;
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
}
