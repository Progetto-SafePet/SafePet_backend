package it.safepet.backend.gestioneCartellaClinica.service;

import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;

import java.util.List;

public interface GestioneCartellaClinicaService {
    /**
     * Recupera la cartella clinica completa di un pet, verificando che l'utente autenticato
     * sia autorizzato ad accedere ai relativi dati (proprietario del pet o veterinario associato).
     *
     * @param petId identificativo del pet di cui recuperare la cartella clinica.
     * @return un {@link CartellaClinicaResponseDTO} contenente vaccinazioni, visite mediche,
     *         patologie e terapie associate al pet.
     *
     * @throws RuntimeException se l'utente non è autenticato, se il pet non esiste
     *                          oppure se l'accesso non è autorizzato.
     */
    CartellaClinicaResponseDTO getCartellaClinica(Long petId);
}
