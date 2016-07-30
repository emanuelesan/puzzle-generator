package com.eds.netaclon.puzzlegraph.graphic;

import com.eds.netaclon.puzzlegraph.tile.Tile;

public class SingleCellChanger implements Growable {
	private IntPosition germinatedPos;
	private Tile newTile;
	private char[][] renderedMap;
	
	
	public SingleCellChanger(IntPosition pos, Tile newTile2,char[][] renderedMap) {
		germinatedPos=pos;
		newTile=newTile2;
		this.renderedMap = renderedMap;
	}
	
	public void germinate() {
		renderedMap[germinatedPos.y][germinatedPos.x]=newTile.text();
	}
	
}
