package it.safepet.backend.gestioneUtente.repository;

import it.safepet.backend.gestioneUtente.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    Optional<Veterinario> findByEmail(String email);
}
