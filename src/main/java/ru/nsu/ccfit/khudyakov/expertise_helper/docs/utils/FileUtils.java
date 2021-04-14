package ru.nsu.ccfit.khudyakov.expertise_helper.docs.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static File createResultFile(String resultPath) {
        try {
            File file = new File(resultPath);

            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IllegalStateException();
                }
            }

            return file;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

}
