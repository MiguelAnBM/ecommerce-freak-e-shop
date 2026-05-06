/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.model.Accesorio;
import java.io.*;
import java.util.*;

public class AccesorioRepository {

    private final String RUTA_ARCHIVO = "accesorios.txt";

    public void guardar(Accesorio accesorio) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {

            String linea = accesorio.getId() + ";" +
                           accesorio.getNombre() + ";" +
                           accesorio.getDescripcion() + ";" +
                           accesorio.getPrecio() + ";" +
                           accesorio.getImagen() + ";" +
                           accesorio.getTipo();

            bw.write(linea);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error al guardar accesorio: " + e.getMessage());
        }
    }

    public List<Accesorio> obtenerTodos() {
        List<Accesorio> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");

                if (datos.length == 6) {
                    Accesorio a = new Accesorio(
                        datos[0],
                        datos[1],
                        datos[2],
                        Double.parseDouble(datos[3]),
                        datos[4],
                        datos[5]
                    );
                    lista.add(a);
                }
            }
        } catch (IOException e) {
            System.out.println("Archivo no encontrado.");
        }

        return lista;
    }
}