package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.DataDirectoryResolver;
import com.freakeshop.freak_e_shop.model.Accesorio;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@Repository
public class AccesorioRepository {

    private final DataDirectoryResolver dataResolver;
    private String rutaArchivo;

    public AccesorioRepository(DataDirectoryResolver dataResolver) {
        this.dataResolver = dataResolver;
    }

    @PostConstruct
    public void init() {
        this.rutaArchivo = Paths.get(dataResolver.getPath(), "accesorios.txt").toString();
    }

    public void guardar(Accesorio accesorio) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo, true), StandardCharsets.UTF_8))) {
            String linea = accesorio.getId() + ";" +
                           accesorio.getNombre() + ";" +
                           accesorio.getDescripcion() + ";" +
                           accesorio.getPrecio() + ";" +
                           accesorio.getImagen() + ";" +
                           accesorio.getTipo() + ";" +
                           accesorio.isDestacado();
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar accesorio: " + e.getMessage());
        }
    }

    public List<Accesorio> obtenerTodos() {
        List<Accesorio> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 6) {
                    Accesorio a = new Accesorio(
                        datos[0], datos[1], datos[2],
                        Double.parseDouble(datos[3]),
                        datos[4], datos[5]
                    );
                    if (datos.length > 6) a.setDestacado(Boolean.parseBoolean(datos[6]));
                    lista.add(a);
                }
            }
        } catch (IOException e) {
            // Archivo no encontrado — se creará al guardar
        }
        return lista;
    }
    public Accesorio buscarPorId(String id) {
        return obtenerTodos().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void eliminar(String id) {
        List<Accesorio> lista = obtenerTodos();
        lista.removeIf(a -> a.getId().equals(id));
        reescribir(lista);
    }

    private void reescribir(List<Accesorio> lista) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            for (Accesorio a : lista) {
                String linea = a.getId() + ";" + a.getNombre() + ";" +
                               a.getDescripcion() + ";" + a.getPrecio() + ";" +
                               a.getImagen() + ";" + a.getTipo() + ";" + a.isDestacado();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al reescribir accesorios: " + e.getMessage());
        }
    }

    public void actualizar(Accesorio accesorio) {
        List<Accesorio> lista = obtenerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(accesorio.getId())) {
                lista.set(i, accesorio);
                break;
            }
        }
        reescribir(lista);
    }
}
