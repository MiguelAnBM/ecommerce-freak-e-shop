package com.freakeshop.freak_e_shop.service;

import com.freakeshop.freak_e_shop.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StockService {

    private final StockRepository stockRepo;

    public StockService(StockRepository stockRepo) {
        this.stockRepo = stockRepo;
    }

    public int obtenerStock(String productoId) {
        return stockRepo.obtenerStock(productoId);
    }

    public void actualizarStock(String productoId, int cantidad) {
        stockRepo.actualizarStock(productoId, cantidad);
    }

    public boolean disminuirStock(String productoId) {
        return stockRepo.disminuirStock(productoId);
    }

    public Map<String, Integer> obtenerTodos() {
        return stockRepo.obtenerTodos();
    }
}
