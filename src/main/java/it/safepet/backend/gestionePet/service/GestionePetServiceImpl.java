package it.safepet.backend.gestionePet.service;

import it.safepet.backend.gestionePet.repository.NoteProprietarioRepository;
import it.safepet.backend.gestionePet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class GestionePetServiceImpl implements GestionePetService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private NoteProprietarioRepository noteProprietarioRepository;
}

