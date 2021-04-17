package ru.nsu.ccfit.khudyakov.expertise_helper.recommendations.model;

import java.util.List;
import java.util.UUID;

public interface Doc {

    UUID getID();

    List<String> getDocTerms();

}