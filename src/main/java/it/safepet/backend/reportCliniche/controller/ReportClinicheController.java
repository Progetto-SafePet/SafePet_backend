package it.safepet.backend.reportCliniche.controller;

import it.safepet.backend.reportCliniche.service.ReportClinicheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reportCliniche")
public class ReportClinicheController {
    @Autowired
    private ReportClinicheService reportClinicheService;
}
