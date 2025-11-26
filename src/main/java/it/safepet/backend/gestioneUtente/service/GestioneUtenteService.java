package it.safepet.backend.gestioneUtente.service;

import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
import it.safepet.backend.gestioneUtente.dto.VisualizzaDettagliVeterinariResponseDTO;
import jakarta.validation.Valid;

public interface GestioneUtenteService {
    /**
     * Registra un nuovo proprietario nel sistema.
     *
     * @param registrazioneDTO DTO contenente i dati di registrazione del proprietario
     * @throws IllegalArgumentException se i dati di registrazione non sono validi
     */
    void registraProprietario(@Valid RegistrazioneProprietarioRequestDTO registrazioneDTO);

    /**
     * Recupera i dettagli completi di un veterinario, includendo informazioni personali,
     * contatti, clinica associata, orari di apertura e chiusura, recensioni ricevute e la media
     * complessiva delle valutazioni. L’accesso richiede che l’utente sia autenticato.
     *
     * @param idVet identificativo del veterinario di cui recuperare i dettagli
     * @return un {@link VisualizzaDettagliVeterinariResponseDTO} contenente tutte le informazioni
     *         anagrafiche, cliniche e relative alle recensioni del veterinario richiesto
     */
    VisualizzaDettagliVeterinariResponseDTO visualizzaDettagliVeterinari(Long idVet);
}
