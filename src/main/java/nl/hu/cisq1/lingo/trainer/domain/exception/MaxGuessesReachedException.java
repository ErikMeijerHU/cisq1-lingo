package nl.hu.cisq1.lingo.trainer.domain.exception;

public class MaxGuessesReachedException extends RuntimeException{
    public MaxGuessesReachedException(String message){super(message);}
}
