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
                    "A0001", "Gorra Monsters University",
                    "Gorra oficial de Monsters University con diseño bordado. Ajustable con diseño bordado frontal.",
                    55000, "https://i.pinimg.com/736x/a8/88/26/a88826a360097a66014afa7f2fe71abe.jpg",
                    "Gorra");
            accesorio.setDestacado(true);
            accesorioRepo.guardar(accesorio);
            System.out.println("Producto de prueba creado: Accesorio (A0001)");
        }
        if (stockRepo.obtenerStock("A0001") == 0)
            stockRepo.actualizarStock("A0001", 15);

        // 2. Mochila -> ID: M0001
        if (mochilaRepo.buscarPorId("M0001") == null) {
            Mochila mochila = new Mochila(
                    "M0001", "Mochila Edición Bleach",
                    "Mochila inspirada en la Sociedad de Almas, perfecta para llevar tus pertenencias con estilo shinigami.",
                    120000, "https://http2.mlstatic.com/D_NQ_NP_2X_823201-CBT110031241712_042026-F.webp",
                    "Mediano", "Lona impermeable", true);
            mochila.setDestacado(true);
            mochilaRepo.guardar(mochila);
            System.out.println("Producto de prueba creado: Mochila (M0001)");
        }
        if (stockRepo.obtenerStock("M0001") == 0)
            stockRepo.actualizarStock("M0001", 10);

        // 3. Comic -> ID: CO001
        if (comicRepo.buscarPorId("CO001") == null) {
            Comic comic = new Comic(
                    "CO001", "El Asombroso Spiderman: Revelaciones",
                    "Peter Parker se enfrenta a una nueva amenaza en las calles de Nueva York mientras descubre un oscuro secreto de su pasado que cambiará su vida.",
                    83000, "https://http2.mlstatic.com/D_NQ_NP_2X_930398-MCO109680488862_042026-F.webp",
                    "Salvat Marvel", 24, "Español");
            comic.setDestacado(true);
            comicRepo.guardar(comic);
            System.out.println("Producto de prueba creado: Comic (CO001)");
        }
        if (stockRepo.obtenerStock("CO001") == 0)
            stockRepo.actualizarStock("CO001", 20);

        // 4. Camisa -> ID: CA001
        if (camisaRepo.buscarPorId("CA001") == null) {
            Camisa camisa = new Camisa(
                    "CA001", "Camisa One Piece Beige",
                    "Camiseta oversize beige con diseño frontal y trasero de Luffy de One Piece..",
                    95000, "https://i.pinimg.com/1200x/9c/0c/62/9c0c62b6a823a2328b6f2d676985f2db.jpg",
                    "XL", "Algodón premium");
            camisa.setDestacado(true);
            camisaRepo.guardar(camisa);
            System.out.println("Producto de prueba creado: Camisa (CA001)");
        }
        if (stockRepo.obtenerStock("CA001") == 0)
            stockRepo.actualizarStock("CA001", 25);

        // 5. FiguraColeccionable -> ID: F0001
        if (figuraRepo.buscarPorId("F0001") == null) {
            FiguraColeccionable figura = new FiguraColeccionable(
                    "F0001", "Figura Choso Jujutsu Kaisen",
                    "Figura coleccionable estilo anime del personaje Choso.",
                    120000, "https://i.pinimg.com/1200x/90/df/c0/90dfc0d76241a6d3021b693d2e7aa7ff.jpg",
                    "Jujutsu Kaisen", true);
            figura.setDestacado(true);
            figuraRepo.guardar(figura);
            System.out.println("Producto de prueba creado: FiguraColeccionable (F0001)");
        }
        if (stockRepo.obtenerStock("F0001") == 0)
            stockRepo.actualizarStock("F0001", 8);

        // 6. Peluche -> ID: P0001
        if (pelucheRepo.buscarPorId("P0001") == null) {
            Peluche peluche = new Peluche(
                    "P0001", "Peluche Charmander",
                    "Peluche suave y tierno con detalles bordados, perfecto para regalar y coleccionar.",
                    90000, "https://http2.mlstatic.com/D_NQ_NP_2X_729281-MCO100433347769_122025-F.webp",
                    "Exterior de Felpa, Relleno de Poliéster", "Grande");
            peluche.setDestacado(true);
            pelucheRepo.guardar(peluche);
            System.out.println("Producto de prueba creado: Peluche (P0001)");
        }
        if (stockRepo.obtenerStock("P0001") == 0)
            stockRepo.actualizarStock("P0001", 10);

        // 7. Figura Destacada 1 → ID: F0002
        if (figuraRepo.buscarPorId("F0002") == null) {
            FiguraColeccionable fig2 = new FiguraColeccionable(
                    "F0002", "Funko Pop Creeper Minecraft",
                    "Figura Funko Pop del Creeper inspirado en Minecraft.",
                    98000, "https://i.pinimg.com/1200x/a4/e8/25/a4e825662002d30584f34fb402836716.jpg",
                    "Minecraft", false);
            fig2.setDestacado(true);
            figuraRepo.guardar(fig2);
            System.out.println("Producto destacado creado: Figura (F0002)");
        }
        if (stockRepo.obtenerStock("F0002") == 0)
            stockRepo.actualizarStock("F0002", 5);

        // 8. Accesorio Destacado 2 → ID: A0002
        if (accesorioRepo.buscarPorId("A0002") == null) {
            Accesorio acc2 = new Accesorio(
                    "A0002", "Pulsera Capitan America",
                    "Pulsera negra de cuero con símbolo del Capitán América en el centro.",
                    35000, "https://tse1.mm.bing.net/th/id/OIP.GtO_Wu4qF_7-7Jb7DJR2bAHaHa?r=0&rs=1&pid=ImgDetMain&o=7&rm=3",
                    "Pulsera");
            acc2.setDestacado(true);
            accesorioRepo.guardar(acc2);
            System.out.println("Producto destacado creado: Accesorio (A0002)");
        }
        if (stockRepo.obtenerStock("A0002") == 0)
            stockRepo.actualizarStock("A0002", 3);

        // 9. Mochila Destacada 3 → ID: M0002
        if (mochilaRepo.buscarPorId("M0002") == null) {
            Mochila moc2 = new Mochila(
                    "M0002", "Mochila Chainsaw Man",
                    "Diseño agresivo y llamativo basado en Denji y Pochita, ideal para destacar en tus clases o eventos.",
                    140000, "https://http2.mlstatic.com/D_NQ_NP_2X_891357-CBT86371526455_062025-F.webp",
                    "Grande", "Poliéster", false);
            moc2.setDestacado(true);
            mochilaRepo.guardar(moc2);
            System.out.println("Producto destacado creado: Mochila (M0002)");
        }
        if (stockRepo.obtenerStock("M0002") == 0)
            stockRepo.actualizarStock("M0002", 7);

        System.out.println("DataInitializer completado.");
    }
}
