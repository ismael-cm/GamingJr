package com.example.gamingjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.gamingjr.R;
import com.example.gamingjr.model.Juego;
import com.example.gamingjr.model.Nivel;

import java.util.List;

public class ProgresoExpandableAdapter extends BaseExpandableListAdapter {

    private List<Juego> juegosList;

    public void setJuegosList(List<Juego> juegosList) {
        this.juegosList = juegosList;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return juegosList != null ? juegosList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return juegosList.get(groupPosition).getNiveles().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return juegosList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return juegosList.get(groupPosition).getNiveles().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_juego, parent, false);
        }

        TextView nombreJuego = convertView.findViewById(R.id.nombreJuego);
        Juego juego = (Juego) getGroup(groupPosition);
        nombreJuego.setText(juego.getNombre());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_nivel, parent, false);
        }

        TextView nombreNivel = convertView.findViewById(R.id.nombreNivel);
        Nivel nivel = (Nivel) getChild(groupPosition, childPosition);
        nombreNivel.setText(nivel.getNombre());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
