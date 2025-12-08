package it.safepet.backend.gestionePaziente.Service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.exception.ConflictException;
import it.safepet.backend.exception.NotFoundException;
import it.safepet.backend.exception.UnauthorizedException;
import it.safepet.backend.gestionePaziente.dto.PazienteRequestDTO;
import it.safepet.backend.gestionePaziente.model.LinkingCode;
import it.safepet.backend.gestionePaziente.repository.LinkingCodeRepository;
import it.safepet.backend.gestionePaziente.service.GestionePazienteServiceImpl;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class GestionePazienteServiceImplAggiungiPazienteTest {

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @Mock
    private LinkingCodeRepository linkingCodeRepository;

    @InjectMocks
    private GestionePazienteServiceImpl service;

    private Validator validator;
    private MockedStatic<AuthContext> authContextMock;

    private final Long VETERINARIO_ID = 100L;
    private final String LINKING_CODE_VALIDO = "XYZ123";
    private final Long PET_ID = 1L;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        authContextMock = mockStatic(AuthContext.class);
    }

    @AfterEach
    void tearDown() {
        if (authContextMock != null) {
            authContextMock.close();
        }
    }

    /**
     * Helper pulito: crea solo l'istanza mockata.
     */
    private Veterinario createMockVeterinario(Long id, List<Pet> associatedPets) {
        return mock(Veterinario.class);
    }

    /**
     * Helper pulito: crea solo l'istanza mockata.
     */
    private LinkingCode createMockLinkingCode() {
        return mock(LinkingCode.class);
    }

    // ================================================================
    // TEST CASES
    // ================================================================

    /**
     * * ===========================
     * Test Case ID: TC_aggiungiPaziente_1
     * Test Frame: AU2, RU1, PE1, LC1, US1, SC1, AS1
     * Obiettivo:
     * Verificare che il metodo fallisca se l'Utente non è Autenticato
     * <p>
     * Parametri di input:
     * petId: 1
     * linkingCode: "XYZ123"
     * ===========================
     */
    @Test
    @DisplayName("TF1: Fallimento - Utente non autenticato (AU2)")
    void TC_aggiungiPaziente_1() {
        // Arrange
        PazienteRequestDTO dto = new PazienteRequestDTO(LINKING_CODE_VALIDO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(null);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> service.aggiungiPaziente(dto));
    }

    /**
     * * ===========================
     * Test Case ID: TC_aggiungiPaziente_2
     * Test Frame: AU1, RU2, PE1, LC1, US1, SC1, AS1
     * Obiettivo:
     * Verificare che il metodo fallisca se l'utente è autenticato, ma non ha il ruolo di Veterinario
     * <p>
     * Parametri di input:
     * petId: 1
     * linkingCode: "XYZ123"
     * ===========================
     */
    @Test
    @DisplayName("TF2: Fallimento - Ruolo != Veterinario (RU2)")
    void TC_aggiungiPaziente_2() {
        // Arrange
        PazienteRequestDTO dto = new PazienteRequestDTO(LINKING_CODE_VALIDO);
        AuthenticatedUser unauthorizedUser = new AuthenticatedUser(VETERINARIO_ID, "prop@test.it", Role.PROPRIETARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(unauthorizedUser);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> service.aggiungiPaziente(dto));
    }

    /**
     * * ===========================
     * Test Case ID: TC_aggiungiPaziente_3
     * Test Frame: AU1, RU1, PE2, LC1, US1, SC1, AS1
     * Obiettivo:
     * Verificare che il metodo fallisca se l'ID del Pet non esiste nel DB
     * <p>
     * Parametri di input:
     * petId: 99
     * linkingCode: "XYZ123"
     * ===========================
     */
    @Test
    @DisplayName("TF3: Fallimento - Veterinario non trovato nel DB (PE2)")
    void TC_aggiungiPaziente_3() {
        // Arrange
        PazienteRequestDTO dto = new PazienteRequestDTO(LINKING_CODE_VALIDO);
        AuthenticatedUser vetUser = new AuthenticatedUser(VETERINARIO_ID, "vet@test.it", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);

        when(veterinarioRepository.findById(VETERINARIO_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.aggiungiPaziente(dto));
    }

    /**
     * * ===========================
     * Test Case ID: TC_aggiungiPaziente_4
     * Test Frame: AU1, RU1, PE1, LC2, US1, SC1, AS1
     * Obiettivo:
     * Verificare che il metodo fallisca se il Linking Code non esiste nel DB
     * <p>
     * Parametri di input:
     * petId: 1
     * linkingCode: "XYZ999"
     * ===========================
     */
    @Test
    @DisplayName("TF4: Fallimento - Linking Code non esiste (LC2)")
    void TC_aggiungiPaziente_4() {
        // Arrange
        final String NON_EXISTING_CODE = "XYZ999";
        PazienteRequestDTO dto = new PazienteRequestDTO(NON_EXISTING_CODE);

        AuthenticatedUser vetUser = new AuthenticatedUser(VETERINARIO_ID, "vet@test.it", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        Veterinario mockVet = mock(Veterinario.class);

        // Stub essenziali
        lenient().when(mockVet.getId()).thenReturn(VETERINARIO_ID);
        when(veterinarioRepository.findById(VETERINARIO_ID)).thenReturn(Optional.of(mockVet));

        when(linkingCodeRepository.findByCodice(NON_EXISTING_CODE)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.aggiungiPaziente(dto));

        // Verify
        verify(mockVet, never()).getPetsAssociati();
        verify(linkingCodeRepository, times(1)).findByCodice(NON_EXISTING_CODE);
    }

    /**
     * * ===========================
     * Test Case ID: TC_aggiungiPaziente_5
     * Test Frame: AU1, RU1, PE1, LC1, US2, SC1, AS1
     * Obiettivo:
     * Verificare che il metodo fallisca se il Linking Code è già stato usato
     * <p>
     * Parametri di input:
     * petId: 1
     * linkingCode: "XYZ123"
     * ===========================
     */
    @Test
    @DisplayName("TF5: Fallimento - Linking Code già utilizzato (US2)")
    void TC_aggiungiPaziente_5() {
        // Arrange
        PazienteRequestDTO dto = new PazienteRequestDTO(LINKING_CODE_VALIDO);

        AuthenticatedUser vetUser = new AuthenticatedUser(VETERINARIO_ID, "vet@test.it", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        Veterinario mockVet = mock(Veterinario.class);

        // Stub essenziali
        lenient().when(mockVet.getId()).thenReturn(VETERINARIO_ID);
        when(veterinarioRepository.findById(VETERINARIO_ID)).thenReturn(Optional.of(mockVet));

        LinkingCode mockUsedCode = createMockLinkingCode();

        when(linkingCodeRepository.findByCodice(LINKING_CODE_VALIDO)).thenReturn(Optional.of(mockUsedCode));

        // Stubs essenziali per il fallimento US2
        when(mockUsedCode.getUsato()).thenReturn(true);

        // Stubs lenient
        lenient().when(mockUsedCode.isScaduto()).thenReturn(false);
        lenient().when(mockUsedCode.getCodice()).thenReturn(LINKING_CODE_VALIDO);
        lenient().when(mockUsedCode.getDataScadenza()).thenReturn(LocalDate.now().plusDays(1));
        lenient().when(mockUsedCode.getPet()).thenReturn(mock(Pet.class));


        // Act & Assert
        assertThatThrownBy(() -> service.aggiungiPaziente(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("già utilizzato");

        // Verify
        verify(mockVet, never()).getPetsAssociati();
        verify(mockUsedCode, never()).setUsato(true);
    }

    /**
     * * ===========================
     * Test Case ID: TC_aggiungiPaziente_6
     * Test Frame: AU1, RU1, PE1, LC1, US1, SC2, AS1
     * Obiettivo:
     * Verificare che il metodo fallisca se il Linking Code è scaduto
     * <p>
     * Parametri di input:
     * petId: 1
     * linkingCode: "XYZ123"
     * ===========================
     */
    @Test
    @DisplayName("TF6: Fallimento - Linking Code Scaduto (SC2)")
    void TC_aggiungiPaziente_6() {
        // Arrange
        PazienteRequestDTO dto = new PazienteRequestDTO(LINKING_CODE_VALIDO);

        AuthenticatedUser vetUser = new AuthenticatedUser(VETERINARIO_ID, "vet@test.it", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        Veterinario mockVet = mock(Veterinario.class);

        // Stub essenziali
        lenient().when(mockVet.getId()).thenReturn(VETERINARIO_ID);
        when(veterinarioRepository.findById(VETERINARIO_ID)).thenReturn(Optional.of(mockVet));

        LocalDate expiredDate = LocalDate.now().minusDays(1);
        LinkingCode mockExpiredCode = createMockLinkingCode();

        when(linkingCodeRepository.findByCodice(LINKING_CODE_VALIDO)).thenReturn(Optional.of(mockExpiredCode));

        // Stubs essenziali per il fallimento SC2
        when(mockExpiredCode.isScaduto()).thenReturn(true);
        when(mockExpiredCode.getDataScadenza()).thenReturn(expiredDate);

        // Stubs lenient
        lenient().when(mockExpiredCode.getUsato()).thenReturn(false);
        lenient().when(mockExpiredCode.getCodice()).thenReturn(LINKING_CODE_VALIDO);
        lenient().when(mockExpiredCode.getPet()).thenReturn(mock(Pet.class));


        // Act & Assert
        assertThatThrownBy(() -> service.aggiungiPaziente(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("scaduto in data");

        // Verify
        verify(mockVet, never()).getPetsAssociati();
        verify(mockExpiredCode, never()).setUsato(true);
    }

    /**
     * * ===========================
     * Test Case ID: TC_aggiungiPaziente_7
     * Test Frame: AU1, RU1, PE1, LC1, US1, SC1, AS2
     * Obiettivo:
     * Verificare che il metodo fallisca se il Pet è già associato al Veterinario corrente.
     * <p>
     * Parametri di input:
     * petId: 1
     * linkingCode: "XYZ123"
     * ===========================
     */
    @Test
    @DisplayName("TF7: Fallimento - Pet già associato al Veterinario (AS2)")
    void TC_aggiungiPaziente_7() {
        // Arrange
        PazienteRequestDTO dto = new PazienteRequestDTO(LINKING_CODE_VALIDO);

        AuthenticatedUser vetUser = new AuthenticatedUser(VETERINARIO_ID, "vet@test.it", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);

        Pet mockPet = mock(Pet.class);

        // Stubs per la logica AS2
        List<Pet> associatedPetsMock = mock(List.class);
        when(associatedPetsMock.contains(mockPet)).thenReturn(true); // AS2: Pet già associato

        Veterinario mockVet = mock(Veterinario.class);
        // Stubbing di getPetsAssociati qui dove è usato
        when(mockVet.getPetsAssociati()).thenReturn(associatedPetsMock);

        // Stub essenziali
        lenient().when(mockVet.getId()).thenReturn(VETERINARIO_ID);
        when(veterinarioRepository.findById(VETERINARIO_ID)).thenReturn(Optional.of(mockVet));

        LinkingCode mockValidCode = createMockLinkingCode();
        when(linkingCodeRepository.findByCodice(LINKING_CODE_VALIDO)).thenReturn(Optional.of(mockValidCode));

        // Stubs essenziali per l'accesso a getPet()
        when(mockValidCode.getPet()).thenReturn(mockPet);

        // Stubs lenient per SC1 e US1
        lenient().when(mockValidCode.getUsato()).thenReturn(false);
        lenient().when(mockValidCode.isScaduto()).thenReturn(false);

        // Act & Assert
        assertThrows(ConflictException.class, () -> service.aggiungiPaziente(dto));

        // Verify
        verify(associatedPetsMock, times(1)).contains(mockPet);
        verify(associatedPetsMock, never()).add(mockPet);
    }

    /**
     * * ===========================
     * Test Case ID: TC_aggiungiPaziente_8
     * Test Frame: AU1, RU1, PE1, LC1, US1, SC1, AS1
     * Obiettivo:
     * Verificare il percorso di successo completo e l'avvenuta associazione del pet con l'aggiornamento dello stato del codice
     * <p>
     * Parametri di input:
     * petId: 1
     * linkingCode: "XYZ123"
     * ===========================
     */
    @Test
    @DisplayName("TF8: Successo - Aggiunta paziente e aggiornamento codice (Happy Path)")
    void TC_aggiungiPaziente_8() {
        // Arrange
        PazienteRequestDTO dto = new PazienteRequestDTO(LINKING_CODE_VALIDO);

        AuthenticatedUser vetUser = new AuthenticatedUser(VETERINARIO_ID, "vet@test.it", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);

        Pet mockPet = mock(Pet.class);

        // Stubs per la logica di successo
        List<Veterinario> petVetsMock = mock(List.class);
        List<Pet> vetPetsMock = mock(List.class);

        when(mockPet.getVeterinariAssociati()).thenReturn(petVetsMock);
        when(vetPetsMock.contains(mockPet)).thenReturn(false); // AS1

        Veterinario mockVet = mock(Veterinario.class);
        // Stubbing di getPetsAssociati qui dove è usato
        when(mockVet.getPetsAssociati()).thenReturn(vetPetsMock);

        // Stub essenziali
        lenient().when(mockVet.getId()).thenReturn(VETERINARIO_ID);
        when(veterinarioRepository.findById(VETERINARIO_ID)).thenReturn(Optional.of(mockVet));

        LinkingCode mockValidCode = createMockLinkingCode();
        when(linkingCodeRepository.findByCodice(LINKING_CODE_VALIDO)).thenReturn(Optional.of(mockValidCode));

        // Stubs essenziali per il successo
        when(mockValidCode.getPet()).thenReturn(mockPet);
        lenient().when(mockValidCode.getUsato()).thenReturn(false); // US1
        lenient().when(mockValidCode.isScaduto()).thenReturn(false); // SC1

        // Act
        service.aggiungiPaziente(dto);

        // Assert/Verify
        verify(vetPetsMock, times(1)).add(mockPet);
        verify(petVetsMock, times(1)).add(mockVet);
        verify(mockValidCode, times(1)).setUsato(true);
        verify(linkingCodeRepository, times(1)).save(mockValidCode);
        verify(veterinarioRepository, times(1)).save(mockVet);
    }
}