package it.safepet.backend.reportCliniche.service;

import it.safepet.backend.reportCliniche.dto.ElencoResponseDTO;

import java.util.List;

public interface ReportClinicheService {
    public List<ElencoResponseDTO> visualizzaElencoVeterinari();
}
