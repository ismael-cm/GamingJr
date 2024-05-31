package com.example.gamingjr;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AgregarJuegoActivity extends AppCompatActivity {


    private FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_juego);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setTitle("Crear Juego");
        mfirestore = FirebaseFirestore.getInstance();


        TextView nombre, slug, estado, thumbnail;
        nombre = findViewById(R.id.nombre);
        slug = findViewById(R.id.slug);
        estado = findViewById(R.id.estado);
        thumbnail = findViewById(R.id.thumbnail);

        findViewById(R.id.guardar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombrejuego = nombre.getText().toString().trim();
                String slugjuego = slug.getText().toString().trim();
                String estadojuego = estado.getText().toString().trim();
                String thumbnailjuego = thumbnail.getText().toString().trim();



                postJuego(nombrejuego, slugjuego, estadojuego, thumbnailjuego);
            }
        });

    }

    private void postJuego(String nombrejuego, String slugjuego, String estadojuego, String thumbnailjuego) {
        if(nombrejuego.isEmpty() && slugjuego.isEmpty() && estadojuego.isEmpty() && thumbnailjuego.isEmpty()) {
            Toast.makeText(getApplicationContext(), "LLene todos los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> map = new HashMap<>();

        map.put("nombre", nombrejuego);
        map.put("slug", slugjuego);
        map.put("estado", estadojuego);
        map.put("thumbnail", thumbnailjuego);

        mfirestore.collection("game").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Ingresado con éxito", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });



    }
}