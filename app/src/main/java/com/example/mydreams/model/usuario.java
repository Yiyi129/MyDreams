package com.example.mydreams.model;

public class usuario {
    String contraseña, email, nombre, username;

    public usuario(){}

    public usuario(String contraseña, String email, String nombre, String username) {
        this.contraseña = contraseña;
        this.email = email;
        this.nombre = nombre;
        this.username = username;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
















