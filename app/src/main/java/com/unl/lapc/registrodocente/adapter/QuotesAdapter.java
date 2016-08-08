package com.unl.lapc.registrodocente.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;

/**
 * Created by Usuario on 11/07/2016.
 */
public class QuotesAdapter extends CursorAdapter {

    public QuotesAdapter (Context context, Cursor cursor) {
        super(context, cursor);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        /*TextView body = (TextView) view.findViewById(R.id.txtNombre);
        body.setText(cursor.getString(cursor.getColumnIndex(0)));*/

        TextView author = (TextView)view.findViewById(R.id.txtNombre);
        author.setText(cursor.getString(cursor.getColumnIndex("nombre")));
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_periodo, null, false);
        return view;
    }
}
