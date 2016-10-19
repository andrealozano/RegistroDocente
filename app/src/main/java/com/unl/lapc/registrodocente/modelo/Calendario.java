package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Clase de entidad que representa un día laborable o feriado de un periodo académico.
 */
public class Calendario implements Parcelable{

    public  static final String ESTADO_ACTIVO = "Activo";
    public  static final String ESTADO_FERIADO = "Feriado";

    private int id;
    private String estado=ESTADO_ACTIVO;
    private Date fecha = new Date();
    private String observacion="";

    private Periodo periodo;

    public Calendario(){
    }

    public Calendario(int id){
        this.id = id;
    }

    public Calendario(Periodo periodo){
        this.setPeriodo(periodo);
    }

    public Calendario(int id, String estado, Date fecha, String observacion, Periodo periodo){
        this.id = id;
        this.setEstado(estado);
        this.setFecha(fecha);
        this.setObservacion(observacion);
        this.setPeriodo(periodo);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj ==null || !(obj instanceof Calendario)){
            return false;
        }
        Calendario other = (Calendario)obj;
        return (other.getId() == this.getId());
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    private Calendario(Parcel in) {
        super();
        this.id = in.readInt();
        this.estado = in.readString();
        this.fecha = new Date(in.readLong());
        this.observacion = in.readString();
        this.setPeriodo(new Periodo(in.readInt()));
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(estado);
        parcel.writeLong(fecha.getTime());
        parcel.writeString(observacion);
        parcel.writeInt(getPeriodo().getId());
    }

    public static final Creator<Calendario> CREATOR = new Creator<Calendario>() {
        public Calendario createFromParcel(Parcel in) {
            return new Calendario(in);
        }
        public Calendario[] newArray(int size) {
            return new Calendario[size];
        }
    };

}
