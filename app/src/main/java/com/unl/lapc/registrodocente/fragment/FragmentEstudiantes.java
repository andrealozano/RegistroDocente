package com.unl.lapc.registrodocente.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.activity.EditEstudiante;
import com.unl.lapc.registrodocente.adapter.ClaseEstudianteAdapter;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Utils;

import java.io.File;
import java.util.List;

public class FragmentEstudiantes extends Fragment {

    static final int PICK_DESTINO_REPORTE_REQUEST = 1;

    private ListView mLeadsList;
    private ClaseDao dao;
    private EstudianteDao daoEstudiante;

    private Clase clase;
    private Periodo periodo;

    private ClaseEstudianteAdapter mLeadsAdapter;

    private File emailFile = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_main_estudiantes);

        View view = inflater.inflate(R.layout.fragment_main_estudiantes, container, false);
        setHasOptionsMenu(true);

        mLeadsList = (ListView)view.findViewById(R.id.listView);

        dao = new ClaseDao(getContext());
        daoEstudiante = new EstudianteDao(getContext());

        Bundle bundle = getArguments();
        clase = bundle.getParcelable("clase");
        periodo = bundle.getParcelable("periodo");

        if(clase.getId() > 0){
            clase = dao.getClase(clase.getId());
        }

        mLeadsAdapter = new ClaseEstudianteAdapter(getContext(), daoEstudiante.getEstudiantes(clase));
        mLeadsList.setAdapter(mLeadsAdapter);
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Estudiante cls = (Estudiante) mLeadsList.getItemAtPosition(i);
                if(cls!=null) {
                    editEstudiante(cls);
                }
            }
        });

        //setTitle("Estudiantes: " + clase.getNombre());

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btnAddEstudiante);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                editEstudiante(new Estudiante(clase, periodo));

            }
        });

        return view;
    }

    private void editEstudiante(Estudiante estudiante){
        Intent intent = new Intent(getContext(), EditEstudiante.class);

        intent.putExtra("clase", clase);
        intent.putExtra("periodo", periodo);
        intent.putExtra("estudiante", estudiante);

        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_estudiantes, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.action_ord_apellido) {
            daoEstudiante.ordernarApellidos(clase);
            mLeadsAdapter = new ClaseEstudianteAdapter(getContext(), daoEstudiante.getEstudiantes(clase));
            mLeadsList.setAdapter(mLeadsAdapter);

            mLeadsAdapter.notifyDataSetChanged();
            mLeadsList.invalidateViews();
            return true;
        }*/

        /*if (id == R.id.action_asistencias) {
            Intent intent = new Intent(getContext(), FragmentAsistancias.class);
            intent.putExtra("clase", clase);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_notas) {
            Intent intent = new Intent(getContext(), MainClase.class);
            intent.putExtra("clase", clase);
            startActivity(intent);
            return true;
        }*/

        if (id == R.id.action_share) {
            reporteEstudiantes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reporteEstudiantes(){
        new AlertDialog.Builder(getContext()).setTitle("Reporte estudiates").setItems(R.array.destino_respaldo_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                List<Estudiante> lista = daoEstudiante.getEstudiantes(clase);
                StringBuilder sb= new StringBuilder();
                Utils.writeCsvLine(sb, "N","CEDULA", "NOMBRES", "APELLIDOS");
                for (int i = 0; i < lista.size(); i++){
                    Estudiante e = lista.get(i);
                    Utils.writeCsvLine(sb, i + 1, e.getCedula(), e.getNombres(), e.getApellidos());
                }

                emailFile = Utils.getExternalStorageFile("reportes", String.format("Estudiantes_%s_%s.csv", clase.getNombre(),  Utils.currentReportDate()));
                Utils.writeToFile(sb, emailFile);

                if (which == 0){
                    emailFile = null;
                }else{
                    //Envia al correo
                    Uri u1 = Uri.fromFile(emailFile);
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Registro Docente - Lista estudiantes");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Lista estudiantes: " + emailFile.getName());
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
