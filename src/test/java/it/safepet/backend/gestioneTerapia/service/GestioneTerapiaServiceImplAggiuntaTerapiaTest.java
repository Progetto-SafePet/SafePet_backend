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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    /**
     * DTO completamente valido secondo:
     * LN4, FF4, DO3, PO4, VS4, DU3, FR4, MO4
     */
    private TerapiaRequestDTO buildValidRequest(Long petId) {
        return buildRequest(
                "Antibiotico",                // nome (>=3 && <=100)
                "Compressa",                  // formaFarmaceutica (len 2–50)
                "20mg",                       // dosaggio (len 1–20)
                "2 volte/die",                // posologia (len 5–100)
                "Orale",                      // via (len 2–50)
                "7 giorni",                   // durata (len 1–10)
                "ogni 12h",                   // frequenza (len 3–50)
                "Trattamento infezione",      // motivo (len 5–160)
                petId
        );
    }

    private AuthenticatedUser buildVet(Long id) {
        return new AuthenticatedUser(id, "vet@test.com", Role.VETERINARIO);
    }

    // ============================================================
    // TEST CASES dal Category Partitioning
    // ============================================================

    /**
     * TC_AggiungiTerapia_1:
     * AU2 [ERROR] → accesso non autorizzato (utente non autenticato)
     */
    @Test
    void TC_AggiungiTerapia_1() {
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(null);

        TerapiaRequestDTO dto = buildValidRequest(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.aggiungiTerapia(dto));

        assertThat(ex.getMessage()).isEqualTo("Accesso non autorizzato");
        verifyNoInteractions(veterinarioRepository, petRepository, terapiaRepository);
    }

    /**
     * TC_AggiungiTerapia_2:
     * AU1, RU2 [ERROR] → accesso non autorizzato (utente non veterinario)
     */
    @Test
    void TC_AggiungiTerapia_2() {
        AuthenticatedUser owner = new AuthenticatedUser(50L, "owner@test.com", Role.PROPRIETARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(owner);

        TerapiaRequestDTO dto = buildValidRequest(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.aggiungiTerapia(dto));

        assertThat(ex.getMessage()).isEqualTo("Accesso non autorizzato");
        verifyNoInteractions(veterinarioRepository, petRepository, terapiaRepository);
    }

    /**
     * TC_AggiungiTerapia_3:
     * Verifica che, se il petId non esiste nel DB,
     * il service lancia "Pet non trovato".
     */
    @Test
    void TC_AggiungiTerapia3() {
        // Utente veterinario autenticato (AU1, RU1)
        AuthenticatedUser vetUser = buildVet(1L);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);

        // Il veterinario ESISTE
        Veterinario vet = new Veterinario();
        vet.setId(1L);
        vet.setNome("Mario");
        vet.setCognome("Rossi");
        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(vet));

        // Il pet NON esiste nel DB
        when(petRepository.findById(10L)).thenReturn(Optional.empty());

        // DTO valido ma con petId inesistente
        TerapiaRequestDTO dto = buildValidRequest(10L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.aggiungiTerapia(dto));

        assertThat(ex.getMessage()).isEqualTo("Pet non trovato");

        verify(veterinarioRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).findById(10L);

        verify(petRepository, never()).verificaAssociazionePetVeterinario(anyLong(), anyLong());
        verifyNoInteractions(terapiaRepository);
    }

    /**
     * TC_Aggiungiterapia_4
     * Verifica che, se il pet esiste ma non è paziente del veterinario,
     * il service lancia "Il pet non è un paziente del veterinario".
     */
    @Test
    void TC_AggiungiTerapia_4() {
        // Utente veterinario autenticato (AU1, RU1)
        AuthenticatedUser vetUser = buildVet(1L);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);

        // Il veterinario ESISTE
        Veterinario vet = new Veterinario();
        vet.setId(1L);
        vet.setNome("Mario");
        vet.setCognome("Rossi");
        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(vet));

        // Il pet ESISTE nel DB
        Pet pet = new Pet();
        pet.setId(10L);
        pet.setNome("Fido");
        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));

        // ❌ Il pet NON è associato a quel veterinario
        when(petRepository.verificaAssociazionePetVeterinario(10L, 1L))
                .thenReturn(false);

        TerapiaRequestDTO dto = buildValidRequest(10L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.aggiungiTerapia(dto));

        assertThat(ex.getMessage()).isEqualTo("Il pet non è un paziente del veterinario");

        verify(veterinarioRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).findById(10L);
        verify(petRepository, times(1)).verificaAssociazionePetVeterinario(10L, 1L);

        verifyNoInteractions(terapiaRepository);
    }



    // ============================ NOME (LN) ============================

    /**
     * TC_AggiungiTerapia_5:
     * LN1 [ERROR] → nome vuoto
     */
    @Test
    void TC_AggiungiTerapia_5() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setNome(""); // lunghezza = 0

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_6:
     * LN2 [ERROR] → nome troppo corto
     */
    @Test
    void TC_AggiungiTerapia_6() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setNome("Ab"); // lunghezza = 2 (<3)

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_7:
     * LN3 [ERROR] → nome troppo lungo
     */
    @Test
    void TC_AggiungiTerapia_7() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setNome("a".repeat(101)); // lunghezza > 100

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    // ===================== FORMA FARMACEUTICA (FF) =====================

    /**
     * TC_AggiungiTerapia_8:
     * FF1 [ERROR] → forma farmaceutica vuota
     */
    @Test
    void TC_AggiungiTerapia_8() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setFormaFarmaceutica(""); // len = 0

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_9:
     * FF2 [ERROR] → forma farmaceutica troppo corta
     */
    @Test
    void TC_AggiungiTerapia_9() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setFormaFarmaceutica("A"); // len = 1

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_10:
     * FF3 [ERROR] → forma farmaceutica troppo lunga
     */
    @Test
    void TC_AggiungiTerapia_10() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setFormaFarmaceutica("a".repeat(51)); // > 50

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    // ========================== DOSAGGIO (DO) ==========================

    /**
     * TC_AggiungiTerapia_11:
     * DO1 [ERROR] → dosaggio vuoto
     */
    @Test
    void TC_AggiungiTerapia_11() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setDosaggio(""); // len = 0

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_12:
     * DO2 [ERROR] → dosaggio troppo lungo
     */
    @Test
    void TC_AggiungiTerapia_12() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setDosaggio("a".repeat(21)); // > 20

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    // ========================= POSOLOGIA (PO) ==========================

    /**
     * TC_AggiungiTerapia_13:
     * PO1 [ERROR] → posologia vuota
     */
    @Test
    void TC_AggiungiTerapia_13() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setPosologia(""); // len = 0

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_14:
     * PO2 [ERROR] → posologia troppo corta
     */
    @Test
    void TC_AggiungiTerapia_14() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setPosologia("abcd"); // len = 4 (<5)

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_15:
     * PO3 [ERROR] → posologia troppo lunga
     */
    @Test
    void TC_AggiungiTerapia_15() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setPosologia("a".repeat(101)); // > 100

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    // ============ VIA DI SOMMINISTRAZIONE (VS) =========================

    /**
     * TC_AggiungiTerapia_16:
     * VS1 [ERROR] → via di somministrazione vuota
     */
    @Test
    void TC_AggiungiTerapia_16() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setViaDiSomministrazione(""); // len = 0

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_17:
     * VS2 [ERROR] → via di somministrazione troppo corta
     */
    @Test
    void TC_AggiungiTerapia_17() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setViaDiSomministrazione("A"); // len = 1

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_18:
     * VS3 [ERROR] → via di somministrazione troppo lunga
     */
    @Test
    void TC_AggiungiTerapia_18() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setViaDiSomministrazione("a".repeat(101)); // > 100

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    // ============================= DURATA (DU) =============================

    /**
     * TC_AggiungiTerapia_19:
     * DU1 [ERROR] → durata vuota
     */
    @Test
    void TC_AggiungiTerapia_19() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setDurata(""); // len = 0

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_20:
     * DU2 [ERROR] → durata troppo lunga
     */
    @Test
    void TC_AggiungiTerapia_20() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setDurata("a".repeat(11)); // > 10

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    // =========================== FREQUENZA (FR) ============================

    /**
     * TC_AggiungiTerapia_21:
     * FR1 [ERROR] → frequenza vuota
     */
    @Test
    void TC_AggiungiTerapia_21() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setFrequenza(""); // len = 0

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_22:
     * FR2 [ERROR] → frequenza troppo corta
     */
    @Test
    void TC_AggiungiTerapia_22() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setFrequenza("ab"); // len = 2 (<3)

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_23:
     * FR3 [ERROR] → frequenza troppo lunga
     */
    @Test
    void TC_AggiungiTerapia_23() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setFrequenza("a".repeat(51)); // > 50

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    // ============================== MOTIVO (MO) ============================

    /**
     * TC_AggiungiTerapia_24:
     * MO1 [ERROR] → motivo vuoto
     */
    @Test
    void TC_AggiungiTerapia_24() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setMotivo(""); // len = 0

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_25:
     * MO2 [ERROR] → motivo troppo corto
     */
    @Test
    void TC_AggiungiTerapia_25() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setMotivo("abcd"); // len = 4 (<5)

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_26:
     * MO3 [ERROR] → motivo troppo lungo
     */
    @Test
    void TC_AggiungiTerapia_26() {
        TerapiaRequestDTO dto = buildValidRequest(1L);
        dto.setMotivo("a".repeat(161)); // > 160

        Set<ConstraintViolation<TerapiaRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }

    /**
     * TC_AggiungiTerapia_27:
     * AU1, RU1, LN4, FF4, DO3, PO4, VS4, DU3, FR4, MO4
     * → terapia aggiunta con successo
     */
    @Test
    void TC_AggiungiTerapia_27() {
        // AU1, RU1: veterinario autenticato
        AuthenticatedUser vetUser = buildVet(1L);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);

        // Veterinario esistente
        Veterinario vet = new Veterinario();
        vet.setId(1L);
        vet.setNome("Mario");
        vet.setCognome("Rossi");
        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(vet));

        // Pet esistente
        Pet pet = new Pet();
        pet.setId(10L);
        pet.setNome("Fido");
        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));

        // Associazione pet–veterinario valida
        when(petRepository.verificaAssociazionePetVeterinario(10L, 1L))
                .thenReturn(true);

        // Terapia salvata
        Terapia saved = new Terapia();
        saved.setId(99L);
        saved.setNome("Antibiotico");
        saved.setPet(pet);
        saved.setVeterinario(vet);
        saved.setFormaFarmaceutica("Compressa");
        saved.setDosaggio("20mg");
        saved.setPosologia("2 volte/die");
        saved.setViaDiSomministrazione("Orale");
        saved.setDurata("7 giorni");
        saved.setFrequenza("ogni 12h");
        saved.setMotivo("Trattamento infezione");
        when(terapiaRepository.save(any(Terapia.class))).thenReturn(saved);

        TerapiaRequestDTO dto = buildValidRequest(10L);

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
        assertThat(resp.getMotivo()).isEqualTo("Trattamento infezione");
        assertThat(resp.getNomeVeterinarioCompleto()).isEqualTo("Mario Rossi");

        verify(veterinarioRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).findById(10L);
        verify(petRepository, times(1)).verificaAssociazionePetVeterinario(10L, 1L);
        verify(terapiaRepository, times(1)).save(any(Terapia.class));
    }
}
