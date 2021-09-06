/**
 * @author Dikatso Moshweunyane
 * @version 06 Sept 2021
 */

import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class WordDrop implements Runnable
{
    WordRecord[] wordRecord;
    int index;
    int noWords;
    int maxY;
    static private JLabel caught;
    static private JLabel missed;
    static private JLabel scr;
    
    /**
     * Class instatntiater responsible for the dropping of words
     * @param wrd
     * @param index
     * @param noWords
     * @param maxY
     * @param caught
     * @param missed
     * @param scr
     */
    public WordDrop(WordRecord[] wrd,int index, int noWords, int maxY,JLabel caught, JLabel missed,JLabel scr)
    {
        wordRecord = wrd;
        this.index = index;
        this.noWords = noWords;
        this.maxY = maxY;
        this.caught = caught;
        this.missed = missed;
        this.scr = scr;
    }
    /**
     * Updates the JLabels on the GUI
     */
    public static void update()
    {
        missed.setText("Missed:" +  WordApp.score.getMissed()+ "    ");
        caught.setText("Caught: " + WordApp.score.getCaught() + "    ");
        scr.setText("Score:" +  WordApp.score.getScore()+ "    ");
    }


    @Override
    /**
     * Responsible for the handling of multiple cases when the words are dropping and the final displayed message when game is finished
     */
    public void run()
    {
        
        while(WordApp.done != true) // Checks if the game is not done
        {
            
            if(wordRecord[index].getY() < 480 || wordRecord[index].dropped() || (WordApp.wordsDropped.get() > 0))   // Checks if word is still dropping
            {
                wordRecord[index].drop(15);
                try
                {
                    Thread.sleep(wordRecord[index].getSpeed());
                } 
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            if (wordRecord[index].isMatched())  // // Checks if the entered word is correct/matches with one of the dropping words
            {
                WordApp.catchSound();
                WordApp.wordsCaught.getAndIncrement();
                wordRecord[index].resetWord();
                WordApp.score.caughtWord(wordRecord[index].getWord().length());
                update();
            }

            if(wordRecord[index].getY() == 480) // Checks if a word has reached the red zone/bottom
            {
                WordApp.missSound();
                wordRecord[index].resetWord();//resets word
                WordApp.wordsDropped.getAndDecrement();//decrements total words dropped counter
                WordApp.wordsCaught.getAndIncrement();
                WordApp.score.missedWord();
                update();

            }

            if (WordApp.wordsDropped.get() < 0)
            {
                WordApp.done = true;   
                WordApp.tInt = 1; 
                
            }

            if (WordApp.wordsCaught.get() >= WordApp.totalWords )
            {
                WordApp.done = true; 
                 WordApp.tInt = 1;   
            }
        }
        if(WordApp.tInt == 1 && WordApp.finished)   // Prints Game Over message when game is over
        {
            WordApp.endGameSounds();
            WordApp.finished = false;
            JOptionPane.showMessageDialog(WordApp.w,"*********   GAME  OVER   ********* \n \n Your Results Are As Follows: \n Total Points - " + WordApp.score.getScore()+"\n Total Words Caught - " + WordApp.score.getCaught()+"\n Total Words Missed - " + WordApp.score.getMissed() + "\n \n ****** PRESS QUIT TO EXIT ******" , "Falling Words", JOptionPane.INFORMATION_MESSAGE);            WordApp.wordsCaught.set(0);
			WordApp.wordsDropped.set(0);
			WordApp.score.resetScore();
        } 
        
    }

    


}