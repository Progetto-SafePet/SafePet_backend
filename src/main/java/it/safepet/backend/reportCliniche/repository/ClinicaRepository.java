package it.safepet.backend.reportCliniche.repository;

import it.safepet.backend.reportCliniche.model.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
}
