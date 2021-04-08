package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.MaxGuessesReachedException;
import nl.hu.cisq1.lingo.trainer.domain.exception.WordDoesNotExistException;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotSupportedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameService {
    private final SpringGameRepository gameRepository;
    private final SpringWordRepository wordRepository;

    public GameService(final SpringGameRepository gameRepository, final SpringWordRepository wordRepository){
        this.gameRepository = gameRepository;
        this.wordRepository = wordRepository;
    }

    public GameDTO startGame(){
        Game game = new Game();
        Word word = this.wordRepository.findRandomWordByLength(5).orElseThrow(() -> new WordLengthNotSupportedException(5));

        game.startGame(word.getValue());
        this.gameRepository.save(game);
        return this.mapGameToDTO(game);

    }

    public GameDTO newRound(Long id){
        Game game = this.findGameById(id);
        Word word = this.wordRepository.findRandomWordByLength(game.getNextWordLength()).orElseThrow(() -> new WordLengthNotSupportedException(game.getNextWordLength()));

        game.newRound(word.getValue());
        this.gameRepository.save(game);
        return this.mapGameToDTO(game);
    }

    public GameDTO guess(Long id, String guess) {
        Game game = this.findGameById(id);

        //check if word exists
        if(!this.wordRepository.findWordByValue(guess).isEmpty()){
            game.guess(guess);
            this.gameRepository.save(game);
            return this.mapGameToDTO(game);
        }
        else{
            throw new WordDoesNotExistException("Word " + guess + " does not exist.");
        }
    }

    public Game findGameById(Long id){
        return this.gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException("Game with id "+id+" not found"));
    }

    public GameDTO mapGameToDTO(Game game){
        List<Character> currentHint = new ArrayList<>();
        List<Feedback> feedbackList = new ArrayList<>();

        if (game.getRounds().size()>0){
            Round round = game.getRounds().get(game.getRounds().size()-1);
            feedbackList = round.getFeedbackList();
            currentHint = round.getCurrentHint();
        }

        return new GameDTO(game.getId(), game.getScore(), game.getRoundNumber(), currentHint, feedbackList);
    }
}
