package com.unl.lapc.registrodocente.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.adapter.ClasesMainAdapter;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.modelo.Calendario;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.util.UriUtils;
import com.unl.lapc.registrodocente.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Actividad principal para mostrar las clases activas y el menú con las opciones para administrar los periodos, cursos y respaldos.
 * Esta actividad se lanza al arrancar la aplicación. Además verifica si el usuario no está autenticado para mostrar la pantalla de login.
 */
public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final int AUTENTICATION_REQUEST = 1;
    static final int PICK_DESTINO_RESPALDO_REQUEST = 2;
    static final int FILE_SELECT_RESTORE = 3;

    protected static String login = null;


    private ListView listViewClases;
    private ClaseDao claseDao;
    private File backupDB;
    //private String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        customInit();

        if(Main.login == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, AUTENTICATION_REQUEST);
        }

    }

    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putString("login", login);
    }*/

    /**
     * Inicializa componentes, daos, etc.
     */
    private void customInit(){
        listViewClases = (ListView) findViewById(R.id.listView);
        claseDao = new ClaseDao(this);

        ClasesMainAdapter mLeadsAdapter = new ClasesMainAdapter(getApplicationContext(), claseDao.getMainClases());
        listViewClases.setAdapter(mLeadsAdapter);

        listViewClases.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Clase cls = (Clase) listViewClases.getItemAtPosition(i);
                if(cls!=null) {
                    show(cls);
                }
            }
        });
    }


    /**
     * Muestra la actividad MainClase donde se despliega todo los necesario para gestionar el curso (Estudiantes, notas, asistencias, etc.)
     * @param cls El curso
     */
    private void show(Clase cls){
        Intent intent = new Intent(this, MainClase.class);
        intent.putExtra("clase", cls);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Main/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_periodos) {
            Intent intent = new Intent(this, Periodos.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_clases) {
            Intent intent = new Intent(this, Clases.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_salir) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }

        if (id == R.id.action_backup) {
            backupDatabase();
        }

        if (id == R.id.action_restore) {
            showFileChooserRestore();
        }

        /*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Respalda la base de datos según el destino
     */
    private void backupDatabase(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Respaldar base de datos")
                .setItems(R.array.destino_respaldo_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        if (which == 0){
                            File backupDB = backupdDatabaseToFile();
                            if(backupDB != null) {
                                Snackbar.make(listViewClases, "Respaldo realizado correctamente: " + backupDB.getAbsolutePath(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        }else{
                            backupDatabaseToEmail();
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * Respalda la base de datos en un directorio de la tarjeta de memoria
     * @return
     */
    private File backupdDatabaseToFile(){
        try {

            File data = Environment.getDataDirectory();

            String packageName  = "com.unl.lapc.registrodocente";
            String sourceDBName = "registro_docente.db";
            String backupDBPath = "registro_docente_" + Utils.currentReportDate() + ".db";

            Utils.checkReportPermisions(this);
            File backupDB = Utils.getExternalStorageFile("backup", backupDBPath);

            if (backupDB != null) {
                String currentDBPath = "data/" + packageName + "/databases/" + sourceDBName;
                File currentDB = new File(data, currentDBPath);

                Log.i("backup","backupDB=" + backupDB.getAbsolutePath());
                Log.i("backup","sourceDB=" + currentDB.getAbsolutePath());

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                return backupDB;
            }
        } catch (Exception e) {
            Log.i("Backup", e.toString());
        }

        return null;
    }

    /**
     * Respalda la base de datos y la envía por correo o a la nube.
     */
    private void backupDatabaseToEmail(){
        backupDB = backupdDatabaseToFile();
        if(backupDB != null) {
            //Envia al correo
            Uri u1 = Uri.fromFile(backupDB);
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Registro Docente - Backup");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Backup: " + backupDB.getName());
            sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
            sendIntent.putExtra(Intent.EXTRA_EMAIL, Utils.getEmailPref(this));
            sendIntent.setType("text/html");
            startActivityForResult(Intent.createChooser(sendIntent, "Destino respaldo"), PICK_DESTINO_RESPALDO_REQUEST);
        }
    }

    private void showFileChooserRestore() {
        Utils.checkReportPermisions(this);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        //intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Base de datos a restaurar"), FILE_SELECT_RESTORE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Por favor instale un administrador de archivos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void restoreDatabase(File backupDB){
        try {

            if(backupDB.getAbsolutePath().endsWith(".db")) {
                if(backupDB.getAbsolutePath().contains("registro_docente")) {

                    File data = Environment.getDataDirectory();

                    String packageName = "com.unl.lapc.registrodocente";
                    String sourceDBName = "registro_docente.db";

                    String currentDBPath = "data/" + packageName + "/databases/" + sourceDBName;

                    //String currentDBPath = "//data/package name/databases/database_name";
                    File currentDB = new File(data, currentDBPath);


                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(backupDB).getChannel();
                        FileChannel dst = new FileOutputStream(currentDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        //builder.setView(myView);
                        builder.setTitle("Datos restaurados!");
                        builder.setMessage("La base de datos ha sido restaurada correctamente. Se procederá a cerrar la aplicación.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else{
                    Toast.makeText(this, "Archivo no contiene registro_docente en el nombre", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Archivo de base no válido", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error al restaurar", Toast.LENGTH_SHORT).show();
            Log.e("Restore error", e.getMessage());
        }
    }

    /**
     * Procesa el resultado al lanzar las subactividades de autenticación y respaldo
     * @param requestCode Código de petición
     * @param resultCode Codigo de respuesta
     * @param data La actividad
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_DESTINO_RESPALDO_REQUEST) {
            if (resultCode == RESULT_OK && backupDB != null) {
                backupDB.delete();
                backupDB = null;
            }
        }

        if(requestCode == AUTENTICATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                customInit();
            }else{
                finish();
            }
        }

        if(requestCode == FILE_SELECT_RESTORE){
            if (resultCode == RESULT_OK) {
                try{
                    Uri uri = data.getData();
                    Log.d("Restore uri", "File Uri: " + uri.toString());
                    File myFile= new File(UriUtils.getFilePath(this, uri));
                    Log.d("Restore path", "File Path: " + myFile.getAbsolutePath());
                    restoreDatabase(myFile);
                }catch (Exception e){

                }

            }
        }
    }
}
