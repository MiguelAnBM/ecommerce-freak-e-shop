
package com.freakeshop.freak_e_shop.model;

public class Camisa extends Producto {

    private String talla;
    private String material;

    public Camisa(String id, String nombre, String descripcion, double precio, String imagen,
            String talla, String material) {
        super(id, nombre, descripcion, precio, imagen);
        this.talla = talla;
        this.material = material;
    }

    @Override
    public double calcularPrecio() {
        return getPrecio();
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
