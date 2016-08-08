package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Usuario on 11/07/2016.
 */
public class Parcial implements Parcelable{

    private int id;
    private String nombre="";
    private Date inicio = new Date();
    private Date fin = new Date();
    private int numero;

    private Periodo periodo;
    private Quimestre quimestre;

    public Parcial(){
    }

    public Parcial(int id){
        this.id = id;
    }

    public Parcial(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public Parcial(Periodo periodo, Quimestre quimestre){
        this.id = 0;
        this.periodo = periodo;
        this.quimestre = quimestre;
        this.inicio = new Date();
        this.fin = new Date();
    }

    public Parcial(int id, String nombre, Date inicio, Date fin, int numero, Periodo periodo, Quimestre quimestre){
        this.id = id;
        this.nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
        this.numero = numero;
        this.periodo = periodo;
        this.quimestre = quimestre;
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
        if(obj ==null || !(obj instanceof Parcial)){
            return false;
        }
        Parcial other = (Parcial)obj;
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

    public Quimestre getQuimestre() {return quimestre;}
    public void setQuimestre(Quimestre quimestre) {this.quimestre = quimestre;}

    @Override
    public int describeContents() {
        return 0;
    }

    private Parcial(Parcel in) {
        super();
        this.id = in.readInt();
        this.nombre = in.readString();
        this.inicio = new Date(in.readLong());
        this.fin = new Date(in.readLong());
        this.numero = in.readInt();
        this.periodo = new Periodo(in.readInt());
        this.quimestre = new Quimestre(in.readInt());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nombre);
        parcel.writeLong(inicio.getTime());
        parcel.writeLong(fin.getTime());
        parcel.writeInt(numero);
        parcel.writeInt(periodo.getId());
        parcel.writeInt(quimestre.getId());
    }

    public static final Creator<Parcial> CREATOR = new Creator<Parcial>() {
        public Parcial createFromParcel(Parcel in) {
            return new Parcial(in);
        }
        public Parcial[] newArray(int size) {
            return new Parcial[size];
        }
    };
}
