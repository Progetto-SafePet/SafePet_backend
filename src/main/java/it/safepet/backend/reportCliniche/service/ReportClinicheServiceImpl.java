package it.safepet.backend.reportCliniche.service;

import it.safepet.backend.reportCliniche.repository.ClinicaRepository;
import it.safepet.backend.reportCliniche.repository.OrarioDiAperturaRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ReportClinicheServiceImpl implements ReportClinicheService {
    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private OrarioDiAperturaRepository orarioDiAperturaRepository;
}
