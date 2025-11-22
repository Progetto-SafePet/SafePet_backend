package it.safepet.backend.reportCliniche.controller;

import it.safepet.backend.reportCliniche.dto.ElencoResponseDTO;
import it.safepet.backend.reportCliniche.service.ReportClinicheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reportCliniche")
public class ReportClinicheController {
    @Autowired
    private ReportClinicheService reportClinicheService;

    /**
     * Restituisce l'elenco completo dei veterinari con le relative informazioni
     * sulla clinica di appartenenza.
     *
     * <p>Il metodo gestisce una richiesta HTTP GET verso l'endpoint
     * {@code /elencoVeterinari} e delega al servizio {@code reportClinicheService}
     * il recupero dei dati. La risposta contiene una lista di oggetti
     * {@link ElencoResponseDTO}, incapsulata in una {@link ResponseEntity} con
     * stato HTTP 200 (OK).</p>
     *
     * <h3>Esempio di richiesta</h3>
     * <pre>
     * GET /elencoVeterinari HTTP/1.1
     * Host: localhost:8080
     * Accept: application/json
     * </pre>
     *
     * <h3>Esempio di risposta</h3>
     * <pre>
     * HTTP/1.1 200 OK
     * Content-Type: application/json
     *
     * [
     *   {
     *     "idVeterinario": 1,
     *     "nomeVeterinario": "Simone",
     *     "cognomeVeterinario": "Cimmino",
     *     "idClinica": 1,
     *     "nomeClinica": "Zampa Felice",
     *     "indirizzoClinica": "Via Giovanni Paolo II, 150 - Fisciano",
     *     "telefonoClinica": "0899911111",
     *     "mediaRecensioni": 3.5
     *   },
     *   {
     *     "idVeterinario": 2,
     *     "nomeVeterinario": "Luca",
     *     "cognomeVeterinario": "Salvatore",
     *     "idClinica": 2,
     *     "nomeClinica": "Animalia",
     *     "indirizzoClinica": "Via Principe Amedeo, 22 - Lancusi",
     *     "telefonoClinica": "0899922222",
     *     "mediaRecensioni": 0.0
     *   }
     *   ...
     * ]
     * </pre>
     *
     * @return {@link ResponseEntity} contenente la lista di {@link ElencoResponseDTO}.
     */

    @GetMapping("/elencoVeterinari")
    public ResponseEntity<List<ElencoResponseDTO>> getElencoVeterinari() {
        return ResponseEntity.ok(reportClinicheService.visualizzaElencoVeterinari());
    }

}
