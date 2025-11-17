package it.safepet.backend.gestioneUtente.service;

import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class GestioneUtenteServiceImpl implements GestioneUtenteService {
    @Autowired
    private ProprietarioRepository proprietarioRepository;
}

