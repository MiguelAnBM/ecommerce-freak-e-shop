package com.freakeshop.freak_e_shop.controller;

import com.freakeshop.freak_e_shop.service.CarritoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping("/carrito/añadir")
    public String agregarAlCarrito(@RequestParam String productoId) {
        carritoService.agregarProducto(productoId);
        return "redirect:/cart";
    }

    @PostMapping("/carrito/eliminar")
    public String eliminarDelCarrito(@RequestParam String productoId) {
        carritoService.eliminarProducto(productoId);
        return "redirect:/cart";
    }

    @PostMapping("/carrito/vaciar")
    public String vaciarCarrito() {
        carritoService.vaciar();
        return "redirect:/cart";
    }

    @org.springframework.web.bind.annotation.PostMapping("/carrito/actualizar")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<?> actualizarCantidad(@org.springframework.web.bind.annotation.RequestBody java.util.Map<String, Object> payload) {
        try {
            String productoId = (String) payload.get("productoId");
            int nuevaCantidad = (int) payload.get("nuevaCantidad");
            java.util.Map<String, Object> resultado = carritoService.actualizarCantidad(productoId, nuevaCantidad);
            return org.springframework.http.ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest().body(java.util.Map.of("estado", "ERROR", "mensaje", e.getMessage()));
        }
    }
}
