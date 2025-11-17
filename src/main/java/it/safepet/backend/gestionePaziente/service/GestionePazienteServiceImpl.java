package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.gestionePaziente.repository.LinkingCodeRepository;
import it.safepet.backend.gestionePet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class GestionePazienteServiceImpl implements GestionePazienteService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private LinkingCodeRepository linkingCodeRepository;
}
