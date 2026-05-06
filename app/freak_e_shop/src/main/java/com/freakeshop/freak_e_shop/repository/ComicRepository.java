package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.model.Comic;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComicRepository {

    private final String RUTA_ARCHIVO = "comics.txt";

    public void guardar(Comic comic) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            
            String linea = comic.getId() + ";" + 
                           comic.getNombre() + ";" + 
                           comic.getDescripcion() + ";" + 
                           comic.getPrecio() + ";" + 
                           comic.getImagen() + ";" + 
                           comic.getEditorial() + ";" + 
                           comic.getNumeroVolumen() + ";" + 
                           comic.getIdioma();
            
            bw.write(linea);
            bw.newLine();
            
            System.out.println("Comic guardado exitosamente en el archivo.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de cómics: " + e.getMessage());
        }
    }

    public List<Comic> obtenerTodos() {
        List<Comic> listaComics = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                
                if (datos.length == 8) {
                    Comic c = new Comic(
                        datos[0],                     
                        datos[1],                      
                        datos[2],                       
                        Double.parseDouble(datos[3]),   
                        datos[4],                        
                        datos[5],                      
                        Integer.parseInt(datos[6]),      
                        datos[7]                          
                    );
                    listaComics.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println("El archivo de cómics aún no existe o está vacío.");
        } catch (NumberFormatException e) {
            System.err.println("Error de formato en los datos numéricos del archivo: " + e.getMessage());
        }
        
        return listaComics;
    }
}