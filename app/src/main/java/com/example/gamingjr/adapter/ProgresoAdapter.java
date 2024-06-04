package com.example.gamingjr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingjr.R;
import com.example.gamingjr.model.Juego;

import java.util.List;

public class ProgresoAdapter extends RecyclerView.Adapter<ProgresoAdapter.ViewHolder> {
    private List<Juego> juegosList;

    public void setJuegosList(List<Juego> juegosList) {
        this.juegosList = juegosList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_juego, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Juego juego = juegosList.get(position);
        holder.nombreJuego.setText(juego.getNombre());
    }

    @Override
    public int getItemCount() {
        return juegosList != null ? juegosList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombreJuego;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreJuego = itemView.findViewById(R.id.nombreJuego);
        }
    }
}
