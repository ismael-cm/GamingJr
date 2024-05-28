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

import com.example.gamingjr.model.NivelUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamingjr.model.Juego;
import com.example.gamingjr.model.Nivel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MapActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    boolean isTitleOnLeft = true;
    Button videoButton;
    Juego juego;
    NivelUsuario primerNivelUsuario;
    int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        db = FirebaseFirestore.getInstance();

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Obtener el usuario actual
        final FirebaseUser user = mAuth.getCurrentUser();


        LinearLayout levelsContainer = findViewById(R.id.levelsContainer);

        Intent intent = getIntent();
        juego = (Juego) intent.getSerializableExtra("juego");
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
        videoButton = new Button(this);
        videoButton.setText("Ver Video Introducción");

        levelsContainer.addView(videoButton);

        // Crear una vista alternada de título e imagen para cada nivel
        index = 0;
        for (Nivel nivel : niveles) {
            //createNivelUsuario();
            createNivelUsuario(user.getUid(), nivel.getId(), new NivelUsuarioCallback() {
                @Override
                public void onCallback(NivelUsuario nivelUsuario) {

                    LinearLayout levelLayout = new LinearLayout(MapActivity.this);
                    levelLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            300  // Altura fija para hacer las imágenes más grandes
                    ));
                    levelLayout.setOrientation(LinearLayout.HORIZONTAL);
                    levelLayout.setGravity(Gravity.CENTER);  // Centrar los elementos dentro del contenedor

                    TextView titleView = new TextView(MapActivity.this);
                    titleView.setText(nivel.getNombre());
                    titleView.setLayoutParams(new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1
                    ));
                    titleView.setGravity(Gravity.CENTER);

                    ImageView imageView = new ImageView(MapActivity.this);

                    //Imagen para elementos bloqueados
                    String imageName = nivel.getVideo();
                    if (nivelUsuario.getEstado().equals("no_comenzado")){
                        imageName = "level_block";
                    }
                    int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    imageView.setImageResource(imageResId);


                    if(MapActivity.this.index == 0) {
                        MapActivity.this.primerNivelUsuario = nivelUsuario;
                        MapActivity.this.index++;
                    }

                    imageView.setLayoutParams(new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1
                    ));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    if (MapActivity.this.isTitleOnLeft) {
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

                    imageView.setOnClickListener(new View.OnClickListener() {
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
            });


        }
    }
    interface LevelLockCallback {
        void onResult(String result);
    }

    @Override
    protected void onStart() {
        super.onStart();

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this, "Ver Video Introducción", Toast.LENGTH_SHORT).show();

                Intent videoIntent = new Intent(MapActivity.this, VideoIntroduccionActivity.class);
                videoIntent.putExtra("juego", juego);
                videoIntent.putExtra("nivel_usuario", primerNivelUsuario);
                startActivity(videoIntent);
            }
        });
    }

    public void isLevelLock(String id_usuario, String id_nivel, LevelLockCallback callback) {
        AtomicReference<String> result = new AtomicReference<>("lock");
        db.collection("nivel_user")
                .whereEqualTo("id_nivel", id_nivel)
                .whereEqualTo("id_usuario", id_usuario)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String estado = documentSnapshot.getString("estado");

                            if (estado.equals("no_comenzado")) {
                                result.set("lock");
                            } else {
                                result.set("unlock");
                            }
                        }
                    }
                    callback.onResult(result.get());
                });
    }

    public interface NivelUsuarioCallback {
        void onCallback(NivelUsuario nivelUsuario);
    }


    protected void createNivelUsuario(String id_usuario, String id_nivel, final NivelUsuarioCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference nivelUserCollection = db.collection("nivel_user");
        DocumentReference userDocument = nivelUserCollection.document(id_usuario + "_" + id_nivel);

        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "El documento nivel_user ya existe para el usuario con ID: " + id_usuario + " y ID de nivel: " + id_nivel);
                        Map<String, Object> data = document.getData();
                        if (data != null) {
                            NivelUsuario nivelUsuario = NivelUsuario.fromMap(data);
                            callback.onCallback(nivelUsuario);
                        } else {
                            callback.onCallback(null);
                        }
                    } else {
                        NivelUsuario nuevoNivelUsuario = new NivelUsuario(
                                id_usuario + "_" + id_nivel, id_usuario, id_nivel, "0", "false", "no_comenzado");

                        userDocument.set(nuevoNivelUsuario.toMap())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Documento nivel_user creado exitosamente para el usuario con ID: " + id_usuario + " y ID de nivel: " + id_nivel);
                                        callback.onCallback(nuevoNivelUsuario);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "Error al crear el documento nivel_user para el usuario con ID: " + id_usuario + " y ID de nivel: " + id_nivel, e);
                                        callback.onCallback(null); // Manejar error adecuadamente
                                    }
                                });
                    }
                } else {
                    Log.e(TAG, "Error al verificar la existencia del documento nivel_user para el usuario con ID: " + id_usuario + " y ID de nivel: " + id_nivel, task.getException());
                    callback.onCallback(null); // Manejar error adecuadamente
                }
            }
        });
    }

}
