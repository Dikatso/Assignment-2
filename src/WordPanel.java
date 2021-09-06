/**
 * @author CSC DEPT UCT
 * @author Dikatso Moshweunyane
 * @version 6 Sept 2021
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
		private JLabel caught;
		private JLabel missed;
		private JLabel scr;
		static Thread wrt;

		
		public void paintComponent(Graphics g) {
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		   //draw the words
		   //animation must be added 
		    for (int i=0;i<noWords;i++){	    	
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()-10);	
		    }
		  }
		
		WordPanel(WordRecord[] words, int maxY,JLabel caught, JLabel missed,JLabel scr) {
			this.words=words; //will this work?
			noWords = words.length;
			done=false;
			this.maxY=maxY;		
			this.caught = caught;
			this.missed = missed;
			this.scr = scr;
		}
		
		/**
		 * Creates the different word threads and starts them
		*/
		public synchronized void run() {
			//add in code to animate this
			for (int i=0;i<noWords;i++)
			{
				Thread wrt = new Thread(new WordDrop(words, i, noWords, maxY,caught, missed, scr));
				wrt.start();
			}

			while(!WordApp.done)
			{	synchronized(this){
				repaint();
				}
			}
		}

	}


