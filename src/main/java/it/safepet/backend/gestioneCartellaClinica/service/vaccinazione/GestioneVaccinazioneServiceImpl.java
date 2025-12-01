package it.safepet.backend.gestioneCartellaClinica.service.vaccinazione;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.VaccinazioneRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VaccinazioneResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.Vaccinazione;
import it.safepet.backend.gestioneCartellaClinica.model.Vaccinazione.Somministrazione;
import it.safepet.backend.gestioneCartellaClinica.repository.VaccinazioneRepository;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class GestioneVaccinazioneServiceImpl implements GestioneVaccinazioneService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VaccinazioneRepository vaccinazioneRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Override
    @Transactional
    public VaccinazioneResponseDTO aggiungiVaccinazione(Long petId, VaccinazioneRequestDTO dto) {

        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null || !Role.VETERINARIO.equals(currentUser.getRole())) {
            throw new RuntimeException("Accesso non autorizzato");
        }

        Veterinario veterinario = veterinarioRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Veterinario non trovato"));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet non trovato"));

        boolean associato = pet.getVeterinariAssociati().stream()
                .anyMatch(v -> v.getId().equals(veterinario.getId()));

        if (!associato) {
            throw new RuntimeException("Il pet non è un paziente del veterinario");
        }

        Vaccinazione vaccinazione = new Vaccinazione();
        vaccinazione.setVeterinario(veterinario);
        vaccinazione.setPet(pet);

        // CAMPO NOME: viene dal DTO "nomeVaccino"
        vaccinazione.setNome(dto.getNomeVaccino());

        vaccinazione.setTipologia(dto.getTipologia());
        vaccinazione.setDataDiSomministrazione(dto.getDataDiSomministrazione());
        vaccinazione.setDoseSomministrata(dto.getDoseSomministrata());
        vaccinazione.setViaDiSomministrazione(
                Somministrazione.valueOf(dto.getViaDiSomministrazione().toUpperCase())
        );
        vaccinazione.setEffettiCollaterali(dto.getEffettiCollaterali());
        vaccinazione.setRichiamoPrevisto(dto.getRichiamoPrevisto());

        Vaccinazione saved = vaccinazioneRepository.save(vaccinazione);

        return new VaccinazioneResponseDTO(
                saved.getId(),
                saved.getNome(),
                pet.getId(),
                veterinario.getId(),
                saved.getTipologia(),
                saved.getDataDiSomministrazione(),
                saved.getDoseSomministrata(),
                saved.getViaDiSomministrazione().name(),
                saved.getEffettiCollaterali(),
                saved.getRichiamoPrevisto(),
                veterinario.getNome() + " " + veterinario.getCognome()
        );
    }
}
