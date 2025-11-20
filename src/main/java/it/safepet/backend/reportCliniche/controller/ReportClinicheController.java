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
     * Gestisce la richiesta HTTP GET per ottenere l'elenco dei veterinari.
     * <p>
     * Il metodo delega al servizio {@code reportClinicheService} la logica di recupero
     * dei dati e restituisce una lista di {@link ElencoResponseDTO} incapsulata in una
     * {@link ResponseEntity} con stato HTTP 200 (OK).
     * </p>
     *
     * Endpoint: {@code /elencoVeterinari}
     *
     * @return {@link ResponseEntity} contenente la lista di {@link ElencoResponseDTO}
     */

    @GetMapping("/elencoVeterinari")
    public ResponseEntity<List<ElencoResponseDTO>> getElencoVeterinari() {
        return ResponseEntity.ok(reportClinicheService.visualizzaElencoVeterinari());
    }

}
