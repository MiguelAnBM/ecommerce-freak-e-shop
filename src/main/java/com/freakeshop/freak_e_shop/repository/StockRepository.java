package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.DataDirectoryResolver;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@Repository
public class StockRepository {

    private final DataDirectoryResolver dataResolver;
    private String rutaArchivo;

    public StockRepository(DataDirectoryResolver dataResolver) {
        this.dataResolver = dataResolver;
    }

    @PostConstruct
    public void init() {
        this.rutaArchivo = Paths.get(dataResolver.getPath(), "stock.txt").toString();
    }

    public int obtenerStock(String productoId) {
        Map<String, Integer> stockMap = obtenerTodos();
        return stockMap.getOrDefault(productoId, 0);
    }

    public void actualizarStock(String productoId, int cantidad) {
        Map<String, Integer> stockMap = obtenerTodos();
        stockMap.put(productoId, cantidad);
        reescribir(stockMap);
    }

    public boolean disminuirStock(String productoId) {
        return disminuirStock(productoId, 1);
    }

    public boolean disminuirStock(String productoId, int cantidad) {
        Map<String, Integer> stockMap = obtenerTodos();
        int actual = stockMap.getOrDefault(productoId, 0);
        if (actual < cantidad) {
            return false;
        }
        stockMap.put(productoId, actual - cantidad);
        reescribir(stockMap);
        return true;
    }

    public Map<String, Integer> obtenerTodos() {
        Map<String, Integer> stockMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 2) {
                    stockMap.put(datos[0], Integer.parseInt(datos[1]));
                }
            }
        } catch (IOException e) {
            // Archivo no encontrado — se creará al guardar
        } catch (NumberFormatException e) {
            System.err.println("Error de formato en stock.txt: " + e.getMessage());
        }
        return stockMap;
    }

    public void eliminar(String productoId) {
        Map<String, Integer> stockMap = obtenerTodos();
        stockMap.remove(productoId);
        reescribir(stockMap);
    }

    private void reescribir(Map<String, Integer> stockMap) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            for (Map.Entry<String, Integer> entry : stockMap.entrySet()) {
                bw.write(entry.getKey() + ";" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al escribir stock: " + e.getMessage());
        }
    }
}
