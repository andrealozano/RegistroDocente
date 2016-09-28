package com.unl.lapc.registrodocente.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.dao.PeriodoDao;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.List;

public class EditClase extends AppCompatActivity {

    private Clase clase = null;
    private ClaseDao claseDao = null;
    private PeriodoDao periodoDao = null;

    private EditText txtNombreEditClase=null;
    private Spinner cmbPeriodos;
    private CheckBox chkActivaClase;

    private ArrayAdapter periodosAdapter = null;
    private int selectedIndexPeriodo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clase);

        claseDao = new ClaseDao(getApplicationContext());
        periodoDao = new PeriodoDao(getApplicationContext());

        txtNombreEditClase= (EditText)findViewById(R.id.txtNombreEditClase);
        cmbPeriodos = (Spinner)findViewById(R.id.spPeriodos);
        chkActivaClase= (CheckBox)findViewById(R.id.chkActivaClase);

        Bundle bundle = getIntent().getExtras();
        clase = bundle.getParcelable("clase");
        if(clase.getId() > 0){
            clase = claseDao.get(clase.getId());
        }

        List<Periodo> periodos = periodoDao.getAll();

        periodosAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, periodos);
        periodosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbPeriodos.setAdapter(periodosAdapter);

        selectedIndexPeriodo = periodos.indexOf(clase.getPeriodo());

        fijarValores();
    }


    private void fijarValores() {
        if (clase != null) {
            txtNombreEditClase.setText(clase.getNombre());
            cmbPeriodos.setSelection(selectedIndexPeriodo);
            chkActivaClase.setChecked(clase.isActiva());

            Log.i("Sel periodo: ", "Index: " + selectedIndexPeriodo);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Clases.class);
        startActivity(intent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_edit_clase, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void guardar(){
        Periodo per = (Periodo)cmbPeriodos.getSelectedItem();

        clase.setNombre(txtNombreEditClase.getText().toString());
        clase.setPeriodo(per);
        clase.setActiva(chkActivaClase.isChecked());

        if(validar()) {

            if (clase.getId() == 0) {
                claseDao.add(clase);
            } else {
                claseDao.update(clase);
            }

            onBackPressed();
        }
    }

    private void eliminar(){
        if(clase.getId() > 0){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Remover clase")
                    .setMessage("¿Desea remover este  curso?")
                    .setPositiveButton("Remover", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            claseDao.delete(clase);

                            onBackPressed();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }else{
            Snackbar.make(getCurrentFocus(), "No se puede eliminar porque aún no ha guardado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private boolean validar() {
        String nombre = clase.getNombre();

        boolean v = true;

        if (!(nombre != null && nombre.trim().length() > 0)) {
            txtNombreEditClase.setError("Ingrese un nombre");
            v = false;
        }else{
            /*boolean e = claseDao.existe(clase);
            if(e){
                txtNombreEditClase.setError("Nombre duplicado");
                v = false;
            }*/
        }

        if(clase.getPeriodo() == null){
            v= false;
        }

        return v;
    }
}
