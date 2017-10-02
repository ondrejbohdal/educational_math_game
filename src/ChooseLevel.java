import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ChooseLevel extends javax.swing.JDialog implements ActionListener {

    private static final long serialVersionUID = -675535308186554510L;
    
    private int answerlevel;
    JLabel header;
    JLabel [] lablevels;
    JButton [] butlevels;
    
    public ChooseLevel(java.awt.Frame parent, boolean modal)
    {
        super(parent,modal);
        setSize(600, 500);
        setLayout(null);
        lablevels = new JLabel [10];
        butlevels = new JButton [10];
        this.setTitle(MainClass.vocab[49]);
        header = newlab(10,10,400, 40, MainClass.vocab[13]);
        for (int i=0; i<10; i++)
        {
            butlevels[i] = newbut(10, 50+i*40,100,30,MainClass.vocab[15]+" "+(i+1));
            lablevels[i] = newlab(120,50+i*40,480,30, MainClass.vocab[16+i]);
        }
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    //accessor method to private field answerlevel
    public int telllevel() {
        return this.answerlevel;
    }
    
    //this method sets the value of answerlevel based on what button the user clicked to play a level
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == butlevels[0])
        {
            this.answerlevel=1;
            this.dispose();
        }
        if (e.getSource() == butlevels[1])
        {   this.answerlevel=2;
            this.dispose();
        }
        if (e.getSource() == butlevels[2])
        {
            this.answerlevel=3;
            this.dispose();
        }
        if (e.getSource() == butlevels[3])
        {
            this.answerlevel=4;
            this.dispose();
        }
        if (e.getSource() == butlevels[4])
        {
            this.answerlevel=5;
            this.dispose();
        }
        if (e.getSource() == butlevels[5])
        {
            this.answerlevel=6;
            this.dispose();
        }
        if (e.getSource() == butlevels[6])
        {
            this.answerlevel=7;
            this.dispose();
        }
        if (e.getSource() == butlevels[7])
        {
            this.answerlevel=8;
            this.dispose();
        }
        if (e.getSource() == butlevels[8])
        {
            this.answerlevel=9;
            this.dispose();
        }
        if (e.getSource() == butlevels[9])
        {
            this.answerlevel=10;
            this.dispose();
        }
    }

    //methods for creating labels and buttons
    JLabel newlab(int x, int y, int width, int height, String str) {
        JLabel l = new JLabel(str);
        l.setBounds(x, y, width,height);
        l.setVisible(true);
        this.getContentPane().add(l);
        return l;
    }

    JButton newbut(int x,int y, int width, int height, String str)
    {
        JButton b = new JButton(str);
        b.setBounds(x,y,width,height);
        this.getContentPane().add(b);
        b.addActionListener(this);
        return b;
    }

}
