package it.safepet.backend.gestioneUtente.controller;

import it.safepet.backend.gestioneUtente.service.GestioneUtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestioneUtente")
public class GestioneUtenteController {
    @Autowired
    private GestioneUtenteService gestioneUtenteService;
}
