package it.safepet.backend.gestionePet.controller;

import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.service.GestioneCartellaClinicaService;
import it.safepet.backend.gestionePet.dto.DettagliPetResponseDTO;
import it.safepet.backend.gestionePet.dto.InserimentoNoteRequestDTO;
import it.safepet.backend.gestionePet.dto.InserimentoNoteResponseDTO;
import it.safepet.backend.gestionePet.dto.NewPetDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.dto.VisualizzaPetResponseDTO;
import it.safepet.backend.gestionePet.service.GestionePetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/gestionePet")
public class GestionePetController {
    @Autowired
    private GestionePetService gestionePetService;

    @Autowired
    private GestioneCartellaClinicaService gestioneCartellaClinicaService;

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
     * <b>Endpoint:</b> /gestionePet/visualizzaElencoPet</p>
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
     * GET /gestionePet/visualizzaElencoPet
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

    /**
     * Recupera i dettagli completi di un pet, aggregando le informazioni anagrafiche,
     * la cartella clinica e le eventuali note del proprietario.
     *
     * <p><b>Logica:</b></p>
     * <ul>
     *   <li>Recupera i dati anagrafici del pet tramite {@code gestionePetService.getAnagraficaPet()}.</li>
     *   <li>Recupera la cartella clinica associata al pet tramite {@code gestioneCartellaClinicaService.getCartellaClinica()}.</li>
     *   <li>Recupera le note del proprietario tramite {@code gestioneNoteService.getNoteProprietario()}.</li>
     *   <li>Aggrega tutte le informazioni in un {@link DettagliPetResponseDTO}.</li>
     * </ul>
     *
     * <p><b>Endpoint:</b> GET /dettaglioPet/{petId}</p>
     *
     * <p><b>Parametri di percorso:</b></p>
     * <ul>
     *   <li><b>petId</b> – identificativo del pet di cui recuperare i dettagli.</li>
     * </ul>
     *
     * <p><b>Risposta (200 OK):</b></p>
     * <pre>
     * {
     *   "anagraficaDTO": {
     *     "id": 1,
     *     "nome": "Fido",
     *     "specie": "Cane",
     *     "dataNascita": "2020-05-10",
     *     "peso": 12.5,
     *     "coloreMantello": "Marrone",
     *     "isSterilizzato": true,
     *     "razza": "Labrador",
     *     "microchip": "123456789",
     *     "sesso": "MASCHIO",
     *     "fotoBase64": "iVBORw0KGgoAAAANSUhEUgAA..."
     *   },
     *   "cartellaClinicaDTO": {
     *     "vaccinazioni": [...],
     *     "visiteMediche": [...],
     *     "patologie": [...],
     *     "terapie": [...]
     *   },
     *   "noteProprietarioDTO": {
     *     "note": "Il cane è molto vivace e ama correre."
     *   }
     * }
     * </pre>
     *
     * @param petId identificativo del pet
     * @return {@link DettagliPetResponseDTO} contenente anagrafica, cartella clinica e note del proprietario
     */
    @GetMapping("/dettaglioPet/{petId}")
    public ResponseEntity<DettagliPetResponseDTO> getDettaglioPet(@PathVariable Long petId) {
        PetResponseDTO anagrafica = gestionePetService.getAnagraficaPet(petId);
        CartellaClinicaResponseDTO cartellaClinica = gestioneCartellaClinicaService.getCartellaClinica(petId);
        List<InserimentoNoteResponseDTO> note = gestionePetService.getNoteProprietario(petId);

        DettagliPetResponseDTO response = new DettagliPetResponseDTO(anagrafica, cartellaClinica, note);
        return ResponseEntity.ok(response);
    }

    /**
     * Crea e registra una nuova nota per un pet, verificando che l'utente autenticato
     * sia un proprietario e che il pet sia effettivamente associato a lui.
     * Permette al proprietario di aggiungere informazioni testuali relative al proprio animale.
     * Restituisce i dettagli completi della nota appena creata.
     *
     * <p><b>Metodo:</b> POST<br>
     * <b>Endpoint:</b> /it.safepet.backend.gestionePet/creaNota/{petId}<br>
     * <b>Content-Type:</b> application/json</p>
     *
     * <p><b>Parametri di percorso:</b></p>
     * <ul>
     *   <li><b>petId</b> – identificativo del pet a cui associare la nota</li>
     * </ul>
     *
     * <p><b>Corpo richiesta (application/json):</b></p>
     * <ul>
     *   <li><b>titolo</b> – titolo della nota</li>
     *   <li><b>descrizione</b> – contenuto testuale della nota</li>
     *   <li><b>petId</b> – impostato automaticamente tramite il parametro di percorso</li>
     * </ul>
     *
     * <p><b>Esempio risposta (201 CREATED):</b></p>
     * <pre>
     * {
     *   "idNota": 7,
     *   "titolo": "Alimentazione",
     *   "descrizione": "Ricordarsi di acquistare il nuovo mangime ipoallergenico.",
     *   "idPet": 3,
     *   "nomePet": "Luna",
     *   "idProprietario": 12,
     *   "nomeCompletoProprietario": "Mario Rossi"
     * }
     * </pre>
     *
     * @param petId identificativo del pet a cui associare la nota
     * @param inserimentoNoteRequestDTO DTO contenente titolo e descrizione della nota
     * @return {@link InserimentoNoteResponseDTO} con i dettagli della nota creata
     * @throws IOException se si verificano errori durante la gestione dei dati
     * @throws RuntimeException se l'utente non è un proprietario, il pet non esiste
     *         o non appartiene al proprietario autenticato
     */
    @PostMapping(value = "/creaNota/{petId}",  consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InserimentoNoteResponseDTO> creaNota(
            @PathVariable Long petId,
            @RequestBody InserimentoNoteRequestDTO inserimentoNoteRequestDTO) throws IOException {
        inserimentoNoteRequestDTO.setPetId(petId);
        System.out.println(inserimentoNoteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gestionePetService.creaNota(inserimentoNoteRequestDTO));
    }
}
