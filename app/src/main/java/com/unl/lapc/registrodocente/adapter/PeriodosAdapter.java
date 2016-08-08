package com.unl.lapc.registrodocente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Convert;

import java.util.List;

/**
 * Created by Usuario on 11/07/2016.
 */
public class PeriodosAdapter extends ArrayAdapter<Periodo> {

    private Context context;

    public PeriodosAdapter(Context context, List<Periodo> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_item_periodo,
                    parent,
                    false);
        }

        TextView txtNombre = (TextView) convertView.findViewById(R.id.txtNombre);
        TextView txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);

        final Periodo lead = getItem(position);
        txtNombre.setText(lead.getNombre());
        txtDesc.setText(String.format("%s - %s", Convert.toShortDateString(lead.getInicio()), Convert.toShortDateString(lead.getFin())));

        return convertView;
    }
}
