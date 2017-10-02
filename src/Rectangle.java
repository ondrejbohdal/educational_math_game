import java.awt.Color;

public class Rectangle {
	private static int heightRect,lengthRect;
	private int num,pos_x,pos_y,numcol,oldpos_x,oldpos_y;
	private Color colour;
	
	public Rectangle(int num, Color colour, int pos_x, int pos_y, int oldpos_x,int oldpos_y, int numcol)
	{
	this.num=num;
	this.colour=colour;
	this.pos_x=pos_x;
	this.pos_y=pos_y;
	this.oldpos_x=oldpos_x;
	this.oldpos_y=oldpos_y;
	this.numcol=numcol;
	}
	
	//accessor and mutator methods to private variables
	public static void setHeightRect(int height)
	{
		heightRect=height;
	}
	public static void setLengthRect(int length)
	{
		lengthRect=length;
	}	
	public static int getHeightRect()
	{
		return heightRect;
	}
	public static int getLengthRect()
	{
		return lengthRect;
	}
	public int getNum()
	{
		return this.num;
	}	
	public int getNumcol()
	{
		return this.numcol;
	}
	public int getPos_x()
	{
		return this.pos_x;
	}
	public int getOldpos_x()
	{
		return this.oldpos_x;
	}
	public int getOldpos_y()
	{
		return this.oldpos_y;
	}
	public void setPos_x(int pos_x)
	{
		this.pos_x=pos_x;
	}
	public int getPos_y()
	{
		return this.pos_y;
	}	
	public void setPos_y(int pos_y)
	{
		this.pos_y=pos_y;
	}	
	public Color getColour()
	{
		return this.colour;
	}
	
}

