package com.freakeshop.freak_e_shop;

import com.freakeshop.freak_e_shop.model.*;
import com.freakeshop.freak_e_shop.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AccesorioRepository accesorioRepo;
    private final MochilaRepository mochilaRepo;
    private final ComicRepository comicRepo;
    private final CamisaRepository camisaRepo;
    private final FiguraColeccionableRepository figuraRepo;
    private final PelucheRepository pelucheRepo;
    private final StockRepository stockRepo;

    // Datos de prueba

    public DataInitializer(AccesorioRepository accesorioRepo,
            MochilaRepository mochilaRepo,
            ComicRepository comicRepo,
            CamisaRepository camisaRepo,
            FiguraColeccionableRepository figuraRepo,
            PelucheRepository pelucheRepo,
            StockRepository stockRepo) {
        this.accesorioRepo = accesorioRepo;
        this.mochilaRepo = mochilaRepo;
        this.comicRepo = comicRepo;
        this.camisaRepo = camisaRepo;
        this.figuraRepo = figuraRepo;
        this.pelucheRepo = pelucheRepo;
        this.stockRepo = stockRepo;
    }

    @Override
    public void run(String... args) {

        // 1. Accesorio -> ID: A0001
        if (accesorioRepo.buscarPorId("A0001") == null) {
            Accesorio accesorio = new Accesorio(
                    "A0001", "Collar Sharingan Naruto",
                    "Collar metálico con el símbolo Sharingan de la serie Naruto Shippuden. Incluye cadena ajustable de acero inoxidable con acabado premium.",
                    12.99, "https://placehold.co/600x600/2a2a2a/f9f9f9?text=Collar+Sharingan",
                    "Collar");
            accesorio.setDestacado(true);
            accesorioRepo.guardar(accesorio);
            System.out.println("Producto de prueba creado: Accesorio (A0001)");
        }
        if (stockRepo.obtenerStock("A0001") == 0)
            stockRepo.actualizarStock("A0001", 15);

        // 2. Mochila -> ID: M0001
        if (mochilaRepo.buscarPorId("M0001") == null) {
            Mochila mochila = new Mochila(
                    "M0001", "Mochila Attack on Titan",
                    "Mochila con diseño de las Tropas de Reconocimiento. Fabricada en poliéster resistente con compartimento acolchado.",
                    45.50, "https://placehold.co/600x600/2a2a2a/f9f9f9?text=Mochila+Titan",
                    "L", "Poliéster 600D", true);
            mochila.setDestacado(true);
            mochilaRepo.guardar(mochila);
            System.out.println("Producto de prueba creado: Mochila (M0001)");
        }
        if (stockRepo.obtenerStock("M0001") == 0)
            stockRepo.actualizarStock("M0001", 10);

        // 3. Comic -> ID: CO001
        if (comicRepo.buscarPorId("CO001") == null) {
            Comic comic = new Comic(
                    "CO001", "Spider-Man Into the Spider-Verse",
                    "Edición especial del cómic que inspiró la película animada. Arte exclusivo con portada variante coleccionable.",
                    25.00, "https://placehold.co/600x600/2a2a2a/f9f9f9?text=Comic+Spider-Verse",
                    "Marvel Comics", 1, "Español");
            comic.setDestacado(true);
            comicRepo.guardar(comic);
            System.out.println("Producto de prueba creado: Comic (CO001)");
        }
        if (stockRepo.obtenerStock("CO001") == 0)
            stockRepo.actualizarStock("CO001", 20);

        // 4. Camisa -> ID: CA001
        if (camisaRepo.buscarPorId("CA001") == null) {
            Camisa camisa = new Camisa(
                    "CA001", "Camisa Goku Ultra Instinct",
                    "Camiseta con estampado premium de Goku. Algodón orgánico suave al tacto con impresión de alta resolución.",
                    18.50, "https://placehold.co/600x600/2a2a2a/f9f9f9?text=Camisa+Goku",
                    "M", "Algodón 100%");
            camisa.setDestacado(true);
            camisaRepo.guardar(camisa);
            System.out.println("Producto de prueba creado: Camisa (CA001)");
        }
        if (stockRepo.obtenerStock("CA001") == 0)
            stockRepo.actualizarStock("CA001", 25);

        // 5. FiguraColeccionable -> ID: F0001
        if (figuraRepo.buscarPorId("F0001") == null) {
            FiguraColeccionable figura = new FiguraColeccionable(
                    "F0001", "Figura Luffy Gear 5",
                    "Figura de acción de Monkey D. Luffy en su transformación Gear 5. Incluye base decorativa y accesorios.",
                    55.00, "https://placehold.co/600x600/2a2a2a/f9f9f9?text=Figura+Luffy",
                    "One Piece", true);
            figura.setDestacado(true);
            figuraRepo.guardar(figura);
            System.out.println("Producto de prueba creado: FiguraColeccionable (F0001)");
        }
        if (stockRepo.obtenerStock("F0001") == 0)
            stockRepo.actualizarStock("F0001", 8);

        // 6. Peluche -> ID: P0001
        if (pelucheRepo.buscarPorId("P0001") == null) {
            Peluche peluche = new Peluche(
                    "P0001", "Peluche Pikachu Gigante",
                    "Peluche oficial de Pikachu tamaño grande. Material hipoalergénico con relleno de fibra siliconada.",
                    35.00, "https://placehold.co/600x600/2a2a2a/f9f9f9?text=Peluche+Pikachu",
                    "Felpa hipoalergénica", "Grande");
            peluche.setDestacado(true);
            pelucheRepo.guardar(peluche);
            System.out.println("Producto de prueba creado: Peluche (P0001)");
        }
        if (stockRepo.obtenerStock("P0001") == 0)
            stockRepo.actualizarStock("P0001", 10);

        // 7. Figura Destacada 1 → ID: F0002
        if (figuraRepo.buscarPorId("F0002") == null) {
            FiguraColeccionable fig2 = new FiguraColeccionable(
                    "F0002", "Figura Articulada Spider-Man Miles Morales",
                    "Espectacular figura de acción de 15cm altamente articulada, incluye accesorios intercambiables y base dinámica. Edición especial de coleccionista.",
                    89.99, "https://placehold.co/600x600/2a2a2a/f9f9f9?text=Spider-Man+Miles",
                    "Marvel", true);
            fig2.setDestacado(true);
            figuraRepo.guardar(fig2);
            System.out.println("Producto destacado creado: Figura (F0002)");
        }
        if (stockRepo.obtenerStock("F0002") == 0)
            stockRepo.actualizarStock("F0002", 5);

        // 8. Accesorio Destacado 2 → ID: A0002
        if (accesorioRepo.buscarPorId("A0002") == null) {
            Accesorio acc2 = new Accesorio(
                    "A0002", "Anillo Único El Señor de los Anillos",
                    "Réplica exacta bañada en oro de 24k con inscripción élfica láser. Incluye cadena para colgar y caja de madera tallada.",
                    120.00, "https://placehold.co/600x600/2a2a2a/f9f9f9?text=Anillo+Unico",
                    "Anillo");
            acc2.setDestacado(true);
            accesorioRepo.guardar(acc2);
            System.out.println("Producto destacado creado: Accesorio (A0002)");
        }
        if (stockRepo.obtenerStock("A0002") == 0)
            stockRepo.actualizarStock("A0002", 3);

        // 9. Mochila Destacada 3 → ID: M0002
        if (mochilaRepo.buscarPorId("M0002") == null) {
            Mochila moc2 = new Mochila(
                    "M0002", "Mochila Táctica Cyberpunk 2077",
                    "Mochila urbana de diseño futurista con luces LED integradas, puerto de carga USB y materiales impermeables de alta resistencia.",
                    145.50, "https://placehold.co/600x600/2a2a2a/f9f9f9?text=Cyberpunk+Bag",
                    "L", "Nylon/Polímero", true);
            moc2.setDestacado(true);
            mochilaRepo.guardar(moc2);
            System.out.println("Producto destacado creado: Mochila (M0002)");
        }
        if (stockRepo.obtenerStock("M0002") == 0)
            stockRepo.actualizarStock("M0002", 7);

        System.out.println("DataInitializer completado.");
    }
}
