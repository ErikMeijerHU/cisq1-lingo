package nl.hu.cisq1.lingo.trainer.domain;


import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import nl.hu.cisq1.lingo.trainer.domain.exception.MaxGuessesReachedException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "round")
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //using this here and in game messes with ID's but it doesn't really affect anything
    private Long id;

    private String correctWord;
    private int attempts;
    @ElementCollection
    private List<Character> currentHint = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Feedback> feedbackList = new ArrayList<>();

    public Round(String correctWord) {
        this.correctWord = correctWord;
        currentHint.add(correctWord.charAt(0));
        for (int i = 0; i < correctWord.length() -1; i++){
            currentHint.add('.');
        }
    }

    public Round(){
        //empty constructor for spring
    }

    public void guess(String attempt) {
        if (attempts < 5){
            Feedback feedback = generateFeedback(attempt);
            feedbackList.add(feedback);
            List<Character> oldHint = currentHint;
            currentHint = feedback.giveHint(oldHint, correctWord);
            attempts++;
        }
        else{
            throw new MaxGuessesReachedException("Max amount of guesses reached");
        }
    }

    public Feedback generateFeedback(String attempt){
        List<Mark> marks = new ArrayList<>();
        if(correctWord.equals(attempt)){
            for(int i = 0; i < correctWord.length(); i++){
                marks.add(Mark.CORRECT);
            }
        }
        else if(correctWord.length()!=attempt.length()){
            for(int i = 0; i < attempt.length(); i++){
                marks.add(Mark.INVALID);
            }
        }
        else{
            List<Character> attemptLetters = new ArrayList<>();
            List<Character> answerLetters = new ArrayList<>();
            for(int i = 0; i < correctWord.length(); i++){
                answerLetters.add(correctWord.charAt(i));
                attemptLetters.add(attempt.charAt(i));
            }
            for(int i = 0; i < correctWord.length(); i++){
                if(attempt.charAt(i) == correctWord.charAt(i)){
                    marks.add(Mark.CORRECT);
                    attemptLetters.set(i, '.');
                    answerLetters.set(i, '.');
                }
                else{
                    marks.add(Mark.ABSENT);
                }
            }
            for(int i = 0; i < attemptLetters.size(); i++) {
                if(answerLetters.get(i) != '.' && answerLetters.contains(attemptLetters.get(i))){
                    marks.set(i, Mark.PRESENT);
                    answerLetters.set(answerLetters.indexOf(attemptLetters.get(i)), '.');
                    attemptLetters.set(i, '.');
                }
            }
        }

        Feedback feedback = new Feedback(attempt, marks);
        return feedback;
    }

    public List<Character> getCurrentHint() {
        return currentHint;
    }

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public int getAttempts() {
        return attempts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return attempts == round.attempts &&
                Objects.equals(correctWord, round.correctWord) &&
                Objects.equals(currentHint, round.currentHint) &&
                Objects.equals(feedbackList, round.feedbackList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correctWord, attempts, currentHint, feedbackList);
    }

    @Override
    public String toString() {
        return "Round{" +
                "id=" + id +
                ", correctWord='" + correctWord + '\'' +
                ", attempts=" + attempts +
                ", currentHint=" + currentHint +
                ", feedbackList=" + feedbackList +
                '}';
    }
}
