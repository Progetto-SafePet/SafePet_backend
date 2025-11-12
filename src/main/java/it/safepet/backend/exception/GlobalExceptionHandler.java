package it.safepet.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
import java.time.Instant;

/**
 * Gestisce in modo centralizzato le eccezioni dell'applicazione
 * e restituisce risposte strutturate (RFC 7807 Problem Details).
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 400 Bad Request – errore di validazione o input non valido
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            IllegalArgumentException.class
    })
    public ProblemDetail handleBadRequest(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Bad Request");
        problem.setType(URI.create("https://httpstatuses.io/400"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * 401 Unauthorized – utente non autenticato
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorized(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Credenziali non valide.");
        problem.setTitle("Unauthorized");
        problem.setType(URI.create("https://httpstatuses.io/401"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * 403 Forbidden – accesso negato a risorse protette
     */
    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbidden(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Accesso negato.");
        problem.setTitle("Forbidden");
        problem.setType(URI.create("https://httpstatuses.io/403"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * 404 Not Found – risorsa non trovata
     */
    @ExceptionHandler({
            EntityNotFoundException.class
    })
    public ProblemDetail handleNotFound(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource Not Found");
        problem.setType(URI.create("https://httpstatuses.io/404"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * 409 Conflict – violazione di vincoli di integrità (es. chiavi duplicate)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleConflict(DataIntegrityViolationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Violazione di vincoli di integrità dei dati.");
        problem.setTitle("Conflict");
        problem.setType(URI.create("https://httpstatuses.io/409"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * Gestione esplicita di ResponseStatusException
     * (già contiene l’HttpStatus corretto)
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatus(ResponseStatusException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason());
        problem.setTitle(ex.getStatusCode().toString());
        problem.setType(URI.create("https://httpstatuses.io/" + ex.getStatusCode().value()));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * 500 Internal Server Error – fallback generico
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problem.setTitle("Internal Server Error");
        problem.setType(URI.create("https://httpstatuses.io/500"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
