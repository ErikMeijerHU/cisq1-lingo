package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exception.MaxGuessesReachedException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoOngoingGameException;
import nl.hu.cisq1.lingo.trainer.domain.exception.OngoingRoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    @DisplayName("create test game")
    void init() {
        game = new Game();
    }

    @Test
    @DisplayName("guess should do nothing if game has not been started")
    void guessWithoutOngoingGame() throws MaxGuessesReachedException {
        assertThrows(NoOngoingGameException.class, () -> game.guess("test"));
    }

    @Test
    @DisplayName("guess should work and give correct hint if game has been started")
    void guessWithOngoingGame() throws MaxGuessesReachedException {
        game.startGame("stoel");
        game.guess("staal");
        assertEquals(Arrays.asList('s','t','.','.','l'), game.getRound().getCurrentHint());
    }

    @Test
    @DisplayName("score should increase if guess is correct")
    void scoreIncrease() throws MaxGuessesReachedException {
        game.startGame("stoel");
        game.guess("stoel");
        assertTrue(game.getProgress().getScore() > 0);
    }

    @Test
    @DisplayName("guess should not work if previous guess is correct")
    void guessAfterWin() throws MaxGuessesReachedException {
        game.startGame("stoel");
        game.guess("stoel");
        assertThrows(NoOngoingGameException.class, () -> game.guess("test"));
    }

    @Test
    @DisplayName("status should be eliminated if max guesses has been reached")
    void guessAfterFail() throws MaxGuessesReachedException {
        game.startGame("stoel");
        game.guess("staal");
        game.guess("staal");
        game.guess("staal");
        game.guess("staal");
        game.guess("staal");
        assertEquals(game.getStatus(), Status.ELIMINATED);
    }

    @Test
    @DisplayName("length of word should increase by one when a new round starts")
    void newRoundLengthIncrease() throws MaxGuessesReachedException {
        game.startGame("stoel");
        game.guess("stoel");
        game.newRound("schoen");
        assertEquals(game.getProgress().getWordLength(), 6);
    }

    @Test
    @DisplayName("length of word should loop back around to 5 after reaching 7")
    void wordLengthResetToFive() throws MaxGuessesReachedException {
        game.startGame("scheren");
        game.guess("scheren");
        game.newRound("stoel");
        assertEquals(game.getProgress().getWordLength(), 5);
    }

    @Test
    @DisplayName("new round can be started when previous round has ended by losing")
    void newRoundAferLose() throws MaxGuessesReachedException {
        game.startGame("stoel");
        game.guess("staal");
        game.guess("staal");
        game.guess("staal");
        game.guess("staal");
        game.guess("staal");
        assertDoesNotThrow(() -> game.newRound("schoen"));
    }

    @Test
    @DisplayName("new round can be started when previous round has ended by winning")
    void newRoundAferWin() throws MaxGuessesReachedException {
        game.startGame("stoel");
        game.guess("stoel");
        assertDoesNotThrow(() -> game.newRound("schoen"));
    }

    @Test
    @DisplayName("no new round can start if round is still ongoing")
    void ongoingNewRound() {
        game.startGame("stoel");
        assertThrows(OngoingRoundException.class, () -> game.newRound("siroop"));
    }
}