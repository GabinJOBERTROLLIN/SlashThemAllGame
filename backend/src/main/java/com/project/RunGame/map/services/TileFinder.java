package com.project.RunGame.map.services;



import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.map.model.Edge;
import com.project.RunGame.map.model.Tile;

public class TileFinder {
	
    private ArrayList<Tile> tiles;
    
    public TileFinder() {
        this.loadTiles();
    }
    public Tile findTile(Map<DirectionEnum,Integer> tilesNeighbour) {
    	Map<Integer,Tile> tilesMap = new HashMap<Integer,Tile>();
    	for (int i=0;i<this.tiles.size();i++) {
    		Tile currentTile = tiles.get(i);
    		tilesMap.put(currentTile.getId(),currentTile);
    	}
    	
    	
    	Map<DirectionEnum,Edge> edgesNeighbour = new HashMap<DirectionEnum,Edge>();
    	
    	for (Map.Entry<DirectionEnum,Integer> entry : tilesNeighbour.entrySet()) {
    		DirectionEnum direction = entry.getKey();
    		Tile tile = tilesMap.get(entry.getValue());
    		Edge edge = tile.getEdge(getOppositeDirection(direction));
    		edgesNeighbour.put(direction, edge);
    	}
    	return findTileFromEdge(edgesNeighbour);
    }
    

    public Tile findTileFromEdge(Map<DirectionEnum,Edge> edgesNeighbour) {
    	ArrayList<Tile> suitableTiles = findSuitableTiles(edgesNeighbour);
    	
    	Tile tile ;

    	if (suitableTiles.size()>0) {
    		Random random = new Random();
        	//int randomTileIndex = random.nextInt(suitableTiles.size());
        	//tile = suitableTiles.get(randomTileIndex);
    		
    		 int totalWeight = 0;
    	        for (Tile possibleTile : suitableTiles) {
    	            totalWeight += possibleTile.getProbabilityIndex();
    	        }

    	        // Generate a random number between 0 and totalWeight
    	        int randomValue = random.nextInt(totalWeight);

    	        // Select the tile
    	        int accumulatedWeight = 0;
    	        for (Tile possibleTile : suitableTiles) {
    	            accumulatedWeight += possibleTile.getProbabilityIndex();
    	            if (randomValue < accumulatedWeight) {
    	                return possibleTile;
    	            }
    	            
    	        } 
    	        
    	}
    	System.out.println("no tile found "+edgesNeighbour);
    	tile = this.tiles.get(0);
    	return tile;
    }
    
    public Tile getRandomTile() {
    	Random random = new Random();
    	//int randomTileIndex = random.nextInt(this.tiles.size()-1);
    	return this.tiles.get(1);

    	//return this.tiles.get(randomTileIndex);
    }
    private DirectionEnum getOppositeDirection(DirectionEnum direction) {
    	DirectionEnum oppositeDirecion = DirectionEnum.Up;
    	if (direction.equals(DirectionEnum.Up)) {
    		oppositeDirecion = DirectionEnum.Down;
    	}
    	else if (direction.equals(DirectionEnum.Down)) {
    		oppositeDirecion = DirectionEnum.Up;
    	}
		else if (direction.equals(DirectionEnum.Right)) {
			oppositeDirecion = DirectionEnum.Left;
		}
		else if (direction.equals(DirectionEnum.Left)) {
			oppositeDirecion = DirectionEnum.Right;
		}
    	return oppositeDirecion;
    }
    private  ArrayList<Tile> findSuitableTiles(Map<DirectionEnum,Edge> edgesNeighbour) {
    	ArrayList<Tile> suitableTiles = new ArrayList<Tile>(); 
    	if (edgesNeighbour.size() ==0) { return this.tiles;}
    	for (int tileNumber=0;tileNumber<tiles.size();tileNumber++) {
    		int numberOfCorrectEdges = 0;
    		
    		for (Map.Entry<DirectionEnum,Edge> edgesEntry : edgesNeighbour.entrySet()) {
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
    
	private void loadTiles() {
		try {
			ObjectMapper mapper = new ObjectMapper();

			try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Tiles.json")) {
				if (inputStream == null) {
					throw new RuntimeException("Fichier Tiles.json non trouvé dans le classpath !");
				}

				JsonNode root = mapper.readTree(inputStream);
				JsonNode tilesNode = root.get("tiles");
				ArrayList<Tile> tiles = new ArrayList<>();

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

					Map<DirectionEnum, Edge> edges = new HashMap<>();
					edges.put(DirectionEnum.Up, new Edge(upEdgeTable));
					edges.put(DirectionEnum.Down, new Edge(downEdgeTable));
					edges.put(DirectionEnum.Left, new Edge(leftEdgeTable));
					edges.put(DirectionEnum.Right, new Edge(rightEdgeTable));

					Tile tile = new Tile(edges,
							entry.getValue().get("id").asInt(),
							entry.getValue().get("probabilityIndex").asInt());
					tiles.add(tile);
				}

				this.tiles = tiles;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    
}