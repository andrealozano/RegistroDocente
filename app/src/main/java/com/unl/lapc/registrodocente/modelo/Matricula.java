package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andrea on 20/11/2016.
 */

/**
 * Representa la matricula de un estudiante en una curso (paralelo)
 */
public class Matricula implements Parcelable {

    public static final String ESTADO_REGISTRADO = "Registrado";
    public static final String ESTADO_APROBADO = "Aprobado";
    public static final String ESTADO_REPROBADO = "Reprobado";

    private int id;
    private Clase clase;
    private Estudiante estudiante;
    private Periodo periodo;

    private double notaFinal;
    private double porcentajeAsistencias;
    private String estado = ESTADO_REGISTRADO;

    public Matricula(Clase clase, Estudiante estudiante, Periodo periodo){
        this.clase = clase;
        this.periodo = periodo;
        this.estudiante = estudiante;
        //int orden, double notaFinal, double porcentajeAsistencias, String estado
    }

    public Matricula(int id, Clase clase, Estudiante estudiante, Periodo periodo){
        this.id = id;
        this.clase = clase;
        this.periodo = periodo;
        this.estudiante = estudiante;
        //int orden, double notaFinal, double porcentajeAsistencias, String estado
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Clase getClase() {
        return clase;
    }
    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public double getNotaFinal() {
        return notaFinal;
    }
    public void setNotaFinal(double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public double getPorcentajeAsistencias() {
        return porcentajeAsistencias;
    }
    public void setPorcentajeAsistencias(double porcentajeAsistencias) {
        this.porcentajeAsistencias = porcentajeAsistencias;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Periodo getPeriodo() {
        return periodo;
    }
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    private Matricula(Parcel in) {
        super();
        this.setId(in.readInt());

        this.setNotaFinal(in.readDouble());
        this.setPorcentajeAsistencias(in.readDouble());
        this.setEstado(in.readString());
        this.setClase(new Clase(in.readInt()));
        this.setPeriodo(new Periodo(in.readInt()));
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeDouble(getNotaFinal());
        parcel.writeDouble(getPorcentajeAsistencias());
        parcel.writeString(getEstado());
        parcel.writeInt(getClase().getId());
        parcel.writeInt(getPeriodo().getId());

    }

    public static final Parcelable.Creator<Matricula> CREATOR = new Creator<Matricula>() {
        public Matricula createFromParcel(Parcel in) {
            return new Matricula(in);
        }
        public Matricula[] newArray(int size) {
            return new Matricula[size];
        }
    };
}
