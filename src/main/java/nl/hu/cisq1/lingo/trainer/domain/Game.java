package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exception.MaxGuessesReachedException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoOngoingGameException;
import nl.hu.cisq1.lingo.trainer.domain.exception.OngoingRoundException;

public class Game {
    private Status status = Status.WAITING_FOR_ROUND;
    private Round round;
    private Progress progress;

    public Game() {}

    public void startGame(String correctWord){
        this.status = Status.PLAYING;
        this.round = new Round(correctWord);
        this.progress = new Progress();
        progress.setWordLength(correctWord.length());
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

    public void checkGameState(){
        if(round.getFeedbackList().get(round.getFeedbackList().size()-1).isWordGuessed()){
            this.status = Status.WAITING_FOR_ROUND;
            progress.calculateScore(round.getAttempts());
        }
        else if (round.getAttempts()>=5){
            this.status = Status.ELIMINATED;
        }
        else {
            this.status = Status.PLAYING;
        }
    }

    public void newRound(String correctWord) {
        if(status == Status.WAITING_FOR_ROUND || status == Status.ELIMINATED){
            round = new Round(correctWord);
            progress.nextRound();
            status = Status.PLAYING;
        }else {
            throw new OngoingRoundException();
        }
    }

    public Round getRound() {
        return round;
    }

    public Progress getProgress() {
        return progress;
    }

    public Status getStatus() {
        return status;
    }
}
