package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.DataDirectoryResolver;
import com.freakeshop.freak_e_shop.model.Mochila;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@Repository
public class MochilaRepository {

    private final DataDirectoryResolver dataResolver;
    private String rutaArchivo;

    public MochilaRepository(DataDirectoryResolver dataResolver) {
        this.dataResolver = dataResolver;
    }

    @PostConstruct
    public void init() {
        this.rutaArchivo = Paths.get(dataResolver.getPath(), "mochilas.txt").toString();
    }

    public void guardar(Mochila mochila) {
        // Limpiamos la descripción de posibles puntos y coma que rompan el split
        String descLimpia = mochila.getDescripcion().replace(";", ",");
        
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo, true), StandardCharsets.UTF_8))) {
            String linea = mochila.getId() + ";" +
                           mochila.getNombre() + ";" +
                           descLimpia + ";" +
                           mochila.getPrecio() + ";" +
                           mochila.getImagen() + ";" +
                           mochila.getTamano() + ";" +
                           mochila.getMaterialFabricacion() + ";" +
                           mochila.isTieneCompartimentoPC() + ";" +
                           mochila.isDestacado();
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar mochila: " + e.getMessage());
        }
    }

    public List<Mochila> obtenerTodos() {
        List<Mochila> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 8) {
                    Mochila m = new Mochila(
                        datos[0], datos[1], datos[2],
                        Double.parseDouble(datos[3]),
                        datos[4], datos[5], datos[6],
                        Boolean.parseBoolean(datos[7])
                    );
                    if (datos.length > 8) m.setDestacado(Boolean.parseBoolean(datos[8]));
                    lista.add(m);
                }
            }
        } catch (IOException e) {
            // Archivo no encontrado — se creará al guardar
        }
        return lista;
    }

    public Mochila buscarPorId(String id) {
        return obtenerTodos().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void eliminar(String id) {
        List<Mochila> lista = obtenerTodos();
        lista.removeIf(m -> m.getId().equals(id));
        reescribir(lista);
    }

    private void reescribir(List<Mochila> lista) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            for (Mochila m : lista) {
                // Limpiamos la descripción por si alguna mochila nueva o editada trae un ";"
                String descLimpia = (m.getDescripcion() != null) ? m.getDescripcion().replace(";", ",") : "";
                
                String linea = m.getId() + ";" + m.getNombre() + ";" +
                               descLimpia + ";" + m.getPrecio() + ";" +
                               m.getImagen() + ";" + m.getTamano() + ";" +
                               m.getMaterialFabricacion() + ";" + m.isTieneCompartimentoPC() + ";" +
                               m.isDestacado();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al reescribir mochilas: " + e.getMessage());
        }
    }

    public void actualizar(Mochila mochila) {
        List<Mochila> lista = obtenerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(mochila.getId())) {
                lista.set(i, mochila);
                break;
            }
        }
        reescribir(lista);
    }
}
