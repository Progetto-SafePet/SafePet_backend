package it.safepet.backend.ReportVeterinariECliniche.repository;

import it.safepet.backend.ReportVeterinariECliniche.model.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
}
