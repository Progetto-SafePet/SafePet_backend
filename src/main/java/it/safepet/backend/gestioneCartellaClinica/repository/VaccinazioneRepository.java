package it.safepet.backend.gestioneCartellaClinica.repository;

import it.safepet.backend.gestioneCartellaClinica.model.Vaccinazione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinazioneRepository extends JpaRepository<Vaccinazione, Integer> {
}
