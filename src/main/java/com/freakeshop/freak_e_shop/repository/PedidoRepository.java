package com.freakeshop.freak_e_shop.repository;

import com.freakeshop.freak_e_shop.model.ItemPedido;
import com.freakeshop.freak_e_shop.model.Pedido;
import com.freakeshop.freak_e_shop.DataDirectoryResolver;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PedidoRepository {

    private final Path filePath;

    public PedidoRepository(DataDirectoryResolver directoryResolver) {
        this.filePath = Paths.get(directoryResolver.getPath()).resolve("pedidos.txt");
    }

    public synchronized void guardar(Pedido pedido) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(serializarPedido(pedido));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<Pedido> obtenerTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return pedidos;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Pedido pedido = deserializarPedido(line);
                if (pedido != null) {
                    pedidos.add(pedido);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    private String serializarPedido(Pedido p) {
        StringBuilder sb = new StringBuilder();
        sb.append(p.getNumeroPedido()).append("|")
          .append(p.getFecha()).append("|")
          .append(p.getDireccion()).append("|")
          .append(p.getTelefono()).append("|")
          .append(p.getMetodoPago()).append("|")
          .append(p.getTotal()).append("|");

        List<ItemPedido> items = p.getItems();
        for (int i = 0; i < items.size(); i++) {
            ItemPedido item = items.get(i);
            sb.append(item.getIdProducto()).append(":")
              .append(item.getNombreProducto()).append(":")
              .append(item.getCategoria()).append(":")
              .append(item.getPrecio()).append(":")
              .append(item.getCantidad());
            if (i < items.size() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    private Pedido deserializarPedido(String line) {
        try {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 7) return null;

            Pedido p = new Pedido();
            p.setNumeroPedido(parts[0]);
            p.setFecha(parts[1]);
            p.setDireccion(parts[2]);
            p.setTelefono(parts[3]);
            p.setMetodoPago(parts[4]);
            p.setTotal(Double.parseDouble(parts[5]));

            List<ItemPedido> items = new ArrayList<>();
            String itemsStr = parts[6];
            if (!itemsStr.isEmpty()) {
                String[] itemsParts = itemsStr.split(";");
                for (String itemStr : itemsParts) {
                    String[] itemData = itemStr.split(":", 5); // limitar a 5 para permitir nombres con : ? Mejor limitarlo
                    if (itemData.length >= 5) {
                        ItemPedido item = new ItemPedido(
                            itemData[0],
                            itemData[1],
                            itemData[2],
                            Double.parseDouble(itemData[3]),
                            Integer.parseInt(itemData[4])
                        );
                        items.add(item);
                    }
                }
            }
            p.setItems(items);
            return p;
        } catch (Exception e) {
            System.err.println("Error deserializando pedido: " + line);
            return null;
        }
    }
}
