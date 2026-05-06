/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.model.Camisa;
import java.io.*;
import java.util.*;

public class CamisaRepository {

    private final String RUTA_ARCHIVO = "camisas.txt";

    public void guardar(Camisa camisa) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {

            String linea = camisa.getId() + ";" +
                           camisa.getNombre() + ";" +
                           camisa.getDescripcion() + ";" +
                           camisa.getPrecio() + ";" +
                           camisa.getImagen() + ";" +
                           camisa.getTalla() + ";" +
                           camisa.getMaterial();

            bw.write(linea);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error al guardar camisa: " + e.getMessage());
        }
    }

    public List<Camisa> obtenerTodos() {
        List<Camisa> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");

                if (datos.length == 7) {
                    Camisa c = new Camisa(
                        datos[0],
                        datos[1],
                        datos[2],
                        Double.parseDouble(datos[3]),
                        datos[4],
                        datos[5],
                        datos[6]
                    );

                    lista.add(c);
                }
            }

        } catch (IOException e) {
            System.out.println("Archivo no encontrado.");
        }

        return lista;
    }
}
