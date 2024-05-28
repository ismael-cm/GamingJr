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

public class VideoIntroduccionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_introduccion);

        Intent intent = getIntent();
        Juego juego = (Juego) intent.getSerializableExtra("juego");
        NivelUsuario primerNivelUsuario = (NivelUsuario) intent.getSerializableExtra("nivel_usuario");
        String nombreVideo = juego.getThumbnail() + "introduccion";

        Toast.makeText(this, primerNivelUsuario.getEstado() + " Juego " + juego.getNombre(), Toast.LENGTH_SHORT).show();

        VideoView videoView = findViewById(R.id.videoView);
        Button buttonSkip = findViewById(R.id.buttonSkip);

        // Obtener el URI del video
        int videoResId = getResources().getIdentifier(nombreVideo, "raw", getPackageName());
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);

        videoView.setVideoURI(videoUri);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
        videoView.start();

        buttonSkip.setOnClickListener(v -> finish());
    }
}
