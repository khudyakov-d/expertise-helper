package ru.nsu.ccfit.khudyakov.expertise_helper.files;

import java.io.File;

public interface FileManager {

    void create(String relativePath);

    File createAndGet(String relativePath);

    void save(String relativePath, byte[] content);

    void delete(String relativePath);

    File load(String relativePath);

    String getResourcesDir();

}
