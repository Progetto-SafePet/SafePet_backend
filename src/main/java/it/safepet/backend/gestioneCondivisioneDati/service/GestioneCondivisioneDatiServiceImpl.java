package it.safepet.backend.gestioneCondivisioneDati.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.service.GestioneCartellaClinicaService;
import it.safepet.backend.gestioneCondivisioneDati.dto.CondivisioneDatiPetResponseDTO;
import it.safepet.backend.gestioneCondivisioneDati.html.LibrettoPetHtmlBuilder;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestionePet.service.GestionePetService;
import it.safepet.backend.gestioneUtente.dto.ProprietarioResponseDTO;
import it.safepet.backend.gestioneUtente.service.GestioneUtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.ByteArrayOutputStream;

@Service
@Validated
public class GestioneCondivisioneDatiServiceImpl implements GestioneCondivisioneDatiService {

    @Autowired
    private GestionePetService gestionePetService;

    @Autowired
    private GestioneCartellaClinicaService gestioneCartellaClinicaService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private GestioneUtenteService gestioneUtenteService;

    // builder HTML dedicato
    private final LibrettoPetHtmlBuilder htmlBuilder = new LibrettoPetHtmlBuilder();

    @Override
    @Transactional(readOnly = true)
    public CondivisioneDatiPetResponseDTO getDatiCompletiPet(Long petId) {

        // Recupera utente autenticato
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Accesso non autorizzato: nessun utente autenticato");
        }

        // Solo PROPRIETARIO può accedere
        if (!Role.PROPRIETARIO.equals(currentUser.getRole())) {
            throw new RuntimeException("Accesso negato: solo i proprietari possono accedere ai dati");
        }

        // Recupera pet e controlla che appartenga al proprietario loggato
        Pet petEntity = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet non trovato"));

        if (!petEntity.getProprietario().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Accesso negato: il pet non appartiene all'utente corrente");
        }

        // Recupera DTO già pronti dagli altri service
        PetResponseDTO petDTO = gestionePetService.getAnagraficaPet(petId);
        CartellaClinicaResponseDTO cartellaDTO = gestioneCartellaClinicaService.getCartellaClinica(petId);
        ProprietarioResponseDTO proprietarioDTO = gestioneUtenteService.getProprietario(petEntity.getProprietario().getId());

        // Costruzione DTO aggregato aggiornato
        return new CondivisioneDatiPetResponseDTO(
                proprietarioDTO,
                petDTO,
                cartellaDTO
        );
    }

    //     GENERAZIONE PDF (con HTML)
    @Override
    @Transactional(readOnly = true)
    public byte[] generaPdfPet(Long petId) {

        // 🔐 Anche qui serve il controllo autorizzazione
        CondivisioneDatiPetResponseDTO dto = getDatiCompletiPet(petId);

        String html = htmlBuilder.buildHtml(dto);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Errore generazione PDF: " + e.getMessage());
        }
    }

}
