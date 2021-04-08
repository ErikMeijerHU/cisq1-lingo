package nl.hu.cisq1.lingo.trainer.domain;


import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exception.MaxGuessesReachedException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoOngoingGameException;
import nl.hu.cisq1.lingo.trainer.domain.exception.OngoingRoundException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //using this here and in round messes with ID's but it doesn't really affect anything
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING_FOR_ROUND;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Round> rounds = new ArrayList<>();

    private int score;
    private int nextWordLength = 6;
    private int roundNumber = 0;

    public Game() {
        //empty constructor for spring
    }

    public void startGame(String correctWord){
        this.status = Status.PLAYING;
        this.rounds.add(new Round(correctWord));
        roundNumber++;
    }

    public void guess(String attempt){
        if(status == Status.PLAYING) {
            rounds.get(rounds.size()-1).guess(attempt);
            checkGameState();
        }
        else {
            throw new NoOngoingGameException("No game is running!");
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
            throw new OngoingRoundException("There is already a round active!");
        }
        else {
            throw new NoOngoingGameException("No game is running!");
        }
    }

    public void calculateScore(int attempts){
        score = score + (5*(5-attempts) + 5);
    }

    public void nextRound(){
        roundNumber++;
        if(nextWordLength < 7){nextWordLength++;}
        else {nextWordLength =5;}
    }

    public Long getId() {
        return id;
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

    public int getNextWordLength() {
        return nextWordLength;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return score == game.score &&
                nextWordLength == game.nextWordLength &&
                roundNumber == game.roundNumber &&
                status == game.status &&
                Objects.equals(rounds, game.rounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, rounds, score, nextWordLength, roundNumber);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", status=" + status +
                ", rounds=" + rounds +
                ", score=" + score +
                ", nextWordLength=" + nextWordLength +
                ", roundNumber=" + roundNumber +
                '}';
    }
}
