package com.example.gamingjr.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NivelUsuario implements Serializable {
    private String id;
    private String idUsuario;
    private String idNivel;
    private String puntuacion;
    private String completado;
    private String estado;

    public NivelUsuario(String id, String idUsuario, String idNivel, String puntuacion, String completado, String estado) {
        this.idUsuario = idUsuario;
        this.idNivel = idNivel;
        this.puntuacion = puntuacion;
        this.completado = completado;
        this.estado = estado;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getters y Setters
    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getIdNivel() { return idNivel; }
    public void setIdNivel(String idNivel) { this.idNivel = idNivel; }

    public String getPuntuacion() { return puntuacion; }
    public void setPuntuacion(String puntuacion) { this.puntuacion = puntuacion; }

    public String getCompletado() { return completado; }
    public void setCompletado(String completado) { this.completado = completado; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // Método para convertir a Map
    // Método para convertir a Map (ya lo tienes)
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("id_usuario", idUsuario);
        map.put("id_nivel", idNivel);
        map.put("puntuacion", puntuacion);
        map.put("completado", completado);
        map.put("estado", estado);
        return map;
    }

    // Método estático para crear una instancia a partir de un Map
    public static NivelUsuario fromMap(Map<String, Object> map) {
        String id = (String) map.get("id");
        String idUsuario = (String) map.get("id_usuario");
        String idNivel = (String) map.get("id_nivel");
        String puntuacion = (String) map.get("puntuacion");
        String completado = (String) map.get("completado");
        String estado = (String) map.get("estado");

        return new NivelUsuario(id, idUsuario, idNivel, puntuacion, completado, estado);
    }
}