package com.unl.lapc.registrodocente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.modelo.Parcial;
import com.unl.lapc.registrodocente.modelo.Quimestre;
import com.unl.lapc.registrodocente.util.Convert;

import java.util.List;

/**
 * Created by Usuario on 11/07/2016.
 */
public class ParcialAdapter extends ArrayAdapter<Parcial> {
    public ParcialAdapter(Context context, List<Parcial> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.list_item_generic, parent, false);
        }

        // Referencias UI.

        TextView name = (TextView) convertView.findViewById(R.id.txtNombre);
        TextView desc = (TextView) convertView.findViewById(R.id.txtDesc);

        // Lead actual.
        Parcial lead = getItem(position);


        name.setText(lead.getNombre());
        desc.setText(lead.getNumero() + ". " + Convert.toShortDateString(lead.getInicio()) + " - " + Convert.toShortDateString(lead.getFin()));

        return convertView;
    }
}
