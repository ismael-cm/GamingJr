package com.example.gamingjr.model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Nivel {
    private String id;
    private String activity;
    private String estado;
    private String id_juego;
    private String nombre;
    private String param1;
    private String param2;
    private String param3;

    private int puntos_minimos;
    private String video;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId_juego() {
        return id_juego;
    }

    public void setId_juego(String id_juego) {
        this.id_juego = id_juego;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public int getPuntos_minimos() {
        return puntos_minimos;
    }

    public void setPuntos_minimos(int puntos_minimos) {
        this.puntos_minimos = puntos_minimos;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Nivel() {
    }

    public Nivel(String id, String activity, String estado, String id_juego, String nombre, int puntos_minimos, String video, String param1, String param2, String param3) {
        this.id = id;
        this.activity = activity;
        this.estado = estado;
        this.id_juego = id_juego;
        this.nombre = nombre;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.puntos_minimos = puntos_minimos;
        this.video = video;
    }
    public interface FirestoreCallback {
        void onCallback(List<Nivel> nivelesList);
    }
    public void getAll(final Nivel.FirestoreCallback firestoreCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference niveles = db.collection("niveles");

        niveles.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<Nivel> nivelesList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Log.d(TAG, document.getId() + " => " + document.getData());

                            try {
                                Map<String, Object> data = document.getData();

                                String activity = data.containsKey("activity") ? data.get("activity").toString() : "";
                                String id_juego = data.containsKey("id_juego") ? data.get("id_juego").toString() : "";
                                String nombre = data.containsKey("nombre") ? data.get("nombre").toString() : "";
                                String param1 = data.containsKey("param1") ? data.get("param1").toString() : "";
                                String param2 = data.containsKey("param2") ? data.get("param2").toString() : "";
                                String estado = data.containsKey("estado") ? data.get("estado").toString() : "";
                                String param3 = data.containsKey("param3") ? data.get("param3").toString() : "";
                                int puntos_minimos = Integer.parseInt(data.containsKey("puntos_minimos") ? data.get("puntos_minimos").toString() : "0");
                                String video = data.containsKey("video") ? data.get("video").toString() : "";

                                Nivel n = new Nivel(document.getId(),  activity,  estado,  id_juego,  nombre,  puntos_minimos,  video,  param1,  param2,  param3);
                                nivelesList.add(n);

                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing document: " + document.getId(), e);
                            }
                        }
                        firestoreCallback.onCallback(nivelesList);
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
