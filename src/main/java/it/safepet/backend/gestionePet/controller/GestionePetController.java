package it.safepet.backend.gestionePet.controller;

import it.safepet.backend.gestionePet.dto.NewPetDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.service.GestionePetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/gestionePet")
public class GestionePetController {
    @Autowired
    private GestionePetService gestionePetService;


    @PostMapping(value = "/creaPet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponseDTO> creaPet(@ModelAttribute NewPetDTO newPetDTO) throws IOException {
        PetResponseDTO nuovoPet = gestionePetService.creaPet(newPetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovoPet);
    }
    
}
