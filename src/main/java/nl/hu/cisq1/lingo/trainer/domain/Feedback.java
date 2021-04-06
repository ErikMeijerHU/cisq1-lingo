package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Feedback {
    private String attempt;
    private List<Mark> marks;

    public Feedback(String attempt, List<Mark> marks) {
        this.attempt = attempt;
        this.marks = marks;
    }

    public boolean isWordGuessed(){
        return marks.stream().allMatch(letter -> letter == Mark.CORRECT);
    }

    public boolean isGuessInvalid(){
        return marks.contains(Mark.INVALID);
    }

    public List<Character> giveHint(List<Character> previousHint, String answer){
        List<Character> hint = new ArrayList<Character>();
        for (int i = 0; i < answer.length() ; i++){
            if(marks.get(i) == Mark.CORRECT){
                hint.add(attempt.charAt(i));
            }

            else if(previousHint.get(i) != '.'){
                hint.add(previousHint.get(i));
            }
            else{
                hint.add('.');
            }
        }
        return hint;
    }

    public List<Mark> getMarks() {
        return marks;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Feedback feedback = (Feedback) o;
//        return Objects.equals(attempt, feedback.attempt) &&
//                Objects.equals(marks, feedback.marks);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(attempt, marks);
//    }
//
//    @Override
//    public String toString() {
//        return "Feedback{" +
//                "attempt='" + attempt + '\'' +
//                ", marks=" + marks +
//                '}';
//    }
}
