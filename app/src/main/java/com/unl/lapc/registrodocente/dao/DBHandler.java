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

/**
 * Manejador de la base de datos.
 * Se encarga de crear la base de datos, y dar soporte a las diferentes versiones.
 * También inicializa la base con datos por defecto en caso de que sea la primera vez q se acceda a la aplicación.
 */
public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "registro_docente.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Formate una fecha a yyyy-MM-dd
     * @param date
     * @return
     */
    public String toShortDate(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return  sd.format(date);
    }

    /**
     * Convierte un string en formato yyyy-MM-dd a una fecha
     * @param sdate
     * @return
     */
    public Date toDate(String sdate){
        try{
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            return  sd.parse(sdate);
        }catch (Exception e){
            return  null;
        }
    }

    /**
     * Cuando se crea la base de datos.
     * Sentencias para crear las tablas e inicialiar la base con datos por defecto.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PA = "CREATE TABLE periodo(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, inicio DATE, fin DATE, escala REAL, quimestres INTEGER, parciales INTEGER, equivalenciaParciales REAL, equivalenciaExamenes REAL, porcentajeAsistencias REAL, notaMinima REAL)";
        String CREATE_AC = "CREATE TABLE acreditable(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, alias TEXT, tipo TEXT, equivalencia REAL, numero INTEGER, periodo_id INTEGER NOT NULL, FOREIGN KEY(periodo_id) REFERENCES periodo(id))";
        String CREATE_CR = "CREATE TABLE calendario(id INTEGER PRIMARY KEY AUTOINCREMENT, estado TEXT, fecha DATE, observacion TEXT, periodo_id INTEGER NOT NULL, FOREIGN KEY(periodo_id) REFERENCES periodo(id))";
        String CREATE_CL = "CREATE TABLE clase(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, activa BOOLEAN DEFAULT 1, periodo_id INTEGER NOT NULL, FOREIGN KEY(periodo_id) REFERENCES periodo(id))";
        //verificar

        String CREATE_ES = "CREATE TABLE estudiante(id INTEGER PRIMARY KEY AUTOINCREMENT, cedula TEXT, nombres TEXT, apellidos TEXT, sexo TEXT, email TEXT, celular TEXT)";
        String CREATE_CE = "CREATE TABLE matricula(id INTEGER PRIMARY KEY AUTOINCREMENT, notaFinal REAL, porcentajeAsistencias REAL, estado TEXT, periodo_id INTEGER NOT NULL, clase_id INTEGER NOT NULL, estudiante_id INTEGER NOT NULL, FOREIGN KEY(clase_id) REFERENCES clase(id), FOREIGN KEY(periodo_id) REFERENCES periodo(id), FOREIGN KEY(estudiante_id) REFERENCES estudiante(id))";
        String CREATE_AS = "CREATE TABLE asistencia(id INTEGER PRIMARY KEY AUTOINCREMENT, periodo_id INTEGER NOT NULL, calendario_id INTEGER NOT NULL, clase_id INTEGER NOT NULL, estudiante_id INTEGER NOT NULL, fecha DATE, estado TEXT, FOREIGN KEY(clase_id) REFERENCES clase(id), FOREIGN KEY(estudiante_id) REFERENCES estudiante(id), FOREIGN KEY(calendario_id) REFERENCES calendario(id), FOREIGN KEY(periodo_id) REFERENCES periodo(id))";

        String CREATE_NQ = "CREATE TABLE registroquimestral(id INTEGER PRIMARY KEY AUTOINCREMENT, periodo_id INTEGER NOT NULL, clase_id INTEGER NOT NULL, estudiante_id INTEGER NOT NULL, quimestre INTEGER, notaParciales REAL, notaExamenes REAL, notaFinal REAL, FOREIGN KEY(periodo_id) REFERENCES periodo(id), FOREIGN KEY(clase_id) REFERENCES clase(id), FOREIGN KEY(estudiante_id) REFERENCES estudiante(id))";
        String CREATE_NP = "CREATE TABLE registroparcial(id INTEGER PRIMARY KEY AUTOINCREMENT, periodo_id INTEGER NOT NULL, clase_id INTEGER NOT NULL, estudiante_id INTEGER NOT NULL, quimestre INTEGER, parcial INTEGER, notaFinal REAL, FOREIGN KEY(periodo_id) REFERENCES periodo(id), FOREIGN KEY(clase_id) REFERENCES clase(id), FOREIGN KEY(estudiante_id) REFERENCES estudiante(id))";
        String CREATE_NA = "CREATE TABLE registroacreditable(id INTEGER PRIMARY KEY AUTOINCREMENT, periodo_id INTEGER NOT NULL, clase_id INTEGER NOT NULL, estudiante_id INTEGER NOT NULL, acreditable_id INTEGER NOT NULL, quimestre INTEGER, parcial INTEGER, notaPromedio REAL, notaFinal REAL, FOREIGN KEY(periodo_id) REFERENCES periodo(id), FOREIGN KEY(clase_id) REFERENCES clase(id), FOREIGN KEY(estudiante_id) REFERENCES estudiante(id), FOREIGN KEY(acreditable_id) REFERENCES acreditable(id))";

        String CREATE_IA = "CREATE TABLE itemacreditable(id INTEGER PRIMARY KEY AUTOINCREMENT, periodo_id INTEGER NOT NULL, clase_id INTEGER NOT NULL, acreditable_id INTEGER NOT NULL, alias TEXT, nombre TEXT, fecha DATE, quimestre INTEGER, parcial INTEGER, FOREIGN KEY(periodo_id) REFERENCES periodo(id), FOREIGN KEY(clase_id) REFERENCES clase(id), FOREIGN KEY(acreditable_id) REFERENCES acreditable(id))";
        String CREATE_RI = "CREATE TABLE registroitem(id INTEGER PRIMARY KEY AUTOINCREMENT, periodo_id INTEGER NOT NULL, clase_id INTEGER NOT NULL, estudiante_id INTEGER NOT NULL, acreditable_id INTEGER NOT NULL, itemacreditable_id INTEGER NOT NULL, nota REAL, FOREIGN KEY(periodo_id) REFERENCES periodo(id), FOREIGN KEY(clase_id) REFERENCES clase(id), FOREIGN KEY(estudiante_id) REFERENCES estudiante(id), FOREIGN KEY(acreditable_id) REFERENCES acreditable(id), FOREIGN KEY(itemacreditable_id) REFERENCES itemacreditable(id))";

        db.execSQL(CREATE_PA);
        db.execSQL(CREATE_AC);
        db.execSQL(CREATE_CL);
        db.execSQL(CREATE_CR);

        db.execSQL(CREATE_ES);
        db.execSQL(CREATE_CE);
        db.execSQL(CREATE_AS);

        db.execSQL(CREATE_NQ);
        db.execSQL(CREATE_NP);
        db.execSQL(CREATE_NA);

        db.execSQL(CREATE_IA);
        db.execSQL(CREATE_RI);

        this.initData(db);
    }

    /**
     * Inicializa la base con un periodo, acreditables, un curso, un estudiante, etc; como datos de ejemplo.
     * @param db
     */
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
        cvp.put("quimestres", 2);
        cvp.put("parciales", 3);
        cvp.put("equivalenciaParciales", 8);
        cvp.put("equivalenciaExamenes", 2);
        cvp.put("porcentajeAsistencias", 80);
        cvp.put("notaMinima", 7);
        int pid = (int)db.insert("periodo", null, cvp);

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
        db.execSQL("insert into clase (nombre, activa, periodo_id) values ('Curso 1', 1, 1)");
        db.execSQL("insert into estudiante (cedula, nombres, apellidos, sexo) values ('0000000000','Jhon', 'Doe', 'Hombre')");
        db.execSQL("insert into matricula (estado, estudiante_id, clase_id, periodo_id) values ('Registrado', 1, 1, 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*if (oldVersion < 2)
        {
            db.execSQL("ALTER TABLE itemacreditable ADD COLUMN alias TEXT");
            //Log.i("Actualización", "Versión 2");
        }

        if (oldVersion < 3)
        {
            db.execSQL("ALTER TABLE registroacreditable ADD COLUMN notaPromedio REAL");
            //Log.i("Actualización", "Versión 2");
        }

        if (oldVersion < 5)
        {
            db.execSQL("ALTER TABLE periodo ADD COLUMN equivalenciaParciales REAL DEFAULT 8");
            db.execSQL("ALTER TABLE periodo ADD COLUMN equivalenciaExamenes REAL DEFAULT 2");
            db.execSQL("ALTER TABLE periodo ADD COLUMN porcentajeAsistencias REAL DEFAULT 80");
            db.execSQL("ALTER TABLE periodo ADD COLUMN notaMinima REAL DEFAULT 7");
            //Log.i("Actualización", "Versión 2");
        }
        */



    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys = '1';");
            }
        }
    }

}
