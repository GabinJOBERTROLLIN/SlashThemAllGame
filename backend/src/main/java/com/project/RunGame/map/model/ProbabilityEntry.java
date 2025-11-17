package com.project.RunGame.map.model;

import java.util.Map;

public class ProbabilityEntry {
    private Map<Integer, Double> tileProbabilities;

    //TODO: maybe remove ?
    public ProbabilityEntry() {
    }

    public ProbabilityEntry(Map<Integer, Double> tileProbabilities) {
        this.tileProbabilities = tileProbabilities;
    }

    public Map<Integer, Double> getTileProbabilities() {
        return tileProbabilities;
    }

    public void setTileProbabilities(Map<Integer, Double> tileProbabilities) {
        this.tileProbabilities = tileProbabilities;
    }
}
