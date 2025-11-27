package it.safepet.backend.gestioneRecensioni.service;

import it.safepet.backend.gestioneRecensioni.dto.NewRecensioneDTO;
import it.safepet.backend.gestioneRecensioni.dto.RecensioneResponseDTO;
import jakarta.validation.Valid;

public interface GestioneRecensioniService {

    /**
     * Crea una nuova recensione per il veterinario indicato.
     * <p>
     * Verifica che l'utente autenticato sia un proprietario, controlla che il veterinario esista
     * e che non ci sia già una recensione per lo stesso proprietario. Se tutto è valido,
     * salva la recensione e restituisce il DTO di risposta.
     * </p>
     *
     * Il metodo è annotato con {@code @Transactional} per garantire la persistenza atomica.
     *
     * @param idVeterinario ID del veterinario a cui associare la recensione
     * @param newRecensioneDTO dati della recensione (punteggio e descrizione)
     * @return {@link RecensioneResponseDTO} con i dati della recensione creata
     * @throws RuntimeException se l'utente non è autorizzato o le entità non esistono
     * @throws IllegalStateException se esiste già una recensione per questo veterinario da questo proprietario
     */
    RecensioneResponseDTO creaRecensione(Long idVeterinario, @Valid NewRecensioneDTO newRecensioneDTO) throws Exception;
}
