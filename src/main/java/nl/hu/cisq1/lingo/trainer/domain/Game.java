package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exception.MaxGuessesReachedException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoOngoingGameException;
import nl.hu.cisq1.lingo.trainer.domain.exception.OngoingRoundException;

public class Game {
    private Status status = Status.WAITING_FOR_ROUND;
    private Round round;
    private int score;
    private int wordLength = 5;
    private int roundNumber = 0;

    public Game() {
        //TODO add comment here
    }

    public void startGame(String correctWord){
        this.status = Status.PLAYING;
        this.round = new Round(correctWord);
        roundNumber++;
        this.wordLength = correctWord.length();
    }

    public void guess(String attempt) throws MaxGuessesReachedException {
        if(status == Status.PLAYING) {
            round.guess(attempt);
            checkGameState();
        }
        else {
            throw new NoOngoingGameException();
        }
    }

    public Status checkGameState(){
        if(round.getFeedbackList().get(round.getFeedbackList().size()-1).isWordGuessed()){
            this.status = Status.WAITING_FOR_ROUND;
            calculateScore(round.getAttempts());
        }
        else if (round.getAttempts()>=5){
            this.status = Status.ELIMINATED;
        }
        else {
            this.status = Status.PLAYING;
        }
        return this.status;
    }

    public void newRound(String correctWord) {
        if(status == Status.WAITING_FOR_ROUND){
            round = new Round(correctWord);
            nextRound();
            status = Status.PLAYING;
        }else if(status == Status.PLAYING) {
            throw new OngoingRoundException();
        }
        else {
            throw new NoOngoingGameException();
        }
    }

    public void calculateScore(int attempts){
        score = score + (5*5-attempts) + 5;
    }

    public void nextRound(){
        roundNumber++;
        if(wordLength < 7){wordLength++;}
        else {wordLength =5;}
    }

    public Round getRound() {
        return round;
    }

    public Status getStatus() {
        return status;
    }
}
