package it.safepet.backend.exception;

/**
 * 401 Unauthorized – quando l'utente non è autenticato.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
