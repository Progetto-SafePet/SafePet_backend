package it.safepet.backend.gestioneAmministrativa.repository;

import it.safepet.backend.gestioneAmministrativa.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
