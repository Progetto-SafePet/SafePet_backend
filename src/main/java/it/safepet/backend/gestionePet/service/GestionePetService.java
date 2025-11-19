package it.safepet.backend.gestionePet.service;

import it.safepet.backend.gestionePet.dto.NewPetDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

public interface GestionePetService {
    public PetResponseDTO creaPet(@Valid NewPetDTO newPetDTO) throws IOException;
    public List<PetResponseDTO> visualizzaMieiPet();
}
