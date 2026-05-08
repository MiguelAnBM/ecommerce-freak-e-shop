
package com.freakeshop.freak_e_shop.model;

public class FiguraColeccionable extends Producto {

    private String franquicia;
    private boolean edicionLimitada;

    public FiguraColeccionable(String id, String nombre, String descripcion, double precio, String imagen,
            String franquicia, boolean edicionLimitada) {
        super(id, nombre, descripcion, precio, imagen);
        this.franquicia = franquicia;
        this.edicionLimitada = edicionLimitada;
    }

    @Override
    public double calcularPrecio() {
        return getPrecio();
    }

    public String getFranquicia() {
        return franquicia;
    }

    public void setFranquicia(String franquicia) {
        this.franquicia = franquicia;
    }

    public boolean isEdicionLimitada() {
        return edicionLimitada;
    }

    public void setEdicionLimitada(boolean edicionLimitada) {
        this.edicionLimitada = edicionLimitada;
    }
}
