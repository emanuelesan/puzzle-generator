package com.eds.netaclon.puzzlegraph.graphic;

/**
 * Created by emanuelesan on 30/03/16.
 */
public class IntPosition {

    public final int x;
    public final int y;
    public IntPosition(int x,int y)
    {
        this.x =x;
        this.y=y;
    }

    @Override
    public boolean equals(Object o)
    {	if (o instanceof IntPosition)
    {	 IntPosition otherPos = (IntPosition)o;
        return otherPos!=null&&otherPos.x==this.x&& otherPos.y==this.y;
    }
        return false;
    }
    @Override
    public int hashCode() {
        return this.x * 10000 + y;
    };

    public IntPosition up()
    {return new IntPosition(this.x,this.y+1); }

    public IntPosition down()
    {return new IntPosition(this.x,this.y-1); }

    public IntPosition left()
    {return new IntPosition(this.x-1,this.y); }

    public IntPosition right()
    {return new IntPosition(this.x+1,this.y); }

    public String toString()
    {return "x: "+x+", y: "+y;}
}
