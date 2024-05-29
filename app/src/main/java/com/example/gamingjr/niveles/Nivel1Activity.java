package com.example.gamingjr.niveles;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamingjr.R;
import com.example.gamingjr.model.Nivel;
import com.example.gamingjr.model.NivelUsuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    TextView tvNombreNivel;
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

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        tvNombreNivel = findViewById(R.id.tvNombreNivel);

        getInitialData();
        setupButtons();
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
            Intent intent = getIntent();
            param1 = intent.getStringExtra("param1");
            param2 = intent.getStringExtra("param1");
            param3 = intent.getStringExtra("param1");
            nivelUsuario = (NivelUsuario) intent.getSerializableExtra("nivelUsuario");
            nivel = (Nivel) intent.getSerializableExtra("nivel");
            cargarNombreNivel();
        } catch (Exception e) {
            showSnakBar("Ocurrio un error>: " + e.getMessage());
        }
    }

    private void cargarNombreNivel() {
        tvNombreNivel.setText(nivel.getNombre());
    }

    private void setupButtons() {
        Button btnActualizarEstado = findViewById(R.id.btnActualizarEstado);
        Button btnAgregarPuntos = findViewById(R.id.btnAgregarPuntos);

        btnActualizarEstado.setOnClickListener(v -> {
            String nuevoEstado = "en_progreso";  // Ejemplo de nuevo estado
            actualizarEstadoEnFirestore(nuevoEstado);
            nivelUsuario.setEstado(nuevoEstado);
        });

        btnAgregarPuntos.setOnClickListener(v -> {
            int puntosActuales = Integer.parseInt(nivelUsuario.getPuntuacion());
            int nuevosPuntos = puntosActuales + 5;
            actualizarPuntuacionEnFirestore(String.valueOf(nuevosPuntos));
            nivelUsuario.setPuntuacion(String.valueOf(nuevosPuntos));
        });
    }

    // Método para actualizar puntuación en Firestore
    private void actualizarPuntuacionEnFirestore(String nuevaPuntuacion) {
        DocumentReference docRef = db.collection("nivel_user").document(nivelUsuario.getId());
        docRef.update("puntuacion", nuevaPuntuacion)
                .addOnSuccessListener(aVoid -> showSnakBar("Puntuación actualizada a: " + nuevaPuntuacion))
                .addOnFailureListener(e -> showSnakBar("Error al actualizar puntuación: " + e.getMessage()));
    }

    // Método para actualizar estado en Firestore
    private void actualizarEstadoEnFirestore(String nuevoEstado) {
        DocumentReference docRef = db.collection("nivel_user").document(nivelUsuario.getId());
        docRef.update("estado", nuevoEstado)
                .addOnSuccessListener(aVoid -> showSnakBar("Estado actualizado a: " + nuevoEstado))
                .addOnFailureListener(e -> showSnakBar("Error al actualizar estado: " + e.getMessage()));
    }
}
