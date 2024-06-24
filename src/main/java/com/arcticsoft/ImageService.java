package com.arcticsoft;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageService {
    private static final Logger LOGGER = Logger.getLogger(ImageService.class.getSimpleName());
    private static final Path IMAGES_DIR;

    static {
        try {
            IMAGES_DIR = Files.createDirectories(Paths.get("images"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private ImageService() {
    }

    public static void downloadPages(int fromPage, int toPage, boolean virtualThreads) {
        if (virtualThreads)
            downloadPagesConcurrent(fromPage, toPage);
        else
            downloadPagesSequential(fromPage, toPage);
    }

    private static void downloadPagesSequential(int fromPage, int toPage) {
        for (var pageNumber = fromPage; pageNumber <= toPage; pageNumber++) {
            var links = HtmlParser.getLinksPagina(pageNumber);
            for (var link : links)
                downloadImage(HtmlParser.obtenerImagenURI(link));
        }
    }

    private static void downloadPagesConcurrent(int fromPage, int toPage) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor();) {
            for (var pageNumber = fromPage; pageNumber <= toPage; pageNumber++) {
                var finalPageNumber = pageNumber;
                executor.submit(() -> {
                    var links = HtmlParser.getLinksPagina(finalPageNumber);
                    for (var link : links)
                        downloadImage(HtmlParser.obtenerImagenURI(link));
                });
            }
        }
    }

    private static void downloadImage(final URI uri) {
        var fileName = Paths.get(uri.getPath()).getFileName().toString();
        var imagePath = IMAGES_DIR.resolve(fileName);
        if (Files.exists(imagePath)) {
            LOGGER.log(Level.INFO, "Imagen {0} ya existe, omitiendo descarga.", fileName);
            return;
        }
        try {
            var data = uri.toURL().openStream().readAllBytes();
            Files.write(imagePath, data, StandardOpenOption.CREATE);
            LOGGER.log(Level.INFO, "Imagen {0} descargada desde thread: {1}.",
                    new Object[] { fileName, Thread.currentThread().threadId() });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}