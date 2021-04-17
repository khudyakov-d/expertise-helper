package ru.nsu.ccfit.khudyakov.expertise_helper.recommendations.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ExpertApplicationWordBag implements Doc {

    private UUID expertId;

    private List<String> applicationsWordBag;

    @Override
    public UUID getID() {
        return expertId;
    }

    @Override
    public List<String> getDocTerms() {
        return applicationsWordBag;
    }

}
