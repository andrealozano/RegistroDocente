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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.activity.EditItemAcreditable;
import com.unl.lapc.registrodocente.activity.MainClase;
import com.unl.lapc.registrodocente.dao.AcreditableDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.dto.ResumenAcreditable;
import com.unl.lapc.registrodocente.dto.ResumenParcialAcreditable;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.ItemAcreditable;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Utils;

import java.io.File;
import java.util.List;

/**
 * Fragmento para mostrar los items acreditable de un acreditable.
 */
public class FragmentAcreditables extends Fragment {

    static final int PICK_DESTINO_REPORTE_REQUEST = 1;

    private Clase clase;
    private Periodo periodo;
    private Acreditable acreditable;
    private int quimestre;
    private int parcial;

    private EstudianteDao estudianteDao;
    private AcreditableDao acreditableDao;
    private List<ItemAcreditable> itemsAcreditables;
    private List<ResumenAcreditable> lista;

    private File emailFile;
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

    /**
     * Inicializa componentes
     */
    private void customInit(){
        tlResumenNotas.removeAllViews();
        this.itemsAcreditables = acreditableDao.getItemsAcreditables(acreditable, quimestre, parcial);
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

        if(id == R.id.action_share){
            reporteNotas();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Edición de un item acreditable (Ejm: Leccion 1)
     * @param itemAcreditable
     */
    private void editItem(ItemAcreditable itemAcreditable){
        Intent mIntent = new Intent(getContext(), EditItemAcreditable.class);
        mIntent.putExtra("clase", clase);
        mIntent.putExtra("periodo", periodo);
        mIntent.putExtra("acreditable", acreditable);
        mIntent.putExtra("itemAcreditable", itemAcreditable);

        startActivity(mIntent);
    }

    /**
     * Crea la cabecera de la tabla
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


        for(int j = 0; j < itemsAcreditables.size(); j++){
            final ItemAcreditable itemAcreditable = itemsAcreditables.get(j);

            TextView tv4 = new TextView(getContext());
            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv4.setGravity(Gravity.CENTER);
            tv4.setTextSize(18);
            tv4.setPadding(5, 5, 5, 5);
            tv4.setBackgroundResource(R.drawable.cell_shape_head);
            tv4.setText(itemsAcreditables.get(j).getAlias());
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
        tv6.setPadding(5, 5, 5, 5);
        tv6.setBackgroundResource(R.drawable.cell_shape_head);
        tv6.setText("Pm"); //Promedio
        row.addView(tv6);

        TextView tv7 = new TextView(getContext());
        tv7.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv7.setGravity(Gravity.CENTER);
        tv7.setTextSize(18);
        tv7.setPadding(5, 5, 5, 5);
        tv7.setBackgroundResource(R.drawable.cell_shape_head);
        tv7.setText("Eq"); //Equivalencia
        row.addView(tv7);

        tlResumenNotas.addView(row);
    }

    /**
     * Crea las filas de la tabla
     */
    public void cargarTr(){
        lista = estudianteDao.getResumenAcreditable(periodo, clase, acreditable, quimestre, parcial);

        for(int i= 0; i < lista.size(); i++){
            final ResumenAcreditable e = lista.get(i);

            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tvOrd = new TextView(getContext());
            tvOrd.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvOrd.setGravity(Gravity.CENTER);
            tvOrd.setTextSize(18);
            tvOrd.setPadding(5, 5, 5, 5);
            tvOrd.setBackgroundResource(R.drawable.cell_shape);
            tvOrd.setText((i + 1) + ". ");
            row.addView(tvOrd);

            TextView tvNom = new TextView(getContext());
            tvNom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvNom.setGravity(Gravity.CENTER);
            tvNom.setTextSize(18);
            tvNom.setPadding(5, 5, 5, 5);
            tvNom.setBackgroundResource(R.drawable.cell_shape);
            tvNom.setText(e.getNombres());
            row.addView(tvNom);

            final TextView tvPm = new TextView(getContext());
            final TextView tvEq = new TextView(getContext());

            for(int j = 0; j < itemsAcreditables.size(); j++){

                final ItemAcreditable itemAcreditable = itemsAcreditables.get(j);
                final ResumenParcialAcreditable registro = e.getAcreditables().get(itemAcreditable.getId());

                final TextView tvNot = new TextView(getContext());
                tvNot.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvNot.setGravity(Gravity.CENTER);
                tvNot.setTextSize(18);
                tvNot.setPadding(5, 5, 5, 5);
                tvNot.setBackgroundResource(R.drawable.cell_shape);
                tvNot.setText(Utils.toString2(registro.getNotaFinal()));
                tvNot.setClickable(true);
                tvNot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //editItem(itemAcreditable);
                        showDialogNota(tvNot, tvPm, tvEq, e, itemAcreditable, registro);
                    }
                });

                row.addView(tvNot);
            }



            tvPm.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvPm.setGravity(Gravity.CENTER);
            tvPm.setTextSize(18);
            tvPm.setPadding(5, 5, 5, 5);
            tvPm.setBackgroundResource(R.drawable.cell_shape_total);
            tvPm.setText(Utils.toString2(e.getNotaPromedio()));
            row.addView(tvPm);

            tvEq.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvEq.setGravity(Gravity.CENTER);
            tvEq.setTextSize(18);
            tvEq.setPadding(5, 5, 5, 5);
            tvEq.setBackgroundResource(R.drawable.cell_shape_total);
            tvEq.setText(Utils.toString2(e.getNotaFinal()));
            row.addView(tvEq);

            tlResumenNotas.addView(row);
        }
    }

    /**
     * Muestra un diálogo para ingresar la nota de un item acreditable para un estudiante.
     * @param tvNota
     * @param tvPm
     * @param tvEq
     * @param resumen
     * @param itemAcreditable
     * @param registro
     */
    private void showDialogNota(final TextView tvNota, final TextView tvPm, final TextView tvEq, final ResumenAcreditable resumen, ItemAcreditable itemAcreditable, final ResumenParcialAcreditable registro){
        final View myView = View.inflate(getContext(), R.layout.content_dlg_nota, null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(myView);
        builder.setTitle("Asignar nota: " + acreditable.getNombre());
        builder.setCancelable(false);

        final TextView txtAcre = (TextView)myView.findViewById(R.id.txtAcreditable);
        final TextView txtEstu = (TextView)myView.findViewById(R.id.txtEstudiante);
        final EditText txtNota = (EditText)myView.findViewById(R.id.txtNota);

        txtAcre.setText("Acreditable: " + itemAcreditable.getNombre());
        txtEstu.setText("Estudiante : " + resumen.getNombres());
        txtNota.setText(""+registro.getNotaFinal());
        txtNota.selectAll();

        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);

        builder.setPositiveButton("Asignar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //NO usar en este caso. Se cierra automaticamnete
            }
        });

        builder.setNegativeButton("Cerrar",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            }
        });

        final AlertDialog alert=builder.create();
        alert.show();

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(txtNota.getText().toString().length() > 0) {
                    double nota = Utils.toDouble(txtNota.getText().toString());
                    //validar ramgo

                    if(nota <= periodo.getEscala()) {

                        registro.setNotaFinal(nota);
                        resumen.calcularPromedio(acreditable, periodo);

                        Estudiante estudiante = new Estudiante(resumen.getEstudianteId());
                        acreditableDao.updateNota(resumen, registro, periodo, clase, estudiante, acreditable, quimestre, parcial);

                        tvNota.setText("" + nota);
                        tvPm.setText("" + resumen.getNotaPromedio());
                        tvEq.setText("" + resumen.getNotaFinal());
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        alert.dismiss();
                    }else{
                        //Snackbar.make(txtNota, "La nota no debe sobrepasar de " + periodo.getEscala(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        txtNota.setError("La nota no debe sobrepasar de " + periodo.getEscala());
                    }
                }else{
                    //Snackbar.make(myView, "Ingrese la nota", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    txtNota.setError("Ingrese la nota");
                }
            }
        });

        txtNota.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * Genera el reporte de notas para este acreditable.
     */
    private void reporteNotas(){
        new AlertDialog.Builder(getContext()).setTitle("Reporte notas " + acreditable.getNombre()).setItems(R.array.destino_respaldo_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                StringBuilder sb= new StringBuilder();
                Utils.writeCsv(sb, "N","NOMBRES");
                for(int j = 0; j < itemsAcreditables.size(); j++){
                    ItemAcreditable itemAcreditable = itemsAcreditables.get(j);
                    Utils.writeCsv(sb, itemsAcreditables.get(j).getNombre());
                }
                Utils.writeCsvLine(sb, "PROMEDIO","EQUIVALENCIA");

                for (int i = 0; i < lista.size(); i++){
                    ResumenAcreditable e = lista.get(i);
                    Utils.writeCsv(sb, i + 1, e.getNombres());

                    for(int j = 0; j < itemsAcreditables.size(); j++){
                        ItemAcreditable itemAcreditable = itemsAcreditables.get(j);
                        ResumenParcialAcreditable registro = e.getAcreditables().get(itemAcreditable.getId());
                        Utils.writeCsv(sb, registro.getNotaFinal());
                    }
                    Utils.writeCsvLine(sb, e.getNotaPromedio(), e.getNotaFinal());
                }


                Utils.checkReportPermisions(getActivity());
                emailFile = Utils.getExternalStorageFile("reportes", String.format("%s_Q%d_P%d_%s_%s.csv", acreditable.getNombre(), quimestre, parcial, clase.getNombre(),  Utils.currentReportDate()));
                Utils.writeToFile(sb, emailFile);

                if (which == 0){
                    emailFile = null;
                }else if(emailFile != null){
                    //Envia al correo
                    Uri u1 = Uri.fromFile(emailFile);
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Registro Docente - " + acreditable.getNombre());
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Acreditable: " + acreditable.getNombre() + "<br/>" + "Clase: " + clase.getNombre() + "<br/>Quimestre: " + quimestre+ "<br/>Parcial: " + parcial);
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
