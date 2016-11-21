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
 * Clase de acceso a datos para cursos.
 */
public class ClaseDao extends DBHandler {

    public ClaseDao(Context context){
        super(context);
    }

    /**
     * Inserta un curso en la base de datos.
     * @param clase
     */
    public void add(Clase clase) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", clase.getNombre());
        values.put("activa", clase.isActiva() ? 1 : 0);
        values.put("periodo_id", clase.getPeriodo().getId());

        db.insert("clase", null, values);
        db.close();
    }

    /*public void addEstudiante(Matricula cls) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("clase_id", cls.get().getEstudianteId());
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

    /**
     * Actualiza un curso en la base de datos.
     * @param clase
     * @return
     */
    public int update(Clase clase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", clase.getNombre());
        values.put("activa", clase.isActiva() ? 1 : 0);
        values.put("periodo_id", clase.getPeriodo().getId());


        return db.update("clase", values, "id = ?", new String[]{String.valueOf(clase.getId())});
    }

    /**
     * Obtiene un curso por su id.
     * @param id
     * @return
     */
    public Clase get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("clase", new String[] { "id", "nombre", "activa", "periodo_id"}, "id=?", new String[] { String.valueOf(id) }, null, null, null, null);
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

    /**
     * Borra un curso.
     * @param clase
     */
    public void delete(Clase clase) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("asistencia", "clase_id = ?", new String[] { String.valueOf(clase.getId()) });
        db.delete("registroitem", "clase_id = ?", new String[] { String.valueOf(clase.getId()) });
        db.delete("registroparcial", "clase_id = ?", new String[] { String.valueOf(clase.getId()) });
        db.delete("registroquimestral", "clase_id = ?", new String[] { String.valueOf(clase.getId()) });
        db.delete("registroacreditable", "clase_id = ?", new String[] { String.valueOf(clase.getId()) });
        db.delete("itemacreditable", "clase_id = ?", new String[] { String.valueOf(clase.getId()) });
        db.delete("estudiante", "clase_id = ?", new String[] { String.valueOf(clase.getId()) });
        db.delete("clase", "id = ?", new String[] { String.valueOf(clase.getId()) });
        db.close();
    }

    /**
     * Obtiene todos los cursos.
     * @return
     */
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

    /**
     * Obtiene el listado de cursos para mostrar en la pantalla inicial.
     * @return
     */
    public List<Clase> getMainClases() {
        List<Clase> shopList = new ArrayList<>();

        String selectQuery = "SELECT c.id, c.nombre, c.activa, (select count(m.id) from matricula m where m.clase_id = c.id) as numeroEstudiantes, periodo_id FROM clase c WHERE c.activa = 1";
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

    /**
     * Verifica si ya existe un curso con el mismo nombre
     * @param per
     * @return
     */
    public boolean existe(Clase per){
        String selectQuery = "SELECT count(*) FROM clase where lower(trim(nombre)) =? and id <> ?";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new  String[]{per.getNombre().toLowerCase(), ""+per.getId()});
        cursor.moveToFirst();

        return cursor.getInt(0) > 0;
    }

}
