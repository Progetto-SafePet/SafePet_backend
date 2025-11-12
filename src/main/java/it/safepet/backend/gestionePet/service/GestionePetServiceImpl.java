package it.safepet.backend.gestionePet.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.exception.ForbiddenException;
import it.safepet.backend.exception.UnauthorizedException;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.persistence.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestionePetServiceImpl implements GestionePetService {

    @Autowired
    PetRepository petRepository;

    @Override
    public List<PetResponseDTO> visualizzaElencoPet() {
        // 1. Recupera l’utente autenticato
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("Accesso non autorizzato: nessun utente autenticato.");
        }

        // 2. Controllo ruolo
        if (!currentUser.getRole().equals(Role.PROPRIETARIO)) {
            throw new ForbiddenException("Accesso negato: solo i proprietari possono visualizzare i propri pet.");
        }

        // 3. Recupera i pet del proprietario
        return petRepository.findByOwnerId(currentUser.getId())
                .stream()
                .map(p -> new PetResponseDTO(
                        p.getId(),
                        p.getRazza(),
                        p.getNome(),
                        p.getMicrochip(),
                        p.getSesso(),
                        p.getFoto()
                ))
                .toList();
    }
}
