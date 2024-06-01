package com.example.gamingjr.niveles;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamingjr.R;
import com.example.gamingjr.adapter.CardAdapter;
import com.example.gamingjr.model.AuthListener;
import com.example.gamingjr.model.Card;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Nivel2Activity extends AppCompatActivity {


    private GridView gridView;
    private CardAdapter adapter;
    private List<Card> cardList;
    private int paresEncontrados = 0;
    private MediaPlayer backgroundMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel2);

        // Inicializa el MediaPlayer para la música de fondo
        backgroundMusic = MediaPlayer.create(this, R.raw.fondo);
        backgroundMusic.setLooping(true); // Repite la música de fondo
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


}
