package it.safepet.backend.gestioneCartellaClinica.repository;

import it.safepet.backend.gestioneCartellaClinica.model.Patologia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatologiaRepository extends JpaRepository<Patologia, Long> {
}
