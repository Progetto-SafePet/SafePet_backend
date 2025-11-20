package it.safepet.backend.gestionePet.service;

import it.safepet.backend.gestionePet.dto.NewPetDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

public interface GestionePetService {
    public PetResponseDTO creaPet(@Valid NewPetDTO newPetDTO) throws IOException;

    /**
     * Restituisce la lista degli animali associati all’utente autenticato.
     *
     * <p>Ogni elemento della lista contiene le informazioni principali del pet,
     * incluse eventuali immagini convertite in Base64.</p>
     *
     * @return una lista di {@link PetResponseDTO} appartenenti all’utente autenticato
     */

    public List<PetResponseDTO> visualizzaMieiPet();
}
