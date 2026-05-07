package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.model.Mochila;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MochilaRepository {

    private final String RUTA_ARCHIVO = "mochilas.txt";

    public void guardar(Mochila mochila) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            
            String linea = mochila.getId() + ";" + 
                           mochila.getNombre() + ";" + 
                           mochila.getDescripcion() + ";" + 
                           mochila.getPrecio() + ";" + 
                           mochila.getImagen() + ";" + 
                           mochila.getTamano() + ";" + 
                           mochila.getMaterialFabricacion() + ";" + 
                           mochila.isTieneCompartimentoPC();
            
            bw.write(linea);
            bw.newLine();
            
            System.out.println("Mochila guardada con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de mochilas: " + e.getMessage());
        }
    }

    public List<Mochila> obtenerTodos() {
        List<Mochila> listaMochilas = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                
                if (datos.length == 8) {
                    Mochila m = new Mochila(
                        datos[0],                        
                        datos[1],                       
                        datos[2],                        
                        Double.parseDouble(datos[3]),    
                        datos[4],                        
                        datos[5],                        
                        datos[6],                         
                        Boolean.parseBoolean(datos[7])    
                    );
                    listaMochilas.add(m);
                }
            }
        } catch (IOException e) {
            System.out.println("El archivo mochilas.txt no existe aún.");
        }
        
        return listaMochilas;
    }
}