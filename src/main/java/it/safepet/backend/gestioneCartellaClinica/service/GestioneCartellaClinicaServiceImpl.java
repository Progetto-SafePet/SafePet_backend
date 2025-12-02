package it.safepet.backend.gestioneCartellaClinica.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.PatologiaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.TerapiaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VaccinazioneResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.Patologia;
import it.safepet.backend.gestioneCartellaClinica.model.RecordMedico;
import it.safepet.backend.gestioneCartellaClinica.model.Terapia;
import it.safepet.backend.gestioneCartellaClinica.model.Vaccinazione;
import it.safepet.backend.gestioneCartellaClinica.model.VisitaMedica;
import it.safepet.backend.gestioneCartellaClinica.repository.PatologiaRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.RecordMedicoRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.TerapiaRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.VaccinazioneRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.VisitaMedicaRepository;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class GestioneCartellaClinicaServiceImpl implements GestioneCartellaClinicaService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ProprietarioRepository proprietarioRepository;

    @Autowired
    private PatologiaRepository patologiaRepository;

    @Autowired
    private RecordMedicoRepository recordMedicoRepository;

    @Autowired
    private TerapiaRepository terapiaRepository;

    @Autowired
    private VaccinazioneRepository vaccinazioneRepository;

    @Autowired
    private VisitaMedicaRepository visitaMedicaRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Transactional(readOnly = true)
    public CartellaClinicaResponseDTO getCartellaClinica(Long petId) {
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Accesso non autorizzato: utente non autenticato");
        }

        petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet non trovato"));

        if (!isAuthorized(currentUser, petId)) {
            throw new RuntimeException("Accesso negato: utente non autorizzato per questo pet");
        }

        List<RecordMedico> records = recordMedicoRepository.findByPetId(petId);
        return buildCartellaClinicaDTO(records);
    }

    /**
     * Verifica se l'utente autenticato è autorizzato ad accedere alla cartella clinica di un pet.
     *
     * <p><b>Logica:</b></p>
     * <ul>
     *   <li>Se il ruolo è {@code PROPRIETARIO}, controlla che il pet appartenga al proprietario.</li>
     *   <li>Se il ruolo è {@code VETERINARIO}, controlla che il pet sia associato al veterinario.</li>
     *   <li>Per altri ruoli, restituisce {@code false}.</li>
     * </ul>
     *
     * @param user utente autenticato
     * @param petId identificativo del pet
     * @return {@code true} se l'utente è autorizzato, {@code false} altrimenti
     */
    private boolean isAuthorized(AuthenticatedUser user, Long petId) {
        return switch (user.getRole()) {
            case PROPRIETARIO -> proprietarioRepository.findById(user.getId())
                    .map(p -> p.getPets().stream().anyMatch(pet -> pet.getId().equals(petId)))
                    .orElse(false);
            case VETERINARIO -> veterinarioRepository.findById(user.getId())
                    .map(v -> v.getPetsAssociati().stream().anyMatch(pet -> pet.getId().equals(petId)))
                    .orElse(false);
            default -> false;
        };
    }

    /**
     * Costruisce un {@link CartellaClinicaResponseDTO} a partire da una lista di record medici.
     *
     * <p><b>Logica:</b></p>
     * <ul>
     *   <li>Inizializza il DTO con liste vuote.</li>
     *   <li>Itera sui record medici e li classifica in base al tipo:
     *       {@link Vaccinazione}, {@link VisitaMedica}, {@link Patologia}, {@link Terapia}.</li>
     *   <li>Converte ogni record nel rispettivo DTO di risposta.</li>
     * </ul>
     *
     * @param records lista di record medici associati al pet
     * @return {@link CartellaClinicaResponseDTO} popolato con i dati dei record medici
     */
    private CartellaClinicaResponseDTO buildCartellaClinicaDTO(List<RecordMedico> records) {
        CartellaClinicaResponseDTO dto = new CartellaClinicaResponseDTO(
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
        );

        for (RecordMedico record : records) {
            if (record instanceof Vaccinazione) {
                dto.getVaccinazioni().add(VaccinazioneResponseDTO.from((Vaccinazione) record));
            } else if (record instanceof VisitaMedica) {
                dto.getVisiteMediche().add(VisitaMedicaResponseDTO.from((VisitaMedica) record));
            } else if (record instanceof Patologia) {
                dto.getPatologie().add(PatologiaResponseDTO.from((Patologia) record));
            } else if (record instanceof Terapia) {
                dto.getTerapie().add(TerapiaResponseDTO.from((Terapia) record));
            }
        }
        return dto;
    }
}
