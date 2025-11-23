package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.gestionePaziente.dto.LinkingCodeRequestDTO;
import it.safepet.backend.gestionePaziente.dto.LinkingCodeResponseDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteRequestDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;

import java.util.List;

public interface GestionePazienteService {
    void aggiungiPaziente(PazienteRequestDTO paziente);
    LinkingCodeResponseDTO generaLinkingCode (LinkingCodeRequestDTO linkingCodeRequestDTO);
    List<PazienteResponseDTO> visualizzaListaPazienti();
}
