package com.freakeshop.freak_e_shop.service;

import com.freakeshop.freak_e_shop.dto.PaginaResultadoDTO;
import com.freakeshop.freak_e_shop.dto.ReporteDTO;
import com.freakeshop.freak_e_shop.model.ItemPedido;
import com.freakeshop.freak_e_shop.model.Pedido;
import com.freakeshop.freak_e_shop.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReporteService {

    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;

    public ReporteService(PedidoRepository pedidoRepository, ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
    }

    public ReporteDTO generarReporte() {
        List<Pedido> pedidos = pedidoRepository.obtenerTodos();
        ReporteDTO reporte = new ReporteDTO();

        reporte.setTotalVentas(pedidos.size());

        double ingresosTotales = 0;
        Map<String, Integer> ventasPorCategoria = new TreeMap<>();
        Map<String, Double> ingresosPorCategoria = new TreeMap<>();

        // Inicializar categorías (para que aparezcan incluso si no tienen ventas)
        for (String cat : productoService.obtenerCategorias()) {
            ventasPorCategoria.put(cat, 0);
            ingresosPorCategoria.put(cat, 0.0);
        }

        Map<String, Integer> unidadesPorProducto = new HashMap<>();
        Map<String, String> categoriaPorProducto = new HashMap<>();

        Pedido ultimoPedido = null;

        for (Pedido pedido : pedidos) {
            ingresosTotales += pedido.getTotal();
            ultimoPedido = pedido;
            // Como se leen en orden, el último en la lista es el más reciente (o al revés,
            // dependerá del archivo. Se insertan al final, así que el último es el más
            // reciente).

            for (ItemPedido item : pedido.getItems()) {
                String cat = item.getCategoria();
                int cant = item.getCantidad();
                double subtotal = item.getPrecio() * cant;

                ventasPorCategoria.put(cat, ventasPorCategoria.getOrDefault(cat, 0) + cant);
                ingresosPorCategoria.put(cat, ingresosPorCategoria.getOrDefault(cat, 0.0) + subtotal);

                String nombreProd = item.getNombreProducto();
                unidadesPorProducto.put(nombreProd, unidadesPorProducto.getOrDefault(nombreProd, 0) + cant);
                categoriaPorProducto.putIfAbsent(nombreProd, cat);
            }
        }

        reporte.setIngresosTotales(ingresosTotales);
        if (pedidos.size() > 0) {
            reporte.setIngresoPromedio(ingresosTotales / pedidos.size());
        } else {
            reporte.setIngresoPromedio(0);
        }

        reporte.setVentasPorCategoria(ventasPorCategoria);
        reporte.setIngresosPorCategoria(ingresosPorCategoria);

        if (!unidadesPorProducto.isEmpty()) {
            Map.Entry<String, Integer> topProduct = Collections.max(unidadesPorProducto.entrySet(),
                    Map.Entry.comparingByValue());
            reporte.setProductoMasVendidoNombre(topProduct.getKey());
            reporte.setProductoMasVendidoCategoria(categoriaPorProducto.get(topProduct.getKey()));
            reporte.setProductoMasVendidoUnidades(topProduct.getValue());
        } else {
            reporte.setProductoMasVendidoNombre("—");
            reporte.setProductoMasVendidoCategoria("");
            reporte.setProductoMasVendidoUnidades(0);
        }

        if (ultimoPedido != null) {
            reporte.setUltimoPedidoNumero(ultimoPedido.getNumeroPedido());
            reporte.setUltimoPedidoFecha(ultimoPedido.getFecha());
            reporte.setUltimoPedidoTotal(ultimoPedido.getTotal());
        } else {
            reporte.setUltimoPedidoNumero("—");
            reporte.setUltimoPedidoFecha("—");
            reporte.setUltimoPedidoTotal(0);
        }

        return reporte;
    }

    public List<Pedido> obtenerHistorialCompleto() {
        List<Pedido> pedidos = pedidoRepository.obtenerTodos();
        Collections.reverse(pedidos);
        return pedidos;
    }

    public PaginaResultadoDTO<Pedido> obtenerHistorialPaginado(int pagina) {
        List<Pedido> pedidos = pedidoRepository.obtenerTodos();
        // Invertir para mostrar del más reciente al más antiguo
        Collections.reverse(pedidos);

        int totalProductos = pedidos.size();
        int productosPorPagina = 10;
        int totalPaginas = (int) Math.ceil((double) totalProductos / productosPorPagina);

        if (totalPaginas == 0)
            totalPaginas = 1;
        if (pagina < 1)
            pagina = 1;
        if (pagina > totalPaginas)
            pagina = totalPaginas;

        int fromIndex = (pagina - 1) * productosPorPagina;
        int toIndex = Math.min(fromIndex + productosPorPagina, totalProductos);
        List<Pedido> pedidosPagina = pedidos.subList(fromIndex, toIndex);

        List<Object> elementosPaginacion = new ArrayList<>();
        if (totalPaginas <= 7) {
            for (int i = 1; i <= totalPaginas; i++)
                elementosPaginacion.add(i);
        } else {
            elementosPaginacion.add(1);
            if (pagina > 3)
                elementosPaginacion.add("...");
            int start = Math.max(2, pagina - 1);
            int end = Math.min(totalPaginas - 1, pagina + 1);
            for (int i = start; i <= end; i++)
                elementosPaginacion.add(i);
            if (pagina < totalPaginas - 2)
                elementosPaginacion.add("...");
            elementosPaginacion.add(totalPaginas);
        }

        return new PaginaResultadoDTO<>(pedidosPagina, pagina, totalPaginas, totalProductos, Collections.emptyMap(),
                elementosPaginacion);
    }
}
