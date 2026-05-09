package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.DataDirectoryResolver;
import com.freakeshop.freak_e_shop.model.Camisa;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@Repository
public class CamisaRepository {

    private final DataDirectoryResolver dataResolver;
    private String rutaArchivo;

    public CamisaRepository(DataDirectoryResolver dataResolver) {
        this.dataResolver = dataResolver;
    }

    @PostConstruct
    public void init() {
        this.rutaArchivo = Paths.get(dataResolver.getPath(), "camisas.txt").toString();
    }

    public void guardar(Camisa camisa) {
        // Limpiamos la descripción de posibles puntos y coma que rompan el split
        String descLimpia = camisa.getDescripcion().replace(";", ",");
        
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo, true), StandardCharsets.UTF_8))) {
            String linea = camisa.getId() + ";" +
                           camisa.getNombre() + ";" +
                           descLimpia + ";" +
                           camisa.getPrecio() + ";" +
                           camisa.getImagen() + ";" +
                           camisa.getTalla() + ";" +
                           camisa.getMaterial() + ";" +
                           camisa.isDestacado();
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar camisa: " + e.getMessage());
        }
    }

    public List<Camisa> obtenerTodos() {
        List<Camisa> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 7) {
                    Camisa c = new Camisa(
                        datos[0], datos[1], datos[2],
                        Double.parseDouble(datos[3]),
                        datos[4], datos[5], datos[6]
                    );
                    if (datos.length > 7) c.setDestacado(Boolean.parseBoolean(datos[7]));
                    lista.add(c);
                }
            }
        } catch (IOException e) {
            // Archivo no encontrado — se creará al guardar
        }
        return lista;
    }

    public Camisa buscarPorId(String id) {
        return obtenerTodos().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void eliminar(String id) {
        List<Camisa> lista = obtenerTodos();
        lista.removeIf(c -> c.getId().equals(id));
        reescribir(lista);
    }

    private void reescribir(List<Camisa> lista) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            for (Camisa c : lista) {
                // Limpiamos la descripción por si alguna camisa nueva o editada trae un ";"
                String descLimpia = (c.getDescripcion() != null) ? c.getDescripcion().replace(";", ",") : "";
                
                String linea = c.getId() + ";" + c.getNombre() + ";" +
                               descLimpia + ";" + c.getPrecio() + ";" +
                               c.getImagen() + ";" + c.getTalla() + ";" +
                               c.getMaterial() + ";" + c.isDestacado();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al reescribir camisas: " + e.getMessage());
        }
    }

    public void actualizar(Camisa camisa) {
        List<Camisa> lista = obtenerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(camisa.getId())) {
                lista.set(i, camisa);
                break;
            }
        }
        reescribir(lista);
    }
}
