package it.safepet.backend.gestioneCartellaClinica.service;

import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.*;
import it.safepet.backend.gestioneCartellaClinica.repository.PatologiaRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.RecordMedicoRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.TerapiaRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.VaccinazioneRepository;
import it.safepet.backend.gestioneCartellaClinica.repository.VisitaMedicaRepository;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class GestioneCartellaClinicaServiceImpl implements GestioneCartellaClinicaService {
    @Autowired
    private PetRepository petRepository;

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


//    public CartellaClinicaResponseDTO getCartellaClinica(Long petId) {
//        // Recupera tutti i record medici associati al pet
//        List<RecordMedico> records = recordMedicoRepository.findByPetId(petId);
//
//        // Inizializza il DTO con liste vuote
//        CartellaClinicaResponseDTO dto = new CartellaClinicaResponseDTO(
//                new ArrayList<>(), // visiteMediche
//                new ArrayList<>(), // patologie
//                new ArrayList<>(), // terapie
//                new ArrayList<>()  // vaccinazioni
//        );
//
//        // Itera sui record e mappa in base al tipo
//        for (RecordMedico record : records) {
//            if (record instanceof Vaccinazione) {
//                dto.getVaccinazioni().add(vaccinazioneMapper.toDto((Vaccinazione) record));
//            } else if (record instanceof VisitaMedica) {
//                dto.getVisiteMediche().add(visitaMedicaMapper.toDto((VisitaMedica) record));
//            } else if (record instanceof Patologia) {
//                dto.getPatologie().add(patologiaMapper.toDto((Patologia) record));
//            } else if (record instanceof Terapia) {
//                dto.getTerapie().add(terapiaMapper.toDto((Terapia) record));
//            }
//        }
//
//        return dto;
//    }

}
