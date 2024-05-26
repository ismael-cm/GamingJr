package com.example.gamingjr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText editTextEmail, editTextPassword, editTextRepeatPassword, editTextDisplayName;
    private ImageView imageViewProfile;
    private Uri imageUri;
    private Button buttonRegister, buttonSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register2);

        // Inicializar FirebaseAuth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar vistas
        editTextDisplayName = findViewById(R.id.editTextDisplayName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Configurar el botón para seleccionar imagen
        buttonSelectImage.setOnClickListener(view -> openFileChooser());

        // Configurar el botón de registro
        buttonRegister.setOnClickListener(view -> {
            String displayName = editTextDisplayName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String password2 = editTextRepeatPassword.getText().toString().trim();

            if (displayName.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(password2)) {
                Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(displayName, email, password);
        });

        findViewById(R.id.buttonCancel).setOnClickListener(v -> finish());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Register", e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerUser(String displayName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso
                        Toast.makeText(RegisterActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Subir la imagen de perfil
                            if (imageUri != null) {
                                uploadProfileImage(user, displayName, email);
                            } else {
                                updateProfile(user, displayName, null, email);
                            }
                        }
                    } else {
                        // Error en el registro
                        Toast.makeText(RegisterActivity.this, "Error al registrar el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadProfileImage(FirebaseUser user, String displayName, String email) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("profileImages/" + UUID.randomUUID().toString());
        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            updateProfile(user, displayName, imageUrl, email);
        }));
    }

    private void updateProfile(FirebaseUser user, String displayName, String imageUrl, String email) {
        UserProfileChangeRequest.Builder profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName);

        if (imageUrl != null) {
            profileUpdates.setPhotoUri(Uri.parse(imageUrl));
        }

        user.updateProfile(profileUpdates.build()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                saveUserToFirestore(user.getUid(), displayName, email, imageUrl);
            } else {
                Toast.makeText(RegisterActivity.this, "Error al actualizar el perfil: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserToFirestore(String uid, String displayName, String email, @Nullable String imageUrl) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("displayName", displayName);
        userMap.put("email", email);
        if (imageUrl != null) {
            userMap.put("imageUrl", imageUrl);
        }

        db.collection("users").document(uid).set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Perfil guardado en Firestore", Toast.LENGTH_SHORT).show();
                    // Redirigir a la pantalla de inicio
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error al guardar en Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
