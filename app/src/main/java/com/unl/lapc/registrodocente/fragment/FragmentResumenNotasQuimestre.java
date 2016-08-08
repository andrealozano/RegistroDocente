package com.unl.lapc.registrodocente.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Quimestre;

import java.util.List;

public class FragmentResumenNotasQuimestre extends Fragment {

    private EstudianteDao estudianteDao;
    private Clase clase;
    private Quimestre quimestre;
    private TableLayout tlResumenNotas;

    public FragmentResumenNotasQuimestre() {
        // Required empty public constructor
    }

    public void cargar(){
        Bundle args = getArguments();
        this.clase = args.getParcelable("clase");
        this.quimestre = args.getParcelable("quimestre");

        estudianteDao = new EstudianteDao(getContext());
        List<Estudiante> lista = estudianteDao.getEstudiantes(clase);

        for(int i= 0; i < lista.size(); i++){
            Estudiante e = lista.get(i);

            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv1 = new TextView(getContext());
            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv1.setGravity(Gravity.CENTER);
            tv1.setTextSize(18);
            tv1.setPadding(0, 5, 0, 5);
            tv1.setText(e.getOrden() + ". ");
            row.addView(tv1);

            TextView tv2 = new TextView(getContext());
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv2.setGravity(Gravity.CENTER);
            tv2.setTextSize(18);
            tv2.setPadding(0, 5, 0, 5);
            tv2.setText(e.getNombresCompletos());
            row.addView(tv2);

            TextView tv3 = new TextView(getContext());
            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv3.setGravity(Gravity.CENTER);
            tv3.setTextSize(18);
            tv3.setPadding(0, 5, 0, 5);
            tv3.setText(e.getEstado());
            row.addView(tv3);

            TextView tv4 = new TextView(getContext());
            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv4.setGravity(Gravity.CENTER);
            tv4.setTextSize(18);
            tv4.setPadding(0, 5, 0, 5);
            tv4.setText("" + e.getNotaFinal());
            row.addView(tv4);

            TextView tv5 = new TextView(getContext());
            tv5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv5.setGravity(Gravity.CENTER);
            tv5.setTextSize(18);
            tv5.setPadding(0, 5, 0, 5);
            tv5.setText("" + e.getPorcentajeAsistencias());
            row.addView(tv5);

            tlResumenNotas.addView(row);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resumen_notas_quimestre, container, false);

        tlResumenNotas = (TableLayout)view.findViewById(R.id.tlResumenNotas);

        cargar();

        return view;
    }


}
