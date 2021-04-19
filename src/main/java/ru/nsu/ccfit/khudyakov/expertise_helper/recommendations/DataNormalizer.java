package ru.nsu.ccfit.khudyakov.expertise_helper.recommendations;

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class DataNormalizer {
    private static final Set<String> stopWords;

    private static final Pattern specialSymbolsPattern = Pattern.compile("\\pP*\\d*");

    private static final Pattern spacesPattern = Pattern.compile("\\s+");

   private static final SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.RUSSIAN, 3);

    static {
        stopWords = new HashSet<>();
        try {
            File file = new ClassPathResource("stop-word-russian.txt").getFile();
            Files.lines(file.toPath()).forEach(stopWords::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String textToLowerCase(String text) {
        return text == null ? null : text.toLowerCase();
    }

    private String removeSpecialSymbols(String text) {
        return text == null ? null : specialSymbolsPattern.matcher(text).replaceAll("");
    }

    public List<String> splitText(String text) {
        if (text != null) {
            List<String> list = new ArrayList<>(Arrays.asList(spacesPattern.split(text)));
            list.removeIf(s -> s.equals(""));
            return list;
        } else {
            return null;
        }
    }

    private List<String> removeStopWords(List<String> words) {
        if (words != null) {
            words.removeIf(stopWords::contains);
            return words;
        } else {
            return null;
        }
    }

    private List<String> stemWords(List<String> words) {
        if (words != null) {
            for (int i = 0, wordsSize = words.size(); i < wordsSize; i++) {
                String word = words.get(i);
                words.set(i, (String) stemmer.stem(word));
            }
            return words;
        } else {
            return null;
        }
    }

    public List<String> normalizeText(String text) {
        return stemWords(removeStopWords(splitText(removeSpecialSymbols(textToLowerCase(text)))));
    }
}