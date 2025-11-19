package it.safepet.backend.gestioneAmministrativa.service;

import it.safepet.backend.gestioneAmministrativa.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class GestioneAmministrativaServiceImpl implements GestioneAmministrativaService {
    @Autowired
    private AdminRepository adminRepository;
}
