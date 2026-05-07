package com.freakeshop.freak_e_shop.model;

public class Cliente extends Usuario {

    private String direccionEnvio;
    private String telefono;

    // 2. Constructor 
    public Cliente(String id, String nombre, String correo, String contrasena) {
        
        super(id, nombre, correo, contrasena);
        
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