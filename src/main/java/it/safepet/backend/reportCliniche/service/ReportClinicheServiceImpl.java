package it.safepet.backend.reportCliniche.service;

import it.safepet.backend.gestioneRecensioni.model.Recensione;
import it.safepet.backend.gestioneRecensioni.repository.RecensioneRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.reportCliniche.dto.InfoClinicheDTO;
import it.safepet.backend.reportCliniche.dto.ElencoResponseDTO;
import it.safepet.backend.reportCliniche.dto.OrariClinicaResponseDTO;
import it.safepet.backend.reportCliniche.model.Clinica;
import it.safepet.backend.reportCliniche.repository.ClinicaRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private RecensioneRepository recensioneRepository;

    @Transactional(readOnly = true)
    public List<ElencoResponseDTO> visualizzaElencoVeterinari() {
        List<Veterinario> veterinari = veterinarioRepository.findAll();
        return veterinari.stream()
                .map(v -> {
                    double mediaRecensioni = v.getRecensioni().isEmpty() ? 0 :
                            v.getRecensioni().stream()
                                    .mapToInt(Recensione::getPunteggio)
                                    .average()
                                    .orElse(0);

                    return new ElencoResponseDTO(
                            v.getId(),
                            v.getNome(),
                            v.getCognome(),
                            v.getClinica().getId(),
                            v.getClinica().getNome(),
                            v.getClinica().getIndirizzo(),
                            v.getClinica().getNumeroTelefono(),
                            mediaRecensioni
                    );
                })
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<InfoClinicheDTO> prelevaDatiMappa() {
        List<Clinica> cliniche = clinicaRepository.findAll();
        return cliniche.stream()
                .map(c -> new InfoClinicheDTO(
                        c.getId(),
                        c.getNome(),
                        c.getIndirizzo(),
                        c.getNumeroTelefono(),
                        c.getVeterinario().getId(),
                        c.getVeterinario().getNome(),
                        c.getVeterinario().getCognome(),
                        recensioneRepository.countByVeterinarioId(c.getVeterinario().getId()),
                        veterinarioRepository.calcolaMediaRecensioniVeterinario(c.getVeterinario().getId()),
                        c.getLatitudine(),
                        c.getLongitudine(),
                        (c.getOrariApertura().stream()
                                .map(o -> new OrariClinicaResponseDTO(
                                        o.getGiorno(),
                                        o.getOrarioApertura(),
                                        o.getOrarioChiusura(),
                                        o.getAperto24h()
                                ))
                                .toList())
                ))
                .toList();
    }
}
