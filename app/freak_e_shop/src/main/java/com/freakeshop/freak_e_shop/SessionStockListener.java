package com.freakeshop.freak_e_shop;

import com.freakeshop.freak_e_shop.service.CarritoService;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

/*
 * Listener para liberar el stock cuando una sesión expira o es destruida.
 * Esto asegura que si un usuario abandona el carrito, el stock se devuelva
 * automáticamente al inventario.
 */
@Component
public class SessionStockListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            // Intentamos obtener el CarritoService de la sesión.
            // Spring Scoped Beans suelen tener este prefijo en la sesión.
            CarritoService carrito = (CarritoService) se.getSession().getAttribute("scopedTarget.carritoService");

            if (carrito != null) {
                // El método vaciar() de CarritoService ya tiene la lógica
                // de devolver el stock al StockService/StockRepository.
                carrito.vaciar();
            }
        } catch (Exception e) {
            // Loguear error si es necesario, pero no interrumpir la destrucción de la
            // sesión
            System.err.println("Error al liberar stock en sessionDestroyed: " + e.getMessage());
        }
    }
}
