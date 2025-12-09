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

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    public List<InfoClinicheDTO> prelevaDatiMappa(Double lat, Double lon) {
        if (lat < -90.f || lat > 90.f) {
            throw new IllegalArgumentException("La latitudine ricevuta non è compresa tra -90 e 90 gradi.");
        }

        if (lon < -180.f || lon > 180.f) {
            throw new IllegalArgumentException("La longitudine ricevuta non è compresa tra -180 e 180 gradi.");
        }

        List<Clinica> cliniche = clinicaRepository.findAll();
        return cliniche.stream()
                .map(c -> new AbstractMap.SimpleEntry<>(
                        c, haversineKm(lat, lon, c.getLatitudine(), c.getLongitudine())
                ))
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .limit(5)
                .map(entry -> {
                    Clinica c = entry.getKey();
                    return new InfoClinicheDTO(
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
                            c.getOrariApertura().stream()
                                    .map(o -> new OrariClinicaResponseDTO(
                                            o.getGiorno(),
                                            o.getOrarioApertura(),
                                            o.getOrarioChiusura(),
                                            o.getAperto24h()
                                    ))
                                    .toList()
                    );
                })
                .toList();
    }

    /**
     * Calcola la distanza in km tra due punti (lat, lon) usando la formula di Haversine.
     */
    private static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // raggio medio Terra in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}


