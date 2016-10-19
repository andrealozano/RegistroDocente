package com.unl.lapc.registrodocente.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Periodo;

/**
 * Actividad para editar los datos de un estudiante
 */
public class EditEstudiante extends AppCompatActivity {

    private EstudianteDao estudianteDao;
    private ClaseDao claseDao;

    private Clase clase;
    private Periodo periodo;
    private Estudiante estudiante;
    //private ClaseEstudiante claseEstudiante;

    private EditText txtCodigo;
    private EditText txtNombres;
    private EditText txtApellidos;
    private EditText txtEmail;
    private EditText txtTelefono;
    //private EditText txtOrden;
    private RadioGroup rgSexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_estudiante);

        estudianteDao = new EstudianteDao(getApplicationContext());
        claseDao = new ClaseDao(getApplicationContext());

        txtCodigo = (EditText)findViewById(R.id.txtCodigo);
        txtNombres = (EditText)findViewById(R.id.txtNombres);
        txtApellidos = (EditText)findViewById(R.id.txtApellidos);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtTelefono = (EditText)findViewById(R.id.txtTelefono);
        txtTelefono = (EditText)findViewById(R.id.txtTelefono);
        //txtOrden = (EditText)findViewById(R.id.txtOrden);
        rgSexo =(RadioGroup)findViewById(R.id.rgSexo);

        Bundle bundle = getIntent().getExtras();
        clase = bundle.getParcelable("clase");
        estudiante = bundle.getParcelable("estudiante");
        periodo =  bundle.getParcelable("periodo");

        if(estudiante.getId() > 0){
            //Recarga porque solo vienen datos generales
            estudiante = estudianteDao.get(estudiante.getId());
        }

        fijarValores();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_edit_estudiante, menu);
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
            cancelar();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Guarda el estudiante en edición
     */
    private void guardar(){

        estudiante.setCedula(txtCodigo.getText().toString());
        estudiante.setNombres(txtNombres.getText().toString());
        estudiante.setApellidos(txtApellidos.getText().toString());
        estudiante.setEmail(txtEmail.getText().toString());
        estudiante.setCelular(txtTelefono.getText().toString());
        estudiante.setClase(clase);
        //estudiante.setOrden(Integer.parseInt(txtOrden.getText().toString()));
        int rbSexo = rgSexo.getCheckedRadioButtonId();
        if(rbSexo == R.id.rbHombre){
            estudiante.setSexo("Hombre");
        }else{
            estudiante.setSexo("Mujer");
        }

        if(validar()) {

            if (estudiante.getId() == 0) {
                estudianteDao.add(estudiante);
            } else {
                estudianteDao.update(estudiante);
            }
        }

        cancelar();

    }

    /**
     * Valida los datos ingresados antes de guardar
     * @return
     */
    private boolean validar(){
        boolean b = true;

        if(estudiante.getNombres().trim().length() < 1){
            txtNombres.setError("Ingrese el nombre"); b = false;
        }

        if(estudiante.getApellidos().trim().length() < 1){
            txtApellidos.setError("Ingrese el apellido"); b = false;
        }

        return  b;
    }

    /**
     * Método para eliminar un estudiante del curso
     */
    private void eliminar(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Remover estudiante")
                .setMessage("¿Desea remover el estudiante de esta clase?")
                .setPositiveButton("Remover", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        estudianteDao.delete(estudiante);
                        cancelar();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Muestra los datos del estudiante en la vista
     */
    private void fijarValores() {
        if (clase != null) {
            txtCodigo.setText(estudiante.getCedula());
            txtNombres.setText(estudiante.getNombres());
            txtApellidos.setText(estudiante.getApellidos());
            txtEmail.setText(estudiante.getEmail());
            txtTelefono.setText(estudiante.getCelular());
            if(estudiante.getSexo().equals("Hombre")){
                rgSexo.check(R.id.rbHombre);
            }else{
                rgSexo.check(R.id.rbMujer);
            }
            //txtOrden.setText(""+estudiante.getOrden());
        }
    }

    /*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainClaseActivity.class);
        intent.putExtra("clase", clase);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }*/

    /**
     * Retorna a la actividad MainClase
     */
    public void cancelar() {
        Intent mIntent = new Intent(this, MainClase.class);
        mIntent.putExtra("clase", clase);
        startActivity(mIntent);
        finish();
    }
}
