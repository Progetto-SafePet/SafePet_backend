package it.safepet.backend.gestioneRecensioni.repository;

import it.safepet.backend.gestioneRecensioni.model.Recensione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecensioneRepository extends JpaRepository<Recensione, Long> {
}
