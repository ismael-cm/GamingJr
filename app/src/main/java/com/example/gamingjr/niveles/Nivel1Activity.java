package com.example.gamingjr.niveles;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamingjr.R;
import com.example.gamingjr.model.Nivel;
import com.example.gamingjr.model.NivelUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Nivel1Activity extends AppCompatActivity {

    String param1,param2,param3;
    FirebaseUser user;
    View rootView;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    NivelUsuario nivelUsuario;
    TextView tvNombreNivel;
    Nivel nivel;

    private VideoView videoView;
    private Button btnSkipVideo;
    boolean isFinished = false;

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
        videoView = findViewById(R.id.videoView);
        btnSkipVideo = findViewById(R.id.btnSkipVideo);
        tvNombreNivel = findViewById(R.id.tvNombreNivel);

        getInitialData();
        setupButtons();
        setupVideoView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showSnakBar("Iniciando bien con puntuacion " + nivelUsuario.getPuntuacion());

        //Reproducir video de introduccion.
        setPlayVideo("intro");
    }

    private void showSnakBar(String s, int color) {
        Snackbar snackbar = Snackbar.make(rootView, s, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(color);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(20);
        snackbar.show();
    }

    private void showSnakBar(String s) {
        Snackbar snackbar = Snackbar.make(rootView, s, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.GREEN);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(20);
        snackbar.show();
    }




    private void setupVideoView() {
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.nivel1final;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        videoView.setOnCompletionListener(mp -> hideVideoView());
        btnSkipVideo.setOnClickListener(v -> hideVideoView());
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                hideVideoView();
                if(isFinished) {
                    nextLevel();
                }
            }
        });
    }

    private void setPlayVideo(String compleme) {
        try {
            String nombreVideo = nivel.getVideo() + compleme;

            // Obtener el URI del video
            int videoResId = getResources().getIdentifier(nombreVideo, "raw", getPackageName());
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);
            videoView.setVideoURI(videoUri);
            //Bandera para saber que el nivel esta finalizado
            if(compleme.equals("final")) isFinished = true;
            showVideoView();
        } catch (Exception e) {
            showSnakBar("Ocurrio un error en el video " + e.getMessage());
        }
    }

    private void showVideoView() {
        videoView.setVisibility(View.VISIBLE);
        btnSkipVideo.setVisibility(View.VISIBLE);
        videoView.start();
    }

    private void hideVideoView() {
        if (isFinished) nextLevel();
        videoView.stopPlayback();
        videoView.setVisibility(View.GONE);
        btnSkipVideo.setVisibility(View.GONE);
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
        Button btnAgregarPuntos = findViewById(R.id.btnAgregarPuntos);

        btnAgregarPuntos.setOnClickListener(v -> {
            int puntosActuales = Integer.parseInt(nivelUsuario.getPuntuacion());
            int nuevosPuntos = puntosActuales + 5;

            if(nuevosPuntos >= nivel.getPuntos_minimos()) {
                setPlayVideo("final");

                String nuevoEstado = "completado";
                actualizarEstadoEnFirestore(nuevoEstado);
                nivelUsuario.setEstado(nuevoEstado);
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Método para manejar la acción de "Siguiente nivel"
    private void nextLevel() {
        try {
            CollectionReference ref = db.collection("nivel_user");

            Query query = ref
                    .whereEqualTo("id_usuario", user.getUid())
                    .whereEqualTo("orden", (nivelUsuario.getOrden() + 1));

            Log.i("Test", nivel.getId());
            Log.i("Test", user.getUid());
            Log.i("Test", (nivel.getOrden()) + "");
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            DocumentReference docRef = db.collection("nivel_user").document(document.getId());
                            docRef.update("estado", "Activo")
                                    .addOnSuccessListener(aVoid -> showSnakBar("Juego Completado"))
                                    .addOnFailureListener(e -> showSnakBar("Error al actualizar estado: " + e.getMessage()));

                        }
                    } else {
                        showSnakBar("No hay datos");
                    }
                }
            });


            finish();

        } catch (Exception e) {
            showSnakBar("ERROR: " + e.getMessage());
            Log.e("Test ", e.getMessage());
        }

    }
}
