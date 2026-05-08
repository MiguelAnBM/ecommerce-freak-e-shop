package com.freakeshop.freak_e_shop.util;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

@Component("precioUtils")
public class PrecioUtils {
    // Formatea precios a pesos colombianos.
    public String formatearCOP(double precio) {
        NumberFormat formato = NumberFormat.getNumberInstance(new Locale("es", "CO"));
        formato.setMaximumFractionDigits(0);
        formato.setMinimumFractionDigits(0);
        return "$ " + formato.format((long) precio);
    }
}
