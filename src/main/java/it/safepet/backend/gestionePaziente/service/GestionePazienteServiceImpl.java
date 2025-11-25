package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePaziente.dto.DettagliResponseDTO;
import it.safepet.backend.gestionePaziente.repository.LinkingCodeRepository;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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


    @Override
    @Transactional
    public List<PazienteResponseDTO> visualizzaListaPazienti() {

        // Recupera l'utente loggato
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();

        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non autenticato.");
        }

        // Estrai ID del veterinario dal token
        Long idVeterinario = currentUser.getId();

        // Recupero del veterinario
        Veterinario vet = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Veterinario non trovato."
                ));

        // Recupero lista pazienti associati
        List<Pet> pets = vet.getPetsAssociati();

        // Nessun paziente associato → 404
        if (pets.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nessun paziente associato a questo veterinario."
            );
        }

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
                pet.getId(),
                pet.getSpecie(),
                pet.getNome(),
                pet.getDataNascita(),
                proprietario,
                pet.getSesso(),
                pet.getFoto()
        );
    }

    @Override
    @Transactional
    public DettagliResponseDTO visualizzaDettagliPaziente(Long petId) {

        // 1. Recupera l'utente loggato
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non autenticato.");
        }

        // 2. Recupera il Pet
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pet non trovato."
                ));

        // 3. Controllo autorizzazione → il veterinario deve essere associato al pet
        boolean autorizzato = pet.getVeterinariAssociati().stream()
                .anyMatch(v -> v.getId().equals(currentUser.getId()));

        if (!autorizzato) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Non puoi visualizzare i dettagli di un animale che non segui."
            );
        }

        // 4. Nome completo del proprietario
        String proprietario = pet.getProprietario() != null
                ? pet.getProprietario().getNome() + " " + pet.getProprietario().getCognome()
                : "Sconosciuto";

        // 5. Conversione in DTO
        return new DettagliResponseDTO(
                pet.getId(),
                pet.getNome(),
                pet.getSesso(),
                pet.getSpecie(),
                pet.getRazza(),
                pet.getDataNascita(),
                pet.getPeso(),
                pet.getColoreMantello(),
                pet.getMicrochip(),
                pet.getSterilizzato(),
                proprietario,
                pet.getFoto()
        );
    }

}
