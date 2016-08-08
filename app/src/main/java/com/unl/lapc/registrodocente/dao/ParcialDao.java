package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Parcial;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.modelo.Quimestre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class ParcialDao extends DBHandler {

    public ParcialDao(Context context){
        super(context);
    }

    public void add(Parcial parcial) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", parcial.getNombre());
        values.put("inicio", toShortDate(parcial.getInicio()));
        values.put("fin", toShortDate(parcial.getFin()));
        values.put("numero", parcial.getNumero());
        values.put("periodo_id", parcial.getPeriodo().getId());
        values.put("quimestre_id", parcial.getQuimestre().getId());

        db.insert("parcial", null, values);
        db.close();
    }

    public int update(Parcial parcial) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", parcial.getNombre());
        values.put("inicio", toShortDate(parcial.getInicio()));
        values.put("fin", toShortDate(parcial.getFin()));
        values.put("numero", parcial.getNumero());
        values.put("periodo_id", parcial.getPeriodo().getId());
        values.put("quimestre_id", parcial.getQuimestre().getId());

        return db.update("parcial", values, "id = ?", new String[]{String.valueOf(parcial.getId())});
    }

    public void delete(Parcial parcial) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("parcial", "id = ?", new String[] { String.valueOf(parcial.getId()) });
        db.close();
    }

    public List<Parcial> getAll(Quimestre quimestre) {
        List<Parcial> lista = new ArrayList<>();

        String selectQuery = "SELECT id, nombre, inicio, fin, numero, periodo_id FROM parcial where quimestre_id = " + quimestre.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Parcial cls = new Parcial();
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setNombre(cursor.getString(1));
                cls.setInicio(toDate(cursor.getString(2)));
                cls.setFin(toDate(cursor.getString(3)));
                cls.setNumero(cursor.getInt(4));
                cls.setPeriodo(new Periodo(cursor.getInt(5)));
                cls.setQuimestre(quimestre);

                lista.add(cls);
            } while (cursor.moveToNext());
        }

        return lista;
    }



}
