package nl.hu.cisq1.lingo.trainer.presentation.dto;


import nl.hu.cisq1.lingo.trainer.domain.Feedback;

import java.util.List;
import java.util.Objects;


public class GameDTO {
    public Long id;
    public int score;
    public int roundNumber;
    public List<Character> hint;
    public List<Feedback> feedbackList;

    public GameDTO(Long id, int score, int roundNumber, List<Character> hint, List<Feedback> feedbackList) {
        this.id = id;
        this.score = score;
        this.roundNumber = roundNumber;
        this.hint = hint;
        this.feedbackList = feedbackList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDTO gameDTO = (GameDTO) o;
        return score == gameDTO.score &&
                roundNumber == gameDTO.roundNumber &&
                Objects.equals(hint, gameDTO.hint) &&
                Objects.equals(feedbackList, gameDTO.feedbackList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, roundNumber, hint, feedbackList);
    }

    @Override
    public String toString() {
        return "GameDTO{" +
                "id=" + id +
                ", score=" + score +
                ", roundNumber=" + roundNumber +
                ", hint=" + hint +
                ", feedbackList=" + feedbackList +
                '}';
    }
}
