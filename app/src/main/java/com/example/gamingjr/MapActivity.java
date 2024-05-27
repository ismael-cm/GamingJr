package com.example.gamingjr;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.gamingjr.adapter.JuegoAdapter;
import com.example.gamingjr.adapter.OnItemClickListener;
import com.example.gamingjr.model.Juego;
import com.example.gamingjr.model.Nivel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class MapActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        TextView textView2 = findViewById(R.id.textView2);

        // Obtener los datos del Intent
        String id2 = getIntent().getStringExtra("id");
        String nombre = getIntent().getStringExtra("nombre");
        String subtitulo = getIntent().getStringExtra("subtitulo");
        String thumbnail = getIntent().getStringExtra("thumbnail");

        textView2.setText("\nID: " + id2
                + "\nNombre: " + nombre + "\nSubtitulo: " + subtitulo + "\n Thumbnail: " + thumbnail);

        Log.d(TAG, "Llamando a getNiveles con id_juego: " + id2);
        Nivel nivel = new Nivel();

        nivel.getAll(new Nivel.FirestoreCallback() {
            @Override
            public void onCallback(List<Nivel> nivelesList) {

                try {
                    for (Nivel n : nivelesList) {
                        try {
                            Log.i(TAG, "Nivel " + n.getNombre());

                        } catch (Exception e) {
                            Log.e(TAG, "Error al convertir el documento a Nivel: ", e);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(MapActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
