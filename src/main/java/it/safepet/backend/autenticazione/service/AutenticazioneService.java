package it.safepet.backend.autenticazione.service;

import it.safepet.backend.autenticazione.dto.AuthResponseDTO;
import it.safepet.backend.autenticazione.dto.LoginRequestDTO;
import it.safepet.backend.autenticazione.dto.RegistrazioneProprietarioRequestDTO;
import jakarta.validation.Valid;

public interface AutenticazioneService {
    /**
     * Effettua il login di un utente esistente, validando credenziali e generando il token JWT.
     *
     * @param loginRequestDTO DTO contenente email e password
     * @return AuthResponseDTO con token JWT e dati dell'utente autenticato
     * @throws RuntimeException se l'utente non esiste o la password è errata
     */
    AuthResponseDTO login(@Valid LoginRequestDTO loginRequestDTO);

    /**
     * Registra un nuovo proprietario nel sistema.
     *
     * @param registrazioneDTO DTO contenente i dati di registrazione del proprietario
     * @throws IllegalArgumentException se i dati di registrazione non sono validi
     */
    void registraProprietario(@Valid RegistrazioneProprietarioRequestDTO registrazioneDTO);
}
