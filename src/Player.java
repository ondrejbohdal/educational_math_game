
public class Player {
	private String name;
    private int score,timeScore;

    public Player(String name,int score, int timeScore)
    {
        this.name=name;
        this.score=score;
        this.timeScore=timeScore;
    }

    //accessor methods to private variables
    public String getName()
    {
        return this.name;
    }
    public int getScore()
    {
        return this.score;
    }
    public int getTimeScore()
    {
        return this.timeScore;
    }

}
