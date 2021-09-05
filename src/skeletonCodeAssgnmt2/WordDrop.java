package skeletonCodeAssgnmt2;

import javax.swing.JOptionPane;

public class WordDrop implements Runnable
{
    WordRecord[] wordRecord;
    int index;
    int noWords;
    int maxY;

    public WordDrop(WordRecord[] wrd,int index, int noWords, int maxY)
    {
        wordRecord = wrd;
        this.index = index;
        this.noWords = noWords;
        this.maxY = maxY;


    }


    @Override
    public void run()
    {
        while(WordApp.done != true)
        {
            
            // Checks if the word has been dropped or all words have been dropped while checking if it hasn't reached the bottom
            if(wordRecord[index].getY() < 480 || wordRecord[index].dropped() || (WordApp.wordsDropped.get() > 0))
            {
                wordRecord[index].drop(5);
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

                wordRecord[index].resetWord();
                


            }

            if(wordRecord[index].getY() == 480)
            {
                wordRecord[index].resetWord();//resets word
                WordApp.wordsDropped.getAndDecrement();//decrements total words dropped counter
                WordApp.score.missedWord();
                System.out.println(WordApp.wordsDropped.get());

            }

            if (WordApp.wordsDropped.get() < 0)
            {
                WordApp.done = true;   
                JOptionPane.showMessageDialog(WordApp.w,"Game Over! \n You Lose :(");
                
            }
    }
    }

    


}