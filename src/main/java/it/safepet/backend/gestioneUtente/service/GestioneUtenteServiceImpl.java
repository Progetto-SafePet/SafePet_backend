package it.safepet.backend.gestioneUtente.service;

import it.safepet.backend.exception.NotFoundException;
import it.safepet.backend.exception.UnauthorizedException;
import it.safepet.backend.gestioneRecensioni.dto.RecensioneResponseDTO;
import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestioneUtente.dto.ProfiloProprietarioResponseDTO;
import it.safepet.backend.gestioneUtente.dto.ProprietarioResponseDTO;
import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
import it.safepet.backend.gestioneUtente.dto.VisualizzaDettagliVeterinariResponseDTO;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import it.safepet.backend.reportCliniche.dto.OrariClinicaResponseDTO;
import it.safepet.backend.reportCliniche.model.Clinica;
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
    public VisualizzaDettagliVeterinariResponseDTO visualizzaDettagliVeterinari(Long idVet) {
       Veterinario veterinario = veterinarioRepository.findById(idVet)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinario non trovato"));

        Double mediaRecensioni = veterinarioRepository.calcolaMediaRecensioniVeterinario(idVet);

        Clinica clinica = veterinario.getClinica();

        List<OrariClinicaResponseDTO> orariDTO = clinica.getOrariApertura().stream()
                .map(o -> new OrariClinicaResponseDTO(
                        o.getGiorno(), //creato un dto per avere solo queste informazioni specifiche
                        o.getOrarioApertura(),
                        o.getOrarioChiusura(),
                        o.getAperto24h()
                ))
                .toList();

        List<RecensioneResponseDTO> recensioniDTO = veterinario.getRecensioni()
                .stream()
                .map(r -> new RecensioneResponseDTO(
                        r.getId(),  //creato un dto per avere solo queste informazioni specifiche
                        r.getPunteggio(),
                        r.getDescrizione(),
                        r.getVeterinario().getId(),
                        r.getProprietario().getId(),
                        r.getProprietario().getNome(),
                        r.getProprietario().getCognome()
                ))
                .toList();

        return new VisualizzaDettagliVeterinariResponseDTO(
                veterinario.getId(),
                veterinario.getNome(),
                veterinario.getCognome(),
                veterinario.getDataNascita(),
                veterinario.getGenere(),
                veterinario.getEmail(),
                veterinario.getNumeroTelefono(),
                veterinario.getSpecializzazioniAnimali(),
                clinica.getId(),
                clinica.getNome(),
                clinica.getIndirizzo(),
                clinica.getNumeroTelefono(),
                clinica.getLatitudine(),
                clinica.getLongitudine(),
                orariDTO,
                recensioniDTO,
                mediaRecensioni);
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

    public ProprietarioResponseDTO getProprietario(Long propId) {
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();

        if (currentUser == null) {
            throw new UnauthorizedException("Utente non autenticato");
        }

        // Verifica che l'utente sia un proprietario
        if (!Role.PROPRIETARIO.equals(currentUser.getRole())) {
            throw new NotFoundException( "Accesso negato: solo i proprietari possono visualizzare il proprio profilo");
        }

        Proprietario p = proprietarioRepository.findById(propId)
                .orElseThrow(() -> new NotFoundException( "Proprietario non trovato"));

        return new ProprietarioResponseDTO(
                p.getNome(),
                p.getCognome(),
                p.getEmail(),
                p.getNumeroTelefono(),
                p.getIndirizzoDomicilio()
        );
    }
}
