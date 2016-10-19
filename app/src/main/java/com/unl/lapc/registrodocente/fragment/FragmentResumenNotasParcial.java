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
import com.unl.lapc.registrodocente.activity.MainClase;
import com.unl.lapc.registrodocente.dao.AcreditableDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.dto.Parcial;
import com.unl.lapc.registrodocente.dto.ResumenParcial;
import com.unl.lapc.registrodocente.dto.ResumenQuimestre;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Utils;

import java.io.File;
import java.util.List;

public class FragmentResumenNotasParcial extends Fragment {

    static final int PICK_DESTINO_REPORTE_REQUEST = 1;

    private EstudianteDao estudianteDao;
    private AcreditableDao acreditableDao;

    private Clase clase;
    private Parcial parcial;
    private Periodo periodo;
    private List<Acreditable> acreditables;
    List<ResumenParcial> lista;

    private TableLayout tlResumenNotas;
    private MainClase main;

    private File emailFile;

    public FragmentResumenNotasParcial() {
        // Required empty public constructor
    }


    /**
     * Otiene los acreditables que corresponden al parcial.
     * @return
     */
    public List<Acreditable> getAcreditables() {
        if(acreditables == null) {
            acreditables = acreditableDao.getAcreditablesParcial(periodo, Acreditable.TIPO_ACREDITABLE_PARCIAL);
        }

        return acreditables;
    }

    /**
     * Carga la cabecera de la tabla.
     */
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


        for(int j = 0; j < getAcreditables().size(); j++){
            TextView tv4 = new TextView(getContext());
            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv4.setGravity(Gravity.CENTER);
            tv4.setTextSize(18);
            tv4.setPadding(5, 5, 5, 5);
            tv4.setBackgroundResource(R.drawable.cell_shape_head);
            tv4.setText(acreditables.get(j).getAlias());
            tv4.setHint(acreditables.get(j).getNombre());
            row.addView(tv4);
        }

        TextView tv6 = new TextView(getContext());
        tv6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv6.setGravity(Gravity.CENTER);
        tv6.setTextSize(18);
        tv6.setPadding(5, 5, 5, 5);
        tv6.setBackgroundResource(R.drawable.cell_shape_head);
        tv6.setText("Nf"); //Nota final
        tv6.setHint("Nota final");
        row.addView(tv6);

        tlResumenNotas.addView(row);
    }

    /**
     * Carga el cuerpo de la tablas (Filas)
     */
    private void cargarTr(){
        lista = estudianteDao.getResumenParcial(periodo, clase, parcial.getQuimestre(), parcial.getNumero());

        for(int i= 0; i < lista.size(); i++){
            ResumenParcial e = lista.get(i);

            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv1 = new TextView(getContext());
            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv1.setGravity(Gravity.CENTER);
            tv1.setTextSize(18);
            tv1.setPadding(5, 5, 5, 5);
            tv1.setBackgroundResource(R.drawable.cell_shape);
            tv1.setText((i + 1) + ". ");
            row.addView(tv1);

            TextView tv2 = new TextView(getContext());
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv2.setGravity(Gravity.CENTER);
            tv2.setTextSize(18);
            tv2.setPadding(5, 5, 5, 5);
            tv2.setBackgroundResource(R.drawable.cell_shape);
            tv2.setText(e.getNombres());
            row.addView(tv2);

            for(int j = 0; j < getAcreditables().size(); j++){
                TextView tv4 = new TextView(getContext());
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setGravity(Gravity.CENTER);
                tv4.setTextSize(18);
                tv4.setPadding(5, 5, 5, 5);
                tv4.setBackgroundResource(R.drawable.cell_shape);
                tv4.setText("" + e.getAcreditables().get(acreditables.get(j).getId()).getNotaFinal());
                row.addView(tv4);
            }



            TextView tv6 = new TextView(getContext());
            tv6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv6.setGravity(Gravity.CENTER);
            tv6.setTextSize(18);
            tv6.setPadding(5, 5, 5, 5);
            tv6.setBackgroundResource(R.drawable.cell_shape);
            tv6.setText("" + e.getNotaFinal());
            row.addView(tv6);

            tlResumenNotas.addView(row);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resumen_notas_parcial, container, false);
        setHasOptionsMenu(true);

        tlResumenNotas = (TableLayout)view.findViewById(R.id.tlResumenNotas);
        this.main = (MainClase) inflater.getContext();

        Bundle args = getArguments();
        this.clase = args.getParcelable("clase");
        this.parcial = args.getParcelable("parcial");
        this.periodo = args.getParcelable("periodo");

        estudianteDao = new EstudianteDao(getContext());
        acreditableDao = new AcreditableDao(getContext());

        cargarTh();
        cargarTr();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        for (int i= 0; i < getAcreditables().size(); i++){
            //   add(group_id , item_id , order, nombre);
            final Acreditable acreditable = acreditables.get(i);
            MenuItem mi = menu.add(1, acreditable.getId(), i, String.format("%s (%s)", acreditable.getNombre(), acreditable.getAlias()));
            mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    main.cargarAcreditable(acreditable, parcial.getQuimestre(), parcial.getNumero());
                    return true;
                }
            });
        }

        inflater.inflate(R.menu.menu_main_parcial, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            reporteNotas();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Genera el reporte de notas.
     */
    private void reporteNotas(){
        new AlertDialog.Builder(getContext()).setTitle("Reporte notas parcial").setItems(R.array.destino_respaldo_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                StringBuilder sb= new StringBuilder();
                Utils.writeCsv(sb, "N","NOMBRES");
                for(int j = 0; j < getAcreditables().size(); j++){
                    Utils.writeCsv(sb, acreditables.get(j).getNombre());
                }
                Utils.writeCsvLine(sb, "NOTA FINAL");

                for (int i = 0; i < lista.size(); i++){
                    ResumenParcial e = lista.get(i);
                    Utils.writeCsv(sb, i + 1, e.getNombres());
                    for(int j = 0; j < getAcreditables().size(); j++){
                        Utils.writeCsv(sb, e.getAcreditables().get(acreditables.get(j).getId()).getNotaFinal());
                    }
                    Utils.writeCsvLine(sb, e.getNotaFinal());
                }

                emailFile = Utils.getExternalStorageFile("reportes", String.format("NotasParcial_%d_Quimestre_%d_%s_%s.csv", parcial.getNumero(), parcial.getQuimestre(), clase.getNombre(),  Utils.currentReportDate()));
                Utils.writeToFile(sb, emailFile);

                if (which == 0){
                    emailFile = null;
                }else{
                    //Envia al correo
                    Uri u1 = Uri.fromFile(emailFile);
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Registro Docente - Notas parcial " + parcial.getNumero() + ", Quimestre " + parcial.getQuimestre());
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Notas parcial: " + emailFile.getName());
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
