package com.freakeshop.freak_e_shop.util;

import com.freakeshop.freak_e_shop.model.Producto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

// Helper para serializar los datos del carrito a JSON,
// para inyectarlos como atributo data-* en el HTML del modal de pago.
@Component("carritoJsonHelper")
public class CarritoJsonHelper {

    // Convierte la lista de ítems del carrito a un JSON string seguro para HTML.
    // Cada ítem se simplifica a: id, nombre, precio, cantidad, subtotal, imagen.
    public String toJson(List<Map<String, Object>> items) {
        if (items == null || items.isEmpty())
            return "[]";

        StringBuilder sb = new StringBuilder("[");
        boolean primero = true;

        for (Map<String, Object> item : items) {
            if (!primero)
                sb.append(",");
            primero = false;

            Producto p = (Producto) item.get("producto");
            int cantidad = (int) item.get("cantidad");
            double subtotal = (double) item.get("subtotal");

            sb.append("{");
            sb.append("\"id\":\"").append(escaparJson(p.getId())).append("\",");
            sb.append("\"nombre\":\"").append(escaparJson(p.getNombre())).append("\",");
            sb.append("\"precio\":").append(p.getPrecio()).append(",");
            sb.append("\"imagen\":\"").append(escaparJson(p.getImagen())).append("\",");
            sb.append("\"cantidad\":").append(cantidad).append(",");
            sb.append("\"subtotal\":").append(subtotal);
            sb.append("}");
        }

        sb.append("]");
        return sb.toString();
    }

    // Caracteres especiales para JSON.
    private String escaparJson(String valor) {
        if (valor == null)
            return "";
        return valor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
