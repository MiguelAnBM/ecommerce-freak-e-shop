package com.freakeshop.freak_e_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Controlador principal para la navegación entre páginas estáticas del sitio.
@Controller
public class PaginasController {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/catalog")
    public String catalogo() {
        return "catalog";
    }

    @GetMapping("/cart")
    public String carrito() {
        return "cart";
    }
}
