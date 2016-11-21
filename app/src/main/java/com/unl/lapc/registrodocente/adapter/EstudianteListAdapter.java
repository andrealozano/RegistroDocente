package com.unl.lapc.registrodocente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.activity.EstudianteList;
import com.unl.lapc.registrodocente.dto.EstudianteCheck;

import java.util.List;

/**
 * Adaptador para mostrar los estudiantes en una lista.
 * Muestra nombres y orden.
 */
public class EstudianteListAdapter extends ArrayAdapter<EstudianteCheck> {

    private EstudianteList form;

    public EstudianteListAdapter(EstudianteList context, List<EstudianteCheck> objects) {
        super(context, 0, objects);
        this.form = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_item_estudiante_check,
                    parent,
                    false);
        }

        // Referencias UI.

        CheckBox chk = (CheckBox) convertView.findViewById(R.id.chkEst);
        TextView name = (TextView) convertView.findViewById(R.id.txtEstudiante);
        TextView ced = (TextView) convertView.findViewById(R.id.txtCedula);

        // Lead actual.
        final EstudianteCheck lead = getItem(position);

        name.setText(lead.getEstudiante().getNombresCompletos());
        ced.setText(String.format("%10s-", lead.getEstudiante().getCedula()).replace(' ','0'));
        chk.setChecked(lead.isChecked());

        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lead.setChecked(isChecked);
                form.Check(lead);
            }
        });

        return convertView;
    }
}
