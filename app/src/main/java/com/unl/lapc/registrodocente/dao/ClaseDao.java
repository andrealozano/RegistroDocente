package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class ClaseDao extends DBHandler {

    public static final String TABLE_NAME = "clase";
    public static final String ID = "id";
    public static final String NOMBRE = "nombre";
    public static final String ACTIVA = "activa";
    public static final String PERIODO_ID = "periodo_id";

    public ClaseDao(Context context){
        super(context);
    }

    public void add(Clase clase) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOMBRE, clase.getNombre());
        values.put(ACTIVA, clase.isActiva() ? 1 : 0);
        values.put(PERIODO_ID, clase.getPeriodo().getId());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /*public void addEstudiante(ClaseEstudiante cls) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("clase_id", cls.getClase().getEstudianteId());
        values.put("estudiante_id", cls.getEstudiante().getEstudianteId());
        values.put("orden", cls.getOrden());

        if(cls.getEstudianteId() == 0){
            int id = (int) db.insert("clase_estudiante", null, values);
            cls.setEstudianteId(id);
        }else{
            db.update("clase_estudiante", values, ID + " = ?", new String[]{String.valueOf(cls.getEstudianteId())});
        }
        db.close();
    }*/



    public int update(Clase clase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOMBRE, clase.getNombre());
        values.put(ACTIVA, clase.isActiva() ? 1 : 0);
        values.put(PERIODO_ID, clase.getPeriodo().getId());


        return db.update(TABLE_NAME, values, ID + " = ?", new String[]{String.valueOf(clase.getId())});
    }

    public Clase getClase(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { ID, NOMBRE, ACTIVA, PERIODO_ID}, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();



        Clase contact = new Clase(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getInt(2)>0);

        String spid = cursor.getString(3);
        if(spid != null){
            Periodo p = new Periodo();
            p.setId(Integer.parseInt(spid));
            contact.setPeriodo(p);
        }


        return contact;
    }

    public void deleteClase(Clase clase) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[] { String.valueOf(clase.getId()) });
        db.close();
    }

    public List<Clase> getAll() {
        List<Clase> shopList = new ArrayList<>();

        String selectQuery = "SELECT c.id, c.nombre, c.activa, c.periodo_id, p.nombre periodo_nombre FROM clase c, periodo p where c.periodo_id = p.id";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Clase cls = new Clase();
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setNombre(cursor.getString(1));
                cls.setActiva(cursor.getInt(2) > 0);

                Periodo p = new Periodo(cursor.getInt(3), cursor.getString(4));
                cls.setPeriodo(p);

                shopList.add(cls);
            } while (cursor.moveToNext());
        }

        return shopList;
    }



    public List<Clase> getMainClases() {
        List<Clase> shopList = new ArrayList<>();

        String selectQuery = "SELECT c.id, c.nombre, c.activa, (select count(ce.id) from estudiante ce where ce.clase_id = c.id) as numeroEstudiantes, periodo_id FROM clase c WHERE c.activa = 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Clase cls = new Clase();
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setNombre(cursor.getString(1));
                cls.setActiva(cursor.getInt(2) > 0);
                cls.setNumeroEstudiantes(cursor.getInt(3));

                Periodo p = new Periodo();
                p.setId(cursor.getInt(4));
                cls.setPeriodo(p);
                shopList.add(cls);

            } while (cursor.moveToNext());
        }

        return shopList;
    }

    public boolean existe(Clase per){
        String selectQuery = "SELECT count(*) FROM " + TABLE_NAME + " where lower(trim(" + NOMBRE + ")) =? and " + ID +" <> ?";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new  String[]{per.getNombre().toLowerCase(), ""+per.getId()});
        cursor.moveToFirst();

        return cursor.getInt(0) > 0;
    }

}
