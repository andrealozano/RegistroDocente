package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Asistencia;
import com.unl.lapc.registrodocente.modelo.Calendario;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class AsistenciaDao extends DBHandler {

    public static final String TABLE_NAME = "asistencia";

    public AsistenciaDao(Context context){
        super(context);
    }

    public void add(Asistencia asistencia) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fecha", toShortDate(asistencia.getFecha()));
        values.put("estado", asistencia.getEstado());
        //values.put("claseestudiante_id", asistencia.getClaseEstudiante().getEstudianteId());
        values.put("clase_id", asistencia.getClase().getId());
        values.put("estudiante_id", asistencia.getEstudiante().getId());
        values.put("calendario_id", asistencia.getCalendario().getId());

        long id = db.insert(TABLE_NAME, null, values);
        asistencia.setId((int)id);

        db.close();
    }

    public int update(Asistencia asistencia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("estado", asistencia.getEstado());

        return db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(asistencia.getId())});
    }

    public Asistencia get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "id"}, "id=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Asistencia contact = new Asistencia(Integer.parseInt(cursor.getString(0)));

        return contact;
    }


    /*public List<Periodo> getAll() {
        List<Periodo> shopList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Periodo shop = new Periodo();
                shop.setEstudianteId(Integer.parseInt(cursor.getString(0)));
                shop.setNombre(cursor.getString(1));
                shopList.add(shop);
            } while (cursor.moveToNext());
        }

        return shopList;
    }*/

    /*public boolean existe(Periodo per){
        String selectQuery = "SELECT count(*) FROM " + TABLE_NAME + " where lower(trim(" + NOMBRE + ")) =? and " + ID +" <> ?";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new  String[]{per.getNombre().toLowerCase(), ""+per.getEstudianteId()});
        cursor.moveToFirst();

        return cursor.getInt(0) > 0;
    }*/

    public List<Asistencia> getAsistencias(Clase clase, Date fecha) {
        List<Asistencia> shopList = new ArrayList<>();

        String selectQuery = "SELECT a.id, a.estado, a.estudiante_id, a.calendario_id, e.orden, e.nombres, e.apellidos FROM asistencia a, estudiante e where a.estudiante_id=e.id and a.clase_id = " + clase.getId() + " and a.fecha = date('" + toShortDate(fecha) + "')";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Asistencia a = new Asistencia();
                a.setId(cursor.getInt(0));
                a.setEstado(cursor.getString(1));
                a.setFecha(fecha);

                Estudiante e = new Estudiante(cursor.getInt(2));
                e.setClase(clase);
                a.setEstudiante(e);
                a.setCalendario(new Calendario(cursor.getInt(3)));
                e.setOrden(cursor.getInt(4));

                e.setNombres(cursor.getString(5));
                e.setApellidos(cursor.getString(6));

                shopList.add(a);
            } while (cursor.moveToNext());
        }

        return shopList;
    }

    public void borrarAsistencias(Clase clase, Date fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("asistencia", "clase_id = ? and fecha=date(?)", new String[] { String.valueOf(clase.getId()), toShortDate(fecha) });
        db.close();
    }

}
