package com.eds.netaclon.puzzlegraph.renderer;

import java.util.*;
import java.util.logging.Logger;

import com.eds.netaclon.puzzlegraph.graphic.IntPosition;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GenericGraphicSeed;
import com.eds.netaclon.puzzlegraph.tile.Tile;

public class PuzzleZeldaLikeRenderer implements Visualizer {


    private static final Logger logger = Logger.getLogger("logger");
	private static final int Xsize = 13;
	private static final int Ysize = 7;
	private static final int Xmiddle =Xsize/2;
	private static final int Ymiddle =Ysize/2;

	private static final double ROOM_ANYWAY = 1d;//.5d
	private final Puzzle puz;

	private Random roomCollapser= new Random(0);
	
	private Map<Room,IntPosition> roomMap;
	private Map<IntPosition,Room> posMap;
    private char[][] rendered;

	public PuzzleZeldaLikeRenderer(Puzzle puz) {
		this.puz = puz;
	}

	public static Visualizer render(Puzzle puz)
	{	PuzzleZeldaLikeRenderer renderer = new PuzzleZeldaLikeRenderer(puz);
		PosMapCalculator posMapCalculator = new PosMapCalculator();
		renderer.roomMap = posMapCalculator.calculateRoomMap(puz);
		renderer.posMap=posMapCalculator.getPosMap();
		char[][] res= renderer.renderDynamic();
        renderer.rendered = res;
        return renderer;

	}

    public void show()
    {
        for(char[] row: rendered)
        {
            for (char cell: row)
                System.out.print(cell);
            System.out.println();
        }
    }
	
	public char[][] renderDynamic()
	{	char[][] renderedPuzzle;
		


		int upperx = Integer.MIN_VALUE, uppery=Integer.MIN_VALUE;
		int lowerx = Integer.MAX_VALUE, lowery=Integer.MAX_VALUE;
		for (IntPosition pos: roomMap.values())
		{
            lowerx = Math.min(lowerx, pos.x);
			lowery = Math.min(lowery, pos.y);
			
			upperx = Math.max(upperx, pos.x);
			uppery = Math.max(uppery, pos.y);
		}
		renderedPuzzle = new char[(uppery-lowery+1)*Ysize][];
		for (int i =0;i<renderedPuzzle.length;i++)
		{	renderedPuzzle[i] = new char[(upperx-lowerx+1)*Xsize];
			for (int j=0;j<renderedPuzzle[i].length;j++)
				renderedPuzzle[i][j]=' ';
		}
		for (Room room:roomMap.keySet())
		{	
			if(room.getItemNames().size()>0||roomCollapser.nextDouble()<ROOM_ANYWAY)
			{printRoomSmart(renderedPuzzle,room,new IntPosition(lowerx,lowery));}
			else
			{printCorridors(renderedPuzzle,room,new IntPosition(lowerx,lowery));}

		}
		
		buildBaseWalls(renderedPuzzle);

		return renderedPuzzle;
	}
	


	private void buildBaseWalls(char[][] renderedPuzzle) {
		GenericGraphicSeed ggs = new GenericGraphicSeed(new char[][]{{'.','.','.'},{'.',' ','.'},{'.','.','.'}},
				true, true, new IntPosition(1,1), Tile.BASE_WALL);
		ggs.semina(renderedPuzzle);
		
	}


	
	private void printCorridors(char[][] array, Room r, IntPosition lower) {
		IntPosition p = roomMap.get(r);
		IntPosition southLeft=new IntPosition((p.x-lower.x)*Xsize,(p.y-lower.y)*Ysize);
		IntPosition middleOfRoom= new IntPosition(southLeft.x+Xmiddle,southLeft.y+Ymiddle);
		if (isAccessibleFrom(r, p.down()))
		{ 
			drawEmptyRect(array,new IntPosition(middleOfRoom.x,middleOfRoom.y-Ymiddle),middleOfRoom,Tile.BASE_FLOOR);
			
		}
		if (isAccessibleFrom(r, p.up()))
		{
			drawEmptyRect(array,middleOfRoom,new IntPosition(middleOfRoom.x,middleOfRoom.y+Ymiddle),Tile.BASE_FLOOR);
		}
		if (isAccessibleFrom(r, p.right()))
		{
			drawEmptyRect(array,middleOfRoom,new IntPosition(middleOfRoom.x+Xmiddle,middleOfRoom.y),Tile.BASE_FLOOR);
		}
		if (isAccessibleFrom(r, p.left()))
		{
			drawEmptyRect(array,new IntPosition(middleOfRoom.x-Xmiddle,middleOfRoom.y),middleOfRoom,Tile.BASE_FLOOR);
		}
		
	}
	
	private void printRoomSmart(char[][] array, Room r, IntPosition lower)
	{
		IntPosition p = roomMap.get(r);
		IntPosition southLeft=new IntPosition((p.x-lower.x)*Xsize,(p.y-lower.y)*Ysize);
		IntPosition northRight = new IntPosition(southLeft.x+Xsize,southLeft.y+Ysize);
		drawFullRect(array,southLeft.up().up().right().right(),northRight.down().down().left().left(),Tile.BASE_FLOOR);
//		drawEmptyRect(array,northRight,southLeft,Tile.BASE_WALL);
		printCorridors(array,r,lower);
		
		
	}


	private boolean isAccessibleFrom(Room room, IntPosition p)
	{	
		Room otherRoom = posMap.get(p);
		for (Door d:puz.getDoors(room))
		{	if(otherRoom==d.getInRoom(puz)||otherRoom==d.getOutRoom(puz))
				return true;
		}
		return false;
	}

	

	
	private void drawEmptyRect(char[][] renderedPuzzle, IntPosition minPos,IntPosition maxPos,Tile tile)
	{	
		for (int rowNum=minPos.y;rowNum<=maxPos.y;rowNum++)
		{ 	renderedPuzzle[rowNum][minPos.x]=tile.text();
			renderedPuzzle[rowNum][maxPos.x]=tile.text();
		}
		for (int colNum=minPos.x;colNum<=maxPos.x;colNum++)
		{ 	renderedPuzzle[minPos.y][colNum]=tile.text();
			renderedPuzzle[maxPos.y][colNum]=tile.text();
		}
	}
	
	private void drawFullRect(char[][] renderedPuzzle,  IntPosition minPos,IntPosition maxPos, Tile tile)
	{	for (int rowNum=minPos.y;rowNum<maxPos.y;rowNum++)
		{	for (int colNum=minPos.x;colNum<maxPos.x;colNum++)
			{
				renderedPuzzle[rowNum][colNum]=tile.text();
			}
		}
		
	}
	
	

}
