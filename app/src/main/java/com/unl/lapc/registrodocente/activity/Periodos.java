package com.unl.lapc.registrodocente.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.adapter.PeriodosAdapter;
import com.unl.lapc.registrodocente.dao.PeriodoDao;
import com.unl.lapc.registrodocente.modelo.Calendario;
import com.unl.lapc.registrodocente.modelo.Periodo;

public class Periodos extends AppCompatActivity {

    private ListView lvPeriodos;
    private PeriodoDao dao;
    private PeriodosAdapter periodosAdapter;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodos);

        lvPeriodos = (ListView) findViewById(R.id.listView);

        dao = new PeriodoDao(getApplicationContext());

        // Inicializar el adaptador con la fuente de datos.
         periodosAdapter = new PeriodosAdapter(getApplicationContext(), dao.getAll());

        //Relacionando la lista con el adaptador
        lvPeriodos.setAdapter( periodosAdapter);

        lvPeriodos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Periodo per = (Periodo) lvPeriodos.getItemAtPosition(i);
                if (per != null) {
                    editAction(per);
                }
            }
        });

        registerForContextMenu(lvPeriodos);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_periodos, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            editAction(new Periodo());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v.getId() == R.id.listView){
            AdapterView.AdapterContextMenuInfo inf = (AdapterView.AdapterContextMenuInfo)menuInfo;

            Periodo p = periodosAdapter.getItem(inf.position);
            menu.setHeaderTitle(p.getNombre());

            //menu.add(group_id , item_id , order, nombre);
            menu.add(0, 0, 0, "Editar");
            //------------------------------
            //menu.add(1, 1, 1, "Quimestres");
            menu.add(1, 2, 2, "Acreditables");
            menu.add(1, 3, 4, "Calendario");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo inf = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Periodo p = periodosAdapter.getItem(inf.position);

        int id = item.getItemId();

        if(id == 0) {
            editAction(p);
        }

        /*if(id == 1) {
            Intent intent = new Intent(this, Quimestres.class);
            intent.putExtra("periodo", p);
            startActivity(intent);
        }*/

        if(id == 2) {
            Intent intent = new Intent(this, Acreditables.class);
            intent.putExtra("periodo", p);
            startActivity(intent);
        }

        if(id == 3) {
            Intent intent = new Intent(this, Calendarios.class);
            intent.putExtra("periodo", p);
            startActivity(intent);
        }

        return true;
    }

    private void editAction(Periodo per) {
        Intent intent = new Intent(this, EditPeriodo.class);
        intent.putExtra("periodo", per);
        startActivity(intent);
    }


}
