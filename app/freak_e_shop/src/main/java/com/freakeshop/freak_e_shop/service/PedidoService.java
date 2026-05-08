package com.freakeshop.freak_e_shop.service;

import com.freakeshop.freak_e_shop.dto.PedidoRequestDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.freakeshop.freak_e_shop.model.ItemPedido;
import com.freakeshop.freak_e_shop.model.Pedido;
import com.freakeshop.freak_e_shop.repository.PedidoRepository;
import com.freakeshop.freak_e_shop.dto.ItemPedidoDTO;

import java.util.ArrayList;
import java.util.List;

// Servicio para confirmar pedidos.
// El stock ya fue descontado incrementalmente durante la gestión del carrito,
// por lo tanto aquí solo se vacía el carrito y se persiste el pedido.
@Service
public class PedidoService {

    private final CarritoService carritoService;
    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;

    public PedidoService(CarritoService carritoService, PedidoRepository pedidoRepository,
            ProductoService productoService, StockService stockService) {
        this.carritoService = carritoService;
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
    }

    // Confirma un pedido: verifica sesión, vacía el carrito y persiste el pedido.
    // Retorna un mapa con el estado de la operación.
    public Map<String, Object> confirmarPedido(PedidoRequestDTO pedidoDTO, HttpSession session) {
        // Verificar sesión (segunda barrera)
        if (session.getAttribute("adminLogueado") == null && session.getAttribute("usuarioLogueado") == null) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("estado", "ERROR");
            error.put("error", "no_autenticado");
            return error;
        }

        // Vaciar el carrito (el stock ya fue descontado al añadir/modificar items)
        carritoService.vaciarSinDevolverStock();

        // Persistir el pedido
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(pedidoDTO.getNumeroPedido());
        pedido.setFecha(pedidoDTO.getFecha());
        pedido.setDireccion(pedidoDTO.getDireccion());
        pedido.setTelefono(pedidoDTO.getTelefono());
        pedido.setMetodoPago(pedidoDTO.getMetodoPago());
        pedido.setTotal(pedidoDTO.getTotal());
        pedido.setImpuesto(pedidoDTO.getImpuesto());
        pedido.setEnvio(pedidoDTO.getEnvio());

        List<ItemPedido> items = new ArrayList<>();
        if (pedidoDTO.getItems() != null) {
            for (ItemPedidoDTO dtoItem : pedidoDTO.getItems()) {
                String categoria = "Desconocida";
                com.freakeshop.freak_e_shop.model.Producto p = productoService.obtenerPorId(dtoItem.getId());
                if (p != null) {
                    categoria = productoService.obtenerCategoria(p);
                }

                items.add(new ItemPedido(
                        dtoItem.getId(),
                        dtoItem.getNombre(),
                        categoria,
                        dtoItem.getPrecio(),
                        dtoItem.getCantidad()));
            }
        }
        pedido.setItems(items);
        pedidoRepository.guardar(pedido);

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("estado", "OK");
        resultado.put("numeroPedido", pedidoDTO.getNumeroPedido());
        return resultado;
    }
}
