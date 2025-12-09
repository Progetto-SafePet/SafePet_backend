package it.safepet.backend.gestioneAnalisiDermatologica.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneAnalisiDermatologica.SkinDetectorAdapter;
import it.safepet.backend.gestioneAnalisiDermatologica.dto.RisultatoDiagnosiDTO;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalisiDermatologicaServiceImplTest {

    @Mock
    private SkinDetectorAdapter skinDetectorAdapter;

    @Mock
    private ProprietarioRepository proprietarioRepository;

    @InjectMocks
    private AnalisiDermatologicaServiceImpl service;

    private MockedStatic<AuthContext> authContextMock;

    // Costanti per i test
    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "proprietario@test.com";
    private static final byte[] VALID_PNG_HEADER = new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, 0x00};
    private static final byte[] VALID_JPG_HEADER = new byte[]{(byte) 0xFF, (byte) 0xD8, 0x00};

    @BeforeEach
    void setUp() throws Exception {
        authContextMock = mockStatic(AuthContext.class);

        // Iniezione manuale del repository tramite Reflection, poiché il service usa un mix di Constructor Injection e Field Injection
        Field repoField = AnalisiDermatologicaServiceImpl.class.getDeclaredField("proprietarioRepository");
        repoField.setAccessible(true);
        repoField.set(service, proprietarioRepository);
    }

    @AfterEach
    void tearDown() {
        authContextMock.close();
    }

    // --- HELPER METHODS ---

    private void mockAuthenticatedUser(Role role) {
        AuthenticatedUser user = new AuthenticatedUser(USER_ID, USER_EMAIL, role);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);
    }

    private void mockUserNotAuthenticated() {
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(null);
    }

    private void mockProprietarioExists(boolean exists) {
        lenient().when(proprietarioRepository.existsById(USER_ID)).thenReturn(exists);
    }

    // --- TEST CASES ---

    /**
     * ===========================
     * Test Case ID: TC_analisiDermatologica_1
     * Test Frame: AU2, DI1, FI1
     * Obiettivo:
     * Verificare che venga sollevata un'eccezione se l'utente non è autenticato.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - image = [Array di byte valido con header PNG]
     * ===========================
     */
    @Test
    void TC_analisiDermatologica_1() {
        mockUserNotAuthenticated();
        byte[] image = VALID_PNG_HEADER;

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.avviaAnalisiDermatologica(image));

        assertThat(exception.getMessage()).isEqualTo("Accesso non autorizzato");
        verifyNoInteractions(proprietarioRepository);
        verifyNoInteractions(skinDetectorAdapter);
    }

    /**
     * ===========================
     * Test Case ID: TC_analisiDermatologica_2
     * Test Frame: AU1, RU2, DI1, FI1
     * Obiettivo:
     * Verificare che venga sollevata un'eccezione se l'utente autenticato ha un ruolo diverso da PROPRIETARIO.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - image = [Array di byte valido con header PNG]
     * ===========================
     */
    @Test
    void TC_analisiDermatologica_2() {
        mockAuthenticatedUser(Role.VETERINARIO); // Ruolo non valido
        byte[] image = VALID_PNG_HEADER;

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.avviaAnalisiDermatologica(image));

        assertThat(exception.getMessage()).isEqualTo("Accesso non autorizzato");
        verifyNoInteractions(proprietarioRepository);
        verifyNoInteractions(skinDetectorAdapter);
    }

    /**
     * ===========================
     * Test Case ID: TC_analisiDermatologica_3
     * Test Frame: AU1, RU1, DI2
     * Obiettivo:
     * Verificare che venga sollevata un'eccezione se l'immagine è vuota (dimensione 0).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - image = [Array di byte vuoto]
     * ===========================
     */
    @Test
    void TC_analisiDermatologica_3() {
        mockAuthenticatedUser(Role.PROPRIETARIO);
        mockProprietarioExists(true);
        byte[] image = new byte[0]; // Dimensione 0

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.avviaAnalisiDermatologica(image));

        assertThat(exception.getMessage()).contains("Immagine non valida");
        verifyNoInteractions(skinDetectorAdapter);
    }

    /**
     * ===========================
     * Test Case ID: TC_analisiDermatologica_4
     * Test Frame: AU1, RU1, DI3
     * Obiettivo:
     * Verificare che venga sollevata un'eccezione se l'immagine supera la dimensione massima di 10MB.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - image = [Array di byte di dimensione 10MB + 1 byte]
     * ===========================
     */
    @Test
    void TC_analisiDermatologica_4() {
        mockAuthenticatedUser(Role.PROPRIETARIO);
        mockProprietarioExists(true);
        int size = 10 * 1024 * 1024 + 1; // 10MB + 1 byte
        byte[] image = new byte[size];

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.avviaAnalisiDermatologica(image));

        assertThat(exception.getMessage()).contains("Immagine troppo grande");
        verifyNoInteractions(skinDetectorAdapter);
    }

    /**
     * ===========================
     * Test Case ID: TC_analisiDermatologica_5
     * Test Frame: AU1, RU1, DI1, FI2
     * Obiettivo:
     * Verificare che venga sollevata un'eccezione se il formato dell'immagine non è supportato (né PNG né JPG).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - image = [Array di byte con magic number non validi]
     * ===========================
     */
    @Test
    void TC_analisiDermatologica_5() {
        mockAuthenticatedUser(Role.PROPRIETARIO);
        mockProprietarioExists(true);
        byte[] image = new byte[]{0x00, 0x01, 0x02, 0x03}; // Formato non valido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.avviaAnalisiDermatologica(image));

        assertThat(exception.getMessage()).contains("Formato immagine non supportato");
        verifyNoInteractions(skinDetectorAdapter);
    }

    /**
     * ===========================
     * Test Case ID: TC_analisiDermatologica_6
     * Test Frame: AU1, RU1, DI1, FI1
     * Obiettivo:
     * Verificare il corretto funzionamento: Utente proprietario, autenticato, immagine PNG valida e dimensione corretta.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     * - image = [Array di byte valido con header PNG]
     * ===========================
     */
    @Test
    void TC_analisiDermatologica_6() {
        mockAuthenticatedUser(Role.PROPRIETARIO);
        mockProprietarioExists(true);
        byte[] image = VALID_PNG_HEADER;

        RisultatoDiagnosiDTO expectedResponse = new RisultatoDiagnosiDTO("Dermatite", 0.95);
        when(skinDetectorAdapter.avviaAnalisiDermatologica(any(byte[].class), eq(MediaType.IMAGE_PNG)))
                .thenReturn(expectedResponse);

        RisultatoDiagnosiDTO result = service.avviaAnalisiDermatologica(image);

        assertThat(result).isNotNull();
        assertThat(result.classe()).isEqualTo("Dermatite");
        assertThat(result.confidence()).isEqualTo(0.95);

        verify(proprietarioRepository).existsById(USER_ID);
        verify(skinDetectorAdapter).avviaAnalisiDermatologica(image, MediaType.IMAGE_PNG);
    }
}
