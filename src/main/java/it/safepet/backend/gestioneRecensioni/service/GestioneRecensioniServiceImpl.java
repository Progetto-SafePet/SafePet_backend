package it.safepet.backend.gestioneRecensioni.service;

import it.safepet.backend.gestioneCartellaClinica.repository.VisitaMedicaRepository;
import it.safepet.backend.gestioneRecensioni.repository.RecensioneRepository;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class GestioneRecensioniServiceImpl implements GestioneRecensioniService {
    @Autowired
    RecensioneRepository recensioneRepository;

    @Autowired
    ProprietarioRepository proprietarioRepository;

    @Autowired
    VeterinarioRepository veterinarioRepository;

    @Autowired
    VisitaMedicaRepository visitaMedicaRepository;
}
