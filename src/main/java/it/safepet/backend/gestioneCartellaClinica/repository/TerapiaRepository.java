package it.safepet.backend.gestioneCartellaClinica.repository;

import it.safepet.backend.gestioneCartellaClinica.model.Terapia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerapiaRepository extends JpaRepository<Terapia, Integer> {
}
