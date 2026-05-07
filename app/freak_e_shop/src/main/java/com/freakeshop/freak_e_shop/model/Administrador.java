package com.freakeshop.freak_e_shop.model;

public class Administrador extends Usuario {

    private String nivelDeAcceso;

    // Constructor
    public Administrador(String id, String nombre, String correo, String contrasena, String nivelDeAcceso) {
        
        super(id, nombre, correo, contrasena);
        
        this.nivelDeAcceso = nivelDeAcceso;
    }

    @Override
    public String mostrarRol() {
        return "Rol: Administrador (Nivel de acceso: " + this.nivelDeAcceso + ")";
    }

    // Getters y Setters
    public String getNivelDeAcceso() {
        return nivelDeAcceso;
    }

    public void setNivelDeAcceso(String nivelDeAcceso) {
        this.nivelDeAcceso = nivelDeAcceso;
    }
}