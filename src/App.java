import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class App extends JPanel {

//changing these values will change the size of the game, while still remaining functional
//within the size limit specified.
static int windowWidth = 1300;
static int windowHeight = 800;

int randNumb = 0;
int squareWidth = 25;
int squareHeight = 25;
int squareYLocation = -squareWidth;
boolean numberCreated = false;
static boolean gameRunning = false;

//generates a random Y value inside the window for the square to spawn at
public void generateRandomNumber() {
    Random rand = new Random();
    randNumb = rand.nextInt(windowWidth - squareWidth);
    numberCreated = true;
}

//paints a black screen, then paints a rectangle on top of the black screen
public void paint(Graphics g) {
    g.setColor(Color.black);
    g.fillRect(0, 0, windowWidth, windowHeight);
    g.setColor(Color.BLUE);
    g.fillRect(randNumb, squareYLocation, squareWidth, squareHeight);
}

public void update() {

    //calls the generateRandomNumber() method which gives the square a random x value inside the screen
    if (!numberCreated) {
        generateRandomNumber();
    }
    //moves the squares y coordinate towards the bottom of the screen and stops once it hits the bottom
    if (squareYLocation <= windowHeight) {
        squareYLocation++;

        //resets the x and y location to a new position
    } else {
        numberCreated = false;
        squareYLocation = -squareHeight;
    }
}

//sets the while loop to true to start the game
public void start() {
    gameRunning = true;
}

public static void main(String[] args) throws InterruptedException {

    App game = new App();
    JFrame frame = new JFrame();
    frame.add(game);
    frame.setVisible(true);
    frame.setSize(windowWidth, windowHeight);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setTitle("Raining Squares");
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);

    game.start();

    //updates square position, repaints square, and slows down update and paint speed.
    while (gameRunning) {
        game.update();
        game.repaint();
        Thread.sleep(1);
    }
}
}


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
//model is separate from the view.

public class WordApp {
//shared variables
	static int noWords=4;
	static int totalWords;
        static AtomicInteger count;
        static AtomicBoolean startClicked;
        

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static WordRecord[] words;
	static volatile boolean done;  //must be volatile
	static 	Score score = new Score();
        static AtomicBoolean stopped;
        static AtomicInteger stopDropInt;
        static AtomicBoolean tbu;

	static WordPanel w;
	
	
	
       
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
    	JFrame frame = new JFrame("WordGame"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
 
    	
		w = new WordPanel(words,yLimit,score);
		w.setSize(frameX,yLimit+100);
                
	    g.add(w);
	    
	    
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    JLabel caught = new JLabel("Caught: " + score.getCaught() + "    ");
	    JLabel missed = new JLabel("Missed:" + score.getMissed()+ "    ");
	    JLabel scr = new JLabel("Score:" + score.getScore()+ "    ");    
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);
    
	    //[snip]
  
	    final JTextField textEntry = new JTextField("",20);
	   textEntry.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent evt) {
	          String text = textEntry.getText();
                 
	          //[snip]
                  
                  if(!done){ //check to see if game is actually done, should be triggered from threads though
                      for(int i = 0; i < words.length; i++){
                          if(words[i].matchWord(text)){
                              words[i].setMatched(true);
                              break;
                            }
                      }
                  }
                  
	          textEntry.setText("");
	          textEntry.requestFocus();
	      }
	    });
	   
           
	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize() );
	   g.add(txt);
	    
	    JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
	   	JButton startB = new JButton("Start");
		
			// add the listener to the jbutton to handle the "pressed" event
			startB.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  //[snip]
		    	  textEntry.requestFocus();  //return focus to the text entry field
                          if(startClicked.get() == false){ // if the start button has not been clicked yet, or if a game is completed or ended: reset intial variables
                            WordApp.done = false;
                            WordApp.stopped.set(false);
                            score.resetScore();
                            count.set(0);
                            Thread ww = new Thread(w);
                            stopDropInt.set(totalWords+1);
                            ww.start();
                            Thread scoreUpdater = new Thread(new ScoreUpdaterThread(caught, missed, scr, score));
                            scoreUpdater.start();
                            startClicked.set(true);
                          }
                          
                          
		      }
		    });
		JButton endB = new JButton("End");
			
				// add the listener to the jbutton to handle the "pressed" event
				endB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
			    	  //[snip]
                                  
                                  WordApp.stopped.set(true); // stops game over message from showing
                                  WordApp.done = true; // stops thread while loop checking
                                  startClicked.set(false); // allows start button to be clicked again
                                  score.resetScore(); //resets score object
                                  
                                  for(int i = 0; i < words.length; i++){
                                      
                                      words[i].resetWord(); //resets the words, ready for the next game
                                      
                                  }
			      }
			    });
                                
                JButton quitB = new JButton("Quit");
			
				// add the listener to the jbutton to handle the "pressed" event
				quitB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
			    	  
                                  System.exit(0);
                                  
			      }
			    });
                                
                JButton pauseB = new JButton("Pause"); //Pause Button feature
			
                // add the listener to the jbutton to handle the "pressed" event
                    pauseB.addActionListener(new ActionListener()
			{
			    public void actionPerformed(ActionEvent e)
                            {
			    	  
                              WordApp.done = true; // stops thread while loop checking
                              WordApp.stopped.set(true); // stops game over message from showing
                              startClicked.set(false); // allows start button to be clicked again
                                  
			    }
			});
                                
		
		b.add(startB);
                b.add(pauseB);
		b.add(endB);
                b.add(quitB);
		
		g.add(b);
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
       	//frame.pack();  // don't do this - packs it into small space
        frame.setVisible(true);

		
	}
       
	
public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();
			//System.out.println("read '" + dictLength+"'");

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;

	}

	public static void main(String[] args) {
    	
                count = new AtomicInteger(0);
                startClicked = new AtomicBoolean(false);
                stopped = new AtomicBoolean(false);
                stopDropInt = new AtomicInteger(0);
                tbu = new AtomicBoolean(false);
		//deal with command line arguments
		totalWords=Integer.parseInt(args[0]);  //total words to fall
		noWords=Integer.parseInt(args[1]); // total words falling at any point
                stopDropInt.set(Integer.parseInt(args[0])+1); // setting the stopdrop integer value to noWords
		assert(totalWords>=noWords); // this could be done more neatly
		String[] tmpDict=getDictFromFile(args[2]); //file of words
		if (tmpDict!=null)
			dict= new WordDictionary(tmpDict);
		
		WordRecord.dict=dict; //set the class dictionary for the words.
		
		words = new WordRecord[noWords];  //shared array of current words
		
		//[snip]
		
		setupGUI(frameX, frameY, yLimit);  
    	        //Start WordPanel thread - for redrawing animation

		int x_inc=(int)(frameX-120)/noWords;
	  	//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		}
	}

}
