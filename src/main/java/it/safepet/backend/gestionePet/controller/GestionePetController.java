package it.safepet.backend.gestionePet.controller;

import it.safepet.backend.gestionePet.dto.NewPetDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.service.GestionePetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/gestionePet")
public class GestionePetController {
    @Autowired
    private GestionePetService gestionePetService;

    /**
     * Crea un nuovo animale domestico per l’utente autenticato.
     *
     * <p><b>Metodo:</b> POST<br>
     * <b>Endpoint:</b> /gestionePet/creaPet<br>
     * <b>Content-Type:</b> multipart/form-data</p>
     *
     * <p><b>Parametri richiesti:</b></p>
     * <ul>
     *     <li><b>Authorization</b> (header): token JWT in formato <code>Bearer &lt;token&gt;</code></li>
     *     <li><b>nome</b> (form-data): nome del pet</li>
     *     <li><b>razza</b> (form-data): razza dell’animale</li>
     *     <li><b>microchip</b> (form-data): codice microchip univoco</li>
     *     <li><b>sesso</b> (form-data): M o F</li>
     *     <li><b>foto</b> (form-data): immagine JPEG o PNG</li>
     * </ul>
     *
     * <p><b>Esempio richiesta:</b></p>
     * <pre>
     * POST /gestionePet/creaPet
     * Authorization: Bearer eyJhbGciOi...
     * Content-Type: multipart/form-data
     *
     * nome=Luna
     * razza=Labrador
     * microchip=123456789012345
     * sesso=F
     * foto=dog1.jpg
     * </pre>
     *
     * <p><b>Esempio risposta:</b></p>
     * <pre>
     * {
     *   "id": 5,
     *   "nome": "Luna",
     *   "razza": "Labrador",
     *   "microchip": "123456789012345",
     *   "sesso": "F",
     *   "fotoBase64": "/9j/4AAQSkZJRgABAQAAAQABAAD..."
     * }
     * </pre>
     *
     * @param newPetDTO dati inviati tramite form-data per la creazione del nuovo pet
     * @return un oggetto {@link PetResponseDTO} contenente le informazioni del pet creato
     * @throws IOException se si verifica un errore nella lettura o conversione dell’immagine
     * @see PetResponseDTO
     */

    @PostMapping(value = "/creaPet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponseDTO> creaPet(@ModelAttribute NewPetDTO newPetDTO) throws IOException {
        PetResponseDTO nuovoPet = gestionePetService.creaPet(newPetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovoPet);
    }

    @GetMapping("/visualizzaPet")
    public ResponseEntity<List<PetResponseDTO>> visualizzaMieiPet() {
        List<PetResponseDTO> pets = gestionePetService.visualizzaMieiPet();
        return ResponseEntity.ok(pets);
    }

    
}
