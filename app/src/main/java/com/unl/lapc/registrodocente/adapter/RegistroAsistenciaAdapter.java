package com.unl.lapc.registrodocente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.modelo.Estudiante;

import java.util.List;

/**
 * Adaptador para mostrar las asistencias de un día en una lista.
 */
public class RegistroAsistenciaAdapter extends ArrayAdapter<Estudiante> {

    public RegistroAsistenciaAdapter(Context context, List<Estudiante> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_item_registro_asistencia,
                    parent,
                    false);
        }

        // Referencias UI.

        TextView name = (TextView) convertView.findViewById(R.id.txtEstudiante);
        TextView cod = (TextView) convertView.findViewById(R.id.txtCodigo);
        CheckBox chk = (CheckBox) convertView.findViewById(R.id.cbEstado);

        // Lead actual.
        Estudiante lead = getItem(position);


        name.setText(lead.getNombresCompletos());
        cod.setText(""+(position+1)+". ");


        return convertView;
    }
}
