package it.safepet.backend.reportCliniche.service;

import it.safepet.backend.reportCliniche.dto.InfoClinicheDTO;
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

    /**
     * Recupera le cinque cliniche più vicine alla posizione geografica specificata.
     *
     * <p>Questo metodo elabora le coordinate fornite (latitudine e longitudine) per
     * identificare e restituire le cliniche limitrofe, includendo i dati necessari
     * per la geolocalizzazione su mappa (come indirizzo e coordinate) e le statistiche
     * del veterinario associato.</p>
     *
     * @param lat La latitudine della posizione attuale dell'utente. Range di valori ammessi: [-90, 90]
     * @param lon La longitudine della posizione attuale dell'utente. Range di valori ammessi: [-180, 180]
     * @return Una lista di {@link InfoClinicheDTO} contenente le informazioni delle cliniche
     * ordinate per vicinanza.
     */
    List<InfoClinicheDTO> prelevaDatiMappa(Double lat, Double lon);
}
