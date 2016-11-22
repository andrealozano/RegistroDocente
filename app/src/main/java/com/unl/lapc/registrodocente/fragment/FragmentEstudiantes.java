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
import com.unl.lapc.registrodocente.activity.EstudianteList;
import com.unl.lapc.registrodocente.activity.EditEstudiante;
import com.unl.lapc.registrodocente.adapter.EstudianteAdapter;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Utils;

import java.io.File;
import java.util.List;

/**
 * Fragmento para gestionar los estudiantes de un curso.
 */
public class FragmentEstudiantes extends Fragment {

    static final int PICK_DESTINO_REPORTE_REQUEST = 1;
    static final int PICK_ESTUDIANTE_REQUEST = 2;

    private ListView listView;
    private ClaseDao claseDao;
    private EstudianteDao estudianteDao;

    private Clase clase;
    private Periodo periodo;

    private EstudianteAdapter mLeadsAdapter;

    private File emailFile = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_main_estudiantes);

        View view = inflater.inflate(R.layout.fragment_main_estudiantes, container, false);
        setHasOptionsMenu(true);

        listView = (ListView)view.findViewById(R.id.listView);

        claseDao = new ClaseDao(getContext());
        estudianteDao = new EstudianteDao(getContext());

        Bundle bundle = getArguments();
        clase = bundle.getParcelable("clase");
        periodo = bundle.getParcelable("periodo");

        if(clase.getId() > 0){
            clase = claseDao.get(clase.getId());
        }

        mLeadsAdapter = new EstudianteAdapter(getContext(), estudianteDao.getEstudiantes(clase));
        listView.setAdapter(mLeadsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Estudiante cls = (Estudiante) listView.getItemAtPosition(i);
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
                //editEstudiante(new Estudiante());
                addEstudiante();

            }
        });

        return view;
    }

    /**
     * Muestra la actividad para editar un estudiante.
     * @param estudiante
     */
    private void editEstudiante(Estudiante estudiante){
        Intent intent = new Intent(getContext(), EditEstudiante.class);

        intent.putExtra("clase", clase);
        intent.putExtra("periodo", periodo);
        intent.putExtra("estudiante", estudiante);

        startActivity(intent);
    }

    private void addEstudiante(){
        Intent intent = new Intent(getContext(), EstudianteList.class);

        intent.putExtra("clase", clase);
        intent.putExtra("periodo", periodo);

        startActivityForResult(intent, PICK_ESTUDIANTE_REQUEST);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_estudiantes, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            reporteEstudiantes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Genera el reporte de estudiantes para esté curso.
     */
    private void reporteEstudiantes(){
        new AlertDialog.Builder(getContext()).setTitle("Reporte estudiates").setItems(R.array.destino_respaldo_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                List<Estudiante> lista = estudianteDao.getEstudiantes(clase);
                StringBuilder sb= new StringBuilder();
                Utils.writeCsvLine(sb, "N","CEDULA", "NOMBRES", "APELLIDOS");
                for (int i = 0; i < lista.size(); i++){
                    Estudiante e = lista.get(i);
                    Utils.writeCsvLine(sb, i + 1, e.getCedula(), e.getNombres(), e.getApellidos());
                }

                Utils.checkReportPermisions(getActivity());
                emailFile = Utils.getExternalStorageFile("reportes", String.format("Estudiantes_%s_%s.csv", clase.getNombre(),  Utils.currentReportDate()));
                Utils.writeToFile(sb, emailFile);

                if (which == 0){
                    emailFile = null;
                }else if(emailFile != null){
                    Uri u1 = Uri.fromFile(emailFile);
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Registro Docente - Lista estudiantes");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Lista estudiantes: " + emailFile.getName());
                    sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, Utils.getEmailPref(getContext()));
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

        if(requestCode == PICK_ESTUDIANTE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                int[] ids = data.getExtras().getIntArray("estudiantes");
                estudianteDao.add(ids, clase, periodo);

                //Consulta nuevamente
                mLeadsAdapter.clear();
                mLeadsAdapter.addAll(estudianteDao.getEstudiantes(clase));
            }
        }
    }
}
