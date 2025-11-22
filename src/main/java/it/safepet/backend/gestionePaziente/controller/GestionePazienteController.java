package it.safepet.backend.gestionePaziente.controller;

import it.safepet.backend.gestionePaziente.dto.LinkingCodeRequestDTO;
import it.safepet.backend.gestionePaziente.dto.LinkingCodeResponseDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteRequestDTO;
import it.safepet.backend.gestionePaziente.service.GestionePazienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestionePaziente")
public class GestionePazienteController {
    @Autowired
    private GestionePazienteService gestionePazienteService;

    @PostMapping("/aggiungiPaziente")
    public void aggiungiPaziente (@RequestBody PazienteRequestDTO pazienteRequestDTO) {
        gestionePazienteService.aggiungiPaziente(pazienteRequestDTO);
    }

    @PostMapping("/generaLinkingCode")
    public ResponseEntity<LinkingCodeResponseDTO> generaLinkingCode (@RequestBody LinkingCodeRequestDTO linkingCodeRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gestionePazienteService.generaLinkingCode(linkingCodeRequestDTO));
    }
}
