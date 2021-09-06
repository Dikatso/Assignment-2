

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

    public static void update()
    {
        missed.setText("Missed:" +  WordApp.score.getMissed()+ "    ");
        caught.setText("Caught: " + WordApp.score.getCaught() + "    ");
        scr.setText("Score:" +  WordApp.score.getScore()+ "    ");
    }

    @Override
    public void run()
    {
        
        while(WordApp.done != true)
        {
            
            if(wordRecord[index].getY() < 480 || wordRecord[index].dropped() || (WordApp.wordsDropped.get() > 0))
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

            if (wordRecord[index].isMatched())
            {
                WordApp.catchSound();
                WordApp.wordsCaught.getAndIncrement();
                wordRecord[index].resetWord();
                WordApp.score.caughtWord(wordRecord[index].getWord().length());
                update();
            }

            if(wordRecord[index].getY() == 480)
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
        if(WordApp.tInt == 1 && WordApp.finished)
        {
            WordApp.endGameSounds();
            WordApp.finished = false;
            JOptionPane.showMessageDialog(WordApp.w,"*********   GAME  OVER   ********* \n \n Your Results Are As Follows: \n Total Points - " + WordApp.score.getScore()+"\n Total Words Caught - " + WordApp.score.getCaught()+"\n Total Words Missed - " + WordApp.score.getMissed() + "\n \n ****** PRESS QUIT TO EXIT ******" , "Falling Words", JOptionPane.INFORMATION_MESSAGE);            WordApp.wordsCaught.set(0);
			WordApp.wordsDropped.set(0);
			WordApp.score.resetScore();
        } 
        
    }

    


}