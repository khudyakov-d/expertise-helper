package ru.nsu.ccfit.khudyakov.expertise_helper.files;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileManagerConfig {

    @Bean
    public FileManager fileManager() {
        return new LocalFileManager();
    }

}
