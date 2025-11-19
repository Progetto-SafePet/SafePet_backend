package it.safepet.backend.gestionePet.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestionePet.dto.NewPetDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.NoteProprietarioRepository;
import it.safepet.backend.gestionePet.repository.PetRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;

@Service
@Validated
public class GestionePetServiceImpl implements GestionePetService {
    @Autowired
    private PetRepository petRepository;
    
    @Autowired
    private NoteProprietarioRepository noteProprietarioRepository;

    @Override
    public PetResponseDTO creaPet(@Valid NewPetDTO newPetDTO) throws IOException {
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Accesso non autorizzato");
        }

        if (!Role.PROPRIETARIO.equals(currentUser.getRole())){
            throw new RuntimeException("Accesso non autorizzato");
        }

        //TODO: controllare che il microchip non esiste già

        Pet pet = new Pet();
        pet.setNome(newPetDTO.getNome());
        pet.setMicrochip(newPetDTO.getMicrochip());
        pet.setColoreMantello(newPetDTO.getColoreMantello());
        pet.setRazza(newPetDTO.getRazza());
        pet.setDataNascita(newPetDTO.getDataNascita());
        pet.setSterilizzato(newPetDTO.getSterilizzato());
        pet.setSesso(newPetDTO.getSesso());

        // 5. Validazione e salvataggio foto
        if (newPetDTO.getFoto() != null && !newPetDTO.getFoto().isEmpty()) {
            String contentType = newPetDTO.getFoto().getContentType();
            if (contentType == null ||
                    !(contentType.equalsIgnoreCase("image/jpeg") ||
                            contentType.equalsIgnoreCase("image/png"))) {
                throw new RuntimeException("Formato immagine non valido: sono ammessi solo JPEG o PNG");
            }
            pet.setFoto(newPetDTO.getFoto().getBytes());
        }

        Pet savedPet = petRepository.save(pet);

        PetResponseDTO petResponseDTO = new PetResponseDTO(
                savedPet.getId(),
                savedPet.getRazza(),
                savedPet.getNome(),
                savedPet.getMicrochip(),
                savedPet.getSesso(),
                savedPet.getFoto()
        );

        return petResponseDTO;
    }
}

