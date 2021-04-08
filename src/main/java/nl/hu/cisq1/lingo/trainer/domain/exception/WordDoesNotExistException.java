package nl.hu.cisq1.lingo.trainer.domain.exception;

public class WordDoesNotExistException extends RuntimeException{
    public WordDoesNotExistException(String message){super(message);}
}
