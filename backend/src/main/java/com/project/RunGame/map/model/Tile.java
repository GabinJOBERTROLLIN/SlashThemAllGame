package com.project.RunGame.map.model;

import com.project.RunGame.helper.DirectionEnum;

import java.util.Map;

public class Tile {
    Map<DirectionEnum, Edge> edges;
    int id;
    private int probabilityIndex;
    private Map<DirectionEnum, ProbabilityEntry> probabilityChange;

    public Tile(Map<DirectionEnum, Edge> edges, int id) {
        this.edges = edges;
        this.id = id;
    }

    public Tile(Map<DirectionEnum, Edge> edges, int id, int probabilityIndex) {
        this.edges = edges;
        this.id = id;
        this.probabilityIndex = probabilityIndex;
    }

    public Tile(Map<DirectionEnum, Edge> edges, int id, int probabilityIndex, Map<DirectionEnum, ProbabilityEntry> probabilityChange) {
        this.edges = edges;
        this.id = id;
        this.probabilityIndex = probabilityIndex;
        this.probabilityChange = probabilityChange;
    }

    public Edge getEdge(DirectionEnum direction) {
        return edges.get(direction);
    }

    public Map<DirectionEnum, Edge> getEdges() {
        return this.edges;
    }

    public int getId() {
        return this.id;
    }

    public int getProbabilityIndex() {
        return this.probabilityIndex;
    }

    public Map<DirectionEnum, ProbabilityEntry> getProbabilityChange() {
        return probabilityChange;
    }

    public String toString() {
        return "id=" + this.id + ", edges=" + this.edges.toString();
    }
}
