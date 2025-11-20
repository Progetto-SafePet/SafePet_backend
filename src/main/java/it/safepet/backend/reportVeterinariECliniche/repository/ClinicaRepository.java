package it.safepet.backend.reportVeterinariECliniche.repository;

import it.safepet.backend.reportVeterinariECliniche.model.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
}
