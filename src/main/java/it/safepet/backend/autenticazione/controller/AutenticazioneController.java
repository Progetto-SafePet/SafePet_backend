package it.safepet.backend.autenticazione.controller;

import it.safepet.backend.autenticazione.dto.AuthResponseDTO;
import it.safepet.backend.autenticazione.dto.LoginRequestDTO;
import it.safepet.backend.autenticazione.service.AutenticazioneService;
import it.safepet.backend.exception.UnauthorizedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AutenticazioneController {
    @Autowired
    private AutenticazioneService autenticazioneService;

    /**
     * Autentica un utente esistente e restituisce un token JWT valido.
     *
     * <p><b>Metodo:</b> POST<br>
     * <b>Endpoint:</b> /auth/login<br>
     * <b>Content-Type:</b> application/json</p>
     *
     * <p><b>Corpo richiesta (JSON):</b></p>
     * <pre>
     * {
     *   "email": "luca.rossi@mail.com",
     *   "password": "password123"
     * }
     * </pre>
     *
     * <p><b>Esempio risposta (200 OK):</b></p>
     * <pre>
     * {
     *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     *   "id": 1,
     *   "email": "luca.rossi@mail.com"
     * }
     * </pre>
     *
     * @param loginRequestDTO credenziali dell'utente (email e password)
     * @return {@link AuthResponseDTO} contenente il token JWT e i dati dell'utente autenticato
     * @see AuthResponseDTO
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        AuthResponseDTO responseDTO = autenticazioneService.login(loginRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Gestisce i fallimenti di autenticazione e restituisce un 401 Unauthorized.
     * @param ex l'eccezione catturata (UnauthorizedException o EntityNotFoundException)
     * @return ResponseEntity contenente il messaggio di errore e lo stato 401
     */
    @ExceptionHandler({UnauthorizedException.class, EntityNotFoundException.class})
    public ResponseEntity<String> handleAuthenticationFailure(Exception ex) {
        // Messaggio generico per prevenire l'enumerazione degli utenti.
        final String GENERIC_MESSAGE = "Credenziali non valide. Riprova.";

        // Restituisce lo stato HTTP 401 Unauthorized
        return new ResponseEntity<>(GENERIC_MESSAGE, HttpStatus.UNAUTHORIZED);
    }
}
