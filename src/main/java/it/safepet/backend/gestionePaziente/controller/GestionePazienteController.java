package it.safepet.backend.gestionePaziente.controller;

import it.safepet.backend.gestionePaziente.service.GestionePazienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestionePaziente")
public class GestionePazienteController {
    @Autowired
    private GestionePazienteService gestionePazienteService;
}
