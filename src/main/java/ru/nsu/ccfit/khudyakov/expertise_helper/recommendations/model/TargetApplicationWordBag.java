package ru.nsu.ccfit.khudyakov.expertise_helper.recommendations.model;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class TargetApplicationWordBag implements Doc {

    private UUID id;

    private List<String> applicationsWordBag;

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public List<String> getDocTerms() {
        return applicationsWordBag;
    }

}
