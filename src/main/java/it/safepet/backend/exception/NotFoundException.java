package it.safepet.backend.exception;

/**
 * 404 Not Found - quando un record non viene trovato nel database
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
