package it.safepet.backend.gestionePaziente.controller;

import it.safepet.backend.gestionePaziente.dto.LinkingCodeRequestDTO;
import it.safepet.backend.gestionePaziente.dto.LinkingCodeResponseDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteRequestDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePaziente.service.GestionePazienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestionePaziente")
public class GestionePazienteController {
    @Autowired
    private GestionePazienteService gestionePazienteService;

    @PostMapping("/aggiungiPaziente")
    public void aggiungiPaziente (@RequestBody PazienteRequestDTO pazienteRequestDTO) {
        gestionePazienteService.aggiungiPaziente(pazienteRequestDTO);
    }

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
