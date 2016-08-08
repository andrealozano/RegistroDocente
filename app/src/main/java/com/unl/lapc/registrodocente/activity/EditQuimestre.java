package com.unl.lapc.registrodocente.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.dao.PeriodoDao;
import com.unl.lapc.registrodocente.dao.QuimestreDao;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.modelo.Quimestre;
import com.unl.lapc.registrodocente.util.Convert;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditQuimestre extends AppCompatActivity {

    private Periodo periodo = null;
    private Quimestre quimestre = null;

    private QuimestreDao dao = null;

    private EditText txtNombre;
    private EditText txtNumero;
    private DatePicker dpInicio;
    private DatePicker dpFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quimestre);

        txtNombre= (EditText)findViewById(R.id.txtNombre);
        txtNumero= (EditText)findViewById(R.id.txtNumero);
        dpInicio= (DatePicker)findViewById(R.id.dpInicio);
        dpFin= (DatePicker)findViewById(R.id.dpFin);

        Bundle bundle = getIntent().getExtras();
        periodo = bundle.getParcelable("periodo");
        quimestre = bundle.getParcelable("quimestre");

        setValue();

        dao = new QuimestreDao(this);
    }

    private void setValue() {
        Calendar c = GregorianCalendar.getInstance();

        txtNombre.setText(quimestre.getNombre());
        txtNumero.setText(""+quimestre.getNumero());

        c.setTime(quimestre.getInicio());
        dpInicio.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        c.setTime(quimestre.getFin());
        dpFin.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_edit_quimestre, menu);
        if(quimestre.getId() > 0){
            inflater.inflate(R.menu.menu_edit_quimestre_extras, menu);
        }
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

        if (id == R.id.action_parciales) {
            parciales();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void guardar(){
        quimestre.setNombre(txtNombre.getText().toString());
        quimestre.setNumero(Convert.toInt(txtNumero.getText().toString()));
        quimestre.setInicio(Convert.toDate(dpInicio));
        quimestre.setFin(Convert.toDate(dpFin));

        if(validate()) {
            if (quimestre.getId() == 0) {
                dao.add(quimestre);
            } else {
                dao.update(quimestre);
            }

            atras();
        }
    }

    public void atras(){
        Intent intent = new Intent(this, Quimestres.class);
        intent.putExtra("periodo", periodo);
        startActivity(intent);
    }

    public void parciales(){
        Intent intent = new Intent(this, Parciales.class);
        intent.putExtra("periodo", periodo);
        intent.putExtra("quimestre", quimestre);
        startActivity(intent);
    }

    public void eliminar(){
        if(quimestre.getId() > 0){
            dao.delete(quimestre);
            atras();
        }else{
            Snackbar.make(getCurrentFocus(), "No se puede eliminar porque aún no ha guardado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }


    private boolean validate() {
        String nombre = quimestre.getNombre();

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
