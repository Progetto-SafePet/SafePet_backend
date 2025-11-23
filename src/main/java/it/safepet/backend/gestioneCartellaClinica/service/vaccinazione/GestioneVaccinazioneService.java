package it.safepet.backend.gestioneCartellaClinica.service.vaccinazione;

import it.safepet.backend.gestioneCartellaClinica.dto.VaccinazioneRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VaccinazioneResponseDTO;

public interface GestioneVaccinazioneService {

    /**
     * Aggiunge una nuova vaccinazione alla cartella clinica di un pet,
     * verificando che il veterinario autenticato sia autorizzato.
     *
     * @param requestDTO Dati della vaccinazione da registrare.
     * @return VaccinazioneResponseDTO contenente i dettagli della nuova vaccinazione.
     */
    VaccinazioneResponseDTO aggiungiVaccinazione(VaccinazioneRequestDTO requestDTO);
}
