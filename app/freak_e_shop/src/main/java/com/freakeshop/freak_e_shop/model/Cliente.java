package com.freakeshop.freak_e_shop.model;

public class Cliente extends Usuario {

    private String direccionEnvio;
    private String telefono;
    private int puntosFidelidad; 

    // Constructor
    public Cliente(String id, String nombre, String correo, String contrasena, String direccionEnvio, String telefono) {
        
        super(id, nombre, correo, contrasena);
        
        this.direccionEnvio = direccionEnvio;
        this.telefono = telefono;
        this.puntosFidelidad = 0;
    }

    @Override
    public String mostrarRol() {
        return "Rol: Cliente Registrado";
    }

    public void sumarPuntos(int puntos) {
        if (puntos > 0) {
            this.puntosFidelidad += puntos;
        }
    }

    // Getters y Setters
    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public void setPuntosFidelidad(int puntosFidelidad) {
        this.puntosFidelidad = puntosFidelidad;
    }
}
