package com.unl.lapc.registrodocente.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.dao.AcreditableDao;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Actividad para editar un acreditable del periodo seleccionado
 */
public class EditAcreditable extends AppCompatActivity {

    private Periodo periodo = null;
    private Acreditable acreditable = null;
    private String tipo;

    private AcreditableDao acreditableDao = null;

    private EditText txtNombre;
    private EditText txtAlias;
    //private RadioGroup rgTipo;
    private EditText txtEquivalencia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_acreditable);

        txtNombre= (EditText)findViewById(R.id.txtNombre);
        txtAlias= (EditText)findViewById(R.id.txtAlias);
        //rgTipo= (RadioGroup)findViewById(R.id.rgTipo);
        txtEquivalencia= (EditText)findViewById(R.id.txtEquivalencia);

        Bundle bundle = getIntent().getExtras();
        periodo = bundle.getParcelable("periodo");
        acreditable = bundle.getParcelable("acreditable");
        tipo = bundle.getString("tipo");
        setTitle("Editar acreditables: " + tipo);

        fijarValores();

        acreditableDao = new AcreditableDao(this);
    }

    /**
     * Muestra los datos del acreditable en la vista
     */
    private void fijarValores() {
        Calendar c = GregorianCalendar.getInstance();

        txtNombre.setText(acreditable.getNombre());
        txtAlias.setText(acreditable.getAlias());
        txtEquivalencia.setText(""+acreditable.getEquivalencia());

        /*if(acreditable.getTipo().equals(Acreditable.TIPO_ACREDITABLE_PARCIAL)){
            rgTipo.check(R.id.rbParcial);
        }else{
            rgTipo.check(R.id.rbQuimestre);
        }*/
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_edit_acreditable, menu);
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

        /*if (id == R.id.action_back) {
            atras();
            return true;
        }*/

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Guarda el acreditable en edición
     */
    private void guardar(){
        if(!acreditableDao.existenNotas(periodo)) {
            acreditable.setNombre(txtNombre.getText().toString());
            acreditable.setAlias(txtAlias.getText().toString());
            acreditable.setEquivalencia(Utils.toDouble(txtEquivalencia.getText().toString()));
            acreditable.setTipo(tipo);
            /*int rbTipo = rgTipo.getCheckedRadioButtonId();
            if (rbTipo == R.id.rbParcial) {
                acreditable.setTipo(Acreditable.TIPO_ACREDITABLE_PARCIAL);
            } else {
                acreditable.setTipo(Acreditable.TIPO_ACREDITABLE_QUIMESTRE);
            }*/

            if (validar()) {
                if (acreditable.getId() == 0) {
                    acreditableDao.add(acreditable);
                } else {
                    acreditableDao.update(acreditable);
                }

                onBackPressed();
            }
        }else{
            Snackbar.make(txtNombre, "No se puede guardar, porque ya ha ingresado notas.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    /**
     * Regresa a la actividad Acreditables
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Acreditables.class);
        intent.putExtra("periodo", periodo);
        intent.putExtra("tipo", tipo);
        startActivity(intent);
        finish();
    }

    /**
     * Método invocado para eliminar un acreditable
     */
    public void eliminar(){
        if(acreditable.getId() > 0){
            if(!acreditableDao.existenNotas(periodo)) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Remover acreditable")
                        .setMessage("¿Desea remover este acreditable?")
                        .setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                acreditableDao.delete(acreditable);
                                onBackPressed();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }else{
                Snackbar.make(txtNombre, "No se puede remover, porque ya ha ingresado notas.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }else{
            Snackbar.make(getCurrentFocus(), "No se puede eliminar porque aún no ha guardado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }


    /**
     * Valida los datos del acreditable antes de guardarlo
     * @return
     */
    private boolean validar() {
        String nombre = acreditable.getNombre();

        boolean v = true;

        if (!(nombre != null && nombre.trim().length() > 0)) {
            txtNombre.setError("Ingrese un nombre");
            v = false;
        }else{
            if(acreditableDao.existeNombre(acreditable)){
                txtNombre.setError("Nombre duplicado");
                v = false;
            }
        }

        if(acreditable.getEquivalencia() > 100 || acreditable.getEquivalencia() <= 0){
            txtEquivalencia.setError("Porcentaje entre 1 y 100");
        }

        return v;
    }
}
