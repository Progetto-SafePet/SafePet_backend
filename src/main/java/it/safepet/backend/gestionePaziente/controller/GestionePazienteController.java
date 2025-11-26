package it.safepet.backend.gestionePaziente.controller;

import it.safepet.backend.exception.ConflictException;
import it.safepet.backend.exception.NotFoundException;
import it.safepet.backend.exception.UnauthorizedException;
import it.safepet.backend.gestionePaziente.dto.LinkingCodeRequestDTO;
import it.safepet.backend.gestionePaziente.dto.LinkingCodeResponseDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteRequestDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePaziente.service.GestionePazienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/gestionePaziente")
public class GestionePazienteController {
    @Autowired
    private GestionePazienteService gestionePazienteService;

    /**
     * Associa un nuovo paziente (Pet) al veterinario autenticato tramite linking code.
     *
     * <p><b>Metodo:</b> POST<br>
     * <b>Endpoint:</b> /gestionePaziente/aggiungiPaziente<br>
     * <b>Content-Type:</b> application/json</p>
     *
     * <p><b>Parametri richiesti:</b></p>
     * <ul>
     *     <li><b>Authorization</b> (header): token JWT in formato <code>Bearer &lt;token&gt;</code></li>
     *     <li><b>linkingCode</b> (JSON): codice di collegamento fornito dal proprietario</li>
     * </ul>
     *
     * <p><b>Esempio richiesta:</b></p>
     * <pre>
     * POST /gestionePaziente/aggiungiPaziente
     * Authorization: Bearer eyJhbGciOi...
     * Content-Type: application/json
     *
     * {
     *   "linkingCode": "ABC123456"
     * }
     * </pre>
     *
     * <p><b>Esempio risposta (successo):</b><br>
     * Nessun contenuto (HTTP 200 OK)</p>
     *
     * <p><b>Possibili errori:</b></p>
     * <ul>
     *     <li><b>401 Unauthorized</b>: utente non autenticato o ruolo non valido (solo veterinario)</li>
     *     <li><b>404 Not Found</b>: linking code non esistente oppure veterinario non trovato</li>
     *     <li><b>409 Conflict</b>: il pet è già associato al veterinario</li>
     *     <li><b>400 Bad Request</b>: linking code non valido o già utilizzato</li>
     * </ul>
     *
     * @param pazienteRequestDTO DTO contenente il linking code da utilizzare per l’associazione
     * @throws UnauthorizedException se l’utente non è un veterinario o non è autenticato
     * @throws NotFoundException se il linking code o il veterinario non sono trovati
     * @throws ConflictException se il pet risulta già associato al veterinario
     * @see PazienteRequestDTO
     */
    @PostMapping("/aggiungiPaziente")
    public void aggiungiPaziente (@RequestBody PazienteRequestDTO pazienteRequestDTO) {
        gestionePazienteService.aggiungiPaziente(pazienteRequestDTO);
    }

    /**
     * Genera un nuovo linking code per un pet appartenente al proprietario autenticato.
     *
     * <p><b>Metodo:</b> POST<br>
     * <b>Endpoint:</b> /gestionePaziente/generaLinkingCode<br>
     * <b>Content-Type:</b> application/json</p>
     *
     * <p><b>Parametri richiesti:</b></p>
     * <ul>
     *     <li><b>Authorization</b> (header): token JWT in formato <code>Bearer &lt;token&gt;</code></li>
     *     <li><b>petId</b> (JSON): ID del pet per cui generare il linking code</li>
     * </ul>
     *
     * <p><b>Esempio richiesta:</b></p>
     * <pre>
     * POST /gestionePaziente/generaLinkingCode
     * Authorization: Bearer eyJhbGciOi...
     * Content-Type: application/json
     *
     * {
     *   "petId": 1
     * }
     * </pre>
     *
     * <p><b>Esempio risposta:</b></p>
     * <pre>
     * {
     *   "petId": 1,
     *   "nomePet": "Rocky",
     *   "linkingCodeId": 1,
     *   "codice": "ABC12345",
     *   "dataScadenza": "2026-05-14",
     *   "usato": false
     * }
     * </pre>
     *
     * <p><b>Possibili errori:</b></p>
     * <ul>
     *     <li><b>401 Unauthorized</b>: l’utente non è autenticato oppure non è un proprietario</li>
     *     <li><b>404 Not Found</b>: pet non trovato per l’ID fornito</li>
     *     <li><b>401 Unauthorized</b>: il pet non appartiene all’utente autenticato</li>
     *     <li><b>500 Internal Server Error</b>: errore generico nella generazione del codice</li>
     * </ul>
     *
     * @param linkingCodeRequestDTO DTO contenente l’ID del pet per cui generare un nuovo linking code
     * @return un {@link LinkingCodeResponseDTO} con il linking code generato e i dati del pet
     * @see LinkingCodeRequestDTO
     * @see LinkingCodeResponseDTO
     */
    @PostMapping("/generaLinkingCode")
    public ResponseEntity<LinkingCodeResponseDTO> generaLinkingCode (@RequestBody LinkingCodeRequestDTO linkingCodeRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gestionePazienteService.generaLinkingCode(linkingCodeRequestDTO));
    }

    /**
     * Restituisce la lista dei pazienti associati al veterinario attualmente autenticato.
     *
     * <p>Il metodo:
     * <ul>
     *     <li>Chiama il service {@link GestionePazienteService#visualizzaListaPazienti()}</li>
     *     <li>Restituisce i pazienti in forma di {@link PazienteResponseDTO}</li>
     * </ul>
     *
     * <p>Viene utilizzato dal frontend per visualizzare l’elenco sintetico dei pazienti
     * (specie, nome, nascita, sesso, proprietario, ecc.).</p>
     *
     * @return una lista di {@link PazienteResponseDTO} contenente i dati dei pazienti del veterinario
     *
     * @throws RuntimeException se l’utente non è autenticato (gestito nel service)
     */
    @GetMapping("/listaPazienti")
    public List<PazienteResponseDTO> visualizzaPazientiDelVeterinario() {
        return gestionePazienteService.visualizzaListaPazienti();
    }
}
