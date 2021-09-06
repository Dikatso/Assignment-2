

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.Scanner;
import java.io.FileInputStream;
//model is separate from the view.

public class WordApp {
//shared variables
	static int noWords=4;
	static int totalWords;

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static AtomicInteger wordsDropped = new AtomicInteger(0);
	static AtomicInteger wordsCaught = new AtomicInteger(0);
	static int tInt = 0;

	static WordRecord[] words;
	static volatile boolean done;  //must be volatile
	static 	Score score = new Score();
	static String text;
	static boolean finished = true;

	static WordPanel w;
	static Thread ww;
	
	public static void endGameSounds()
    {
        String soundName = "./data/gameoverFinal1.wav";    
            AudioInputStream audioInputStream =null;
            try {
                audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            } catch (UnsupportedAudioFileException | IOException e2) {
                e2.printStackTrace();
            }
            Clip clip = null;
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e1) {
                e1.printStackTrace();
            }
            try {
                clip.open(audioInputStream);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        clip.start();

    }
	public static void missSound()
    {
        String soundName2 = "./data/wrongL.wav";    
            AudioInputStream audioInputStream2 =null;
            try {
                audioInputStream2 = AudioSystem.getAudioInputStream(new File(soundName2).getAbsoluteFile());
            } catch (UnsupportedAudioFileException | IOException e2) {
                e2.printStackTrace();
            }
            Clip clip2 = null;
            try {
                clip2 = AudioSystem.getClip();
            } catch (LineUnavailableException e1) {
                e1.printStackTrace();
            }
            try {
                clip2.open(audioInputStream2);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        clip2.start();
    }
	public static void catchSound()
    {
        String soundName1 = "./data/correctL.wav";    
            AudioInputStream audioInputStream1 =null;
            try {
                audioInputStream1 = AudioSystem.getAudioInputStream(new File(soundName1).getAbsoluteFile());
            } catch (UnsupportedAudioFileException | IOException e2) {
                e2.printStackTrace();
            }
            Clip clip1 = null;
            try {
                clip1 = AudioSystem.getClip();
            } catch (LineUnavailableException e1) {
                e1.printStackTrace();
            }
            try {
                clip1.open(audioInputStream1);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        clip1.start();
    }
	public static void setupGUI(int frameX,int frameY,int yLimit)
	{
		// Frame init and dimensions
		JLabel caught =new JLabel("Caught: " + score.getCaught() + "    ");
		JLabel missed =new JLabel("Missed:" + score.getMissed()+ "    ");
		JLabel scr =new JLabel("Score:" + score.getScore()+ "    "); 
		JFrame frame = new JFrame("WordGame"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
      	JPanel g = new JPanel();
		g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
		g.setSize(frameX,frameY);
    	
		w = new WordPanel(words,yLimit,caught,missed,scr);
		w.setSize(frameX,yLimit+100);
	   	g.add(w); 
	    
      	JPanel txt = new JPanel();
      	txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
         
		txt.add(caught);
	   	txt.add(missed);
	   	txt.add(scr);
    
	    //[snip]
  
	   	final JTextField textEntry = new JTextField("",20);
	   	textEntry.addActionListener(new ActionListener()
	   	{
			public void actionPerformed(ActionEvent evt)
		  	{
	        	String text = textEntry.getText();
				int tempInt =0;
				//[snip]
			
				while( tempInt < words.length)
				{
					if(words[tempInt].matchWord(text))
					{
						words[tempInt].matchTrue();
						tempInt++;  
					}
					else
					{
						words[tempInt].matchFalse();
					}
					tempInt++;
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
	   	JButton startB = new JButton("Start");;
		startB.setBackground(Color.RED);
		startB.setForeground(Color.BLACK);
		
			// add the listener to the jbutton to handle the "pressed" event
		startB.addActionListener(new ActionListener()
		{
		   public void actionPerformed(ActionEvent e)
		   {
		      //[snip]
			  done = false;
			  ww = new Thread(w);
			  ww.start();
		      textEntry.requestFocus();  //return focus to the text entry field
		   }
		});
		JButton endB = new JButton("End");;
		endB.setBackground(Color.RED);
		endB.setForeground(Color.BLACK);
			
				// add the listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener()
		{
		   public void actionPerformed(ActionEvent e)
		   {
			done = true; // stops thread while loop checking
			wordsCaught.set(0);
			wordsDropped.set(0);
			score.resetScore();
			tInt = 0; //resets score object
			
			for(int i = 0; i < noWords; i++)
			{
				words[i].resetWord(); //resets the words, ready for the next game
				
			}
			WordDrop.update();
			ww.interrupt();
		   }
		});

		JButton quitButton = new JButton("Quit");;
		quitButton.setBackground(Color.RED);
		quitButton.setForeground(Color.BLACK);
			
		// add the listener to the jbutton to handle the "pressed" event
		quitButton.addActionListener(new ActionListener()
		{
		   public void actionPerformed(ActionEvent e)
		   {
				System.exit(0);
		   }
		});
		
		b.add(startB);
		b.add(endB);
		b.add(quitButton);
		
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
    	
		//deal with command line arguments
		totalWords=Integer.parseInt(args[0]);  //total words to fall
		noWords=Integer.parseInt(args[1]); // total words falling at any point
		wordsDropped.set(totalWords);
		assert(totalWords>=noWords); // this could be done more neatly
		System.out.println(args[2]);
		String[] tmpDict=getDictFromFile(args[2]); //file of words
		// ./data/example_dict.txt
		if (tmpDict!=null) dict= new WordDictionary(tmpDict);
		WordRecord.dict=dict; //set the class dictionary for the words.
		
		words = new WordRecord[noWords];  //shared array of current words
		
		setupGUI(frameX, frameY, yLimit);  
    	//Start WordPanel thread - for redrawing animation

		int x_inc=(int)frameX/noWords;
	  	//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		}
	}
}