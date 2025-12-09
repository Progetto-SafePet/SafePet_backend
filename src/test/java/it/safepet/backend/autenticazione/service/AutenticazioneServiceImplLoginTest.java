package it.safepet.backend.autenticazione.service;

import it.safepet.backend.autenticazione.dto.AuthResponseDTO;
import it.safepet.backend.autenticazione.dto.LoginRequestDTO;
import it.safepet.backend.autenticazione.jwt.JwtUtil;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.exception.UnauthorizedException;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticazioneServiceImplLoginTest {

    @Mock
    private ProprietarioRepository proprietarioRepository;

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AutenticazioneServiceImpl service;

    private static final String VALID_EMAIL = "utente@test.com";
    private static final String RAW_PASSWORD = "Password123";
    private static final String ENCODED_PASSWORD = "hashedPassword";
    private static final String GENERATED_TOKEN = "jwt.token.generated";
    private Proprietario proprietarioMock;
    private Veterinario veterinarioMock;


    @BeforeEach
    void setUp() {

        proprietarioMock = new Proprietario("Mario", "Rossi", LocalDate.of(1990, 1, 1), "M",
                VALID_EMAIL, ENCODED_PASSWORD, "1234567890", "Via Roma 1");
        proprietarioMock.setId(1L);

        veterinarioMock = new Veterinario("Laura", "Bianchi", LocalDate.of(1985, 5, 5), "F",
                VALID_EMAIL, ENCODED_PASSWORD, "0987654321", "Cani");
        veterinarioMock.setId(2L);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * ===========================
     * Test Case ID: TC_Login_1
     * Test Frame: TF1 (PE1 [ERROR])
     * Obiettivo:
     * Verificare il fallimento del login (errore di business) quando l'email non è presente né come proprietario né come veterinario (PE1 [ERROR]).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - email = utente@test.com
     * - password = Password123
     * ===========================
     */
    @Test
    @DisplayName("TC1: Email non trovata nel DB")
    void TC_Login_1() {

        LoginRequestDTO request = new LoginRequestDTO(VALID_EMAIL, RAW_PASSWORD);
        when(proprietarioRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(veterinarioRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());

        UnauthorizedException thrown = assertThrows(UnauthorizedException.class, () -> {
            service.login(request);
        });

        Assertions.assertThat(thrown.getMessage()).isEqualTo("Credenziali non valide.");

        verify(proprietarioRepository, times(1)).findByEmail(VALID_EMAIL);
        verify(veterinarioRepository, times(1)).findByEmail(VALID_EMAIL);
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtUtil, never()).generateToken(any(), any(), any());
    }

    /**
     * ===========================
     * Test Case ID: TC_Login_2
     * Test Frame: TF2 (PE2 + CP1 [ERROR])
     * Obiettivo:
     * Verificare il fallimento del login (errore di business) quando l'email è presente (PE2) ma la password fornita non corrisponde a quella salvata (CP1 [ERROR]). Testato su Proprietario.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - email = utente@test.com
     * - password = Password123
     * ===========================
     */
    @Test
    @DisplayName("TC2: Password errata")
    void TC_Login_2() {

        LoginRequestDTO request = new LoginRequestDTO(VALID_EMAIL, RAW_PASSWORD);
        when(proprietarioRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(proprietarioMock));
        when(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(false); // CP1: La password non corrisponde

        UnauthorizedException thrown = assertThrows(UnauthorizedException.class, () -> {
            service.login(request);
        });

        Assertions.assertThat(thrown.getMessage()).isEqualTo("Credenziali non valide: password errata.");

        verify(proprietarioRepository, times(1)).findByEmail(VALID_EMAIL);
        verify(passwordEncoder, times(1)).matches(RAW_PASSWORD, ENCODED_PASSWORD);
        verify(veterinarioRepository, never()).findByEmail(any()); // Non deve cercare tra i veterinari
        verify(jwtUtil, never()).generateToken(any(), any(), any());
    }

    /**
     * ===========================
     * Test Case ID: TC_Login_3
     * Test Frame: TF3 (PE2 + CP2 - Valido)
     * Obiettivo:
     * Verificare il successo del login (valido) quando l'email è presente (PE2) e la password corrisponde (CP2). Testato su Veterinario per copertura.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - email = utente@test.com
     * - password = Password123
     * ===========================
     */
    @Test
    @DisplayName("TC3: Successo - Veterinario")
    void TC_Login_3() {
        // Arrange (Setup PE2, CP2)
        LoginRequestDTO request = new LoginRequestDTO(VALID_EMAIL, RAW_PASSWORD);

        // 1. Proprietario non trovato (fallisce)
        when(proprietarioRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());

        // 2. Veterinario trovato (successo)
        when(veterinarioRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(veterinarioMock));
        when(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateToken(veterinarioMock.getId(), veterinarioMock.getEmail(), Role.VETERINARIO))
                .thenReturn(GENERATED_TOKEN);

        AuthResponseDTO result = service.login(request);

        Assertions.assertThat(result.getToken()).isEqualTo(GENERATED_TOKEN);
        Assertions.assertThat(result.getUserId()).isEqualTo(veterinarioMock.getId());
        Assertions.assertThat(result.getRole()).isEqualTo(Role.VETERINARIO);

        verify(proprietarioRepository, times(1)).findByEmail(VALID_EMAIL);
        verify(veterinarioRepository, times(1)).findByEmail(VALID_EMAIL);
        verify(passwordEncoder, times(1)).matches(RAW_PASSWORD, ENCODED_PASSWORD);
        verify(jwtUtil, times(1)).generateToken(eq(veterinarioMock.getId()), eq(veterinarioMock.getEmail()), eq(Role.VETERINARIO));
    }
}