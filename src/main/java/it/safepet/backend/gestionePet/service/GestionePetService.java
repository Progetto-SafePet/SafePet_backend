package it.safepet.backend.gestionePet.service;

import it.safepet.backend.gestionePet.dto.PetResponseDTO;

import java.util.List;

public interface GestionePetService {

    /**
     * Restituisce la lista degli animali associati all’utente autenticato.
     *
     * @return lista di PetResponseDTO
     */
    List<PetResponseDTO> visualizzaElencoPet();
}
