package com.freakeshop.freak_e_shop;

import com.freakeshop.freak_e_shop.model.Producto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

// Configuración centralizada de prefijos de ID por categoría.
// Genera IDs lineales con formato: [Prefijo][Número de 4 dígitos con ceros a la izquierda]
// Ejemplos: A0001 (Accesorio), M0001 (Mochila), CO001 (Comic), etc.
@Component
public class CategoriaIdConfig {

    // Mapa de prefijos por categoría.
    // Se usa "CO" para Comic y "CA" para Camisa para evitar conflictos
    private static final Map<String, String> PREFIJOS = Map.of(
            "Accesorio", "A",
            "Mochila", "M",
            "Comic", "CO",
            "Camisa", "CA",
            "FiguraColeccionable", "F",
            "Peluche", "P");

    // Obtiene el prefijo asignado a una categoría.
    public String obtenerPrefijo(String categoria) {
        String prefijo = PREFIJOS.get(categoria);
        if (prefijo == null) {
            throw new IllegalArgumentException("Categoría desconocida: " + categoria);
        }
        return prefijo;
    }

    // Genera un nuevo ID para un producto de la categoría indicada,
    // basándose en la lista actual de productos de esa categoría.
    public String generarId(String categoria, List<? extends Producto> existentes) {
        String prefijo = obtenerPrefijo(categoria);
        int maxNumero = 0;

        for (Producto p : existentes) {
            String id = p.getId();
            if (id != null && id.startsWith(prefijo)) {
                try {
                    // Extrae la parte numérica después del prefijo
                    String parteNumerica = id.substring(prefijo.length());
                    int numero = Integer.parseInt(parteNumerica);
                    if (numero > maxNumero) {
                        maxNumero = numero;
                    }
                } catch (NumberFormatException e) {
                    // Ignora IDs con formato inválido
                }
            }
        }

        int nuevoNumero = maxNumero + 1;
        return String.format("%s%04d", prefijo, nuevoNumero);
    }

    // Retorna el mapa completo de prefijos (solo lectura).
    public Map<String, String> obtenerPrefijos() {
        return PREFIJOS;
    }
}
