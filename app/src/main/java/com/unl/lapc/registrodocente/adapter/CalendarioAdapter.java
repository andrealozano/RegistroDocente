package com.unl.lapc.registrodocente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.modelo.Calendario;
import com.unl.lapc.registrodocente.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Usuario on 11/07/2016.
 */
public class CalendarioAdapter extends ArrayAdapter<Calendario> {

    Calendar calendar = GregorianCalendar.getInstance();

    public CalendarioAdapter(Context context, List<Calendario> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_item_calendario,
                    parent,
                    false);
        }

        // Referencias UI.

        TextView name = (TextView) convertView.findViewById(R.id.txtNombre);
        TextView desc = (TextView) convertView.findViewById(R.id.txtDesc);

        // Lead actual.
        Calendario lead = getItem(position);

        calendar.setTime(lead.getFecha());
        //int day = calendar.get(Calendar.DAY_OF_WEEK);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", new Locale("es-EC"));
        String s = lead.getObservacion() == null ? "" : lead.getObservacion().trim();
        if(s.equals("")) s = ": " + s;

        name.setText(String.format("%s", Utils.toShortDateString(lead.getFecha())));
        desc.setText(String.format("%s (%s%s)", sdf.format(lead.getFecha()), lead.getEstado(), s));


        if(lead.getEstado().equals(Calendario.ESTADO_FERIADO)){
            name.setTextColor(getContext().getResources().getColor(R.color.colorListItemInactive));
            desc.setTextColor(getContext().getResources().getColor(R.color.colorListItemSubtitleInactive));
        }else{
            name.setTextColor(getContext().getResources().getColor(R.color.colorListItemPrimary));
            desc.setTextColor(getContext().getResources().getColor(R.color.colorListItemSubtitle));
        }
        //ContextCompat.getColor()
        return convertView;
    }
}
