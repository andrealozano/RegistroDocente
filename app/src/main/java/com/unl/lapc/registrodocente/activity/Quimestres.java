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
import com.unl.lapc.registrodocente.adapter.QuimestreAdapter;
import com.unl.lapc.registrodocente.dao.QuimestreDao;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.modelo.Quimestre;

public class Quimestres extends AppCompatActivity {

    private ListView mLeadsList;
    private QuimestreDao dao;
    private Periodo periodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quimestres);

        Bundle bundle = getIntent().getExtras();
        periodo = bundle.getParcelable("periodo");

        mLeadsList = (ListView) findViewById(R.id.listView);
        dao = new QuimestreDao(this);

        // Inicializar el adaptador con la fuente de datos.
        QuimestreAdapter mLeadsAdapter = new QuimestreAdapter(this, dao.getAll(periodo));

        //Relacionando la lista con el adaptador
        mLeadsList.setAdapter(mLeadsAdapter);

        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Quimestre m = (Quimestre) mLeadsList.getItemAtPosition(i);
                if(m != null){
                    editAction(m);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_quimestres, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            editAction(new Quimestre(periodo));
            return true;
        }

        if (id == R.id.action_back) {
            back();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void back(){
        Intent intent = new Intent(this, Periodos.class);
        startActivity(intent);
    }

    private void editAction(Quimestre quimestre){
        Intent intent = new Intent(this, EditQuimestre.class);
        intent.putExtra("periodo", periodo);
        intent.putExtra("quimestre", quimestre);
        startActivity(intent);
    }
}
