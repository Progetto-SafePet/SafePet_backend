package it.safepet.backend.gestioneUtente.controller;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneUtente.dto.ProfiloProprietarioResponseDTO;
import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
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
        // Verifica autenticazione
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Verifica autorizzazione: solo proprietari
        if (currentUser.getRole() != Role.PROPRIETARIO) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Recupera e ritorna il profilo dell'utente autenticato
        ProfiloProprietarioResponseDTO profilo = gestioneUtenteService.visualizzaProfiloProprietario(currentUser.getId());
        return ResponseEntity.ok(profilo);
    }
}
