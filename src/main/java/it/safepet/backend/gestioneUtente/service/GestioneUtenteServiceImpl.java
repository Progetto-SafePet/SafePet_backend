package it.safepet.backend.gestioneUtente.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestioneUtente.dto.ProfiloProprietarioResponseDTO;
import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public ProfiloProprietarioResponseDTO visualizzaProfiloProprietario() {
        // Recupera l'utente autenticato
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non autenticato");
        }

        // Verifica che l'utente sia un proprietario
        if (!Role.PROPRIETARIO.equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato: solo i proprietari possono visualizzare il proprio profilo");
        }

        // Recupera il proprietario dal database
        Proprietario proprietario = proprietarioRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proprietario non trovato"));

        // Mappa i dati dei pet usando PetResponseDTO
        List<PetResponseDTO> petsInfo = proprietario.getPets().stream()
                .map(pet -> new PetResponseDTO(
                        pet.getId(),
                        pet.getNome(),
                        pet.getSpecie(),
                        pet.getDataNascita(),
                        pet.getPeso(),
                        pet.getColoreMantello(),
                        pet.getSterilizzato(),
                        pet.getRazza(),
                        pet.getMicrochip(),
                        pet.getSesso(),
                        pet.getFoto()
                ))
                .collect(Collectors.toList());

        // Crea e ritorna il DTO della risposta
        return new ProfiloProprietarioResponseDTO(
                proprietario.getId(),
                proprietario.getNome(),
                proprietario.getCognome(),
                proprietario.getEmail(),
                proprietario.getNumeroTelefono(),
                proprietario.getDataNascita(),
                proprietario.getGenere(),
                proprietario.getIndirizzoDomicilio(),
                petsInfo
        );
    }
}
