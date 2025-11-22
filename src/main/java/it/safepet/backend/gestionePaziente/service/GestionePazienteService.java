package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.gestionePaziente.dto.PazienteRequestDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;

public interface GestionePazienteService {
    PazienteResponseDTO aggiungiPaziente(PazienteRequestDTO paziente);
}
