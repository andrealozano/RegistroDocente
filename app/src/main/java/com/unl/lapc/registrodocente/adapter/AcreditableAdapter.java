package com.unl.lapc.registrodocente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.modelo.Acreditable;

import java.util.List;

/**
 * Adaptador para mostrar los acreditables en una lista.
 */
public class AcreditableAdapter extends ArrayAdapter<Acreditable> {
    public AcreditableAdapter(Context context, List<Acreditable> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_item_acreditable,
                    parent,
                    false);
        }

        // Referencias UI.

        TextView name = (TextView) convertView.findViewById(R.id.txtNombre);
        TextView desc = (TextView) convertView.findViewById(R.id.txtDesc);

        // Lead actual.
        Acreditable lead = getItem(position);


        name.setText(lead.getNombre());
        desc.setText(String.format("%s. %s (%s: %s)", lead.getNumero(), lead.getAlias(), lead.getTipo(), lead.getEquivalencia()));

        return convertView;
    }
}
