package com.freakeshop.freak_e_shop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;

// Encuentra la ruta del directorio de datos (donde se encuentra pom.xml), 
// independientemente del directorio de trabajo o la carpeta padre donde esté 
// ubicado el proyecto.
@Component
public class DataDirectoryResolver {

    private final String resolvedPath;

    public DataDirectoryResolver(@Value("${app.data.dir:data}") String dataDir) {
        Path projectRoot = findProjectRoot();
        Path path = projectRoot.resolve(dataDir);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("No se pudo crear el directorio de datos: " + e.getMessage());
        }
        this.resolvedPath = path.toString();
        System.out.println("Directorio de datos resuelto: " + this.resolvedPath);
    }

    public String getPath() {
        return resolvedPath;
    }

    // Busca la raíz del proyecto (donde está pom.xml) usando múltiples estrategias.
    private Path findProjectRoot() {
        // Estrategia 1: user.dir es la raíz del proyecto
        Path cwd = Paths.get(System.getProperty("user.dir"));
        if (Files.exists(cwd.resolve("pom.xml"))) {
            return cwd;
        }

        // Estrategia 2: Subir desde la ubicación de las clases compiladas
        // En dev: target/classes/ → subimos 2 niveles → raíz del proyecto
        try {
            Path classLocation = Paths.get(
                    DataDirectoryResolver.class.getProtectionDomain()
                            .getCodeSource().getLocation().toURI());
            Path candidate = classLocation;
            // Subimos hasta encontrar pom.xml (máximo 5 niveles)
            for (int i = 0; i < 5 && candidate != null; i++) {
                if (Files.exists(candidate.resolve("pom.xml"))) {
                    return candidate;
                }
                candidate = candidate.getParent();
            }
        } catch (URISyntaxException | SecurityException e) {
            // Si falla, intentamos la siguiente estrategia
        }

        // Estrategia 3: Subir desde user.dir buscando pom.xml
        Path search = cwd;
        for (int i = 0; i < 5 && search != null; i++) {
            // Buscar en subdirectorios inmediatos
            try (DirectoryStream<Path> dirs = Files.newDirectoryStream(search, Files::isDirectory)) {
                for (Path dir : dirs) {
                    if (Files.exists(dir.resolve("pom.xml"))) {
                        return dir;
                    }
                }
            } catch (IOException ignored) {
            }
            search = search.getParent();
        }

        // Fallback: usar user.dir
        System.err.println("ADVERTENCIA: No se encontró pom.xml. Usando directorio de trabajo: " + cwd);
        return cwd;
    }
}
