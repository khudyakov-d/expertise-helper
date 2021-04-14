package ru.nsu.ccfit.khudyakov.expertise_helper.configuration;

import freemarker.core.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.nio.charset.Charset;
import java.util.Properties;

@Configuration
public class FreeMarkerConfig {

    @Bean
    public FreeMarkerConfigurer getFreeMarkerConfigurer() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setDefaultEncoding(Charset.defaultCharset().displayName());
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates/");

        Properties settings = new Properties();
        settings.setProperty(Configurable.OUTPUT_ENCODING_KEY, Charset.defaultCharset().displayName());
        settings.setProperty("url_escaping_charset", Charset.defaultCharset().displayName());
        settings.setProperty(Configurable.NUMBER_FORMAT_KEY, "0.##");
        settings.setProperty(Configurable.DATETIME_FORMAT_KEY, "dd.MM.yyyy HH:mm:ss");
        settings.setProperty(Configurable.DATE_FORMAT_KEY, "dd.MM.yyyy ");
        settings.setProperty(Configurable.TIME_FORMAT_KEY, "HH:mm:ss");
        settings.setProperty(Configurable.AUTO_IMPORT_KEY_SNAKE_CASE, "/spring.ftl as spring");
        settings.setProperty(Configurable.TEMPLATE_EXCEPTION_HANDLER_KEY, "rethrow");
        freeMarkerConfigurer.setFreemarkerSettings(settings);

        return freeMarkerConfigurer;
    }

}
