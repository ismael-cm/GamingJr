package com.example.gamingjr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamingjr.model.Juego;
import com.example.gamingjr.model.NivelUsuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class VideoIntroduccionActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    NivelUsuario primerNivelUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_introduccion);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        Juego juego = (Juego) intent.getSerializableExtra("juego");
        primerNivelUsuario = (NivelUsuario) intent.getSerializableExtra("nivel_usuario");
        String nombreVideo = juego.getThumbnail() + "introduccion";

        VideoView videoView = findViewById(R.id.videoView);
        Button buttonSkip = findViewById(R.id.buttonSkip);

        // Obtener el URI del video
        int videoResId = getResources().getIdentifier(nombreVideo, "raw", getPackageName());
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);

        videoView.setVideoURI(videoUri);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                updateNivelUsuario(primerNivelUsuario);
                finish();
            }
        });
        videoView.start();

        buttonSkip.setOnClickListener(v -> {
            updateNivelUsuario(primerNivelUsuario);
            finish();
        });
    }

    private void updateNivelUsuario(NivelUsuario nivelUsuario) {
        if ("no_comenzado".equals(nivelUsuario.getEstado())) {
            Map<String, Object> update = new HashMap<>();
            update.put("estado", "activo");

            db.collection("nivel_user")
                    .document(nivelUsuario.getId())
                    .set(update, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al actualizar el nivel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

            primerNivelUsuario.setEstado("activo");
        }
    }
}
