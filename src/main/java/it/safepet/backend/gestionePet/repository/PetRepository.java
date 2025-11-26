package it.safepet.backend.gestionePet.repository;

import it.safepet.backend.gestionePet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    /**
     * Verifica se un determinato {@code Pet} risulta associato ad uno specifico
     * veterinario. L’associazione viene controllata interrogando la relazione
     * tra l'entità {@code Pet} e la collezione dei veterinari ad esso associati.
     *
     * <p>La query restituisce {@code true} se esiste almeno una corrispondenza tra
     * il pet identificato da {@code petId} e il veterinario identificato da
     * {@code veterinarioId}; in caso contrario restituisce {@code false}.</p>
     *
     * @param petId          l'identificativo del pet di cui verificare l’associazione
     * @param veterinarioId  l'identificativo del veterinario da controllare
     * @return {@code true} se il pet è associato al veterinario, {@code false} altrimenti
     */
    @Query("""
        SELECT COUNT(p) > 0
        FROM Pet p
        JOIN p.veterinariAssociati v
        WHERE p.id = :petId
        AND v.id = :veterinarioId
    """)
    boolean verificaAssociazionePetVeterinario(@Param("petId") Long petId, @Param("veterinarioId") Long veterinarioId);
    Optional<Pet> findByMicrochip(String microchip);
    List<Pet> findByProprietario_Id(Long ownerId);
}
