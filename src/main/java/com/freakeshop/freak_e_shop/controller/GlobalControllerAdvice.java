package com.freakeshop.freak_e_shop.controller;

import com.freakeshop.freak_e_shop.service.CarritoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final CarritoService carritoService;

    public GlobalControllerAdvice(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @ModelAttribute("cartCount")
    public int globalCartCount() {
        return carritoService.contarItemsValidos();
    }
}
