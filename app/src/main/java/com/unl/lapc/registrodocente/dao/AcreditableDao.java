package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.dto.ResumenAcreditable;
import com.unl.lapc.registrodocente.dto.ResumenParcialAcreditable;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
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
        db.close();

        initNotasItem(id);
    }

    private void initNotasItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into registroitem (periodo_id, clase_id, estudiante_id, acreditable_id, itemacreditable_id, nota) " +
                "select ia.periodo_id, ia.clase_id, e.id, ia.acreditable_id, ia.id, 0 from itemacreditable ia, estudiante e where e.clase_id = ia.clase_id and ia.id = " + id + " and " +
                "not exists (select r.id from registroitem r where r.itemacreditable_id = " + id + " and r.estudiante_id = e.id)");
        db.close();
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

        int u = db.update("itemacreditable", values, "id = ?", new String[]{String.valueOf(itemAcreditable.getId())});
        db.close();

        initNotasItem(itemAcreditable.getId());

        return  u;
    }

    public int updateNota(ResumenAcreditable resumen, ResumenParcialAcreditable registro, Periodo periodo, Clase clase, Estudiante estudiante, Acreditable acreditable, int quimestre, int parcial) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Actualiza nota
        ContentValues vri = new ContentValues();
        vri.put("nota", registro.getNotaFinal());
        int up = db.update("registroitem", vri, "id = ?", new String[]{String.valueOf(registro.getId())});

        //Actualiza registo acreditable
        ContentValues vre = new ContentValues();
        vre.put("notaPromedio", resumen.getNotaPromedio());
        vre.put("notaFinal", resumen.getNotaFinal());
        int up1 = db.update("registroacreditable", vre, "id = ?", new String[]{String.valueOf(resumen.getId())});

        //Actualiza registro parcial (Si acreditable tipo=Parcial)
        if(acreditable.getTipo().equals(Acreditable.TIPO_ACREDITABLE_PARCIAL)){
            String s = String.format(
                    "update registroparcial set notaFinal=(select sum(ra.notaFinal) from registroacreditable ra where ra.estudiante_id = %d and ra.quimestre = %d and ra.parcial = %d) " +
                    "where registroparcial.estudiante_id = %d and registroparcial.quimestre = %d and registroparcial.parcial = %d", estudiante.getId(), quimestre, parcial, estudiante.getId(), quimestre, parcial);
            db.execSQL(s);
        }

        //Actualia registroquimestre
        if(acreditable.getTipo().equals(Acreditable.TIPO_ACREDITABLE_QUIMESTRE)){
            String s = String.format(
                    "update registroquimestre r set notaFinal=(select sum(ra.notaFinal) from registroacreditable ra where ra.estudiante_id = %d and ra.quimestre = %d and ra.parcial = %d) " +
                            "where r.estudiante_id = %d and r.quimestre = %d and r.parcial = %d", estudiante.getId(), quimestre, parcial, estudiante.getId(), quimestre, parcial);
            //db.execSQL(s);
        }

        return up;
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

    public List<ItemAcreditable> getItemsAcreditables(Acreditable acreditable, int quimestre, int parcial) {
        List<ItemAcreditable> lista = new ArrayList<>();

        String selectQuery = "SELECT id, periodo_id, clase_id, acreditable_id, alias, nombre, fecha, quimestre, parcial FROM itemacreditable where acreditable_id = " + acreditable.getId() + " and quimestre = "+quimestre+" and parcial = "+parcial+" order by fecha asc, id asc";
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
