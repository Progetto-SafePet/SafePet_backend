package it.safepet.backend.gestioneListaPreferiti.controller;

import it.safepet.backend.gestioneListaPreferiti.service.GestioneListaPreferitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestioneListaPreferiti")
public class GestioneListaPreferitiController {
    @Autowired
    private GestioneListaPreferitiService gestioneListaPreferitiService;
}
