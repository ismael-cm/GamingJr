package com.example.gamingjr.niveles;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.gamingjr.R;
import com.example.gamingjr.adapter.CardAdapter;
import com.example.gamingjr.model.Card;
import com.example.gamingjr.model.Nivel;
import com.example.gamingjr.model.NivelUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Nivel2Activity extends AppCompatActivity implements CardAdapter.OnGameEndListener {


    private GridView gridView;
    private CardAdapter adapter;
    private List<Card> cardList;
    private int paresEncontrados = 0;
    private MediaPlayer backgroundMusic;

    //Parametros para
    String param1,param2,param3;
    FirebaseUser user;
    View rootView;
    private FirebaseAuth mAuth;

    FirebaseFirestore db;
    NivelUsuario nivelUsuario;
    Nivel nivel;

    private VideoView videoView;
    private Button btnSkipVideo;
    boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel2);

        // Inicializa el MediaPlayer para la música de fondo
        backgroundMusic = MediaPlayer.create(this, R.raw.fondo);
        backgroundMusic.setLooping(true); // Repite la música de fondo
        backgroundMusic.setVolume(0.05f, 0.05f);
        backgroundMusic.start(); // Inicia la reproducción de la música de fondo

        gridView = findViewById(R.id.gridView);
        cardList = new ArrayList<>();

        // Define las imágenes y los audios de las cartas
        int[] images = {
                R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.cuatro, R.drawable.cinco, R.drawable.seis
        };

        int[] audios = {
                R.raw.uno, R.raw.dos, R.raw.tres, R.raw.cuatro, R.raw.cinco, R.raw.seis
        };

        // Agrega pares de cartas a la lista
        int id = 1;
        for (int i = 0; i < images.length; i++) {
            cardList.add(new Card(id, images[i], audios[i]));
            cardList.add(new Card(id, images[i], audios[i]));
            id++;
        }

        // Baraja las cartas
        Collections.shuffle(cardList);

        adapter = new CardAdapter(this, cardList);
        gridView.setAdapter(adapter);


        //Agregado para video
        try {
            rootView = findViewById(android.R.id.content);

            // Inicializar Firebase
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            videoView = findViewById(R.id.videoView);
            btnSkipVideo = findViewById(R.id.btnSkipVideo);

            getInitialData();
            setupVideoView();
        } catch (Exception e) {
            Log.e("Nivel2", e.getMessage());
            showSnakBar(e.getMessage());
        }
        adapter = new CardAdapter(this, cardList);
        adapter.setOnGameEndListener(this);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onGameEnd(int cartasReveladas) {

        if(cartasReveladas <= nivel.getPuntos_minimos()) {
            setPlayVideo("final");

            String nuevoEstado = "completado";
            actualizarEstadoEnFirestore(nuevoEstado);
            nivelUsuario.setEstado(nuevoEstado);

            actualizarPuntuacionEnFirestore(String.valueOf(cartasReveladas));
            nivelUsuario.setPuntuacion(String.valueOf(cartasReveladas));
            return;
        }

        onLose();

    }


    @Override
    public void getIntentos(int cartasReveladas) {
        TextView vtConteo = findViewById(R.id.tvConteo);
        vtConteo.setText(cartasReveladas + " Cartas levantadas");

        Log.e("Nivel2", nivel.getPuntos_minimos() + "");
        if(cartasReveladas >= nivel.getPuntos_minimos()){
            onLose();
        }
    }

    private void onLose() {
        showDangerSnakBar("Juego termindao, haz alcanzado el limite de cartas leventadas. Sigue intentando");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recreate();
            }
        }, 5000);
    }

    protected void onResume() {
        super.onResume();

        showSnakBar("Iniciando bien con puntuacion " + nivelUsuario.getPuntuacion());

        //Reproducir video de introduccion.
        setPlayVideo("intro");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detiene la reproducción de la música de fondo cuando se destruye la actividad
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
        }
    }

    // Método para detener la música de fondo cuando se encuentran todos los pares
    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }



    //Funciones privadas agregadas para video.

    private void showSnakBar(String s) {
        Snackbar snackbar = Snackbar.make(rootView, s, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.GREEN);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(20);
        snackbar.show();
    }

    private void showDangerSnakBar(String s) {
        Snackbar snackbar = Snackbar.make(rootView, s, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(Color.RED);
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
            Log.e("Nivel2", nivel.getVideo());

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
        } catch (Exception e) {
            showSnakBar("Ocurrio un error>: " + e.getMessage());
        }
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
