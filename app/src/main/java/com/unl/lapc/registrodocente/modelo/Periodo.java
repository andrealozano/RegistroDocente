package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Usuario on 11/07/2016.
 */
public class Periodo implements Parcelable{

    private int id;
    private String nombre="";
    private Date inicio = new Date();
    private Date fin = new Date();
    private double escala = 10; //escala calificacion sobre 10, 20, 100

    public Periodo(){
    }

    public Periodo(int id){
        this.id = id;
    }

    public Periodo(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public Periodo(int id, String nombre, Date inicio, Date fin, double escala){
        this.id = id;
        this.nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
        this.escala = escala;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj ==null || !(obj instanceof Periodo)){
            return false;
        }
        Periodo other = (Periodo)obj;
        return (other.getId() == this.getId());
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

    public Date getInicio() {return inicio;}
    public void setInicio(Date inicio) {this.inicio = inicio;}

    public Date getFin() {return fin;}
    public void setFin(Date fin) {this.fin = fin;}

    public double getEscala() {return escala;}
    public void setEscala(double escala) {this.escala = escala;}

    @Override
    public int describeContents() {
        return 0;
    }

    private Periodo(Parcel in) {
        super();
        this.id = in.readInt();
        this.nombre = in.readString();
        this.inicio = new Date(in.readLong());
        this.fin = new Date(in.readLong());
        this.escala = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getNombre());
        parcel.writeLong(getInicio().getTime());
        parcel.writeLong(getFin().getTime());
        parcel.writeDouble(getEscala());
    }

    public static final Parcelable.Creator<Periodo> CREATOR = new Parcelable.Creator<Periodo>() {
        public Periodo createFromParcel(Parcel in) {
            return new Periodo(in);
        }
        public Periodo[] newArray(int size) {
            return new Periodo[size];
        }
    };
}
