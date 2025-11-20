package it.safepet.backend.reportVeterinariECliniche.service;

import it.safepet.backend.reportVeterinariECliniche.repository.ClinicaRepository;
import it.safepet.backend.reportVeterinariECliniche.repository.OrarioDiAperturaRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ReportVeterinariEClinicheServiceImpl implements ReportVeterinariEClinicheService {
    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private OrarioDiAperturaRepository orarioDiAperturaRepository;
}
