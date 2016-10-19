package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Clase de entidad que representa la asistencia de un estudiante para un día en especial.
 */
public class Asistencia implements Parcelable{

    private int id;
    private Date fecha;
    private String estado; //{A=Asiste,F=Falta}

    //private ClaseEstudiante claseEstudiante;
    private Clase clase;
    private Estudiante estudiante;
    private Calendario calendario;
    private Periodo periodo;

    public Asistencia(){
    }

    public Asistencia(int id){
        this.id = id;
    }

    public Asistencia(int id, Date fecha, Estudiante estudiante){
        this.id = id;
        this.fecha = fecha;
        this.estado = "P";
        this.estudiante = estudiante;
    }

    public Asistencia(int id, Date fecha, Clase clase, Estudiante estudiante, Calendario calendario, Periodo periodo){
        this.id = id;
        this.fecha = fecha;
        this.estado = "P";
        //this.claseEstudiante = claseEstudiante;
        this.clase = clase;
        this.estudiante = estudiante;
        this.calendario = calendario;
        this.periodo = periodo;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {return fecha;}
    public void setFecha(Date fecha) {this.fecha = fecha;}

    //public ClaseEstudiante getClaseEstudiante() {return claseEstudiante;}
    //public void setClaseEstudiante(ClaseEstudiante claseEstudiante) {this.claseEstudiante = claseEstudiante;}

    public Clase getClase() {return clase;}
    public void setClase(Clase clase) {this.clase = clase;}

    public Estudiante getEstudiante() {return estudiante;}
    public void setEstudiante(Estudiante estudiante) {this.estudiante = estudiante;}

    public Calendario getCalendario() {return calendario;}
    public void setCalendario(Calendario calendario) {this.calendario = calendario;}

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public String getEstado() {return estado;}
    public void setEstado(String estado) {this.estado = estado;}

    @Override
    public int describeContents() {
        return 0;
    }

    private Asistencia(Parcel in) {
        super();
        this.id = in.readInt();
        //this.fecha = in.readString();
        //this.activa = in.readInt() > 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        //parcel.writeString(getNombre());
        //parcel.writeInt(isActiva() ? 1 : 0);
    }

    public static final Creator<Asistencia> CREATOR = new Creator<Asistencia>() {
        public Asistencia createFromParcel(Parcel in) {
            return new Asistencia(in);
        }

        public Asistencia[] newArray(int size) {
            return new Asistencia[size];
        }
    };

}
