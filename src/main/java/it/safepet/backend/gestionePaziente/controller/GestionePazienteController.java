package it.safepet.backend.gestionePaziente.controller;

import it.safepet.backend.gestionePaziente.service.GestionePazienteService;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gestionePaziente")
public class GestionePazienteController {
    @Autowired
    private GestionePazienteService gestionePazienteService;

    @GetMapping("/pets")
    public List<PetResponseDTO> getAllPets() {
        /**
         * Restituisce la lista di tutti i pazienti (PET) presenti nel sistema.
         * L’endpoint richiama il service, che recupera i dati dal database e li
         * converte in PetResponseDTO da inviare al client in formato JSON.
         *
         * URL: GET /gestionePet/pets
         */
        return gestionePazienteService.getAllPets();
    }
}
