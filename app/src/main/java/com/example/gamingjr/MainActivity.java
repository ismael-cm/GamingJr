package com.example.gamingjr;


import com.google.firebase.FirebaseApp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamingjr.model.AuthListener;
import com.example.gamingjr.model.AuthManager;

public class MainActivity extends AppCompatActivity implements AuthListener {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private AuthManager mAuthManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        //Custom content by ismael
        mAuthManager = new AuthManager(this);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                mAuthManager.loginUser(username, password, MainActivity.this);
            }
        });

        listeners();
    }

    private void listeners() {
        findViewById(R.id.registerLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(this, "Éxito, inicio de sesión correcto", Toast.LENGTH_SHORT).show();
        Intent reproductor = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(reproductor);
    }

    @Override
    public void onLoginFailure(String errorMessage) {
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
        Log.e("Login", "Error: " + errorMessage);
    }

}