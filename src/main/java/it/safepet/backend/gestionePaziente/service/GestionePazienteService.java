package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePaziente.dto.DettagliResponseDTO;
import it.safepet.backend.gestionePet.model.Pet;

import java.util.List;

public interface GestionePazienteService {


    List<PazienteResponseDTO> visualizzaListaPazienti();
    DettagliResponseDTO visualizzaDettagliPaziente(Long petId);

}
