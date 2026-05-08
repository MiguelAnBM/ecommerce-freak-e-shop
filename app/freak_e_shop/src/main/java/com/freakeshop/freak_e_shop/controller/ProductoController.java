package com.freakeshop.freak_e_shop.controller;

import com.freakeshop.freak_e_shop.dto.ProductoDTO;
import com.freakeshop.freak_e_shop.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscarProductos(@RequestParam("q") String q) {
        if (q == null || q.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        String queryLower = q.trim().toLowerCase();
        
        // Obtener todos los productos y filtrar por nombre
        List<ProductoDTO> resultados = productoService.obtenerTodos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(queryLower))
                .limit(5) // Limitar a 5 resultados para el autocompletado
                .map(p -> {
                    ProductoDTO dto = new ProductoDTO();
                    dto.setId(p.getId());
                    dto.setNombre(p.getNombre());
                    dto.setPrecio(p.getPrecio());
                    dto.setImagen(p.getImagen());
                    dto.setCategoria(productoService.obtenerCategoria(p));
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultados);
    }
}
