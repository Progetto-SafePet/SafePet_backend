package it.safepet.backend.gestioneTerapia.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.TerapiaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.TerapiaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.Terapia;
import it.safepet.backend.gestioneCartellaClinica.repository.TerapiaRepository;
import it.safepet.backend.gestioneCartellaClinica.service.terapia.GestioneTerapiaServiceImpl;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TerapiaServiceImplAggiungiTerapiaTest {

    @Mock
    private TerapiaRepository terapiaRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @InjectMocks
    private GestioneTerapiaServiceImpl service;

    private Validator validator;
    private MockedStatic<AuthContext> authContextMock;

    // ============================================================
    // Setup
    // ============================================================

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

    private TerapiaRequestDTO buildRequest(
            String nome,
            String forma,
            String dosaggio,
            String posologia,
            String via,
            String durata,
            String frequenza,
            String motivo,
            Long petId
    ) {
        TerapiaRequestDTO dto = new TerapiaRequestDTO();
        dto.setNome(nome);
        dto.setFormaFarmaceutica(forma);
        dto.setDosaggio(dosaggio);
        dto.setPosologia(posologia);
        dto.setViaDiSomministrazione(via);
        dto.setDurata(durata);
        dto.setFrequenza(frequenza);
        dto.setMotivo(motivo);
        dto.setPetId(petId);
        return dto;
    }

    private AuthenticatedUser buildVet(Long id) {
        return new AuthenticatedUser(id, "vet@test.com", Role.VETERINARIO);
    }

    // ============================================================
    // TEST CASES
    // ============================================================


    /**
     * TC_13_1
     * AU2: utente non autenticato [ERROR]
     *
     * Oracolo:
     *  Deve lanciare RuntimeException "Accesso non autorizzato"
     */
    @Test
    void TC_13_1() {
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(null);

        TerapiaRequestDTO dto = buildRequest(
                "Antibiotico", "Compressa", "20mg", "2 volte/die", "Orale",
                "7 giorni", "ogni 12h", "Infezione", 1L
        );

        assertThrows(RuntimeException.class, () -> service.aggiungiTerapia(dto));
        verifyNoInteractions(petRepository, terapiaRepository);
    }

    /**
     * TC_13_2
     * AU1, RU2: utente autenticato ma non veterinario [ERROR]
     */
    @Test
    void TC_13_2() {
        AuthenticatedUser owner = new AuthenticatedUser(50L, "p@test.com", Role.PROPRIETARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(owner);

        TerapiaRequestDTO dto = buildRequest(
                "Antibiotico", "Compressa", "20mg", "2 volte/die", "Orale",
                "7 giorni", "ogni 12h", "Infezione", 1L
        );

        assertThrows(RuntimeException.class, () -> service.aggiungiTerapia(dto));
        verifyNoInteractions(petRepository, terapiaRepository);
    }

    /**
     * TC_13_3
     * AU1, RU1
     * Pet inesistente → PE2 [ERROR]
     */
    @Test
    void TC_13_3() {
        // Utente veterinario autenticato
        AuthenticatedUser vet = buildVet(1L);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vet);

        // Mock veterinario (se richiesto dal servizio)
        Veterinario vetEntity = new Veterinario();
        vetEntity.setId(1L);
        vetEntity.setNome("Mario");
        vetEntity.setCognome("Rossi");

        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(vetEntity));

        // Pet inesistente → deve essere empty
        when(petRepository.findById(10L)).thenReturn(Optional.empty());

        // Costruisco DTO COMPLETAMENTE valido
        TerapiaRequestDTO dto = buildRequest(
                "Antibiotico",           // nome valido
                "Compressa",             // forma valida
                "20mg",                  // dosaggio valido
                "2 volte/die",           // posologia valida
                "Orale",                 // via valida
                "7 giorni",              // durata valida
                "ogni 12h",              // frequenza valida
                "Infezione",             // motivo valido
                10L                      // petId inesistente → PE2
        );

        assertThrows(RuntimeException.class, () -> service.aggiungiTerapia(dto));

        // ORACOLO: IL SERVIZIO DEVE AVER PROVATO A CERCARE IL PET
        verify(petRepository, times(1)).findById(10L);
    }


    /**
     * TC_13_4
     * Pet esistente → PE1
     * Ma dati non validi → nome vuoto [LT1]
     */
    @Test
    void TC_13_4() {
        TerapiaRequestDTO dto = buildRequest(
                "", "Compressa", "20mg", "Due volte/die",
                "Orale", "7", "12h", "Motivo valido", 1L
        );

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_13_5
     * Nome troppo corto (< 3)
     */
    @Test
    void TC_13_5() {
        TerapiaRequestDTO dto = buildRequest(
                "Ab", "Compressa", "20mg", "Due volte/die",
                "Orale", "7", "12h", "Motivo valido", 1L
        );

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_13_6
     * Nome > 100 caratteri
     */
    @Test
    void TC_13_6() {
        String longName = "a".repeat(101);
        TerapiaRequestDTO dto = buildRequest(
                longName, "Compressa", "20mg", "Due volte/die",
                "Orale", "7", "12h", "Motivo valido", 1L
        );

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_13_7
     * Via di somministrazione = 0 chars
     */
    @Test
    void TC_13_7() {
        TerapiaRequestDTO dto = buildRequest(
                "Antibiotico", "Compressa", "20mg", "2 volte/die",
                "", "7", "12h", "Motivo valido", 1L
        );

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }


    /**
     * TC_13_8
     * Motivo < 5 caratteri
     */
    @Test
    void TC_13_8() {
        TerapiaRequestDTO dto = buildRequest(
                "Antibiotico", "Compressa", "20mg", "2 volte/die",
                "Orale", "7", "12h", "Abc", 1L
        );

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }


    /**
     * TC_13_9
     * Caso COMPLETAMENTE VALIDO
     */
    @Test
    void TC_13_9() {
        // AU1, RU1: utente veterinario autenticato
        AuthenticatedUser vet = buildVet(1L);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vet);

        // MOCK: il veterinario ESISTE
        Veterinario vetEntity = new Veterinario();
        vetEntity.setId(1L);
        vetEntity.setNome("Mario");
        vetEntity.setCognome("Rossi");
        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(vetEntity));

        Pet pet = new Pet();
        pet.setId(10L);
        pet.setNome("Fido");
        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));

        when(petRepository.verificaAssociazionePetVeterinario(10L, 1L))
                .thenReturn(true);

        Terapia saved = new Terapia();
        saved.setId(99L);
        saved.setNome("Antibiotico");
        saved.setPet(pet);
        saved.setVeterinario(vetEntity);
        saved.setFormaFarmaceutica("Compressa");
        saved.setDosaggio("20mg");
        saved.setPosologia("2 volte/die");
        saved.setViaDiSomministrazione("Orale");
        saved.setDurata("7 giorni");
        saved.setFrequenza("ogni 12h");
        saved.setMotivo("Infezione");

        when(terapiaRepository.save(any(Terapia.class))).thenReturn(saved);

        TerapiaRequestDTO dto = buildRequest(
                "Antibiotico",          // nome
                "Compressa",            // formaFarmaceutica
                "20mg",                 // dosaggio
                "2 volte/die",          // posologia
                "Orale",                // via di somministrazione
                "7 giorni",             // durata
                "ogni 12h",             // frequenza
                "Infezione",            // motivo
                10L                     // petId
        );

        TerapiaResponseDTO resp = service.aggiungiTerapia(dto);

        assertThat(resp.getTerapiaId()).isEqualTo(99L);
        assertThat(resp.getNome()).isEqualTo("Antibiotico");
        assertThat(resp.getPetId()).isEqualTo(10L);
        assertThat(resp.getVeterinarioId()).isEqualTo(1L);

        assertThat(resp.getFormaFarmaceutica()).isEqualTo("Compressa");
        assertThat(resp.getDosaggio()).isEqualTo("20mg");
        assertThat(resp.getPosologia()).isEqualTo("2 volte/die");
        assertThat(resp.getViaDiSomministrazione()).isEqualTo("Orale");
        assertThat(resp.getDurata()).isEqualTo("7 giorni");
        assertThat(resp.getFrequenza()).isEqualTo("ogni 12h");
        assertThat(resp.getMotivo()).isEqualTo("Infezione");
        assertThat(resp.getNomeVeterinarioCompleto()).isEqualTo("Mario Rossi");

        verify(veterinarioRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).findById(10L);
        verify(petRepository, times(1)).verificaAssociazionePetVeterinario(10L, 1L);
        verify(terapiaRepository, times(1)).save(any(Terapia.class));
    }


}

