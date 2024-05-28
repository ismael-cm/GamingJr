package com.example.gamingjr;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingjr.adapter.JuegoAdapter;
import com.example.gamingjr.adapter.OnItemClickListener;
import com.example.gamingjr.model.Juego;
import com.example.gamingjr.model.Nivel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        addMenu();

        RecyclerView rvJuegos;
        rvJuegos = findViewById(R.id.recyclerJuegos);

        Juego juego = new Juego();
        juego.getAll(new Juego.FirestoreCallback() {
            @Override
            public void onCallback(List<Juego> juegosList) {
                JuegoAdapter adapter2 = new JuegoAdapter(juegosList, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Juego juego) {
                        Intent intent = new Intent(HomeActivity.this, MapActivity.class);
                        intent.putExtra("juego", juego);




                        startActivity(intent);
                    }
                });

                try {
                    rvJuegos.setAdapter(adapter2);
                } catch (Exception e) {
                    Toast.makeText(HomeActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void addMenu() {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int navHome = R.id.nav_home;
                int navProgress = R.id.nav_progreso;
                int navProfile = R.id.nav_profile;
                int selectedItem = item.getItemId();

                if (selectedItem == navProgress) {
                    Intent progresoActivity = new Intent(HomeActivity.this, ProgresoActivity.class);
                    startActivity(progresoActivity);
                } else if (selectedItem == navProfile) {
                    Intent profileActivity = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(profileActivity);
                }
                return false;
            }
        });
    }
}
