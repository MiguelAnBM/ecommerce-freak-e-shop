package com.freakeshop.freak_e_shop.service;

import com.freakeshop.freak_e_shop.model.Producto;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CarritoService {

    private final ProductoService productoService;
    private final StockService stockService;

    // productoId → cantidad en el carrito
    private final Map<String, Integer> items = new LinkedHashMap<>();

    public CarritoService(ProductoService productoService, StockService stockService) {
        this.productoService = productoService;
        this.stockService = stockService;
    }

    // Añade un producto al carrito y disminuye su stock en 1.
    public boolean agregarProducto(String productoId) {
        Producto p = productoService.obtenerPorId(productoId);
        if (p == null)
            return false;

        boolean stockDisminuido = stockService.disminuirStock(productoId);
        if (!stockDisminuido)
            return false;

        items.merge(productoId, 1, Integer::sum);
        return true;
    }

    // Retorna la lista de ítems del carrito con datos del producto.
    public List<Map<String, Object>> obtenerItems() {
        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Producto p = productoService.obtenerPorId(entry.getKey());
            if (p != null) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("producto", p);
                item.put("cantidad", entry.getValue());
                item.put("subtotal", p.getPrecio() * entry.getValue());
                item.put("categoria", productoService.obtenerCategoria(p));
                item.put("stockDisponible", stockService.obtenerStock(entry.getKey()));
                resultado.add(item);
            }
        }
        return resultado;
    }

    // Elimina un producto del carrito y devuelve su stock.
    public void eliminarProducto(String productoId) {
        Integer cantidad = items.remove(productoId);
        if (cantidad != null) {
            int stockActual = stockService.obtenerStock(productoId);
            stockService.actualizarStock(productoId, stockActual + cantidad);
        }
    }

    // Vacía todo el carrito y devuelve el stock.
    public void vaciar() {
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            int stockActual = stockService.obtenerStock(entry.getKey());
            stockService.actualizarStock(entry.getKey(), stockActual + entry.getValue());
        }
        items.clear();
    }

    // Actualiza la cantidad de un producto y ajusta el stock diferencialmente.
    public Map<String, Object> actualizarCantidad(String productoId, int nuevaCantidad) {
        if (nuevaCantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1");
        }

        Integer cantidadAnterior = items.get(productoId);
        if (cantidadAnterior == null) {
            throw new IllegalArgumentException("El producto no está en el carrito");
        }

        int stockActual = stockService.obtenerStock(productoId);
        int diferencia = nuevaCantidad - cantidadAnterior;

        if (diferencia > 0) {
            // Se quiere aumentar: verificar si hay stock suficiente
            if (stockActual < diferencia) {
                return Map.of("estado", "ERROR", "mensaje", "Stock insuficiente");
            }
            stockService.actualizarStock(productoId, stockActual - diferencia);
        } else if (diferencia < 0) {
            // Se quiere reducir: devolver stock
            stockService.actualizarStock(productoId, stockActual + Math.abs(diferencia));
        }

        items.put(productoId, nuevaCantidad);
        
        Producto p = productoService.obtenerPorId(productoId);
        double nuevoSubtotal = p.getPrecio() * nuevaCantidad;
        
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("estado", "OK");
        respuesta.put("nuevaCantidad", nuevaCantidad);
        respuesta.put("stockRestante", stockService.obtenerStock(productoId));
        respuesta.put("nuevoSubtotal", nuevoSubtotal);
        respuesta.put("nuevoTotal", obtenerTotal());
        return respuesta;
    }

    // Calcula el total del carrito.
    public double obtenerTotal() {
        double total = 0;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Producto p = productoService.obtenerPorId(entry.getKey());
            if (p != null) {
                total += p.getPrecio() * entry.getValue();
            }
        }
        return total;
    }

    public int obtenerCantidadTotal() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int contarItemsValidos() {
        items.keySet().removeIf(id -> productoService.obtenerPorId(id) == null);
        return obtenerCantidadTotal();
    }

    // Vacía el carrito SIN devolver stock (ya fue descontado).
    public void vaciarSinDevolverStock() {
        items.clear();
    }

    public Map<String, Integer> getItemsMap() {
        return new LinkedHashMap<>(items);
    }

    public static void eliminarProductoDeTodasLasSesiones(String productoId) {
        for (jakarta.servlet.http.HttpSession session : com.freakeshop.freak_e_shop.SessionRegistry.getSessions()
                .values()) {
            try {
                CarritoService carrito = (CarritoService) session.getAttribute("scopedTarget.carritoService");
                if (carrito != null) {
                    carrito.items.remove(productoId);
                }
            } catch (Exception e) {
            }
        }
    }
}
