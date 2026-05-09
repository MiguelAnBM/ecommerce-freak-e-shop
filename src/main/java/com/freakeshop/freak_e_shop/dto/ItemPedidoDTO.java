package com.freakeshop.freak_e_shop.dto;

// DTO que representa un ítem individual dentro de un pedido.
public class ItemPedidoDTO {

    private String id;
    private String nombre;
    private double precio;
    private int cantidad;

    public ItemPedidoDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
