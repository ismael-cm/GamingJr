package com.example.gamingjr.niveles;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamingjr.R;
import com.example.gamingjr.model.Juego;
import com.example.gamingjr.model.Nivel;
import com.example.gamingjr.model.NivelUsuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Nivel1Activity extends AppCompatActivity {

    String param1;
    String param2;
    String param3;
    FirebaseUser user;
    View rootView;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    NivelUsuario nivelUsuario;
    Nivel nivel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nivel1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rootView = findViewById(android.R.id.content);

        getInitialData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Holamundo", Toast.LENGTH_SHORT).show();
        showSnakBar("Iniciando bien con puntuacion " + nivelUsuario.getPuntuacion());
    }

    private void showSnakBar(String s) {

        Snackbar snackbar = Snackbar.make(rootView, s, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.GREEN);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(20);
        snackbar.show();
    }

    private void getInitialData() {
        try {
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();

            Intent intent = getIntent();
            param1 = intent.getStringExtra("param1");
            param2 = intent.getStringExtra("param1");
            param3 = intent.getStringExtra("param1");
            nivelUsuario = (NivelUsuario) intent.getSerializableExtra("nivelUsuario");
            nivel = (Nivel) intent.getSerializableExtra("nivel");
        } catch (Exception e) {
            showSnakBar("Ocurrio un error>: " + e.getMessage());
        }
    }
}