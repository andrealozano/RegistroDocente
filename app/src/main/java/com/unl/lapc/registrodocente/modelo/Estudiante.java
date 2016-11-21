package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase de entidad que representa un estudiante.
 */
public class Estudiante implements Parcelable{

    private int id;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String sexo = "Hombre";
    private String email;
    private String celular;

    public Estudiante(){
    }

    public Estudiante(int id){
        this.id = id;
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

    public Estudiante(int id, String cedula, String nombres, String apellidos, String email, String celular, String sexo){
        this.setId(id);
        this.setCedula(cedula);
        this.setNombres(nombres);
        this.setApellidos(apellidos);
        this.setEmail(email);
        this.setCelular(celular);
        this.setSexo(sexo);
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




}
