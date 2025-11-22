package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.gestionePaziente.dto.LinkingCodeRequestDTO;
import it.safepet.backend.gestionePaziente.dto.LinkingCodeResponseDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteRequestDTO;

public interface GestionePazienteService {
    void aggiungiPaziente(PazienteRequestDTO paziente);
    LinkingCodeResponseDTO generaLinkingCode (LinkingCodeRequestDTO linkingCodeRequestDTO);
}
