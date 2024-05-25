package com.example.gamingjr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingjr.R;
import com.example.gamingjr.model.Juego;

import java.util.List;

//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//FirestoreRecyclerAdapter
public class JuegoAdapter extends RecyclerView.Adapter<JuegoAdapter.ViewHolderJuego> {

    List<Juego> listJuegos;

    public JuegoAdapter(List<Juego> listJuegos) {
        this.listJuegos = listJuegos;
    }

    @NonNull
    @Override
    public ViewHolderJuego onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.juego_item, parent, false);
        return new ViewHolderJuego(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderJuego holder, int position) {
        holder.asignarDatos(listJuegos.get(position));
    }

    @Override
    public int getItemCount() {
        return listJuegos.size();
    }

    public class ViewHolderJuego extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvSubtitulo;
        ImageView ivJuego;

        public ViewHolderJuego(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloJuego);
            tvSubtitulo = itemView.findViewById(R.id.tvSubtituloJuego);
            ivJuego = itemView.findViewById(R.id.ivJuego);
        }

        public void asignarDatos(Juego juego) {
            tvTitulo.setText(juego.getNombre());
            tvSubtitulo.setText(juego.getSubtitulo());
            ivJuego.setImageResource(!juego.getThumbnail().isEmpty() ? Integer.parseInt(juego.getThumbnail()) : 0 );
        }
    }
}
