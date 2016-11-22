package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.dto.ResumenAcreditable;
import com.unl.lapc.registrodocente.dto.ResumenGeneral;
import com.unl.lapc.registrodocente.dto.ResumenParcial;
import com.unl.lapc.registrodocente.dto.ResumenParcialAcreditable;
import com.unl.lapc.registrodocente.dto.ResumenQuimestre;
import com.unl.lapc.registrodocente.dto.ResumenQuimestreParcial;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Matricula;
import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Clase de acceso a datos para estudiantes.
 */
public class EstudianteDao extends DBHandler {

    public EstudianteDao(Context context){
        super(context);
    }

    /**
     * Inserta un estudiante
     * @param est
     */
    public void add(Estudiante est, Clase clase, Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Inserta el estudiante
        ContentValues evalues = new ContentValues();
        evalues.put("cedula", est.getCedula());
        evalues.put("nombres", est.getNombres());
        evalues.put("apellidos", est.getApellidos());
        evalues.put("email", est.getEmail());
        evalues.put("celular", est.getCelular());
        evalues.put("sexo", est.getSexo());

        long id = db.insert("estudiante", null, evalues);
        est.setId((int)id);

        //Inserta la matricula
        if(clase != null && periodo != null) {
            Matricula mat = new Matricula(clase, est, periodo);
            ContentValues mvalues = new ContentValues();

            mvalues.put("notaFinal", mat.getNotaFinal());
            mvalues.put("porcentajeAsistencias", mat.getPorcentajeAsistencias());
            mvalues.put("estado", mat.getEstado());
            mvalues.put("clase_id", mat.getClase().getId());
            mvalues.put("periodo_id", mat.getPeriodo().getId());
            mvalues.put("estudiante_id", mat.getEstudiante().getId());

            long mid = db.insert("matricula", null, mvalues);
            mat.setId((int) mid);
        }

        db.close();
    }

    public void add(int estudiantes[], Clase clase, Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int id:estudiantes){
            String sql = "insert into matricula (estudiante_id, periodo_id, clase_id, estado, notaFinal, porcentajeAsistencias) select %d, %d, %d, '%s', 0, 0 where not exists " +
                    "(select m.id from matricula m where m.estudiante_id = %d and m.clase_id = %d)";
            String fsql = String.format(sql,
                    id, periodo.getId(), clase.getId(), Matricula.ESTADO_REGISTRADO, id, clase.getId());

            db.execSQL(fsql);
        }

        db.close();
    }

    /**
     * Actualia un estudiante
     * @param est
     * @return
     */
    public int update(Estudiante est) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cedula", est.getCedula());
        values.put("nombres", est.getNombres());
        values.put("apellidos", est.getApellidos());
        values.put("email", est.getEmail());
        values.put("celular", est.getCelular());
        values.put("sexo", est.getSexo());

        /*
        values.put("orden", est.getOrden());
        values.put("notaFinal", est.getNotaFinal());
        values.put("porcentajeAsistencias", est.getPorcentajeAsistencias());
        values.put("estado", est.getEstado());
        values.put("clase_id", est.getClase().getId());
        values.put("periodo_id", est.getPeriodo().getId());
        */

        int u = db.update("estudiante", values, "id = ?", new String[]{String.valueOf(est.getId())});

        //ordernarApellidos(db, est.getClase());

        return u;
    }

    /**
     * Obtiene un estudiante por su id.
     * @param id
     * @return
     */
    public Estudiante get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("estudiante", new String[] { "id", "cedula", "nombres", "apellidos", "email", "celular", "sexo"
                /*, "orden", "notaFinal", "porcentajeAsistencias", "estado", "clase_id", "periodo_id"*/}, "id=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Estudiante contact = new Estudiante(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)/*, cursor.getInt(7), cursor.getDouble(8), cursor.getDouble(9), cursor.getString(10), new Clase(cursor.getInt(11)), new Periodo(cursor.getInt(12))*/);

        return contact;
    }

    /**
     * Borra un estudiante.
     * @param estudiante
     */
    public void delete(Estudiante estudiante, Clase clase) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("asistencia", "estudiante_id = ?", new String[] { String.valueOf(estudiante.getId()) });
        db.delete("registroitem", "estudiante_id = ?", new String[] { String.valueOf(estudiante.getId()) });
        db.delete("registroparcial", "estudiante_id = ?", new String[] { String.valueOf(estudiante.getId()) });
        db.delete("registroquimestral", "estudiante_id = ?", new String[] { String.valueOf(estudiante.getId()) });
        db.delete("registroacreditable", "estudiante_id = ?", new String[] { String.valueOf(estudiante.getId()) });
        db.delete("matricula", "estudiante_id = ?", new String[] { String.valueOf(estudiante.getId()) });
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


    /**
     * Verifica si ya existe un estudiante con la misma cedula
     * @param per
     * @return
     */
    public boolean existeCedula(Estudiante per){
        String selectQuery = "SELECT count(*) FROM estudiante where cedula = ? and id <> ?";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new  String[]{per.getCedula(), "" + per.getId()});
        cursor.moveToFirst();

        return cursor.getInt(0) > 0;
    }

    /**
     * Inicializa las notas de todos los estudiantes de una clase para que no se queden registros faltantes.
     * @param cls
     * @param periodo
     */
    public void initNotas(Clase cls, Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int q = 1; q <= periodo.getQuimestres();q++){
            String sqlrq = String.format("insert into registroquimestral (periodo_id, clase_id, estudiante_id, quimestre, notaParciales, notaExamenes, notaFinal) select m.periodo_id, m.clase_id, m.estudiante_id, %d, 0, 0, 0 from matricula m where m.clase_id = %d and not exists (select n.id from registroquimestral n where n.estudiante_id = m.estudiante_id and n.clase_id = m.clase_id and n.quimestre = %d)", q, cls.getId(), q);
            db.execSQL(sqlrq);

            String sqlraq = String.format("insert into registroacreditable (periodo_id, clase_id, estudiante_id, acreditable_id, quimestre, parcial, notaPromedio, notaFinal) select m.periodo_id, m.clase_id, m.estudiante_id, a.id, %d, 0, 0, 0 from matricula m, acreditable a where m.periodo_id=a.periodo_id and a.tipo='Quimestre' and  m.clase_id = %d and not exists (select n.id from registroacreditable n, acreditable b where n.acreditable_id = b.id and n.estudiante_id = m.estudiante_id and n.clase_id = m.clase_id and b.tipo='Quimestre' and n.quimestre = %d)", q, cls.getId(), q);
            db.execSQL(sqlraq);

            for(int p = 1; p <= periodo.getParciales();p++){
                String sqlrp = String.format("insert into registroparcial (periodo_id, clase_id, estudiante_id, quimestre, parcial, notaFinal) select m.periodo_id, m.clase_id, m.estudiante_id, %d, %d, 0 from matricula m where m.clase_id = %d and not exists (select n.id from registroparcial n where n.estudiante_id = m.estudiante_id and n.clase_id = m.clase_id and n.quimestre = %d and n.parcial = %d)", q, p, cls.getId(), q, p);
                db.execSQL(sqlrp);

                String sqlrap = String.format("insert into registroacreditable (periodo_id, clase_id, estudiante_id, acreditable_id, quimestre, parcial, notaPromedio, notaFinal) select m.periodo_id, m.clase_id, m.estudiante_id, a.id, %d, %d, 0, 0 from matricula m, acreditable a where m.periodo_id=a.periodo_id and a.tipo='Parcial' and  m.clase_id = %d and not exists (select n.id from registroacreditable n, acreditable b where n.acreditable_id = b.id and n.estudiante_id = m.estudiante_id and n.clase_id = m.clase_id and b.tipo='Parcial' and n.quimestre = %d and n.parcial = %d)", q, p, cls.getId(), q, p);
                db.execSQL(sqlrap);
            }
        }

        String sqlrit = String.format("insert into registroitem (periodo_id, clase_id, estudiante_id, acreditable_id, itemacreditable_id, nota) " +
                "select m.periodo_id, m.clase_id, m.estudiante_id, a.acreditable_id, a.id, 0 from matricula m, itemacreditable a where m.clase_id=a.clase_id and  m.clase_id = %d and " +
                "not exists (select n.id from registroitem n, itemacreditable b where n.itemacreditable_id = b.id and n.estudiante_id = m.estudiante_id and n.clase_id = m.clase_id and b.id = a.id)", cls.getId());
        db.execSQL(sqlrit);

        db.close();
    }

    /**
     * Obtiene el lsitado de estudiantes de una clase.
     * @param clase
     * @return
     */
    public List<Estudiante> getEstudiantes(Clase clase) {
        List<Estudiante> shopList = new ArrayList<>();

        String selectQuery = "SELECT e.id, e.cedula, e.nombres, e.apellidos FROM estudiante e, matricula m where m.estudiante_id = e.id and m.clase_id = " + clase.getId() + " order by e.apellidos, e.nombres";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {


                Estudiante e = new Estudiante(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                /*e.setOrden(cursor.getInt(4));
                e.setNotaFinal(cursor.getDouble(5));
                e.setPorcentajeAsistencias(cursor.getDouble(6));
                e.setEstado(cursor.getString(7));
                e.setClase(clase);
                e.setPeriodo(clase.getPeriodo());*/

                shopList.add(e);
            } while (cursor.moveToNext());
        }

        return shopList;
    }

    /**
     * Obtiene el lsitado de estudiantes.
     * @param text El texto a buscar
     * @return
     */
    public List<Estudiante> getEstudiantes(String text, int limit) {
        List<Estudiante> shopList = new ArrayList<>();

        String selectQuery = "SELECT e.id, e.cedula, e.nombres, e.apellidos FROM estudiante e where e.cedula like '"+text+"%' or (e.nombres || ' ' || e.apellidos) like '%"+text+"%' or (e.apellidos || ' ' || e.nombres) like '%"+text+"%' order by e.apellidos, e.nombres limit " + limit;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Estudiante e = new Estudiante(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                shopList.add(e);
            } while (cursor.moveToNext());
        }

        return shopList;
    }

    /**
     * Obtiene el lsitado de estudiantes de una clase.
     * @param clase
     * @return
     */
    public List<Matricula> getMatriculas(Clase clase, Periodo periodo) {
        List<Matricula> shopList = new ArrayList<>();

        String selectQuery = "SELECT e.id, e.cedula, e.nombres, e.apellidos, m.id, m.notaFinal, m.porcentajeAsistencias, m.estado FROM estudiante e, matricula m where m.estudiante_id = e.id and m.clase_id = " + clase.getId() + " order by e.apellidos, e.nombres";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {


                Estudiante e = new Estudiante(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                Matricula m = new Matricula(cursor.getInt(4), clase, e, periodo);

                m.setNotaFinal(cursor.getDouble(5));
                m.setPorcentajeAsistencias(cursor.getDouble(6));
                m.setEstado(cursor.getString(7));

                shopList.add(m);
            } while (cursor.moveToNext());
        }

        return shopList;
    }

    /**
     * Obtiene un resumen quimestral de notas.
     * @param periodo
     * @param clase
     * @param quimestre
     * @return
     */
    public List<ResumenQuimestre> getResumenQuimestre(Periodo periodo, Clase clase, int quimestre) {
        List<ResumenQuimestre> list = new ArrayList<>();

        String selectQuery = "SELECT e.id, (e.nombres || ' ' || e.apellidos) nombres, r.notaParciales, r.notaExamenes, r.notaFinal from estudiante e, registroquimestral r where r.estudiante_id = e.id and r.clase_id = " + clase.getId() + " and r.quimestre = " + quimestre + " order by e.apellidos, e.nombres";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = 0;

        if (cursor.moveToFirst()) {
            do {

                ResumenQuimestre e = new ResumenQuimestre(cursor.getInt(0), i, cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4));
                list.add(e);
                i++;
            } while (cursor.moveToNext());
        }


        String selectQuery1 = "SELECT e.id, r.parcial, r.notaFinal from estudiante e, registroparcial r where r.estudiante_id = e.id and r.clase_id = " + clase.getId() + " and r.quimestre = "+quimestre+" order by r.parcial asc, e.id asc";
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

    /**
     * Obtiene un resumen parcial de notas.
     * @param periodo
     * @param clase
     * @param quimestre
     * @param parcial
     * @return
     */
    public List<ResumenParcial> getResumenParcial(Periodo periodo, Clase clase, int quimestre, int parcial) {
        List<ResumenParcial> list = new ArrayList<>();

        String selectQuery = "SELECT e.id, (e.nombres || ' ' || e.apellidos) nombres, r.notaFinal from estudiante e, registroparcial r where r.estudiante_id = e.id and r.clase_id = " + clase.getId() + " and r.quimestre=" + quimestre + " and r.parcial = " + parcial + " order by e.apellidos, e.nombres";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = 0;

        if (cursor.moveToFirst()) {
            do {

                ResumenParcial e = new ResumenParcial(cursor.getInt(0), i, cursor.getString(1), cursor.getDouble(2));
                list.add(e);
                i++;
            } while (cursor.moveToNext());
        }


        String selectQuery1 = "SELECT r.id, e.id, a.id, r.notaFinal from estudiante e, registroacreditable r, acreditable a where r.estudiante_id = e.id and r.acreditable_id = a.id and r.clase_id = " + clase.getId() + " and r.quimestre = " + quimestre + " and r.parcial = " + parcial + " order by a.numero asc, e.id asc";
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

    /**
     * Obtiene un resumen de notas de un acreditable
     * @param periodo
     * @param clase
     * @param acreditable
     * @param quimestre
     * @param parcial
     * @return
     */
    public List<ResumenAcreditable> getResumenAcreditable(Periodo periodo, Clase clase, Acreditable acreditable, int quimestre, int parcial) {
        List<ResumenAcreditable> list = new ArrayList<>();

        String selectQuery = "SELECT r.id, e.id, (e.nombres || ' ' || e.apellidos) nombres, r.notaPromedio, r.notaFinal from estudiante e, registroacreditable r where r.estudiante_id = e.id and r.clase_id = " + clase.getId() + " and r.acreditable_id = " + acreditable.getId() + " and r.quimestre=" + quimestre + " and r.parcial = " + parcial + " order by e.apellidos, e.nombres";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = 0;

        if (cursor.moveToFirst()) {
            do {
                ResumenAcreditable e = new ResumenAcreditable(cursor.getInt(0), cursor.getInt(1), i, cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4));
                list.add(e);
                i++;
            } while (cursor.moveToNext());
        }


        String selectQuery1 = "SELECT r.id, e.id, r.itemacreditable_id, r.nota from estudiante e, registroitem r where r.estudiante_id = e.id and r.acreditable_id = " + acreditable.getId() + " and r.clase_id = " + clase.getId() + " order by e.id asc";
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

    /**
     * Obtiene el resumen general de notas.
     * @param periodo
     * @param clase
     * @return
     */
    public List<ResumenGeneral> getResumenGeneral(Periodo periodo, Clase clase) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<ResumenGeneral> lisEst = new ArrayList<>();

        String sqlEst = "SELECT e.id, (e.nombres || ' ' || e.apellidos) nombres, m.notaFinal, m.porcentajeAsistencias from estudiante e, matricula m where m.estudiante_id = e.id and m.clase_id = " + clase.getId() + " order by e.apellidos, e.nombres";
        Cursor curEst = db.rawQuery(sqlEst, null);

        int i = 0;
        if (curEst.moveToFirst()) {
            do {
                ResumenGeneral e = new ResumenGeneral(curEst.getInt(0), i, curEst.getString(1), curEst.getDouble(2), curEst.getDouble(3));
                lisEst.add(e);
                i++;
            } while (curEst.moveToNext());
        }

        for(int quimestre = 1; quimestre <= periodo.getQuimestres(); quimestre++) {

            String sqlQui = "SELECT e.id, r.notaParciales, r.notaExamenes, r.notaFinal from estudiante e, registroquimestral r where r.estudiante_id = e.id and r.clase_id = " + clase.getId() + " and r.quimestre = " + quimestre + " order by e.apellidos, e.nombres";
            Cursor curQui = db.rawQuery(sqlQui, null);
            List<ResumenQuimestre> lisQui = new ArrayList<>();

            if (curQui.moveToFirst()) {
                do {

                    ResumenQuimestre e = new ResumenQuimestre(curQui.getInt(0), quimestre, "", curQui.getDouble(1), curQui.getDouble(2), curQui.getDouble(3));
                    lisQui.add(e);
                } while (curQui.moveToNext());
            }

            for (ResumenGeneral r : lisEst) {
                for (ResumenQuimestre r1 : lisQui) {
                    if (r.getId() == r1.getId()) {
                        r.getQuimestres().put(r1.getNumero(), r1);
                    }
                }
            }


            String sqlPar = "SELECT e.id, r.parcial, r.notaFinal from estudiante e, registroparcial r where r.estudiante_id = e.id and r.clase_id = " + clase.getId() + " and r.quimestre = " + quimestre + " order by r.parcial asc, e.id asc";
            Cursor curPar = db.rawQuery(sqlPar, null);
            List<ResumenQuimestreParcial> lisPar = new ArrayList<>();

            if (curPar.moveToFirst()) {
                do {
                    ResumenQuimestreParcial e = new ResumenQuimestreParcial(curPar.getInt(0), curPar.getInt(1), curPar.getDouble(2));
                    lisPar.add(e);
                } while (curPar.moveToNext());
            }

            for (ResumenQuimestre r : lisQui) {
                for (ResumenQuimestreParcial r1 : lisPar) {
                    if (r.getId() == r1.getId()) {
                        r.getParciales().put(r1.getParcial(), r1.getNotaFinal());
                    }
                }
            }
        }

        return lisEst;
    }

}
