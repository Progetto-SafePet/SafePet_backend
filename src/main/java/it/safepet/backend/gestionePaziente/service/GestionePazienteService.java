package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePaziente.dto.DettagliResponseDTO;
import it.safepet.backend.gestionePet.model.Pet;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface GestionePazienteService {

    /**
     * Restituisce la lista dei pazienti associati al veterinario attualmente autenticato.
     *
     * @return una lista di PazienteResponseDTO
     * @throws ResponseStatusException se non autenticato, veterinario non trovato o nessun paziente associato
     */
    List<PazienteResponseDTO> visualizzaListaPazienti();
    DettagliResponseDTO visualizzaDettagliPaziente(Long petId);

}
