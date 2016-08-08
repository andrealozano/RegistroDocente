package com.unl.lapc.registrodocente.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.dao.ParcialDao;
import com.unl.lapc.registrodocente.dao.QuimestreDao;
import com.unl.lapc.registrodocente.modelo.Parcial;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.modelo.Quimestre;
import com.unl.lapc.registrodocente.util.Convert;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditParcial extends AppCompatActivity {

    private Periodo periodo = null;
    private Quimestre quimestre = null;
    private Parcial parcial = null;

    private ParcialDao dao = null;

    private EditText txtNombre;
    private EditText txtNumero;
    private DatePicker dpInicio;
    private DatePicker dpFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_parcial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CustomInit();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void CustomInit(){
        txtNombre= (EditText)findViewById(R.id.txtNombre);
        txtNumero= (EditText)findViewById(R.id.txtNumero);
        dpInicio= (DatePicker)findViewById(R.id.dpInicio);
        dpFin= (DatePicker)findViewById(R.id.dpFin);

        Bundle bundle = getIntent().getExtras();
        periodo = bundle.getParcelable("periodo");
        quimestre = bundle.getParcelable("quimestre");
        parcial = bundle.getParcelable("parcial");

        setValue();

        dao = new ParcialDao(this);
    }

    private void setValue() {
        Calendar c = GregorianCalendar.getInstance();

        txtNombre.setText(parcial.getNombre());
        txtNumero.setText(""+parcial.getNumero());

        c.setTime(parcial.getInicio());
        dpInicio.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        c.setTime(parcial.getFin());
        dpFin.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_edit_parcial, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            guardar();
            return true;
        }

        if (id == R.id.action_delete) {
            eliminar();
            return true;
        }

        if (id == R.id.action_back) {
            atras();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void guardar(){
        parcial.setNombre(txtNombre.getText().toString());
        parcial.setNumero(Convert.toInt(txtNumero.getText().toString()));
        parcial.setInicio(Convert.toDate(dpInicio));
        parcial.setFin(Convert.toDate(dpFin));

        if(validate()) {
            if (parcial.getId() == 0) {
                dao.add(parcial);
            } else {
                dao.update(parcial);
            }

            atras();
        }
    }

    public void atras(){
        Intent intent = new Intent(this, Parciales.class);
        intent.putExtra("periodo", periodo);
        intent.putExtra("quimestre", quimestre);
        startActivity(intent);
    }

    public void eliminar(){
        if(parcial.getId() > 0){
            dao.delete(parcial);
            atras();
        }else{
            Snackbar.make(getCurrentFocus(), "No se puede eliminar porque aún no ha guardado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }


    private boolean validate() {
        String nombre = parcial.getNombre();

        boolean v = true;

        if (!(nombre != null && nombre.trim().length() > 0)) {
            txtNombre.setError("Ingrese un nombre");
            v = false;
        }else{
            /*boolean e = dao.existe(periodo);
            if(e){
                txtNombre.setError("Nombre duplicado");
                v = false;
            }*/
        }

        return v;
    }

}
