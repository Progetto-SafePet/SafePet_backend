package it.safepet.backend.gestioneUtente.controller;

import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
import it.safepet.backend.gestioneUtente.dto.VisualizzaDettagliVeterinariResponseDTO;
import it.safepet.backend.gestioneUtente.service.GestioneUtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

     Recupera e visualizza i dettagli completi di un veterinario specifico.
     L’endpoint richiede che l’utente sia autenticato; in caso contrario viene
     restituito un errore di autorizzazione. I dettagli forniti includono dati
     anagrafici, contatti, clinica associata e la media delle recensioni ricevute.*
     <p><b>Metodo:</b> GET<br>
     <b>Endpoint:</b> /it.safepet.backend.gestioneUtente/visualizzaDettagliVeterinari/{vetId}</p>*
     <p><b>Parametri di percorso:</b></p>
     <ul>
     <li><b>vetId</b> – identificativo del veterinario di cui recuperare i dettagli</li>
     </ul>*
     <p><b>Esempio risposta (200 OK):</b></p>
     <pre>
     [
     {
     "idVeterinario": 5,
     "nome": "Giulia",
     "cognome": "Rinaldi",
     "dataNascita": "1987-03-22",
     "genere": "F",
     "email": "giulia.rinaldi@example.com",
     "numeroTelefono": "+39 320 1234567",
     "specializzazioniAnimali": ["Cani", "Gatti"],
     "idClinica": 2,
     "nomeClinica": "Clinica Veterinaria Aurora",
     "indirizzoClinica": "Via Roma 15, Milano",
     "mediaRecensioni": 4.7
     }
     ]
     </pre>*
     @param vetId identificativo del veterinario di cui visualizzare i dettagli
     @return lista di {@link VisualizzaDettagliVeterinariResponseDTO} contenente i dettagli del veterinario*/
    @GetMapping("/visualizzaDettagliVeterinari/{vetId}")
    public ResponseEntity<List<VisualizzaDettagliVeterinariResponseDTO>> visualizzaDettagliVeterinari(@PathVariable Long vetId) {
        List<VisualizzaDettagliVeterinariResponseDTO> dettagliVeterinari = gestioneUtenteService.visualizzaDettagliVeterinari(vetId);
        return ResponseEntity.ok(dettagliVeterinari);}
}
