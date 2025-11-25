package it.safepet.backend.gestioneCartellaClinica.service.terapia;

import it.safepet.backend.gestioneCartellaClinica.dto.TerapiaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.TerapiaResponseDTO;
import jakarta.validation.Valid;

public interface GestioneTerapiaService {
    TerapiaResponseDTO aggiungiTerapia( TerapiaRequestDTO dto);
}