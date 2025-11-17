package it.safepet.backend.gestionePaziente.repository;

import it.safepet.backend.gestionePaziente.model.LinkingCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkingCodeRepository extends JpaRepository<LinkingCode, Long> {
}

