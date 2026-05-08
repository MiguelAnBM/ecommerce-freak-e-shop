package com.freakeshop.freak_e_shop.model;

import java.util.List;

public class Pedido {
    private String numeroPedido;
    private String fecha;
    private String direccion;
    private String telefono;
    private String metodoPago;
    private double total;
    private List<ItemPedido> items;

    public Pedido() {
    }

    public Pedido(String numeroPedido, String fecha, String direccion, String telefono, String metodoPago, double total,
            List<ItemPedido> items) {
        this.numeroPedido = numeroPedido;
        this.fecha = fecha;
        this.direccion = direccion;
        this.telefono = telefono;
        this.metodoPago = metodoPago;
        this.total = total;
        this.items = items;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public void setItems(List<ItemPedido> items) {
        this.items = items;
    }
}
