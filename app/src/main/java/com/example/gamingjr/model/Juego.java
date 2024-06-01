package com.example.gamingjr.model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Juego implements Serializable {
    private String nombre;
    private String id;
    private String subtitulo;
    private String thumbnail;
    public List<Juego> juegosList = new ArrayList<>();
    private List<Nivel> niveles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public List<Juego> getJuegosList() {
        return juegosList;
    }

    public void setJuegosList(List<Juego> juegosList) {
        this.juegosList = juegosList;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    public Juego(String id, String nombre, String subtitulo, String thumbnail) {
        this.id = id;
        this.nombre = nombre;
        this.subtitulo = subtitulo;
        this.thumbnail = thumbnail;
    }

    public Juego() {
        this.niveles = new ArrayList<>();
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

                        for (QueryDocumentSnapshot juegoDocument : querySnapshot) {
                            Log.d(TAG, juegoDocument.getId() + " => " + juegoDocument.getData());

                            try {
                                Map<String, Object> data = juegoDocument.getData();

                                String nombre = data.containsKey("nombre") ? data.get("nombre").toString() : "";
                                String subtitulo = data.containsKey("subtitulo") ? data.get("subtitulo").toString() : "";
                                String thumbnail = data.containsKey("thumbnail") ? data.get("thumbnail").toString() : "";

                                Juego juego = new Juego(juegoDocument.getId(), nombre, subtitulo, thumbnail);

                                // Obtener los niveles que hacen referencia a este juego
                                CollectionReference niveles = db.collection("niveles");
                                niveles.whereEqualTo("id_juego", juegoDocument.getReference())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot nivelQuerySnapshot) {
                                                List<Nivel> nivelesList = new ArrayList<>();
                                                for (QueryDocumentSnapshot nivelDocument : nivelQuerySnapshot) {
                                                    Log.d(TAG, nivelDocument.getId() + " => " + nivelDocument.getData());

                                                    try {
                                                        Map<String, Object> data = nivelDocument.getData();

                                                        String activity = data.containsKey("activity") ? data.get("activity").toString() : "";
                                                        String id_juego = data.containsKey("id_juego") ? data.get("id_juego").toString() : "";
                                                        String nombre = data.containsKey("nombre") ? data.get("nombre").toString() : "";
                                                        String param1 = data.containsKey("param1") ? data.get("param1").toString() : "";
                                                        String param2 = data.containsKey("param2") ? data.get("param2").toString() : "";
                                                        String estado = data.containsKey("estado") ? data.get("estado").toString() : "";
                                                        String param3 = data.containsKey("param3") ? data.get("param3").toString() : "";
                                                        int puntos_minimos = Integer.parseInt(data.containsKey("puntos_minimos") ? data.get("puntos_minimos").toString() : "0");
                                                        int orden = Integer.parseInt(data.containsKey("orden") ? data.get("orden").toString() : "0");
                                                        String video = data.containsKey("video") ? data.get("video").toString() : "";

                                                        Nivel n = new Nivel(nivelDocument.getId(), activity, estado, id_juego, nombre, puntos_minimos, video, param1, param2, param3, orden);
                                                        nivelesList.add(n);

                                                    } catch (Exception e) {
                                                        Log.e(TAG, "Error parsing document: " + nivelDocument.getId(), e);
                                                    }
                                                }

                                                Collections.sort(nivelesList, new Comparator<Nivel>() {
                                                    @Override
                                                    public int compare(Nivel n1, Nivel n2) {
                                                        return Integer.compare(n1.getOrden(), n2.getOrden()); // Cambiado para orden descendente
                                                    }
                                                });


                                                juego.setNiveles(nivelesList); // Asignar la lista de niveles ordenados al juego
                                                juegosList.add(juego);

                                                // Verificar si se procesaron todos los juegos
                                                if (juegosList.size() == querySnapshot.size()) {
                                                    firestoreCallback.onCallback(juegosList);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error fetching niveles for juego: " + juegoDocument.getId(), e);
                                            }
                                        });

                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing document: " + juegoDocument.getId(), e);
                            }
                        }
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
