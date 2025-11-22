package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.exception.ConflictException;
import it.safepet.backend.exception.NotFoundException;
import it.safepet.backend.exception.UnauthorizedException;
import it.safepet.backend.gestionePaziente.dto.PazienteRequestDTO;
import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePaziente.model.LinkingCode;
import it.safepet.backend.gestionePaziente.repository.LinkingCodeRepository;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Validated
public class GestionePazienteServiceImpl implements GestionePazienteService {
    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private LinkingCodeRepository linkingCodeRepository;

    @Transactional
    public PazienteResponseDTO aggiungiPaziente(PazienteRequestDTO paziente) {
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();

        if (currentUser == null || !Role.VETERINARIO.equals(currentUser.getRole())) {
            throw new UnauthorizedException("Accesso non autorizzato");
        }

        Veterinario veterinario = veterinarioRepository.findById(currentUser.getId())
                .orElseThrow(() -> new NotFoundException("Veterinario non trovato"));

        LinkingCode linkingCode = linkingCodeRepository.findByCodice(paziente.getLinkingCode())
                .orElseThrow(() -> new NotFoundException("Linking code non trovato"));

        if (linkingCode.getUsato()) {
            throw new RuntimeException("Linking code già utilizzato");
        }

        if (linkingCode.isScaduto()) {
            throw new RuntimeException("Linking code scaduto in data "
                    + linkingCode.getDataScadenza().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            );
        }

        Pet pet = linkingCode.getPet();
        if (veterinario.getPetsAssociati().contains(pet)) {
            throw new ConflictException(
                    "Pet {" + pet.getId() + "} già associato al veterinario {" + veterinario.getId() + "}"
            );
        }

        veterinario.getPetsAssociati().add(pet);
        pet.getVeterinariAssociati().add(veterinario);
        linkingCode.setUsato(true);
        linkingCodeRepository.save(linkingCode);
        veterinarioRepository.save(veterinario);
        return new PazienteResponseDTO();
    }
}
