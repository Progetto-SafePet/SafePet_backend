package it.safepet.backend.gestioneUtente.service;

import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneUtente.dto.ProfiloProprietarioResponseDTO;
import it.safepet.backend.gestioneUtente.dto.ProprietarioResponseDTO;
import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
import it.safepet.backend.gestioneUtente.dto.VisualizzaDettagliVeterinariResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    /**
     * Restituisce i dati di un proprietario specifico come {@link ProprietarioResponseDTO}.
     * <p>
     * Prima di recuperare i dati, il metodo esegue i seguenti controlli di autorizzazione:
     * <ul>
     *     <li>Verifica che ci sia un utente autenticato.</li>
     *     <li>Verifica che l'utente autenticato abbia il ruolo di {@link Role#PROPRIETARIO}.</li>
     * </ul>
     * </p>
     *
     * @param propId l'ID del proprietario di cui recuperare i dati
     * @return un {@link ProprietarioResponseDTO} contenente nome, cognome, email, numero di telefono e indirizzo del proprietario
     * @throws ResponseStatusException con stato:
     *         <ul>
     *             <li>{@link HttpStatus#UNAUTHORIZED} se nessun utente è autenticato</li>
     *             <li>{@link HttpStatus#FORBIDDEN} se l'utente autenticato non è un proprietario</li>
     *             <li>{@link HttpStatus#NOT_FOUND} se non esiste un proprietario con l'ID specificato</li>
     *         </ul>
     */
    ProprietarioResponseDTO getProprietario(Long propId);
}
