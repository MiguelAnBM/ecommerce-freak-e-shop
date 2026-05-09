package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.DataDirectoryResolver;
import com.freakeshop.freak_e_shop.model.FiguraColeccionable;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@Repository
public class FiguraColeccionableRepository {

    private final DataDirectoryResolver dataResolver;
    private String rutaArchivo;

    public FiguraColeccionableRepository(DataDirectoryResolver dataResolver) {
        this.dataResolver = dataResolver;
    }

    @PostConstruct
    public void init() {
        this.rutaArchivo = Paths.get(dataResolver.getPath(), "figuras.txt").toString();
    }

    public void guardar(FiguraColeccionable figura) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo, true), StandardCharsets.UTF_8))) {
            String linea = figura.getId() + ";" +
                           figura.getNombre() + ";" +
                           figura.getDescripcion() + ";" +
                           figura.getPrecio() + ";" +
                           figura.getImagen() + ";" +
                           figura.getFranquicia() + ";" +
                           figura.isEdicionLimitada() + ";" +
                           figura.isDestacado();
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar figura: " + e.getMessage());
        }
    }

    public List<FiguraColeccionable> obtenerTodos() {
        List<FiguraColeccionable> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 7) {
                    FiguraColeccionable f = new FiguraColeccionable(
                        datos[0], datos[1], datos[2],
                        Double.parseDouble(datos[3]),
                        datos[4], datos[5],
                        Boolean.parseBoolean(datos[6])
                    );
                    if (datos.length > 7) f.setDestacado(Boolean.parseBoolean(datos[7]));
                    lista.add(f);
                }
            }
        } catch (IOException e) {
            // Archivo no encontrado — se creará al guardar
        }
        return lista;
    }

    public FiguraColeccionable buscarPorId(String id) {
        return obtenerTodos().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void eliminar(String id) {
        List<FiguraColeccionable> lista = obtenerTodos();
        lista.removeIf(f -> f.getId().equals(id));
        reescribir(lista);
    }

    private void reescribir(List<FiguraColeccionable> lista) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            for (FiguraColeccionable f : lista) {
                String linea = f.getId() + ";" + f.getNombre() + ";" +
                               f.getDescripcion() + ";" + f.getPrecio() + ";" +
                               f.getImagen() + ";" + f.getFranquicia() + ";" +
                               f.isEdicionLimitada() + ";" + f.isDestacado();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al reescribir figuras: " + e.getMessage());
        }
    }

    public void actualizar(FiguraColeccionable figura) {
        List<FiguraColeccionable> lista = obtenerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(figura.getId())) {
                lista.set(i, figura);
                break;
            }
        }
        reescribir(lista);
    }
}
