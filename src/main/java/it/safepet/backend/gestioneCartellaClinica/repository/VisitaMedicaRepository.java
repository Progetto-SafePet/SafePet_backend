package it.safepet.backend.gestioneCartellaClinica.repository;

import it.safepet.backend.gestioneCartellaClinica.model.VisitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitaMedicaRepository extends JpaRepository<VisitaMedica, Integer> {
}
