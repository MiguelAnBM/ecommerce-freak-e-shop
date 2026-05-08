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
}
