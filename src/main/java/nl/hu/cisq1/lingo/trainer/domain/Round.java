package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import nl.hu.cisq1.lingo.trainer.domain.exception.MaxGuessesReachedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Round {
    private String correctWord;
    private int attempts;

    private List<Character> currentHint = new ArrayList<>();
    private List<Feedback> feedbackList = new ArrayList<>();

    public Round(String correctWord) {
        this.correctWord = correctWord;
        currentHint.add(correctWord.charAt(0));
        for (int i = 0; i < correctWord.length() -1; i++){
            currentHint.add('.');
        }
    }

    public void guess(String attempt) throws MaxGuessesReachedException {
        if (attempts < 5){
            Feedback feedback = generateFeedback(attempt);
            feedbackList.add(feedback);
            List<Character> oldHint = currentHint;
            currentHint = feedback.giveHint(oldHint, correctWord);
            attempts++;
        }
        else{
            throw new MaxGuessesReachedException();
        }
    }

    public Feedback generateFeedback(String attempt){
        List<Mark> marks = new ArrayList<>();
        if(correctWord.equals(attempt)){
            for(int i = 0; i < correctWord.length(); i++){
                marks.add(Mark.CORRECT);
            }
        }
        else if(correctWord.length()!=attempt.length()){ //TODO add check if word exists
            for(int i = 0; i < attempt.length(); i++){
                marks.add(Mark.INVALID);
            }
        }
        else{
            List<Character> lettersToGuess = new ArrayList<>();
            for(int i = 0; i < correctWord.length(); i++){
                lettersToGuess.add(correctWord.charAt(i));
            }
            for(int i = 0; i < correctWord.length(); i++){
                if(attempt.charAt(i) == correctWord.charAt(i)){
                    marks.add(Mark.CORRECT);
                    lettersToGuess.set(i, '.');
                }
                else{
                    marks.add(Mark.ABSENT);
                }
            }
            for(int i = 0; i < lettersToGuess.size(); i++) {
                if(lettersToGuess.contains(attempt.charAt(i))){
                    marks.set(i, Mark.PRESENT);
                    lettersToGuess.remove(lettersToGuess.indexOf(attempt.charAt(i)));
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Round round = (Round) o;
//        return attempts == round.attempts &&
//                Objects.equals(correctWord, round.correctWord);
//    }
//
//    @Override
//    public String toString() {
//        return "Round{" +
//                "correctWord='" + correctWord + '\'' +
//                ", attempts=" + attempts +
//                '}';
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(correctWord, attempts);
//    }


}
