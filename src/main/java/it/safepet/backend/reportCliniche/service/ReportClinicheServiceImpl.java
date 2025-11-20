package it.safepet.backend.reportCliniche.service;

import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.reportCliniche.dto.ElencoResponseDTO;
import it.safepet.backend.reportCliniche.model.Clinica;
import it.safepet.backend.reportCliniche.repository.ClinicaRepository;
import it.safepet.backend.reportCliniche.repository.OrarioDiAperturaRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class ReportClinicheServiceImpl implements ReportClinicheService {
    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private OrarioDiAperturaRepository orarioDiAperturaRepository;

    public List<ElencoResponseDTO> visualizzaElencoVeterinari(){
            List<Veterinario> veterinari = veterinarioRepository.findAll();
            return veterinari.stream()
                    .map(v -> new ElencoResponseDTO(
                            v.getId(),
                            v.getNome(),
                            v.getCognome(),
                            v.getClinica().getId(),
                            v.getClinica().getNome(),
                            v.getClinica().getIndirizzo(),
                            v.getClinica().getNumeroTelefono()
                    ))
                    .toList();
        }
}
