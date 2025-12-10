package it.safepet.backend.gestioneRecensioni.repository;

import it.safepet.backend.gestioneRecensioni.model.Recensione;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecensioneRepository extends JpaRepository<Recensione, Long> {

    /**
     * Verifica se esiste già una recensione associata a un determinato veterinario
     * e a un determinato proprietario.
     *
     * @param veterinario entità {@link Veterinario} per cui controllare la recensione
     * @param proprietario entità {@link Proprietario} che ha eventualmente lasciato la recensione
     * @return {@code true} se esiste almeno una recensione per la coppia veterinario-proprietario,
     *         {@code false} altrimenti
     */
    boolean existsByVeterinarioAndProprietario(Veterinario veterinario, Proprietario proprietario);

    int countByVeterinarioId(Long veterinarioId);
}
