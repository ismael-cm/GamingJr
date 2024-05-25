package com.example.gamingjr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor ingresa usuario y contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        boolean loginSuccessful = checkLogin(username, password);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (loginSuccessful) {
                                    Toast.makeText(MainActivity.this, "Éxito, inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                                    Intent reproductor = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(reproductor);
                                } else {
                                    Toast.makeText(MainActivity.this, "Usuario o contraseña no válidos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });

    }

    private boolean checkLogin(String username, String password) {
        return username.equals("ismael") && password.equals("123");
    }
}