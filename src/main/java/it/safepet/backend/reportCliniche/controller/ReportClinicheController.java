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


    @GetMapping("/elencoVeterinari")
    public ResponseEntity<List<ElencoResponseDTO>> getElencoVeterinari() {
        return ResponseEntity.ok(reportClinicheService.visualizzaElencoVeterinari());
    }

}
