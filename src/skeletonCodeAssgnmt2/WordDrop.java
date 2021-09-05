package skeletonCodeAssgnmt2;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class WordDrop implements Runnable
{
    WordRecord[] wordRecord;
    int index;
    int noWords;
    int maxY;
    private JLabel caught;
    private JLabel missed;
    private JLabel scr;
    

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

    public void update()
    {
        missed.setText("Missed:" +  WordApp.score.getMissed()+ "    ");
        caught.setText("Caught: " + WordApp.score.getCaught() + "    ");
        scr.setText("Score:" +  WordApp.score.getScore()+ "    ");
    }

    @Override
    public void run()
    {
        int tInt = 0;
        // boolean finished = true;
        while(WordApp.done != true)
        {
            
            // Checks if the word has been dropped or all words have been dropped while checking if it hasn't reached the bottom
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
                WordApp.wordsCaught.getAndIncrement();
                wordRecord[index].resetWord();
                WordApp.score.caughtWord(wordRecord[index].getWord().length());
                System.out.println("Caught No: " +WordApp.wordsCaught.get());
                update();
                



            }

            if(wordRecord[index].getY() == 480)
            {
                wordRecord[index].resetWord();//resets word
                WordApp.wordsDropped.getAndDecrement();//decrements total words dropped counter
                WordApp.wordsCaught.getAndIncrement();
                System.out.println("Caught No: " +WordApp.wordsCaught.get());
                WordApp.score.missedWord();
                System.out.println(WordApp.wordsDropped.get());
                update();

            }

            if (WordApp.wordsDropped.get() < 0)
            {
                WordApp.done = true;   
                tInt = 2; 
                
            }

            if (WordApp.wordsCaught.get() >= WordApp.totalWords )
            {
                WordApp.done = true; 
                tInt = 1;   
            }
        }
        if(tInt == 2 && WordApp.finished )
        {
            WordApp.finished = false;
            JOptionPane.showMessageDialog(WordApp.w,"Game Over! \n You Lose :(", "Falling Words", JOptionPane.INFORMATION_MESSAGE);
        }

        if(tInt == 1 && WordApp.finished)
        {
            WordApp.finished = false;
            JOptionPane.showMessageDialog(WordApp.w,"Game Over! \n You Win :)", "Falling Words", JOptionPane.INFORMATION_MESSAGE);
        }
        if(WordApp.done)
        {
            WordPanel.stopThreads();
            System.out.println("We out");
        }
        
    }

    


}