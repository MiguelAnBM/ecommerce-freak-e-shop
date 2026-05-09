package com.freakeshop.freak_e_shop.dto;

import java.util.Map;

public class ReporteDTO {
    private int totalVentas;
    private double ingresosTotales;
    private double ingresoPromedio;
    private Map<String, Integer> ventasPorCategoria;
    private Map<String, Double> ingresosPorCategoria;

    private String productoMasVendidoNombre;
    private String productoMasVendidoCategoria;
    private int productoMasVendidoUnidades;

    private String ultimoPedidoNumero;
    private String ultimoPedidoFecha;
    private double ultimoPedidoTotal;

    public ReporteDTO() {
    }

    public int getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(int totalVentas) {
        this.totalVentas = totalVentas;
    }

    public double getIngresosTotales() {
        return ingresosTotales;
    }

    public void setIngresosTotales(double ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public double getIngresoPromedio() {
        return ingresoPromedio;
    }

    public void setIngresoPromedio(double ingresoPromedio) {
        this.ingresoPromedio = ingresoPromedio;
    }

    public Map<String, Integer> getVentasPorCategoria() {
        return ventasPorCategoria;
    }

    public void setVentasPorCategoria(Map<String, Integer> ventasPorCategoria) {
        this.ventasPorCategoria = ventasPorCategoria;
    }

    public Map<String, Double> getIngresosPorCategoria() {
        return ingresosPorCategoria;
    }

    public void setIngresosPorCategoria(Map<String, Double> ingresosPorCategoria) {
        this.ingresosPorCategoria = ingresosPorCategoria;
    }

    public String getProductoMasVendidoNombre() {
        return productoMasVendidoNombre;
    }

    public void setProductoMasVendidoNombre(String productoMasVendidoNombre) {
        this.productoMasVendidoNombre = productoMasVendidoNombre;
    }

    public String getProductoMasVendidoCategoria() {
        return productoMasVendidoCategoria;
    }

    public void setProductoMasVendidoCategoria(String productoMasVendidoCategoria) {
        this.productoMasVendidoCategoria = productoMasVendidoCategoria;
    }

    public int getProductoMasVendidoUnidades() {
        return productoMasVendidoUnidades;
    }

    public void setProductoMasVendidoUnidades(int productoMasVendidoUnidades) {
        this.productoMasVendidoUnidades = productoMasVendidoUnidades;
    }

    public String getUltimoPedidoNumero() {
        return ultimoPedidoNumero;
    }

    public void setUltimoPedidoNumero(String ultimoPedidoNumero) {
        this.ultimoPedidoNumero = ultimoPedidoNumero;
    }

    public String getUltimoPedidoFecha() {
        return ultimoPedidoFecha;
    }

    public void setUltimoPedidoFecha(String ultimoPedidoFecha) {
        this.ultimoPedidoFecha = ultimoPedidoFecha;
    }

    public double getUltimoPedidoTotal() {
        return ultimoPedidoTotal;
    }

    public void setUltimoPedidoTotal(double ultimoPedidoTotal) {
        this.ultimoPedidoTotal = ultimoPedidoTotal;
    }
}
