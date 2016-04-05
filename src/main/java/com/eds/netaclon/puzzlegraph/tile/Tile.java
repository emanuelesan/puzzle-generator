package com.eds.netaclon.puzzlegraph.tile;

import java.util.HashMap;
import java.util.Map;

/**
 * queste sono tile category. rispecchiano i nomi delle immagini all'interno della texture skin fuck yea
 * @author SangregorioEm
 *
 */
public enum Tile {
	BASE_WALL('#'),
	BASE_FLOOR('.'),
	BASE_VOID(' '),
	ANYTILE('*');
	
	static Map<Integer,Tile> charMap = null;
	private char text;
	
	Tile(char corresponding)
	{this.text=corresponding;}
	
	public char text()
	{
		return this.text;
	}

	public static Tile fromChar(char askedChar)
	{
		if (charMap==null)
		{charMap = new HashMap<Integer,Tile>();
		for (Tile tile:Tile.values())
			charMap.put((int) tile.text, tile);
		}
		return charMap.get((int)askedChar);
	}
	

}
