package it.safepet.backend.gestioneCondivisioneDati.service;

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
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class GestioneCondivisioneDatiServiceImpl implements GestioneCondivisioneDatiService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    ProprietarioRepository proprietarioRepository;

    @Autowired
    VeterinarioRepository veterinarioRepository;

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
}
