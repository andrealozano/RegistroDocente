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

import java.util.List;

public class FragmentResumenNotasParcial extends Fragment {

    private EstudianteDao estudianteDao;
    private AcreditableDao acreditableDao;

    private Clase clase;
    private Parcial parcial;
    private Periodo periodo;
    private List<Acreditable> acreditables;

    private TableLayout tlResumenNotas;
    private MainClase main;

    public FragmentResumenNotasParcial() {
        // Required empty public constructor
    }

    public List<Acreditable> getAcreditables() {
        if(acreditables == null) {
            acreditables = acreditableDao.getAcreditablesParcial(periodo, Acreditable.TIPO_ACREDITABLE_PARCIAL);
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

    public void cargarTr(){
        List<ResumenParcial> lista = estudianteDao.getResumenParcial(periodo, clase, parcial.getQuimestre(), parcial.getNumero());

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
            MenuItem mi = menu.add(0, acreditable.getId(), i, String.format("%s (%s)", acreditable.getNombre(), acreditable.getAlias()));
            mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    main.cargarAcreditable(acreditable, parcial.getQuimestre(), parcial.getNumero());
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
}
