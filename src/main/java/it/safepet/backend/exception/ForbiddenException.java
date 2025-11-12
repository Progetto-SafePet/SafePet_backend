package it.safepet.backend.exception;

/**
 * 403 Forbidden – quando l'utente è autenticato ma non ha i permessi necessari.
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
