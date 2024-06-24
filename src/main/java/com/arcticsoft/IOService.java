package com.arcticsoft;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IOService {

    private IOService() {
    }

    public static Path createDirectory(String dirName) {
        try {
            return Files.createDirectories(Paths.get(dirName));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
