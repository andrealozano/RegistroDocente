package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Usuario on 26/07/2016.
 */
public class ItemAcreditable implements Parcelable {

    private int id;

    private Periodo periodo;
    private Clase clase;
    private Acreditable acreditable;

    private String alias;
    private String nombre;
    private Date fecha = new Date();

    private int quimestre; //{1,2}
    private int parcial; //{1,2, 3}



    public ItemAcreditable(){

    }

    public ItemAcreditable(int id){
        this.id = id;
    }

    public ItemAcreditable(int id, Periodo periodo, Clase clase, Acreditable acreditable, String alias, String nombre, Date fecha, int quimestre, int parcial){
        this.id = id;
        this.periodo = periodo;
        this.clase = clase;
        this.acreditable = acreditable;
        this.alias = alias;
        this.nombre = nombre;
        this.fecha = fecha;
        this.quimestre = quimestre;
        this.parcial = parcial;
    }

    public ItemAcreditable(Periodo periodo, Clase clase, Acreditable acreditable, int quimestre, int parcial){
        this.periodo = periodo;
        this.clase = clase;
        this.acreditable = acreditable;
        this.quimestre = quimestre;
        this.parcial = parcial;
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

    public Acreditable getAcreditable() {
        return acreditable;
    }

    public void setAcreditable(Acreditable acreditable) {
        this.acreditable = acreditable;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getQuimestre() {
        return quimestre;
    }

    public void setQuimestre(int quimestre) {
        this.quimestre = quimestre;
    }

    public int getParcial() {
        return parcial;
    }

    public void setParcial(int parcial) {
        this.parcial = parcial;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    private ItemAcreditable(Parcel in) {
        super();
        this.setId(in.readInt());
        this.setPeriodo(new Periodo(in.readInt()));
        this.setClase(new Clase(in.readInt()));
        this.setAcreditable(new Acreditable(in.readInt()));
        this.setAlias(in.readString());
        this.setNombre(in.readString());
        this.setFecha(new Date(in.readLong()));
        this.setQuimestre(in.readInt());
        this.setParcial(in.readInt());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeInt(getPeriodo().getId());
        parcel.writeInt(getClase().getId());
        parcel.writeInt(getAcreditable().getId());
        parcel.writeString(getAlias());
        parcel.writeString(getNombre());
        parcel.writeLong(fecha.getTime());
        parcel.writeInt(getQuimestre());
        parcel.writeInt(getParcial());
    }

    public static final Creator<ItemAcreditable> CREATOR = new Creator<ItemAcreditable>() {
        public ItemAcreditable createFromParcel(Parcel in) {
            return new ItemAcreditable(in);
        }
        public ItemAcreditable[] newArray(int size) {
            return new ItemAcreditable[size];
        }
    };
}
