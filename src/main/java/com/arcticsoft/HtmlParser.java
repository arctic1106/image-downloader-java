package com.arcticsoft;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.jsoup.Jsoup;

public class HtmlParser {
    private static final String BASE_URL = "https://windows10spotlight.com/page/%s";

    private HtmlParser() {
    }

    public static List<String> getLinksPagina(int i) {
        try {
            return Jsoup.connect(BASE_URL.formatted(i)).get()
                    .select("a.anons-thumbnail.show[href]")
                    .stream().map(e -> e.attr("href"))
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static URI obtenerImagenURI(String link) {
        try {
            return new URI(Jsoup.connect(link).get()
                    .select("div.entry").first()
                    .select("a").first()
                    .attr("href"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
}