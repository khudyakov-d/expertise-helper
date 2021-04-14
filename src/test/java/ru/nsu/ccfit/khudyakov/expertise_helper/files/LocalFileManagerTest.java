package ru.nsu.ccfit.khudyakov.expertise_helper.files;

import org.junit.jupiter.api.Test;

import java.io.File;

import static java.nio.file.Paths.get;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalFileManagerTest {

    private final LocalFileManager localFileManager = new LocalFileManager();

    @Test
    void saveProjectConfigurationFile() {
        localFileManager.save("/project/test.txt", new byte[100]);
        File savedFile = new File(get(localFileManager.getResourcesDir(), "project", "test.txt").toString());
        assertTrue(savedFile.exists());
        savedFile.delete();
    }

}