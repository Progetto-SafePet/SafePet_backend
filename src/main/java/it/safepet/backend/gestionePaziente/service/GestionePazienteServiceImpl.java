package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.service.GestioneCartellaClinicaService;
import it.safepet.backend.gestionePaziente.dto.DettagliResponseDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePaziente.repository.LinkingCodeRepository;
import it.safepet.backend.gestionePet.dto.InserimentoNoteResponseDTO;
import it.safepet.backend.gestionePet.repository.NoteProprietarioRepository;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class GestionePazienteServiceImpl implements GestionePazienteService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private LinkingCodeRepository linkingCodeRepository;

    @Autowired
    private NoteProprietarioRepository noteRepository;

    @Autowired
    private GestioneCartellaClinicaService gestioneCartellaClinicaService;

    private final VeterinarioRepository veterinarioRepository;

    public GestionePazienteServiceImpl(VeterinarioRepository veterinarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
    }

    // ================================================================
    //   LISTA PAZIENTI (UGUALE A PRIMA)
    // ================================================================
    @Override
    @Transactional
    public List<PazienteResponseDTO> visualizzaListaPazienti() {
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();

        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non autenticato.");
        }

        Long idVeterinario = currentUser.getId();

        Veterinario vet = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Veterinario non trovato."
                ));

        List<Pet> pets = vet.getPetsAssociati();

        if (pets.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nessun paziente associato a questo veterinario."
            );
        }

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
    @Transactional(readOnly = true)
    public DettagliResponseDTO visualizzaDettagliPaziente(Long petId) {

        // 1. Utente loggato
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non autenticato.");
        }

        // 2. Recupero pet
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pet non trovato."
                ));

        // 3. Controllo autorizzazione
        boolean autorizzato = pet.getVeterinariAssociati().stream()
                .anyMatch(v -> v.getId().equals(currentUser.getId()));

        if (!autorizzato) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Non puoi visualizzare un animale che non segui."
            );
        }

        // 4. Nome proprietario
        String proprietario = pet.getProprietario() != null
                ? pet.getProprietario().getNome() + " " + pet.getProprietario().getCognome()
                : "Sconosciuto";

        // 5. Cartella clinica completa
        CartellaClinicaResponseDTO cartellaClinica =
                gestioneCartellaClinicaService.getCartellaClinica(petId);

        // 6. Note del proprietario
        List<InserimentoNoteResponseDTO> note =
                noteRepository.findByPetId(petId).stream()
                        .map(n -> new InserimentoNoteResponseDTO(
                                n.getId(),
                                n.getTitolo(),
                                n.getDescrizione(),
                                pet.getId(),
                                pet.getNome(),
                                pet.getProprietario().getId(),
                                proprietario
                        )).collect(Collectors.toList());

        // 7. Restituzione DTO completo
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
                pet.getFoto(),
                cartellaClinica,
                note
        );
    }
}
