package it.safepet.backend.gestioneCartellaClinica.service;

import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaResponseDTO;

public interface GestioneCartellaClinicaService {
    VisitaMedicaResponseDTO creaVisitaMedica(VisitaMedicaRequestDTO visitaMedicaDTO);
    VisitaMedicaResponseDTO leggiPFD(Long id);
}
