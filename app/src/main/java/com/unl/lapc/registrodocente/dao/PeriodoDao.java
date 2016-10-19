package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para periodos.
 */
public class PeriodoDao extends DBHandler {

    public PeriodoDao(Context context){
        super(context);
    }

    /**
     * Inserta el periodo
     * @param periodo
     */
    public void add(Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", periodo.getNombre());
        values.put("inicio", toShortDate(periodo.getInicio()));
        values.put("fin", toShortDate(periodo.getFin()));
        values.put("escala", periodo.getEscala());
        values.put("quimestres", periodo.getQuimestres());
        values.put("parciales", periodo.getParciales());
        values.put("equivalenciaParciales", periodo.getEquivalenciaParciales());
        values.put("equivalenciaExamenes", periodo.getEquivalenciaExamenes());
        values.put("porcentajeAsistencias", periodo.getPorcentajeAsistencias());
        values.put("notaMinima", periodo.getNotaMinima());

        int id = (int)db.insert("periodo", null, values);
        periodo.setId(id);

        db.close();
    }

    /**
     * Actualiza el periodo
     * @param periodo
     */
    public void update(Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", periodo.getNombre());
        values.put("inicio", toShortDate(periodo.getInicio()));
        values.put("fin", toShortDate(periodo.getFin()));
        values.put("escala", periodo.getEscala());
        values.put("quimestres", periodo.getQuimestres());
        values.put("parciales", periodo.getParciales());
        values.put("equivalenciaParciales", periodo.getEquivalenciaParciales());
        values.put("equivalenciaExamenes", periodo.getEquivalenciaExamenes());
        values.put("porcentajeAsistencias", periodo.getPorcentajeAsistencias());
        values.put("notaMinima", periodo.getNotaMinima());

        db.update("periodo", values, "id = ?", new String[]{String.valueOf(periodo.getId())});
    }

    /**
     * Obtiene el periodo por id
     * @param id
     * @return
     */
    public Periodo get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("periodo", new String[] { "id", "nombre", "inicio", "fin", "escala", "quimestres", "parciales", "equivalenciaParciales", "equivalenciaExamenes", "porcentajeAsistencias", "notaMinima"}, "id=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Periodo per = new Periodo(cursor.getInt(0), cursor.getString(1), toDate(cursor.getString(2)), toDate(cursor.getString(3)), cursor.getDouble(4), cursor.getInt(5), cursor.getInt(6), cursor.getDouble(7), cursor.getDouble(8), cursor.getDouble(9), cursor.getDouble(10));

        return per;
    }

    /**
     * Borra el periodo y todas sus dependencias (Cursos, estudiantes, notas, etc.).
     * @param periodo
     */
    public void delete(Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("asistencia", "periodo_id = ?", new String[] { String.valueOf(periodo.getId()) });
        db.delete("calendario", "periodo_id = ?", new String[] { String.valueOf(periodo.getId()) });

        db.delete("registroacreditable", "periodo_id = ?", new String[] { String.valueOf(periodo.getId()) });
        db.delete("registroparcial", "periodo_id = ?", new String[] { String.valueOf(periodo.getId()) });
        db.delete("registroquimestral", "periodo_id = ?", new String[] { String.valueOf(periodo.getId()) });
        db.delete("registroitem", "periodo_id = ?", new String[] { String.valueOf(periodo.getId()) });
        db.delete("itemacreditable", "periodo_id = ?", new String[] { String.valueOf(periodo.getId()) });
        db.delete("acreditable", "periodo_id = ?", new String[] { String.valueOf(periodo.getId()) });

        db.delete("estudiante", "periodo_id = ?", new String[] { String.valueOf(periodo.getId()) });
        db.delete("clase", "periodo_id = ?", new String[] { String.valueOf(periodo.getId()) });
        db.delete("periodo", "id = ?", new String[] { String.valueOf(periodo.getId()) });

        db.close();
    }

    /**
     * Obtiene todos los periodos.
     * @return
     */
    public List<Periodo> getAll() {
        List<Periodo> lista = new ArrayList<>();

        String selectQuery = "SELECT id, nombre, inicio, fin, escala, quimestres, parciales, equivalenciaParciales, equivalenciaExamenes, porcentajeAsistencias, notaMinima FROM periodo";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Periodo per = new Periodo(cursor.getInt(0), cursor.getString(1), toDate(cursor.getString(2)), toDate(cursor.getString(3)), cursor.getDouble(4), cursor.getInt(5), cursor.getInt(6), cursor.getDouble(7), cursor.getDouble(8), cursor.getDouble(9), cursor.getDouble(10));
                lista.add(per);
            } while (cursor.moveToNext());
        }

        return lista;
    }

    /**
     * Verifica si existe un periodo con el mismo nombre.
     * @param per
     * @return
     */
    public boolean existe(Periodo per){
        String selectQuery = "SELECT count(*) FROM periodo where lower(trim(nombre)) =? and id <> ?";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new  String[]{per.getNombre().toLowerCase(), ""+per.getId()});
        cursor.moveToFirst();

        return cursor.getInt(0) > 0;
    }

}
