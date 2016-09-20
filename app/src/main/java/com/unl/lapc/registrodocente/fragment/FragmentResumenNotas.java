package com.unl.lapc.registrodocente.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.dto.ResumenGeneral;
import com.unl.lapc.registrodocente.dto.ResumenQuimestre;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Utils;

import java.io.File;
import java.util.List;

public class FragmentResumenNotas extends Fragment {

    static final int PICK_DESTINO_REPORTE_REQUEST = 1;

    private EstudianteDao estudianteDao;
    private Clase clase;
    private Periodo periodo;
    private TableLayout tlResumenNotas;
    private List<Estudiante> lista;
    private File emailFile;

    public FragmentResumenNotas() {
        // Required empty public constructor
    }

    private void cargarTh(){
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


        TextView tv4 = new TextView(getContext());
        tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextSize(18);
        tv4.setPadding(5, 5, 5, 5);
        tv4.setBackgroundResource(R.drawable.cell_shape_head);
        tv4.setText("ESTADO");
        row.addView(tv4);

        TextView tv5 = new TextView(getContext());
        tv5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv5.setGravity(Gravity.CENTER);
        tv5.setTextSize(18);
        tv5.setPadding(5, 5, 5, 5);
        tv5.setBackgroundResource(R.drawable.cell_shape_head);
        tv5.setText("NOTA");
        row.addView(tv5);

        TextView tv6 = new TextView(getContext());
        tv6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv6.setGravity(Gravity.CENTER);
        tv6.setTextSize(18);
        tv6.setPadding(5, 5, 5, 5);
        tv6.setBackgroundResource(R.drawable.cell_shape_head);
        tv6.setText("ASISTENCIA");
        row.addView(tv6);

        tlResumenNotas.addView(row);
    }

    private void cargarTr(){
        Bundle args = getArguments();
        this.clase = args.getParcelable("clase");
        this.periodo = args.getParcelable("periodo");

        estudianteDao = new EstudianteDao(getContext());
        lista = estudianteDao.getEstudiantes(clase);

        for(int i= 0; i < lista.size(); i++){
            Estudiante e = lista.get(i);

            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv1 = new TextView(getContext());
            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv1.setGravity(Gravity.CENTER);
            tv1.setTextSize(18);
            tv1.setPadding(5, 5, 5, 5);
            tv1.setBackgroundResource(R.drawable.cell_shape);
            tv1.setText(e.getOrden() + ". ");
            row.addView(tv1);

            TextView tv2 = new TextView(getContext());
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv2.setGravity(Gravity.CENTER);
            tv2.setTextSize(18);
            tv2.setPadding(5, 5, 5, 5);
            tv2.setBackgroundResource(R.drawable.cell_shape);
            tv2.setText(e.getNombresCompletos());
            row.addView(tv2);

            TextView tv3 = new TextView(getContext());
            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv3.setGravity(Gravity.CENTER);
            tv3.setTextSize(18);
            tv3.setPadding(5, 5, 5, 5);
            tv3.setBackgroundResource(R.drawable.cell_shape);
            tv3.setText(e.getEstado());
            row.addView(tv3);

            TextView tv4 = new TextView(getContext());
            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv4.setGravity(Gravity.CENTER);
            tv4.setTextSize(18);
            tv4.setPadding(5, 5, 5, 5);
            tv4.setBackgroundResource(R.drawable.cell_shape);
            tv4.setText("" + e.getNotaFinal());
            if(e.getNotaFinal() < periodo.getNotaMinima()){
                tv4.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            row.addView(tv4);

            TextView tv5 = new TextView(getContext());
            tv5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv5.setGravity(Gravity.CENTER);
            tv5.setTextSize(18);
            tv5.setPadding(5, 5, 5, 5);
            tv5.setBackgroundResource(R.drawable.cell_shape);
            tv5.setText("" + e.getPorcentajeAsistencias() + " %");
            if(e.getPorcentajeAsistencias() < periodo.getPorcentajeAsistencias()){
                tv5.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            row.addView(tv5);

            tlResumenNotas.addView(row);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resumen_notas, container, false);
        setHasOptionsMenu(true);

        tlResumenNotas = (TableLayout)view.findViewById(R.id.tlResumenNotas);

        cargarTh();
        cargarTr();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_notas, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            reporteNotas();
            return true;
        }

        if (id == R.id.action_share_detalle) {
            reporteNotasGeneral();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void reporteNotas(){
        new AlertDialog.Builder(getContext()).setTitle("Reporte resumen notas").setItems(R.array.destino_respaldo_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                StringBuilder sb= new StringBuilder();
                Utils.writeCsvLine(sb, "N","NOMBRES", "ESTADO", "NOTA", "ASISTENCIA (%)");

                for (int i = 0; i < lista.size(); i++){
                    Estudiante e = lista.get(i);
                    Utils.writeCsvLine(sb, i + 1, e.getNombres(), e.getEstado(), e.getNotaFinal(), e.getPorcentajeAsistencias());
                }

                emailFile = Utils.getExternalStorageFile("reportes", String.format("ResumenNotas_%s_%s.csv", clase.getNombre(),  Utils.currentReportDate()));
                Utils.writeToFile(sb, emailFile);

                if (which == 0){
                    emailFile = null;
                }else{
                    //Envia al correo
                    Uri u1 = Uri.fromFile(emailFile);
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Registro Docente - Resumen notas " + clase.getNombre());
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Resumen de notas: " + emailFile.getName());
                    sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                    sendIntent.setType("text/html");
                    startActivityForResult(Intent.createChooser(sendIntent, "Destino reporte"), PICK_DESTINO_REPORTE_REQUEST);
                }
            }
        }).create().show();
    }

    private void reporteNotasGeneral(){

        new AlertDialog.Builder(getContext()).setTitle("Reporte notas").setItems(R.array.destino_respaldo_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                List<ResumenGeneral> lista = estudianteDao.getResumenGeneral(periodo, clase);

                StringBuilder sb= new StringBuilder();

                //Th 1
                Utils.writeCsv(sb, "","");
                for (int quimestre = 1; quimestre <= periodo.getQuimestres(); quimestre++){
                    for (int parcial = 1; parcial <= periodo.getParciales(); parcial++){
                        if(parcial == 1) {
                            Utils.writeCsv(sb, "QUIMESTRE " + quimestre);
                        }else{
                            Utils.writeCsv(sb, "");
                        }
                    }
                    Utils.writeCsv(sb, "","", "");
                }
                Utils.writeCsvLine(sb, "");

                //Th2
                Utils.writeCsv(sb, "N","NOMBRES");
                for (int quimestre = 1; quimestre <= periodo.getQuimestres(); quimestre++){
                    for (int parcial = 1; parcial <= periodo.getParciales(); parcial++){
                        Utils.writeCsv(sb, "PARCIAL " + parcial);
                    }
                    Utils.writeCsv(sb, "NOTA PARCIALES","NOTA EXAMENES", "NOTA QUIMESTRE");
                }
                Utils.writeCsvLine(sb, "NOTA FINAL");

                //Datos
                for (int i = 0; i < lista.size(); i++){
                    ResumenGeneral e = lista.get(i);
                    Utils.writeCsv(sb, i + 1, e.getNombres());

                    for (int quimestre = 1; quimestre <= periodo.getQuimestres(); quimestre++) {
                        ResumenQuimestre rq = e.getQuimestres().get(quimestre);
                        for (int parcial = 1; parcial <= periodo.getParciales(); parcial++) {
                            Utils.writeCsv(sb, rq.getParciales().get(parcial));
                        }
                        Utils.writeCsv(sb, rq.getNotaParciales(), rq.getNotaExamenes(), rq.getNotaFinal());
                    }
                    Utils.writeCsvLine(sb, e.getNotaFinal());
                }

                emailFile = Utils.getExternalStorageFile("reportes", String.format("Notas_%s_%s.csv", clase.getNombre(),  Utils.currentReportDate()));
                Utils.writeToFile(sb, emailFile);

                if (which == 0){
                    emailFile = null;
                }else{
                    //Envia al correo
                    Uri u1 = Uri.fromFile(emailFile);
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Registro Docente - Notas " + clase.getNombre());
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Notas: " + emailFile.getName());
                    sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                    sendIntent.setType("text/html");
                    startActivityForResult(Intent.createChooser(sendIntent, "Destino reporte"), PICK_DESTINO_REPORTE_REQUEST);
                }
            }
        }).create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_DESTINO_REPORTE_REQUEST) {
            if (resultCode == Activity.RESULT_OK && emailFile != null) {
                emailFile.delete();
                emailFile = null;
            }
        }
    }


}
