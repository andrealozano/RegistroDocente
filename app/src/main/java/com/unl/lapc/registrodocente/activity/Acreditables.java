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
import com.unl.lapc.registrodocente.adapter.AcreditableAdapter;
import com.unl.lapc.registrodocente.adapter.QuimestreAdapter;
import com.unl.lapc.registrodocente.dao.AcreditableDao;
import com.unl.lapc.registrodocente.dao.QuimestreDao;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.modelo.Quimestre;

public class Acreditables extends AppCompatActivity {

    private ListView mLeadsList;
    private AcreditableDao dao;
    private Periodo periodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acreditables);

        Bundle bundle = getIntent().getExtras();
        periodo = bundle.getParcelable("periodo");

        mLeadsList = (ListView) findViewById(R.id.listView);
        dao = new AcreditableDao(this);

        // Inicializar el adaptador con la fuente de datos.
        AcreditableAdapter mLeadsAdapter = new AcreditableAdapter(this, dao.getAll(periodo));

        //Relacionando la lista con el adaptador
        mLeadsList.setAdapter(mLeadsAdapter);

        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Acreditable m = (Acreditable) mLeadsList.getItemAtPosition(i);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            editAction(new Acreditable(periodo));
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

    private void editAction(Acreditable acreditable){
        Intent intent = new Intent(this, EditAcreditable.class);
        intent.putExtra("periodo", periodo);
        intent.putExtra("acreditable", acreditable);
        startActivity(intent);
    }
}
