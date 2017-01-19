package com.unl.lapc.registrodocente.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.adapter.EstudianteListAdapter;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.dto.EstudianteCheck;
import com.unl.lapc.registrodocente.modelo.Estudiante;

import java.util.ArrayList;
import java.util.List;

/**
 * Actividad para mostrar y buscar todos los estudiantes registrados.
 * Tambi{en permite seleccionarlos para retornarlos a la actividad que los invocó
 */
public class EstudianteList extends AppCompatActivity {

    static final int ADD_ESTUDIANTE_REQUEST = 1;

    private ListView listView;
    private EditText txtBuscar;
    private EstudianteDao  estudianteDao;
    private EstudianteListAdapter mLeadsAdapter;

    private List<EstudianteCheck> lista = new ArrayList<>();
    private List<EstudianteCheck> listaCheck = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante_list);
        estudianteDao = new EstudianteDao(this);

        txtBuscar = (EditText) findViewById(R.id.txtBuscar);
        listView = (ListView) findViewById(R.id.listView);

        lista = EstudianteCheck.getCheckList(estudianteDao.getEstudiantes("", 10), listaCheck);
        mLeadsAdapter = new EstudianteListAdapter(this, lista);
        listView.setAdapter(mLeadsAdapter);

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                //Snackbar.make(txtBuscar, "Text: " + s.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                mLeadsAdapter.clear();
                List<EstudianteCheck> lista1 = EstudianteCheck.getCheckList(estudianteDao.getEstudiantes(s.toString(), 10), listaCheck);
                mLeadsAdapter.addAll(lista1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_estudiante_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Main/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent i = new Intent();

            i.putExtra("estudiantes", getEstudiantes());
            setResult(RESULT_OK, i);
            finish();

            return true;
        }

        if (id == R.id.action_clear) {
            this.Check(null);
            return true;
        }

        if (id == R.id.action_new) {
            addEstudiante();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_ESTUDIANTE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Estudiante e = data.getExtras().getParcelable("estudiante");
                EstudianteCheck ec = new EstudianteCheck(e, true);
                mLeadsAdapter.add(ec);
                Check(ec);
            }
        }
    }

    /**
     * Lanza actividad para crear nuevo estudiante
     */
    private void addEstudiante(){
        Intent intent = new Intent(this, EditEstudiante.class);
        intent.putExtra("estudiante", new Estudiante());
        intent.putExtra("startType", "startActivityForResult");
        startActivityForResult(intent, ADD_ESTUDIANTE_REQUEST);
    }

    /**
     * Agrega a la lista de estudiantes seleccionados
     * @param c
     */
    public void Check(EstudianteCheck c){

        if(c!=null){
            EstudianteCheck e = null;

            for(EstudianteCheck o: listaCheck){
                if(o.getEstudiante().getId()==c.getEstudiante().getId()){
                    e = o;
                }
            }

            if(c.isChecked() && e == null){
                listaCheck.add(c);
            }

            if(!c.isChecked() && e != null){
                listaCheck.remove(e);
            }
        }else{
            listaCheck.clear();
            for(EstudianteCheck ci: lista){
                ci.setChecked(false);
            }
            mLeadsAdapter.notifyDataSetChanged();
        }

        setTitle("Estudiantes ("+ listaCheck.size()+")");
    }

    /**
     * Obtiene los ids de los estudiantes seleccionados
     * @return
     */
    public int[] getEstudiantes(){
        int[] ids = new int[listaCheck.size()];
        int i = 0;
        for(EstudianteCheck c: listaCheck){
            ids[i] = c.getEstudiante().getId();
            i++;
        }
        return ids;
    }


}
