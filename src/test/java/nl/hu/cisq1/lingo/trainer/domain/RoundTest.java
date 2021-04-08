package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import nl.hu.cisq1.lingo.trainer.domain.exception.MaxGuessesReachedException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    private Round round;

    @BeforeEach
    @DisplayName("create test round")
    void init() {
        round = new Round("stoel");
    }

    @Test
    @DisplayName("hint should be automatically generated when a new round is created")
    void hintIsCreated(){
        //given
        List<Character> desiredHint = Arrays.asList('s','.','.','.','.');
        //when

        //then
        assertEquals(desiredHint, round.getCurrentHint());
    }

    @Test
    @DisplayName("guess is valid if the lengths are equal")
    void guessIsValid() throws MaxGuessesReachedException {
        //given

        //when
        round.guess("stoel");

        //then
        assertFalse(round.getFeedbackList().get(round.getFeedbackList().size()-1).isGuessInvalid());
    }

    @Test
    @DisplayName("guess is invalid if the lengths are not equal")
    void guessIsInvalid() throws MaxGuessesReachedException {
        //given

        //when
        round.guess("schoen");

        //then
        assertTrue(round.getFeedbackList().get(round.getFeedbackList().size()-1).isGuessInvalid());
    }

    @Test
    @DisplayName("guess does not count if max guesses has been reached")
    void maxGuessesReached() throws MaxGuessesReachedException {
        round.guess("staal");
        round.guess("staal");
        round.guess("staal");
        round.guess("staal");
        round.guess("staal");
        assertThrows(MaxGuessesReachedException.class, () -> round.guess("stoel"));
    }

    @Test
    @DisplayName("feedback should be all CORRECT if word is guessed")
    void correctGuess() throws MaxGuessesReachedException {
        round.guess("stoel");
        Feedback feedback = round.getFeedbackList().get(round.getFeedbackList().size()-1);

        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("feedback should only include one PRESENT if letter is in word multiple times")
    void presentGuess() throws MaxGuessesReachedException {
        round.guess("ooboo");
        Feedback feedback = round.getFeedbackList().get(round.getFeedbackList().size()-1);
        int presentcount = 0;
        for(Mark mark : feedback.getMarks()){
            if(mark == Mark.PRESENT){
                presentcount++;
            }
        }
        assertEquals(1, presentcount);
    }

    @Test
    @DisplayName("equals tested by EqualsVerifier")
    void equalsTrue(){
        EqualsVerifier.simple().forClass(Round.class).verify();
    }

    @Test
    @DisplayName("toString should return a formatted string")
    void formattedString(){
        assertEquals(round.toString(), "Round{id=null, correctWord='stoel', attempts=0, currentHint=[s, ., ., ., .], feedbackList=[]}");
    }
}