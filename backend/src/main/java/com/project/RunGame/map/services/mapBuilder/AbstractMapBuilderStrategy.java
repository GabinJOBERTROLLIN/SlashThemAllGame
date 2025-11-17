package com.project.RunGame.map.services.mapBuilder;

import com.project.RunGame.map.model.Coordinates;
import com.project.RunGame.map.services.TileFinder;

import java.util.Map;

public abstract class AbstractMapBuilderStrategy {
    protected TileFinder tileFinder;

    public AbstractMapBuilderStrategy(TileFinder tileFinder) {
        this.tileFinder = tileFinder;
    }

    public abstract Map<Coordinates, Integer> createStartingMap(int mapSize);

}
