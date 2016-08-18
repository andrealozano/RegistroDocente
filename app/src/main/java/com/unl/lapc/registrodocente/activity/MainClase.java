package com.unl.lapc.registrodocente.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;

import com.unl.lapc.registrodocente.dao.AcreditableDao;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.dao.ParcialDao;
import com.unl.lapc.registrodocente.dao.PeriodoDao;
import com.unl.lapc.registrodocente.dao.QuimestreDao;
import com.unl.lapc.registrodocente.fragment.FragmentAsistancias;
import com.unl.lapc.registrodocente.fragment.FragmentEstudiantes;
import com.unl.lapc.registrodocente.fragment.FragmentResumenNotas;
import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.fragment.FragmentResumenNotasParcial;
import com.unl.lapc.registrodocente.fragment.FragmentResumenNotasQuimestre;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Parcial;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.modelo.Quimestre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainClase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private PeriodoDao periodoDao;
    private ClaseDao claseDao;
    private AcreditableDao daoAcreditable;
    private QuimestreDao quimestreDao;
    private ParcialDao parcialDao;
    private EstudianteDao estudianteDao;

    private Clase clase;
    private Periodo periodo;

    private Map<MenuItem, Object> menuItems = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notas_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Custom init
        Menu menu = navigationView.getMenu();

        periodoDao = new PeriodoDao(this);
        claseDao = new ClaseDao(this);
        daoAcreditable = new AcreditableDao(this);
        quimestreDao = new QuimestreDao(this);
        parcialDao = new ParcialDao(this);
        estudianteDao = new EstudianteDao(this);

        Bundle bundle = getIntent().getExtras();
        clase = bundle.getParcelable("clase");
        periodo = periodoDao.get(clase.getPeriodo().getId());

        TextView txtNavSubtitle = (TextView)findViewById(R.id.txtNavSubtitle);
        //txtNavSubtitle.setText(String.format("%s (%s)", clase.getNombre(), periodo.getNombre()));

        cargarMenu(menu);
        cargarEstudiantes();
    }

    private void cargarMenu(Menu menu){
        List<Quimestre> quimestres = quimestreDao.getAll(clase.getPeriodo());

        //group_id , item_id , order, nombre
        int gid = 1;
        int id = 1;

        for(Quimestre q : quimestres){
            MenuItem s = menu.add(gid, id, id, q.getNombre());
            s.setIcon(R.drawable.ic_dashboard_black_18dp);
            menuItems.put(s, q);
            id++;

            List<Parcial> parciales = parcialDao.getAll(q);
            for (Parcial p: parciales){
                MenuItem sp = menu.add(gid, id, id, p.getNombre());
                sp.setIcon(R.drawable.ic_toc_black_18dp);
                menuItems.put(sp, p);

                id++;
            }

            gid++;
        }
    }

    private void mostrarMenu(boolean b){
        for(MenuItem mi: menuItems.keySet()){
            mi.setVisible(b);
        }
    }

    private void cargarEstudiantes(){
        FragmentEstudiantes fragment = new FragmentEstudiantes();

        Bundle args = new Bundle();
        args.putParcelable("clase", clase);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

        getSupportActionBar().setTitle(clase.getNombre() + ": Estudiantes");

        mostrarMenu(false);
    }

    private void cargarAsistencias(){
        FragmentAsistancias fragment = new FragmentAsistancias();

        Bundle args = new Bundle();
        args.putParcelable("clase", clase);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

        getSupportActionBar().setTitle(clase.getNombre() + ": Asistencias");

        mostrarMenu(false);
    }

    private void cargarResumenNotas(){
        FragmentResumenNotas fragment = new FragmentResumenNotas();

        Bundle args = new Bundle();
        args.putParcelable("clase", clase);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportActionBar().setTitle(clase.getNombre() + ": Resumen notas");
        mostrarMenu(true);
    }

    private void cargarResumenNotasQuimestre(Quimestre quimestre){
        FragmentResumenNotasQuimestre fragment = new FragmentResumenNotasQuimestre();

        Bundle args = new Bundle();
        args.putParcelable("clase", clase);
        args.putParcelable("quimestre", quimestre);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportActionBar().setTitle(clase.getNombre() + ": " + quimestre.getNombre());
    }

    private void cargarResumenNotasParcial(Parcial parcial){
        FragmentResumenNotasParcial fragment = new FragmentResumenNotasParcial();

        Bundle args = new Bundle();
        args.putParcelable("clase", clase);
        args.putParcelable("parcial", parcial);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportActionBar().setTitle(clase.getNombre() + ": " + parcial.getNombre());
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
        getMenuInflater().inflate(R.menu.menu_main_clase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Main/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_back) {
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        boolean fragmentTransaction = false;
        //Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Object tag = menuItems.get(item);

        if(id == R.id.nav_estudiantes){
            cargarEstudiantes();
        }

        if(id == R.id.nav_notas){
            cargarResumenNotas();
        }

        if(id == R.id.nav_asistencias){
            cargarAsistencias();
        }

        if(tag instanceof Quimestre){
            cargarResumenNotasQuimestre((Quimestre)tag);
        }

        if(tag instanceof Parcial){
            cargarResumenNotasParcial((Parcial)tag);
        }

        item.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
