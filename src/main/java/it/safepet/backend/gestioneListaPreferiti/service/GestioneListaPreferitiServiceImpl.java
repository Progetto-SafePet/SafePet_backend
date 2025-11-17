package it.safepet.backend.gestioneListaPreferiti.service;

import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class GestioneListaPreferitiServiceImpl implements GestioneListaPreferitiService {
    @Autowired
    ProprietarioRepository proprietarioRepository;

    @Autowired
    VeterinarioRepository veterinarioRepository;
}
