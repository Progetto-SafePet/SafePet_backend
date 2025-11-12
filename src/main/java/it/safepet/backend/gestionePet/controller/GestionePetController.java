package it.safepet.backend.gestionePet.controller;

import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.service.GestionePetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gestionePet")
public class GestionePetController {

    @Autowired
    GestionePetService gestionePetService;

    /**
     * Restituisce la lista dei pet associati all’utente autenticato.
     *
     * <p><b>Metodo:</b> GET<br>
     * <b>Endpoint:</b> /gestionePet/visualizzaPet</p>
     *
     * <p><b>Parametri richiesti:</b></p>
     * <ul>
     *     <li><b>Authorization</b> (header): token JWT in formato <code>Bearer &lt;token&gt;</code></li>
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
     *     "microchip": "ABC123",
     *     "sesso": "F",
     *     "fotoBase64": "/9j/4AAQSkZJRgABAQAAAQABAAD..."
     *   },
     *   {
     *     "id": 6,
     *     "nome": "Rocky",
     *     "razza": "Bulldog",
     *     "microchip": "XYZ987",
     *     "sesso": "M",
     *     "fotoBase64": "/9j/4AAQSkZJRgABAQEASABIAAD..."
     *   }
     * ]
     * </pre>
     *
     * @return una lista di {@link PetResponseDTO} relativi al proprietario autenticato
     * @see PetResponseDTO
     */
    @GetMapping("/visualizzaPet")
    public ResponseEntity<List<PetResponseDTO>> visualizzaMieiPet() {
        List<PetResponseDTO> pets = gestionePetService.visualizzaElencoPet();
        return ResponseEntity.ok(pets);
    }
}
