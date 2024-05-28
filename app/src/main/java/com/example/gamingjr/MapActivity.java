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




        Intent intent = getIntent();
        Juego juego = (Juego) intent.getSerializableExtra("juego");
        Log.d(TAG, "Llamando a getNiveles con id_juego: " + juego.getId());
        // Accede a los niveles del juego seleccionado
        List<Nivel> niveles = juego.getNiveles();
        for (Nivel nivel : niveles) {
            Toast.makeText(MapActivity.this, nivel.getNombre(), Toast.LENGTH_SHORT).show();
        }

        textView2.setText("\nID: " + juego.getId());
    }

}
