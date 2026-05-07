package com.freakeshop.freak_e_shop.model;

public class Mochila extends Producto {

    private String tamano;
    private String materialFabricacion;
    private boolean tieneCompartimentoPC;

    // Constructor
    public Mochila(String id, String nombre, String descripcion, double precio, String imagen, 
                   String tamano, String materialFabricacion, boolean tieneCompartimentoPC) {
        
        super(id, nombre, descripcion, precio, imagen);
        
        this.tamano = tamano;
        this.materialFabricacion = materialFabricacion;
        this.tieneCompartimentoPC = tieneCompartimentoPC;
    }

    @Override
    public double calcularPrecio() {
        return getPrecio();
    }

    // Getters y Setters

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        if (tamano == null || tamano.trim().isEmpty()) {
            throw new IllegalArgumentException("El tamaño no puede estar vacío.");
        }
        this.tamano = tamano;
    }

    public String getMaterialFabricacion() {
        return materialFabricacion;
    }

    public void setMaterialFabricacion(String materialFabricacion) {
        this.materialFabricacion = materialFabricacion;
    }

    public boolean isTieneCompartimentoPC() {
        return tieneCompartimentoPC;
    }

    public void setTieneCompartimentoPC(boolean tieneCompartimentoPC) {
        this.tieneCompartimentoPC = tieneCompartimentoPC;
    }
}