package it.safepet.backend.gestioneUtente.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.gestioneRecensioni.dto.RecensioneResponseDTO;
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
    @Transactional(readOnly = true)
    public VisualizzaDettagliVeterinariResponseDTO visualizzaDettagliVeterinari(Long idVet) {
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();

        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non autenticato.");
        }

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
                        r.getProprietario().getId(),
                        r.getVeterinario().getId()
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
                mediaRecensioni
        );
    }
}
