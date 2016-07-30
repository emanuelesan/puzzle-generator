package com.eds.netaclon.puzzlegraph.graphic;

import java.util.LinkedList;
import java.util.List;

import com.eds.netaclon.puzzlegraph.tile.Tile;

public class GenericGraphicSeed implements GraphicSeed {

	private IntPosition center;

	private char[][]  localGroup;
	private char[][] renderedMap;
	private boolean borderAsAnyTile=false;
	private boolean atLeastOne=false;
	private Tile newTile;

	
	public GenericGraphicSeed(char[][] localGroup, boolean atLeastOne,boolean borderAsAnyTile, IntPosition center,Tile newTile)
	{	this.localGroup = localGroup;
		this.atLeastOne = atLeastOne;
		this.borderAsAnyTile = borderAsAnyTile;
		this.center = center;
		this.newTile = newTile;
		
	}
	


	public void semina(char[][] renderedMap) {
		this.renderedMap=renderedMap;
		int halfX = localGroup[0].length-center.x;
		int halfY = localGroup.length-center.y;
		List<Growable> seedList=new LinkedList<Growable>();
		for (int row=0; row<renderedMap.length;row++)
		{
			for(int col=0;col<renderedMap[row].length;col++)
			{	IntPosition pos = new IntPosition(col,row);
				IntPosition min = new IntPosition(col-center.x,row-center.y);
				IntPosition max =  new IntPosition(col+halfX,row+halfY);
				if (match(min,max))
				{
					seedList.add(new SingleCellChanger(pos,newTile,renderedMap));
				}
			}
		}
		
		for(Growable growable:seedList)
		{
			growable.germinate();
		}
	}


	private boolean match(IntPosition min,IntPosition max)
	{	
		if (!borderAsAnyTile&&(max.y-min.y+1!=localGroup.length || max.x-min.x+1!=localGroup[0].length))
		{return false;}
		int matched=0;
		for (int row=0; row<localGroup.length;row++)
		{
			for(int col=0;col<localGroup[row].length;col++)
			{	if (min.y+row <0 ||min.y+row>=renderedMap.length
					||min.x+col <0 ||min.x+col>=renderedMap[0].length)
				{
					if (!atLeastOne)
					{ return false;} 
					continue;
				}
				
				if( localGroup[row][col]!=Tile.ANYTILE.text()
					&& localGroup[row][col]==renderedMap[min.y+row][min.x+col])
				{	
						matched++;//atLeastOne dedicated return condition
				}
				else if (!atLeastOne||(row==this.center.y&&col==this.center.x))
				{ return false;}
			}
		}
		//atLeastOne must return in its dedicated return condition
		return (atLeastOne && matched>1)||!atLeastOne;
		
	}

}
