import java.util.Vector;

public class Group {
    private static int num,numOp;
    private int pos_x,pos_y;
    Vector<Integer> rects;
    
    public Group(int pos_x, int pos_y) {
    	rects = new Vector<Integer>();
        this.pos_x=pos_x;
        this.pos_y=pos_y;
        //there are no rectangles in the groups at the beginning (symbolised by -1)
        //and there are maximally 5 rectangles in group
        this.rects.add(-1);
        this.rects.add(-1);
        this.rects.add(-1);
        this.rects.add(-1);
        this.rects.add(-1);
    }

    //accessor and mutator methods
    public int getPos_y()
	{
		return this.pos_y;
	}
    
    public int getPos_x()
	{
		return this.pos_x;
	}
    
    public static void setNum(int number)
    {
    	num=number;
    }
    
    public static int getNum()
    {
    	return num;
    }

    public static void setNumOp(int number)
    {
    	numOp=number;
    }
    
    public static int getNumOp()
    {
    	return numOp;
    }
    
}

