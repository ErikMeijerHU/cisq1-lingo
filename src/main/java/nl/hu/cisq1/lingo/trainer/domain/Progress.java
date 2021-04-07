package nl.hu.cisq1.lingo.trainer.domain;

public class Progress {
    private int score = 0;
    private int roundNumber = 0;
    private int wordLength = 5;

    public Progress() {

    }

    public void calculateScore(int attempts){
        score = score + (5*5-attempts) + 5;
    }

    public void nextRound(){
        roundNumber++;
        if(wordLength < 7){wordLength++;}
        else {wordLength =5;}
    }

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    public int getScore() {
        return score;
    }

    public int getWordLength() {
        return wordLength;
    }
}
