package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.ItemAcreditable;
import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class AcreditableDao extends DBHandler {

    public AcreditableDao(Context context){
        super(context);
    }

    public Acreditable get(int id) {

        String selectQuery = "SELECT id, nombre, alias, tipo, equivalencia, numero, periodo_id FROM acreditable where id = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

                Acreditable cls = new Acreditable();
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setNombre(cursor.getString(1));
                cls.setAlias(cursor.getString(2));
                cls.setTipo(cursor.getString(3));
                cls.setEquivalencia(cursor.getDouble(4));
                cls.setNumero(cursor.getInt(5));
                cls.setPeriodo(new Periodo(cursor.getInt(6)));

                return cls;
        }

        return null;
    }

    public void add(Acreditable acreditable) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", acreditable.getNombre());
        values.put("alias", acreditable.getAlias());
        values.put("tipo", acreditable.getTipo());
        values.put("equivalencia", acreditable.getEquivalencia());
        values.put("numero", acreditable.getNumero());
        values.put("periodo_id", acreditable.getPeriodo().getId());

        db.insert("acreditable", null, values);
        db.close();
    }

    public int update(Acreditable acreditable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", acreditable.getNombre());
        values.put("alias", acreditable.getAlias());
        values.put("tipo", acreditable.getTipo());
        values.put("equivalencia", acreditable.getEquivalencia());
        values.put("numero", acreditable.getNumero());
        values.put("periodo_id", acreditable.getPeriodo().getId());

        return db.update("acreditable", values, "id = ?", new String[]{String.valueOf(acreditable.getId())});
    }

    public void delete(Acreditable acreditable) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("acreditable", "id = ?", new String[] { String.valueOf(acreditable.getId()) });
        db.close();
    }

    public void addItem(ItemAcreditable itemAcreditable) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("alias", itemAcreditable.getAlias());
        values.put("nombre", itemAcreditable.getNombre());
        values.put("fecha", toShortDate(itemAcreditable.getFecha()));
        values.put("periodo_id", itemAcreditable.getPeriodo().getId());
        values.put("clase_id", itemAcreditable.getClase().getId());
        values.put("acreditable_id", itemAcreditable.getAcreditable().getId());
        values.put("quimestre", itemAcreditable.getQuimestre());
        values.put("parcial", itemAcreditable.getParcial());

        int id = (int)db.insert("itemacreditable", null, values);
        itemAcreditable.setId(id);

        initNotasItem(db, id);

        db.close();
    }

    private void initNotasItem(SQLiteDatabase db, int id){
        db.execSQL("insert into registroitem (periodo_id, clase_id, estudiante_id, acreditable_id, itemacreditable_id, nota) " +
                "select ia.periodo_id, ia.clase_id, e.id, ia.acreditable_id, ia.id, 0 from itemacreditable ia, estudiante e where e.clase_id = ia.clase_id and ia.id = " + id + " and " +
                "not exists (select r.id from registroitem r where r.id = " + id + " and r.estudiante_id = e.id)");
    }

    public int updateItem(ItemAcreditable itemAcreditable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("alias", itemAcreditable.getAlias());
        values.put("nombre", itemAcreditable.getNombre());
        values.put("fecha", toShortDate(itemAcreditable.getFecha()));
        values.put("periodo_id", itemAcreditable.getPeriodo().getId());
        values.put("clase_id", itemAcreditable.getClase().getId());
        values.put("acreditable_id", itemAcreditable.getAcreditable().getId());
        values.put("quimestre", itemAcreditable.getQuimestre());
        values.put("parcial", itemAcreditable.getParcial());

        initNotasItem(db, itemAcreditable.getId());

        return db.update("itemacreditable", values, "id = ?", new String[]{String.valueOf(itemAcreditable.getId())});
    }

    public List<Acreditable> getAll(Periodo periodo) {
        List<Acreditable> lista = new ArrayList<>();

        String selectQuery = "SELECT id, nombre, alias, tipo, equivalencia, numero FROM acreditable where periodo_id = " + periodo.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Acreditable cls = new Acreditable();
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setNombre(cursor.getString(1));
                cls.setAlias(cursor.getString(2));
                cls.setTipo(cursor.getString(3));
                cls.setEquivalencia(cursor.getDouble(4));
                cls.setNumero(cursor.getInt(5));

                cls.setPeriodo(periodo);

                lista.add(cls);
            } while (cursor.moveToNext());
        }

        return lista;
    }

    public List<Acreditable> getAcreditablesParcial(Periodo periodo, String tipo) {
        List<Acreditable> lista = new ArrayList<>();

        String selectQuery = "SELECT id, nombre, alias, tipo, equivalencia, numero FROM acreditable where tipo='"+tipo+"' and periodo_id = " + periodo.getId() + " order by numero";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Acreditable cls = new Acreditable();
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setNombre(cursor.getString(1));
                cls.setAlias(cursor.getString(2));
                cls.setTipo(cursor.getString(3));
                cls.setEquivalencia(cursor.getDouble(4));
                cls.setNumero(cursor.getInt(5));

                cls.setPeriodo(periodo);

                lista.add(cls);
            } while (cursor.moveToNext());
        }

        return lista;
    }

    public List<ItemAcreditable> getItemsAcreditables(Acreditable acreditable) {
        List<ItemAcreditable> lista = new ArrayList<>();

        String selectQuery = "SELECT id, periodo_id, clase_id, acreditable_id, alias, nombre, fecha, quimestre, parcial FROM itemacreditable where acreditable_id = " + acreditable.getId() + " order by fecha asc, id asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemAcreditable cls = new ItemAcreditable(
                        cursor.getInt(0),
                        new Periodo(cursor.getInt(1)),
                        new Clase(cursor.getInt(2)),
                        new Acreditable(cursor.getInt(3)),
                        cursor.getString(4),
                        cursor.getString(5),
                        toDate(cursor.getString(6)),
                        //new Date(cursor.getLong(6)),
                        cursor.getInt(7),
                        cursor.getInt(8)
                );

                lista.add(cls);
            } while (cursor.moveToNext());
        }

        return lista;
    }



}
