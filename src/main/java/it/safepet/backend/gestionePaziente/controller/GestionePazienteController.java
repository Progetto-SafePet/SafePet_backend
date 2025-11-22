package it.safepet.backend.gestionePaziente.controller;

import it.safepet.backend.gestionePaziente.dto.PazienteRequestDTO;
import it.safepet.backend.gestionePaziente.model.LinkingCode;
import it.safepet.backend.gestionePaziente.service.GestionePazienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gestionePaziente")
public class GestionePazienteController {
    @Autowired
    private GestionePazienteService gestionePazienteService;

    @PostMapping("/aggiungiPaziente")
    public void aggiungiPaziente (@RequestBody PazienteRequestDTO pazienteRequestDTO) {
        gestionePazienteService.aggiungiPaziente(pazienteRequestDTO);
    }
}
