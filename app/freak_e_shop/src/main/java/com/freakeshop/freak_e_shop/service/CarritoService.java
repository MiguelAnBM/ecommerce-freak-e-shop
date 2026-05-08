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
                resultado.add(item);
            }
        }
        return resultado;
    }

    // Elimina un producto del carrito (devuelve stock).
    public void eliminarProducto(String productoId) {
        Integer cantidad = items.remove(productoId);
        if (cantidad != null) {
            // Devolver stock
            int stockActual = stockService.obtenerStock(productoId);
            stockService.actualizarStock(productoId, stockActual + cantidad);
        }
    }

    // Vacía todo el carrito y devuelve stock.
    public void vaciar() {
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            int stockActual = stockService.obtenerStock(entry.getKey());
            stockService.actualizarStock(entry.getKey(), stockActual + entry.getValue());
        }
        items.clear();
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

    // Retorna la cantidad total de artículos en el carrito.
    public int obtenerCantidadTotal() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Cuenta los ítems que todavía existen válidamente en la tienda.
    public int contarItemsValidos() {
        items.keySet().removeIf(id -> productoService.obtenerPorId(id) == null);
        return obtenerCantidadTotal();
    }

    // Vacía el carrito SIN devolver stock.
    // Se usa al confirmar un pedido, ya que el stock fue reservado al añadir.
    public void vaciarSinDevolverStock() {
        items.clear();
    }

    // Elimina el producto de los carritos de todas las sesiones activas (sin
    // devolver stock).
    public static void eliminarProductoDeTodasLasSesiones(String productoId) {
        for (jakarta.servlet.http.HttpSession session : com.freakeshop.freak_e_shop.SessionRegistry.getSessions()
                .values()) {
            try {
                CarritoService carrito = (CarritoService) session.getAttribute("scopedTarget.carritoService");
                if (carrito != null) {
                    carrito.items.remove(productoId);
                }
            } catch (Exception e) {
                // Ignora las sesiones invalidadas
            }
        }
    }
}
