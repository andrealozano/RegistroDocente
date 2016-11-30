package com.unl.lapc.registrodocente.fragment;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.activity.MainClase;
import com.unl.lapc.registrodocente.dao.AsistenciaDao;
import com.unl.lapc.registrodocente.dao.CalendarioDao;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.modelo.Asistencia;
import com.unl.lapc.registrodocente.modelo.Calendario;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Periodo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Fragmento para mostrar las asistencias de los estudiantes por día.
 */
public class FragmentAsistancias extends Fragment {

    private Clase clase;
    private Periodo periodo;

    //private ListView mLeadsList;
    private AsistenciaDao asistenciaDao;
    private ClaseDao claseDao;
    private EstudianteDao estudianteDao;
    private CalendarioDao calendarioDao;

    //private RegistroAsistenciaAdapter mLeadsAdapter;
    private Date fecha = new Date();
    private TableLayout tlAsistencias;
    private List<Asistencia> asistencias;
    private List<Estudiante> estudiantes;
    private Calendario calendario;
    private boolean cancelarMarcar = false;

    @Override
    //protected void onCreate(Bundle savedInstanceState) {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_asistencias);

        View view = inflater.inflate(R.layout.fragment_asistencias, container, false);
        setHasOptionsMenu(true);

        asistenciaDao = new AsistenciaDao(getContext());
        claseDao = new ClaseDao(getContext());
        estudianteDao = new EstudianteDao(getContext());
        calendarioDao = new CalendarioDao(getContext());

        Bundle bundle = getArguments();
        clase = bundle.getParcelable("clase");
        periodo = bundle.getParcelable("periodo");

        //mLeadsList = (ListView) findViewById(R.id.listViewAsistencias);
        //mLeadsAdapter = new RegistroAsistenciaAdapter(getApplicationContext(), asistenciaDao.getEstudiantes(clase));
        //mLeadsList.setAdapter(mLeadsAdapter);
        estudiantes = estudianteDao.getEstudiantes(clase);

        /*FloatingActionButton btnAsiToday = (FloatingActionButton) view.findViewById(R.id.btnAsiToday);
        btnAsiToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendario newCal = calendarioDao.get(clase.getPeriodo(), fecha);
                registrar(newCal);
            }
        });*/

        FloatingActionButton btnAsiNext = (FloatingActionButton) view.findViewById(R.id.btnAsiNext);
        btnAsiNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });

        FloatingActionButton btnAsiPrev = (FloatingActionButton) view.findViewById(R.id.btnAsiPrev);
        btnAsiPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });

        tlAsistencias = (TableLayout) view.findViewById(R.id.tlAsistencias);

        calendario = calendarioDao.getLast(clase.getPeriodo(), new Date());
        if (calendario != null) {
            mostrarDia(calendario);
        } else {
            //Snackbar.make(getView(), "Este día no está registrdo en el calendario académico", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            getActivity().setTitle("Asistencias: " + clase.getNombre());
        }

        return view;
    }

    @Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_asistencias, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_periodo_clase) {
            showDialogToday();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    DatePicker dp;

    /**
     * Muestra el díalogo para seleccionar el día.
     */
    private void showDialogToday(){
        View myView = View.inflate(getContext(), R.layout.content_dlg_periodo_clase, null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(myView);
        builder.setTitle("Seleccionar día");
        builder.setCancelable(false);

        Calendar c = GregorianCalendar.getInstance();
        c.setTime(fecha);

        dp = (DatePicker)myView.findViewById(R.id.dpFechaPeriodoClase);
        dp.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        builder.setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                GregorianCalendar calendarBeg=new GregorianCalendar(dp.getYear(),dp.getMonth(),dp.getDayOfMonth());
                Date fecha=calendarBeg.getTime();
                Calendario newCal = calendarioDao.get(clase.getPeriodo(), fecha);
                if(newCal != null) {
                    //registrar(newCal);
                    mostrarDia(newCal);
                    dialog.dismiss();
                }else{
                    Snackbar.make(tlAsistencias, R.string.msg_dia_no_calendario, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        //builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
          //  public void onClick(DialogInterface dialog, int id) {
                //GregorianCalendar calendarBeg=new GregorianCalendar(dp.getYear(),dp.getMonth(),dp.getDayOfMonth());
                //Date fecha=calendarBeg.getTime();
                //Calendario newCal = calendarioDao.get(clase.getPeriodo(), fecha);
                //if(newCal != null) {
                    //asistenciaDao.borrarAsistencias(clase, fecha);
                  //  mostrarDia(newCal);
                    //dialog.dismiss();
                //}
            //}
        //});

        builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.dismiss();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();
    }

    /*private void registrar(Calendario calendario){
        if(calendario != null) {
            if(calendario.getEstado().equals(Calendario.ESTADO_ACTIVO)){

                asistencias = asistenciaDao.getAsistencias(clase, fecha);
                boolean any = asistencias.size() > 0;

                for(Estudiante c : estudiantes) {
                    Asistencia asi = null;

                    for (Asistencia ra : asistencias) {
                        if (ra.getEstudiante().getId() == c.getId()) {
                            asi = ra;
                        }
                    }

                    if (asi == null) {
                        asi = new Asistencia(0, fecha, c.get(), c, calendario, periodo);
                        asi.setEstado(any ? "F" : "P");
                        asistenciaDao.add(asi);
                    }
                }

                mostrarDia(calendario);
            }else{
                Snackbar.make(tlAsistencias, "Este día no está registrdo en el calendario académico como feriado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }else{
            Snackbar.make(tlAsistencias, "Este día no está registrdo en el calendario académico", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }*/

    /**
     * Crea la cabecera de la tabla.
     * @param marcar
     */
    private void createTh(boolean marcar){
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row.setBackgroundColor(getResources().getColor(R.color.backgroundTh));

        TextView tv1 = new TextView(getContext());
        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(18);
        tv1.setPadding(5, 5, 5, 5);
        tv1.setBackgroundResource(R.drawable.cell_shape_head);
        tv1.setText("N°");
        row.addView(tv1);

        TextView tv2 = new TextView(getContext());
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(18);
        tv2.setPadding(5, 5, 5, 5);
        tv2.setBackgroundResource(R.drawable.cell_shape_head);
        tv2.setText("NOMBRES");
        row.addView(tv2);

        if(calendario != null && calendario.getEstado().equals(Calendario.ESTADO_ACTIVO)) {

            final CheckBox chk = new CheckBox(getContext());
            chk.setChecked(marcar);
            row.addView(chk);

            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                    if (asistencias != null && cancelarMarcar == false) {
                        new AlertDialog.Builder(getContext())
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(b ? "Marcar todo" : "Desmarcar todo")
                                .setMessage(b ? "¿Desea marcar a todos los estudiantes como presentes?" : "¿Desea marcar a todos los estudiantes como ausentes?")
                                .setPositiveButton(b ? "Marcar" : "Desmarcar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (Asistencia as : asistencias) {
                                            as.setEstado(b ? "P" : "F");
                                            asistenciaDao.update(as, clase, periodo);
                                        }
                                        mostrarDia(calendario);
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        cancelarMarcar = true;
                                        chk.setChecked(!chk.isChecked());
                                    }
                                })
                                .show();
                    }

                    if (cancelarMarcar) {
                        cancelarMarcar = false;
                    }
                }
            });
        }

        tlAsistencias.addView(row);
    }

    /**
     * Muestra las asistencias del día calendario.
     * @param calendario
     */
    private void mostrarDia(Calendario calendario){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        this.fecha = calendario.getFecha();
        this.calendario = calendario;

        ((MainClase)getActivity()).getSupportActionBar().setTitle(sd.format(fecha)+ " - " + calendario.getEstado());

        tlAsistencias.removeAllViews();
        asistencias = asistenciaDao.getAsistencias(clase, fecha);

        int marcadas = 0;
        for(Asistencia a: asistencias){
            if(a.getEstado().equals("P")){
                marcadas += 1;
            }
        }

        createTh(marcadas == asistencias.size() && asistencias.size() > 0);

        int i = 0;
        for(Estudiante c : estudiantes){
            i++;
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            Asistencia asi = null;

            for (Asistencia ra : asistencias){
                if(ra.getEstudiante().getId() == c.getId()){
                    asi = ra;
                }
            }

            TextView tv = new TextView(getContext());
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(0, 5, 0, 5);
            tv.setText(i + ". ");
            row.addView(tv);

            TextView tv1 = new TextView(getContext());
            tv1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            tv1.setGravity(Gravity.LEFT);
            tv1.setTextSize(18);
            tv1.setPadding(0, 5, 0, 5);
            tv1.setText(c.getNombresCompletos());
            row.addView(tv1);

            if(calendario.getEstado().equals(Calendario.ESTADO_ACTIVO)){
                if (asi == null) {
                    asi = new Asistencia(0, fecha, clase, c, calendario, periodo);
                    asi.setEstado("F");
                    asistenciaDao.add(asi, periodo);

                    asistencias.add(asi);
                }
            }

            if(asi!=null) {
                CheckBox chk = new CheckBox(getContext());
                chk.setTag(asi);
                if(asi.getEstado().equals("P")){
                    chk.setChecked(true);
                }else{
                    chk.setChecked(false);
                }

                chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        Asistencia as = (Asistencia) compoundButton.getTag();
                        as.setEstado(b ? "P" :  "F");
                        asistenciaDao.update(as, clase, periodo);
                    }
                });

                row.addView(chk);
            }

            tlAsistencias.addView(row);
        }

        //Snackbar.make(this.getCurrentFocus(), "Fecha seleccionada: " , Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    /**
     * Muestra el día siguiente
     */
    private void next(){
        Calendario newcal = calendarioDao.getNext(clase.getPeriodo(), fecha);

        if(newcal != null) {
            mostrarDia(newcal);
        }
    }

    /**
     * Muestra el día anterior
     */
    private void prev(){
        Calendario newcal = calendarioDao.getPrevius(clase.getPeriodo(), fecha);
        if(newcal != null) {
            mostrarDia(newcal);
        }
    }

    /*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainClaseActivity.class);
        intent.putExtra("clase", clase);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }*/
}
