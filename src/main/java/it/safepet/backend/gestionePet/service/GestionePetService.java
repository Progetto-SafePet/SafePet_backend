package it.safepet.backend.gestionePet.service;

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
}
