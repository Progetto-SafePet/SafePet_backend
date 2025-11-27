package it.safepet.backend.gestioneRecensioni.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.repository.VisitaMedicaRepository;
import it.safepet.backend.gestioneRecensioni.dto.NewRecensioneDTO;
import it.safepet.backend.gestioneRecensioni.dto.RecensioneResponseDTO;
import it.safepet.backend.gestioneRecensioni.model.Recensione;
import it.safepet.backend.gestioneRecensioni.repository.RecensioneRepository;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public RecensioneResponseDTO creaRecensione(Long idVeterinario, @Valid NewRecensioneDTO newRecensioneDTO) throws Exception {

        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null || !Role.PROPRIETARIO.equals(currentUser.getRole())) {
            throw new RuntimeException("Accesso non autorizzato");
        }

        Proprietario proprietario = proprietarioRepository
                .findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Proprietario non trovato"));


        Veterinario veterinario = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new RuntimeException("Veterinario non trovato"));

        // Controlla che non esista già una recensione per questo veterinario da questo proprietario
        boolean esisteGia = recensioneRepository.existsByVeterinarioAndProprietario(veterinario, proprietario);
        if (esisteGia) {
            throw new IllegalStateException("Hai già lasciato una recensione per questo veterinario");
        }

        Recensione recensione = new Recensione();
        recensione.setPunteggio(newRecensioneDTO.getPunteggio());
        recensione.setDescrizione(newRecensioneDTO.getDescrizione());
        recensione.setVeterinario(veterinario);
        recensione.setProprietario(proprietario);

        Recensione saved = recensioneRepository.save(recensione);

        return new RecensioneResponseDTO(
                saved.getId(),
                saved.getPunteggio(),
                saved.getDescrizione(),
                veterinario.getId(),
                proprietario.getId()
        );
    }
}
