package com.freakeshop.freak_e_shop.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO genérico que envuelve una lista de resultados paginados.
 * Contiene la lista de productos, información de paginación y filtros
 * aplicados.
 * 
 * @param <T> El tipo de objeto contenido en la página (ej. ProductoDTO,
 *            ResenaDTO).
 */
public class PaginaResultadoDTO<T> {
    private List<T> productos;
    private int paginaActual;
    private int totalPaginas;
    private int totalProductos;
    private Map<String, Object> filtrosAplicados;
    private List<Object> elementosPaginacion;

    public PaginaResultadoDTO(List<T> productos, int paginaActual, int totalPaginas, int totalProductos,
            Map<String, Object> filtrosAplicados, List<Object> elementosPaginacion) {
        this.productos = productos;
        this.paginaActual = paginaActual;
        this.totalPaginas = totalPaginas;
        this.totalProductos = totalProductos;
        this.filtrosAplicados = filtrosAplicados;
        this.elementosPaginacion = elementosPaginacion;
    }

    public List<T> getProductos() {
        return productos;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public Map<String, Object> getFiltrosAplicados() {
        return filtrosAplicados;
    }

    public List<Object> getElementosPaginacion() {
        return elementosPaginacion;
    }
}
