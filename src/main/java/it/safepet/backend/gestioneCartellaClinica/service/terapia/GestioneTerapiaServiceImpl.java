package it.safepet.backend.gestioneCartellaClinica.service.terapia;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.TerapiaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.TerapiaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.Terapia;
import it.safepet.backend.gestioneCartellaClinica.repository.TerapiaRepository;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class GestioneTerapiaServiceImpl implements GestioneTerapiaService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TerapiaRepository terapiaRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Override
    @Transactional
    public TerapiaResponseDTO aggiungiTerapia(TerapiaRequestDTO dto) {
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();

        if (currentUser == null || !Role.VETERINARIO.equals(currentUser.getRole())) {
            throw new RuntimeException("Accesso non autorizzato");
        }
        Veterinario veterinario = veterinarioRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Veterinario non trovato"));

        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet non trovato"));

        if (!petRepository.verificaAssociazionePetVeterinario(pet.getId(), veterinario.getId())) {
            throw new RuntimeException("Il pet non è un paziente del veterinario");
        }

        Terapia terapia = new Terapia();
        terapia.setVeterinario(veterinario);
        terapia.setPet(pet);
        terapia.setNome(dto.getNome());
        terapia.setFormaFarmaceutica(dto.getFormaFarmaceutica());
        terapia.setDosaggio(dto.getDosaggio());
        terapia.setPosologia(dto.getPosologia());
        terapia.setViaDiSomministrazione(dto.getViaDiSomministrazione());
        terapia.setDurata(dto.getDurata());
        terapia.setFrequenza(dto.getFrequenza());
        terapia.setMotivo(dto.getMotivo());

        Terapia saved = terapiaRepository.save(terapia);

        return new TerapiaResponseDTO(
                saved.getId(),
                saved.getNome(),
                saved.getPet().getId(),
                veterinario.getId(),
                saved.getFormaFarmaceutica(),
                saved.getDosaggio(),
                saved.getPosologia(),
                saved.getViaDiSomministrazione(),
                saved.getDurata(),
                saved.getFrequenza(),
                saved.getMotivo(),
                veterinario.getNome() + " " + veterinario.getCognome()
        );
    }
}
