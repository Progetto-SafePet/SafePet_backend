package it.safepet.backend.gestioneCondivisioneDati.service;

import it.safepet.backend.gestioneCondivisioneDati.dto.CondivisioneDatiPetResponseDTO;
import it.safepet.backend.gestioneCondivisioneDati.html.LibrettoPetHtmlBuilder;

public interface GestioneCondivisioneDatiService {
    /**
     * Recupera i dati completi di un pet, aggregando informazioni
     * dal pet stesso, dalla cartella clinica e dai dati del proprietario.
     * <p>
     * Effettua controlli di autorizzazione:
     * solo il proprietario del pet autenticato può accedere ai dati.
     *
     * @param petId l'identificativo univoco del pet
     * @return un {@link CondivisioneDatiPetResponseDTO} contenente
     *         i dati anagrafici del pet, la cartella clinica e i dati del proprietario
     * @throws RuntimeException se l'utente non è autenticato, non è il proprietario
     *                          del pet, o se il pet non viene trovato
     */
    CondivisioneDatiPetResponseDTO getDatiCompletiPet(Long petId);

    /**
     * Genera un PDF contenente la scheda completa di un pet, comprensiva di dati anagrafici,
     * contatti del proprietario e storico clinico (vaccinazioni, patologie, terapie e visite mediche).
     * <p>
     * Il PDF viene generato partendo da un HTML costruito con {@link LibrettoPetHtmlBuilder} e convertito
     * in PDF tramite la libreria OpenHTMLToPDF (PdfRendererBuilder).
     * </p>
     * <p>
     * Prima di generare il PDF, viene eseguito un controllo di autorizzazione:
     * solo il proprietario del pet autenticato può accedere ai dati.
     * </p>
     *
     * @param petId l'ID del pet di cui generare il PDF
     * @return un array di byte contenente il PDF generato
     * @throws RuntimeException se:
     *         <ul>
     *             <li>il pet non esiste</li>
     *             <li>l'utente corrente non è autorizzato ad accedere ai dati</li>
     *             <li>si verifica un errore durante la generazione del PDF</li>
     *         </ul>
     */
    byte[] generaPdfPet(Long petId);
}
