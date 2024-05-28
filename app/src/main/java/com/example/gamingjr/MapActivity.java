package com.example.gamingjr;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamingjr.model.Juego;
import com.example.gamingjr.model.Nivel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        LinearLayout levelsContainer = findViewById(R.id.levelsContainer);

        Intent intent = getIntent();
        Juego juego = (Juego) intent.getSerializableExtra("juego");
        Log.d(TAG, "Llamando a getNiveles con id_juego: " + juego.getId());

        // Accede a los niveles del juego seleccionado
        List<Nivel> niveles = juego.getNiveles();

        // Ordena la lista de niveles por el campo 'orden'
        Collections.sort(niveles, new Comparator<Nivel>() {
            @Override
            public int compare(Nivel n1, Nivel n2) {
                return Integer.compare(n1.getOrden(), n2.getOrden());
            }
        });

        // Añadir botón de ver video introducción
        Button videoButton = new Button(this);
        videoButton.setText("Ver Video Introducción");
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this, "Ver Video Introducción", Toast.LENGTH_SHORT).show();
                // Aquí puedes iniciar una actividad de video, por ejemplo:
                // Intent videoIntent = new Intent(MapActivity.this, VideoActivity.class);
                // startActivity(videoIntent);
            }
        });
        levelsContainer.addView(videoButton);

        // Crear una vista alternada de título e imagen para cada nivel
        boolean isTitleOnLeft = true;
        for (Nivel nivel : niveles) {
            LinearLayout levelLayout = new LinearLayout(this);
            levelLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    300  // Altura fija para hacer las imágenes más grandes
            ));
            levelLayout.setOrientation(LinearLayout.HORIZONTAL);
            levelLayout.setGravity(Gravity.CENTER);  // Centrar los elementos dentro del contenedor

            TextView titleView = new TextView(this);
            titleView.setText(nivel.getNombre());
            titleView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            ));
            titleView.setGravity(Gravity.CENTER);

            ImageView imageView = new ImageView(this);
            // Configura tu imagen aquí
            imageView.setImageResource(R.drawable.default_image);  // placeholder debe ser una imagen en tu carpeta drawable
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            ));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if (isTitleOnLeft) {
                levelLayout.addView(titleView);
                levelLayout.addView(imageView);
            } else {
                levelLayout.addView(imageView);
                levelLayout.addView(titleView);
            }

            // Alterna la disposición para el siguiente nivel
            isTitleOnLeft = !isTitleOnLeft;

            // Añade el nivelLayout al levelsContainer
            levelsContainer.addView(levelLayout);

            // Añadir lógica de clic al título
            titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MapActivity.this, "Iniciando " + nivel.getNombre(), Toast.LENGTH_SHORT).show();
                    // Aquí puedes iniciar la actividad correspondiente al nivel, por ejemplo:
                    // Intent nivelIntent = new Intent(MapActivity.this, NivelActivity.class);
                    // nivelIntent.putExtra("nivel", nivel);
                    // startActivity(nivelIntent);
                }
            });
        }
    }
}
