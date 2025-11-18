package com.project.RunGame.map.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.map.model.Edge;
import com.project.RunGame.map.model.ProbabilityEntry;
import com.project.RunGame.map.model.Tile;

import java.io.File;
import java.util.*;

public class TileFinder {

    Map<Integer, Tile> tilesMap = new HashMap<Integer, Tile>();
    private ArrayList<Tile> tiles;

    public TileFinder() {
        this.loadTiles();
    }

    public Tile findTileRiverBank(DirectionEnum direction, Map<DirectionEnum, Integer> tilesNeighbour) {
        Tile tile;
        if (direction.equals(DirectionEnum.Up)) {
            tile = this.tilesMap.get(137);
        } else if (direction.equals(DirectionEnum.Down)) {
            tile = this.tilesMap.get(199);
        } else if (direction.equals(DirectionEnum.Left)) {
            tile = this.tilesMap.get(156);
        } else if (direction.equals(DirectionEnum.Right)) {
            tile = this.tilesMap.get(180);
        } else {
            return null;
        }
        if (!this.isTileSuitable(tile, tilesNeighbour)) {
            return null;
        } else {
            return tile;
        }
    }

    private Map<DirectionEnum, Edge> getEdgesMap(Map<DirectionEnum, Integer> tilesNeighbour) {
        Map<DirectionEnum, Edge> edgesNeighbour = new HashMap<DirectionEnum, Edge>();

        for (Map.Entry<DirectionEnum, Integer> entry : tilesNeighbour.entrySet()) {
            DirectionEnum direction = entry.getKey();
            Tile tile = tilesMap.get(entry.getValue());
            Edge edge = tile.getEdge(DirectionEnum.getOpposite(direction));
            edgesNeighbour.put(direction, edge);
        }
        return edgesNeighbour;
    }

    public Tile findTile(Map<DirectionEnum, Integer> tilesNeighbour) {
        Map<DirectionEnum, Edge> edgesNeighbour = this.getEdgesMap(tilesNeighbour);
        ArrayList<Tile> suitableTiles = findSuitableTiles(edgesNeighbour);
        return findTileRandomly(suitableTiles, tilesNeighbour);
    }

    private boolean isTileSuitable(Tile tile, Map<DirectionEnum, Integer> tilesNeighbour) {
        Map<DirectionEnum, Edge> edgesNeighbour = this.getEdgesMap(tilesNeighbour);
        ArrayList<Tile> suitableTiles = this.findSuitableTiles(edgesNeighbour);
        for (Tile suitableTile : suitableTiles) {
            if (suitableTile.equals(tile)) {
                return true;
            }
        }
        return false;

    }

    private int getUpdatedTileProbabilityIndex(Map<DirectionEnum, Integer> tilesNeighbours, Tile possibleTile) {
        double basisProbabilityIndex = possibleTile.getProbabilityIndex();
        Map<DirectionEnum, ProbabilityEntry> probabilityChange = possibleTile.getProbabilityChange();

        for (Map.Entry<DirectionEnum, Integer> tilesNeighbour : tilesNeighbours.entrySet()) {
            DirectionEnum tilesNeighbourDirection = tilesNeighbour.getKey();

            ProbabilityEntry basicTileProbability = probabilityChange.get(tilesNeighbourDirection);
            if (basicTileProbability != null) {
                Map<Integer, Double> probabilityByTileId = basicTileProbability.getTileProbabilities();

                for (Map.Entry<Integer, Double> tile : probabilityByTileId.entrySet()) {
                    Integer tileId = tile.getKey();
                    Double tileProbabilityChange = tile.getValue();

                    if (possibleTile.getId() == tileId) {
                        basisProbabilityIndex = basisProbabilityIndex * tileProbabilityChange;
                    }
                }
            }

        }
        return (int) basisProbabilityIndex;
    }

    public Tile findTileRandomly(ArrayList<Tile> suitableTiles, Map<DirectionEnum, Integer> tilesNeighbour) {
        if (suitableTiles.isEmpty()) {
            return this.tiles.get(0); // or handle empty list case
        }

        Random random = new Random();
        int totalWeight = 0;
        for (Tile possibleTile : suitableTiles) {
            int weight = this.getUpdatedTileProbabilityIndex(tilesNeighbour, possibleTile);
            if (weight < 0) {
                throw new IllegalStateException("Tile weight cannot be negative: " + weight);
            }
            totalWeight += weight;
        }

        if (totalWeight <= 0) {
            System.err.println("Total weight is not positive: " + totalWeight);
            return this.tiles.get(0); // or throw an exception
        }

        int randomValue = random.nextInt(totalWeight);
        int accumulatedWeight = 0;
        for (Tile possibleTile : suitableTiles) {
            int weight = this.getUpdatedTileProbabilityIndex(tilesNeighbour, possibleTile);

            accumulatedWeight += weight;
            if (randomValue < accumulatedWeight) {
                return possibleTile;
            }
        }

        // Fallback (should theoretically never reach here if totalWeight > 0)
        return this.tiles.get(0);
    }


    public Tile getRandomTile() {
        Random random = new Random();
        //int randomTileIndex = random.nextInt(this.tiles.size()-1);
        return this.tiles.get(1);

        //return this.tiles.get(randomTileIndex);
    }


    private ArrayList<Tile> findSuitableTiles(Map<DirectionEnum, Edge> edgesNeighbour) {
        ArrayList<Tile> suitableTiles = new ArrayList<Tile>();
        if (edgesNeighbour.size() == 0) {
            return this.tiles;
        }
        for (int tileNumber = 0; tileNumber < tiles.size(); tileNumber++) {
            int numberOfCorrectEdges = 0;

            for (Map.Entry<DirectionEnum, Edge> edgesEntry : edgesNeighbour.entrySet()) {
                DirectionEnum direction = edgesEntry.getKey();
                Tile currentTile = tiles.get(tileNumber);
                Edge edgeDatabase = currentTile.getEdge(direction);
                Edge edgeNeighbour = edgesNeighbour.get(direction);

                if (edgeDatabase.equals(edgeNeighbour)) {
                    numberOfCorrectEdges++;

                }
                if (numberOfCorrectEdges == edgesNeighbour.size()) {
                    suitableTiles.add(currentTile);
                }
            }
        }
        return suitableTiles;
    }

    private Map<DirectionEnum, ProbabilityEntry> getProbabilityEntries(JsonNode probabilityChangeNode) {
        Map<DirectionEnum, ProbabilityEntry> probabilityChange = new HashMap<>();
        if (probabilityChangeNode != null) {
            Iterator<Map.Entry<String, JsonNode>> probFields = probabilityChangeNode.fields();
            while (probFields.hasNext()) {
                Map.Entry<String, JsonNode> probEntry = probFields.next();
                DirectionEnum direction = DirectionEnum.valueOf(probEntry.getKey());
                JsonNode probMapNode = probEntry.getValue();

                Map<Integer, Double> tileProbabilities = new HashMap<>();
                Iterator<Map.Entry<String, JsonNode>> probMapFields = probMapNode.fields();
                while (probMapFields.hasNext()) {
                    Map.Entry<String, JsonNode> probMapEntry = probMapFields.next();
                    int tileId = Integer.parseInt(probMapEntry.getKey());
                    double probability = probMapEntry.getValue().asDouble();
                    tileProbabilities.put(tileId, probability);
                }
                ProbabilityEntry probabilityEntry = new ProbabilityEntry(tileProbabilities);
                probabilityChange.put(direction, probabilityEntry);
            }
        }


        return probabilityChange;
    }

    private void loadTiles() {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File("src/main/resources/TilesLowWaterProba.json"));
            //JsonNode root = mapper.readTree(new File("src/main/resources/Tiles.json"));
            JsonNode tilesNode = root.get("tiles");
            ArrayList<Tile> tiles = new ArrayList<Tile>();
            Iterator<Map.Entry<String, JsonNode>> fields = tilesNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();

                JsonNode tileData = entry.getValue().get("coord");
                int size = tileData.size();
                int[] upEdgeTable = new int[size];
                int[] downEdgeTable = new int[size];
                int[] leftEdgeTable = new int[size];
                int[] rightEdgeTable = new int[size];
                for (int col = 0; col < size; col++) {
                    upEdgeTable[col] = tileData.get(0).get(col).asInt();
                    downEdgeTable[col] = tileData.get(size - 1).get(col).asInt();
                    leftEdgeTable[col] = tileData.get(col).get(0).asInt();
                    rightEdgeTable[col] = tileData.get(col).get(size - 1).asInt();
                }
                Map<DirectionEnum, Edge> edges = new HashMap<DirectionEnum, Edge>();
                edges.put(DirectionEnum.Up, new Edge(upEdgeTable));
                edges.put(DirectionEnum.Down, new Edge(downEdgeTable));
                edges.put(DirectionEnum.Left, new Edge(leftEdgeTable));
                edges.put(DirectionEnum.Right, new Edge(rightEdgeTable));
                int[] upLeft = new int[]{tileData.get(0).get(0).asInt()};
                int[] upRight = new int[]{tileData.get(0).get(size - 1).asInt()};
                int[] downLeft = new int[]{tileData.get(size - 1).get(0).asInt()};
                int[] downRight = new int[]{tileData.get(size - 1).get(size - 1).asInt()};
                edges.put(DirectionEnum.UpLeft, new Edge(upLeft));
                edges.put(DirectionEnum.UpRight, new Edge(upRight));
                edges.put(DirectionEnum.DownLeft, new Edge(downLeft));
                edges.put(DirectionEnum.DownRight, new Edge(downRight));

                int id = entry.getValue().get("id").asInt();
                int ProbabilityIndex = entry.getValue().get("probabilityIndex").asInt();
                JsonNode probabilityChangeNode = entry.getValue().get("probabilityChange");
                Map<DirectionEnum, ProbabilityEntry> probabilityChange = this.getProbabilityEntries(probabilityChangeNode);
                Tile tile = new Tile(edges, id, ProbabilityIndex, probabilityChange);
                tiles.add(tile);
                this.tiles = tiles;
                for (int i = 0; i < this.tiles.size(); i++) {
                    Tile currentTile = tiles.get(i);
                    this.tilesMap.put(currentTile.getId(), currentTile);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}