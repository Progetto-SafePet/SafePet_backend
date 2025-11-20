package it.safepet.backend.reportVeterinariECliniche.controller;

import it.safepet.backend.reportVeterinariECliniche.service.ReportVeterinariEClinicheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ReportVeterinariECliniche")
public class ReportVeterinariEClinicheController {
    @Autowired
    private ReportVeterinariEClinicheService reportVeterinariEClinicheService;
}
