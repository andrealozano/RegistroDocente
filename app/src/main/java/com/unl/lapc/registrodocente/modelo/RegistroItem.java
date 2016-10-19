package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Clase de entidad que representa la nota de un estudiante para un item acreditable (Ejm: Leccion 1, nota 8.5).
 */
public class RegistroItem implements Parcelable {

    private int id;

    private Periodo periodo;
    private Clase clase;
    private Estudiante estudiante;
    private Acreditable acreditable;
    private ItemAcreditable itemAcreditable;

    //private int quimestre; //{1,2}
    //private int parcial; //{1,2, 3}

    private double nota;

    public RegistroItem(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Acreditable getAcreditable() {
        return acreditable;
    }

    public void setAcreditable(Acreditable acreditable) {
        this.acreditable = acreditable;
    }

    public ItemAcreditable getItemAcreditable() {
        return itemAcreditable;
    }

    public void setItemAcreditable(ItemAcreditable itemAcreditable) {
        this.itemAcreditable = itemAcreditable;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    private RegistroItem(Parcel in) {
        super();
        this.setId(in.readInt());
        this.setPeriodo(new Periodo(in.readInt()));
        this.setClase(new Clase(in.readInt()));
        this.setEstudiante(new Estudiante(in.readInt()));
        this.setAcreditable(new Acreditable(in.readInt()));
        this.setItemAcreditable(new ItemAcreditable(in.readInt()));
        this.setNota(in.readDouble());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeInt(getPeriodo().getId());
        parcel.writeInt(getClase().getId());
        parcel.writeInt(getEstudiante().getId());
        parcel.writeInt(getAcreditable().getId());
        parcel.writeInt(getItemAcreditable().getId());
        parcel.writeDouble(getNota());
    }

    private static final Creator<RegistroItem> CREATOR = new Creator<RegistroItem>() {
        public RegistroItem createFromParcel(Parcel in) {
            return new RegistroItem(in);
        }
        public RegistroItem[] newArray(int size) {
            return new RegistroItem[size];
        }
    };


}
