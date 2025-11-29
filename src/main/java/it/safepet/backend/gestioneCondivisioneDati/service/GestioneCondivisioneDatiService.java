package it.safepet.backend.gestioneCondivisioneDati.service;

import it.safepet.backend.gestioneCondivisioneDati.dto.CondivisioneDatiPetResponseDTO;

public interface GestioneCondivisioneDatiService {
    CondivisioneDatiPetResponseDTO getDatiCompletiPet(Long petId);
    byte[] generaPdfPet(Long petId);
}
