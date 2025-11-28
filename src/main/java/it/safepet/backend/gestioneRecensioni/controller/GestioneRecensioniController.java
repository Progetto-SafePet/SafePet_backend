package it.safepet.backend.gestioneRecensioni.controller;

import it.safepet.backend.gestioneRecensioni.dto.NewRecensioneDTO;
import it.safepet.backend.gestioneRecensioni.dto.RecensioneResponseDTO;
import it.safepet.backend.gestioneRecensioni.service.GestioneRecensioniService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestioneRecensioni")
public class GestioneRecensioniController {

    @Autowired
    private GestioneRecensioniService gestioneRecensioniService;

    /**
     * Crea una nuova recensione per il veterinario specificato.
     *
     * <p><b>Metodo:</b> POST<br>
     * <b>Endpoint:</b> /gestioneRecensioni/veterinari/{idVeterinario}/recensioni<br>
     * <b>Content-Type:</b> application/json</p>
     *
     * <p><b>Parametri richiesti:</b></p>
     * <ul>
     *     <li><b>Authorization</b> (header): token JWT in formato <code>Bearer &lt;token&gt;</code></li>
     *     <li><b>idVeterinario</b> (path): ID del veterinario a cui associare la recensione</li>
     *     <li><b>Body JSON:</b>
     *         <ul>
     *             <li><code>punteggio</code> (Integer): obbligatorio, range 1-5</li>
     *             <li><code>commento</code> (String): obbligatorio, non vuoto</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * <p><b>Esempio richiesta:</b></p>
     * <pre>
     * POST /gestioneRecensioni/veterinari/8/recensioni
     * Authorization: Bearer eyJhbGciOi...
     * Content-Type: application/json
     *
     * {
     *   "punteggio": 5,
     *   "commento": "Servizio eccellente, molto professionale!"
     * }
     * </pre>
     *
     * <p><b>Esempio risposta:</b></p>
     * <pre>
     * {
     *   "idRecensione": 12,
     *   "punteggio": 5,
     *   "commento": "Servizio eccellente, molto professionale!",
     *   "veterinarioId": 8,
     *   "proprietarioId": 3
     * }
     * </pre>
     *
     * @param idVeterinario ID del veterinario a cui associare la recensione
     * @param newRecensioneDTO DTO contenente punteggio e commento
     * @return {@link RecensioneResponseDTO} con i dati della recensione creata
     * @throws Exception se il veterinario non esiste, il proprietario non è autenticato
     *                   o esiste già una recensione per questo veterinario
     */
    @PostMapping("/veterinari/{idVeterinario}/recensioni")
    public ResponseEntity<RecensioneResponseDTO> creaRecensione(
            @PathVariable Long idVeterinario,
            @Valid @RequestBody NewRecensioneDTO newRecensioneDTO) throws Exception {

        RecensioneResponseDTO response = gestioneRecensioniService.creaRecensione(idVeterinario, newRecensioneDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
