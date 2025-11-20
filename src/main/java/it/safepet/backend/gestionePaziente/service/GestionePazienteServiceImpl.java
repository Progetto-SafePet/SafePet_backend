package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.gestioneCartellaClinica.model.RecordMedico;
import it.safepet.backend.gestioneCartellaClinica.model.VisitaMedica;
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

import java.util.Date;
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
