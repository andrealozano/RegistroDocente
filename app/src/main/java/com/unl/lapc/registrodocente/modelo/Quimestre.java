package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Usuario on 11/07/2016.
 */
public class Quimestre implements Parcelable{

    private int id;
    private String nombre="";
    private Date inicio = new Date();
    private Date fin = new Date();
    private int numero;

    private Periodo periodo;

    public Quimestre(){
    }

    public Quimestre(int id){
        this.id = id;
    }

    public Quimestre(Periodo periodo){
        this.periodo = periodo;
    }

    public Quimestre(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public Quimestre(int id, String nombre, Date inicio, Date fin, int numero, Periodo periodo){
        this.id = id;
        this.nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
        this.numero = numero;
        this.periodo = periodo;
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
        if(obj ==null || !(obj instanceof Quimestre)){
            return false;
        }
        Quimestre other = (Quimestre)obj;
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

    public int getNumero() {return numero;}
    public void setNumero(int numero) {this.numero = numero;}

    public Periodo getPeriodo() {return periodo;}
    public void setPeriodo(Periodo periodo) {this.periodo = periodo;}

    @Override
    public int describeContents() {
        return 0;
    }

    private Quimestre(Parcel in) {
        super();
        this.id = in.readInt();
        this.nombre = in.readString();
        this.inicio = new Date(in.readLong());
        this.fin = new Date(in.readLong());
        this.numero = in.readInt();
        this.periodo = new Periodo(in.readInt());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nombre);
        parcel.writeLong(inicio.getTime());
        parcel.writeLong(fin.getTime());
        parcel.writeInt(numero);
        parcel.writeInt(periodo.getId());
    }

    public static final Creator<Quimestre> CREATOR = new Creator<Quimestre>() {
        public Quimestre createFromParcel(Parcel in) {
            return new Quimestre(in);
        }
        public Quimestre[] newArray(int size) {
            return new Quimestre[size];
        }
    };
}
