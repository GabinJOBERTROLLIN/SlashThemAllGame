package com.project.RunGame.map.model;

import java.util.Map;

import com.project.RunGame.helper.DirectionEnum;

public class Tile {
	Map<DirectionEnum,Edge> edges;
	int id;
	private int probabilityIndex;
	
	public Tile(Map<DirectionEnum,Edge> edges,int id){
		this.edges = edges;
		this.id = id;
	}
	public Tile(Map<DirectionEnum,Edge> edges,int id,int probabilityIndex){
		this.edges = edges;
		this.id = id;
		this.probabilityIndex = probabilityIndex;
	}
	public Edge getEdge(DirectionEnum direction) {
		return edges.get(direction);
	}
	public Map<DirectionEnum,Edge> getEdges(){
		return this.edges;
	}
	public int getId() {
		return this.id;
	}
	public int getProbabilityIndex() {
		return this.probabilityIndex;
	}
	
	public String toString() {
		return "id="+this.id+", edges="+this.edges.toString();
	}
}
