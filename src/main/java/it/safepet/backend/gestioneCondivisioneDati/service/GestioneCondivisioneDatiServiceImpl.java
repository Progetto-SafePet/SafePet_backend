package it.safepet.backend.gestioneCondivisioneDati.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.service.GestioneCartellaClinicaService;
import it.safepet.backend.gestioneCondivisioneDati.dto.CondivisioneDatiPetResponseDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestionePet.service.GestionePetService;
import it.safepet.backend.gestioneUtente.model.Proprietario;
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

    // ============================
    //   AUTORIZZAZIONE + DTO
    // ============================
    @Override
    @Transactional(readOnly = true)
    public CondivisioneDatiPetResponseDTO getDatiCompletiPet(Long petId) {

        // 1️⃣ Recupera utente autenticato
        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null)
            throw new RuntimeException("Accesso non autorizzato: nessun utente autenticato");

        // 2️⃣ Solo PROPRIETARIO può accedere
        if (!Role.PROPRIETARIO.equals(currentUser.getRole()))
            throw new RuntimeException("Accesso negato: solo i proprietari possono accedere ai dati");

        // 3️⃣ Recupera pet e controlla che appartenga al proprietario loggato
        Pet petEntity = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet non trovato"));

        if (!petEntity.getProprietario().getId().equals(currentUser.getId()))
            throw new RuntimeException("Accesso negato: il pet non appartiene all'utente corrente");

        // 4️⃣ Recupera DTO già pronti dagli altri service
        PetResponseDTO petDTO = gestionePetService.getAnagraficaPet(petId);
        CartellaClinicaResponseDTO cartellaDTO = gestioneCartellaClinicaService.getCartellaClinica(petId);

        Proprietario proprietario = petEntity.getProprietario();

        // 5️⃣ Costruzione DTO aggregato
        return new CondivisioneDatiPetResponseDTO(
                proprietario.getNome(),
                proprietario.getCognome(),
                proprietario.getEmail(),
                proprietario.getNumeroTelefono(),
                proprietario.getIndirizzoDomicilio(),
                petDTO,
                cartellaDTO
        );
    }

    // ====================================
    //     GENERAZIONE PDF (con HTML)
    // ====================================
    @Override
    @Transactional(readOnly = true)
    public byte[] generaPdfPet(Long petId) {

        // 🔐 Anche qui serve il controllo autorizzazione
        CondivisioneDatiPetResponseDTO dto = getDatiCompletiPet(petId);

        String html = buildHtml(dto);

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

    // ====================================
    //     COSTRUZIONE HTML DEL PDF
    // ====================================
    private String buildHtml(CondivisioneDatiPetResponseDTO dto) {

        String vaccinazioni = dto.getCartellaClinica().getVaccinazioni().stream()
                .map(v -> "<tr><td>" + v.getNomeVaccino() + "</td><td>" + v.getDataDiSomministrazione() + "</td></tr>")
                .reduce("", String::concat);

        String patologie = dto.getCartellaClinica().getPatologie().stream()
                .map(p -> "<tr><td>" + p.getNome() + "</td><td>" + p.getDataDiDiagnosi() + "</td></tr>")
                .reduce("", String::concat);

        String terapie = dto.getCartellaClinica().getTerapie().stream()
                .map(t -> "<tr><td>" + t.getNome() + "</td><td>" + t.getDurata() + "</td></tr>")
                .reduce("", String::concat);

        String visite = dto.getCartellaClinica().getVisiteMediche().stream()
                .map(v -> "<tr><td>" + v.getNome() + "</td><td>" + v.getData() + "</td></tr>")
                .reduce("", String::concat);

        return """
        <html>
        <head>
            <meta charset="UTF-8"/>
            <style>
    
                body {
                    font-family: Arial, sans-serif;
                    padding: 30px;
                    color: #3a2c1a;
                }
    
                h1 {
                    text-align: center;
                    color: #7d4f21;
                    font-size: 34px;
                    margin-bottom: 40px;
                    border-bottom: 3px solid #c5a982;
                    padding-bottom: 10px;
                }
    
                h2 {
                    color: #7d4f21;
                    font-size: 24px;
                    margin-top: 40px;
                    margin-bottom: 15px;
                }
    
                table {
                    width: 100%%;
                    border-collapse: collapse;
                    margin-bottom: 25px;
                }
    
                th {
                    background-color: #f7e7ce;
                    color: #3a2c1a;
                    font-weight: bold;
                    padding: 10px;
                    border: 1px solid #e0d3b8;
                    width: 25%%;
                }
    
                td {
                    background-color: #ffffff;
                    padding: 10px;
                    border: 1px solid #e0d3b8;
                }
    
                /* BOX PROPRIETARIO */
                .box {
                    background-color: #f7e7ce;
                    border: 2px solid #d6b894;
                    padding: 20px;
                    border-radius: 8px;
                    margin-top: 20px;
                    margin-bottom: 30px;
                }
    
                .label {
                    font-weight: bold;
                }
    
                .divider {
                    border-bottom: 2px solid #c5a982;
                    margin: 40px 0;
                }
    
            </style>
        </head>
    
        <body>
    
            <h1>Scheda Anagrafica Pet: %s</h1>
    
            <!-- DATI ANAGRAFICI PET -->
            <h2>Dati Anagrafici</h2>
    
            <table>
                <tr><th>Specie</th><td>%s</td></tr>
                <tr><th>Razza</th><td>%s</td></tr>
                <tr><th>Sesso</th><td>%s</td></tr>
                <tr><th>Data Nascita</th><td>%s</td></tr>
                <tr><th>Peso</th><td>%s kg</td></tr>
                <tr><th>Colore Mantello</th><td>%s</td></tr>
                <tr><th>Microchip</th><td>%s</td></tr>
                <tr><th>Sterilizzato</th><td>%s</td></tr>
            </table>
    
            <!-- DATI PROPRIETARIO -->
            <h2>Contatti Proprietario</h2>
            <div class="box">
                <p><span class="label">Nome Completo:</span> %s %s<br/>
                   <span class="label">Email:</span> %s<br/>
                   <span class="label">Telefono:</span> %s<br/>
                   <span class="label">Indirizzo:</span> %s
                </p>
            </div>
   
    
            <h1>Storico Clinico Completo</h1>
    
            <h2>Vaccinazioni</h2>
            <table>
                <tr><th>Vaccino</th><th>Data</th></tr>
                %s
            </table>
    
            <h2>Patologie</h2>
            <table>
                <tr><th>Patologia</th><th>Data Diagnosi</th></tr>
                %s
            </table>
    
            <h2>Terapie</h2>
            <table>
                <tr><th>Terapia</th><th>Durata</th></tr>
                %s
            </table>
    
            <h2>Visite Mediche</h2>
            <table>
                <tr><th>Visita</th><th>Data</th></tr>
                %s
            </table>
    
        </body>
        </html>
        """.formatted(
                dto.getPet().getNome(),
                dto.getPet().getSpecie(),
                dto.getPet().getRazza(),
                dto.getPet().getSesso(),
                dto.getPet().getDataNascita(),
                dto.getPet().getPeso(),
                dto.getPet().getColoreMantello(),
                dto.getPet().getMicrochip(),
                dto.getPet().getSterilizzato() ? "Sì" : "No",
                dto.getNomeProprietario(),
                dto.getCognomeProprietario(),
                dto.getEmailProprietario(),
                dto.getTelefonoProprietario(),
                dto.getIndirizzoProprietario(),
                vaccinazioni,
                patologie,
                terapie,
                visite
        );
    }

}
