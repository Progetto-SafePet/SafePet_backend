package it.safepet.backend.persistence.repository;

import it.safepet.backend.persistence.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

}
