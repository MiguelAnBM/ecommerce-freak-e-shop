/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.model.FiguraColeccionable;
import java.io.*;
import java.util.*;

public class FiguraColeccionableRepository {

    private final String RUTA_ARCHIVO = "figuras.txt";

    public void guardar(FiguraColeccionable figura) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {

            String linea = figura.getId() + ";" +
                           figura.getNombre() + ";" +
                           figura.getDescripcion() + ";" +
                           figura.getPrecio() + ";" +
                           figura.getImagen() + ";" +
                           figura.getFranquicia() + ";" +
                           figura.isEdicionLimitada();

            bw.write(linea);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error al guardar figura: " + e.getMessage());
        }
    }

    public List<FiguraColeccionable> obtenerTodos() {
        List<FiguraColeccionable> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");

                if (datos.length == 7) {
                    FiguraColeccionable f = new FiguraColeccionable(
                        datos[0],
                        datos[1],
                        datos[2],
                        Double.parseDouble(datos[3]),
                        datos[4],
                        datos[5],
                        Boolean.parseBoolean(datos[6])
                    );
                    lista.add(f);
                }
            }
        } catch (IOException e) {
            System.out.println("Archivo no encontrado.");
        }

        return lista;
    }
}


