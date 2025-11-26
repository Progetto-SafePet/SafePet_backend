package it.safepet.backend.gestioneCartellaClinica.service.patologia;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.PatologiaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.PatologiaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.Patologia;
import it.safepet.backend.gestioneCartellaClinica.repository.PatologiaRepository;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class GestionePatologiaServiceImpl implements GestionePatologiaService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PatologiaRepository patologiaRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Override
    @Transactional
    public PatologiaResponseDTO creaPatologia(PatologiaRequestDTO richiesta) {

        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null || !Role.VETERINARIO.equals(currentUser.getRole())) {
            throw new RuntimeException("Accesso non autorizzato");
        }

        Veterinario veterinario = veterinarioRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Veterinario non trovato"));

        Pet pet = petRepository.findById(richiesta.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet non trovato"));

        if (!petRepository.verificaAssociazionePetVeterinario(pet.getId(), veterinario.getId())) {
            throw new RuntimeException("Il pet non è un paziente del veterinario");
        }

        Patologia patologia = new Patologia();
        patologia.setNome(richiesta.getNome());
        patologia.setDataDiDiagnosi(richiesta.getDataDiDiagnosi());
        patologia.setSintomiOsservati(richiesta.getSintomiOsservati());
        patologia.setDiagnosi(richiesta.getDiagnosi());
        patologia.setTerapiaAssociata(richiesta.getTerapiaAssociata());
        patologia.setPet(pet);
        patologia.setVeterinario(veterinario);

        Patologia nuovaPatologia = patologiaRepository.save(patologia);

        return new PatologiaResponseDTO(
                nuovaPatologia.getId(),
                nuovaPatologia.getNome(),
                nuovaPatologia.getDataDiDiagnosi(),
                nuovaPatologia.getSintomiOsservati(),
                nuovaPatologia.getDiagnosi(),
                nuovaPatologia.getTerapiaAssociata(),
                nuovaPatologia.getPet().getId(),
                nuovaPatologia.getVeterinario().getId(),
                nuovaPatologia.getVeterinario().getNome() + " " + nuovaPatologia.getVeterinario().getCognome()
        );
    }
}
