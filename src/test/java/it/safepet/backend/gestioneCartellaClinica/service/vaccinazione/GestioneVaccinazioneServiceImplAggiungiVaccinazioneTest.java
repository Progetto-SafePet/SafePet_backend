
package it.safepet.backend.gestioneCartellaClinica.service.vaccinazione;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneCartellaClinica.dto.VaccinazioneRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VaccinazioneResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.Vaccinazione;
import it.safepet.backend.gestioneCartellaClinica.model.Vaccinazione.Somministrazione;
import it.safepet.backend.gestioneCartellaClinica.repository.VaccinazioneRepository;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Classe di test generata a partire da Category Partitioning per il metodo
 * {@link GestioneVaccinazioneServiceImpl#aggiungiVaccinazione(Long, VaccinazioneRequestDTO)}.
 *
 * Approccio:
 * - Test black-box guidati dai Test Frame TC_1–TC_19.
 * - Errori di autenticazione / business: assertThrows + verifiche sulle interazioni con i repository.
 * - Errori di validazione DTO: Bean Validation su {@link VaccinazioneRequestDTO} senza coinvolgere il service.
 * - Casi corretti: verifica del {@link VaccinazioneResponseDTO} e del mapping dei campi.
 */
@ExtendWith(MockitoExtension.class)
class GestioneVaccinazioneServiceImplAggiungiVaccinazioneTest {

    private static final long VET_ID = 100L;
    private static final long PET_ID = 200L;

    @Mock
    private PetRepository petRepository;

    @Mock
    private VaccinazioneRepository vaccinazioneRepository;

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @InjectMocks
    private GestioneVaccinazioneServiceImpl service;

    private Validator validator;
    private MockedStatic<AuthContext> authContextMock;

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

    // =========================================================
    // Metodi helper
    // =========================================================

    private VaccinazioneRequestDTO createValidRequest() {
        LocalDate today = LocalDate.now();
        LocalDate dataSomministrazione = today.minusDays(1);
        LocalDate richiamoPrevisto = dataSomministrazione.plusDays(21);

        return new VaccinazioneRequestDTO(
                "RabiesVax",              // nomeVaccino: lunghezza 9 (LN4)
                "VaccinoBase",            // tipologia: lunghezza 11 (LT4)
                dataSomministrazione,     // dataDiSomministrazione: <= ACTUAL (ADS2)
                1.0f,                     // doseSomministrata: tra 0.1 e 10 (VDS3)
                "SOTTOCUTANEA",           // viaDiSomministrazione: valore ammesso (FS2)
                "Leggero arrossamento",   // effettiCollaterali: 1..200 (PEC2, LEC2)
                richiamoPrevisto          // richiamoPrevisto: >= data + 21 (ARP2)
        );
    }

    private AuthenticatedUser createAuthenticatedVetUser() {
        return new AuthenticatedUser(VET_ID, "vet@example.com", Role.VETERINARIO);
    }

    private Veterinario createVeterinarioEntity(Long id) {
        Veterinario v = new Veterinario();
        v.setId(id);
        v.setNome("Mario");
        v.setCognome("Rossi");
        return v;
    }

    private Pet createPetEntity(Long id, Veterinario associato, boolean associatoFlag) {
        Pet pet = new Pet();
        pet.setId(id);
        List<Veterinario> veterinariAssociati = new ArrayList<>();
        if (associatoFlag && associato != null) {
            veterinariAssociati.add(associato);
        }
        pet.setVeterinariAssociati(veterinariAssociati);
        return pet;
    }

    // =========================================================
    // Test Case TC_1 – TC_4: Autenticazione / Business
    // =========================================================

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_1
     * Test Frame: TF1
     * Obiettivo:
     *   Verificare che, in assenza di utente autenticato (AU2), la chiamata
     *   a aggiungiVaccinazione fallisca con errore di accesso non autorizzato
     *   prima di qualsiasi interazione con i repository, anche se il pet esiste
     *   e il DTO è altrimenti valido (PE1, LN4, FS2, FD2, ADS2, ARP2, VDS3, PEC2, LEC2, LT4).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 200
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_1() {
        VaccinazioneRequestDTO dto = createValidRequest();

        // AU2: utente non autenticato
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(null);

        assertThatThrownBy(() -> service.aggiungiVaccinazione(PET_ID, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Accesso non autorizzato");

        verifyNoInteractions(petRepository, veterinarioRepository, vaccinazioneRepository);
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_2
     * Test Frame: TF2
     * Obiettivo:
     *   Verificare che, con utente autenticato ma con ruolo diverso da VETERINARIO
     *   (AU1, RU2), la chiamata fallisca con errore di accesso non autorizzato
     *   senza interrogare i repository di dominio, anche se il pet esiste e
     *   il DTO è valido (PE1, LN4, FS2, FD2, ADS2, ARP2, VDS3, PEC2, LEC2, LT4).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 200
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_2() {
        VaccinazioneRequestDTO dto = createValidRequest();

        // AU1, RU2: utente autenticato ma non veterinario
        AuthenticatedUser nonVetUser = new AuthenticatedUser(VET_ID, "user@example.com", Role.PROPRIETARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(nonVetUser);

        assertThatThrownBy(() -> service.aggiungiVaccinazione(PET_ID, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Accesso non autorizzato");

        verifyNoInteractions(petRepository, veterinarioRepository, vaccinazioneRepository);
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_3
     * Test Frame: TF3
     * Obiettivo:
     *   Verificare che, in presenza di utente veterinario autenticato (AU1, RU1),
     *   ma con pet non esistente nel database (PE2), venga sollevato l’errore
     *   "Pet non trovato" dopo il recupero del veterinario ma prima di salvare
     *   qualsiasi vaccinazione, anche se il DTO è valido (LN4, FS2, FD2, ADS2,
     *   ARP2, VDS3, PEC2, LEC2, LT4).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 200 (PE2: non esiste)
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_3() {
        VaccinazioneRequestDTO dto = createValidRequest();

        AuthenticatedUser vetUser = createAuthenticatedVetUser();
        Veterinario veterinario = createVeterinarioEntity(VET_ID);

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        when(veterinarioRepository.findById(VET_ID)).thenReturn(Optional.of(veterinario));
        when(petRepository.findById(PET_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.aggiungiVaccinazione(PET_ID, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Pet non trovato");

        verify(veterinarioRepository).findById(VET_ID);
        verify(petRepository).findById(PET_ID);
        verifyNoInteractions(vaccinazioneRepository);
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_4
     * Test Frame: TF4
     * Obiettivo:
     *   Verificare che, in presenza di utente veterinario autenticato (AU1, RU1),
     *   pet esistente nel database (PE1) ma non associato al veterinario (AP2),
     *   il metodo sollevi l’errore "Il pet non è un paziente del veterinario"
     *   senza salvare alcuna vaccinazione, con DTO valido (LN4, FS2, FD2, ADS2,
     *   ARP2, VDS3, PEC2, LEC2, LT4).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 200 (PE1, AP2)
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_4() {
        VaccinazioneRequestDTO dto = createValidRequest();

        AuthenticatedUser vetUser = createAuthenticatedVetUser();
        Veterinario veterinario = createVeterinarioEntity(VET_ID);

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        when(veterinarioRepository.findById(VET_ID)).thenReturn(Optional.of(veterinario));

        // Pet esistente ma non associato al veterinario (lista vuota)
        Pet pet = createPetEntity(PET_ID, veterinario, false);
        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(pet));

        assertThatThrownBy(() -> service.aggiungiVaccinazione(PET_ID, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Il pet non è un paziente del veterinario");

        verify(veterinarioRepository).findById(VET_ID);
        verify(petRepository).findById(PET_ID);
        verifyNoInteractions(vaccinazioneRepository);
    }

    // =========================================================
    // Test Case TC_5 – TC_9: Errori DTO (nome, via, data)
    // =========================================================

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_5
     * Test Frame: TF5
     * Obiettivo:
     *   Verificare che un DTO con nome vaccino vuoto (LN1, lunghezza = 0)
     *   produca almeno una violazione di Bean Validation (NotBlank/Size),
     *   a fronte di tutti gli altri campi corretti (FS2, FD2, ADS2, ARP2,
     *   VDS3, PEC2, LEC2, LT4).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "" (stringa vuota, LN1)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_5() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setNomeVaccino("");

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_6
     * Test Frame: TF6
     * Obiettivo:
     *   Verificare che un DTO con nome vaccino troppo corto (LN2, lunghezza 1–2)
     *   produca almeno una violazione di Bean Validation sulla Size minima,
     *   con tutti gli altri campi corretti.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "AB" (2 caratteri, LN2)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_6() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setNomeVaccino("AB");

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_7
     * Test Frame: TF7
     * Obiettivo:
     *   Verificare che un DTO con nome vaccino troppo lungo (LN3, lunghezza > 20)
     *   produca almeno una violazione di Bean Validation sulla Size massima,
     *   con tutti gli altri campi corretti.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = stringa di 21 caratteri (LN3)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_7() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setNomeVaccino("AAAAAAAAAAAAAAAAAAAAA"); // 21 caratteri

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_8
     * Test Frame: TF8
     * Obiettivo:
     *   Verificare che un DTO con via di somministrazione non conforme al pattern
     *   previsto (FS1: non appartiene all'insieme ammesso) produca una violazione
     *   di Bean Validation sul Pattern, con tutti gli altri campi corretti.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "ENDOVENA" (FS1: non ammessa)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_8() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setViaDiSomministrazione("ENDOVENA");

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_9
     * Test Frame: TF9
     * Obiettivo:
     *   Rappresentare il caso di formato data non corretto (FD1). A livello di DTO
     *   il formato stringa viene già trasformato in LocalDate dallo strato esterno;
     *   in questo test si simula l'assenza del valore (dataDiSomministrazione = null)
     *   che deve produrre una violazione @NotNull, a fronte di tutti gli altri campi
     *   corretti (LN4, FS2, ADS2, ARP2, VDS3, PEC2, LEC2, LT4).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = null (FD1 simulato)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = oggi + 20 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_9() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setDataDiSomministrazione(null);

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    // =========================================================
    // Test Case TC_10 – TC_11: Regole di ammissione su date (business)
    // =========================================================

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_10
     * Test Frame: TF10
     * Obiettivo:
     *   Verificare il comportamento atteso in caso di data di somministrazione
     *   futura (ADS1: dataDiSomministrazione > ACTUAL). Questo test esprime una
     *   regola di business: il sistema dovrebbe rifiutare una vaccinazione con
     *   data futura, sollevando un'eccezione a runtime, a fronte di tutti gli altri
     *   parametri corretti (AU1, RU1, PE1, AP1, LN4, FS2, FD2, ARP2, VDS3, PEC2, LEC2, LT4).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 200 (PE1, AP1)
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi + 1 giorno (ADS1)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_10() {
        VaccinazioneRequestDTO dto = createValidRequest();
        LocalDate futureDate = LocalDate.now().plusDays(1);
        dto.setDataDiSomministrazione(futureDate);
        dto.setRichiamoPrevisto(futureDate.plusDays(21));

        AuthenticatedUser vetUser = createAuthenticatedVetUser();
        Veterinario veterinario = createVeterinarioEntity(VET_ID);
        Pet pet = createPetEntity(PET_ID, veterinario, true);

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        when(veterinarioRepository.findById(VET_ID)).thenReturn(Optional.of(veterinario));
        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(pet));

        // Nota: il codice attuale potrebbe non implementare ancora questa regola.
        // Il test esprime il comportamento desiderato come specificato nel Test Frame.
        assertThatThrownBy(() -> service.aggiungiVaccinazione(PET_ID, dto))
                .isInstanceOf(RuntimeException.class);

        verify(veterinarioRepository).findById(VET_ID);
        verify(petRepository).findById(PET_ID);
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_11
     * Test Frame: TF11
     * Obiettivo:
     *   Verificare il comportamento atteso in caso di richiamo previsto troppo
     *   precoce (ARP1: richiamoPrevisto < dataDiSomministrazione + 21 giorni).
     *   Anche questa è una regola di business: il sistema dovrebbe rifiutare
     *   la richiesta sollevando un'eccezione, con tutti gli altri parametri
     *   corretti (AU1, RU1, PE1, AP1, LN4, FS2, FD2, ADS2, VDS3, PEC2, LEC2, LT4).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 200 (PE1, AP1)
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 7 giorni (ARP1)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_11() {
        VaccinazioneRequestDTO dto = createValidRequest();
        LocalDate dataSomministrazione = LocalDate.now().minusDays(1);
        LocalDate richiamoTroppoPrecoce = dataSomministrazione.plusDays(7);
        dto.setDataDiSomministrazione(dataSomministrazione);
        dto.setRichiamoPrevisto(richiamoTroppoPrecoce);

        AuthenticatedUser vetUser = createAuthenticatedVetUser();
        Veterinario veterinario = createVeterinarioEntity(VET_ID);
        Pet pet = createPetEntity(PET_ID, veterinario, true);

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        when(veterinarioRepository.findById(VET_ID)).thenReturn(Optional.of(veterinario));
        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(pet));

        // Nota: il codice attuale potrebbe non implementare ancora questa regola.
        assertThatThrownBy(() -> service.aggiungiVaccinazione(PET_ID, dto))
                .isInstanceOf(RuntimeException.class);

        verify(veterinarioRepository).findById(VET_ID);
        verify(petRepository).findById(PET_ID);
    }

    // =========================================================
    // Test Case TC_12 – TC_17: Errori DTO (dose, effetti, tipologia)
    // =========================================================

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_12
     * Test Frame: TF12
     * Obiettivo:
     *   Verificare che una dose somministrata pari a 0 (VDS1) violi i vincoli
     *   di Bean Validation definiti su doseSomministrata (@DecimalMin 0.1),
     *   con tutti gli altri parametri corretti (LN4, FS2, FD2, ADS2, ARP2,
     *   PEC2, LEC2, LT4).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 0.0 (VDS1)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_12() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setDoseSomministrata(0.0f);

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_13
     * Test Frame: TF13
     * Obiettivo:
     *   Verificare che una dose somministrata maggiore di 10 (VDS2) violi i
     *   vincoli di Bean Validation su doseSomministrata (@DecimalMax 10.0),
     *   con tutti gli altri parametri corretti (LN4, FS2, FD2, ADS2, ARP2,
     *   PEC2, LEC2, LT4).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 11.0 (VDS2)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_13() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setDoseSomministrata(11.0f);

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_14
     * Test Frame: TF14
     * Obiettivo:
     *   Verificare che effetti collaterali troppo lunghi (> 200 caratteri, LEC1)
     *   violino il vincolo @Size(min = 1, max = 200) presente sul campo,
     *   con tutti gli altri parametri corretti (AU1, RU1, PE1, AP1, LN4, FS2,
     *   FD2, ADS2, ARP2, VDS3, PEC2, LT4).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = stringa di 201 caratteri (PEC2, LEC1)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_14() {
        VaccinazioneRequestDTO dto = createValidRequest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            sb.append('a');
        }
        dto.setEffettiCollaterali(sb.toString());

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_15
     * Test Frame: TF15
     * Obiettivo:
     *   Verificare che una tipologia vuota (LT1, lunghezza = 0) produca almeno
     *   una violazione di Bean Validation (NotBlank/Size), con tutti gli altri
     *   parametri corretti (LN4, FS2, FD2, ADS2, ARP2, VDS3, PEC2, LEC2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "" (LT1)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_15() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setTipologia("");

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_16
     * Test Frame: TF16
     * Obiettivo:
     *   Verificare che una tipologia troppo corta (LT2, 1–2 caratteri) violi
     *   il vincolo di lunghezza minima definito su tipologia, con tutti gli altri
     *   parametri corretti (LN4, FS2, FD2, ADS2, ARP2, VDS3, PEC2, LEC2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "AB" (2 caratteri, LT2)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_16() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setTipologia("AB");

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_17
     * Test Frame: TF17
     * Obiettivo:
     *   Verificare che una tipologia troppo lunga (LT3, lunghezza > 20) violi
     *   il vincolo di lunghezza massima definito su tipologia, con tutti gli altri
     *   parametri corretti (LN4, FS2, FD2, ADS2, ARP2, VDS3, PEC2, LEC2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = stringa di 21 caratteri (LT3)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_17() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setTipologia("AAAAAAAAAAAAAAAAAAAAA"); // 21 caratteri

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    // =========================================================
    // Test Case TC_18 – TC_19: Casi corretti
    // =========================================================

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_18
     * Test Frame: TF18
     * Obiettivo:
     *   Verificare il caso completamente corretto con effetti collaterali
     *   presenti e di lunghezza ammessa (PEC2, LEC2), utente veterinario
     *   autenticato (AU1, RU1), pet esistente e associato (PE1, AP1), e DTO
     *   valido in ogni campo (LN4, FS2, FD2, ADS2, ARP2, VDS3, LT4). Il test
     *   controlla:
     *   - assenza di violazioni Bean Validation sul DTO;
     *   - corretta interazione con i repository;
     *   - mapping corretto nel VaccinazioneResponseDTO, inclusa la conversione
     *     della via di somministrazione in enum Somministrazione e la composizione
     *     del nome completo del veterinario.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 200 (PE1, AP1)
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = "Leggero arrossamento" (PEC2, LEC2)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_18() {
        VaccinazioneRequestDTO dto = createValidRequest();

        // DTO valido: nessuna violazione attesa
        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();

        AuthenticatedUser vetUser = createAuthenticatedVetUser();
        Veterinario veterinario = createVeterinarioEntity(VET_ID);
        Pet pet = createPetEntity(PET_ID, veterinario, true);

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        when(veterinarioRepository.findById(VET_ID)).thenReturn(Optional.of(veterinario));
        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(pet));

        when(vaccinazioneRepository.save(any(Vaccinazione.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VaccinazioneResponseDTO response = service.aggiungiVaccinazione(PET_ID, dto);

        assertThat(response).isNotNull();
        assertThat(response.getNomeVaccino()).isEqualTo(dto.getNomeVaccino());
        assertThat(response.getPetId()).isEqualTo(PET_ID);
        assertThat(response.getVeterinarioId()).isEqualTo(VET_ID);
        assertThat(response.getTipologia()).isEqualTo(dto.getTipologia());
        assertThat(response.getDataDiSomministrazione()).isEqualTo(dto.getDataDiSomministrazione());
        assertThat(response.getDoseSomministrata()).isEqualTo(dto.getDoseSomministrata());
        assertThat(response.getViaDiSomministrazione()).isEqualTo(Somministrazione.SOTTOCUTANEA.name());
        assertThat(response.getEffettiCollaterali()).isEqualTo(dto.getEffettiCollaterali());
        assertThat(response.getRichiamoPrevisto()).isEqualTo(dto.getRichiamoPrevisto());
        assertThat(response.getNomeVeterinarioCompleto()).isEqualTo("Mario Rossi");

        verify(veterinarioRepository).findById(VET_ID);
        verify(petRepository).findById(PET_ID);
        verify(vaccinazioneRepository).save(any(Vaccinazione.class));
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_19
     * Test Frame: TF19
     * Obiettivo:
     *   Verificare il caso corretto in cui gli effetti collaterali non sono
     *   presenti (PEC1 → effettiCollaterali = null), ma tutte le altre categorie
     *   sono valide (AU1, RU1, PE1, AP1, LN4, FS2, FD2, ADS2, ARP2, VDS3, LT4).
     *   Il test controlla che:
     *   - il DTO sia valido dal punto di vista di Bean Validation;
     *   - il service salvi comunque la vaccinazione;
     *   - il VaccinazioneResponseDTO contenga effettiCollaterali null.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 200 (PE1, AP1)
     *   - nomeVaccino = "RabiesVax" (LN4)
     *   - tipologia = "VaccinoBase" (LT4)
     *   - dataDiSomministrazione = oggi - 1 giorno (ADS2)
     *   - doseSomministrata = 1.0 (VDS3)
     *   - viaDiSomministrazione = "SOTTOCUTANEA" (FS2)
     *   - effettiCollaterali = null (PEC1)
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni (ARP2)
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_19() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setEffettiCollaterali(null);

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();

        AuthenticatedUser vetUser = createAuthenticatedVetUser();
        Veterinario veterinario = createVeterinarioEntity(VET_ID);
        Pet pet = createPetEntity(PET_ID, veterinario, true);

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        when(veterinarioRepository.findById(VET_ID)).thenReturn(Optional.of(veterinario));
        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(pet));

        ArgumentCaptor<Vaccinazione> vaccinazioneCaptor = ArgumentCaptor.forClass(Vaccinazione.class);
        when(vaccinazioneRepository.save(vaccinazioneCaptor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VaccinazioneResponseDTO response = service.aggiungiVaccinazione(PET_ID, dto);

        assertThat(response).isNotNull();
        assertThat(response.getEffettiCollaterali()).isNull();

        Vaccinazione salvata = vaccinazioneCaptor.getValue();
        assertThat(salvata).isNotNull();
        assertThat(salvata.getEffettiCollaterali()).isNull();

        verify(veterinarioRepository).findById(VET_ID);
        verify(petRepository).findById(PET_ID);
        verify(vaccinazioneRepository).save(any(Vaccinazione.class));
    }
}
