package it.safepet.backend.gestioneCartellaClinica.controller;

import it.safepet.backend.gestioneCartellaClinica.service.GestioneCartellaClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestioneCartellaClinica")
public class GestioneCartellaClinicaController {
    @Autowired
    private GestioneCartellaClinicaService gestioneCartellaClinicaService;
}
