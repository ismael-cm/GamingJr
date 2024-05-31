package com.example.gamingjr.adapter;

import android.content.Context;
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

public class JuegoAdapter extends RecyclerView.Adapter<JuegoAdapter.ViewHolderJuego> {

    private List<Juego> listJuegos;
    private OnItemClickListener listener;
    private Context context;

    public JuegoAdapter(List<Juego> listJuegos, OnItemClickListener listener) {
        this.listJuegos = listJuegos;
        this.listener = listener;
    }

    public JuegoAdapter(List<Juego> listJuegos) {
        this.listJuegos = listJuegos;
    }

    @NonNull
    @Override
    public ViewHolderJuego onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(listJuegos.get(position));
                    }
                }
            });
        }

        public void asignarDatos(Juego juego) {
            tvTitulo.setText(juego.getNombre());
            tvSubtitulo.setText(juego.getSubtitulo());
            String thumbnailName = juego.getThumbnail();

            if (!thumbnailName.isEmpty()) {
                int resourceId = context.getResources().getIdentifier(thumbnailName, "drawable", context.getPackageName());
                if (resourceId != 0) {
                    ivJuego.setImageResource(resourceId);
                } else {
                    ivJuego.setImageResource(R.drawable.default_image);
                }
            } else {
                ivJuego.setImageResource(R.drawable.default_image);
            }
        }
    }
}
