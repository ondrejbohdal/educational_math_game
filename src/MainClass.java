import java.awt.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;

public class MainClass extends JFrame implements ActionListener, MouseMotionListener, MouseListener {
   /*
    * Author: Ondrej Bohdal, Internal assessment from computer science, May 2015
    * */
    private static final long serialVersionUID = -3538440273305694038L;
    private Player [] bestPlayers;
    //there are 10 levels so 10 best players
    private int level,numofex,exnum,numofgr,timesc,levcor,levtot,plussignsize,beginrectgr,totnumop,mnn,mxx,truelevel,time, num;

    /*level is the current level which the player plays
     * numofex is the number of examples in the level which player plays, numofex is random and is between 2 and 7
     * exnum is the number of the current example in the level
     * numofgr is the number of group
     * timesc is the total time so far spent in the level by the player
     * levcor is the number of correctly filled groups in the level
     * levtot is the total number of groups in the level
     * plussignsize is used for saving the width of this character
     * beginrectgr is used for saving the initial position where the rectangles drawn in groups start
     * totnumop is the total number of operands in the level
     * mnn is minimum value of the rectangle numbers in the level
     * mxx is maximum value of the rectangle numbers in the level
     * truelevel signalizes which level was chosen
     * level is the level which is played, when truelevel is 9 or 10, it changes with each example
     * num is the number of the rectangle which is being moved currently by the player
     * */

    private Vector<Rectangle> rect;
    //these are all the rectangles currently used in the example
    private boolean running;
    //this variable symbolizes that the game is active and player is playing it
    private Vector<Group> gr;
    //these are all the groups currently used in the example

    //these features come from JFrame and are used to provide GUI
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem menuItem1, menuItem2, menuItem3;
    JButton checkbt;
    JLabel timelb,numofexlb,levlb;
    JLabel background;

    javax.swing.Timer tm;
    static String [] svkvocab = {"Nov� hra","Pravidl�","Sie� sl�vy","�no","Nie","Naozaj chce� ukon�i� hru?","Naozaj?",
		  						"Naozaj chce� za�a� nov� hru? Aktu�lna hra bude straten�.",
		  "<html>1. Na za�iatku si zvol�� level, ktor� chce� hra�.<br>"
		  + "<br>2. V ka�dom leveli m�� n�hodn� mno�stvo pr�kladov,"
		  + "<br> a v ka�dom pr�klade m�� tri skupiny s n�hodn�m po�tom ��sel.<br><br>"
		  + "3. V ka�dom pr�klade m�� skupinu zafarben�ch ��sel,<br> ktor� m�� �ahan�m umiestni� do skup�n"
		  + " tak,<br> aby ich s��et bol v ka�dej skupine rovnak�,<br> pri�om ka�d� ��slo v skupine m� ma� odli�n� farbu.<br><br>"
		  + "4. Ak si v�imne� chybu, ��slo m��e� vr�ti� nasp��.<br><br>"
		  + "5. ��sla m��e� umiest�ova� len na miesta s tou istou farbou.<br>"
		  + "<br>6. Ke� s� v�etky ��sla v skupin�ch, m��e� si da� pr�klad<br> skontrolova� kliknut�m na dan� tla�idlo.<br><br>"
		  + "7. Celkov� sk�re je vyr�tan� ako percento spr�vne vyplnen�ch skup�n.<br><br>8. Ak si najlep��m hr��om v leveli"
		  + " (najlep�ie sk�re a �asov� sk�re), <br>po�iadame �a o tvoje meno do siene sl�vy.<br> "
		  + "Ak neuvedie� �iadne, bude� v siene sl�vy bez mena.<br><br>9. Ak nie si najlep��m hr��om, aspo� si si precvi�il rie�enie<br>"
		  + " matematick�ch pr�kladov, a hru si predsa m��e� zahra� znova.<br><br>10. Ak hru ukon��� pred�asne, do siene sl�vy sa nedostane�,<br>"
		  + " a ani neuvid�� fin�lny v�sledok.<br></html>",
                "Na spustenie hry cho� do menu a klikni na tla�idlo Nov� hra.","Vitaj!",
                "Meno","Sk�re","Zvo� si, pros�m, level, ktor� chce� hra�.","Zvo� level","Level",
                "Mal� kladn� ��sla, od 0 do 20","Mal� kladn� a z�porn� ��sla, od -20 do 20",
                "Mal� kladn� ��sla s najviac jedn�m desatinn�m miestom, od 0 do 10",
                "Mal� kladn� a z�porn� ��sla s najviac jedn�m desatinn�m miestom, od -10 do 10",
                "Mal� kladn� ��sla s najviac dvomi desatinn�mi miestami, od 0 do 10",
                "Mal� kladn� a z�porn� ��sla s najviac dvomi desatinn�mi miestami, od -10 do 10",
                "Ve�k� kladn� ��sla, od 0 do 1 mili�na",
                "Ve�k� kladn� a z�porn� ��sla, od -1 mili�na do 1 mili�na",
                "Mix v�etk�ch predo�l�ch cvi�en� 1 - len mal� ��sla",
                "Mix v�etk�ch predo�l�ch cvi�en� 2 - mal� a ve�k� ��sla", "Skontroluj", "�as", "V�sledok", "Pr�klad ", "Po�et spr�vne vyplnen�ch skup�n: ",
                "Gratulujeme!","Tvoje chyby: ", "Skupina", "Tvoj s��et","Spr�vny s��et", "<br>Tvoj v�sledok je vynikaj�ci, dost�va� sa do siene sl�vy!<br>"
                		+ " Gratulujeme!<br>Zadaj, pros�m �a, svoje meno.<br></html>", "V�sledok", "Tvoje v�sledn� sk�re za level je ", "Tvoje �asov� sk�re za level je ",
                		"Ak sa chce� dosta� do siene sl�vy, nez�faj, ale sk�s si zahra� znova!","�al�� level", "In� level", "�vodn� obrazovka",
                		"Toto bol posledn� pr�klad v leveli, a preto sa rozhodni, �o chce� robi� �alej.","�o �alej",
                		"Toto bol posledn� pr�klad v poslednom leveli, a preto sa rozhodni, �o chce� robi� �alej.",
                		"Tento level","IAbackgroundsvk.png","Zvo� level"
                	};

    static String [] engvocab = {"New Game","Rules","Hall of Fame","Yes","No","Do you really want to quit the game?","Are you sure?",
                                 "Do you really want to start a new game? The current game will be lost.",
                                 "<html>1. At first you choose the level which you want to play.<br><br>"
                                 + "2. In each level you have a random number of example,<br> and each example you have three groups with random number of operands.<br><br>"
                                 + "3. In each example, you have a set of coloured numbers,<br> which you should put by dragging rectangles with the numbers<br>"
                                 + " into several groups with the same sum so that each number<br> in a group has a different colour.<br><br>"
                                 + "4. If you realize a mistake, you can put<br> the rectangle away from the group.<br><br>"
                                 + "5. You can put a rectangle with a certain<br> colour only at the place with that colour.<br><br>"
                                 + "6. When you put all rectangles into the groups,<br> you can check them by clicking on a button used for it.<br><br>"
                                 + "7. The final score for the level is calculated<br> as the percentage of correctly filled groups.<br><br>"
                                 + "8. If you are the best player in the given level (best score and time score),<br>"
                                 + " you will be asked to enter your name into the hall of fame.<br> If you leave it blank, you will be there under none name.<br><br>"
                                 + "9. If you are not the best player, <br>you have at least practised solving math problems, <br>and you can try again playing the game.<br><br>"
                                 + "10. If you quit the game before the last example in the level,<br>you neither get into the hall of fame nor you see your final result.<br>"
                                 + "</html>",
                                 "For playing the game, go into the menu and click on the button New Game.","Welcome!",
                                 "Name","Score","Please choose the level which you want to play.","Choose level","Level",
                                 "Small positive numbers, from 0 to 20","Small positive and negative numbers, from -20 to 20",
                                 "Small positive numbers up to one decimal place, from 0 to 10",
                                 "Small positive and negative numbers up to one decimal place, from -10 to 10",
                                 "Small positive numbers up to two decimal places, from 0 to 10",
                                 "Small positive and negative numbers up to two decimal places, from -10 to 10",
                                 "Big positive numbers, from 0 to 1 million",
                                 "Big positive and negative numbers, from -1 million to 1 million",
                                 "Mix of all previous exercises 1 - only small numbers",
                                 "Mix of all previous exercises 2 - small and big numbers","Check","Time","Result","Problem ", "Number of correctly filled groups: ",
                                 "Congratulations!","Your mistakes: ", "Group", "Your sum","Correct sum","<br>Your result was great, you are in the hall of fame!<br>"
                                 + " Congratulations!<br>Enter your name, please.<br></html>", "Result", "Your score for this level is ", "Your time score for this level is ",
                                 "If you would like to get into the hall of fame, try playing the game again!","Next level", "Other level", "Main screen",
                                 "That was the last example in the level, so choose what you want to do next.","What next",
                                 "That was the last example in the last level, so choose what you want to do next.",
                                 "This level","IAbackground.png","Choose level"
                                };
    //this will be the field with the current language
    static String [] vocab = new String [svkvocab.length];

    public MainClass()
    {
        super("Educational Math Game");
        setSize(750, 750);
        setLayout(null);
        //initial settings
        level = -1;
        truelevel=0;
        num=-1;
        time=0;
        rect = new Vector<Rectangle>();
        gr = new Vector<Group>();
        bestPlayers = new Player[10];

        //ask the player to choose the language
        Object[] options = {"English",
                            "Sloven�ina"
                           };

        int n = JOptionPane.showOptionDialog(this,
                                             "Please choose your language. "
                                             + "Zvo� si svoj jazyk, pros�m.",
                                             "Language/Jazyk",
                                             JOptionPane.YES_NO_CANCEL_OPTION,
                                             JOptionPane.QUESTION_MESSAGE,
                                             null,
                                             options,
                                             options[0]);
        if (n == 1)
            for (int i = 0; i < vocab.length; i++)
                vocab[i] = svkvocab[i];
        else
            for (int i = 0; i < vocab.length; i++)
                vocab[i] = engvocab[i];

        running = false;
        //set the buttons and labels
        checkbt = new JButton(vocab[26]);
        checkbt.setBounds(getWidth()-200,getHeight()-170,120,40);
        checkbt.addActionListener(this);
        checkbt.setVisible(false);
        this.getContentPane().add(checkbt);

        timelb = new JLabel("");
        timelb.setBounds(300, 20, 150, 30);
        timelb.setFont(new Font("Arial", Font.BOLD, 20));
        timelb.setVisible(false);
        this.getContentPane().add(timelb);

        numofexlb = new JLabel("");
        numofexlb.setBounds(150, 20, 150, 30);
        numofexlb.setFont(new Font("Arial", Font.BOLD, 20));
        numofexlb.setVisible(false);
        this.getContentPane().add(numofexlb);

        levlb = new JLabel("");
        levlb.setBounds(30, 20, 150, 30);
        levlb.setFont(new Font("Arial", Font.BOLD, 20));
        levlb.setVisible(false);
        this.getContentPane().add(levlb);

        menuBar = new JMenuBar();

        //the menu itself
        menu = new JMenu("Menu");
        menuBar.add(menu);

        //a group of JMenuItems and adding them
        menuItem1 = new JMenuItem(vocab[0]);// "New Game"
        menuItem1.addActionListener(this);
        menu.add(menuItem1);

        menuItem2 = new JMenuItem(vocab[1]);// "Rules"
        menuItem2.addActionListener(this);
        menu.add(menuItem2);

        menuItem3 = new JMenuItem(vocab[2]);// "Hall of Fame"
        menuItem3.addActionListener(this);
        menu.add(menuItem3);

        setJMenuBar(menuBar);


        level=-1;

        //set what to do when the player closes the application - are you sure you want to close the game
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {
                if (level != -1) {
                	//level is not -1 if the game is active
                    Object[] options2 = {vocab[3], vocab[4]};//yes, no
                    int u = JOptionPane.showOptionDialog(MainClass.this,
                                                         vocab[5],//do you really want to quit the game?
                                                         vocab[6],//are you sure?
                                                         JOptionPane.YES_NO_CANCEL_OPTION,
                                                         JOptionPane.QUESTION_MESSAGE,
                                                         null,
                                                         options2,
                                                         options2[0]);
                    if (u == 0) {
                        createFile("BestPlayers.txt");
                        System.exit(0);
                    }
                    else
                        ;
                }
                else {
                    createFile("BestPlayers.txt");
                    System.exit(0);
                }

            }
        });



        addMouseMotionListener(this);
        addMouseListener(this);


        tm = new javax.swing.Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                time++;
                timelb.setText(vocab[27] + ": " + time);
            }
        });

        //this is used to put a background into the game at the start
        background = new JLabel();
        this.setLayout(new BorderLayout());
        this.add(background);
        background.setIcon(new ImageIcon(vocab[48]));
        background.setLayout(new FlowLayout());
        repaint();
    }

    //a method for creating labels quickly
    JLabel newlab(int x, int y, int width, int height, String str) {
        JLabel l = new JLabel(str);
        l.setBounds(x, y, width,height);
        l.setVisible(true);
        this.getContentPane().add(l);
        return l;
    }

    public void drawCenteredString(String s, int pos_x, int pos_y, Graphics g) {
        FontMetrics f = g.getFontMetrics();
        int x = pos_x + (80 - f.stringWidth(s)) / 2;
        int y = pos_y + (f.getAscent() + (40 - (f.getAscent() + f.getDescent())) / 2);
        g.drawString(s, x, y);
    }

    //read the best players from the file
    void readPlayersHall(String fileName) {
        String name = "";
        int level=0;
        int score = 0;
        int timeScore = 0;
        try {
            BufferedReader glasses = new BufferedReader(new FileReader (fileName));
            String line;
            int row = 1;
            line = glasses.readLine();
            while (line != null) {
                if (row % 4 == 1)
                    level = Integer.parseInt(line);

                if (row % 4 == 2)
                    name=line;

                if (row % 4 == 3)
                    score = Integer.parseInt(line);

                if (row % 4 == 0) {
                    timeScore = Integer.parseInt(line);
                    bestPlayers[level-1] = new Player(name, score, timeScore);
                }
                line = glasses.readLine();
                row++;
            }
            glasses.close();
        }
        catch (IOException e) {
            for (int i = 0; i < bestPlayers.length; i++)
            	//if there is problem with the file, we assume there was no data
                bestPlayers[i] = new Player("",-1,-1);
        }
    }



    //this is for outputting the current best players in the hall of fame
    void createFile(String fileName) {

        PrintWriter pen;
        try {
            pen = new PrintWriter(new FileOutputStream(fileName));

            for (int i = 0; i < bestPlayers.length; i++) {
                pen.println(i+1);
                pen.println(bestPlayers[i].getName());
                pen.println(bestPlayers[i].getScore());
                pen.println(bestPlayers[i].getTimeScore());
            }
            pen.flush();
            pen.close();
        }
        catch(IOException e) { }
    }

    //this method is used for quick setting of buttons
    JButton newbut(int x,int y, int width, int height, String str)
    {
        JButton b = new JButton(str);
        b.setBounds(x,y,width,height);
        this.getContentPane().add(b);
        b.addActionListener(this);
        return b;
    }

    //we just draw the rectangles here
    void drawrects(Graphics g) {
        if (level!=-1)
            for (int i = 0; i < (Group.getNumOp()*gr.size()); i++)
            {
                g.setColor(rect.elementAt(i).getColour());
                g.fillRect(rect.elementAt(i).getPos_x(), rect.elementAt(i).getPos_y(),
                			Rectangle.getLengthRect(), Rectangle.getHeightRect());
                g.setColor(Color.black);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                String nmbr="";
                //in these levels there are only whole numbers
                if (level==1||level==2||level==7||level==8)
                    nmbr=Integer.toString(rect.elementAt(i).getNum());
                else if (level==3||level==4)
                	//there are also decimal numbers with one decimal place
                    if ((rect.elementAt(i).getNum())>=0)
                        nmbr=Integer.toString((rect.elementAt(i).getNum())/10)+"."
                        		+Integer.toString((rect.elementAt(i).getNum())%10);
                    else nmbr="-"+Integer.toString(Math.abs(rect.elementAt(i).getNum())/10)
                    			+"."+Integer.toString(Math.abs(rect.elementAt(i).getNum())%10);
                else if (level==5||level==6)
                	//these are numbers with two decimal places
                    if ((rect.elementAt(i).getNum())>=0)
                        if (rect.elementAt(i).getNum() % 100<10)
                            nmbr=Integer.toString((rect.elementAt(i).getNum())/100)+".0"
                            		+Integer.toString((rect.elementAt(i).getNum())%100);
                        else nmbr=Integer.toString((rect.elementAt(i).getNum())/100)+"."
                            		+Integer.toString((rect.elementAt(i).getNum())%100);

                    else if (Math.abs(rect.elementAt(i).getNum())%100 <10)
                        nmbr="-"+Integer.toString(Math.abs(rect.elementAt(i).getNum())/100)
                        	+".0"+Integer.toString(Math.abs(rect.elementAt(i).getNum())%100);
                    else nmbr="-"+Integer.toString(Math.abs(rect.elementAt(i).getNum())/100)
                    		+"."+Integer.toString(Math.abs(rect.elementAt(i).getNum())%100);

                else nmbr=Integer.toString(rect.elementAt(i).getNum());
                drawCenteredString(nmbr, rect.elementAt(i).getPos_x(), rect.elementAt(i).getPos_y(),g);

            }
    }

    //we draw the groups
    void drawgroups(Graphics g)
    {
        if (level!=-1)
            for (int i = 0; i < gr.size(); i++)
            {
                g.setColor(Color.black);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                String nmbr="";
                //whole numbers
                if (level==1||level==2||level==7||level==8)
                    nmbr=Integer.toString(Group.getNum());
                //numbers with one decimal place
                else if (level==3||level==4)
                    if (Group.getNum()>=0)
                        nmbr=Integer.toString((Group.getNum())/10)+"."+Integer.toString((Group.getNum())%10);
                    else nmbr="-"+Integer.toString(Math.abs(Group.getNum())/10)+"."+Integer.toString(Math.abs(Group.getNum())%10);
                //numbers with two decimal places
                else if (level==5||level==6)
                    if ((Group.getNum())>=0)
                        if ((Group.getNum())%100<10)
                            nmbr=Integer.toString((Group.getNum())/100)+".0"+Integer.toString((Group.getNum())%100);
                        else nmbr=Integer.toString((Group.getNum())/100)+"."+Integer.toString((Group.getNum())%100);
                    else if (Math.abs(Group.getNum())%100<10)
                        nmbr="-"+Integer.toString(Math.abs(Group.getNum())/100)+".0"+Integer.toString(Math.abs(Group.getNum())%100);
                    else nmbr="-"+Integer.toString(Math.abs(Group.getNum())/100)+"."+Integer.toString(Math.abs(Group.getNum())%100);
                else nmbr=Integer.toString(Group.getNum());
                drawCenteredString(nmbr, gr.elementAt(i).getPos_x(), gr.elementAt(i).getPos_y(),g);
                FontMetrics fm = g.getFontMetrics();
                drawCenteredString("=", gr.elementAt(i).getPos_x()+fm.stringWidth(nmbr),gr.elementAt(i).getPos_y(),g);
                //these are the colours used for rectangles on given positions in groups
                Color [] colours = new Color [5];
                colours[0]=Color.green;
                colours[1]=Color.blue;
                colours[2]=Color.yellow;
                colours[3]=Color.cyan;
                colours[4]=Color.red;
                plussignsize=fm.stringWidth("+");
                beginrectgr=gr.elementAt(i).getPos_x()+fm.stringWidth(nmbr)+fm.stringWidth("=")+40;

                for (int k=0; k<Group.getNumOp()-1; k++)
                {
                    g.setColor(colours[k]);
                    g.fillRect(
                        gr.elementAt(i).getPos_x()+fm.stringWidth(nmbr)+fm.stringWidth("=")+40
                        	+k*(Rectangle.getLengthRect()+20)+k*fm.stringWidth("+"),
                        gr.elementAt(i).getPos_y(), Rectangle.getLengthRect(), Rectangle.getHeightRect());
                    g.setColor(Color.black);
                    drawCenteredString("+", gr.elementAt(i).getPos_x()+fm.stringWidth(nmbr)-5+fm.stringWidth("=")
                    		+(k+1)*(Rectangle.getLengthRect()+20)+k*fm.stringWidth("+"),gr.elementAt(i).getPos_y(),g);
                }
                int k=Group.getNumOp()-1;
                g.setColor(colours[k]);
                g.fillRect(gr.elementAt(i).getPos_x()+fm.stringWidth(nmbr)+fm.stringWidth("=")+40
                			+k*(Rectangle.getLengthRect()+20)+k*fm.stringWidth("+"),
                           gr.elementAt(i).getPos_y(), Rectangle.getLengthRect(), Rectangle.getHeightRect());

            }
    }

    //these are the initial settings regarding the intervals in which the random numbers may be created
    public void initset(int lev)
    {
        switch(lev)
        {
        case 1:  {
            mnn=0;
            mxx=20;
        }
        break;
        case 2:  {
            mnn=-20;
            mxx=20;
        }
        break;
        case 3:  {
            mnn=0;
            mxx=100;
        }
        break;
        case 4:  {
            mnn=-100;
            mxx=100;
        }
        break;
        case 5:  {
            mnn=0;
            mxx=1000;
        }
        break;
        case 6:  {
            mnn=-1000;
            mxx=1000;
        }
        break;
        case 7:  {
            mnn=0;
            mxx=1000000;
        }
        break;
        case 8:  {
            mnn=-1000000;
            mxx=1000000;
        }
        break;
        case 9:  {
            mnn=-1000;
            mxx=1000;
        }
        break;
        case 10:  {
            mnn=-1000000;
            mxx=1000000;
        }
        break;
        default:
            break;
        }
    }

    //only a method for painting the whole image
    public void paint(Graphics g) {
        BufferedImage bf = new BufferedImage( this.getWidth(),this.getHeight(), BufferedImage.TYPE_INT_RGB);
        screen(bf.getGraphics());
        g.drawImage(bf,0,0,null);
    }

    public void screen(Graphics g) {
        super.paint(g);
        drawgroups(g);
        drawrects(g);
    }

    //in this method we generate suitable numbers into the rectangles
    int [] generaterectnums()
    {
        int numrects=Group.getNumOp()*numofgr;
        int numOp=Group.getNumOp();
        int [] numbers = new int[numrects];//numbers of rectangles
        int [] groupnums = new int[numOp];
        int sumgroup=0;//this is the current sum of numbers in a given group
        Vector<Integer> numRectGen = new Vector<Integer>();
        numRectGen.clear();
        for (int i=0; i<(numrects/numOp); i++) //for each group of numbers do
        {
            boolean neew=false;
            //these are only positive numbers
            if (level==1||level==3 || level==5 || level==7)
                while (neew == false)
                {
                    sumgroup=0;
                    for (int k=0; k<numOp-1; k++)
                    {
                        groupnums[k]=(mnn + (int)(Math.random()*(Group.getNum()-mnn-sumgroup)));
                        //we adjust the interval and the sum of numbers created in the group
                        sumgroup=sumgroup+groupnums[k];
                    }
                    groupnums[numOp-1]=(Group.getNum()-sumgroup);
                    //last number is just created as the difference of the group sum
                    //and the sum of numbers created yet

                    neew=true;
                    //check if the created groups of rectangles are new in their colours
                    for (int o=0; o<(numRectGen.size()/numOp); o++)
                        for (int p=0; p<numOp; p++)
                            if (groupnums[p]==numRectGen.elementAt(o*numOp+p))
                            {
                                if (p==(numOp-1)) neew = false;
                            }
                            else break;
                }
            //these are both positive and negative numbers
            if (level==2||level==4||level==6||level==8)
                while (neew == false)
                {
                    sumgroup=0;
                    for (int k=0; k<numOp-1; k++)
                    {
                    	//we generate randomly the numbers in the given interval
                        groupnums[k]=(mnn + (int)(Math.random()*(2*mxx+1)));
                        sumgroup=sumgroup+groupnums[k];
                    }
                    //we calculate the remaining number based on the sum of all numbers in groups yet
                    groupnums[numOp-1]=(Group.getNum()-sumgroup);

                    if (groupnums[numOp-1]<(mxx+1) && groupnums[numOp-1]>(mnn-1))
                    	//we check if the last number is in the given interval - it should be, if it is not, we try again
                    {   neew=true;
                    	//check if the created numbers are truly new
                        for (int o=0; o<(numRectGen.size()/numOp); o++)
                            for (int p=0; p<numOp; p++)
                                if (groupnums[p]==numRectGen.elementAt(o*numOp+p))
                                {
                                    if (p==(numOp-1)) neew = false;
                                }
                                else break;
                    }
                }


            for (int u=0; u<numOp; u++)
                numRectGen.add(groupnums[u]);
        }

        //shuffle the numbers of the same colour so that they are rearranged when displayed
        for (int s=0; s<100; s++)
        {
            int cl=(int)(Math.random()*numOp);
            int n1=cl+numOp*(int)(Math.random()*(numrects/numOp));
            int n2=cl+numOp*(int)(Math.random()*(numrects/numOp));
            int temp = numRectGen.elementAt(n1);
            numRectGen.set(n1, numRectGen.elementAt(n2));
            numRectGen.set(n2, temp);
        }

        for (int r=0; r<numrects; r++)
            numbers[r]=numRectGen.elementAt(r);
        return numbers;
    }

    //this method just initializes the settings for each example
    void playgame()
    {
        background.setIcon(new ImageIcon("IAbackground2.png"));
        exnum++;
        numofexlb.setVisible(true);
        numofexlb.setText(vocab[29]+exnum+"/"+numofex);
        timelb.setVisible(true);
        time=0;
        timelb.setText(vocab[27] + ": " + time);

        if (truelevel==9)
        {
            level=1+(int)(Math.random()*6);
        }
        if (truelevel==10)
        {
            level=1+(int)(Math.random()*8);
        }
        initset(level);


        if (level==1)
            Group.setNumOp(2 + (int)(Math.random()*2));
        else Group.setNumOp(2 + (int)(Math.random()*4));
        //this numOp will be dependent on the level, e.g. in the first level there will be only maximally 3 numOp
        rect.clear();
        gr.clear();
        Rectangle.setHeightRect(40);
        Rectangle.setLengthRect(80);
        numofgr=3;//this is the number of groups used in the game, could be easily changed or given as random

        if (level==1||level==3 || level==5 || level==7)
            Group.setNum(3 + (int)(Math.random()*mxx-2));
        else if (level==2||level==4||level==6||level==8)
            	Group.setNum(mnn + (int)(Math.random()*(2*mxx+1)));
        	 else Group.setNum(3 + (int)(Math.random()*mxx-2));

        Color [] colours = new Color [5];
        colours[0]=Color.green;
        colours[1]=Color.blue;
        colours[2]=Color.yellow;
        colours[3]=Color.cyan;
        colours[4]=Color.red;
        int [] rectnums = new int[Group.getNumOp()*numofgr];

        rectnums=generaterectnums();

        //create the rectangles
        for (int i=0; i<(Group.getNumOp()*numofgr); i++)
        {
            Integer num=rectnums[i];
            Color colour = colours[i % Group.getNumOp()];
            int pos_x = 100+100*(i % Group.getNumOp());
            int pos_y = 400+(i/Group.getNumOp())*50;
            rect.add(new Rectangle(num,colour, pos_x,pos_y,pos_x,pos_y,i % Group.getNumOp()));
        }

        for (int i=0; i<numofgr; i++)
            gr.add(new Group(30,110+(i)*50));
        running=true;
        //start the timer
        tm.start();
        totnumop=totnumop+Group.getNumOp();
        repaint();
    }

    //manage what happens when the player clicks on various buttons or menuItems
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuItem1) {
            if (level != -1) {
                Object[] options = {vocab[3], vocab[4]};
                int n = JOptionPane.showOptionDialog(this,
                                                     vocab[7],//do you really want to start a new game? the current game will be lost.
                                                     vocab[6],//are you sure?
                                                     JOptionPane.YES_NO_CANCEL_OPTION,
                                                     JOptionPane.QUESTION_MESSAGE,
                                                     null,
                                                     options,
                                                     options[0]);
                if (n == 0) {
                	//start a new game
                    running=false;
                    tm.stop();
                    level=-1;
                    totnumop=0;
                    exnum=0;
                    timesc=0;
                    levcor=0;
                    levtot=0;
                    ChooseLevel levelwindow = new ChooseLevel(this, true);
                    levelwindow.setLocation(this.getLocationOnScreen().x+80,this.getLocationOnScreen().y+80);
                    levelwindow.setVisible(true);
                    levelwindow.repaint();
                    level=levelwindow.telllevel();
                    truelevel=level;
                    levlb.setText("Level: "+truelevel);
                    levlb.setVisible(true);
                    numofex=2 + (int)(Math.random()*6);
                    playgame();
                }
                else //do not start a new game
                    ;
            }
            else {
            	//we just start a new game - there was not any game previously running
            	//initial settings
                totnumop=0;//total number of operands yet
                exnum=0;//number of examples
                timesc=0;//time score is 0 at the beginning
                levcor=0;//number of correctly filled groups
                levtot=0;//number of all groups in the level yet
                ChooseLevel levelwindow = new ChooseLevel(this, true);
                levelwindow.setLocation(this.getLocationOnScreen().x+80,this.getLocationOnScreen().y+80);
                levelwindow.setVisible(true);
                levelwindow.repaint();
                level=levelwindow.telllevel();
                truelevel=level;
                levlb.setText("Level: "+truelevel);
                levlb.setVisible(true);
                numofex=2 + (int)(Math.random()*6);//we randomly generate the number of examples in the level
                playgame();

            }
        }

        //these are the rules
        if (e.getSource() == menuItem2)
            JOptionPane.showMessageDialog(this, vocab[8], vocab[1], JOptionPane.INFORMATION_MESSAGE);
        //we only display here the rules - it is a long and complicated html string

        //the hall of fame
        if (e.getSource() == menuItem3) {
            String halloffame="<html><table>";
            halloffame = halloffame + "<tr><td></td><td><b><i>"
            			+ vocab[11]+"</i></b></td><td><b><i>"
            			+ vocab[12] + "</i></b></td></tr>";
            for (int i = 0; i < bestPlayers.length; i++) {
                if (bestPlayers[i].getScore()==-1)
                    halloffame = halloffame + "<tr><td>"
                    			+ "Level: "+ (i+1) + "</td>   <td>"
                    			+ "" + "</td>   <td>" + "" + "</td> </tr>";
                else halloffame = halloffame + "<tr><td>"  + "Level: "
                    			+ (i+1) + "</td>   <td>" + bestPlayers[i].getName()
                    			+ "</td>   <td>" + bestPlayers[i].getScore() + "</td> </tr>";
            }
            halloffame = halloffame + "</table></html>";
            JOptionPane.showMessageDialog(this, halloffame, vocab[2], JOptionPane.PLAIN_MESSAGE);
        }

        //the button for checking the answers appears when all groups are filled
        //we use this button for checking the answers and managing what to do next
        if (e.getSource() == checkbt)
        {
            running=false;
            tm.stop();
            Vector<Integer> incorrectgroups=new Vector<Integer>();
            int corr=0;
            Vector<Integer> incorrectgrsums=new Vector<Integer>();

            int ssum=0;
            //this is the sum of rectangle numbers put into a particular group by the player
            for (int i=0; i<gr.size(); i++)
            {
                ssum=0;
                for (int h=0; h<Group.getNumOp(); h++)
                {
                    ssum=rect.elementAt(gr.elementAt(i).rects.elementAt(h)).getNum()+ssum;
                }
                //this below is used for calculating the score
                if (ssum==Group.getNum()) corr++;
                else {
                    incorrectgroups.add(i);
                    incorrectgrsums.add(ssum);
                }
            }
            //updating the statistics
            timesc=timesc+time;
            levcor=levcor+corr;
            levtot=levtot+gr.size();
            checkbt.setVisible(false);
            //notifying the player about the result
            String strresult = "<html>"+vocab[30]+corr+"/"+gr.size()+"<br><br>";
            if (corr==gr.size()) strresult=strresult+vocab[31]+"<br></html>";
            if (corr<gr.size())
            {
                strresult=strresult+vocab[32]+"<br>";
                strresult=strresult+"<table><tr><td><b><i>"+vocab[33]+"</i></b></td><td><b><i>" + vocab[34]+"</i></b></td><td><b><i>" +
                          vocab[35] + "</i></b></td></tr>";

                for (int r=0; r<gr.size()-corr; r++)
                {
                    String nmbr="";
                    String grnm="";
                    if (level==1||level==2||level==7||level==8)
                    {   nmbr=Integer.toString(incorrectgrsums.elementAt(r));
                        grnm=Integer.toString(Group.getNum());
                    }
                    else if (level==3||level==4)

                    {
                        if (Group.getNum()>=0)
                            grnm=Integer.toString((Group.getNum())/10)+"."+Integer.toString((Group.getNum())%10);

                        else	grnm="-"+Integer.toString(Math.abs(Group.getNum())/10)+"."+Integer.toString(Math.abs(Group.getNum())%10);

                        if (incorrectgrsums.elementAt(r)>=0)
                            nmbr=Integer.toString((incorrectgrsums.elementAt(r))/10)+"."+Integer.toString((incorrectgrsums.elementAt(r))%10);
                        else nmbr="-"+Integer.toString(Math.abs(incorrectgrsums.elementAt(r))/10)+"."+Integer.toString(Math.abs(incorrectgrsums.elementAt(r))%10);

                    }


                    else if (level==5||level==6)
                    {
                        if (Group.getNum()>=0)
                            if ((Group.getNum())%100<10)
                                grnm=Integer.toString((Group.getNum())/100)+".0"+Integer.toString((Group.getNum())%100);
                            else grnm=Integer.toString((Group.getNum())/100)+"."+Integer.toString((Group.getNum())%100);
                        else if (Math.abs(Group.getNum())%100<10)
                            grnm="-"+Integer.toString(Math.abs(Group.getNum())/100)+".0"+Integer.toString(Math.abs(Group.getNum())%100);
                        else grnm="-"+Integer.toString(Math.abs(Group.getNum())/100)+"."+Integer.toString(Math.abs(Group.getNum())%100);

                        if (incorrectgrsums.elementAt(r)>=0)
                            if ((incorrectgrsums.elementAt(r))%100<10)
                                nmbr=Integer.toString((incorrectgrsums.elementAt(r))/100)+".0"+Integer.toString((incorrectgrsums.elementAt(r))%100);
                            else nmbr=Integer.toString((incorrectgrsums.elementAt(r))/100)+"."+Integer.toString((incorrectgrsums.elementAt(r))%100);
                        else if (Math.abs(incorrectgrsums.elementAt(r))%100<10)
                            nmbr="-"+Integer.toString(Math.abs(incorrectgrsums.elementAt(r))/100)+".0"+Integer.toString(Math.abs(incorrectgrsums.elementAt(r))%100);
                        else nmbr="-"+Integer.toString(Math.abs(incorrectgrsums.elementAt(r))/100)+"."+Integer.toString(Math.abs(incorrectgrsums.elementAt(r))%100);
                    }
                    else {

                        nmbr=Integer.toString(incorrectgrsums.elementAt(r));
                        grnm=Integer.toString(Group.getNum());
                    }
                    strresult=strresult+"<tr><td>"  + (incorrectgroups.elementAt(r)+1) + "</td>   <td>" + nmbr +
                              "</td>   <td>" + grnm + "</td> </tr>";
                }
                strresult=strresult + "</table><br></html>";
            }
            JOptionPane.showMessageDialog(this,
                                          strresult,
                                          vocab[28],
                                          JOptionPane.INFORMATION_MESSAGE);
            //if this was the last example in the level
            if (exnum==numofex)
            {
                int percsc=levcor*100/levtot;//the percentage score of the player
                int timsc=10*timesc/totnumop;
                //if it is the best player
                if (bestPlayers[truelevel-1].getScore()<percsc)
                {
                	ImageIcon icon = new ImageIcon("winnericon.png");
                    //ask for name and change the best player at the given level
                    String finresult="<html>"+vocab[38]+percsc+"%.<br>"+ vocab[39]+timsc+".<br>"+vocab[36];
                    String plname = "";
                    plname = (String)JOptionPane.showInputDialog(
                                        MainClass.this,
                                        finresult,
                                        vocab[37],
                                        JOptionPane.PLAIN_MESSAGE,
                                        icon,
                                        null,
                                        null
                                        );

                    Player bestp=new Player(plname,percsc,timsc);
                    bestPlayers[truelevel-1]=bestp;
                }
                //if the score of this player is the same as of the current best player
                else if (bestPlayers[truelevel-1].getScore()==percsc)
                	//compare the time scores of both players
                    if (bestPlayers[truelevel-1].getTimeScore()>timsc)
                    {
                    	ImageIcon icon = new ImageIcon("winnericon.png");
                        String finresult="<html>"+vocab[38]+percsc+"%.<br>"+ vocab[39]+timsc+".<br><br>"+vocab[36];
                        String plname = "";
                        plname = (String)JOptionPane.showInputDialog(
                                            MainClass.this,
                                            finresult,
                                            vocab[37],
                                            JOptionPane.PLAIN_MESSAGE,
                                            icon,
                                            null,
                                            null);

                        Player bestp=new Player(plname,percsc,timsc);
                        bestPlayers[truelevel-1]=bestp;
                    }
                    else
                    {
                        String finresult="<html>" +vocab[38]+percsc+"%.<br>"+ vocab[39]+timsc+".<br>"+vocab[40]+"<br></html>";
                        JOptionPane.showMessageDialog(this,
                                                      finresult,
                                                      vocab[28],
                                                      JOptionPane.INFORMATION_MESSAGE);
                    }
                //just write the result and other options
                else
                {
                    String finresult="<html>" +vocab[38]+percsc+"%.<br>"+ vocab[39]+timsc+".<br>"+vocab[40]+"<br></html>";
                    JOptionPane.showMessageDialog(this,
                                                  finresult,
                                                  vocab[28],
                                                  JOptionPane.INFORMATION_MESSAGE);
                } //just write the result and other options

                if (truelevel<10)
                {
                	//give the player options what to do next
                    Object[] options = {vocab[41],vocab[47],vocab[42],vocab[43]};
                    int n = JOptionPane.showOptionDialog(this,
                                                         vocab[44],
                                                         vocab[45],
                                                         JOptionPane.YES_NO_OPTION,
                                                         JOptionPane.QUESTION_MESSAGE,
                                                         null,     //do not use a custom Icon
                                                         options,  //the titles of buttons
                                                         options[0]);
                    if (n == 0)//next level
                    {
                    	//initial settings
                    	exnum=0;
                        totnumop=0;
                        timesc=0;
                        levcor=0;
                        levtot=0;
                        truelevel=truelevel+1;
                        level=level+1;
                        levlb.setText("Level: "+truelevel);
                        numofex=2 + (int)(Math.random()*6);
                        playgame();
                    }
                    else if (n == 1)//current level again
                    {
                    	exnum=0;
                        totnumop=0;
                        timesc=0;
                        levcor=0;
                        levtot=0;
                        numofex=2 + (int)(Math.random()*6);
                        playgame();
                    }
                    else if (n == 2)//display the screen for choosing levels
                    {
                    	exnum=0;
                        level=-1;
                        totnumop=0;
                        timesc=0;
                        levcor=0;
                        levtot=0;
                        ChooseLevel levelwindow = new ChooseLevel(this, true);
                        levelwindow.setLocation(this.getLocationOnScreen().x+80,this.getLocationOnScreen().y+80);
                        levelwindow.setVisible(true);
                        levelwindow.repaint();
                        level=levelwindow.telllevel();
                        truelevel=level;
                        levlb.setText("Level: "+truelevel);
                        levlb.setVisible(true);
                        numofex=2 + (int)(Math.random()*6);
                        playgame();
                    }
                    else //go to the initial background
                    {
                        running=false;
                        level=-1;
                        levlb.setVisible(false);
                        timelb.setVisible(false);
                        numofexlb.setVisible(false);
                        background.setIcon(new ImageIcon(vocab[48]));
                        repaint();
                    }
                }

                else //if this is the tenth level - no other possibilities as checkbutton can be clicked on when there is only a level from 1 to 10
                {
                    Object[] options = {vocab[42],vocab[47],vocab[43]};
                    int n = JOptionPane.showOptionDialog(this,
                                                         vocab[46],
                                                         vocab[45],
                                                         JOptionPane.YES_NO_OPTION,
                                                         JOptionPane.QUESTION_MESSAGE,
                                                         null,     //do not use a custom Icon
                                                         options,  //the titles of buttons
                                                         options[0]);

                    if (n == 0)//another level
                    {
                    	exnum=0;
                        totnumop=0;
                        level=-1;
                        timesc=0;
                        levcor=0;
                        levtot=0;
                        ChooseLevel levelwindow = new ChooseLevel(this, true);
                        levelwindow.setLocation(this.getLocationOnScreen().x+80,this.getLocationOnScreen().y+80);
                        levelwindow.setVisible(true);
                        levelwindow.repaint();
                        level=levelwindow.telllevel();
                        truelevel=level;
                        levlb.setText("Level: "+truelevel);
                        levlb.setVisible(true);
                        numofex=2 + (int)(Math.random()*6);
                        playgame();
                    }
                    else if (n == 1)//tenth level again
                    {
                        exnum=0;
                        totnumop=0;
                        timesc=0;
                        levcor=0;
                        levtot=0;
                        mnn=0;
                        mxx=20;
                        numofex=2 + (int)(Math.random()*6);
                        playgame();
                    }
                    else
                    {
                    	//background
                    	running=false;
                        level=-1;
                        levlb.setVisible(false);
                        timelb.setVisible(false);
                        numofexlb.setVisible(false);
                        background.setIcon(new ImageIcon(vocab[48]));
                        repaint();
                    }
                }

            }
            else playgame();   //if it was not the last example in level, start a new example
        }
    }

    public static void main(String [] args) {
        MainClass window = new MainClass();
        window.readPlayersHall("BestPlayers.txt");
        window.setVisible(true);
        JOptionPane.showMessageDialog(window, vocab[9], vocab[10], JOptionPane.INFORMATION_MESSAGE);
    }

    public void mouseDragged(MouseEvent e) {
        if (running==true)
        {
            int x=e.getX();
            int y=e.getY();
            if (num!=-1) {
                try {
                    rect.elementAt(num).setPos_x(x-30);
                    rect.elementAt(num).setPos_y(y-20);
                    repaint();
                }
                catch(Exception eg)		{}
            }
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (running==true)
        {
        	//find the rectangle which was clicked on
            int x=e.getX();
            int y=e.getY();
            num=-1;
            for (int i=0; i<rect.size(); i++)
                if (rect.elementAt(i).getPos_x()<x)
                    if (rect.elementAt(i).getPos_x()+80>x)
                        if (rect.elementAt(i).getPos_y()<y)
                            if (rect.elementAt(i).getPos_y()+40>y)
                                num=i;
            //if the rectangle was in some group, put it away
            if (num!=-1)
            for (int i=0; i<gr.size(); i++)
                for (int h=0; h<Group.getNumOp(); h++)
                    if (num==gr.elementAt(i).rects.elementAt(h)) gr.elementAt(i).rects.set(h,-1);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //if the rectangle is in a group in a suitable colour position - leave it there
        if (running==true)
        {
            if (num!=-1)
            {
                int x=e.getX();
                int y=e.getY();
                boolean placed=false;
                for (int i=0; i<gr.size(); i++)
                {
                	//find if the rectangle was released in a group and right colour place
                    if ((beginrectgr+rect.elementAt(num).getNumcol()*(Rectangle.getLengthRect()+20+plussignsize))<x)
                        if ((beginrectgr+rect.elementAt(num).getNumcol()*(Rectangle.getLengthRect()+20+plussignsize))+80>x)
                            if (gr.elementAt(i).getPos_y()<y)
                                if (gr.elementAt(i).getPos_y()+40>y)
                                {
                                    if (gr.elementAt(i).rects.elementAt(rect.elementAt(num).getNumcol())!=-1) {
                                        rect.elementAt(gr.elementAt(i).rects.elementAt(rect.elementAt(num).getNumcol()))
                                        .setPos_x(rect.elementAt(gr.elementAt(i).rects.elementAt(rect.elementAt(num).getNumcol())).getOldpos_x());

                                        rect.elementAt(gr.elementAt(i).rects.elementAt(rect.elementAt(num).getNumcol()))
                                        .setPos_y(rect.elementAt(gr.elementAt(i).rects.elementAt(rect.elementAt(num).getNumcol())).getOldpos_y());
                                    }
                                    rect.elementAt(num).setPos_x(beginrectgr+rect.elementAt(num).getNumcol()*(Rectangle.getLengthRect()+20+plussignsize));
                                    rect.elementAt(num).setPos_y(gr.elementAt(i).getPos_y());
                                    placed=true;
                                    gr.elementAt(i).rects.set(rect.elementAt(num).getNumcol(),num);
                                }

                }


                if (placed==false) {
                    //if the rectangle is now put away from the group, write -1 onto the given place
                    for (int i=0; i<gr.size(); i++)
                    {
                        for (int h=0; h<Group.getNumOp(); h++)
                            if (num==gr.elementAt(i).rects.elementAt(h)) gr.elementAt(i).rects.set(h,-1);
                    }
                    //just return back
                    rect.elementAt(num).setPos_x(rect.elementAt(num).getOldpos_x());
                    rect.elementAt(num).setPos_y(rect.elementAt(num).getOldpos_y());
                }
                //check if all groups are filled - if yes display checkbutton
                boolean full=true;
                for (int i=0; i<gr.size(); i++)
                    for (int h=0; h<Group.getNumOp(); h++)
                        if (gr.elementAt(i).rects.elementAt(h)==-1)
                            full=false;
                if (full==true)
                    checkbt.setVisible(true);
                else checkbt.setVisible(false);
                num=-1;
                repaint();
            }
        }

    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }


}
