package it.safepet.backend.gestioneUtente.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.gestioneUtente.dto.RegistrazioneProprietarioRequestDTO;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class GestioneUtenteServiceImplRegistraProprietarioTest {

    @Mock
    private ProprietarioRepository proprietarioRepository;

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private GestioneUtenteServiceImpl service;

    private Validator validator;
    private MockedStatic<AuthContext> authContextMock;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        authContextMock = Mockito.mockStatic(AuthContext.class);
    }

    @AfterEach
    void tearDown() {
        authContextMock.close();
    }

    // ============================================================
    // Helper
    // ============================================================

    private RegistrazioneProprietarioRequestDTO buildRequest(
            String nome, String cognome, String email, String password,
            String confermaPassword, String telefono, LocalDate dataNascita,
            String indirizzo, String genere) {
        return new RegistrazioneProprietarioRequestDTO(
                nome, cognome, email, password, confermaPassword,
                telefono, dataNascita, indirizzo, genere
        );
    }

    private RegistrazioneProprietarioRequestDTO buildValidRequest() {
        return buildRequest(
                "Mario", "Rossi", "mario.rossi@test.com", "Password123",
                "Password123", "1234567890", LocalDate.now().minusYears(25),
                "Via Roma 10", "M"
        );
    }

    // ============================================================
    // TEST CASES
    // ============================================================

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_1
     * Test Frame: TF1
     * Obiettivo:
     * Verificare che la validazione fallisca se la lunghezza del nome è < 2 (LN1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - nome = "A"
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_1() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setNome("A");

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Il nome deve contenere tra 2 e 50 caratteri"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_2
     * Test Frame: TF2
     * Obiettivo:
     * Verificare che la validazione fallisca se la lunghezza del nome è > 50 (LN2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - nome = "A".repeat(51)
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_2() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setNome("A".repeat(51));

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Il nome deve contenere tra 2 e 50 caratteri"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_3
     * Test Frame: TF3
     * Obiettivo:
     * Verificare che la validazione fallisca se la lunghezza del cognome è < 2 (LC1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - cognome = "B"
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_3() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setCognome("B");

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Il cognome deve contenere tra 2 e 50 caratteri"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_4
     * Test Frame: TF4
     * Obiettivo:
     * Verificare che la validazione fallisca se la lunghezza del cognome è > 50 (LC2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - cognome = "B".repeat(51)
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_4() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setCognome("B".repeat(51));

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Il cognome deve contenere tra 2 e 50 caratteri"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_5
     * Test Frame: TF5
     * Obiettivo:
     * Verificare che la validazione fallisca se il formato email non è valido (FE1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - email = "email-non-valida"
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_5() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setEmail("email-non-valida");

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Formato email non valido"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_6
     * Test Frame: TF6
     * Obiettivo:
     * Verificare che il servizio lanci un'eccezione se l'email è già presente nel DB (UE1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - email = "mario.rossi@test.com"
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_6() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();

        // UE1: Email già presente
        when(proprietarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new Proprietario()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.registraProprietario(dto));
        assertThat(ex.getMessage()).isEqualTo("Email già registrata nel sistema");

        verify(proprietarioRepository, never()).save(any());
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_7
     * Test Frame: TF7
     * Obiettivo:
     * Verificare che la validazione fallisca se la password è troppo corta (VP1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - password = "Short1!"
     * - confermaPassword = "Short1!"
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_7() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setPassword("Short1!");
        dto.setConfermaPassword("Short1!");

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("La password deve contenere almeno 8 caratteri"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_8
     * Test Frame: TF8
     * Obiettivo:
     * Verificare che la validazione fallisca se la password non rispetta il pattern richiesto (VP2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - password = "passwordtuttaminuscola"
     * - confermaPassword = "passwordtuttaminuscola"
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_8() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setPassword("passwordtuttaminuscola");
        dto.setConfermaPassword("passwordtuttaminuscola");

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("La password deve contenere almeno una lettera minuscola, una maiuscola e un numero"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_9
     * Test Frame: TF9
     * Obiettivo:
     * Verificare che il servizio lanci un'eccezione se la password e la conferma non coincidono (CP1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - password = "Password123"
     * - confermaPassword = "Password999"
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_9() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setPassword("Password123");
        dto.setConfermaPassword("Password999");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.registraProprietario(dto));
        assertThat(ex.getMessage()).isEqualTo("La password e la conferma password non coincidono");

        verify(proprietarioRepository, never()).save(any());
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_10
     * Test Frame: TF10
     * Obiettivo:
     * Verificare che la validazione fallisca se il numero di telefono non rispetta il pattern (FT1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - numeroTelefono = "123" (non 10 cifre)
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_10() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setNumeroTelefono("123");

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Il numero di telefono deve contenere esattamente 10 cifre numeriche"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_11
     * Test Frame: TF11
     * Obiettivo:
     * Verificare che la validazione fallisca se la data di nascita è futura (DN1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - dataNascita = LocalDate.now().plusDays(1)
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_11() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setDataNascita(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("La data di nascita deve essere una data passata"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_12
     * Test Frame: TF12
     * Obiettivo:
     * Verificare che la validazione fallisca se l'indirizzo è troppo corto (LI1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - indirizzoDomicilio = "Via" (3 caratteri)
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_12() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setIndirizzoDomicilio("Via");

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("L'indirizzo deve contenere tra 5 e 100 caratteri"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_13
     * Test Frame: TF13
     * Obiettivo:
     * Verificare che la validazione fallisca se l'indirizzo è troppo lungo (LI2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - indirizzoDomicilio = "Via".repeat(34) (> 100 caratteri)
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_13() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setIndirizzoDomicilio("Via".repeat(34));

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("L'indirizzo deve contenere tra 5 e 100 caratteri"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_14
     * Test Frame: TF14
     * Obiettivo:
     * Verificare che la validazione fallisca se il genere non è valido (FG1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - genere = "X"
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_14() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();
        dto.setGenere("X");

        Set<ConstraintViolation<RegistrazioneProprietarioRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Il genere deve essere: M (maschio), F (femmina) o A (altro)"));
    }

    /**
     * ===========================
     * Test Case ID: TC_RegistraProprietario_15
     * Test Frame: TF15
     * Obiettivo:
     * Verificare che la registrazione avvenga con successo quando tutti i dati sono validi (Happy Path).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - nome = "Mario"
     * - cognome = "Rossi"
     * - email = "mario.rossi@test.com"
     * - password = "Password123"
     * - confermaPassword = "Password123"
     * - numeroTelefono = "1234567890"
     * - dataNascita = oggi - 25 anni
     * - indirizzoDomicilio = "Via Roma 10"
     * - genere = "M"
     * ===========================
     */
    @Test
    void TC_RegistraProprietario_15() {
        RegistrazioneProprietarioRequestDTO dto = buildValidRequest();

        // Mock comportamenti validi
        when(proprietarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        service.registraProprietario(dto);

        verify(proprietarioRepository).save(argThat(p ->
                p.getNome().equals(dto.getNome()) &&
                p.getCognome().equals(dto.getCognome()) &&
                p.getEmail().equals(dto.getEmail()) &&
                p.getPassword().equals("encodedPassword") &&
                p.getNumeroTelefono().equals(dto.getNumeroTelefono()) &&
                p.getGenere().equals(dto.getGenere())
        ));
    }
}