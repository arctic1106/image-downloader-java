package com.arcticsoft;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageService {
    private static final Logger LOGGER = Logger.getLogger(ImageService.class.getSimpleName());
    private static final Path imagesDir = IOService.createDirectory("images");

    private ImageService() {
    }

    public static void downloadNPages(int fromPage, int toPage) {
        for (var i = fromPage; i <= toPage; i++) {
            var links = HtmlParser.getLinksPagina(i);
            LOGGER.log(Level.INFO, "\nPágina {0}: {1} Links encontrados\n", new Object[] { i, links.size() });
            for (var link : links)
                ImageService.downloadImage(HtmlParser.obtenerImagenURI(link));
            LOGGER.log(Level.INFO, "\nPágina {0} completada\n", i);
        }
    }

    private static void downloadImage(final URI uri) {
        var fileName = Paths.get(uri.getPath()).getFileName().toString();
        var imagePath = imagesDir.resolve(fileName);
        if (Files.exists(imagePath)) {
            LOGGER.log(Level.INFO, "Imagen {0} ya existe, omitiendo descarga\n", fileName);
            return;
        }
        try {
            var data = uri.toURL().openStream().readAllBytes();
            Files.write(imagePath, data, StandardOpenOption.CREATE);
            LOGGER.log(Level.INFO, "Imagen {0} descargada\n", fileName);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}