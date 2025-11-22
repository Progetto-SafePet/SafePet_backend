package it.safepet.backend.gestionePaziente.repository;

import it.safepet.backend.gestionePaziente.model.LinkingCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkingCodeRepository extends JpaRepository<LinkingCode, Long> {
    Optional<LinkingCode> findByCodice(String codice);
    Optional<LinkingCode> findByPetId(Long petId);
}

