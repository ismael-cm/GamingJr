package com.example.gamingjr;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProgresoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_progreso);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addMenu();
    }

    private void addMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int navHome = R.id.nav_home;
                int navProgress = R.id.nav_progreso;
                int navProfile = R.id.nav_profile;
                int selectedItem = item.getItemId();

                if(selectedItem == navProgress) {
                    Intent progresoActivity = new Intent(ProgresoActivity.this, ProgresoActivity.class);
                    startActivity(progresoActivity);
                } else if (selectedItem == navProfile) {
                    Intent profileActivity = new Intent(ProgresoActivity.this, ProfileActivity.class);
                    startActivity(profileActivity);
                } else if (selectedItem == navHome) {
                    Intent profileActivity = new Intent(ProgresoActivity.this, ProfileActivity.class);
                    startActivity(profileActivity);
                }
                return true;
            }
        });
    }
}