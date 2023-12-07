package com.arcticsoft;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Service {
    private static String imagesDir;

    public Service() throws Exception {
        imagesDir = Files.createDirectories(Paths.get("images")).toString();
    }

    public void downloadNPages(int n) throws Exception {
        for (var i = 1; i <= n; i++) {
            var links = getLinksPagina(i);
            for (var link : links) {
                descargarImagen(obtenerImagenURI(link));
            }
            System.out.printf("Página %s completada%n", i);
        }
    }

    private List<String> getLinksPagina(int i) throws Exception {
        String url = "https://windows10spotlight.com/page/%s".formatted(i);
        Document doc = Jsoup.connect(url).get();
        return doc.select("a.anons-thumbnail.show[href]").stream().map(e -> e.attr("href")).toList();
    }

    private URI obtenerImagenURI(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        return URI.create(doc.select("figure").first().select("a").attr("href"));
    }

    private void descargarImagen(URI uri) throws Exception {
        String fileName = Paths.get(uri.getPath()).getFileName().toString();
        Path imagePath = Path.of(imagesDir, fileName);
        byte[] data = uri.toURL().openStream().readAllBytes();
        Files.write(imagePath, data, StandardOpenOption.CREATE);
    }
}
