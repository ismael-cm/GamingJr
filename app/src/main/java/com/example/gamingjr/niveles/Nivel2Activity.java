package com.example.gamingjr.niveles;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamingjr.MapActivity;
import com.example.gamingjr.R;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Nivel2Activity extends AppCompatActivity {

    String param1, param2, param3;
    FirebaseUser user;
    View rootView;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    NivelUsuario nivelUsuario;
    TextView tvNombreNivel;
    Nivel nivel;
    Button btnNiveles;
    private MediaPlayer backgroundMusic;
    private VideoView videoView;
    private Button btnSkipVideo;
    boolean isFinished = false;
    private final List<Integer> imageResources = new ArrayList<>();
    private int pairsFound = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nivel2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnNiveles= findViewById(R.id.btnNiveles);
        btnNiveles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nivel2Activity.this, MapActivity.class);
                startActivity(intent);
            }
        });


        // Inicializa el MediaPlayer para la música de fondo marvin y cubias
        backgroundMusic = MediaPlayer.create(this, R.raw.fondo);
        backgroundMusic.setLooping(true); // Repite la música de fondo
        backgroundMusic.setVolume(0.05f, 0.05f);
        backgroundMusic.start(); // Inicia la reproducción de la música de fondo



        //Codigo de marvin
        imageResources.add(R.drawable.lunch);
        imageResources.add(R.drawable.cabin);
        imageResources.add(R.drawable.suit);
        imageResources.add(R.drawable.despegar);
        imageResources.add(R.drawable.base);
        imageResources.add(R.drawable.lunch);
        imageResources.add(R.drawable.cabin);
        imageResources.add(R.drawable.suit);
        imageResources.add(R.drawable.despegar);
        imageResources.add(R.drawable.base);

        // Añadir más imágenes en pares aquí

        // Mezclar las imágenes
        Collections.shuffle(imageResources);

        // Obtener el GridLayout del layout
        GridLayout gridLayout = findViewById(R.id.grid_layout);

        // Configurar parámetros de Layout
        int numColumns = gridLayout.getColumnCount();
        int numRows = gridLayout.getRowCount();
        int totalCells = numColumns * numRows;

        // Crear ImageViews y añadirlas al GridLayout
        for (int i = 0; i < totalCells && i < imageResources.size(); i++) {
            int imageRes = imageResources.get(i);
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(ContextCompat.getDrawable(this, imageRes));

            // Configurar el tamaño de las imágenes
            int imageSize = getResources().getDisplayMetrics().widthPixels / numColumns;
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = imageSize;
            params.height = imageSize;
            params.setMargins(-5, 10, -5, 10);
            imageView.setLayoutParams(params);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(20, 20, 20, 20); // Ajustar padding si es necesario
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(null, shadowBuilder, v, 0);
                    return true;
                }




            });
            imageView.setOnDragListener(new DragListener());
            gridLayout.addView(imageView);


        }


        rootView = findViewById(android.R.id.content);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        videoView = findViewById(R.id.videoView);
        btnSkipVideo = findViewById(R.id.btnSkipVideo);


        getInitialData();
        setupButtons();
        setupVideoView();

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

    private class DragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    view.setAlpha(0.5f);
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    view.setAlpha(1.0f);
                    return true;
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) dragEvent.getLocalState();
                    Drawable draggedImage = ((ImageView) draggedView).getDrawable();
                    Drawable targetImage = ((ImageView) view).getDrawable();

                    if (Objects.equals(draggedImage.getConstantState(), targetImage.getConstantState())) {
                        draggedView.setVisibility(View.INVISIBLE);
                        view.setVisibility(View.INVISIBLE);
                        pairsFound++;
                        if (pairsFound == imageResources.size() / 2) {
                            Toast.makeText(Nivel2Activity.this, "Has avanzado de nivel, suerte en tu viaje astronauta", Toast.LENGTH_LONG).show();
                            int puntosActuales = Integer.parseInt(nivelUsuario.getPuntuacion());
                            int nuevosPuntos = puntosActuales + 105;


                                setPlayVideo("final");

                                String nuevoEstado = "completado";
                                actualizarEstadoEnFirestore(nuevoEstado);
                                nivelUsuario.setEstado(nuevoEstado);


                            actualizarPuntuacionEnFirestore(String.valueOf(nuevosPuntos));
                            nivelUsuario.setPuntuacion(String.valueOf(nuevosPuntos));
                        }
                    } else {
                        view.setAlpha(1.0f);
                    }

                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    view.setAlpha(1.0f);
                    return true;
                default:
                    return false;
            }

        }

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
