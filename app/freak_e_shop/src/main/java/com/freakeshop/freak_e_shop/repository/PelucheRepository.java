package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.model.Peluche;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PelucheRepository {

    private final String RUTA_ARCHIVO = "peluches.txt";

    public void guardar(Peluche peluche) {
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            
            String linea = peluche.getId() + ";" + 
                           peluche.getNombre() + ";" + 
                           peluche.getDescripcion() + ";" + 
                           peluche.getPrecio() + ";" + 
                           peluche.getImagen() + ";" + 
                           peluche.getMaterial() + ";" + 
                           peluche.getTamano();
            
            bw.write(linea);
            bw.newLine();
            
        } catch (IOException e) {
            System.out.println("Error al guardar el peluche: " + e.getMessage());
        }
    }

    public List<Peluche> obtenerTodos() {
        List<Peluche> listaPeluches = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            
            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(";");
                
                if (datos.length == 7) {

                    Peluche p = new Peluche(
                        datos[0],               
                        datos[1],                
                        datos[2],                
                        Double.parseDouble(datos[3]), 
                        datos[4],                
                        datos[5],               
                        datos[6]                
                    );
                    listaPeluches.add(p);
                }
            }
        } catch (IOException e) {

            System.out.println("No se encontró el archivo o está vacío. Se creará uno nuevo al guardar.");
        }
        
        return listaPeluches;
    }
}