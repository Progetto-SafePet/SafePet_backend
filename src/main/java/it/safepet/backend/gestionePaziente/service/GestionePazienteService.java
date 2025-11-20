package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.model.Pet;

import java.util.List;

public interface GestionePazienteService {


    int getEta(Pet pet);

    List<PetResponseDTO> getAllPets();
}
