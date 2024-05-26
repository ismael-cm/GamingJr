package com.example.gamingjr;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingjr.adapter.JuegoAdapter;
import com.example.gamingjr.model.Juego;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int navHome = R.id.nav_home;
                int navSearch = R.id.nav_search;
                int navProfile = R.id.nav_profile;
                int selectedItem = item.getItemId();

                if(selectedItem == navSearch) {
                    Intent profileActivity = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(profileActivity);
                } else if (selectedItem == navProfile) {
                    Intent juegoActivity = new Intent(HomeActivity.this, JuegoActivity.class);
                    startActivity(juegoActivity);
                }

                Toast.makeText(HomeActivity.this, " sds " + item.getItemId() + " home " + navHome , Toast.LENGTH_SHORT).show();
                return false;
            }
        });



        RecyclerView rvJuegos;
        rvJuegos = findViewById(R.id.recyclerJuegos);

        Juego juego = new Juego();
        juego.getAll(new Juego.FirestoreCallback() {
            @Override
            public void onCallback(List<Juego> juegosList) {
                // Aquí puedes trabajar con la lista de juegos después de que se ha completado la carga
                JuegoAdapter adapter2 = new JuegoAdapter(juegosList);

                try {
                    rvJuegos.setAdapter(adapter2);
                } catch (Exception e) {
                    Toast.makeText(HomeActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.i(TAG, "Jungla: " + R.drawable.tjungla);
        Log.i(TAG, "Pirates: " + R.drawable.tpirates);
        Log.i(TAG, "space: " + R.drawable.tspace);

    }
}