package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed(){
        //Arrange (given)

        //Act (when)
        Feedback feedback = new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        //Assert (then)
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word not is guessed if not all letters are correct")
    void wordIsNotGuessed(){
        //Arrange (given)

        //Act (when)
        Feedback feedback = new Feedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        //Assert (then)
        assertFalse(feedback.isWordGuessed());
    }


    @Test
    @DisplayName("guess is invalid if word does not exist or word is not the right amount of letters")
    void guessIsInvalid(){
        //Arrange (given)

        //Act (when)
        Feedback feedback = new Feedback("woord", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        //Assert (then)
        assertTrue(feedback.isGuessInvalid());
    }

    @Test
    @DisplayName("guess is not invalid if the guessed word exists and is of right length")
    void guessIsNotInvalid(){
        //Arrange (given)

        //Act (when)
        Feedback feedback = new Feedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT));
        //Assert (then)
        assertFalse(feedback.isGuessInvalid());
    }
}