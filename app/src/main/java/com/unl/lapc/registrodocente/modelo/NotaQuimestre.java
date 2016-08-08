package com.unl.lapc.registrodocente.modelo;

/**
 * Created by Usuario on 26/07/2016.
 */
public class NotaQuimestre {

    //private ClaseEstudiante claseEstudiante;
    private Clase clase;
    private Estudiante estudiante;

    private int numero; //{1,2}
    private double parciales; //Promedio parciales
    private double examen; //Nota del exame
    private double nota; //Nota final (parciales * 0.8 + examen * 0.2)
}
