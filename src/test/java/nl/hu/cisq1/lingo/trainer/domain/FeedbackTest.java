package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed(){
        //given

        //when
        Feedback feedback = new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        //then
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word not is guessed if not all letters are correct")
    void wordIsNotGuessed(){
        //given

        //when
        Feedback feedback = new Feedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        //then
        assertFalse(feedback.isWordGuessed());
    }


    @Test
    @DisplayName("guess is invalid if word does not exist or word is not the right amount of letters")
    void guessIsInvalid(){
        //given

        //when
        Feedback feedback = new Feedback("woord", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        //then
        assertTrue(feedback.isGuessInvalid());
    }

    @Test
    @DisplayName("guess is not invalid if the guessed word exists and is of right length")
    void guessIsNotInvalid(){
        //given

        //when
        Feedback feedback = new Feedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT));
        //then
        assertFalse(feedback.isGuessInvalid());
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("hint is correct if the characters and points are in the correct position according to the attempt and the final word")
    void hintIsCorrect(List<Character> previousHint, Feedback feedback, String correctAnswer, List<Character> desiredHint){
        assertEquals(desiredHint, feedback.giveHint(previousHint, correctAnswer));
    }

    private static Stream<Arguments> provideHintExamples() {

        String correctAnswer = "pasta";
        List<Character> previousHint = Arrays.asList('.', 'a', '.', '.', '.');


        Feedback invalidFeedback = new Feedback("paasen", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        //should return:
        List<Character> invalidHint = Arrays.asList('.', 'a', '.', '.', '.');

        Feedback presentFeedback = new Feedback("paard", List.of(Mark.CORRECT, Mark.CORRECT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT));
        //should return:
        List<Character> presentHint = Arrays.asList('p', 'a', '.', '.', '.');

        Feedback correctFeedback = new Feedback("pasta", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        //should return:
        List<Character> correctHint = Arrays.asList('p', 'a', 's', 't', 'a');

        return Stream.of(
                Arguments.of(previousHint, invalidFeedback, correctAnswer, invalidHint),
                Arguments.of(previousHint, presentFeedback, correctAnswer, presentHint),
                Arguments.of(previousHint, correctFeedback, correctAnswer, correctHint)
        );
    }
}
