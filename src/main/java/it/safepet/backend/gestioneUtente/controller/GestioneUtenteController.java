package it.safepet.backend.gestioneUtente.controller;

import it.safepet.backend.gestioneUtente.dto.ProfiloProprietarioResponseDTO;
import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
import it.safepet.backend.gestioneUtente.dto.VisualizzaDettagliVeterinariResponseDTO;
import it.safepet.backend.gestioneUtente.service.GestioneUtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/gestioneUtente")
public class GestioneUtenteController {
    @Autowired
    private GestioneUtenteService gestioneUtenteService;

    /**
     * Registra un nuovo proprietario nel sistema.
     *
     * <p><b>Metodo:</b> POST<br>
     * <b>Endpoint:</b> /gestioneUtente/registraProprietario<br>
     * <b>Content-Type:</b> application/json</p>
     *
     * <p><b>Corpo richiesta (JSON):</b></p>
     * <pre>
     * {
     *   "nome": "Mario",
     *   "cognome": "Rossi",
     *   "email": "mario.rossi@mail.com",
     *   "password": "Password123",
     *   "confermaPassword": "Password123",
     *   "numeroTelefono": "1234567890",
     *   "dataNascita": "1990-01-01",
     *   "indirizzoDomicilio": "Via Roma 1, Milano",
     *   "genere": "M"
     * }
     * </pre>
     *
     * @param registrazioneDTO dati di registrazione del proprietario
     * @return ResponseEntity con status 201 Created
     */
    @PostMapping("/registraProprietario")
    public ResponseEntity<Void> registrazioneProprietario(
            @RequestBody RegistrazioneProprietarioRequestDTO registrazioneDTO) {
        gestioneUtenteService.registraProprietario(registrazioneDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Recupera e visualizza i dettagli completi di un veterinario specifico.
     * L’endpoint richiede che l’utente sia autenticato; in caso contrario viene
     * restituito un errore di autorizzazione. I dettagli restituiti includono
     * informazioni anagrafiche, contatti, clinica associata (con orari di apertura e chiusura)
     * e l’elenco delle recensioni ricevute, oltre alla media complessiva delle stesse.
     *
     * <p><b>Metodo:</b> GET<br>
     * <b>Endpoint:</b> /it.safepet.backend.gestioneUtente/visualizzaDettagliVeterinari/{vetId}</p>
     *
     * <p><b>Parametri di percorso:</b></p>
     * <ul>
     *   <li><b>vetId</b> – identificativo del veterinario di cui recuperare i dettagli</li>
     * </ul>
     *
     * <p><b>Esempio risposta (200 OK):</b></p>
     * <pre>
     * {
     *   "idVeterinario": 5,
     *   "nome": "Giulia",
     *   "cognome": "Rinaldi",
     *   "dataNascita": "1987-03-22",
     *   "genere": "F",
     *   "email": "giulia.rinaldi@example.com",
     *   "numeroTelefono": "+39 320 1234567",
     *   "specializzazioniAnimali": ["Cani", "Gatti"],
     *   "idClinica": 2,
     *   "nomeClinica": "Clinica Veterinaria Aurora",
     *   "indirizzoClinica": "Via Roma 15, Milano",
     *   "numeroTelefonoClinica": "+39 02 9876543",
     *   "latitudineClinica": 45.4642,
     *   "longitudineClinica": 9.1900,
     *   "orariApertura": [
     *     {
     *       "giorno": "LUNEDI",
     *       "orarioApertura": "09:00",
     *       "orarioChiusura": "19:00",
     *       "aperto24h": false
     *     }
     *   ],
     *   "recensioni": [
     *     {
     *       "idRecensione": 12,
     *       "punteggio": 5,
     *       "descrizione": "Molto competente",
     *       "idProprietario": 3,
     *       "idVeterinario": 5
     *     }
     *   ],
     *   "mediaRecensioni": 4.7
     * }
     * </pre>
     *
     * @param vetId identificativo del veterinario di cui visualizzare i dettagli
     * @return {@link ResponseEntity} contenente un {@link VisualizzaDettagliVeterinariResponseDTO}
     *         con tutte le informazioni anagrafiche, cliniche e di recensioni del veterinario
     */
    @GetMapping("/visualizzaDettagliVeterinari/{vetId}")
    public ResponseEntity<VisualizzaDettagliVeterinariResponseDTO> visualizzaDettagliVeterinari(@PathVariable Long vetId) {
        VisualizzaDettagliVeterinariResponseDTO dettagliVeterinario = gestioneUtenteService.visualizzaDettagliVeterinari(vetId);
        return ResponseEntity.ok(dettagliVeterinario);
      
    /**  
     * Visualizza il profilo completo del proprietario autenticato.
     *
     * <p><b>Metodo:</b> GET<br>
     * <b>Endpoint:</b> /gestioneUtente/visualizzaProfiloProprietario<br>
     * <b>Autenticazione:</b> Richiesta (JWT)</p>
     *
     * <p><b>Autorizzazione:</b> Solo proprietari autenticati</p>
     *
     * @return ResponseEntity con ProfiloProprietarioResponseDTO
     */
    @GetMapping("/visualizzaProfiloProprietario")
    public ResponseEntity<ProfiloProprietarioResponseDTO> visualizzaProfiloProprietario() {
        ProfiloProprietarioResponseDTO profilo = gestioneUtenteService.visualizzaProfiloProprietario();
        return ResponseEntity.ok(profilo);
    }
}
