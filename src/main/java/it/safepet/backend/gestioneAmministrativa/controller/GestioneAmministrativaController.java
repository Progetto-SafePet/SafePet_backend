package it.safepet.backend.gestioneAmministrativa.controller;

import it.safepet.backend.gestioneAmministrativa.service.GestioneAmministrativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestioneAmministrativa")
public class GestioneAmministrativaController {
    @Autowired
    private GestioneAmministrativaService gestioneAmministrativaService;
}
