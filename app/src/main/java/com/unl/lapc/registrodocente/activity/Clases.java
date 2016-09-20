package com.unl.lapc.registrodocente.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.adapter.ClasesAdapter;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.modelo.Clase;

public class Clases extends AppCompatActivity {

    private ListView listView;
    private ClaseDao claseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clases);

        listView = (ListView) findViewById(R.id.listView);

        claseDao = new ClaseDao(getApplicationContext());

        // Inicializar el adaptador con la fuente de datos.
        ClasesAdapter mLeadsAdapter = new ClasesAdapter(getApplicationContext(), claseDao.getAll());

        //Relacionando la lista con el adaptador
        listView.setAdapter(mLeadsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Clase cls = (Clase) listView.getItemAtPosition(i);
                if(cls != null){
                    editAction(cls);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_clases, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            editAction(new Clase());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editAction(Clase cls){
        Intent intent = new Intent(this, EditClase.class);
        intent.putExtra("clase", cls);
        startActivity(intent);
    }
}
