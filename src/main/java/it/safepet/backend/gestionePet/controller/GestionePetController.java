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
    @PostMapping(value = "/creaPet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponseDTO> creaPet(@ModelAttribute NewPetDTO newPetDTO) throws IOException {
        PetResponseDTO nuovoPet = gestionePetService.creaPet(newPetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovoPet);
    }

    /**
     * Restituisce la lista dei pet associati all’utente autenticato.
     *
     * <p><b>Metodo:</b> GET<br>
     * <b>Endpoint:</b> /gestionePet/visualizzaPet</p>
     *
     * <p><b>Parametri richiesti:</b></p>
     * <ul>
     *     <li><b>Authorization</b> (header): token JWT in formato
     *         <code>Bearer &lt;token&gt;</code>
     *     </li>
     * </ul>
     *
     * <p><b>Esempio richiesta:</b></p>
     * <pre>
     * GET /gestionePet/visualizzaPet
     * Authorization: Bearer eyJhbGciOi...
     * </pre>
     *
     * <p><b>Esempio risposta:</b></p>
     * <pre>
     * [
     *   {
     *     "id": 5,
     *     "nome": "Luna",
     *     "razza": "Labrador",
     *     "microchip": "123456789012345",
     *     "sesso": "F",
     *     "fotoBase64": "/9j/4AAQSkZJRgABAQAAAQABAAD..."
     *   },
     *   {
     *     "id": 6,
     *     "nome": "Rocky",
     *     "razza": "Bulldog",
     *     "microchip": "111222333444555",
     *     "sesso": "M",
     *     "fotoBase64": "/9j/4AAQSkZJRgABAQEASABIAAD..."
     *   }
     * ]
     * </pre>
     *
     * @return una lista di {@link PetResponseDTO} appartenenti all’utente autenticato
     * @see PetResponseDTO
     */

    @GetMapping("/visualizzaPet")
    public ResponseEntity<List<PetResponseDTO>> visualizzaMieiPet() {
        List<PetResponseDTO> pets = gestionePetService.visualizzaMieiPet();
        return ResponseEntity.ok(pets);
    }
    
}
