package com.unl.lapc.registrodocente.dao;

/**
 * Created by Usuario on 11/07/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.unl.lapc.registrodocente.modelo.Acreditable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "registro_docente.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public String toShortDate(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return  sd.format(date);
    }

    public Date toDate(String sdate){
        try{
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            return  sd.parse(sdate);
        }catch (Exception e){
            return  null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PA = "CREATE TABLE periodo(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, inicio DATE, fin DATE, escala REAL)";
        String CREATE_AC = "CREATE TABLE acreditable(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, alias TEXT, tipo TEXT, equivalencia REAL, numero INTEGER, periodo_id INTEGER NOT NULL, FOREIGN KEY(periodo_id) REFERENCES periodo(id))";
        String CREATE_QM = "CREATE TABLE quimestre(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, inicio DATE, fin DATE, numero INTEGER, periodo_id INTEGER NOT NULL, FOREIGN KEY(periodo_id) REFERENCES periodo(id))";
        String CREATE_PR = "CREATE TABLE parcial(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, inicio DATE, fin DATE, numero INTEGER, periodo_id INTEGER NOT NULL, quimestre_id INTEGER NOT NULL, FOREIGN KEY(periodo_id) REFERENCES periodo(id), FOREIGN KEY(quimestre_id) REFERENCES quimestre(id))";
        String CREATE_CR = "CREATE TABLE calendario(id INTEGER PRIMARY KEY AUTOINCREMENT, estado TEXT, fecha DATE, observacion TEXT, periodo_id INTEGER NOT NULL, FOREIGN KEY(periodo_id) REFERENCES periodo(id))";
        String CREATE_CL = "CREATE TABLE clase(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, activa BOOLEAN DEFAULT 1, periodo_id INTEGER NOT NULL, FOREIGN KEY(periodo_id) REFERENCES periodo(id))";
        //verificar

        String CREATE_ES = "CREATE TABLE estudiante(id INTEGER PRIMARY KEY AUTOINCREMENT, cedula TEXT, nombres TEXT, apellidos TEXT, sexo TEXT, email TEXT, celular TEXT, orden INTEGER, notaFinal REAL, porcentajeAsistencias REAL, estado TEXT, clase_id INTEGER NOT NULL, FOREIGN KEY(clase_id) REFERENCES clase(id))";
        //String CREATE_ES_CL = "CREATE TABLE clase_estudiante(id INTEGER PRIMARY KEY AUTOINCREMENT, clase_id INTEGER NOT NULL, estudiante_id INTEGER NOT NULL, orden INTEGER)";
        String CREATE_AS = "CREATE TABLE asistencia(id INTEGER PRIMARY KEY AUTOINCREMENT, calendario_id INTEGER NOT NULL, clase_id INTEGER NOT NULL, estudiante_id INTEGER NOT NULL, fecha DATE, estado TEXT, FOREIGN KEY(clase_id) REFERENCES clase(id), FOREIGN KEY(estudiante_id) REFERENCES estudiante(id), FOREIGN KEY(calendario_id) REFERENCES calendario(id))";

        db.execSQL(CREATE_PA);
        db.execSQL(CREATE_AC);
        db.execSQL(CREATE_QM);
        db.execSQL(CREATE_PR);
        db.execSQL(CREATE_CL);
        db.execSQL(CREATE_CR);

        //Verificar
        db.execSQL(CREATE_ES);
        //db.execSQL(CREATE_ES_CL);
        db.execSQL(CREATE_AS);

        this.initData(db);
    }

    private void initData(SQLiteDatabase db){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        String syear = sf.format(new Date());
        String sfecha = toShortDate(new Date());

        //PERIODO
        ContentValues cvp = new ContentValues();
        cvp.put("nombre", "Periodo " + syear);
        cvp.put("inicio", sfecha);
        cvp.put("fin", sfecha);
        cvp.put("escala", 10);
        int pid = (int)db.insert("periodo", null, cvp);

        //QUIMESTRES
        ContentValues cvq = new ContentValues();
        cvq.put("nombre", "Quimestre 1");
        cvq.put("inicio", sfecha);
        cvq.put("fin", sfecha);
        cvq.put("numero", 1);
        cvq.put("periodo_id", pid);
        int qid1 = (int)db.insert("quimestre", null, cvq);

        cvq.put("nombre", "Quimestre 2");
        cvq.put("numero", 2);
        int qid2 = (int)db.insert("quimestre", null, cvq);

        //PARCIALES
        ContentValues cvpr = new ContentValues();
        cvpr.put("inicio", sfecha);
        cvpr.put("fin", sfecha);
        cvpr.put("periodo_id", pid);

        cvpr.put("nombre", "Parcial 1");
        cvpr.put("numero", 1);
        cvpr.put("quimestre_id", qid1);
        int pr1 = (int)db.insert("parcial", null, cvpr);

        cvpr.put("nombre", "Parcial 2");
        cvpr.put("numero", 2);
        cvpr.put("quimestre_id", qid1);
        int pr2 = (int)db.insert("parcial", null, cvpr);

        cvpr.put("nombre", "Parcial 3");
        cvpr.put("numero", 3);
        cvpr.put("quimestre_id", qid1);
        int pr3 = (int)db.insert("parcial", null, cvpr);

        cvpr.put("nombre", "Parcial 1");
        cvpr.put("numero", 1);
        cvpr.put("quimestre_id", qid2);
        int pr4 = (int)db.insert("parcial", null, cvpr);

        cvpr.put("nombre", "Parcial 2");
        cvpr.put("numero", 2);
        cvpr.put("quimestre_id", qid2);
        int pr5 = (int)db.insert("parcial", null, cvpr);

        cvpr.put("nombre", "Parcial 3");
        cvpr.put("numero", 3);
        cvpr.put("quimestre_id", qid2);
        int pr6 = (int)db.insert("parcial", null, cvpr);

        //ACREDITABLES
        ContentValues ca1 = new ContentValues();
        ca1.put("nombre", "Actividad Indivial");
        ca1.put("alias", "AI");
        ca1.put("tipo", Acreditable.TIPO_ACREDITABLE_PARCIAL);
        ca1.put("equivalencia", 2.0);
        ca1.put("numero", 1);
        ca1.put("periodo_id", pid);
        db.insert("acreditable", null, ca1);

        ca1.put("nombre", "Trabajos Individuales");
        ca1.put("alias", "TI");
        ca1.put("numero", 2);
        db.insert("acreditable", null, ca1);

        ca1.put("nombre", "Trabajos Grupales");
        ca1.put("alias", "TG");
        ca1.put("numero", 3);
        db.insert("acreditable", null, ca1);

        ca1.put("nombre", "Lecciones Orales o Escritas");
        ca1.put("alias", "LOE");
        ca1.put("numero", 4);
        db.insert("acreditable", null, ca1);

        ca1.put("nombre", "Prueba de Bloque");
        ca1.put("alias", "PB");
        ca1.put("numero", 5);
        db.insert("acreditable", null, ca1);

        ca1.put("nombre", "Examen Quimestral");
        ca1.put("alias", "EQ");
        ca1.put("tipo", Acreditable.TIPO_ACREDITABLE_QUIMESTRE);
        ca1.put("numero", 6);
        db.insert("acreditable", null, ca1);

        //
        db.execSQL("insert into clase (nombre, activa, periodo_id) values ('Clase 1', 1, 1)");
        db.execSQL("insert into estudiante (cedula, nombres, apellidos, sexo, orden, estado, clase_id) values ('0000000000','Jhon', 'Doe', 'Hombre', 1, 'Registrado', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2)
        {
            //db.execSQL("ALTER TABLE clase_estudiante  ADD COLUMN orden INTEGER");
            //Log.i("Actualización", "Versión 2");
        }

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys = '1'");
            }
        }
    }

}
