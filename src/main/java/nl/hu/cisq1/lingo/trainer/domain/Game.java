package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exception.MaxGuessesReachedException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoOngoingGameException;
import nl.hu.cisq1.lingo.trainer.domain.exception.OngoingRoundException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING_FOR_ROUND;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Round> rounds = new ArrayList<>();

    private int score;
    private int wordLength = 5;
    private int roundNumber = 0;

    public Game() {
        //empty constructor for spring
    }

    public void startGame(String correctWord){
        this.status = Status.PLAYING;
        this.rounds.add(new Round(correctWord));
        roundNumber++;
        this.wordLength = correctWord.length();
    }

    public void guess(String attempt) throws MaxGuessesReachedException {
        if(status == Status.PLAYING) {
            rounds.get(rounds.size()-1).guess(attempt);
            checkGameState();
        }
        else {
            throw new NoOngoingGameException();
        }
    }

    public Status checkGameState(){
        if(rounds.get(rounds.size()-1).getFeedbackList().get(rounds.get(rounds.size()-1).getFeedbackList().size()-1).isWordGuessed()){
            this.status = Status.WAITING_FOR_ROUND;
            calculateScore(rounds.get(rounds.size()-1).getAttempts());
        }
        else if (rounds.get(rounds.size()-1).getAttempts()>=5){
            this.status = Status.ELIMINATED;
        }
        else {
            this.status = Status.PLAYING;
        }
        return this.status;
    }

    public void newRound(String correctWord) {
        if(status == Status.WAITING_FOR_ROUND){
            rounds.add(new Round(correctWord));
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

    public List<Round> getRounds() {
        return rounds;
    }

    public Status getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }

    public int getWordLength() {
        return wordLength;
    }
}
