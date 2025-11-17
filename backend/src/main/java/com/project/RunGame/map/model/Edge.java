package com.project.RunGame.map.model;

import java.util.Arrays;

public class Edge {
    private int[] edgeValues;

    public Edge(int[] edgeValues) {
        this.edgeValues = edgeValues;
    }

    public int[] getEdgevalues() {
        return this.edgeValues;
    }

    public String toString() {
        String toPrint = "";
        for (int i = 0; i < this.edgeValues.length; i++) {
            toPrint += this.edgeValues[i];
            toPrint += ",";
        }
        return toPrint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Arrays.equals(edgeValues, edge.edgeValues);
    }


}
