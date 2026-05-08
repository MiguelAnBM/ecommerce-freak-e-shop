package com.freakeshop.freak_e_shop.dto;

public class ProductoDTO {

    private String id;
    private String categoria;

    // Campos comunes
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String imagen;

    // Campos Accesorio
    private String tipo;

    // Campos Mochila
    private String tamanoMochila;
    private String materialFabricacion;
    private boolean tieneCompartimentoPC;

    // Campos Comic
    private String editorial;
    private int numeroVolumen;
    private String idioma;

    // Campos Camisa
    private String talla;
    private String materialCamisa;

    // Campos FiguraColeccionable
    private String franquicia;
    private boolean edicionLimitada;

    // Campos Peluche
    private String materialPeluche;
    private String tamanoPeluche;

    public ProductoDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTamanoMochila() {
        return tamanoMochila;
    }

    public void setTamanoMochila(String tamanoMochila) {
        this.tamanoMochila = tamanoMochila;
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

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getNumeroVolumen() {
        return numeroVolumen;
    }

    public void setNumeroVolumen(int numeroVolumen) {
        this.numeroVolumen = numeroVolumen;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getMaterialCamisa() {
        return materialCamisa;
    }

    public void setMaterialCamisa(String materialCamisa) {
        this.materialCamisa = materialCamisa;
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

    public String getMaterialPeluche() {
        return materialPeluche;
    }

    public void setMaterialPeluche(String materialPeluche) {
        this.materialPeluche = materialPeluche;
    }

    public String getTamanoPeluche() {
        return tamanoPeluche;
    }

    public void setTamanoPeluche(String tamanoPeluche) {
        this.tamanoPeluche = tamanoPeluche;
    }
}
