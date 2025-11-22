package it.safepet.backend.gestioneCartellaClinica.service;

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
}
