package com.unl.lapc.registrodocente.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 18/08/2016.
 */
public class Quimestre implements Parcelable{

    private int numero;

    public Quimestre(){
    }

    public Quimestre(int numero){
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Quimestre " + numero;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    private Quimestre(Parcel in) {
        super();
        this.numero = in.readInt();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(numero);
    }

    public static final Parcelable.Creator<Quimestre> CREATOR = new Parcelable.Creator<Quimestre>() {
        public Quimestre createFromParcel(Parcel in) {
            return new Quimestre(in);
        }
        public Quimestre[] newArray(int size) {
            return new Quimestre[size];
        }
    };
}
