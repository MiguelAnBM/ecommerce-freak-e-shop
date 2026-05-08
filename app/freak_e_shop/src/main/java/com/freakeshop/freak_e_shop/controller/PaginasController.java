package com.freakeshop.freak_e_shop.controller;

import com.freakeshop.freak_e_shop.model.Producto;
import com.freakeshop.freak_e_shop.service.CarritoService;
import com.freakeshop.freak_e_shop.service.ProductoService;
import com.freakeshop.freak_e_shop.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// Controlador principal para la navegación entre páginas del sitio.
@Controller
public class PaginasController {

    private final ProductoService productoService;
    private final StockService stockService;
    private final CarritoService carritoService;

    @org.springframework.beans.factory.annotation.Value("${app.config.iva}")
    private double ivaPorcentaje;

    @org.springframework.beans.factory.annotation.Value("${app.config.envio}")
    private double costoEnvio;

    public PaginasController(ProductoService productoService,
                             StockService stockService,
                             CarritoService carritoService) {
        this.productoService = productoService;
        this.stockService = stockService;
        this.carritoService = carritoService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("productosDestacados", productoService.getProductosDestacadosFijos());
        model.addAttribute("categorias", productoService.obtenerCategorias());
        model.addAttribute("stockMap", stockService.obtenerTodos());
        return "index";
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false, name = "categoria") java.util.List<String> categorias,
                           @RequestParam(required = false) Double precioMin,
                           @RequestParam(required = false) Double precioMax,
                           @RequestParam(required = false) String orden,
                           @RequestParam(required = false) Boolean filtrado,
                           @RequestParam(required = false) String q,
                           @RequestParam(defaultValue = "1") int pagina,
                           Model model) {
        
        java.util.List<String> allCats = productoService.obtenerCategorias();
        
        // Si no hay categorías Y no se ha enviado el flag de filtrado, es la primera carga: seleccionamos todas
        if (categorias == null && filtrado == null) {
            categorias = new java.util.ArrayList<>(allCats);
        } else if (categorias == null) {
            // Si el flag de filtrado existe pero no hay categorías, el usuario desmarcó todo deliberadamente
            categorias = new java.util.ArrayList<>();
        }

        com.freakeshop.freak_e_shop.dto.PaginaResultadoDTO<com.freakeshop.freak_e_shop.dto.ProductoDTO> paginaDTO = 
            productoService.obtenerPaginaCatalogo(categorias, precioMin, precioMax, orden, pagina, q);
            
        model.addAttribute("pagina", paginaDTO);
        model.addAttribute("categorias", allCats);
        model.addAttribute("categoriasActivas", categorias);
        
        // Agregar límites globales de precio
        model.addAttribute("precioMinGlobal", productoService.obtenerPrecioMinimoGlobal());
        model.addAttribute("precioMaxGlobal", productoService.obtenerPrecioMaximoGlobal());
        
        model.addAttribute("stockMap", stockService.obtenerTodos());
        return "catalog";
    }

    @GetMapping("/cart")
    public String carrito(Model model) {
        model.addAttribute("items", carritoService.obtenerItems());
        model.addAttribute("total", carritoService.obtenerTotal());
        model.addAttribute("ivaPorcentaje", ivaPorcentaje);
        model.addAttribute("costoEnvio", costoEnvio);
        return "cart";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String returnUrl, Model model) {
        model.addAttribute("returnUrl", returnUrl);
        return "login";
    }

    @GetMapping("/admin")
    public String admin(jakarta.servlet.http.HttpSession session, Model model) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/";
        }
        model.addAttribute("productos", productoService.obtenerTodosComoDTO());
        model.addAttribute("categorias", productoService.obtenerCategorias());
        model.addAttribute("stockMap", stockService.obtenerTodos());
        return "admin";
    }

    @GetMapping("/producto/{id}")
    public String producto(@PathVariable String id, Model model) {
        Producto producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return "redirect:/catalogo";
        }
        model.addAttribute("producto", producto);
        model.addAttribute("categoria", productoService.obtenerCategoria(producto));
        model.addAttribute("stock", stockService.obtenerStock(id));
        return "producto";
    }
}
