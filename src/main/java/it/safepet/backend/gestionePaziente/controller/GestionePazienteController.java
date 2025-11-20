package it.safepet.backend.gestionePaziente.controller;

import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePaziente.service.GestionePazienteService;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gestionePaziente")
public class GestionePazienteController {
    @Autowired
    private GestionePazienteService gestionePazienteService;

    @GetMapping("/listaPazienti")
    public List<PazienteResponseDTO> visualizzaPazientiDelVeterinario() {
        return gestionePazienteService.visualizzaListaPazienti();
    }


}
