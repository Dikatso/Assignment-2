package skeletonCodeAssgnmt2;

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
        
            if(wordRecord[index].getY() < 480)
            {
                wordRecord[index].drop(5);
                try {
                    Thread.sleep(wordRecord[index].getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
    }
    }

    


}