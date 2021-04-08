package nl.hu.cisq1.lingo.trainer.presentation.controller;

import nl.hu.cisq1.lingo.trainer.application.GameService;
import nl.hu.cisq1.lingo.trainer.domain.exception.*;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/trainer")
public class GameController {
    private GameService service;

    public GameController(GameService service){
        this.service = service;
    }

    @PostMapping
    public GameDTO startGame(){
        return this.service.startGame();
    }

    @PostMapping("/{id}/new-round")
    public GameDTO newRound(@PathVariable("id") Long id){
        try{
            return this.service.newRound(id);
        }catch (GameNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (NoOngoingGameException | OngoingRoundException | WordLengthNotSupportedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/guess")
    public GameDTO guess(@PathVariable("id") Long id, @RequestParam String guess){
        try {
            return this.service.guess(id, guess);
        }catch (GameNotFoundException | WordDoesNotExistException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (MaxGuessesReachedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
