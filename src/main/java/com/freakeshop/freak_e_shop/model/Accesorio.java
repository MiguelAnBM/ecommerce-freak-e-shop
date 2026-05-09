
package com.freakeshop.freak_e_shop.model;

public class Accesorio extends Producto {

    private String tipo; // collar, pulsera, etc.

    public Accesorio(String id, String nombre, String descripcion, double precio, String imagen,
            String tipo) {
        super(id, nombre, descripcion, precio, imagen);
        this.tipo = tipo;
    }

    @Override
    public double calcularPrecio() {
        return getPrecio();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
