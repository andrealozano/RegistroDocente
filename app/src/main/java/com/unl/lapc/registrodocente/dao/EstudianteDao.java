package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class EstudianteDao extends DBHandler {

    public static final String TABLE_NAME = "estudiante";

    public EstudianteDao(Context context){
        super(context);
    }

    public void add(Estudiante est) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("cedula", est.getCedula());
        values.put("nombres", est.getNombres());
        values.put("apellidos", est.getApellidos());
        values.put("email", est.getEmail());
        values.put("celular", est.getCelular());
        values.put("sexo", est.getSexo());
        values.put("orden", est.getOrden());
        values.put("notaFinal", est.getNotaFinal());
        values.put("porcentajeAsistencias", est.getPorcentajeAsistencias());
        values.put("estado", est.getEstado());
        values.put("clase_id", est.getClase().getId());

        long id = db.insert(TABLE_NAME, null, values);
        est.setId((int)id);

        db.close();
    }

    public int update(Estudiante est) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cedula", est.getCedula());
        values.put("nombres", est.getNombres());
        values.put("apellidos", est.getApellidos());
        values.put("email", est.getEmail());
        values.put("celular", est.getCelular());
        values.put("sexo", est.getSexo());
        values.put("orden", est.getOrden());
        values.put("notaFinal", est.getNotaFinal());
        values.put("porcentajeAsistencias", est.getPorcentajeAsistencias());
        values.put("estado", est.getEstado());
        values.put("clase_id", est.getClase().getId());

        return db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(est.getId())});
    }

    public Estudiante get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "cedula", "nombres", "apellidos", "email", "celular", "sexo", "orden", "notaFinal", "porcentajeAsistencias", "estado", "clase_id"}, "id=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Estudiante contact = new Estudiante(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7), cursor.getDouble(8), cursor.getDouble(9), cursor.getString(10), new Clase(cursor.getInt(11)));

        return contact;
    }

    public void delete(Estudiante clase) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { String.valueOf(clase.getId()) });
        db.close();
    }

    /*public List<Estudiante> getAll() {
        List<Estudiante> shopList = new ArrayList<>();

        String selectQuery = "SELECT c.id, c.nombre, c.activa, c.periodo_id, p.nombre periodo_nombre FROM clase c, periodoAcademico p where c.periodo_id = p.id";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Estudiante cls = new Estudiante();
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setNombres(cursor.getString(1));
                //cls.setActiva(cursor.getInt(2) > 0);


                shopList.add(cls);
            } while (cursor.moveToNext());
        }

        return shopList;
    }*/



    /*public boolean existe(Estudiante per){
        String selectQuery = "SELECT count(*) FROM " + TABLE_NAME + " where lower(trim(nombres)) =? and id <> ?";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new  String[]{per.getNombres().toLowerCase(), ""+per.getId()});
        cursor.moveToFirst();

        return cursor.getInt(0) > 0;
    }*/

    public void ordernarApellidos(Clase cls) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT e.id FROM estudiante e where e.clase_id = " + cls.getId() + " order by e.apellidos asc, e.nombres asc";

        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Integer> lista = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                lista.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        int orden = 1;
        for (int id: lista){
            ContentValues values = new ContentValues();
            values.put("orden", orden);
            db.update("estudiante", values, "id = ?", new String[]{String.valueOf(id)});
            orden++;
        }

        db.close();
    }

    public List<Estudiante> getEstudiantes(Clase clase) {
        List<Estudiante> shopList = new ArrayList<>();

        String selectQuery = "SELECT e.id, e.cedula, e.nombres, e.apellidos, e.orden FROM estudiante e where e.clase_id = " + clase.getId() + " order by e.orden asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {


                Estudiante e = new Estudiante(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                e.setOrden(cursor.getInt(4));
                e.setClase(clase);

                shopList.add(e);
            } while (cursor.moveToNext());
        }

        return shopList;
    }

}
