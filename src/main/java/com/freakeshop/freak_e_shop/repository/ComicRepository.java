package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.DataDirectoryResolver;
import com.freakeshop.freak_e_shop.model.Comic;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@Repository
public class ComicRepository {

    private final DataDirectoryResolver dataResolver;
    private String rutaArchivo;

    public ComicRepository(DataDirectoryResolver dataResolver) {
        this.dataResolver = dataResolver;
    }

    @PostConstruct
    public void init() {
        this.rutaArchivo = Paths.get(dataResolver.getPath(), "comics.txt").toString();
    }

    public void guardar(Comic comic) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo, true), StandardCharsets.UTF_8))) {
            String linea = comic.getId() + ";" +
                           comic.getNombre() + ";" +
                           comic.getDescripcion() + ";" +
                           comic.getPrecio() + ";" +
                           comic.getImagen() + ";" +
                           comic.getEditorial() + ";" +
                           comic.getNumeroVolumen() + ";" +
                           comic.getIdioma() + ";" +
                           comic.isDestacado();
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar comic: " + e.getMessage());
        }
    }

    public List<Comic> obtenerTodos() {
        List<Comic> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 8) {
                    Comic c = new Comic(
                        datos[0], datos[1], datos[2],
                        Double.parseDouble(datos[3]),
                        datos[4], datos[5],
                        Integer.parseInt(datos[6]), datos[7]
                    );
                    if (datos.length > 8) c.setDestacado(Boolean.parseBoolean(datos[8]));
                    lista.add(c);
                }
            }
        } catch (IOException e) {
            // Archivo no encontrado — se creará al guardar
        }
        return lista;
    }

    public Comic buscarPorId(String id) {
        return obtenerTodos().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void eliminar(String id) {
        List<Comic> lista = obtenerTodos();
        lista.removeIf(c -> c.getId().equals(id));
        reescribir(lista);
    }

    private void reescribir(List<Comic> lista) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            for (Comic c : lista) {
                String linea = c.getId() + ";" + c.getNombre() + ";" +
                               c.getDescripcion() + ";" + c.getPrecio() + ";" +
                               c.getImagen() + ";" + c.getEditorial() + ";" +
                               c.getNumeroVolumen() + ";" + c.getIdioma() + ";" +
                               c.isDestacado();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al reescribir comics: " + e.getMessage());
        }
    }

    public void actualizar(Comic comic) {
        List<Comic> lista = obtenerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(comic.getId())) {
                lista.set(i, comic);
                break;
            }
        }
        reescribir(lista);
    }
}
