package com.example.gamingjr;
import android.os.Bundle;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingjr.R;
import com.example.gamingjr.adapter.ProgresoAdapter;
import com.example.gamingjr.adapter.ProgresoExpandableAdapter;
import com.example.gamingjr.model.Juego;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProgresoActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ProgresoExpandableAdapter expandableAdapter;
    private List<Juego> juegosList;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progreso);

        expandableListView = findViewById(R.id.expandableListView);
        expandableAdapter = new ProgresoExpandableAdapter();
        expandableListView.setAdapter(expandableAdapter);

        juegosList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        // Obtener los juegos y niveles desde Firestore
        obtenerJuegos();
    }

    private void obtenerJuegos() {
        Juego juego = new Juego();
        juego.getAll(new Juego.FirestoreCallback() {
            @Override
            public void onCallback(List<Juego> juegos) {
                juegosList = juegos;
                expandableAdapter.setJuegosList(juegosList);
            }
        });
    }
}

