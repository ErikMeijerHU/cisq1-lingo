package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.WordDoesNotExistException;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {
    private GameService gameService;

    private SpringGameRepository gameRepository;
    private SpringWordRepository wordRepository;

    @BeforeEach
    void init()
    {
        this.gameRepository = mock(SpringGameRepository.class);
        this.wordRepository = mock(SpringWordRepository.class);
        this.gameService = new GameService(this.gameRepository, this.wordRepository);
    }

    @Test
    @DisplayName("correct game progress dto should be returned when starting new game")
    void correctGameDTOOnStartingNewGame(){
        GameDTO correctGameDTO = new GameDTO(null, 0, 1, Arrays.asList('s','.','.','.','.'), Collections.emptyList());

        when(this.wordRepository.findRandomWordByLength(5)).thenReturn(Optional.of(new Word("stoel")));
        GameDTO gameDTO = this.gameService.startGame();

        assertEquals(correctGameDTO, gameDTO);
    }

    @Test
    @DisplayName("word of unsupported length throws exception")
    void unsupportedWordLength(){
        //this test works because mock repository gives a word with length of 0
        assertThrows(WordLengthNotSupportedException.class, () -> this.gameService.startGame());
    }

    @Test
    @DisplayName("starting a new round returns expected DTO")
    void newRoundCorrectDTO(){
        Game game = new Game();
        GameDTO correctGameDTO = new GameDTO(null, 0, 1, Arrays.asList('s','.','.','.','.','.'), Collections.emptyList());

        when(this.gameRepository.findById(0L)).thenReturn(Optional.of(game));
        when(this.wordRepository.findRandomWordByLength(6)).thenReturn(Optional.of(new Word("schoen")));

        assertEquals(this.gameService.newRound(0L), correctGameDTO);
    }

    @Test
    @DisplayName("starting a new round throws exception when word length unsupported")
    void newRoundWordLength(){
        Game game = new Game();
        GameDTO correctGameDTO = new GameDTO(null, 0, 1, Arrays.asList('s','.','.','.','.','.'), Collections.emptyList());

        when(this.gameRepository.findById((long) 0)).thenReturn(Optional.of(game));

        assertThrows(WordLengthNotSupportedException.class, () -> this.gameService.newRound((long) 0));
    }

    @Test
    @DisplayName("cannot start new round if no game was found")
    void newRoundNoGame(){
        assertThrows(GameNotFoundException.class, () -> this.gameService.newRound((long) 0));
    }

    @Test
    @DisplayName("guessing a word returns correct DTO")
    void guessCorrectDTO(){
        Game game = new Game();

        Feedback correctFeedback = new Feedback("schaar", Arrays.asList(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT));
        GameDTO correctGameDTO = new GameDTO(null, 0, 1, Arrays.asList('s','c','h','.','.', '.'), Arrays.asList(correctFeedback));

        when(this.gameRepository.findById((long) 0)).thenReturn(Optional.of(game));
        when(this.wordRepository.findRandomWordByLength(6)).thenReturn(Optional.of(new Word("schoen")));
        when(this.wordRepository.findWordByValue("schaar")).thenReturn(Optional.of(new Word("schaar")));
        this.gameService.newRound((long) 0);

        assertEquals(this.gameService.guess(0L, "schaar"), correctGameDTO);
    }

    @Test
    @DisplayName("guessing a word that does not exists throws exception")
    void guessWordNotExists(){
        Game game = new Game();
        when(this.gameRepository.findById((long) 0)).thenReturn(Optional.of(game));
        assertThrows(WordDoesNotExistException.class, () -> this.gameService.guess(0L, "staal"));
    }

    @Test
    @DisplayName("guessing a word should not work if no game was found")
    void guessWithoutGame(){
        assertThrows(GameNotFoundException.class, () -> this.gameService.guess(0L, "staal"));
    }

    @Test
    @DisplayName("toString should return a formatted string")
    void formattedString(){
        GameDTO gameDTO = new GameDTO(null, 0, 2, Arrays.asList('s','.','.','.','.', '.'), Collections.emptyList());
        assertEquals(gameDTO.toString(), "GameDTO{id=null, score=0, roundNumber=2, hint=[s, ., ., ., ., .], feedbackList=[]}");
    }
}
