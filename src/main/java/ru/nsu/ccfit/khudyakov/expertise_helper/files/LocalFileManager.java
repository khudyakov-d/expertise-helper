package ru.nsu.ccfit.khudyakov.expertise_helper.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.write;
import static java.nio.file.Paths.get;


public class LocalFileManager implements FileManager {
    private static final String RESOURCES_DIR = get(System.getProperty("user.dir"), "files").toString();

    @Override
    public void create(String relativePath) {
        try {
            Path path = get(RESOURCES_DIR, relativePath);
            Files.createDirectories(path.getParent());
            File file = new File(path.toString());
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IllegalStateException();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public File createAndGet(String relativePath) {
        create(relativePath);
        return load(relativePath);
    }

    public void save(String relativePath, byte[] content) {
        try {
            Path absolutePath = get(RESOURCES_DIR, relativePath);
            createDirectories(absolutePath.getParent());
            write(absolutePath, content);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    public void delete(String relativePath) {
        try {
            Files.delete(get(RESOURCES_DIR, relativePath));
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public File load(String relativePath) {
        return new File(get(RESOURCES_DIR, relativePath).toString());
    }

    public String getResourcesDir() {
        return RESOURCES_DIR;
    }
}
