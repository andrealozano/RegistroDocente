package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase de entidad que representa un curso, paralelo o asignatura.
 */
public class Clase implements Parcelable{

    private int id;
    private String nombre;
    private boolean activa = true;
    private Periodo periodo;

    //No persistibles
    private int numeroEstudiantes;

    public Clase(){
    }

    public Clase(int id){
        this.id = id;
    }

    public Clase(int id, String nombre, boolean activa){
        this.id = id;
        this.nombre = nombre;
        this.activa = activa;
    }
    @Override
    public String toString() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nombre);
        parcel.writeInt(activa ? 1 : 0);
        parcel.writeInt(periodo != null ? periodo.getId() : 0);
    }

    private Clase(Parcel in) {
        super();
        this.id = in.readInt();
        this.nombre = in.readString();
        this.activa = in.readInt() > 0;
        this.setPeriodo(new Periodo(in.readInt()));
    }

    public static final Creator<Clase> CREATOR = new Creator<Clase>() {
        public Clase createFromParcel(Parcel in) {
            return new Clase(in);
        }

        public Clase[] newArray(int size) {
            return new Clase[size];
        }
    };

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public int getNumeroEstudiantes() {
        return numeroEstudiantes;
    }

    public void setNumeroEstudiantes(int numeroEstudiantes) {
        this.numeroEstudiantes = numeroEstudiantes;
    }
}
