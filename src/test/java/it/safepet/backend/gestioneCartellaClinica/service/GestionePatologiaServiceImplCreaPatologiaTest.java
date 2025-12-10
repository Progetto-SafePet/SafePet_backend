package it.safepet.backend.gestioneCartellaClinica.service;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.PatologiaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.PatologiaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.Patologia;
import it.safepet.backend.gestioneCartellaClinica.repository.PatologiaRepository;
import it.safepet.backend.gestioneCartellaClinica.service.patologia.GestionePatologiaServiceImpl;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class GestionePatologiaServiceImplCreaPatologiaTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private PatologiaRepository patologiaRepository;

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @InjectMocks
    private GestionePatologiaServiceImpl service;

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

    private PatologiaRequestDTO buildValidDTO() {
        PatologiaRequestDTO dto = new PatologiaRequestDTO();
        dto.setPetId(1L);
        dto.setNome("Iperplasia Gengivale");
        dto.setDataDiDiagnosi(LocalDate.now().minusDays(1));
        dto.setSintomiOsservati("Dolore alla masticazione, gengive rosse.");
        dto.setDiagnosi("Si conferma Iperplasia Gengivale.");
        dto.setTerapiaAssociata("Riduzione farmaco X.");
        return dto;
    }

    // --- TEST DI SICUREZZA E ASSOCIAZIONE ---

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_1
     * Test Frame: TF1 (AU2)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia() restituisca Errore: Utente non autenticato quando l'Utente non è autenticato .
     * <p>
     * Parametri di input:
     * - petId = 1L
     * - nome = "Iperplasia Gengivale"
     * - dataDiDiagnosi = ieri
     * - sintomiOsservati = "Dolore alla masticazione, gengive rosse."
     * - diagnosi = "Si conferma Iperplasia Gengivale."
     * - terapiaAssociata = "Riduzione farmaco X."
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_1() {
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.creaPatologia(buildValidDTO()));
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_2
     * Test Frame: TF2 (RU2)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia() restituisca Errore: Accesso non autorizzato quando l'utente è autenticato ma non ha il ruolo Veterinario.
     * <p>
     * Parametri di input:
     * - petId = 1L
     * - nome = "Iperplasia Gengivale"
     * - dataDiDiagnosi = ieri
     * - sintomiOsservati = "Dolore alla masticazione, gengive rosse."
     * - diagnosi = "Si conferma Iperplasia Gengivale."
     * - terapiaAssociata = "Riduzione farmaco X."
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_2() {
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(new AuthenticatedUser(1L, "p@t.it", Role.PROPRIETARIO));
        assertThrows(RuntimeException.class, () -> service.creaPatologia(buildValidDTO()));
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_3
     * Test Frame: TF3 (PE2)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia(...) restituisca Errore: Pet non trovato quando il petId non esiste nel DB.
     * <p>
     * Parametri di input:
     * - petId = 999L (override su buildDTO)
     * - nome = "Iperplasia Gengivale"
     * - dataDiDiagnosi = ieri
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_3() {
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(new AuthenticatedUser(5L, "v@t.it", Role.VETERINARIO));
        when(veterinarioRepository.findById(5L)).thenReturn(Optional.of(new Veterinario()));
        when(petRepository.findById(999L)).thenReturn(Optional.empty());

        PatologiaRequestDTO dto = buildValidDTO();
        dto.setPetId(999L);
        assertThrows(RuntimeException.class, () -> service.creaPatologia(dto));
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_4
     * Test Frame: TF4 (PP2)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia(...) restituisca Errore: paziente non appartenente al veterinario quando il paziente non è di proprietà del veterinario
     * <p>
     * Parametri di input:
     * - petId = 1L
     * - vetId = 5L
     * - nome = "Iperplasia Gengivale"
     * - sintomiOsservati = "Dolore alla masticazione, gengive rosse."
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_4() {
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(new AuthenticatedUser(5L, "v@t.it", Role.VETERINARIO));
        when(veterinarioRepository.findById(5L)).thenReturn(Optional.of(new Veterinario()));
        when(petRepository.findById(1L)).thenReturn(Optional.of(new Pet()));
        when(petRepository.verificaAssociazionePetVeterinario(1L, 5L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.creaPatologia(buildValidDTO()));
    }

    // --- TEST DI VALIDAZIONE SINTATTICA ---

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_5
     * Test Frame: TF5 (LN1)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia(...) restituisca Errore: Lunghezza nome < 3 quando la lunghezza del campo nome è 1.
     * <p>
     * Parametri di input:
     * - nome = "Ab" (override su buildDTO)
     * - petId = 1L
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_5() {
        PatologiaRequestDTO dto = buildValidDTO();
        dto.setNome("Ab");
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_6
     * Test Frame: TF6 (LN2)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia(...) restituisca Errore: Lunghezza nome > 25 quando la lunghezza del campo nome supera 25 caratteri
     * <p>
     * Parametri di input:
     * - nome = "A" ripetuto 26 volte (override su buildDTO)
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_6() {
        PatologiaRequestDTO dto = buildValidDTO();
        dto.setNome("A".repeat(26));
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_7
     * Test Frame: TF7 (LSO1)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia(...) restituisca Errore: Lunghezza sintomiOsservati = 0 quando il campo sintomiOsservati è vuoto
     * <p>
     * Parametri di input:
     * - sintomiOsservati = "" (override su buildDTO)
     * - nome = "Iperplasia Gengivale"
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_7() {
        PatologiaRequestDTO dto = buildValidDTO();
        dto.setSintomiOsservati("");
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_8
     * Test Frame: TF8 (LSO2)
     * <p>
     * Obiettivo: 'obiettivo è testare che la funzione creaPatologia(...) restituisca Errore: Lunghezza sintomiOsservati > 200 quando la lunghezza del campo sintomiOsservati supera 200 caratteri
     * <p>
     * Parametri di input:
     * - sintomiOsservati = "A" ripetuto 201 volte (override su buildDTO)
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_8() {
        PatologiaRequestDTO dto = buildValidDTO();
        dto.setSintomiOsservati("A".repeat(201));
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_9
     * Test Frame: TF9 (LD1)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia(...) restituisca Errore: Lunghezza diagnosi = 0 quando il campo diagnosi è vuoto
     * <p>
     * Parametri di input:
     * - diagnosi = "" (override su buildDTO)
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_9() {
        PatologiaRequestDTO dto = buildValidDTO();
        dto.setDiagnosi("");
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_10
     * Test Frame: TF10 (LD2)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia(...) restituisca Errore: Lunghezza diagnosi > 200 quando la lunghezza del campo diagnosi supera 200 caratteri
     * <p>
     * Parametri di input:
     * - diagnosi = "A" ripetuto 201 volte (override su buildDTO)
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_10() {
        PatologiaRequestDTO dto = buildValidDTO();
        dto.setDiagnosi("A".repeat(201));
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_11
     * Test Frame: TF11 (LT1)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia(...) restituisca Errore: Lunghezza terapiaAssociata = 0 quando il campo terapiaAssociata è vuoto .
     * <p>
     * Parametri di input:
     * - terapiaAssociata = "" (override su buildDTO)
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_11() {
        PatologiaRequestDTO dto = buildValidDTO();
        dto.setTerapiaAssociata("");
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_12
     * Test Frame: TF12 (LT2)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia(...) restituisca Errore: Lunghezza trattamento > 200 quando la lunghezza del campo trattamento supera 200 caratteri.
     * <p>
     * Parametri di input:
     * - terapiaAssociata = "A" ripetuto 201 volte (override su buildDTO)
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_12() {
        PatologiaRequestDTO dto = buildValidDTO();
        dto.setTerapiaAssociata("A".repeat(201));
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    // --- TEST DATA E HAPPY PATH ---

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_13
     * Test Frame: TF13 (VDD2)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia(...) restituisca Errore: Data successiva a quella di oggi quando la dataDiDiagnosi è successiva alla data odierna.
     * <p>
     * Precondizioni: Data impostata a domani.
     * <p>
     * Parametri di input:
     * - dataDiDiagnosi = domani (override su buildDTO)
     * - petId = 1L
     * - nome = "Iperplasia Gengivale"
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_13() {
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(new AuthenticatedUser(5L, "v@t.it", Role.VETERINARIO));
        PatologiaRequestDTO dto = buildValidDTO();
        dto.setDataDiDiagnosi(LocalDate.now().plusDays(1));
        when(veterinarioRepository.findById(5L)).thenReturn(Optional.of(new Veterinario()));
        when(petRepository.findById(1L)).thenReturn(Optional.of(new Pet()));
        when(petRepository.verificaAssociazionePetVeterinario(1L, 5L)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.creaPatologia(dto));
    }

    /**
     * ===========================
     * Test Case ID: TC_aggiuntaPatologia_14
     * Test Frame: TF14 (Happy Path)
     * <p>
     * Obiettivo: L'obiettivo è testare che la funzione creaPatologia(...) restituisca Corretto (Successo) quando tutte le scelte sono valide.
     * <p>
     * Precondizioni: Utente Veterinario loggato, Pet associato, DTO valido.
     * <p>
     * Parametri di input:
     * - petId = 1L
     * - nome = "Iperplasia Gengivale"
     * - dataDiDiagnosi = ieri
     * - sintomiOsservati = "Dolore alla masticazione, gengive rosse."
     * - diagnosi = "Si conferma Iperplasia Gengivale."
     * - terapiaAssociata = "Riduzione farmaco X."
     * ===========================
     */
    @Test
    void TC_aggiuntaPatologia_14() {
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(new AuthenticatedUser(5L, "v@t.it", Role.VETERINARIO));
        PatologiaRequestDTO dto = buildValidDTO();
        Veterinario vet = new Veterinario(); vet.setId(5L);
        Pet pet = new Pet(); pet.setId(1L);
        when(veterinarioRepository.findById(5L)).thenReturn(Optional.of(vet));
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.verificaAssociazionePetVeterinario(1L, 5L)).thenReturn(true);
        when(patologiaRepository.save(any(Patologia.class))).thenAnswer(i -> {
            Patologia p = i.getArgument(0);
            p.setId(100L);
            return p;
        });

        PatologiaResponseDTO res = service.creaPatologia(dto);
        assertThat(res.getPatologiaId()).isEqualTo(100L);
        verify(patologiaRepository, times(1)).save(any(Patologia.class));
    }
}