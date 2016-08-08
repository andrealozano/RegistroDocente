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
import com.unl.lapc.registrodocente.modelo.Calendario;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Convert;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditPeriodo extends AppCompatActivity {

    private Periodo periodo = null;
    private PeriodoDao dao = null;

    private EditText txtNombre;
    private EditText txtEscala;
    private DatePicker dpInicio;
    private DatePicker dpFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_periodo);

        txtNombre= (EditText)findViewById(R.id.txtNombre);
        txtEscala= (EditText)findViewById(R.id.txtEscala);
        dpInicio= (DatePicker)findViewById(R.id.dpInicio);
        dpFin= (DatePicker)findViewById(R.id.dpFin);

        Bundle bundle = getIntent().getExtras();
        periodo = bundle.getParcelable("periodo");

        setValue();

        dao = new PeriodoDao(getApplicationContext());
    }

    private void setValue() {
        if (periodo != null) {
            Calendar c = GregorianCalendar.getInstance();

            txtNombre.setText(periodo.getNombre());
            txtEscala.setText(""+periodo.getEscala());

            c.setTime(periodo.getInicio());
            dpInicio.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            c.setTime(periodo.getFin());
            dpFin.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_edit_periodo, menu);
        if(periodo.getId()  > 0){
            inflater.inflate(R.menu.menu_edit_periodo_extras, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            guardarPeriodo();
            return true;
        }

        if (id == R.id.action_delete) {
            eliminarPeriodo();
            return true;
        }

        if (id == R.id.action_quimestres) {
            Intent intent = new Intent(this, Quimestres.class);
            intent.putExtra("periodo", periodo);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_acreditables) {
            Intent intent = new Intent(this, Acreditables.class);
            intent.putExtra("periodo", periodo);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_calendario) {
            Intent intent = new Intent(this, Calendarios.class);
            intent.putExtra("periodo", periodo);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void guardarPeriodo(){
        periodo.setNombre(txtNombre.getText().toString());
        periodo.setEscala(Convert.toDouble(txtEscala.getText().toString()));
        periodo.setInicio(Convert.toDate(dpInicio));
        periodo.setFin(Convert.toDate(dpFin));

        if(validate()) {
            if (periodo.getId() == 0) {
                dao.add(periodo);
            } else {
                dao.update(periodo);
            }

            Intent intent = new Intent(this, Periodos.class);
            startActivity(intent);
        }
    }

    public void eliminarPeriodo(){
        if(periodo.getId() > 0){
            dao.delete(periodo);

            Intent intent = new Intent(this, Periodos.class);
            startActivity(intent);
        }else{
            Snackbar.make(getCurrentFocus(), "No se puede eliminar porque aún no ha guardado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    /*private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }*/

    private boolean validate() {
        String nombre = periodo.getNombre();

        boolean v = true;

        if (!(nombre != null && nombre.trim().length() > 0)) {
            txtNombre.setError("Ingrese un nombre");
            v = false;
        }else{
            boolean e = dao.existe(periodo);
            if(e){
                txtNombre.setError("Nombre duplicado");
                v = false;
            }
        }

        return v;
    }
}
