package it.safepet.backend.gestioneUtente.service;

import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
import jakarta.validation.Valid;

public interface GestioneUtenteService {
    /**
     * Registra un nuovo proprietario nel sistema.
     *
     * @param registrazioneDTO DTO contenente i dati di registrazione del proprietario
     * @throws IllegalArgumentException se i dati di registrazione non sono validi
     */
    void registraProprietario(@Valid RegistrazioneProprietarioRequestDTO registrazioneDTO);
}
