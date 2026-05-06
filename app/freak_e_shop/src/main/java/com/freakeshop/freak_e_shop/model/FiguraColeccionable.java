/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.freakeshop.freak_e_shop.model;

/**
 *
 * @author LENOVO
 */
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

    public boolean isEdicionLimitada() {
        return edicionLimitada;
    }
}
