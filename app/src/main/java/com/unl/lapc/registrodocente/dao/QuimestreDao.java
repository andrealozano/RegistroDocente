package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.modelo.Quimestre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class QuimestreDao extends DBHandler {

    public QuimestreDao(Context context){
        super(context);
    }

    public void add(Quimestre quimestre) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", quimestre.getNombre());
        values.put("inicio", toShortDate(quimestre.getInicio()));
        values.put("fin", toShortDate(quimestre.getFin()));
        values.put("numero", quimestre.getNumero());
        values.put("periodo_id", quimestre.getPeriodo().getId());

        db.insert("quimestre", null, values);
        db.close();
    }

    public int update(Quimestre quimestre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", quimestre.getNombre());
        values.put("inicio", toShortDate(quimestre.getInicio()));
        values.put("fin", toShortDate(quimestre.getFin()));
        values.put("numero", quimestre.getNumero());
        values.put("periodo_id", quimestre.getPeriodo().getId());


        return db.update("quimestre", values, "id = ?", new String[]{String.valueOf(quimestre.getId())});
    }

    public void delete(Quimestre quimestre) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("quimestre", "id = ?", new String[] { String.valueOf(quimestre.getId()) });
        db.close();
    }

    public List<Quimestre> getAll(Periodo periodo) {
        List<Quimestre> lista = new ArrayList<>();

        String selectQuery = "SELECT id, nombre, inicio, fin, numero FROM quimestre where periodo_id = " + periodo.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Quimestre cls = new Quimestre();
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setNombre(cursor.getString(1));
                cls.setInicio(toDate(cursor.getString(2)));
                cls.setFin(toDate(cursor.getString(3)));
                cls.setNumero(cursor.getInt(4));

                cls.setPeriodo(periodo);

                lista.add(cls);
            } while (cursor.moveToNext());
        }

        return lista;
    }



}
