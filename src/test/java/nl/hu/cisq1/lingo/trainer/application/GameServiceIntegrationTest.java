package nl.hu.cisq1.lingo.trainer.application;


import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(CiTestConfiguration.class)
public class GameServiceIntegrationTest {

    @Autowired
    private SpringGameRepository gameRepository;

    @MockBean
    private SpringWordRepository wordRepository;

    @Autowired
    private GameService service;

    private Game game;


    @BeforeEach
    @DisplayName("setup before each test")
    void init(){
        this.game = new Game();
        game.startGame("stoel");
        this.gameRepository.save(game);
    }

    @AfterEach
    @DisplayName("cleanup after each test")
    void teardown(){
        this.gameRepository.deleteById(game.getId());
    }

    @ParameterizedTest
    @MethodSource("provideGuesses")
    @DisplayName("guessing a word correctly should increase score correctly")
    void guessingAWord(List<String> guesses, int expectedScore){
        GameDTO gameDTO = null;
        when(this.wordRepository.findWordByValue("steen")).thenReturn(Optional.of(new Word("steen")));
        when(this.wordRepository.findWordByValue("staal")).thenReturn(Optional.of(new Word("staal")));
        when(this.wordRepository.findWordByValue("stoel")).thenReturn(Optional.of(new Word("stoel")));

        for(String guess : guesses) {
            gameDTO = this.service.guess(game.getId(), guess);
        }

        assertEquals(expectedScore, gameDTO.score);
    }

    private static Stream<Arguments> provideGuesses() {
        return Stream.of(
                Arguments.of(Arrays.asList("steen", "staal", "stoel"), 15),
                Arguments.of(Arrays.asList("staal", "stoel"), 20),
                Arguments.of(Arrays.asList("stoel"), 25)
        );
    }
}
