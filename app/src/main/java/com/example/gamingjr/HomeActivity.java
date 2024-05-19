package com.example.gamingjr;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingjr.model.Juego;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Log.i(TAG, "Jungla: " + R.drawable.tjungla);
        Log.i(TAG, "Pirates: " + R.drawable.tpirates);
        Log.i(TAG, "space: " + R.drawable.tspace);

        RecyclerView rvJuegos;
        rvJuegos = findViewById(R.id.recyclerJuegos);

        Juego juego = new Juego();
        juego.getAll(new Juego.FirestoreCallback() {
            @Override
            public void onCallback(List<Juego> juegosList) {
                // Aquí puedes trabajar con la lista de juegos después de que se ha completado la carga
                for (Juego juego : juegosList) {
                    Toast.makeText(HomeActivity.this, juego.getNombre(), Toast.LENGTH_SHORT).show();
                }

                JuegoAdapter adapter2 = new JuegoAdapter(juegosList);

                try {
                    rvJuegos.setAdapter(adapter2);
                } catch (Exception e) {
                    Toast.makeText(HomeActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //JuegoAdapter adapter2 = new JuegoAdapter(listDatos);



        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AgregarJuegoActivity.class));
            }
        });
    }
}