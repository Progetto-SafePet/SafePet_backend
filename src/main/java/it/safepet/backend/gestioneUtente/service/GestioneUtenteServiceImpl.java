package it.safepet.backend.gestioneUtente.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
import it.safepet.backend.gestioneUtente.dto.VisualizzaDettagliVeterinariResponseDTO;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import org.springframework.web.server.ResponseStatusException;

@Service
@Validated
public class GestioneUtenteServiceImpl implements GestioneUtenteService {
    @Autowired
    private ProprietarioRepository proprietarioRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registraProprietario(@Valid RegistrazioneProprietarioRequestDTO registrazioneDTO) {
        // Verifica che la password e confermaPassword coincidano
        if (!registrazioneDTO.getPassword().equals(registrazioneDTO.getConfermaPassword())) {
            throw new IllegalArgumentException("La password e la conferma password non coincidono");
        }

        // Verifica unicità email
        if (proprietarioRepository.findByEmail(registrazioneDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email già registrata nel sistema");
        }

        // Creazione proprietario
        Proprietario proprietario = new Proprietario();
        proprietario.setNome(registrazioneDTO.getNome().trim());
        proprietario.setCognome(registrazioneDTO.getCognome().trim());
        proprietario.setEmail(registrazioneDTO.getEmail().trim());
        proprietario.setPassword(passwordEncoder.encode(registrazioneDTO.getPassword()));
        proprietario.setNumeroTelefono(registrazioneDTO.getNumeroTelefono());
        proprietario.setDataNascita(registrazioneDTO.getDataNascita());
        proprietario.setIndirizzoDomicilio(registrazioneDTO.getIndirizzoDomicilio().trim());
        proprietario.setGenere(registrazioneDTO.getGenere().toUpperCase());

        // Salvataggio nel database
        proprietarioRepository.save(proprietario);
    }

    @Override
    public List<VisualizzaDettagliVeterinariResponseDTO> visualizzaDettagliVeterinari(Long idVet) {
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();

        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non autenticato.");
        }

        Veterinario veterinario = veterinarioRepository.findById(idVet)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinario non trovato"));

        Double mediaRecensioni = veterinarioRepository.calcolaMediaRecensioniVeterinario(idVet);

        return veterinarioRepository.findById(idVet)
                .stream()
                .map(p -> new VisualizzaDettagliVeterinariResponseDTO(
                        veterinario.getId(),
                        veterinario.getNome(),
                        veterinario.getCognome(),
                        veterinario.getDataNascita(),
                        veterinario.getGenere(),
                        veterinario.getEmail(),
                        veterinario.getNumeroTelefono(),
                        veterinario.getSpecializzazioniAnimali(),
                        veterinario.getClinica().getId(),
                        veterinario.getClinica().getNome(),
                        veterinario.getClinica().getIndirizzo(),
                        mediaRecensioni
                ))
                .toList();
    }
}
