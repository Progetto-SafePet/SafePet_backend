package it.safepet.backend.persistence.repository;

import it.safepet.backend.ReportVeterinariECliniche.model.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
}
