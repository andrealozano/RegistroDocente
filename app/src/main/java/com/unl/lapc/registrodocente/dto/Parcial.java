package com.unl.lapc.registrodocente.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.unl.lapc.registrodocente.modelo.Periodo;

/**
 * Created by Usuario on 18/08/2016.
 */
public class Parcial implements Parcelable {

    private int numero;
    private int quimestre;

    public Parcial(){

    }

    public Parcial(int quimestre, int numero){
        this.numero = numero;
        this.quimestre = quimestre;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getQuimestre() {
        return quimestre;
    }

    public void setQuimestre(int quimestre) {
        this.quimestre = quimestre;
    }

    @Override
    public String toString() {
        return "Parcial " + numero;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Parcial(Parcel in) {
        super();
        this.numero = in.readInt();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(numero);
        parcel.writeInt(quimestre);
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
