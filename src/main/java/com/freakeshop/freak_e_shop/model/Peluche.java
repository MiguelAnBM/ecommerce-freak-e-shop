package com.freakeshop.freak_e_shop.model;

public class Peluche extends Producto {

    private String material;
    private String tamano;

    public Peluche(String id, String nombre, String descripcion, double precio, String imagen, String material,
            String tamano) {
        super(id, nombre, descripcion, precio, imagen);
        this.material = material;
        this.tamano = tamano;
    }

    @Override
    public double calcularPrecio() {
        if (tamano.equalsIgnoreCase("Grande")) {
            return getPrecio() * 1.20;
        }
        return getPrecio();
    }

    // Getters y Setters
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }
}
