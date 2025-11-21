package it.safepet.backend.gestionePet.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestionePet.dto.NewPetDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.NoteProprietarioRepository;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.List;

@Service
@Validated
public class GestionePetServiceImpl implements GestionePetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ProprietarioRepository proprietarioRepository;

    @Autowired
    private NoteProprietarioRepository noteProprietarioRepository;

    @Override
    public PetResponseDTO creaPet(@Valid NewPetDTO newPetDTO) throws IOException {

        // Verifica autenticazione
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null || !Role.PROPRIETARIO.equals(currentUser.getRole())) {
            throw new RuntimeException("Accesso non autorizzato");
        }

        // Recupera il proprietario dal DB
        Proprietario proprietario = proprietarioRepository
                .findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Proprietario non trovato"));

        //TODO: aggiungere controllo microchip univoco
        if (newPetDTO.getMicrochip() != null) {
            if (petRepository.findByMicrochip(newPetDTO.getMicrochip()).isPresent()) {
                throw new RuntimeException("Microchip già esistente");
            }
        }



        // Creazione pet
        Pet pet = new Pet();
        pet.setNome(newPetDTO.getNome());
        pet.setMicrochip(newPetDTO.getMicrochip());
        pet.setRazza(newPetDTO.getRazza());
        pet.setDataNascita(newPetDTO.getDataNascita());
        pet.setSterilizzato(newPetDTO.getSterilizzato());
        pet.setSesso(newPetDTO.getSesso());
        pet.setSpecie(newPetDTO.getSpecie());
        pet.setColoreMantello(newPetDTO.getColoreMantello());
        pet.setPeso(newPetDTO.getPeso());

        // Associazione proprietario
        pet.setProprietario(proprietario);

        // Validazione foto
        if (newPetDTO.getFoto() != null && !newPetDTO.getFoto().isEmpty()) {
            String contentType = newPetDTO.getFoto().getContentType();

            if (contentType == null ||
                    !(contentType.equalsIgnoreCase("image/jpeg") ||
                            contentType.equalsIgnoreCase("image/png"))) {
                throw new RuntimeException("Formato immagine non valido: sono ammessi JPEG o PNG");
            }

            pet.setFoto(newPetDTO.getFoto().getBytes());
        }

        // Salvataggio
        Pet savedPet = petRepository.save(pet);

        // Response DTO
        return new PetResponseDTO(
                savedPet.getId(),
                savedPet.getNome(),
                savedPet.getSpecie(),
                savedPet.getDataNascita(),
                savedPet.getPeso(),
                savedPet.getColoreMantello(),
                savedPet.getSterilizzato(),
                savedPet.getRazza(),
                savedPet.getMicrochip(),
                savedPet.getSesso(),
                savedPet.getFoto()
        );
    }

    @Override
    public List<PetResponseDTO> visualizzaMieiPet() {
        // Recupera l’utente autenticato
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Accesso non autorizzato: nessun utente autenticato");
        }
        //controllo ruolo
        if (!"PROPRIETARIO".equals(currentUser.getRole().name())) {
            throw new RuntimeException("Accesso negato: solo i proprietari possono visualizzare i propri animali");
        }

        return petRepository.findByProprietario_Id(currentUser.getId())
                .stream()
                .map(p -> new PetResponseDTO(
                        p.getNome(),
                        p.getSpecie(),
                        p.getSesso(),
                        p.getFoto(),
                        p.getDataNascita()
                ))
                .toList();
    }

}

