package com.unl.lapc.registrodocente.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.unl.lapc.registrodocente.dao.AcreditableDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.dto.Quimestre;
import com.unl.lapc.registrodocente.dto.ResumenQuimestre;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.List;

public class FragmentResumenNotasQuimestre extends Fragment {

    private EstudianteDao estudianteDao;
    private AcreditableDao acreditableDao;

    private Periodo periodo;
    private Clase clase;
    private Quimestre quimestre;

    private TableLayout tlResumenNotas;

    private List<Acreditable> acreditables;

    public FragmentResumenNotasQuimestre() {
        // Required empty public constructor
    }

    public List<Acreditable> getAcreditables() {
        if(acreditables == null) {
            acreditables = acreditableDao.getAcreditablesParcial(periodo, Acreditable.TIPO_ACREDITABLE_QUIMESTRE);
        }

        return acreditables;
    }

    private void cargarTh(){
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row.setBackgroundColor(getResources().getColor(R.color.backgroundTh));



        TextView tv1 = new TextView(getContext());
        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(18);
        tv1.setPadding(5, 2, 5, 2);
        tv1.setText("N°");
        row.addView(tv1);

        TextView tv2 = new TextView(getContext());
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(18);
        tv2.setPadding(5, 2, 5, 2);
        tv2.setText("NOMBRES");
        row.addView(tv2);


        for(int j = 0; j < periodo.getParciales(); j++){
            TextView tv4 = new TextView(getContext());
            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv4.setGravity(Gravity.CENTER);
            tv4.setTextSize(18);
            tv4.setPadding(5, 2, 5, 2);
            tv4.setText("P" + (j+1));
            row.addView(tv4);
        }

        TextView tv4 = new TextView(getContext());
        tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextSize(18);
        tv4.setPadding(5, 2, 5, 2);
        tv4.setText("NP"); //nota parciales
        row.addView(tv4);

        TextView tv5 = new TextView(getContext());
        tv5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv5.setGravity(Gravity.CENTER);
        tv5.setTextSize(18);
        tv5.setPadding(2, 2, 2, 2);
        tv5.setText("NE"); //nota exmanes
        row.addView(tv5);

        TextView tv6 = new TextView(getContext());
        tv6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv6.setGravity(Gravity.CENTER);
        tv6.setTextSize(18);
        tv6.setPadding(5, 2, 5, 2);
        tv6.setText("NF");
        row.addView(tv6);

        tlResumenNotas.addView(row);
    }

    public void cargarTr(){
        List<ResumenQuimestre> lista = estudianteDao.getResumenQuimestre(periodo, clase, quimestre.getNumero());

        for(int i= 0; i < lista.size(); i++){
            ResumenQuimestre e = lista.get(i);

            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv1 = new TextView(getContext());
            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv1.setGravity(Gravity.CENTER);
            tv1.setTextSize(18);
            tv1.setPadding(5, 2, 5, 2);
            tv1.setText((i + 1) + ". ");
            row.addView(tv1);

            TextView tv2 = new TextView(getContext());
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv2.setGravity(Gravity.CENTER);
            tv2.setTextSize(18);
            tv2.setPadding(5, 2, 5, 2);
            tv2.setText(e.getNombres());
            row.addView(tv2);

            for(int j = 0; j < periodo.getParciales(); j++){
                TextView tv4 = new TextView(getContext());
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setGravity(Gravity.CENTER);
                tv4.setTextSize(18);
                tv4.setPadding(5, 2, 5, 2);
                tv4.setText("" + e.getParciales().get(j+1));
                row.addView(tv4);
            }

            TextView tv4 = new TextView(getContext());
            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv4.setGravity(Gravity.CENTER);
            tv4.setTextSize(18);
            tv4.setPadding(5, 2, 5, 2);
            tv4.setText("" + e.getNotaParciales());
            row.addView(tv4);

            TextView tv5 = new TextView(getContext());
            tv5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv5.setGravity(Gravity.CENTER);
            tv5.setTextSize(18);
            tv5.setPadding(5, 2, 5, 2);
            tv5.setText("" + e.getNotaExamenes());
            row.addView(tv5);

            TextView tv6 = new TextView(getContext());
            tv6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv6.setGravity(Gravity.CENTER);
            tv6.setTextSize(18);
            tv6.setPadding(5, 2, 5, 2);
            tv6.setText("" + e.getNotaFinal());
            row.addView(tv6);

            tlResumenNotas.addView(row);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resumen_notas_quimestre, container, false);
        setHasOptionsMenu(true);

        tlResumenNotas = (TableLayout)view.findViewById(R.id.tlResumenNotas);

        Bundle args = getArguments();
        this.clase = args.getParcelable("clase");
        this.quimestre = args.getParcelable("quimestre");
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
            MenuItem mi = menu.add(0, acreditables.get(i).getId(), i, String.format("%s (%s)", acreditables.get(i).getNombre(), acreditables.get(i).getAlias()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }


}
