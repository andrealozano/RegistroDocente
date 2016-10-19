package com.unl.lapc.registrodocente.activity;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.dao.AcreditableDao;
import com.unl.lapc.registrodocente.dao.CalendarioDao;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Calendario;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.ItemAcreditable;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Actividad para agregar o editar items de un acreditable, es decir si tengo el acreditable "Lecciones" aquí puedo crear el item "Leccion1", "Leccion2", etc.
 */
public class EditItemAcreditable extends AppCompatActivity {

    private Clase clase;
    private Periodo periodo = null;
    private Acreditable acreditable = null;
    private ItemAcreditable itemAcreditable;

    private AcreditableDao acreditableDao = null;
    private CalendarioDao calendarioDao;

    private EditText txtAlias;
    private EditText txtNombre;
    private DatePicker dpFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_acreditable);

        customInit();
    }

    /**
     * Inicializa los daos, atributos y mustra los datos del item
     */
    public void customInit(){
        acreditableDao = new AcreditableDao(this);
        calendarioDao = new CalendarioDao(this);

        Bundle bundle = getIntent().getExtras();
        clase = bundle.getParcelable("clase");
        periodo = bundle.getParcelable("periodo");
        acreditable = bundle.getParcelable("acreditable");
        itemAcreditable = bundle.getParcelable("itemAcreditable");

        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtAlias =  (EditText)findViewById(R.id.txtAlias);
        dpFecha = (DatePicker)findViewById(R.id.dpFecha);

        Calendar c = GregorianCalendar.getInstance();
        c.setTime(itemAcreditable.getFecha());

        txtNombre.setText(itemAcreditable.getNombre());
        txtAlias.setText(itemAcreditable.getAlias());
        dpFecha.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_edit_acreditable_item, menu);
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

    /**
     * Guarda el item
     */
    public void guardar(){
        itemAcreditable.setNombre(txtNombre.getText().toString());
        itemAcreditable.setAlias(txtAlias.getText().toString());
        itemAcreditable.setFecha(Utils.toDate(dpFecha));

        if(validate()) {
            if (itemAcreditable.getId() == 0) {
                acreditableDao.addItem(itemAcreditable);
            } else {
                acreditableDao.updateItem(itemAcreditable);
            }

            atras();
        }
    }

    /**
     * Retorna a la actividad anterior
     */
    public void atras(){
        /*Intent intent = new Intent(this, MainClase.class);
        intent.putExtra("periodo", periodo);
        intent.putExtra("clase", clase);
        startActivity(intent);*/
        onBackPressed();
    }

    /**
     * Elimina el item
     */
    public void eliminar(){
        if(itemAcreditable.getId() > 0){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Remover item")
                    .setMessage("¿Desea remover este item acreditable?")
                    .setPositiveButton("Remover", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            acreditableDao.deleteItem(itemAcreditable);
                            atras();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }else{
            Snackbar.make(getCurrentFocus(), "No se puede eliminar porque aún no ha guardado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    /**
     * Valida los datos del item antes de guardar
     * @return
     */
    public boolean validate(){
        boolean b = true;

        if(itemAcreditable.getNombre().trim().length() < 1){
            txtNombre.setError("Ingrese el nombre"); b = false;
        }

        if(itemAcreditable.getAlias().trim().length() < 1){
            txtAlias.setError("Ingrese un alias"); b = false;
        }

        //Validar fecha
        Calendario c = calendarioDao.get(periodo, itemAcreditable.getFecha());
        if(c!= null){
            if(c.getEstado().equals(Calendario.ESTADO_FERIADO)){
                Toast.makeText(this, "Seleccione un día laborable (No feriado)", Toast.LENGTH_LONG).show();
                b = false;
            }
        }else{
            Toast.makeText(this, "Seleccione un día laborable", Toast.LENGTH_LONG).show();
            b = false;
        }

        return  b;
    }
}
