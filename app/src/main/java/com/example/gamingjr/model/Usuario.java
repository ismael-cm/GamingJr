package com.example.gamingjr.model;

public class Usuario {
    private String id;
    private String correo;
    private String passoword;
    private String nombres;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassoword() {
        return passoword;
    }

    public void setPassoword(String passoword) {
        this.passoword = passoword;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Usuario(String id, String correo, String passoword, String nombres) {
        this.id = id;
        this.correo = correo;
        this.passoword = passoword;
        this.nombres = nombres;
    }

    public Usuario(String correo, String passoword) {
        this.correo = correo;
        this.passoword = passoword;
    }

    public Usuario() {
    }
}
