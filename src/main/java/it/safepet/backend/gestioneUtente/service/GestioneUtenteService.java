package it.safepet.backend.gestioneUtente.service;

import it.safepet.backend.gestioneUtente.dto.ProfiloProprietarioResponseDTO;
import it.safepet.backend.gestioneUtente.dto.ProprietarioResponseDTO;
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
  
    /** 
     * Recupera il profilo completo del proprietario autenticato inclusi i suoi pet.
     * @return DTO contenente tutti i dati del proprietario e la lista dei pet
     * @throws org.springframework.web.server.ResponseStatusException con status 401 se l'utente non è autenticato
     * @throws org.springframework.web.server.ResponseStatusException con status 403 se l'utente non è un proprietario
     * @throws org.springframework.web.server.ResponseStatusException con status 404 se il proprietario non viene trovato
     */
    ProfiloProprietarioResponseDTO visualizzaProfiloProprietario();

    ProprietarioResponseDTO getProprietario(Long propId);
}
