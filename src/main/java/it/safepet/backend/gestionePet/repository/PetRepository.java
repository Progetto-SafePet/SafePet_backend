package it.safepet.backend.gestionePet.repository;

import it.safepet.backend.gestionePet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
