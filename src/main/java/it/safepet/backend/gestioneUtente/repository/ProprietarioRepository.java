package it.safepet.backend.gestioneUtente.repository;

import it.safepet.backend.gestioneUtente.model.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {
    Optional<Proprietario> findByEmail(String email);
}
