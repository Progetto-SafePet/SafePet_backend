package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePaziente.dto.DettagliResponseDTO;
import it.safepet.backend.gestionePet.model.Pet;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface GestionePazienteService {

    /**
     * Restituisce la lista dei pazienti associati al veterinario attualmente autenticato.
     *
     * @return una lista di PazienteResponseDTO
     * @throws ResponseStatusException se non autenticato, veterinario non trovato o nessun paziente associato
     */
    List<PazienteResponseDTO> visualizzaListaPazienti();

    /**
     * Restituisce tutti i dettagli anagrafici e clinici di un paziente (Pet)
     * associato al veterinario attualmente autenticato.
     *
     * <p>
     * Questo endpoint permette al veterinario di accedere alla scheda completa
     * del paziente selezionato all’interno della propria “Lista Pazienti”.
     * I dati restituiti includono:
     * </p>
     *
     * <ul>
     *     <li>Informazioni anagrafiche (nome, sesso, specie, razza, microchip, ecc.)</li>
     *     <li>Dati fisici (peso, colore del mantello, stato sterilizzazione)</li>
     *     <li>Nome e cognome del proprietario</li>
     *     <li>Fotografia del pet in formato Base64</li>
     *     <li>Cartella clinica completa:
     *         <ul>
     *             <li>Vaccinazioni</li>
     *             <li>Visite mediche</li>
     *             <li>Patologie</li>
     *             <li>Terapie</li>
     *         </ul>
     *     </li>
     *     <li>Note personali del proprietario (se presenti)</li>
     * </ul>
     *
     * <p><b>Autorizzazione:</b></p>
     * <ul>
     *     <li>L’utente deve essere autenticato tramite JWT</li>
     *     <li>Deve essere un veterinario</li>
     *     <li>Il pet richiesto deve essere associato al veterinario</li>
     * </ul>
     *
     * <p><b>Errori restituiti:</b></p>
     * <ul>
     *     <li>401 UNAUTHORIZED → utente non autenticato</li>
     *     <li>404 NOT FOUND → pet inesistente</li>
     *     <li>403 FORBIDDEN → il veterinario non è associato al pet</li>
     * </ul>
     *
     * <p><b>Esempio endpoint:</b></p>
     * <pre>
     * GET /gestionePaziente/dettagli/{petId}
     * </pre>
     *
     * <p><b>Esempio risposta JSON:</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "nome": "Bobby",
     *   "specie": "Cane",
     *   "razza": "Labrador",
     *   "sesso": "M",
     *   "dataNascita": "2020-05-01",
     *   "peso": 30.5,
     *   "coloreMantello": "Miele",
     *   "microchip": "982000411234567",
     *   "sterilizzato": true,
     *   "proprietarioCompleto": "Luigi Verdi",
     *   "fotoBase64": "...",
     *   "cartellaClinica": {
     *       "vaccinazioni": [...],
     *       "visiteMediche": [...],
     *       "patologie": [...],
     *       "terapie": [...]
     *   },
     *   "noteProprietario": [...]
     * }
     * </pre>
     *
     * @param petId identificativo del pet di cui recuperare i dettagli
     * @return {@link DettagliResponseDTO} contenente dati anagrafici, clinici, foto e note
     * @throws org.springframework.web.server.ResponseStatusException se l’utente non è
     *         autenticato, se il pet non esiste o se il veterinario non è autorizzato
     */

    DettagliResponseDTO visualizzaDettagliPaziente(Long petId);

}
