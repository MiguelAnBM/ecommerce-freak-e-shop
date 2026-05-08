package com.freakeshop.freak_e_shop.service;

import com.freakeshop.freak_e_shop.dto.PedidoRequestDTO;
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
// El stock ya fue descontado al momento de añadir productos al carrito,
// por lo tanto aquí solo se vacía el carrito sin devolver stock.
// Y guarda el pedido en el repositorio.
@Service
public class PedidoService {

    private final CarritoService carritoService;
    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;

    public PedidoService(CarritoService carritoService, PedidoRepository pedidoRepository,
            ProductoService productoService) {
        this.carritoService = carritoService;
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
    }

    // Confirma un pedido: vacía el carrito de la sesión actual (sin devolver
    // stock).
    // Retorna un mapa con el estado de la operación.
    public Map<String, String> confirmarPedido(PedidoRequestDTO pedidoDTO) {
        // Vaciar el carrito sin devolver stock (ya fue reservado al añadir)
        carritoService.vaciarSinDevolverStock();

        // Persistir el pedido
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(pedidoDTO.getNumeroPedido());
        pedido.setFecha(pedidoDTO.getFecha());
        pedido.setDireccion(pedidoDTO.getDireccion());
        pedido.setTelefono(pedidoDTO.getTelefono());
        pedido.setMetodoPago(pedidoDTO.getMetodoPago());
        pedido.setTotal(pedidoDTO.getTotal());

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

        Map<String, String> resultado = new LinkedHashMap<>();
        resultado.put("estado", "OK");
        resultado.put("numeroPedido", pedidoDTO.getNumeroPedido());
        return resultado;
    }
}
