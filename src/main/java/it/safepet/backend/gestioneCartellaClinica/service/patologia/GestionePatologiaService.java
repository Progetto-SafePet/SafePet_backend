package it.safepet.backend.gestioneCartellaClinica.service.patologia;

import it.safepet.backend.gestioneCartellaClinica.dto.PatologiaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.PatologiaResponseDTO;

public interface GestionePatologiaService {
    /**
     * Crea una nuova patologia per un pet associato al veterinario autenticato.
     *
     * <p>Il metodo esegue i seguenti passaggi:
     * <ul>
     *   <li>Verifica che l’utente autenticato sia un veterinario.</li>
     *   <li>Recupera il pet indicato nel DTO e controlla che sia un paziente del veterinario.</li>
     *   <li>Valida i dati della richiesta (tramite il DTO).</li>
     *   <li>Salva la nuova patologia nel database.</li>
     *   <li>Restituisce un {@link PatologiaResponseDTO} contenente i dati della patologia creata.</li>
     * </ul>
     *
     * @param richiesta dati necessari per la creazione della patologia
     * @return {@link PatologiaResponseDTO} contenente i dettagli della patologia creata
     * @throws RuntimeException se:
     *         <ul>
     *             <li>l’utente non è autenticato o non è un veterinario;</li>
     *             <li>il pet non esiste o non è associato al veterinario;</li>
     *             <li>si verificano errori durante la creazione della patologia.</li>
     *         </ul>
     */
    PatologiaResponseDTO creaPatologia(PatologiaRequestDTO richiesta);
}
