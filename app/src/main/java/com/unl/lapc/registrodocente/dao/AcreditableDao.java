package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.modelo.Quimestre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class AcreditableDao extends DBHandler {

    public AcreditableDao(Context context){
        super(context);
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



}
