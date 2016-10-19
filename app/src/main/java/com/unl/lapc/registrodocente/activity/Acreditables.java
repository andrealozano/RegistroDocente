package com.unl.lapc.registrodocente.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.adapter.AcreditableAdapter;
import com.unl.lapc.registrodocente.dao.AcreditableDao;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Periodo;

/**
 * Actvidad para administrar los acreditables de un periodo académico.
 */
public class Acreditables extends AppCompatActivity {

    private ListView listView;
    private AcreditableDao acreditableDao;
    private Periodo periodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acreditables);

        Bundle bundle = getIntent().getExtras();
        periodo = bundle.getParcelable("periodo");

        listView = (ListView) findViewById(R.id.listView);
        acreditableDao = new AcreditableDao(this);

        // Inicializar el adaptador con la fuente de datos.
        AcreditableAdapter mLeadsAdapter = new AcreditableAdapter(this, acreditableDao.getAll(periodo));

        //Relacionando la lista con el adaptador
        listView.setAdapter(mLeadsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Acreditable m = (Acreditable) listView.getItemAtPosition(i);
                if(m != null){
                    editAction(m);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_acreditables, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Al seleccionar un item del menú
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            editAction(new Acreditable(periodo));
            return true;
        }

        /*if (id == R.id.action_back) {
            back();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * Al presionar el botón atras del dispositivo, retorna la actividad Periodos
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Periodos.class);
        startActivity(intent);
        finish();
    }

    /**
     * Accion para editar un acreditable. Lanza la actividad EditAcreditable
     * @param acreditable
     */
    private void editAction(Acreditable acreditable){
        if(acreditableDao.existenNotas(periodo) && acreditable.getId() == 0) {
            Snackbar.make(listView, "No se puede agregar acreditables, porque ya ha ingresado notas.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }

        Intent intent = new Intent(this, EditAcreditable.class);
        intent.putExtra("periodo", periodo);
        intent.putExtra("acreditable", acreditable);
        startActivity(intent);
        finish();
    }
}
