package it.safepet.backend.gestioneCartellaClinica.service.terapia;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.TerapiaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.TerapiaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.Terapia;
import it.safepet.backend.gestioneCartellaClinica.repository.TerapiaRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;

public interface GestioneTerapiaService {
    /**
     * Aggiunge una nuova terapia alla cartella clinica di un pet, verificando che
     * l'utente autenticato sia effettivamente un veterinario e che il pet sia uno dei
     * suoi pazienti. Il metodo esegue tutte le operazioni applicative e di validazione
     * necessarie per garantire la correttezza dello use case "Aggiungi Terapia".
     *
     * <p>Il flusso operativo prevede:</p>
     * <ul>
     *   <li>recupero dell'utente autenticato tramite {@link AuthContext};</li>
     *   <li>verifica che l'utente sia un veterinario (ruolo {@link Role#VETERINARIO});</li>
     *   <li>recupero del veterinario dal database tramite {@link VeterinarioRepository};</li>
     *   <li>recupero del pet indicato nel DTO e verifica dell'esistenza nel sistema;</li>
     *   <li>controllo dell'associazione pet–veterinario tramite
     *       {@code petRepository.verificaAssociazionePetVeterinario(...)};
     *       se non associato viene lanciata una eccezione;</li>
     *   <li>creazione e popolamento dell'entity {@link Terapia};</li>
     *   <li>salvataggio della terapia tramite {@link TerapiaRepository};</li>
     *   <li>costruzione e restituzione del {@link TerapiaResponseDTO} contenente
     *       i dettagli completi della terapia creata.</li>
     * </ul>
     *
     * <p>L'operazione è eseguita in transazione grazie all'annotazione
     * {@link jakarta.transaction.Transactional}, garantendo atomicità
     * dell'inserimento.</p>
     *
     * Un oggetto dto {@link TerapiaRequestDTO} contenente i dati della terapia
     *            da registrare (nome, forma farmaceutica, dosaggio, posologia,
     *            via di somministrazione, durata, frequenza, motivo e petId)
     *
     * @return un {@link TerapiaResponseDTO} contenente le informazioni della terapia
     *         appena creata, inclusi id della terapia, id del pet, id del veterinario
     *         e nome completo del veterinario che l’ha registrata
     *
     * @throws RuntimeException se:
     *         <ul>
     *           <li>l'utente non è autenticato;</li>
     *           <li>l'utente autenticato non ha ruolo di veterinario;</li>
     *           <li>il veterinario non esiste nel database;</li>
     *           <li>il pet non esiste nel database;</li>
     *           <li>il pet non è associato al veterinario autenticato.</li>
     *         </ul>
     */
    TerapiaResponseDTO aggiungiTerapia( TerapiaRequestDTO dto);
}
