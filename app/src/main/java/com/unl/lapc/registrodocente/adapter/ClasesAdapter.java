package com.unl.lapc.registrodocente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.modelo.Clase;

import java.util.List;

/**
 * Adaptador para mostrar los cursos en una lista.
 * Muestra el nombre, periodo y si está activa o inactiva.
 */
public class ClasesAdapter extends ArrayAdapter<Clase> {
    public ClasesAdapter(Context context, List<Clase> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_item_clase,
                    parent,
                    false);
        }

        // Referencias UI.

        TextView name = (TextView) convertView.findViewById(R.id.txtNombre);
        TextView desc = (TextView) convertView.findViewById(R.id.txtDesc);

        // Lead actual.
        Clase lead = getItem(position);

        // Setup.
        //Glide.with(getContext()).load(lead.getImage()).into(avatar);
        name.setText(lead.getNombre());
        desc.setText(lead.getPeriodo().getNombre() + (lead.isActiva() ? " (Activa)" : " (Inactiva)"));
        //title.setText(lead.getTitle());
        //company.setText(lead.getCompany());

        return convertView;
    }
}
