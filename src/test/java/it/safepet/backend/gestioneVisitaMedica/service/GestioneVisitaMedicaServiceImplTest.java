package it.safepet.backend.gestioneVisitaMedica.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.VisitaMedica;
import it.safepet.backend.gestioneCartellaClinica.repository.VisitaMedicaRepository;
import it.safepet.backend.gestioneCartellaClinica.service.visitaMedica.GestioneVisitaMedicaServiceImpl;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GestioneVisitaMedicaServiceImplTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private VisitaMedicaRepository visitaMedicaRepository;
    @Mock
    private VeterinarioRepository veterinarioRepository;

    @InjectMocks
    private GestioneVisitaMedicaServiceImpl service;

    private Validator validator;
    private MockedStatic<AuthContext> authContextMock;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        authContextMock = mockStatic(AuthContext.class);
    }

    @AfterEach
    void tearDown() {
        authContextMock.close();
    }

    // ===========================
    // Helper per costruire DTO validi
    private VisitaMedicaRequestDTO buildValidRequest(Long petId, MultipartFile referto) {
        return new VisitaMedicaRequestDTO(
                "Visita di controllo",
                petId,
                "Descrizione valida",
                LocalDate.now(),
                referto
        );
    }

    private Veterinario buildVeterinario(Long id) {
        Veterinario v = new Veterinario();
        v.setId(id);
        v.setNome("Mario");
        v.setCognome("Rossi");
        return v;
    }

    private Pet buildPet(Long id) {
        Pet p = new Pet();
        p.setId(id);
        p.setNome("Fido");
        return p;
    }

    // ===========================
    // TEST CASES
    // ===========================

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_1
     * Test Frame: TF1
     * Obiettivo:
     * Verificare che un utente non autenticato non possa creare una visita medica.
     * <p>
     * Parametri di input:
     * - petId = 1
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_1() throws IOException {
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(null);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenReturn("contenuto".getBytes());

        VisitaMedicaRequestDTO request = buildValidRequest(1L, file);

        assertThatThrownBy(() -> service.creaVisitaMedica(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Accesso non autorizzato");
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_2
     * Test Frame: TF2
     * Obiettivo:
     * Verificare che un utente autenticato ma con ruolo diverso da Veterinario non possa creare una visita.
     * <p>
     * Parametri di input:
     * - ruolo = Proprietario
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_2() throws IOException {
        AuthenticatedUser user = new AuthenticatedUser(1L, "mail@example.com", Role.PROPRIETARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenReturn("contenuto".getBytes());

        VisitaMedicaRequestDTO request = buildValidRequest(1L, file);

        assertThatThrownBy(() -> service.creaVisitaMedica(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Accesso non autorizzato");
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_3
     * Test Frame: TF3
     * Obiettivo:
     * Verificare che venga sollevata eccezione se il pet non esiste nel database.
     * <p>
     * Parametri di input:
     * - petId = 99
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_3() throws IOException {
        AuthenticatedUser user = new AuthenticatedUser(1L, "mail@example.com", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);
        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(buildVeterinario(1L)));
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenReturn("contenuto".getBytes());

        VisitaMedicaRequestDTO request = buildValidRequest(99L, file);

        assertThatThrownBy(() -> service.creaVisitaMedica(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Pet non trovato");
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_4
     * Test Frame: TF4
     * Obiettivo:
     *   Verificare che venga sollevata eccezione se il pet non è paziente del veterinario.
     * Parametri di input:
     *   - petId = 2
     *   - veterinario associato = NO
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_4() throws IOException {
        AuthenticatedUser user = new AuthenticatedUser(1L, "mail@example.com", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        Veterinario vet = buildVeterinario(1L);
        Pet pet = buildPet(2L);

        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(vet));
        when(petRepository.findById(2L)).thenReturn(Optional.of(pet));
        when(petRepository.verificaAssociazionePetVeterinario(2L, 1L)).thenReturn(false);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenReturn("contenuto".getBytes());

        VisitaMedicaRequestDTO request = buildValidRequest(2L, file);

        assertThatThrownBy(() -> service.creaVisitaMedica(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Il pet non è un paziente del veterinario");
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_5
     * Test Frame: TF5
     * Obiettivo:
     *   Verificare che venga sollevata violazione Bean Validation se il nome è vuoto.
     * Parametri di input:
     *   - nome = ""
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_5() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenReturn("contenuto".getBytes());

        VisitaMedicaRequestDTO request = new VisitaMedicaRequestDTO(
                "", 1L, "Descrizione valida", LocalDate.now(), file);

        assertThat(validator.validate(request)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_6
     * Test Frame: TF6
     * Obiettivo:
     *   Verificare che venga sollevata violazione Bean Validation se il nome è troppo corto (<3).
     * Parametri di input:
     *   - nome = "AB"
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_6() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenReturn("contenuto".getBytes());

        VisitaMedicaRequestDTO request = new VisitaMedicaRequestDTO(
                "AB", 1L, "Descrizione valida", LocalDate.now(), file);

        assertThat(validator.validate(request)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_7
     * Test Frame: TF7
     * Obiettivo:
     *   Verificare che venga sollevata violazione Bean Validation se il nome è troppo lungo (>20).
     * Parametri di input:
     *   - nome = "NomeMoltoMoltoMoltoLungo"
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_7() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenReturn("contenuto".getBytes());

        VisitaMedicaRequestDTO request = new VisitaMedicaRequestDTO(
                "NomeMoltoMoltoMoltoLungo", 1L, "Descrizione valida", LocalDate.now(), file);

        assertThat(validator.validate(request)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_8
     * Test Frame: TF8
     * Obiettivo:
     *   Verificare che venga sollevata violazione Bean Validation se la descrizione supera i 300 caratteri.
     * Parametri di input:
     *   - descrizione = stringa di 301 caratteri
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_8() throws IOException {
        String longDesc = "a".repeat(301);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenReturn("contenuto".getBytes());

        VisitaMedicaRequestDTO request = new VisitaMedicaRequestDTO(
                "Visita di controllo", 1L, longDesc, LocalDate.now(), file);

        assertThat(validator.validate(request)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_9
     * Test Frame: TF9
     * Obiettivo:
     *   Verificare che venga sollevata violazione Bean Validation se la data non rispetta il formato (null).
     * Parametri di input:
     *   - data = null
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_9() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenReturn("contenuto".getBytes());

        VisitaMedicaRequestDTO request = new VisitaMedicaRequestDTO(
                "Visita di controllo", 1L, "Descrizione valida", null, file);

        assertThat(validator.validate(request)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_10
     * Test Frame: TF10
     * Obiettivo:
     *   Verificare che venga sollevata eccezione se la data è futura (non ammissibile).
     * Parametri di input:
     *   - data = LocalDate.now().plusDays(1)
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_10() throws IOException {
        AuthenticatedUser user = new AuthenticatedUser(1L, "mail@example.com", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        Veterinario vet = buildVeterinario(1L);
        Pet pet = buildPet(1L);

        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(vet));
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.verificaAssociazionePetVeterinario(1L, 1L)).thenReturn(true);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenReturn("contenuto".getBytes());

        VisitaMedicaRequestDTO request = new VisitaMedicaRequestDTO(
                "Visita di controllo", 1L, "Descrizione valida", LocalDate.now().plusDays(1), file);

        assertThatThrownBy(() -> service.creaVisitaMedica(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La data della visita non può essere futura.");
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_11
     * Test Frame: TF11
     * Obiettivo:
     *   Verificare che venga sollevata eccezione se il referto non è in formato PDF.
     * Parametri di input:
     *   - referto = file con contentType != application/pdf
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_11() {
        AuthenticatedUser user = new AuthenticatedUser(1L, "mail@example.com" ,Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(buildVeterinario(1L)));
        when(petRepository.findById(1L)).thenReturn(Optional.of(buildPet(1L)));
        when(petRepository.verificaAssociazionePetVeterinario(1L, 1L)).thenReturn(true);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("text/plain");
        when(file.getOriginalFilename()).thenReturn("referto.txt");
        when(file.getSize()).thenReturn(1024L);

        VisitaMedicaRequestDTO request = buildValidRequest(1L, file);

        assertThatThrownBy(() -> service.creaVisitaMedica(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Il file in input deve essere un PDF");
    }


    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_12
     * Test Frame: TF12
     * Obiettivo:
     *   Verificare che venga sollevata eccezione se la dimensione del referto PDF supera i 5 MB.
     * Parametri di input:
     *   - referto = file PDF con size > 5MB
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_12() {
        // Simula utente veterinario autenticato
        AuthenticatedUser user = new AuthenticatedUser(1L, "mail@example.com", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        Veterinario vet = buildVeterinario(1L);
        Pet pet = buildPet(1L);

        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(vet));
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.verificaAssociazionePetVeterinario(1L, 1L)).thenReturn(true);

        // Referto PDF troppo grande
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(6 * 1024 * 1024L); // 6 MB

        VisitaMedicaRequestDTO request = buildValidRequest(1L, file);

        assertThatThrownBy(() -> service.creaVisitaMedica(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("PDF oltre i 5MB");
    }


    /**
     * ===========================
     * Test Case ID: TC_aggiuntaVisitaMedica_13
     * Test Frame: TF13
     * Obiettivo:
     *   Verificare il caso valido: creazione visita medica corretta con salvataggio e ritorno DTO.
     * Parametri di input:
     *   - petId = valido
     *   - nome = "Visita di controllo"
     *   - descrizione = "Descrizione valida"
     *   - data = oggi
     *   - referto = file PDF valido <= 5MB
     * ===========================
     */
    @Test
    void TC_aggiuntaVisitaMedica_13() throws Exception {
        AuthenticatedUser user = new AuthenticatedUser(1L, "mail", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        Veterinario vet = buildVeterinario(1L);
        Pet pet = buildPet(1L);

        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(vet));
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.verificaAssociazionePetVeterinario(1L, 1L)).thenReturn(true);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("referto.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getBytes()).thenReturn("contenuto".getBytes());

        VisitaMedicaRequestDTO request = buildValidRequest(1L, file);

        VisitaMedica visitaSalvata = new VisitaMedica();
        visitaSalvata.setId(10L);
        visitaSalvata.setNome(request.getNome());
        visitaSalvata.setDescrizione(request.getDescrizione());
        visitaSalvata.setData(request.getData());
        visitaSalvata.setVeterinario(vet);
        visitaSalvata.setPet(pet);
        visitaSalvata.setReferto(file.getBytes());

        when(visitaMedicaRepository.save(any(VisitaMedica.class))).thenReturn(visitaSalvata);

        VisitaMedicaResponseDTO response = service.creaVisitaMedica(request);

        assertThat(response).isNotNull();
        assertThat(response.getVisitaMedicaId()).isEqualTo(10L);
        assertThat(response.getNome()).isEqualTo("Visita di controllo");
        assertThat(response.getPetId()).isEqualTo(1L);
        assertThat(response.getVeterinarioId()).isEqualTo(1L);
        assertThat(response.getDescrizione()).isEqualTo("Descrizione valida");
        assertThat(response.getNomeCompletoVeterinario()).contains("Mario Rossi");
        assertThat(response.getNomePet()).isEqualTo("Fido");
        assertThat(response.isPresentReferto()).isTrue();

        verify(visitaMedicaRepository, times(1)).save(any(VisitaMedica.class));
    }

}
