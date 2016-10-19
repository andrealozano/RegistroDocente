package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Calendario;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Clase de acceso a datos para días laborables y feriados.
 */
public class CalendarioDao extends DBHandler {

    public CalendarioDao(Context context){
        super(context);
    }

    /**
     * Inserta el día en la base de datos
     * @param calendario
     */
    public void add(Calendario calendario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("estado", calendario.getEstado());
        values.put("fecha", toShortDate(calendario.getFecha()));
        values.put("observacion", calendario.getObservacion());
        values.put("periodo_id", calendario.getPeriodo().getId());

        db.insert("calendario", null, values);
        db.close();
    }

    /**
     * Actualiza el día calendario en la base de datos.
     * @param calendario
     * @return
     */
    public int update(Calendario calendario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("estado", calendario.getEstado());
        values.put("fecha", toShortDate(calendario.getFecha()));
        values.put("observacion", calendario.getObservacion());
        values.put("periodo_id", calendario.getPeriodo().getId());

        if(calendario.getEstado().equals(Calendario.ESTADO_FERIADO)) {
            db.delete("asistencia", "calendario_id = ?", new String[]{String.valueOf(calendario.getId())});
        }

        return db.update("calendario", values, "id = ?", new String[]{String.valueOf(calendario.getId())});
    }

    /**
     * Borra el día calendario de la base de datos.
     * @param calendario
     */
    public void delete(Calendario calendario) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("calendario", "id = ?", new String[] { String.valueOf(calendario.getId()) });
        db.close();
    }

    /**
     * Obtiene todos los días calendario de un periodo según las fechas indicadas.
     * @param periodo
     * @param inicio
     * @param fin
     * @return
     */
    public List<Calendario> getAll(Periodo periodo, Date inicio, Date fin) {
        List<Calendario> lista = new ArrayList<>();

        String selectQuery = String.format("SELECT id, estado, fecha, observacion FROM calendario where periodo_id = %s and fecha >= date('%s') and fecha <= date('%s')", periodo.getId(), Utils.toShortDateString(inicio), Utils.toShortDateString(fin));
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Calendario cls = new Calendario(periodo);
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setEstado(cursor.getString(1));
                cls.setFecha(toDate(cursor.getString(2)));
                cls.setObservacion(cursor.getString(3));

                lista.add(cls);
            } while (cursor.moveToNext());
        }

        return lista;
    }

    /**
     * Registra automáticamente todos los días laborables de un periodo académico según la fecha de inicio y fin.
     * Elimina los días que queden fuera del periodo en caso de hacer cambios.
     * @param periodo
     */
    public void registrar(Periodo periodo) {
        GregorianCalendar c = (GregorianCalendar)GregorianCalendar.getInstance();
        c.setTime(periodo.getFin());
        c.add(Calendar.DAY_OF_MONTH, 1);
        Date fin = c.getTime();


        c.setTime(periodo.getInicio());

        SQLiteDatabase db = this.getWritableDatabase();

        //Borra fuera periodo
        db.execSQL(String.format("delete from asistencia where periodo_id = %d and (fecha < date('%s') or fecha > date('%s'))",
                periodo.getId(), Utils.toShortDateString(periodo.getInicio()), Utils.toShortDateString(periodo.getFin())));
        db.execSQL(String.format("delete from calendario where periodo_id = %d and (fecha < date('%s') or fecha > date('%s'))",
                periodo.getId(), Utils.toShortDateString(periodo.getInicio()), Utils.toShortDateString(periodo.getFin())));


        while(c.getTime().before(fin)){
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            if(dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                Date fecha = c.getTime();
                String sfecha = Utils.toShortDateString(fecha);
                String sql = String.format("insert into calendario (estado, fecha, periodo_id) select '%s', date('%s'), %s where not exists (select c.id from calendario c where c.periodo_id = %s and c.fecha = date('%s'))", Calendario.ESTADO_ACTIVO, sfecha, periodo.getId(), periodo.getId(), sfecha);
                db.execSQL(sql);
            }

            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        db.close();

    }

    /**
     * Obtiene el día calendario por periodo y fecha.
     * @param periodo
     * @param fecha
     * @return
     */
    public Calendario get(Periodo periodo, Date fecha) {


        String selectQuery = String.format("SELECT id, estado, fecha, observacion FROM calendario where periodo_id = %s and fecha = date('%s')", periodo.getId(), Utils.toShortDateString(fecha));

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Calendario cls = new Calendario(periodo);
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setEstado(cursor.getString(1));
                cls.setFecha(toDate(cursor.getString(2)));
                cls.setObservacion(cursor.getString(3));

                return  cls;
            } while (cursor.moveToNext());
        }

        return null;
    }

    /**
     * Obtiene el último día menor o igual a la fecha
     * @param periodo
     * @param fecha
     * @return
     */
    public Calendario getLast(Periodo periodo, Date fecha) {


        String selectQuery = String.format("SELECT id, estado, fecha, observacion FROM calendario where periodo_id = %s and fecha <= date('%s') order by fecha desc limit 1", periodo.getId(), Utils.toShortDateString(fecha));

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Calendario cls = new Calendario(periodo);
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setEstado(cursor.getString(1));
                cls.setFecha(toDate(cursor.getString(2)));
                cls.setObservacion(cursor.getString(3));

                return  cls;
            } while (cursor.moveToNext());
        }

        return null;
    }

    /**
     * Obtiene el día anteior a la fecha indicada
     * @param periodo
     * @param fecha
     * @return
     */
    public Calendario getPrevius(Periodo periodo, Date fecha) {


        String selectQuery = String.format("SELECT id, estado, fecha, observacion FROM calendario where periodo_id = %s and fecha < date('%s') order by fecha desc limit 1", periodo.getId(), Utils.toShortDateString(fecha));

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Calendario cls = new Calendario(periodo);
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setEstado(cursor.getString(1));
                cls.setFecha(toDate(cursor.getString(2)));
                cls.setObservacion(cursor.getString(3));

                return  cls;
            } while (cursor.moveToNext());
        }

        return null;
    }

    /**
     * Obtiene el día siguiente a la fecha indicada
     * @param periodo
     * @param fecha
     * @return
     */
    public Calendario getNext(Periodo periodo, Date fecha) {


        String selectQuery = String.format("SELECT id, estado, fecha, observacion FROM calendario where periodo_id = %s and fecha > date('%s') order by fecha asc limit 1", periodo.getId(), Utils.toShortDateString(fecha));

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Calendario cls = new Calendario(periodo);
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setEstado(cursor.getString(1));
                cls.setFecha(toDate(cursor.getString(2)));
                cls.setObservacion(cursor.getString(3));

                return  cls;
            } while (cursor.moveToNext());
        }

        return null;
    }



}
