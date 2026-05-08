package com.freakeshop.freak_e_shop.controller;

import com.freakeshop.freak_e_shop.service.ProductoService;
import com.freakeshop.freak_e_shop.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.freakeshop.freak_e_shop.dto.ProductoDTO;

@Controller
public class AdminController {

    private final ProductoService productoService;
    private final StockService stockService;

    public AdminController(ProductoService productoService, StockService stockService) {
        this.productoService = productoService;
        this.stockService = stockService;
    }

    @PostMapping("/admin/eliminar")
    public String eliminarProducto(@RequestParam String productoId, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(productoId);
            com.freakeshop.freak_e_shop.service.CarritoService.eliminarProductoDeTodasLasSesiones(productoId);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/productos/crear")
    public String crearProducto(@ModelAttribute ProductoDTO productoDTO) {
        productoService.crearProducto(productoDTO);
        return "redirect:/admin";
    }

    @PostMapping("/admin/productos/modificar")
    public String modificarProducto(@ModelAttribute ProductoDTO productoDTO) {
        productoService.modificarProducto(productoDTO);
        return "redirect:/admin";
    }

    @PostMapping("/admin/stock")
    public String actualizarStock(@RequestParam String productoId,
                                  @RequestParam int cantidad) {
        stockService.actualizarStock(productoId, cantidad);
        return "redirect:/admin";
    }
}
