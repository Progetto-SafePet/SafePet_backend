package it.safepet.backend.gestioneUtente.repository;

import it.safepet.backend.gestioneUtente.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    Optional<Veterinario> findByEmail(String email);

    /**
     * Calcola la media dei punteggi delle recensioni associate a un determinato
     * veterinario. La query effettua una aggregazione sulle entità {@code Recensione}
     * filtrando in base all’identificativo del veterinario fornito.
     *
     * <p>Viene utilizzata la funzione SQL {@code AVG()} per ottenere la media dei
     * punteggi. Nel caso in cui il veterinario non abbia ancora ricevuto alcuna
     * recensione, la funzione restituisce {@code 0} grazie all’utilizzo di
     * {@code COALESCE()}.</p>
     *
     * @param veterinarioId l’identificativo del veterinario di cui calcolare la media recensioni
     * @return la media numerica dei punteggi delle recensioni del veterinario, oppure {@code 0} se non ne esistono
     */
    @Query("""
    SELECT COALESCE(AVG(r.punteggio), 0)
    FROM Recensione r
    WHERE r.veterinario.id = :veterinarioId
""")
    Double calcolaMediaRecensioniVeterinario(@Param("veterinarioId") Long veterinarioId);

}
