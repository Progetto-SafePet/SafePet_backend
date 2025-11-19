package it.safepet.backend.gestionePet.service;

import it.safepet.backend.gestionePet.dto.NewPetDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import jakarta.validation.Valid;

import java.io.IOException;

public interface GestionePetService {
    public PetResponseDTO creaPet(@Valid NewPetDTO newPetDTO) throws IOException;
}
