/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.freakeshop.freak_e_shop.model;



public class Camisa extends Producto {

    private String talla;
    private String material;

    public Camisa(String id, String nombre, String descripcion, double precio, String imagen,
                  String talla, String material) {
        super(id, nombre, descripcion, precio, imagen);
        setTalla(talla);
        setMaterial(material);
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        if (talla == null || talla.isEmpty()) {
            throw new IllegalArgumentException("La talla no puede estar vacía");
        }
        this.talla = talla;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        if (material == null || material.isEmpty()) {
            throw new IllegalArgumentException("El material no puede estar vacío");
        }
        this.material = material;
    }
}
