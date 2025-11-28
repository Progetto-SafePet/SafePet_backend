package it.safepet.backend.gestioneCartellaClinica.service;

import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;

public interface GestioneCartellaClinicaService {
    CartellaClinicaResponseDTO getCartellaClinica(Long petId);
}
