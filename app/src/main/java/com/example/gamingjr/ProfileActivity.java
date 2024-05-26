package com.example.gamingjr;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextName, editTextPassword;
    private Button buttonSaveChanges, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Inicializar vistas
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Obtener datos del usuario autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Mostrar el nombre del usuario en el campo de texto
            editTextName.setText(currentUser.getEmail());
        }

        // Configurar el botón para guardar cambios
        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardar los cambios en el perfil del usuario
                String newName = editTextName.getText().toString();
                // Actualizar el nombre del usuario (y otros datos si es necesario)
                // mAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(newName).build());
                // mAuth.getCurrentUser().updatePassword(newPassword);

                // Mostrar un mensaje de éxito
                Toast.makeText(ProfileActivity.this, "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el botón para cerrar sesión
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar sesión
                mAuth.signOut();
                // Redirigir a la actividad de inicio de sesión
                // Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                // startActivity(intent);
                // finish(); // Opcional: cierra esta actividad
            }
        });
    }
}
