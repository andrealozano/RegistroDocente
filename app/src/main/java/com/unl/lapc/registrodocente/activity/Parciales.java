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
import com.unl.lapc.registrodocente.adapter.ParcialAdapter;
import com.unl.lapc.registrodocente.dao.ParcialDao;
import com.unl.lapc.registrodocente.modelo.Parcial;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.modelo.Quimestre;

public class Parciales extends AppCompatActivity {

    private ParcialDao dao;
    private ListView listView;

    private Quimestre quimestre;
    private Periodo periodo;
    private ParcialAdapter parcialAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parciales);
        CustomInit();
    }

    private void CustomInit(){
        Bundle bundle = getIntent().getExtras();
        quimestre = bundle.getParcelable("quimestre");
        periodo = bundle.getParcelable("periodo");

        dao = new ParcialDao(this);

        parcialAdapter = new ParcialAdapter(this, dao.getAll(quimestre));
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(parcialAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Parcial m = (Parcial) listView.getItemAtPosition(i);
                if(m != null){
                    editAction(m);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_parciales, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            editAction(new Parcial(periodo, quimestre));
            return true;
        }

        if (id == R.id.action_back) {
            back();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void back(){
        Intent intent = new Intent(this, Quimestres.class);
        intent.putExtra("periodo", periodo);
        startActivity(intent);
    }

    private void editAction(Parcial parcial){
        Intent intent = new Intent(this, EditParcial.class);
        intent.putExtra("periodo", periodo);
        intent.putExtra("quimestre", quimestre);
        intent.putExtra("parcial", parcial);
        startActivity(intent);
    }
}
