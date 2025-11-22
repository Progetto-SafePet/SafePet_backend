package it.safepet.backend.reportCliniche.service;

import it.safepet.backend.reportCliniche.dto.ElencoResponseDTO;

import java.util.List;

public interface ReportClinicheService {

    /**
     * Recupera l'elenco dei veterinari dal database e costruisce una lista di DTO
     * contenenti le informazioni necessarie per il frontend.
     * <p>
     * Per ogni veterinario vengono incluse:
     * - Dati anagrafici (id, nome, cognome)
     * - Informazioni sulla clinica associata (id, nome, indirizzo, numero telefono)
     * - Media delle recensioni (calcolata sui punteggi delle recensioni associate)
     * </p>
     *
     * Il metodo è annotato con {@code @Transactional(readOnly = true)} per mantenere
     * aperta la sessione Hibernate durante l'accesso alle collezioni lazy.
     *
     * @return lista di {@link ElencoResponseDTO} con i dati aggregati di veterinario e clinica
     */
    List<ElencoResponseDTO> visualizzaElencoVeterinari();
}
