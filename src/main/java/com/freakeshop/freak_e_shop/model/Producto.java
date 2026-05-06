/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.freakeshop.freak_e_shop.model;


public abstract class Producto {

    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String imagen;

    // Constructor
    public Producto(String id, String nombre, String descripcion, double precio, String imagen) {
        setId(id);
        setNombre(nombre);
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
    }

    // MÉTODO ABSTRACTO
    public abstract double calcularPrecio();

    // GETTERS
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }

    // SETTERS
    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }
        this.id = id;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        }
        this.nombre = nombre;
    }
}
