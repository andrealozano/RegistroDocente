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
import com.unl.lapc.registrodocente.modelo.Matricula;
import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */

/**
 * Clase de acceso a datos para acreditables.
 */
public class AcreditableDao extends DBHandler {

    public AcreditableDao(Context context){
        super(context);
    }

    /**
     * Obtiene un acreditable por id
     * @param id
     * @return
     */
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

    /**
     * Verifica si existen notas ingresadas para el periodo indicado.
     * @param periodo
     * @return
     */
    public boolean existenNotas(Periodo periodo) {

        String selectQuery = "SELECT count(id) FROM registroitem where nota > 0 and periodo_id = " + periodo.getId();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(0) > 0;
        }

        return false;
    }

    /**
     * Inserta un acreditable en la base de datos.
     * @param acreditable
     */
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

    /**
     * Actualiza un acreditable
     * @param acreditable
     * @return
     */
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

    /**
     * Borra un acreditable
     * @param acreditable
     */
    public void delete(Acreditable acreditable) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("registroitem", "acreditable_id = ?", new String[] { String.valueOf(acreditable.getId()) });
        db.delete("itemacreditable", "acreditable_id = ?", new String[] { String.valueOf(acreditable.getId()) });
        db.delete("registroacreditable", "acreditable_id = ?", new String[] { String.valueOf(acreditable.getId()) });
        db.delete("acreditable", "id = ?", new String[] { String.valueOf(acreditable.getId()) });
        db.close();
    }

    /**
     * Agrega un item acreditable
     * @param itemAcreditable
     */
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

    /**
     * Inicializa las notas (registroitem) de un item acreditable
     * @param id
     */
    private void initNotasItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into registroitem (periodo_id, clase_id, estudiante_id, acreditable_id, itemacreditable_id, nota) " +
                "select ia.periodo_id, ia.clase_id, m.estudiante_id, ia.acreditable_id, ia.id, 0 from itemacreditable ia, matricula m where m.clase_id = ia.clase_id and ia.id = " + id + " and " +
                "not exists (select r.id from registroitem r where r.itemacreditable_id = " + id + " and r.estudiante_id = m.estudiante_id)");
        db.close();
    }

    /**
     * Actualiza un item acreditable
     * @param itemAcreditable
     * @return
     */
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

    /**
     * Borra un item acreditable
     * @param itemAcreditable
     */
    public void deleteItem(ItemAcreditable itemAcreditable) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("registroitem", "itemacreditable_id = ?", new String[] { String.valueOf(itemAcreditable.getId()) });
        db.delete("itemacreditable", "id = ?", new String[] { String.valueOf(itemAcreditable.getId()) });
        db.close();
    }

    /**
     * Actualiza la nota (registroitem) de un item acreditable para un estudiante en específico (Ejm: Leccion 1).
     * Calcula el promedio del registro acreditable (Ejm: Lecciones), calcula la nota del parcial, calcula la nota del quimestre, calcula la nota final y actualiza el estado del estudiante.
     *
     *
     * @param resumen
     * @param registro
     * @param periodo
     * @param clase
     * @param estudiante
     * @param acreditable
     * @param quimestre
     * @param parcial
     * @return
     */
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
                    "update registroparcial set notaFinal=(select sum(ra.notaFinal) from registroacreditable ra where ra.clase_id = %d and ra.estudiante_id = %d and ra.quimestre = %d and ra.parcial = %d) " +
                    "where clase_id = %d and  estudiante_id = %d and quimestre = %d and parcial = %d",
                    clase.getId(), estudiante.getId(), quimestre, parcial,
                    clase.getId(), estudiante.getId(), quimestre, parcial);
            db.execSQL(s);
        }

        //Actualiza registroquimestre (Nota examenes)
        if(acreditable.getTipo().equals(Acreditable.TIPO_ACREDITABLE_QUIMESTRE)){
            String s = String.format(
                    "update registroquimestral set notaExamenes=(select round(((sum(ra.notaPromedio)/(select count(a.id) from acreditable a where a.periodo_id = %d and a.tipo = '%s')) * %s), 2) from registroacreditable ra where ra.clase_id = %d and ra.estudiante_id = %d and ra.quimestre = %d and ra.parcial = 0) " +
                            "where clase_id = %d and estudiante_id = %d and quimestre = %d",
                    periodo.getId(), Acreditable.TIPO_ACREDITABLE_QUIMESTRE,
                    periodo.getEquivalenciaExamenes() / periodo.getEscala(),
                    clase.getId(), estudiante.getId(), quimestre,
                    clase.getId(), estudiante.getId(), quimestre);
            db.execSQL(s);
        }

        //Actualiza registroquimestre (Nota parciales)
        String s1 = String.format(
                "update registroquimestral set notaParciales=(select round(((sum(rp.notaFinal)/%d)) * %s, 2) from registroparcial rp where rp.clase_id = %d and rp.estudiante_id = %d and rp.quimestre = %d) " +
                        "where clase_id = %d and estudiante_id = %d and quimestre = %d",
                periodo.getParciales(),
                periodo.getEquivalenciaParciales() / periodo.getEscala(),
                clase.getId(), estudiante.getId(), quimestre,
                clase.getId(), estudiante.getId(), quimestre);
        db.execSQL(s1);

        //Actualiza nota final quimestre
        String s = String.format("update registroquimestral set notaFinal=(notaParciales + notaExamenes) where clase_id = %d and estudiante_id = %d and quimestre = %d",
                clase.getId(), estudiante.getId(), quimestre);
        db.execSQL(s);

        //Actualiza nota estudiante
        String s2 = String.format("update matricula set notaFinal = " +
                "(select round((sum(rq.notaFinal) / %d), 2) from registroquimestral rq where rq.clase_id = %d and rq.estudiante_id = %d) " +
                "where clase_id = %d and  estudiante_id = %d",
                periodo.getQuimestres(),
                clase.getId(), estudiante.getId(),
                clase.getId(), estudiante.getId()
        );
        db.execSQL(s2);

        //Actualiza estado matricula
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

        return up;
    }

    /**
     * Obtiene los acreditables de un periodo.
     * @param periodo
     * @return
     */
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

    /**
     * Obtiene los acreditables de un periodo por tipo
     * @param periodo
     * @param tipo Quimestral o parcial
     * @return
     */
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

    /**
     * Obtiene los items acreditables de un acreditable
     * @param acreditable
     * @param quimestre
     * @param parcial
     * @return
     */
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
