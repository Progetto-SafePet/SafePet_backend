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
 * - Test black-box guidati dai Test Frame TC_1–TC_20.
 * - Errori di autenticazione / business: assertThrows.
 * - Errori di validazione DTO: Bean Validation su {@link VaccinazioneRequestDTO}.
 * - Casi corretti: verifica del {@link VaccinazioneResponseDTO} e del mapping dei campi.
 */
@ExtendWith(MockitoExtension.class)
class GestioneVaccinazioneServiceImplAggiungiVaccinazioneTest {

    private static final long VET_ID = 20L;
    private static final long PET_ID = 1L;

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
                "RabiesVax",              // nomeVaccino: LN4 (3..20)
                "VaccinoBase",            // tipologia: LT4 (3..20)
                dataSomministrazione,     // ADS2: <= ACTUAL
                1.0f,                     // VDS3: [0.1..10]
                "SOTTOCUTANEA",           // FS2: formato valido
                "Leggero arrossamento",   // PEC2 + LEC2: 1..200
                richiamoPrevisto          // ARP2: >= data + 21 giorni
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
    // Test Case TC_1 – TC_5: Autenticazione / Veterinario / Associazione Pet
    // =========================================================

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_1
     * Test Frame: TF1
     * Obiettivo:
     *   Verificare che la funzione aggiungiVaccinazione(...) restituisca un errore
     *   di accesso non autorizzato se l’utente non è autenticato (AU2), anche se
     *   il veterinario e il pet esistono nel sistema e i dati della richiesta sono validi.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 1
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
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

        verifyNoInteractions(veterinarioRepository, petRepository, vaccinazioneRepository);
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_2
     * Test Frame: TF2
     * Obiettivo:
     *   Verificare che la funzione aggiungiVaccinazione(...) restituisca un errore
     *   di accesso non autorizzato se l’utente è autenticato ma ha un ruolo diverso
     *   da VETERINARIO (RU2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 1
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
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

        verifyNoInteractions(veterinarioRepository, petRepository, vaccinazioneRepository);
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_3
     * Test Frame: TF3
     * Obiettivo:
     *   Verificare che la funzione aggiungiVaccinazione(...) restituisca un errore
     *   "Veterinario non trovato" se l’utente è autenticato come VETERINARIO
     *   ma l’id del veterinario non esiste nel database (VT2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 1
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_3() {
        VaccinazioneRequestDTO dto = createValidRequest();

        AuthenticatedUser vetUser = createAuthenticatedVetUser();
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);

        // VT2: veterinario non esiste
        when(veterinarioRepository.findById(VET_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.aggiungiVaccinazione(PET_ID, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Veterinario non trovato");

        verify(veterinarioRepository).findById(VET_ID);
        verifyNoInteractions(petRepository, vaccinazioneRepository);
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_4
     * Test Frame: TF4
     * Obiettivo:
     *   Verificare che la funzione aggiungiVaccinazione(...) restituisca un errore
     *   "Pet non trovato" se l’utente è autenticato come VETERINARIO e il veterinario
     *   esiste (VT1), ma il pet indicato non esiste nel database (PE2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 1
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_4() {
        VaccinazioneRequestDTO dto = createValidRequest();

        AuthenticatedUser vetUser = createAuthenticatedVetUser();
        Veterinario veterinario = createVeterinarioEntity(VET_ID);

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        when(veterinarioRepository.findById(VET_ID)).thenReturn(Optional.of(veterinario));

        // PE2: pet non esiste
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
     * Test Case ID: TC_AggiungiVaccinazione_5
     * Test Frame: TF5
     * Obiettivo:
     *   Verificare che la funzione aggiungiVaccinazione(...) restituisca un errore
     *   "Il pet non è un paziente del veterinario" se l’utente è autenticato come
     *   VETERINARIO, il veterinario esiste (VT1) e il pet esiste (PE1), ma non è
     *   associato al veterinario (AP2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 1
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_5() {
        VaccinazioneRequestDTO dto = createValidRequest();

        AuthenticatedUser vetUser = createAuthenticatedVetUser();
        Veterinario veterinario = createVeterinarioEntity(VET_ID);

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        when(veterinarioRepository.findById(VET_ID)).thenReturn(Optional.of(veterinario));

        // Pet esistente ma non associato al veterinario
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
    // Test Case TC_6 – TC_9: Errori DTO su nome, via, data (Bean Validation)
    // =========================================================

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_6
     * Test Frame: TF6
     * Obiettivo:
     *   Verificare che la funzione di validazione segnali un errore quando
     *   il nome del vaccino è vuoto (LN1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = ""
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_6() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setNomeVaccino("");

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_7
     * Test Frame: TF7
     * Obiettivo:
     *   Verificare che la funzione di validazione segnali un errore quando
     *   il nome del vaccino è troppo corto (LN2: lunghezza compresa tra 1 e 2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "AB"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_7() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setNomeVaccino("AB");

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_8
     * Test Frame: TF8
     * Obiettivo:
     *   Verificare che la funzione di validazione segnali un errore quando
     *   il nome del vaccino è troppo lungo (LN3: lunghezza > 20 caratteri).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "AAAAAAAAAAAAAAAAAAAAA" (21 caratteri)
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_8() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setNomeVaccino("AAAAAAAAAAAAAAAAAAAAA"); // 21 caratteri

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_9
     * Test Frame: TF9
     * Obiettivo:
     *   Verificare che la funzione di validazione segnali un errore quando
     *   la via di somministrazione non rispetta il formato previsto (FS1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "ENDOVENA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_9() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setViaDiSomministrazione("ENDOVENA");

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    // =========================================================
    // Test Case TC_10 – TC_12: Data e richiamo (FD, ADS, ARP)
    // =========================================================

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_10
     * Test Frame: TF10
     * Obiettivo:
     *   Verificare che la funzione di validazione segnali un errore quando
     *   la data di somministrazione non è presente (FD1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = null
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = oggi + 20 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_10() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setDataDiSomministrazione(null);

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_11
     * Test Frame: TF11
     * Obiettivo:
     *   Verificare che la funzione aggiungiVaccinazione(...) restituisca un errore
     *   quando la data di somministrazione è futura (ADS1: dataDiSomministrazione > ACTUAL),
     *   pur avendo tutti gli altri dati validi.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 1
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi + 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_11() {
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

        // Regola di business attesa: data futura non ammessa
        assertThatThrownBy(() -> service.aggiungiVaccinazione(PET_ID, dto))
                .isInstanceOf(RuntimeException.class);

        verify(veterinarioRepository).findById(VET_ID);
        verify(petRepository).findById(PET_ID);
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_12
     * Test Frame: TF12
     * Obiettivo:
     *   Verificare che la funzione aggiungiVaccinazione(...) restituisca un errore
     *   quando il richiamo previsto è troppo precoce (ARP1: richiamoPrevisto <
     *   dataDiSomministrazione + 21 giorni), pur avendo tutti gli altri dati validi.
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 1
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 7 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_12() {
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

        // Regola di business attesa: richiamo troppo precoce non ammesso
        assertThatThrownBy(() -> service.aggiungiVaccinazione(PET_ID, dto))
                .isInstanceOf(RuntimeException.class);

        verify(veterinarioRepository).findById(VET_ID);
        verify(petRepository).findById(PET_ID);
    }

    // =========================================================
    // Test Case TC_13 – TC_15: Dose ed effetti collaterali (VDS, LEC)
    // =========================================================

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_13
     * Test Frame: TF13
     * Obiettivo:
     *   Verificare che la funzione di validazione segnali un errore quando
     *   la dose somministrata è pari a 0 (VDS1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 0.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_13() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setDoseSomministrata(0.0f);

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_14
     * Test Frame: TF14
     * Obiettivo:
     *   Verificare che la funzione di validazione segnali un errore quando
     *   la dose somministrata è maggiore di 10 (VDS2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 11.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_14() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setDoseSomministrata(11.0f);

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_15
     * Test Frame: TF15
     * Obiettivo:
     *   Verificare che la funzione di validazione segnali un errore quando
     *   gli effetti collaterali hanno una lunghezza superiore a 200 caratteri (LEC1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = stringa di 201 caratteri
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_15() {
        VaccinazioneRequestDTO dto = createValidRequest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            sb.append('a');
        }
        dto.setEffettiCollaterali(sb.toString());

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    // =========================================================
    // Test Case TC_16 – TC_18: Tipologia (LT)
    // =========================================================

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_16
     * Test Frame: TF16
     * Obiettivo:
     *   Verificare che la funzione di validazione segnali un errore quando
     *   la tipologia è vuota (LT1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = ""
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_16() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setTipologia("");

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_17
     * Test Frame: TF17
     * Obiettivo:
     *   Verificare che la funzione di validazione segnali un errore quando
     *   la tipologia è troppo corta (LT2: 1–2 caratteri).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "AB"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_17() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setTipologia("AB");

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_18
     * Test Frame: TF18
     * Obiettivo:
     *   Verificare che la funzione di validazione segnali un errore quando
     *   la tipologia è troppo lunga (LT3: lunghezza > 20 caratteri).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "AAAAAAAAAAAAAAAAAAAAA" (21 caratteri)
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_18() {
        VaccinazioneRequestDTO dto = createValidRequest();
        dto.setTipologia("AAAAAAAAAAAAAAAAAAAAA"); // 21 caratteri

        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    // =========================================================
    // Test Case TC_19 – TC_20: Casi corretti (PEC2 / PEC1)
    // =========================================================

    /**
     * ===========================
     * Test Case ID: TC_AggiungiVaccinazione_19
     * Test Frame: TF19
     * Obiettivo:
     *   Verificare che la funzione aggiungiVaccinazione(...) consenta la corretta
     *   creazione della vaccinazione quando tutti i dati sono validi e gli effetti
     *   collaterali sono presenti con lunghezza ammessa (PEC2, LEC2).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 1
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = "Leggero arrossamento"
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_19() {
        VaccinazioneRequestDTO dto = createValidRequest();

        // DTO valido
        Set<ConstraintViolation<VaccinazioneRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();

        AuthenticatedUser vetUser = createAuthenticatedVetUser();
        Veterinario veterinario = createVeterinarioEntity(VET_ID);
        Pet pet = createPetEntity(PET_ID, veterinario, true);

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vetUser);
        when(veterinarioRepository.findById(VET_ID)).thenReturn(Optional.of(veterinario));
        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(pet));

        when(vaccinazioneRepository.save(any(Vaccinazione.class)))
                .thenAnswer(invocation -> {
                    Vaccinazione v = invocation.getArgument(0);
                    v.setId(10L);
                    return v;
                });

        VaccinazioneResponseDTO response = service.aggiungiVaccinazione(PET_ID, dto);

        assertThat(response).isNotNull();
        assertThat(response.getVaccinazioneId()).isEqualTo(10L);
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
     * Test Case ID: TC_AggiungiVaccinazione_20
     * Test Frame: TF20
     * Obiettivo:
     *   Verificare che la funzione aggiungiVaccinazione(...) consenta la corretta
     *   creazione della vaccinazione quando tutti i dati sono validi e gli effetti
     *   collaterali non sono presenti (PEC1).
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - petId = 1
     *   - nomeVaccino = "RabiesVax"
     *   - tipologia = "VaccinoBase"
     *   - dataDiSomministrazione = oggi - 1 giorno
     *   - doseSomministrata = 1.0
     *   - viaDiSomministrazione = "SOTTOCUTANEA"
     *   - effettiCollaterali = null
     *   - richiamoPrevisto = dataDiSomministrazione + 21 giorni
     * ===========================
     */
    @Test
    void TC_AggiungiVaccinazione_20() {
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
                .thenAnswer(invocation -> {
                    Vaccinazione v = invocation.getArgument(0);
                    v.setId(11L);
                    return v;
                });

        VaccinazioneResponseDTO response = service.aggiungiVaccinazione(PET_ID, dto);

        assertThat(response).isNotNull();
        assertThat(response.getVaccinazioneId()).isEqualTo(11L);
        assertThat(response.getEffettiCollaterali()).isNull();

        Vaccinazione salvata = vaccinazioneCaptor.getValue();
        assertThat(salvata).isNotNull();
        assertThat(salvata.getEffettiCollaterali()).isNull();

        verify(veterinarioRepository).findById(VET_ID);
        verify(petRepository).findById(PET_ID);
        verify(vaccinazioneRepository).save(any(Vaccinazione.class));
    }
}
