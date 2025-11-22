package it.safepet.backend.gestionePet.controller;

import it.safepet.backend.gestionePet.dto.NewPetDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.dto.VisualizzaPetResponseDTO;
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
     * <p><b>Campi obbligatori nella risposta:</b></p>
     * <ul>
     * <li><code>nome</code> (String): nome del pet, obbligatorio, 3-20 caratteri</li>
     * <li><code>specie</code> (String): specie dell'animale, obbligatorio</li>
     * <li><code>sesso</code> (char): 'M' o 'F', obbligatorio</li>
     * <li><code>dataNascita</code> (String): data in formato ISO yyyy-MM-dd, obbligatorio</li>
     * </ul>*
     * <p><b>Campi opzionali e vincoli:</b></p>
     * <ul>
     * <li><code>razza</code> (String): opzionale, 3-30 caratteri</l
     * <li><code>peso</code> (String): opzionale, 0.1-100.0</l
     * <li><code>colore mantello</code> (String): opzionale, 3-15 caratteri</li>
     * <li><code>microchip</code> (String): opzionale, 15 caratteri alfanumerici</li>
     *<li><code>Sterilizzato</code> (String): opzionale, True-False</li>
     * <li><code>fotoBase64</code> (String): opzionale, immagine codificata in Base64</li>
     * </ul>
     *
     * <p><b>Esempio richiesta:</b></p>
     * <pre>
     * POST /gestionePet/creaPet
     * Authorization: Bearer eyJhbGciOi...
     * Content-Type: multipart/form-data
     *
     * nome=Luna
     * sesso=F
     * specie=Cane
     * dataNascita=2004-06-06
     * microchip=123456789012345
     * foto=dog1.jpg
     * </pre>
     *
     * <p><b>Esempio risposta:</b></p>
     * <pre>
     * {
     *   "id": 5,
     *   "nome": "Luna",
     *   "sesso": "F",
     *   "specie"="Cane",
     *   "dataNascita"="2004-06-06",
     *   "microchip": "123456789012345",
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
     *     "id": "1",
     *     "nome": "Luna",
     *     "specie": "Cane"
     *     "sesso": "F",
     *     "fotoBase64": "/9j/4AAQSkZJRgABAQAAAQABAAD..."
     *     "dataNascita": "2004-09-09"
     *   },
     *   {
     *     "id": "2",
     *     "nome": "Luigi",
     *     "specie": "Gatto"
     *     "sesso": "M",
     *     "fotoBase64": "/9j/4AAQSkZJRgABAQAAAQABAAD..."
     *     "dataNascita": "2004-09-07"
     *   }
     * ]
     * </pre>
     *
     * @return una lista di {@link VisualizzaPetResponseDTO} appartenenti all’utente autenticato
     * @see VisualizzaPetResponseDTO
     */

    @GetMapping("/visualizzaElencoPet")
    public ResponseEntity<List<VisualizzaPetResponseDTO>> visualizzaMieiPet() {
        List<VisualizzaPetResponseDTO> pets = gestionePetService.visualizzaMieiPet();
        return ResponseEntity.ok(pets);
    }

    
}

