package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 11/07/2016.
 */
public class Acreditable implements Parcelable{

    public static final String TIPO_ACREDITABLE_PARCIAL = "Parcial";
    public static final String TIPO_ACREDITABLE_QUIMESTRE = "Quimestre";

    private int id;
    private String nombre="";
    private String alias="";
    private String tipo= TIPO_ACREDITABLE_PARCIAL;
    private double equivalencia=2;
    private int numero=0;

    private Periodo periodo;

    public Acreditable(){
    }

    public Acreditable(int id){
        this.id = id;
    }

    public Acreditable(Periodo periodo){
        this.periodo= periodo;
    }

    public Acreditable(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public Acreditable(int id, String nombre, String alias, String tipo, double eqivalencia, int numero){
        this.id = id;
        this.nombre = nombre;
        this.alias = alias;
        this.tipo = tipo;
        this.equivalencia = equivalencia;
        this.numero = numero;
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
        if(obj ==null || !(obj instanceof Acreditable)){
            return false;
        }
        Acreditable other = (Acreditable)obj;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getEquivalencia() {
        return equivalencia;
    }
    public void setEquivalencia(double equivalencia) {
        this.equivalencia = equivalencia;
    }

    public int getNumero() {return numero;}
    public void setNumero(int numero) {this.numero = numero;}

    public Periodo getPeriodo() {return periodo;}
    public void setPeriodo(Periodo periodo) {this.periodo = periodo;}

    @Override
    public int describeContents() {
        return 0;
    }

    private Acreditable(Parcel in) {
        super();
        this.id = in.readInt();
        this.nombre = in.readString();
        this.alias = in.readString();
        this.tipo = in.readString();
        this.equivalencia = in.readDouble();
        this.numero = in.readInt();
        this.periodo = new Periodo(in.readInt());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nombre);
        parcel.writeString(alias);
        parcel.writeString(tipo);
        parcel.writeDouble(equivalencia);
        parcel.writeInt(numero);
        parcel.writeInt(periodo.getId());
    }

    public static final Creator<Acreditable> CREATOR = new Creator<Acreditable>() {
        public Acreditable createFromParcel(Parcel in) {
            return new Acreditable(in);
        }
        public Acreditable[] newArray(int size) {
            return new Acreditable[size];
        }
    };
}
