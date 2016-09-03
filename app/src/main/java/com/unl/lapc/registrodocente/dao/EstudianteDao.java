package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.dto.ResumenAcreditable;
import com.unl.lapc.registrodocente.dto.ResumenParcial;
import com.unl.lapc.registrodocente.dto.ResumenParcialAcreditable;
import com.unl.lapc.registrodocente.dto.ResumenQuimestre;
import com.unl.lapc.registrodocente.dto.ResumenQuimestreParcial;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class EstudianteDao extends DBHandler {

    public static final String TABLE_NAME = "estudiante";

    public EstudianteDao(Context context){
        super(context);
    }

    public void add(Estudiante est) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("cedula", est.getCedula());
        values.put("nombres", est.getNombres());
        values.put("apellidos", est.getApellidos());
        values.put("email", est.getEmail());
        values.put("celular", est.getCelular());
        values.put("sexo", est.getSexo());
        values.put("orden", est.getOrden());
        values.put("notaFinal", est.getNotaFinal());
        values.put("porcentajeAsistencias", est.getPorcentajeAsistencias());
        values.put("estado", est.getEstado());
        values.put("clase_id", est.getClase().getId());
        values.put("periodo_id", est.getPeriodo().getId());

        long id = db.insert(TABLE_NAME, null, values);
        est.setId((int)id);

        ordernarApellidos(db, est.getClase());

        db.close();
    }

    public int update(Estudiante est) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cedula", est.getCedula());
        values.put("nombres", est.getNombres());
        values.put("apellidos", est.getApellidos());
        values.put("email", est.getEmail());
        values.put("celular", est.getCelular());
        values.put("sexo", est.getSexo());
        values.put("orden", est.getOrden());
        values.put("notaFinal", est.getNotaFinal());
        values.put("porcentajeAsistencias", est.getPorcentajeAsistencias());
        values.put("estado", est.getEstado());
        values.put("clase_id", est.getClase().getId());
        values.put("periodo_id", est.getPeriodo().getId());

        int u = db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(est.getId())});

        ordernarApellidos(db, est.getClase());

        return u;
    }

    public Estudiante get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "cedula", "nombres", "apellidos", "email", "celular", "sexo", "orden", "notaFinal", "porcentajeAsistencias", "estado", "clase_id", "periodo_id"}, "id=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Estudiante contact = new Estudiante(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7), cursor.getDouble(8), cursor.getDouble(9), cursor.getString(10), new Clase(cursor.getInt(11)), new Periodo(cursor.getInt(12)));

        return contact;
    }

    public void delete(Estudiante estudiante) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("asistencia", "estudiante_id = ?", new String[] { String.valueOf(estudiante.getId()) });
        db.delete("registroitem", "estudiante_id = ?", new String[] { String.valueOf(estudiante.getId()) });
        db.delete("registroparcial", "estudiante_id = ?", new String[] { String.valueOf(estudiante.getId()) });
        db.delete("registroquimestral", "estudiante_id = ?", new String[] { String.valueOf(estudiante.getId()) });
        db.delete("registroacreditable", "estudiante_id = ?", new String[] { String.valueOf(estudiante.getId()) });
        db.delete("estudiante", "id = ?", new String[] { String.valueOf(estudiante.getId()) });
        db.close();
    }

    /*public List<Estudiante> getAll() {
        List<Estudiante> shopList = new ArrayList<>();

        String selectQuery = "SELECT c.id, c.nombre, c.activa, c.periodo_id, p.nombre periodo_nombre FROM clase c, periodoAcademico p where c.periodo_id = p.id";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Estudiante cls = new Estudiante();
                cls.setEstudianteId(Integer.parseInt(cursor.getString(0)));
                cls.setNombres(cursor.getString(1));
                //cls.setActiva(cursor.getInt(2) > 0);


                shopList.add(cls);
            } while (cursor.moveToNext());
        }

        return shopList;
    }*/



    /*public boolean existe(Estudiante per){
        String selectQuery = "SELECT count(*) FROM " + TABLE_NAME + " where lower(trim(nombres)) =? and id <> ?";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new  String[]{per.getNombres().toLowerCase(), ""+per.getEstudianteId()});
        cursor.moveToFirst();

        return cursor.getInt(0) > 0;
    }*/

    public void ordernarApellidos(SQLiteDatabase db, Clase cls) {
        //SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT e.id FROM estudiante e where e.clase_id = " + cls.getId() + " order by e.apellidos asc, e.nombres asc";

        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Integer> lista = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                lista.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        int orden = 1;
        for (int id: lista){
            ContentValues values = new ContentValues();
            values.put("orden", orden);
            db.update("estudiante", values, "id = ?", new String[]{String.valueOf(id)});
            orden++;
        }

        //db.close();
    }

    public void initNotas(Clase cls, Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int q = 1; q <= periodo.getQuimestres();q++){
            String sqlrq = String.format("insert into registroquimestral (periodo_id, clase_id, estudiante_id, quimestre, notaParciales, notaExamenes, notaFinal) select e.periodo_id, e.clase_id, e.id, %d, 0, 0, 0 from estudiante e where e.clase_id = %d and not exists (select n.id from registroquimestral n where n.estudiante_id = e.id and n.quimestre = %d)", q, cls.getId(), q);
            db.execSQL(sqlrq);

            String sqlraq = String.format("insert into registroacreditable (periodo_id, clase_id, estudiante_id, acreditable_id, quimestre, parcial, notaPromedio, notaFinal) select e.periodo_id, e.clase_id, e.id, a.id, %d, 0, 0, 0 from estudiante e, acreditable a where e.periodo_id=a.periodo_id and a.tipo='Quimestre' and  e.clase_id = %d and not exists (select n.id from registroacreditable n, acreditable b where n.acreditable_id = b.id and n.estudiante_id = e.id and b.tipo='Quimestre' and n.quimestre = %d)", q, cls.getId(), q);
            db.execSQL(sqlraq);

            for(int p = 1; p <= periodo.getParciales();p++){
                String sqlrp = String.format("insert into registroparcial (periodo_id, clase_id, estudiante_id, quimestre, parcial, notaFinal) select e.periodo_id, e.clase_id, e.id, %d, %d, 0 from estudiante e where e.clase_id = %d and not exists (select n.id from registroparcial n where n.estudiante_id = e.id and n.quimestre = %d and n.parcial = %d)", q, p, cls.getId(), q, p);
                db.execSQL(sqlrp);

                String sqlrap = String.format("insert into registroacreditable (periodo_id, clase_id, estudiante_id, acreditable_id, quimestre, parcial, notaPromedio, notaFinal) select e.periodo_id, e.clase_id, e.id, a.id, %d, %d, 0, 0 from estudiante e, acreditable a where e.periodo_id=a.periodo_id and a.tipo='Parcial' and  e.clase_id = %d and not exists (select n.id from registroacreditable n, acreditable b where n.acreditable_id = b.id and n.estudiante_id = e.id and b.tipo='Parcial' and n.quimestre = %d and n.parcial = %d)", q, p, cls.getId(), q, p);
                db.execSQL(sqlrap);
            }
        }

        String sqlrit = String.format("insert into registroitem (periodo_id, clase_id, estudiante_id, acreditable_id, itemacreditable_id, nota) " +
                "select e.periodo_id, e.clase_id, e.id, a.acreditable_id, a.id, 0 from estudiante e, itemacreditable a where e.clase_id=a.clase_id and  e.clase_id = %d and " +
                "not exists (select n.id from registroitem n, itemacreditable b where n.itemacreditable_id = b.id and n.estudiante_id = e.id and b.id = a.id)", cls.getId());
        db.execSQL(sqlrit);

        db.close();
    }

    public List<Estudiante> getEstudiantes(Clase clase) {
        List<Estudiante> shopList = new ArrayList<>();

        String selectQuery = "SELECT e.id, e.cedula, e.nombres, e.apellidos, e.orden, e.notaFinal, e.porcentajeAsistencias, e.estado FROM estudiante e where e.clase_id = " + clase.getId() + " order by e.orden asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {


                Estudiante e = new Estudiante(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                e.setOrden(cursor.getInt(4));
                e.setNotaFinal(cursor.getDouble(5));
                e.setPorcentajeAsistencias(cursor.getDouble(6));
                e.setEstado(cursor.getString(7));
                e.setClase(clase);
                e.setPeriodo(clase.getPeriodo());

                shopList.add(e);
            } while (cursor.moveToNext());
        }

        return shopList;
    }


    public List<ResumenQuimestre> getResumenQuimestre(Periodo periodo, Clase clase, int quimestre) {
        List<ResumenQuimestre> list = new ArrayList<>();

        String selectQuery = "SELECT e.id, e.orden, (e.nombres || ' ' || e.apellidos) nombres, r.notaParciales, r.notaExamenes, r.notaFinal from estudiante e, registroquimestral r where r.estudiante_id = e.id and e.clase_id = " + clase.getId() + " and r.quimestre = " + quimestre + " order by e.orden asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                ResumenQuimestre e = new ResumenQuimestre(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5));
                list.add(e);
            } while (cursor.moveToNext());
        }


        String selectQuery1 = "SELECT e.id, r.parcial, r.notaFinal from estudiante e, registroparcial r where r.estudiante_id = e.id and e.clase_id = " + clase.getId() + " and r.quimestre = "+quimestre+" order by r.parcial asc, e.id asc";
        Cursor cursor1 = db.rawQuery(selectQuery1, null);
        List<ResumenQuimestreParcial> list1 = new ArrayList<>();

        if (cursor1.moveToFirst()) {
            do {
                ResumenQuimestreParcial e = new ResumenQuimestreParcial(cursor1.getInt(0),cursor1.getInt(1), cursor1.getDouble(2));
                list1.add(e);
            } while (cursor1.moveToNext());
        }

        for(ResumenQuimestre r: list){
            for (ResumenQuimestreParcial r1: list1){
                if(r.getId() == r1.getId()){
                    r.getParciales().put(r1.getParcial(), r1.getNotaFinal());
                }
            }
        }

        return list;
    }

    public List<ResumenParcial> getResumenParcial(Periodo periodo, Clase clase, int quimestre, int parcial) {
        List<ResumenParcial> list = new ArrayList<>();

        String selectQuery = "SELECT e.id, e.orden, (e.nombres || ' ' || e.apellidos) nombres, r.notaFinal from estudiante e, registroparcial r where r.estudiante_id = e.id and e.clase_id = " + clase.getId() + " and r.quimestre=" + quimestre + " and r.parcial = " + parcial + " order by e.orden asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                ResumenParcial e = new ResumenParcial(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getDouble(3));
                list.add(e);
            } while (cursor.moveToNext());
        }


        String selectQuery1 = "SELECT r.id, e.id, a.id, r.notaFinal from estudiante e, registroacreditable r, acreditable a where r.estudiante_id = e.id and r.acreditable_id = a.id and e.clase_id = " + clase.getId() + " and r.quimestre = " + quimestre + " and r.parcial = " + parcial + " order by a.numero asc, e.id asc";
        Cursor cursor1 = db.rawQuery(selectQuery1, null);
        List<ResumenParcialAcreditable> list1 = new ArrayList<>();

        if (cursor1.moveToFirst()) {
            do {
                ResumenParcialAcreditable e = new ResumenParcialAcreditable(cursor1.getInt(0), cursor1.getInt(1), cursor1.getInt(2), cursor1.getDouble(3));
                list1.add(e);
            } while (cursor1.moveToNext());
        }

        for(ResumenParcial r: list){
            for (ResumenParcialAcreditable r1: list1){
                if(r.getId() == r1.getEstudianteId()){
                    r.getAcreditables().put(r1.getAcreditableId(), r1);
                }
            }
        }

        return list;
    }

    public List<ResumenAcreditable> getResumenAcreditable(Periodo periodo, Clase clase, Acreditable acreditable, int quimestre, int parcial) {
        List<ResumenAcreditable> list = new ArrayList<>();

        String selectQuery = "SELECT r.id, e.id, e.orden, (e.nombres || ' ' || e.apellidos) nombres, r.notaPromedio, r.notaFinal from estudiante e, registroacreditable r where r.estudiante_id = e.id and e.clase_id = " + clase.getId() + " and r.acreditable_id = " + acreditable.getId() + " and r.quimestre=" + quimestre + " and r.parcial = " + parcial + " order by e.orden asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ResumenAcreditable e = new ResumenAcreditable(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5));
                list.add(e);
            } while (cursor.moveToNext());
        }


        String selectQuery1 = "SELECT r.id, e.id, r.itemacreditable_id, r.nota from estudiante e, registroitem r where r.estudiante_id = e.id and r.acreditable_id = " + acreditable.getId() + " and e.clase_id = " + clase.getId() + " order by e.id asc";
        Cursor cursor1 = db.rawQuery(selectQuery1, null);
        List<ResumenParcialAcreditable> list1 = new ArrayList<>();

        if (cursor1.moveToFirst()) {
            do {
                ResumenParcialAcreditable e = new ResumenParcialAcreditable(cursor1.getInt(0), cursor1.getInt(1), cursor1.getInt(2), cursor1.getDouble(3));
                list1.add(e);
            } while (cursor1.moveToNext());
        }

        for(ResumenAcreditable r: list){
            for (ResumenParcialAcreditable r1: list1){
                if(r.getEstudianteId() == r1.getEstudianteId()){
                    r.getAcreditables().put(r1.getAcreditableId(), r1);
                }
            }
        }

        /*for(ResumenAcreditable r: list){
            r.calcularPromedio(acreditable, periodo);
        }*/

        return list;
    }

}
