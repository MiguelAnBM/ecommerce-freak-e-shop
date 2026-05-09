package com.freakeshop.freak_e_shop.model;

public class Comic extends Producto {
    
    private String editorial;
    private int numeroVolumen;
    private String idioma;

    // Constructor
    public Comic(String id, String nombre, String descripcion, double precio, String imagen, String editorial, int numeroVolumen, String idioma) {
        
        super(id, nombre, descripcion, precio, imagen);
        
        this.editorial = editorial;
        this.numeroVolumen = numeroVolumen;
        this.idioma = idioma;
    }

    @Override
    public double calcularPrecio() {

        return getPrecio(); 
    }

    //  Getters y Setters 

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
}
