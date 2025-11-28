package it.safepet.backend.exception;

/**
 * 409 Conflict – quando si tenta di aggiungere un record già esistente in una tabella
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
