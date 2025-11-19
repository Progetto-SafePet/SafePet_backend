package it.safepet.backend.gestionePet.controller;

import it.safepet.backend.gestionePet.service.GestionePetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestionePet")
public class GestionePetController {
    @Autowired
    private GestionePetService gestionePetService;
}
