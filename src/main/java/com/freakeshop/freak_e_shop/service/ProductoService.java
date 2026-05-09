package com.freakeshop.freak_e_shop.service;

import com.freakeshop.freak_e_shop.CategoriaIdConfig;
import com.freakeshop.freak_e_shop.model.*;
import com.freakeshop.freak_e_shop.repository.*;
import com.freakeshop.freak_e_shop.dto.ProductoDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final AccesorioRepository accesorioRepo;
    private final MochilaRepository mochilaRepo;
    private final ComicRepository comicRepo;
    private final CamisaRepository camisaRepo;
    private final FiguraColeccionableRepository figuraRepo;
    private final PelucheRepository pelucheRepo;
    private final StockRepository stockRepo;
    private final CategoriaIdConfig categoriaIdConfig;

    public ProductoService(AccesorioRepository accesorioRepo,
            MochilaRepository mochilaRepo,
            ComicRepository comicRepo,
            CamisaRepository camisaRepo,
            FiguraColeccionableRepository figuraRepo,
            PelucheRepository pelucheRepo,
            StockRepository stockRepo,
            CategoriaIdConfig categoriaIdConfig) {
        this.accesorioRepo = accesorioRepo;
        this.mochilaRepo = mochilaRepo;
        this.comicRepo = comicRepo;
        this.camisaRepo = camisaRepo;
        this.figuraRepo = figuraRepo;
        this.pelucheRepo = pelucheRepo;
        this.stockRepo = stockRepo;
        this.categoriaIdConfig = categoriaIdConfig;
    }

    // Retorna todos los productos de todas las categorías en una sola lista.
    public List<Producto> obtenerTodos() {
        List<Producto> todos = new ArrayList<>();
        todos.addAll(accesorioRepo.obtenerTodos());
        todos.addAll(mochilaRepo.obtenerTodos());
        todos.addAll(comicRepo.obtenerTodos());
        todos.addAll(camisaRepo.obtenerTodos());
        todos.addAll(figuraRepo.obtenerTodos());
        todos.addAll(pelucheRepo.obtenerTodos());

        // Sincronizar stock desde el repositorio de stock
        Map<String, Integer> stockMap = stockRepo.obtenerTodos();
        for (Producto p : todos) {
            p.setStock(stockMap.getOrDefault(p.getId(), 0));
        }
        return todos;
    }

    // Busca un producto por id en todos los repositorios.
    public Producto obtenerPorId(String id) {
        Producto p = null;
        p = accesorioRepo.buscarPorId(id);
        if (p == null) p = mochilaRepo.buscarPorId(id);
        if (p == null) p = comicRepo.buscarPorId(id);
        if (p == null) p = camisaRepo.buscarPorId(id);
        if (p == null) p = figuraRepo.buscarPorId(id);
        if (p == null) p = pelucheRepo.buscarPorId(id);
        
        if (p != null) {
            p.setStock(stockRepo.obtenerStock(id));
        }
        return p;
    }

    // Retorna productos de una categoría específica.
    public List<Producto> obtenerPorCategoria(String categoria) {
        return switch (categoria) {
            case "Accesorio" -> new ArrayList<>(accesorioRepo.obtenerTodos());
            case "Mochila" -> new ArrayList<>(mochilaRepo.obtenerTodos());
            case "Comic" -> new ArrayList<>(comicRepo.obtenerTodos());
            case "Camisa" -> new ArrayList<>(camisaRepo.obtenerTodos());
            case "FiguraColeccionable" -> new ArrayList<>(figuraRepo.obtenerTodos());
            case "Peluche" -> new ArrayList<>(pelucheRepo.obtenerTodos());
            default -> new ArrayList<>();
        };
    }

    // Retorna solo productos con stock > 0.
    public List<Producto> obtenerConStock() {
        Map<String, Integer> stockMap = stockRepo.obtenerTodos();
        return obtenerTodos().stream()
                .filter(p -> stockMap.getOrDefault(p.getId(), 0) > 0)
                .collect(Collectors.toList());
    }

    // Busca productos cuyo nombre contenga el texto (ignoreCase).
    public List<Producto> buscarPorNombre(String texto) {
        if (texto == null || texto.isBlank()) {
            return obtenerConStock();
        }
        String busqueda = texto.toLowerCase();
        Map<String, Integer> stockMap = stockRepo.obtenerTodos();
        return obtenerTodos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(busqueda))
                .filter(p -> stockMap.getOrDefault(p.getId(), 0) > 0)
                .collect(Collectors.toList());
    }

    // Retorna exclusivamente los productos destacados fijos (los 9 de prueba).
    public List<Producto> getProductosDestacadosFijos() {
        return obtenerTodos().stream()
                .filter(Producto::isDestacado)
                .filter(p -> stockRepo.obtenerStock(p.getId()) > 0)
                .limit(8)
                .collect(Collectors.toList());
    }

    public Double obtenerPrecioMinimoGlobal() {
        return 0.0;
    }

    public Double obtenerPrecioMaximoGlobal() {
        return 5000000.0;
    }

    public com.freakeshop.freak_e_shop.dto.PaginaResultadoDTO<ProductoDTO> obtenerPaginaCatalogo(
            List<String> categorias, Double precioMin, Double precioMax, String orden, int paginaSolicitada, String q) {

        List<Producto> lista = obtenerTodos().stream()
                .filter(p -> stockRepo.obtenerStock(p.getId()) > 0)
                .collect(Collectors.toList());

        // Filtrar por texto de búsqueda (q)
        if (q != null && !q.trim().isEmpty()) {
            String qLower = q.trim().toLowerCase();
            lista = lista.stream()
                    .filter(p -> p.getNombre().toLowerCase().contains(qLower))
                    .collect(Collectors.toList());
        }

        // Filtrar por categorías múltiples
        if (categorias == null || categorias.isEmpty()) {
            lista = new java.util.ArrayList<>(); // Si no hay categorías seleccionadas, no hay productos
        } else {
            lista = lista.stream()
                    .filter(p -> categorias.stream().anyMatch(c -> obtenerCategoria(p).equalsIgnoreCase(c)))
                    .collect(Collectors.toList());
        }

        // Filtrar por precio
        if (precioMin != null) {
            lista = lista.stream().filter(p -> p.getPrecio() >= precioMin).collect(Collectors.toList());
        }
        if (precioMax != null) {
            lista = lista.stream().filter(p -> p.getPrecio() <= precioMax).collect(Collectors.toList());
        }

        // Ordenamiento
        if ("asc".equalsIgnoreCase(orden)) {
            lista.sort(Comparator.comparing(Producto::getPrecio));
        } else if ("desc".equalsIgnoreCase(orden)) {
            lista.sort(Comparator.comparing(Producto::getPrecio).reversed());
        } else {
            // Por defecto "destacados": destacados primero, luego id ascendente
            lista.sort((p1, p2) -> {
                if (p1.isDestacado() && !p2.isDestacado())
                    return -1;
                if (!p1.isDestacado() && p2.isDestacado())
                    return 1;
                return p1.getId().compareTo(p2.getId());
            });
        }

        // Convertir a DTO para la vista
        List<ProductoDTO> listaDTO = lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        // Paginación
        int TAMANIO_PAGINA = 6;
        int totalProductos = listaDTO.size();
        int totalPaginas = (int) Math.ceil((double) totalProductos / TAMANIO_PAGINA);
        int paginaActual = Math.max(1, Math.min(paginaSolicitada, totalPaginas == 0 ? 1 : totalPaginas));

        int indiceDesde = (paginaActual - 1) * TAMANIO_PAGINA;
        int indiceHasta = Math.min(indiceDesde + TAMANIO_PAGINA, totalProductos);

        List<ProductoDTO> productosPagina = totalProductos == 0 ? new ArrayList<>()
                : listaDTO.subList(indiceDesde, indiceHasta);

        // Generar elementos de paginación con "..." si es necesario
        List<Object> elementosPaginacion = new ArrayList<>();
        if (totalPaginas <= 7) {
            for (int i = 1; i <= totalPaginas; i++)
                elementosPaginacion.add(i);
        } else {
            elementosPaginacion.add(1);
            if (paginaActual > 3)
                elementosPaginacion.add("...");
            int start = Math.max(2, paginaActual - 1);
            int end = Math.min(totalPaginas - 1, paginaActual + 1);
            for (int i = start; i <= end; i++)
                elementosPaginacion.add(i);
            if (paginaActual < totalPaginas - 2)
                elementosPaginacion.add("...");
            elementosPaginacion.add(totalPaginas);
        }

        Map<String, Object> filtrosAplicados = new HashMap<>();
        filtrosAplicados.put("categoria", categorias);
        filtrosAplicados.put("precioMin", precioMin);
        filtrosAplicados.put("precioMax", precioMax);
        filtrosAplicados.put("orden", orden);

        return new com.freakeshop.freak_e_shop.dto.PaginaResultadoDTO<>(
                productosPagina, paginaActual, totalPaginas, totalProductos, filtrosAplicados, elementosPaginacion);
    }

    // Elimina un producto por id del repositorio correspondiente y del stock.
    public void eliminar(String id) {
        Producto p = obtenerPorId(id);
        if (p == null)
            return;

        if (p.isDestacado()) {
            throw new IllegalArgumentException("No se puede eliminar un producto destacado fijo.");
        }

        if (p instanceof Accesorio)
            accesorioRepo.eliminar(id);
        else if (p instanceof Mochila)
            mochilaRepo.eliminar(id);
        else if (p instanceof Comic)
            comicRepo.eliminar(id);
        else if (p instanceof Camisa)
            camisaRepo.eliminar(id);
        else if (p instanceof FiguraColeccionable)
            figuraRepo.eliminar(id);
        else if (p instanceof Peluche)
            pelucheRepo.eliminar(id);

        stockRepo.eliminar(id);
    }

    // Crea un nuevo producto recibiendo todos los datos de un DTO y guardándolo en
    // la categoría elegida.
    // Genera un ID lineal con prefijo de categoría (ej: A0001, M0001, CO001).
    public void crearProducto(ProductoDTO dto) {
        if (dto.getPrecio() < 1000) {
            throw new IllegalArgumentException("El precio mínimo permitido es 1.000 COP.");
        }
        if (dto.getPrecio() > 5000000) {
            throw new IllegalArgumentException("El precio máximo permitido es 5.000.000 COP.");
        }
        // Truncar a 3 decimales
        double precioTruncado = Math.floor(dto.getPrecio() * 1000) / 1000.0;
        dto.setPrecio(precioTruncado);

        String id = categoriaIdConfig.generarId(dto.getCategoria(), obtenerPorCategoria(dto.getCategoria()));
        Producto p = null;

        switch (dto.getCategoria()) {
            case "Accesorio" -> p = new Accesorio(id, dto.getNombre(), dto.getDescripcion(), dto.getPrecio(),
                    dto.getImagen(), dto.getTipo());
            case "Mochila" ->
                p = new Mochila(id, dto.getNombre(), dto.getDescripcion(), dto.getPrecio(), dto.getImagen(),
                        dto.getTamanoMochila(), dto.getMaterialFabricacion(), dto.isTieneCompartimentoPC());
            case "Comic" -> p = new Comic(id, dto.getNombre(), dto.getDescripcion(), dto.getPrecio(), dto.getImagen(),
                    dto.getEditorial(), dto.getNumeroVolumen(), dto.getIdioma());
            case "Camisa" -> p = new Camisa(id, dto.getNombre(), dto.getDescripcion(), dto.getPrecio(), dto.getImagen(),
                    dto.getTalla(), dto.getMaterialCamisa());
            case "FiguraColeccionable" -> p = new FiguraColeccionable(id, dto.getNombre(), dto.getDescripcion(),
                    dto.getPrecio(), dto.getImagen(), dto.getFranquicia(), dto.isEdicionLimitada());
            case "Peluche" -> p = new Peluche(id, dto.getNombre(), dto.getDescripcion(), dto.getPrecio(),
                    dto.getImagen(), dto.getMaterialPeluche(), dto.getTamanoPeluche());
        }

        if (p != null) {
            if (p instanceof Accesorio a)
                accesorioRepo.guardar(a);
            else if (p instanceof Mochila m)
                mochilaRepo.guardar(m);
            else if (p instanceof Comic c)
                comicRepo.guardar(c);
            else if (p instanceof Camisa c)
                camisaRepo.guardar(c);
            else if (p instanceof FiguraColeccionable f)
                figuraRepo.guardar(f);
            else if (p instanceof Peluche pel)
                pelucheRepo.guardar(pel);

            stockRepo.actualizarStock(id, dto.getStock());
        }
    }

    // Modifica un producto existente con los datos del DTO.
    // Localiza el producto por su ID, actualiza sus atributos y persiste los
    // cambios.
    public void modificarProducto(ProductoDTO dto) {
        if (dto.getPrecio() < 1000) {
            throw new IllegalArgumentException("El precio mínimo permitido es 1.000 COP.");
        }
        if (dto.getPrecio() > 5000000) {
            throw new IllegalArgumentException("El precio máximo permitido es 5.000.000 COP.");
        }
        // Truncar a 3 decimales
        double precioTruncado = Math.floor(dto.getPrecio() * 1000) / 1000.0;
        dto.setPrecio(precioTruncado);

        String id = dto.getId();
        Producto p = obtenerPorId(id);
        if (p == null)
            return;

        // Actualizar atributos comunes
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setPrecio(dto.getPrecio());
        p.setImagen(dto.getImagen());

        // Actualizar atributos específicos y persistir
        if (p instanceof Accesorio a) {
            a.setTipo(dto.getTipo());
            accesorioRepo.actualizar(a);
        } else if (p instanceof Mochila m) {
            m.setTamano(dto.getTamanoMochila());
            m.setMaterialFabricacion(dto.getMaterialFabricacion());
            m.setTieneCompartimentoPC(dto.isTieneCompartimentoPC());
            mochilaRepo.actualizar(m);
        } else if (p instanceof Comic c) {
            c.setEditorial(dto.getEditorial());
            c.setNumeroVolumen(dto.getNumeroVolumen());
            c.setIdioma(dto.getIdioma());
            comicRepo.actualizar(c);
        } else if (p instanceof Camisa c) {
            c.setTalla(dto.getTalla());
            c.setMaterial(dto.getMaterialCamisa());
            camisaRepo.actualizar(c);
        } else if (p instanceof FiguraColeccionable f) {
            f.setFranquicia(dto.getFranquicia());
            f.setEdicionLimitada(dto.isEdicionLimitada());
            figuraRepo.actualizar(f);
        } else if (p instanceof Peluche pel) {
            pel.setMaterial(dto.getMaterialPeluche());
            pel.setTamano(dto.getTamanoPeluche());
            pelucheRepo.actualizar(pel);
        }

        // Actualizar stock
        stockRepo.actualizarStock(id, dto.getStock());
    }

    // Retorna el nombre de la categoría (nombre simple de la clase).
    public String obtenerCategoria(Producto p) {
        if (p == null)
            return "";
        return p.getClass().getSimpleName();
    }

    // Retorna la lista canónica de las 6 categorías.
    public List<String> obtenerCategorias() {
        return List.of("Accesorio", "Mochila", "Comic", "Camisa", "FiguraColeccionable", "Peluche");
    }

    // Convierte un Producto a ProductoDTO para optimizar el rendimiento en la
    // vista.
    public ProductoDTO convertirADTO(Producto p) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(p.getId());
        dto.setCategoria(obtenerCategoria(p));
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setImagen(p.getImagen());

        // Agregar información de stock
        dto.setStock(stockRepo.obtenerStock(p.getId()));

        if (p instanceof Accesorio a) {
            dto.setTipo(a.getTipo());
        } else if (p instanceof Mochila m) {
            dto.setTamanoMochila(m.getTamano());
            dto.setMaterialFabricacion(m.getMaterialFabricacion());
            dto.setTieneCompartimentoPC(m.isTieneCompartimentoPC());
        } else if (p instanceof Comic c) {
            dto.setEditorial(c.getEditorial());
            dto.setNumeroVolumen(c.getNumeroVolumen());
            dto.setIdioma(c.getIdioma());
        } else if (p instanceof Camisa c) {
            dto.setTalla(c.getTalla());
            dto.setMaterialCamisa(c.getMaterial());
        } else if (p instanceof FiguraColeccionable f) {
            dto.setFranquicia(f.getFranquicia());
            dto.setEdicionLimitada(f.isEdicionLimitada());
        } else if (p instanceof Peluche pel) {
            dto.setMaterialPeluche(pel.getMaterial());
            dto.setTamanoPeluche(pel.getTamano());
        }
        return dto;
    }

    // Retorna todos los productos en formato DTO.
    public List<ProductoDTO> obtenerTodosComoDTO() {
        return obtenerTodos().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
}
