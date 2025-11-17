package it.safepet.backend.gestioneRecensioni.controller;

import it.safepet.backend.gestioneRecensioni.service.GestioneRecensioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestioneRecensioni")
public class GestioneRecensioniController {

    @Autowired
    private GestioneRecensioniService gestioneRecensioniService;
}
