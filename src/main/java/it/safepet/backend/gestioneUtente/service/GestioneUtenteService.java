package it.safepet.backend.gestioneUtente.service;

import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
import it.safepet.backend.gestioneUtente.dto.VisualizzaDettagliVeterinariResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface GestioneUtenteService {
    /**
     * Registra un nuovo proprietario nel sistema.
     *
     * @param registrazioneDTO DTO contenente i dati di registrazione del proprietario
     * @throws IllegalArgumentException se i dati di registrazione non sono validi
     */
    void registraProprietario(@Valid RegistrazioneProprietarioRequestDTO registrazioneDTO);

    /**
     * Recupera i dettagli completi di un veterinario, inclusi informazioni personali,
     * contatti, clinica associata e media delle recensioni ricevute.
     *
     * @param idVet identificativo del veterinario di cui recuperare i dettagli
     * @return una lista contenente un unico {@link VisualizzaDettagliVeterinariResponseDTO}
     *         con tutte le informazioni del veterinario richiesto
     */
    List<VisualizzaDettagliVeterinariResponseDTO> visualizzaDettagliVeterinari(Long idVet);
}
