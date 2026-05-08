package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.DataDirectoryResolver;
import com.freakeshop.freak_e_shop.model.Peluche;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@Repository
public class PelucheRepository {

    private final DataDirectoryResolver dataResolver;
    private String rutaArchivo;

    public PelucheRepository(DataDirectoryResolver dataResolver) {
        this.dataResolver = dataResolver;
    }

    @PostConstruct
    public void init() {
        this.rutaArchivo = Paths.get(dataResolver.getPath(), "peluches.txt").toString();
    }

    public void guardar(Peluche peluche) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo, true), StandardCharsets.UTF_8))) {
            String linea = peluche.getId() + ";" +
                           peluche.getNombre() + ";" +
                           peluche.getDescripcion() + ";" +
                           peluche.getPrecio() + ";" +
                           peluche.getImagen() + ";" +
                           peluche.getMaterial() + ";" +
                           peluche.getTamano() + ";" +
                           peluche.isDestacado();
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar peluche: " + e.getMessage());
        }
    }

    public List<Peluche> obtenerTodos() {
        List<Peluche> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 7) {
                    Peluche p = new Peluche(
                        datos[0], datos[1], datos[2],
                        Double.parseDouble(datos[3]),
                        datos[4], datos[5], datos[6]
                    );
                    if (datos.length > 7) p.setDestacado(Boolean.parseBoolean(datos[7]));
                    lista.add(p);
                }
            }
        } catch (IOException e) {
            // Archivo no encontrado — se creará al guardar
        }
        return lista;
    }

    public Peluche buscarPorId(String id) {
        return obtenerTodos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void eliminar(String id) {
        List<Peluche> lista = obtenerTodos();
        lista.removeIf(p -> p.getId().equals(id));
        reescribir(lista);
    }

    private void reescribir(List<Peluche> lista) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            for (Peluche p : lista) {
                String linea = p.getId() + ";" + p.getNombre() + ";" +
                               p.getDescripcion() + ";" + p.getPrecio() + ";" +
                               p.getImagen() + ";" + p.getMaterial() + ";" +
                               p.getTamano() + ";" + p.isDestacado();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al reescribir peluches: " + e.getMessage());
        }
    }

    public void actualizar(Peluche peluche) {
        List<Peluche> lista = obtenerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(peluche.getId())) {
                lista.set(i, peluche);
                break;
            }
        }
        reescribir(lista);
    }
}
