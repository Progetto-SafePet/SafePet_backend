package it.safepet.backend.gestionePet.repository;

import it.safepet.backend.gestionePet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByMicrochip(String microchip);
    List<Pet> findByProprietario_Id(Long ownerId);
}
