package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
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
        GameDTO game = this.gameService.startGame();

        assertEquals(correctGameDTO, game);
    }

    @Test
    @DisplayName("word of unsupported length throws exception")
    void unsupportedWordLength(){
        //this test works because mock repository gives a word with length of 0
        assertThrows(WordLengthNotSupportedException.class, () -> this.gameService.startGame());
    }

    @Test
}
