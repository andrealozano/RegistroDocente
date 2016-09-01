package com.unl.lapc.registrodocente.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.adapter.AcreditableAdapter;
import com.unl.lapc.registrodocente.adapter.CalendarioAdapter;
import com.unl.lapc.registrodocente.dao.CalendarioDao;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Calendario;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Convert;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Calendarios extends AppCompatActivity {

    private Periodo periodo;
    private CalendarioDao dao;
    private Date inicio;
    private Date fin;
    private Date fecha;
    private Calendar calendar = GregorianCalendar.getInstance();

    private ListView listView;
    private CalendarioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarios);

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Calendario m = (Calendario) listView.getItemAtPosition(i);
                if(m != null){
                    showDialogCalendario(m);
                }
            }
        });

        dao = new CalendarioDao(this);

        Bundle bundle = getIntent().getExtras();
        periodo = bundle.getParcelable("periodo");
        //dao.registrar(periodo);

        inicio = periodo.getInicio();
        fin = periodo.getFin();
        fecha = periodo.getInicio();

        FloatingActionButton btnAsiNext = (FloatingActionButton) findViewById(R.id.btnCalNext);
        btnAsiNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });

        FloatingActionButton btnAsiPrev = (FloatingActionButton) findViewById(R.id.btnCalPrev);
        btnAsiPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });


        mostrarMes(fecha);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendarios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.action_refresh) {
            registrar();
            return true;
        }*/

        if (id == R.id.action_back) {
            Intent intent = new Intent(this, Periodos.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void next(){
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(periodo.getFin());
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date fin = c.getTime();

        c.setTime(fecha);
        c.add(Calendar.MONTH, 1);
        Date time = c.getTime();

        if(time.before(fin) ||c.getTime().equals(fin)){
            mostrarMes(time);
        }
    }

    private void prev(){
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(periodo.getInicio());
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date fi = c.getTime();

        c.setTime(fecha);
        c.add(Calendar.MONTH, -1);
        Date time = c.getTime();

        long t1 = time.getTime();
        long t2 = fi.getTime();

        if(time.after(fi) || time.equals(fi)){
            mostrarMes(time);
        }
    }

    /*private void registrar(){
        dao.registrar(periodo);
        mostrarMes(periodo.getInicio());
    }*/

    private void mostrarMes(Date fecha){
        calendar.setTime(fecha);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Date i = calendar.getTime();
        this.fecha = i;
        setTitle("Calendario: " + Convert.toYearMonthNameString(fecha));

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date f = calendar.getTime();

        adapter = new CalendarioAdapter(this, dao.getAll(periodo, i, f));
        listView.setAdapter(adapter);
    }

    private void showDialogCalendario(final Calendario m){
        View myView = View.inflate(this, R.layout.content_dlg_calendario, null);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(myView);
        builder.setTitle("Calendario");
        builder.setCancelable(false);

        final EditText txtObs = (EditText)myView.findViewById(R.id.txtObservacion);
        final RadioGroup rgEstado = (RadioGroup) myView.findViewById(R.id.rgEstado);

        txtObs.setText(m.getObservacion());
        if(m.getEstado().equals(Calendario.ESTADO_ACTIVO)){
            rgEstado.check(R.id.rbActivo);
        }else{
            rgEstado.check(R.id.rbFeriado);
        }

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                m.setObservacion(txtObs.getText().toString());
                if(rgEstado.getCheckedRadioButtonId()==R.id.rbActivo){
                    m.setEstado(Calendario.ESTADO_ACTIVO);
                }else{
                    m.setEstado(Calendario.ESTADO_FERIADO);
                }
                dao.update(m);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.dismiss();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();
    }


}
