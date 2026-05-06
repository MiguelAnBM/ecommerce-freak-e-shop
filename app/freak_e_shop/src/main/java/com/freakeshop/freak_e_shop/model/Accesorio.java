/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.freakeshop.freak_e_shop.model;

/**
 *
 * @author LENOVO
 */

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
}
