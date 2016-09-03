package com.unl.lapc.registrodocente.activity;

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
import android.widget.ListView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.adapter.ClasesMainAdapter;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final int PICK_DESTINO_RESPALDO_REQUEST = 1;

    private ListView listViewClases;
    private ClaseDao dao;
    private File backupDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        CustomInit();
    }

    private void CustomInit(){
        listViewClases = (ListView) findViewById(R.id.listView);
        dao = new ClaseDao(getApplicationContext());

        ClasesMainAdapter mLeadsAdapter = new ClasesMainAdapter(getApplicationContext(), dao.getMainClases());
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
        /*if (id == R.id.action_settings) {
            return true;
        }*/

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

        if (id == R.id.action_backup) {
            backupdDatabase();
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

    public void backupdDatabase(){
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
                            backupdDatabaseToEmail();
                        }
                    }
                });
        builder.create().show();
    }

    private File backupdDatabaseToFile(){
        try {

            File data = Environment.getDataDirectory();

            String packageName  = "com.unl.lapc.registrodocente";
            String sourceDBName = "registro_docente.db";
            String backupDBPath = "registro_docente_" + Utils.currentReportDate() + ".db";
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

    private void backupdDatabaseToEmail(){
        backupDB = backupdDatabaseToFile();
        if(backupDB != null) {
            //Envia al correo
            Uri u1 = Uri.fromFile(backupDB);
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Registro Docente - Backup");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Backup: " + backupDB.getName());
            sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
            sendIntent.setType("text/html");
            startActivityForResult(Intent.createChooser(sendIntent, "Destino respaldo"), PICK_DESTINO_RESPALDO_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_DESTINO_RESPALDO_REQUEST) {
            if (resultCode == RESULT_OK && backupDB != null) {
                backupDB.delete();
                backupDB = null;
            }
        }
    }
}
