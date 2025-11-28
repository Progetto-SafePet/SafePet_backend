package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.exception.ConflictException;
import it.safepet.backend.exception.NotFoundException;
import it.safepet.backend.exception.UnauthorizedException;
import it.safepet.backend.gestionePaziente.dto.LinkingCodeRequestDTO;
import it.safepet.backend.gestionePaziente.dto.LinkingCodeResponseDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteRequestDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePaziente.dto.DettagliResponseDTO;
import it.safepet.backend.gestionePet.model.Pet;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface GestionePazienteService {

    /**
     * Aggiunge un nuovo paziente (Pet) alla lista dei pazienti associati
     * al veterinario autenticato, utilizzando un linking code fornito dal client.
     *
     * <p>
     * Il metodo esegue i seguenti controlli:
     * <ul>
     *   <li>Verifica che l'utente corrente sia autenticato e abbia ruolo {@code VETERINARIO}.</li>
     *   <li>Recupera il veterinario tramite l'ID dell'utente autenticato.</li>
     *   <li>Recupera il linking code tramite il codice fornito dal DTO.</li>
     *   <li>Controlla che il linking code non sia già stato utilizzato.</li>
     *   <li>Controlla che il linking code non sia scaduto.</li>
     *   <li>Verifica che il pet associato al linking code non sia già collegato al veterinario.</li>
     * </ul>
     *
     * Se tutti i controlli hanno esito positivo:
     * <ul>
     *   <li>Associa il pet al veterinario e viceversa.</li>
     *   <li>Segna il linking code come utilizzato.</li>
     * </ul>
     *
     * @param paziente DTO contenente il linking code del pet da associare.
     *
     * @throws UnauthorizedException se l'utente non è autenticato o non ha ruolo {@code VETERINARIO}.
     * @throws NotFoundException se il veterinario o il linking code non vengono trovati.
     * @throws RuntimeException se il linking code è già stato utilizzato o risulta scaduto.
     * @throws ConflictException se il pet è già associato al veterinario.
     */
    void aggiungiPaziente(PazienteRequestDTO paziente);

    /**
     * Genera un nuovo linking code per un pet appartenente al proprietario autenticato.
     *
     * <p>Il metodo esegue i seguenti passaggi:</p>
     * <ul>
     *   <li>Verifica che l'utente corrente sia autenticato e abbia ruolo {@code PROPRIETARIO}.</li>
     *   <li>Recupera il pet indicato nel DTO e controlla che appartenga al proprietario autenticato.</li>
     *   <li>Se esiste già un linking code associato al pet, lo elimina prima di crearne uno nuovo.</li>
     *   <li>Genera un nuovo codice univoco composto da 3 lettere e 5 cifre.</li>
     *   <li>Imposta una nuova data di scadenza pari a 6 mesi dalla creazione.</li>
     *   <li>Associa il linking code al pet e lo salva nel repository.</li>
     * </ul>
     *
     * <p>Il linking code generato è marcato come non utilizzato e pronto per essere inviato
     * a un veterinario, che potrà successivamente aggiungere il pet come proprio paziente.</p>
     *
     * @param linkingCodeRequestDTO DTO contenente l'ID del pet per cui generare il linking code.
     *
     * @return un {@link LinkingCodeResponseDTO} contenente le informazioni sul linking code appena generato,
     *         incluso il codice, la data di scadenza e i dati essenziali del pet.
     *
     * @throws UnauthorizedException se l'utente non è autenticato o non ha ruolo {@code PROPRIETARIO}.
     * @throws NotFoundException se il pet indicato non esiste.
     * @throws UnauthorizedException se il pet non appartiene al proprietario autenticato.
     */
    LinkingCodeResponseDTO generaLinkingCode (LinkingCodeRequestDTO linkingCodeRequestDTO);
  
    /**
     * Restituisce la lista dei pazienti associati al veterinario attualmente autenticato.
     *
     * @return una lista di PazienteResponseDTO
     * @throws ResponseStatusException se non autenticato, veterinario non trovato o nessun paziente associato
     */
    List<PazienteResponseDTO> visualizzaListaPazienti();

    /**
     * Restituisce tutti i dettagli anagrafici e clinici di un paziente (Pet)
     * associato al veterinario attualmente autenticato.
     *
     * <p>
     * Questo endpoint permette al veterinario di accedere alla scheda completa
     * del paziente selezionato all’interno della propria “Lista Pazienti”.
     * I dati restituiti includono:
     * </p>
     *
     * <ul>
     *     <li>Informazioni anagrafiche (nome, sesso, specie, razza, microchip, ecc.)</li>
     *     <li>Dati fisici (peso, colore del mantello, stato sterilizzazione)</li>
     *     <li>Nome e cognome del proprietario</li>
     *     <li>Fotografia del pet in formato Base64</li>
     *     <li>Cartella clinica completa:
     *         <ul>
     *             <li>Vaccinazioni</li>
     *             <li>Visite mediche</li>
     *             <li>Patologie</li>
     *             <li>Terapie</li>
     *         </ul>
     *     </li>
     *     <li>Note personali del proprietario (se presenti)</li>
     * </ul>
     *
     * <p><b>Autorizzazione:</b></p>
     * <ul>
     *     <li>L’utente deve essere autenticato tramite JWT</li>
     *     <li>Deve essere un veterinario</li>
     *     <li>Il pet richiesto deve essere associato al veterinario</li>
     * </ul>
     *
     * <p><b>Errori restituiti:</b></p>
     * <ul>
     *     <li>401 UNAUTHORIZED → utente non autenticato</li>
     *     <li>404 NOT FOUND → pet inesistente</li>
     *     <li>403 FORBIDDEN → il veterinario non è associato al pet</li>
     * </ul>
     *
     * <p><b>Esempio endpoint:</b></p>
     * <pre>
     * GET /gestionePaziente/dettagli/{petId}
     * </pre>
     *
     * <p><b>Esempio risposta JSON:</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "nome": "Bobby",
     *   "specie": "Cane",
     *   "razza": "Labrador",
     *   "sesso": "M",
     *   "dataNascita": "2020-05-01",
     *   "peso": 30.5,
     *   "coloreMantello": "Miele",
     *   "microchip": "982000411234567",
     *   "sterilizzato": true,
     *   "proprietarioCompleto": "Luigi Verdi",
     *   "fotoBase64": "...",
     *   "cartellaClinica": {
     *       "vaccinazioni": [...],
     *       "visiteMediche": [...],
     *       "patologie": [...],
     *       "terapie": [...]
     *   },
     *   "noteProprietario": [...]
     * }
     * </pre>
     *
     * @param petId identificativo del pet di cui recuperare i dettagli
     * @return {@link DettagliResponseDTO} contenente dati anagrafici, clinici, foto e note
     * @throws org.springframework.web.server.ResponseStatusException se l’utente non è
     *         autenticato, se il pet non esiste o se il veterinario non è autorizzato
     */

    DettagliResponseDTO visualizzaDettagliPaziente(Long petId);

}
