package it.safepet.backend.gestionePet.service;

import it.safepet.backend.gestionePet.dto.InserimentoNoteRequestDTO;
import it.safepet.backend.gestionePet.dto.InserimentoNoteResponseDTO;
import it.safepet.backend.gestionePet.dto.NewPetDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.dto.VisualizzaPetResponseDTO;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

public interface GestionePetService {

    /**
     * Crea un nuovo animale domestico associato al proprietario autenticato.
     *
     * <p>Il metodo valida i dati forniti tramite {@link NewPetDTO}, effettua un controllo
     * sull’unicità del microchip (se presente), valida l’immagine caricata (se fornita)
     * e salva il nuovo pet nel database restituendo un {@link PetResponseDTO}.</p>
     *
     * <p><b>Prerequisiti:</b></p>
     * <ul>
     *     <li>Utente autenticato</li>
     *     <li>Ruolo dell’utente = PROPRIETARIO</li>
     *     <li>Dati del DTO validati tramite {@link Valid}</li>
     * </ul>
     *
     * <p><b>Eccezioni:</b></p>
     * <ul>
     *     <li><b>RuntimeException</b> in caso di accesso non autorizzato o microchip duplicato</li>
     *     <li><b>IOException</b> se si verifica un errore durante la lettura dell’immagine</li>
     * </ul>
     *
     * @param newPetDTO dati del nuovo animale domestico da creare
     * @return un {@link PetResponseDTO} contenente tutte le informazioni del pet salvato
     * @throws IOException se la lettura dei byte dell’immagine fallisce
     */
    public PetResponseDTO creaPet(@Valid NewPetDTO newPetDTO) throws IOException;

    /**
     * Restituisce la lista degli animali associati all’utente autenticato.
     *
     * <p>Ogni elemento della lista contiene le informazioni principali del pet,
     * incluse eventuali immagini convertite in Base64.</p>
     *
     * @return una lista di {@link VisualizzaPetResponseDTO} appartenenti all’utente autenticato
     */
    public List<VisualizzaPetResponseDTO> visualizzaMieiPet();

    /**
     * Recupera i dati anagrafici di un pet, garantendo che l'utente autenticato
     * sia autorizzato a visualizzarli (solo i proprietari possono accedere).
     *
     * <p><b>Logica:</b></p>
     * <ul>
     *   <li>Verifica che l'utente sia autenticato.</li>
     *   <li>Controlla che il ruolo dell'utente sia {@code PROPRIETARIO}; in caso contrario, viene sollevata un'eccezione.</li>
     *   <li>Recupera il pet dal database tramite {@code petRepository} e verifica la sua esistenza.</li>
     *   <li>Costruisce e restituisce un {@link PetResponseDTO} contenente:
     *       id, nome, specie, data di nascita, peso, colore del mantello,
     *       stato di sterilizzazione, razza, microchip, sesso e foto codificata in Base64.</li>
     * </ul>
     *
     * <p><b>Sicurezza:</b> l'accesso è limitato ai proprietari autenticati per proteggere dati sensibili.</p>
     *
     * <p><b>Transazione:</b> il metodo è annotato con {@code @Transactional(readOnly = true)}
     * per garantire la consistenza dei dati durante la lettura.</p>
     *
     * @param petId identificativo univoco del pet da recuperare
     * @return {@link PetResponseDTO} con i dati anagrafici del pet
     * @throws RuntimeException se l'utente non è autenticato, non è un proprietario o il pet non esiste
     */
    public PetResponseDTO getAnagraficaPet(Long petId);

    /**
     * Crea una nuova nota associata a un pet, validando i dati forniti dal proprietario
     * e verificando che l’utente autenticato sia effettivamente il proprietario del pet.
     * Restituisce i dettagli completi della nota appena registrata.
     *
     * @param inserimentoNoteRequestDTO DTO contenente titolo, descrizione della nota e
     *                                  identificativo del pet a cui associarla
     * @return InserimentoNoteResponseDTO con le informazioni della nota creata,
     *         inclusi nome del pet e dati del proprietario
     * @throws IOException se si verificano errori durante l’elaborazione dei dati
     * @throws RuntimeException se il pet non esiste oppure non appartiene al proprietario autenticato
     */
    InserimentoNoteResponseDTO creaNota(@Valid InserimentoNoteRequestDTO inserimentoNoteRequestDTO) throws IOException;
}
