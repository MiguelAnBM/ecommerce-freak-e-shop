package com.freakeshop.freak_e_shop.controller;

import com.freakeshop.freak_e_shop.dto.PedidoRequestDTO;
import com.freakeshop.freak_e_shop.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Controlador REST para la confirmación de pedidos.

@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Confirma un pedido: vacía el carrito y retorna estado de éxito.

    @PostMapping("/pedido/confirmar")
    public ResponseEntity<?> confirmarPedido(
            @RequestBody PedidoRequestDTO pedido, jakarta.servlet.http.HttpSession session) {
        try {
            Map<String, Object> resultado = pedidoService.confirmarPedido(pedido, session);
            
            if ("ERROR".equals(resultado.get("estado")) && "no_autenticado".equals(resultado.get("error"))) {
                return ResponseEntity.status(401).body(resultado);
            }
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            Map<String, String> error = Map.of(
                    "estado", "ERROR",
                    "mensaje", "Error al confirmar el pedido: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
