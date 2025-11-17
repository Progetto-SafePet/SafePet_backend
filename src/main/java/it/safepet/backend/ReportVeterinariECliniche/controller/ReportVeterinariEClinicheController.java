package it.safepet.backend.ReportVeterinariECliniche.controller;

import it.safepet.backend.ReportVeterinariECliniche.service.ReportVeterinariEClinicheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ReportVeterinariECliniche")
public class ReportVeterinariEClinicheController {
    @Autowired
    private ReportVeterinariEClinicheService reportVeterinariEClinicheService;
}
