package it.safepet.backend.gestioneCartellaClinica.service.visitaMedica;

import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaResponseDTO;
import jakarta.validation.Valid;

public interface GestioneVisitaMedicaService {
    /**
     * Crea una nuova visita medica per un pet esistente, validando i dati ricevuti ed
     * eventualmente salvando il referto PDF allegato. Restituisce i dettagli completi della
     * visita appena registrata.
     *
     * @param visitaMedicaDTO DTO contenente nome, descrizione, data della visita,
     *                        identificativo del pet e un eventuale file PDF di referto
     * @return VisitaMedicaResponseDTO con le informazioni della visita creata,
     *         incluso il nome completo del veterinario, il nome del pet e una variabile booleana
     *         che può assumere valore true(se il referto è presente) o false(se il referto non è presente)
     * @throws RuntimeException se il pet non esiste o se si verificano errori nel salvataggio
     * @throws IllegalArgumentException se il pdf inserito supera la dimensione massima di 5 Mb o se la data inserita è una data futura
     */
    VisitaMedicaResponseDTO creaVisitaMedica(@Valid VisitaMedicaRequestDTO visitaMedicaDTO);
    /**
     * Recupera il referto PDF associato a una visita medica già registrata,
     * restituendone il contenuto scaricabile in formato Base64.
     *
     * @param id identificativo della visita medica
     * @return VisitaMedicaResponseDTO contenente il referto PDF scaricabile, codificato in Base64
     * @throws RuntimeException se la visita medica non esiste o non contiene alcun PDF
     */
    byte[] leggiPFD(Long id);
}
