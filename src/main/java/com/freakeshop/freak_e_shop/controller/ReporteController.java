package com.freakeshop.freak_e_shop.controller;

import com.freakeshop.freak_e_shop.service.ReporteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/admin/reportes")
    public String verReportes(@RequestParam(defaultValue = "1") int paginaReporte, HttpSession session, Model model) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/";
        }

        model.addAttribute("reporte", reporteService.generarReporte());
        model.addAttribute("pedidosCompletos", reporteService.obtenerHistorialCompleto());

        return "admin/reportes";
    }
}
