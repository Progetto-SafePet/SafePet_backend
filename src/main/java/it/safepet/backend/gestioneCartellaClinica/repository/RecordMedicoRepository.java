package it.safepet.backend.gestioneCartellaClinica.repository;

import it.safepet.backend.gestioneCartellaClinica.model.RecordMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordMedicoRepository extends JpaRepository<RecordMedico, Long> {
    List<RecordMedico> findByPetId(Long petId);
}
