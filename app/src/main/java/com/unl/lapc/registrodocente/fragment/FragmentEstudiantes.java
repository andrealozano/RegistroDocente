package com.unl.lapc.registrodocente.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

public class FragmentEstudiantes extends Fragment {

    private ListView mLeadsList;
    private ClaseDao dao;
    private EstudianteDao daoEstudiante;

    private Clase clase;
    private Periodo periodo;

    private ClaseEstudianteAdapter mLeadsAdapter;

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

        return super.onOptionsItemSelected(item);
    }


}
