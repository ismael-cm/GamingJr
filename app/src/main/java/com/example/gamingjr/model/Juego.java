package com.example.gamingjr.model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gamingjr.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Juego {
    private  String nombre;
    private String subtitulo;
    private String thumbnail;
    public List<Juego> juegosList = new ArrayList<>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSlug(String slug) {
        this.subtitulo = subtitulo;
    }

    public Juego(String nombre, String subtitulo, String thumbnail) {
        this.nombre = nombre;
        this.subtitulo = subtitulo;
        this.thumbnail = thumbnail;
    }

    public Juego() {
    }
    private static final String TAG = "Juego";
    public interface FirestoreCallback {
        void onCallback(List<Juego> juegosList);
    }
    public void getAll(final FirestoreCallback firestoreCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference juegos = db.collection("juegos");

        juegos.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<Juego> juegosList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Log.d(TAG, document.getId() + " => " + document.getData());

                            try {
                                Map<String, Object> data = document.getData();


                                String nombre = data.containsKey("nombre") ? data.get("nombre").toString() : "";
                                String subtitulo = data.containsKey("subtitulo") ? data.get("subtitulo").toString() : "";
                                String thumbnail = data.containsKey("thumbnail") ? data.get("thumbnail").toString() : "";

                                Juego j = new Juego(nombre, subtitulo, thumbnail);
                                juegosList.add(j);

                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing document: " + document.getId(), e);
                            }
                        }
                        firestoreCallback.onCallback(juegosList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error al obtener documentos", e);
                    }
                });
    }
}
