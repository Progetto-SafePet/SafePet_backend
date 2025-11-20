package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePaziente.repository.LinkingCodeRepository;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class GestionePazienteServiceImpl implements GestionePazienteService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private LinkingCodeRepository linkingCodeRepository;

    private final VeterinarioRepository veterinarioRepository;

    public GestionePazienteServiceImpl(VeterinarioRepository veterinarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
    }

    /**
     * Restituisce la lista dei pazienti associati al veterinario attualmente autenticato.
     *
     * <p>Il metodo esegue i seguenti passaggi:</p>
     * <ul>
     *     <li>Recupera l'utente autenticato tramite {@link AuthContext#getCurrentUser()}.</li>
     *     <li>Verifica che l'utente sia autenticato, altrimenti solleva una {@link RuntimeException}.</li>
     *     <li>Recupera l'entità {@link Veterinario} dal database tramite il suo ID.</li>
     *     <li>Ottiene la lista dei {@link Pet} associati al veterinario.</li>
     *     <li>Converte ogni pet in {@link PazienteResponseDTO}.</li>
     * </ul>
     *
     * @return una lista di {@link PazienteResponseDTO} contenente i dati dei pazienti del veterinario
     *
     * @throws RuntimeException se l'utente non è autenticato o il veterinario non viene trovato nel database
     */
    @Override
    @Transactional
    public List<PazienteResponseDTO> visualizzaListaPazienti() {

        // Recupera l'utente loggato dal AuthContext
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("Utente non autenticato");
        }

        // Estrai ID del veterinario dal contesto
        Long idVeterinario = currentUser.getId();

        // Recupero l'entità Veterinario dal DB
        Veterinario vet = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new RuntimeException("Veterinario non trovato"));

        // Recupero i suoi pet associati
        List<Pet> pets = vet.getPetsAssociati();

        // Conversione in DTO
        return pets.stream()
                .map(this::convertPetToDTO)
                .collect(Collectors.toList());
    }




    private PazienteResponseDTO convertPetToDTO(Pet pet) {

        String proprietario = pet.getProprietario() != null
                ? pet.getProprietario().getNome() + " " + pet.getProprietario().getCognome()
                : "Sconosciuto";


        return new PazienteResponseDTO(
                pet.getSpecie(),
                pet.getNome(),
                pet.getDataNascita(),
                proprietario,
                pet.getSesso(),
                pet.getFoto()
        );
    }
}
