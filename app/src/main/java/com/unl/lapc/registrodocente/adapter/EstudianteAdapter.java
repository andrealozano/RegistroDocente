package com.unl.lapc.registrodocente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.modelo.Estudiante;

import java.util.List;

/**
 * Created by Usuario on 11/07/2016.
 */
public class EstudianteAdapter extends ArrayAdapter<Estudiante> {

    public EstudianteAdapter(Context context, List<Estudiante> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_item_clase_estudiante,
                    parent,
                    false);
        }

        // Referencias UI.

        TextView name = (TextView) convertView.findViewById(R.id.txtEstudiante);
        TextView cod = (TextView) convertView.findViewById(R.id.txtCodigo);

        // Lead actual.
        Estudiante lead = getItem(position);

        // Setup.
        //Glide.with(getContext()).load(lead.getImage()).into(avatar);
        name.setText(lead.getNombresCompletos());
        cod.setText(""+lead.getOrden()+ ". ");

        return convertView;
    }
}
