package com.unl.lapc.registrodocente.modelo;

/**
 * Created by Usuario on 26/07/2016.
 */
public class NotaParcial {

    //private ClaseEstudiante claseEstudiante;
    private Clase clase;
    private Estudiante estudiante;

    private int quimestre; //[1,2]
    //private NotaQuimestre quimestre;

    private int numero; //Numero parcial (1,2,3)
    private double actividadesInividuales;
    private double trabajosInividuales;
    private double trabajosGrupales;
    private double lecciones;
    private double pruebaBloque;

    private double promedio;
}
