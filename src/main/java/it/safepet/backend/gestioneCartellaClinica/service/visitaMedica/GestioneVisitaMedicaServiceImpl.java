package it.safepet.backend.gestioneCartellaClinica.service.visitaMedica;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.VisitaMedica;
import it.safepet.backend.gestioneCartellaClinica.repository.VisitaMedicaRepository;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@Validated
public class GestioneVisitaMedicaServiceImpl implements GestioneVisitaMedicaService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VisitaMedicaRepository visitaMedicaRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private ProprietarioRepository proprietarioRepository;

    @Override
    public VisitaMedicaResponseDTO creaVisitaMedica(VisitaMedicaRequestDTO visitaMedicaRequestDTO) {
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();

        if (currentUser == null || !Role.VETERINARIO.equals(currentUser.getRole())) {
            throw new RuntimeException("Accesso non autorizzato");
        }
        Veterinario veterinario = veterinarioRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Veterinario non trovato"));

        Optional<Pet> opPet = petRepository.findById(visitaMedicaRequestDTO.getPetId());
        if (opPet.isEmpty()) {
            throw new RuntimeException("Pet non trovato");
        }

        if (!petRepository.verificaAssociazionePetVeterinario(opPet.get().getId(), veterinario.getId())) {
            throw new RuntimeException("Il pet non è un paziente del veterinario");
        }

        MultipartFile referto = getMultipartFile(visitaMedicaRequestDTO);

        VisitaMedica visitaMedica;
        try {
            visitaMedica = new VisitaMedica();
            visitaMedica.setVeterinario(veterinario);
            visitaMedica.setPet(opPet.get());
            visitaMedica.setNome(visitaMedicaRequestDTO.getNome());
            visitaMedica.setDescrizione(visitaMedicaRequestDTO.getDescrizione());
            visitaMedica.setData(visitaMedicaRequestDTO.getData());
            if (referto != null) {
                visitaMedica.setReferto(visitaMedicaRequestDTO.getReferto().getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore di apertura file");
        }

        VisitaMedica newVisitaMedica = visitaMedicaRepository.save(visitaMedica);


        VisitaMedicaResponseDTO visitaMedicaResponseDTO = new VisitaMedicaResponseDTO(
                newVisitaMedica.getId(),
                newVisitaMedica.getNome(),
                newVisitaMedica.getPet().getId(),
                newVisitaMedica.getVeterinario().getId(),
                newVisitaMedica.getDescrizione(),
                newVisitaMedica.getVeterinario().getNome() + " " +
                        newVisitaMedica.getVeterinario().getCognome(),
                newVisitaMedica.getPet().getNome(),
                newVisitaMedica.getData()
        );

        visitaMedicaResponseDTO.setPresentReferto(referto != null);

        return visitaMedicaResponseDTO;
    }

    private static MultipartFile getMultipartFile(VisitaMedicaRequestDTO visitaMedicaRequestDTO) {
        MultipartFile referto = visitaMedicaRequestDTO.getReferto();

        if (referto != null) {
            if (!"application/pdf".equals(referto.getContentType())) {
                throw new RuntimeException("Il file in input deve essere un PDF");
            }

            if (!Objects.requireNonNull(referto.getOriginalFilename()).toLowerCase().endsWith(".pdf")) {
                throw new RuntimeException("Estensione non valida");
            }

            if (referto.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("PDF oltre i 5MB");
            }
        } //i controlli vengono effettuati solo se il referto esiste
        return referto;
    }

    @Override
    public byte[] leggiPFD(Long id) {
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();

        Optional<Pet> opPet = petRepository.findById(visitaMedicaRepository.findById(id).get().getPet().getId());
        if (opPet.isEmpty()) {
            throw new RuntimeException("Pet non trovato");
        }

        if (currentUser.getRole().equals(Role.VETERINARIO)) {
            Veterinario veterinario = veterinarioRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("Veterinario non trovato"));

            if (!petRepository.verificaAssociazionePetVeterinario(opPet.get().getId(), veterinario.getId())) {
                throw new RuntimeException("Il pet non è un paziente del veterinario");
            }
        } //controllo se il veterinario esiste, e se il pet è suo paziente

        if (currentUser.getRole().equals(Role.PROPRIETARIO)) {
            Proprietario proprietario = proprietarioRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("Proprietario non trovato"));

            if (!petRepository.findById(opPet.get().getId()).get().getProprietario().getId().equals(proprietario.getId())) {
                throw new RuntimeException("Il pet non appartiene all'utente corrente");
            }
        } //controllo se il proprietario esiste e se il pet gli appartiene


        //arrivato qui, se si è un veterinario, il cui pet è suo paziente oppure
        //se si è il proprietario del pet, si può procedere per scaricare il pdf

        Optional<VisitaMedica> visitaMedica = visitaMedicaRepository.findById(id);
        if (visitaMedica.isEmpty()) {
            throw new RuntimeException("Visita non trovata");
        }

        if (visitaMedica.get().getReferto() == null) {
            throw new RuntimeException("Referto non trovato");
        } else {
            return visitaMedica.get().getReferto();
        }

    }




    private VisitaMedicaResponseDTO VisitaMedicatoDTO(VisitaMedica entity) {
            VisitaMedicaResponseDTO dto = new VisitaMedicaResponseDTO();
            dto.setVisitaMedicaId(entity.getId());
            dto.setNome(entity.getNome());
            dto.setPetId(entity.getPet().getId());
            dto.setVeterinarioId(entity.getVeterinario().getId());
            dto.setDescrizione(entity.getDescrizione());
            dto.setNomeCompletoVeterinario(entity.getVeterinario().getNome() + " " + entity.getVeterinario().getCognome());
            dto.setNomePet(entity.getPet().getNome());
            dto.setData(entity.getData());
            //dto.setPresentReferto(entity.getReferto() != null && !entity.getReferto().isEmpty());
            return dto;
        }
    }

