/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.freakeshop.freak_e_shop.model;


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
        return getPrecio(); // 👈 importante
    }

    public String getTalla() {
        return talla;
    }

    public String getMaterial() {
        return material;
    }
}