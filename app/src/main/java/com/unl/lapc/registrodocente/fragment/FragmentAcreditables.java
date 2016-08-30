package com.unl.lapc.registrodocente.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.activity.EditItemAcreditable;
import com.unl.lapc.registrodocente.activity.MainClase;
import com.unl.lapc.registrodocente.dao.AcreditableDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.dto.ResumenAcreditable;
import com.unl.lapc.registrodocente.dto.ResumenParcial;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.ItemAcreditable;
import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class FragmentAcreditables extends Fragment {

    private Clase clase;
    private Periodo periodo;
    private Acreditable acreditable;
    private int quimestre;
    private int parcial;

    private EstudianteDao estudianteDao;
    private AcreditableDao acreditableDao;
    private List<ItemAcreditable> acreditables;

    private TableLayout tlResumenNotas;
    private MainClase main;


    public FragmentAcreditables() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_acreditables, container, false);
        setHasOptionsMenu(true);

        tlResumenNotas = (TableLayout)view.findViewById(R.id.tlResumenNotas);
        this.main = (MainClase) inflater.getContext();

        Bundle args = getArguments();
        this.clase = args.getParcelable("clase");
        this.periodo = args.getParcelable("periodo");
        this.acreditable = args.getParcelable("acreditable");
        this.quimestre = args.getInt("quimestre");
        this.parcial = args.getInt("parcial");

        estudianteDao = new EstudianteDao(getContext());
        acreditableDao = new AcreditableDao(getContext());

        customInit();

        return  view;
    }

    private void customInit(){
        tlResumenNotas.removeAllViews();
        this.acreditables = acreditableDao.getItemsAcreditables(acreditable);
        cargarTh();
        cargarTr();
    }

    @Override
    public void onResume() {
        super.onResume();
        customInit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_acreditables_notas, menu);

        List<Acreditable> acreditables = acreditableDao.getAcreditablesParcial(periodo, parcial > 0 ? Acreditable.TIPO_ACREDITABLE_PARCIAL : Acreditable.TIPO_ACREDITABLE_QUIMESTRE);
        for (int i= 0; i < acreditables.size(); i++){
            //   add(group_id , item_id , order, nombre);
            final Acreditable acreditable = acreditables.get(i);
            MenuItem mi = menu.add(0, acreditable.getId(), i, String.format("%s (%s)", acreditable.getNombre(), acreditable.getAlias()));
            mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    main.cargarAcreditable(acreditable, quimestre, parcial);
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_add){
            ItemAcreditable itemAcreditable = new ItemAcreditable(periodo, clase, acreditable, quimestre, parcial);
            editItem(itemAcreditable);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editItem(ItemAcreditable itemAcreditable){
        Intent mIntent = new Intent(getContext(), EditItemAcreditable.class);
        mIntent.putExtra("clase", clase);
        mIntent.putExtra("periodo", periodo);
        mIntent.putExtra("acreditable", acreditable);
        mIntent.putExtra("itemAcreditable", itemAcreditable);

        startActivity(mIntent);
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


        for(int j = 0; j < acreditables.size(); j++){
            final ItemAcreditable itemAcreditable = acreditables.get(j);

            TextView tv4 = new TextView(getContext());
            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv4.setGravity(Gravity.CENTER);
            tv4.setTextSize(18);
            tv4.setPadding(5, 2, 5, 2);
            tv4.setText(acreditables.get(j).getAlias());
            tv4.setClickable(true);
            tv4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editItem(itemAcreditable);
                }
            });

            row.addView(tv4);
        }

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
        List<ResumenAcreditable> lista = estudianteDao.getResumenAcreditable(periodo, clase, acreditable, quimestre, parcial);

        for(int i= 0; i < lista.size(); i++){
            ResumenAcreditable e = lista.get(i);

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

            for(int j = 0; j < acreditables.size(); j++){
                TextView tv4 = new TextView(getContext());
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setGravity(Gravity.CENTER);
                tv4.setTextSize(18);
                tv4.setPadding(5, 2, 5, 2);
                tv4.setText("" + e.getAcreditables().get(acreditables.get(j).getId()));
                row.addView(tv4);
            }


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


}
