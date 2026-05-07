package com.freakeshop.freak_e_shop.model;

public class Cliente extends Usuario {

    // --- 1. Atributos específicos del Cliente ---
    private String direccionEnvio;
    private String telefono;

    // --- 2. Constructor ---
    // Solo pide los datos básicos requeridos por la clase padre (Usuario)
    public Cliente(String id, String nombre, String correo, String contrasena) {
        
        super(id, nombre, correo, contrasena);
        
        // Inicializamos estos valores vacíos. 
        // Se llenarán usando los setters cuando el cliente vaya a pagar en el carrito.
        this.direccionEnvio = "";
        this.telefono = "";
    }

    // --- 3. Getters y Setters ---

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
}