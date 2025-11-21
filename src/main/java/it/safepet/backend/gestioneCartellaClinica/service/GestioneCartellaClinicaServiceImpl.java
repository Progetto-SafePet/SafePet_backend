package it.safepet.backend.gestioneCartellaClinica.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.VisitaMedica;
import it.safepet.backend.gestioneCartellaClinica.repository.PatologiaRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.RecordMedicoRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.TerapiaRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.VaccinazioneRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.VisitaMedicaRepository;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@Validated
public class GestioneCartellaClinicaServiceImpl implements GestioneCartellaClinicaService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    PatologiaRepository patologiaRepository;

    @Autowired
    RecordMedicoRepository recordMedicoRepository;

    @Autowired
    TerapiaRepository terapiaRepository;

    @Autowired
    VaccinazioneRepository vaccinazioneRepository;

    @Autowired
    VisitaMedicaRepository visitaMedicaRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

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

        MultipartFile referto = visitaMedicaRequestDTO.getReferto();

        if (!"application/pdf".equals(referto.getContentType())) {
            throw new RuntimeException("Il file in input deve essere un PDF");
        }

        if (!Objects.requireNonNull(referto.getOriginalFilename()).toLowerCase().endsWith(".pdf")) {
            throw new RuntimeException("Estensione non valida");
        }

        if (referto.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("PDF oltre i 5MB");
        }

        VisitaMedica visitaMedica = null;
        try {
            visitaMedica = new VisitaMedica();
            visitaMedica.setVeterinario(veterinario);
            visitaMedica.setPet(opPet.get());
            visitaMedica.setNome(visitaMedicaRequestDTO.getNome());
            visitaMedica.setDescrizione(visitaMedicaRequestDTO.getDescrizione());
            visitaMedica.setData(visitaMedicaRequestDTO.getData());
            visitaMedica.setReferto(visitaMedicaRequestDTO.getReferto().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Errore di apertura file");
        }

        VisitaMedica newVisitaMedica = visitaMedicaRepository.save(visitaMedica);

        return new VisitaMedicaResponseDTO(
                newVisitaMedica.getNome(),
                newVisitaMedica.getPet().getId(),
                newVisitaMedica.getVeterinario().getId(),
                newVisitaMedica.getDescrizione(),
                newVisitaMedica.getData(),
                newVisitaMedica.getReferto());
    }

    public VisitaMedicaResponseDTO leggiPFD(Long id) {
        Optional<VisitaMedica> visitaMedica = visitaMedicaRepository.findById(id);
        if (visitaMedica.isEmpty()) {
            throw new RuntimeException("Visita non trovata");
        }

        if (visitaMedica.get().getReferto() == null) {
            throw new RuntimeException("Referto non trovato");
        }

        return new VisitaMedicaResponseDTO(
                visitaMedica.get().getNome(),
                visitaMedica.get().getPet().getId(),
                visitaMedica.get().getVeterinario().getId(),
                visitaMedica.get().getDescrizione(),
                visitaMedica.get().getData(),
                visitaMedica.get().getReferto()
        );
    }
}
