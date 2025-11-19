package it.safepet.backend.gestioneCondivisioneDati.controller;

import it.safepet.backend.gestioneCondivisioneDati.service.GestioneCondivisioneDatiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestioneCondivisioneDati")
public class GestioneCondivisioneDatiController {

    @Autowired
    private GestioneCondivisioneDatiService gCondivisioneDatiService;
}
