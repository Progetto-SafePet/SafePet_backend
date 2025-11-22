package it.safepet.backend.gestioneUtente.controller;

import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
import it.safepet.backend.gestioneUtente.service.GestioneUtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
