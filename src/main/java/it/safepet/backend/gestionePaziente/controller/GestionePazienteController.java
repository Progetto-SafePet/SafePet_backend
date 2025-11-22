package it.safepet.backend.gestionePaziente.controller;

import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePaziente.service.GestionePazienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/gestionePaziente")
public class GestionePazienteController {
    @Autowired
    private GestionePazienteService gestionePazienteService;

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
