package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Asistencia;
import com.unl.lapc.registrodocente.modelo.Calendario;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Matricula;
import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase de acceso a datos para asistencias.
 */
public class AsistenciaDao extends DBHandler {

    public AsistenciaDao(Context context){
        super(context);
    }

    /**
     * Inserta una asistencia de un estudiante
     * @param asistencia
     * @param periodo
     */
    public void add(Asistencia asistencia, Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fecha", toShortDate(asistencia.getFecha()));
        values.put("estado", asistencia.getEstado());
        //values.put("claseestudiante_id", asistencia.getClaseEstudiante().getEstudianteId());
        values.put("clase_id", asistencia.getClase().getId());
        values.put("estudiante_id", asistencia.getEstudiante().getId());
        values.put("calendario_id", asistencia.getCalendario().getId());
        values.put("periodo_id", asistencia.getPeriodo().getId());

        long id = db.insert("asistencia", null, values);
        asistencia.setId((int)id);

        this.calcularPorcentaje(db, asistencia.getClase(), asistencia.getEstudiante(), periodo);

        db.close();
    }

    /**
     * Actualia la asistencia de un estudiante
     * @param asistencia
     * @param periodo
     * @return
     */
    public int update(Asistencia asistencia, Clase clase, Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("estado", asistencia.getEstado());

        int up = db.update("asistencia", values, "id = ?", new String[]{String.valueOf(asistencia.getId())});

        this.calcularPorcentaje(db, clase, asistencia.getEstudiante(), periodo);

        return  up;
    }

    /**
     * Calcula el porcentaje de asistencias de un estudiante
     * @param db
     * @param estudiante
     * @param periodo
     */
    private void calcularPorcentaje(SQLiteDatabase db, Clase clase, Estudiante estudiante, Periodo periodo){
        db.execSQL(
                String.format(
                "update matricula set porcentajeAsistencias = round(" +
                        "((select count(a.id) from asistencia a where a.estudiante_id = %d and a.estado = 'P') * 100) / " +
                        "(select count(c.id) from calendario c where c.periodo_id = matricula.periodo_id and c.estado = '%s'), 2) " +
                        "where clase_id = %d and estudiante_id = %d",
                        estudiante.getId(), Calendario.ESTADO_ACTIVO, clase.getId(), estudiante.getId()
                )
        );

        //Actualiza estado estudiante
        String s3 = String.format("update matricula set estado = (case " +
                        "when notaFinal >= %s and porcentajeAsistencias >= %s then  '%s' " +
                        "when notaFinal = 0 and porcentajeAsistencias = 0 then  '%s' " +
                        "when notaFinal < %s or porcentajeAsistencias < %s then  '%s' " +
                        "else '%s' end) " +
                        "where clase_id = %d and estudiante_id = %d",
                periodo.getNotaMinima(), periodo.getPorcentajeAsistencias(), Matricula.ESTADO_APROBADO,
                Matricula.ESTADO_REGISTRADO,
                periodo.getNotaMinima(), periodo.getPorcentajeAsistencias(), Matricula.ESTADO_REPROBADO,
                Matricula.ESTADO_REGISTRADO,
                clase.getId(), estudiante.getId()
        );
        db.execSQL(s3);
    }

    /**
     * Obtiene la asistencia por su id
     * @param id
     * @return
     */
    public Asistencia get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("asistencia", new String[] { "id"}, "id=?", new String[] { String.valueOf(id) }, null, null, null, null);
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

    /**
     * Obtiene las asistencias de un curso para el día indicado.
     * @param clase
     * @param fecha
     * @return
     */
    public List<Asistencia> getAsistencias(Clase clase, Date fecha) {
        List<Asistencia> shopList = new ArrayList<>();

        String selectQuery = "SELECT a.id, a.estado, a.estudiante_id, a.calendario_id, e.nombres, e.apellidos FROM asistencia a, estudiante e where a.estudiante_id=e.id and a.clase_id = " + clase.getId() + " and a.fecha = date('" + toShortDate(fecha) + "') order by e.apellidos, e.nombres";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Asistencia a = new Asistencia();
                a.setId(cursor.getInt(0));
                a.setEstado(cursor.getString(1));
                a.setFecha(fecha);
                a.setPeriodo(clase.getPeriodo());

                Estudiante e = new Estudiante(cursor.getInt(2));
                //e.setClase(clase);
                a.setEstudiante(e);
                a.setCalendario(new Calendario(cursor.getInt(3)));

                e.setNombres(cursor.getString(4));
                e.setApellidos(cursor.getString(5));

                shopList.add(a);
            } while (cursor.moveToNext());
        }

        return shopList;
    }

    /*public void borrarAsistencias(Clase clase, Date fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("asistencia", "clase_id = ? and fecha=date(?)", new String[] { String.valueOf(clase.getId()), toShortDate(fecha) });
        db.close();
    }*/

}
