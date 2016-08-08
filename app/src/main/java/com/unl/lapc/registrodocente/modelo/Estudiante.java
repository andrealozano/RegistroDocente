package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 11/07/2016.
 */
public class Estudiante implements Parcelable{

    public static final String ESTADO_REGISTRADO = "Registrado";
    public static final String ESTADO_APROBADO = "Aprobado";
    public static final String ESTADO_REPROBADO = "Reprobado";

    private int id;

    private String cedula;
    private String nombres;
    private String apellidos;
    private String sexo = "Hombre";

    private String email;
    private String celular;

    private int orden;
    private double notaFinal;
    private double porcentajeAsistencias;
    private String estado = ESTADO_REGISTRADO;

    private Clase clase;

    public Estudiante(){
    }

    public Estudiante(int id){
        this.id = id;
    }

    public Estudiante(Clase clase){
        this.clase = clase;
    }

    public Estudiante(int id, String nombres, String apellidos){
        this.setId(id);
        this.setNombres(nombres);
        this.setApellidos(apellidos);
    }

    public Estudiante(int id, String cedula, String nombres, String apellidos){
        this.setId(id);
        this.setCedula(cedula);
        this.setNombres(nombres);
        this.setApellidos(apellidos);
    }

    public Estudiante(int id, String cedula, String nombres, String apellidos, String email, String celular, String sexo, int orden, double notaFinal, double porcentajeAsistencias, String estado, Clase clase){
        this.setId(id);
        this.setCedula(cedula);
        this.setNombres(nombres);
        this.setApellidos(apellidos);
        this.setEmail(email);
        this.setCelular(celular);
        this.setSexo(sexo);
        this.setOrden(orden);
        this.setNotaFinal(notaFinal);
        this.setPorcentajeAsistencias(porcentajeAsistencias);
        this.setEstado(estado);
        this.clase = clase;
    }



    public String getNombresCompletos() {
        return apellidos + " " + nombres;
    }

    @Override
    public String toString() {
        return apellidos + " " + nombres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Estudiante(Parcel in) {
        super();
        this.setId(in.readInt());
        this.setCedula(in.readString());
        this.setNombres(in.readString());
        this.setApellidos(in.readString());
        this.setSexo(in.readString());
        this.setEmail(in.readString());
        this.setCelular(in.readString());
        this.setOrden(in.readInt());
        this.setNotaFinal(in.readDouble());
        this.setPorcentajeAsistencias(in.readDouble());
        this.setEstado(in.readString());
        this.setClase(new Clase(in.readInt()));
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getCedula());
        parcel.writeString(getNombres());
        parcel.writeString(getApellidos());
        parcel.writeString(getSexo());
        parcel.writeString(getEmail());
        parcel.writeString(getCelular());
        parcel.writeInt(getOrden());
        parcel.writeDouble(getNotaFinal());
        parcel.writeDouble(getPorcentajeAsistencias());
        parcel.writeString(getEstado());
        parcel.writeInt(getClase().getId());
    }

    public static final Creator<Estudiante> CREATOR = new Creator<Estudiante>() {
        public Estudiante createFromParcel(Parcel in) {
            return new Estudiante(in);
        }
        public Estudiante[] newArray(int size) {
            return new Estudiante[size];
        }
    };

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public Clase getClase() {
        return clase;
    }
    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public double getPorcentajeAsistencias() {
        return porcentajeAsistencias;
    }

    public void setPorcentajeAsistencias(double porcentajeAsistencias) {
        this.porcentajeAsistencias = porcentajeAsistencias;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
