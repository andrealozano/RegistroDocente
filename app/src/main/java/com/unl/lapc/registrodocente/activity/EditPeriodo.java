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
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.dao.CalendarioDao;
import com.unl.lapc.registrodocente.dao.PeriodoDao;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Actividad para editar un periodo académico
 */
public class EditPeriodo extends AppCompatActivity {

    private Periodo periodo = null;
    private PeriodoDao dao = null;
    private CalendarioDao calendarioDao;

    private EditText txtNombre;
    private EditText txtEscala;
    private EditText txtQuimestres;
    private EditText txtParciales;
    private TextView txtInicio;
    private TextView txtFin;

    private EditText txtEqvParciales;
    private EditText txtEqvExamenes;
    private EditText txtPorcentajeAsis;
    private EditText txtNotaMinima;

    /*
    private DatePicker dpInicio;
    private DatePicker dpFin;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_periodo);

        txtNombre= (EditText)findViewById(R.id.txtNombre);
        txtEscala= (EditText)findViewById(R.id.txtEscala);
        txtQuimestres= (EditText)findViewById(R.id.txtQuimestres);
        txtParciales= (EditText)findViewById(R.id.txtParciales);
        //dpInicio= (DatePicker)findViewById(R.id.dpInicio);
        //dpFin= (DatePicker)findViewById(R.id.dpFin);
        txtInicio = (TextView)findViewById(R.id.txtInicio);
        txtFin = (TextView)findViewById(R.id.txtFin);

        txtEqvParciales= (EditText)findViewById(R.id.txtEqvParciales);
        txtEqvExamenes= (EditText)findViewById(R.id.txtEqvExamenes);
        txtPorcentajeAsis= (EditText)findViewById(R.id.txtPorcentajeAsis);
        txtNotaMinima= (EditText)findViewById(R.id.txtNotaMinima);

        txtInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFecha("inicio", txtInicio);
            }
        });

        txtFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFecha("fin", txtFin);
            }
        });


        Bundle bundle = getIntent().getExtras();
        periodo = bundle.getParcelable("periodo");

        fijarValores();

        dao = new PeriodoDao(this);
        calendarioDao = new CalendarioDao(this);
    }

    /**
     * Muestra los datos del periodo en la vista
     */
    private void fijarValores() {
        if (periodo != null) {
            //Calendar c = GregorianCalendar.getInstance();

            txtNombre.setText(periodo.getNombre());
            txtEscala.setText(""+periodo.getEscala());
            txtQuimestres.setText(""+periodo.getQuimestres());
            txtParciales.setText(""+periodo.getParciales());

            //c.setTime(periodo.getInicio());
            //dpInicio.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            //c.setTime(periodo.getFin());
            //dpFin.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            txtInicio.setText(Utils.toShortDateString(periodo.getInicio()));
            txtFin.setText(Utils.toShortDateString(periodo.getFin()));

            txtEqvParciales.setText(""+periodo.getEquivalenciaParciales());
            txtEqvExamenes.setText(""+periodo.getEquivalenciaExamenes());
            txtPorcentajeAsis.setText(""+periodo.getPorcentajeAsistencias());
            txtNotaMinima.setText(""+periodo.getNotaMinima());
        }
    }

    /**
     * Regresa a la actividad padre "Periodos"
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Periodos.class);
        startActivity(intent);
        finish();
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
            guardar();
            return true;
        }

        if (id == R.id.action_delete) {
            eliminar();
            return true;
        }

        /*if (id == R.id.action_quimestres) {
            Intent intent = new Intent(this, Quimestres.class);
            intent.putExtra("periodo", periodo);
            startActivity(intent);
            return true;
        }*/

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

    /**
     * Guarda el periodo
     */
    private void guardar(){
        periodo.setNombre(txtNombre.getText().toString());
        periodo.setEscala(Utils.toDouble(txtEscala.getText().toString()));
        //periodo.setInicio(Utils.toDate(dpInicio));
        //periodo.setFin(Utils.toDate(dpFin));
        periodo.setQuimestres(Utils.toInt(txtQuimestres.getText().toString()));
        periodo.setParciales(Utils.toInt(txtParciales.getText().toString()));

        periodo.setEquivalenciaParciales(Utils.toDouble(txtEqvParciales.getText().toString()));
        periodo.setEquivalenciaExamenes(Utils.toDouble(txtEqvExamenes.getText().toString()));
        periodo.setPorcentajeAsistencias(Utils.toDouble(txtPorcentajeAsis.getText().toString()));
        periodo.setNotaMinima(Utils.toDouble(txtNotaMinima.getText().toString()));

        if(validar()) {
            if (periodo.getId() == 0) {
                dao.add(periodo);
            } else {
                dao.update(periodo);
            }

            calendarioDao.registrar(periodo);

            Intent intent = new Intent(this, Periodos.class);
            startActivity(intent);
        }
    }

    /**
     * Elimina el periodo
     */
    private void eliminar(){
        if(periodo.getId() > 0){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Remover periodo")
                    .setMessage("¿Desea remover esta periodo académico?")
                    .setPositiveButton("Remover", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.delete(periodo);
                            Intent intent = new Intent(EditPeriodo.this, Periodos.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
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

    /**
     * Valida el periodo antes de guardar
     * @return
     */
    private boolean validar() {
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

    /**
     * Muestra un diálogo para seleccionar la fecha de inicio y fin del periodo
     * @param tipo Tipo de fecha (Inicio o Fin)
     * @param txt TextView para mostrar la fecha luego de seleccionarla
     */
    private void showDialogFecha(final String tipo, final TextView txt){
        final View myView = View.inflate(this, R.layout.content_dlg_fecha, null);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(myView);
        builder.setTitle("Fecha de " + tipo);
        builder.setCancelable(false);

        final DatePicker dpFecha = (DatePicker)myView.findViewById(R.id.dpFecha);
        final Calendar c = GregorianCalendar.getInstance();

        if(tipo.equals("inicio")){
            c.setTime(periodo.getInicio());
        }else{
            c.setTime(periodo.getFin());
        }


        dpFecha.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        builder.setPositiveButton("Asignar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                c.set(dpFecha.getYear(),dpFecha.getMonth(),dpFecha.getDayOfMonth());
                Date fecha = c.getTime();
                if(tipo.equals("inicio")){
                    periodo.setInicio(fecha);
                }else{
                    periodo.setFin(fecha);
                }
                txt.setText(Utils.toShortDateString(fecha));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cerrar",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.dismiss();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();
    }
}
