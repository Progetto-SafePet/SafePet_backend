package it.safepet.backend.gestionePet.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestionePet.dto.InserimentoNoteRequestDTO;
import it.safepet.backend.gestionePet.dto.InserimentoNoteResponseDTO;
import it.safepet.backend.gestionePet.model.NoteProprietario;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.NoteProprietarioRepository;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



/*
===============================================================
==================== CATEGORY PARTITIONING =====================
===================Aggiunta Note Proprietario===================

Autenticazione Utente:
- AU1: utente autenticato [PROPERTY AUTH]
- AU2: utente non autenticato [ERROR]

Ruolo Utente:
- RU1: Ruolo == PROPRIETARIO [IF AUTH] [PROPERTY UT_OK]
- RU2: Ruolo != PROPRIETARIO [IF AUTH] [ERROR]

id Pet Esistente nel db:
- PE1: pet id esistente [PROPERTY PE_OK]
- PE2: pet id non esistente [ERROR]

Proprietà Pet:
- PP1: il pet è di proprietà dell’utente [IF UT_OK && PE_OK]
- PP2: il pet non è di proprietà dell’utente [IF UT_OK && PE_OK] [ERROR]

Lunghezza Titolo:
- LT1: lunghezza = 0 [ERROR]
- LT2: lunghezza > 100 [ERROR]
- LT3: lunghezza >= 1 && <= 100

Lunghezza Descrizione:
- LD1: lunghezza = 0 [ERROR]
- LD2: lunghezza > 300 [ERROR]
- LD3: lunghezza >= 1 && <= 300


===============================================================
======================== TEST CASE LIST ========================
===============================================================

TC_AggiungiNota_1 → AU2(ERROR), PE1, LT3, LD3 → accesso non autorizzato
TC_AggiungiNota_2 → AU1, RU2(ERROR), PE1, LT3, LD3 → ruolo non valido
TC_AggiungiNota_3 → AU1, RU1, PE2(ERROR), LT3, LD3 → pet non trovato
TC_AggiungiNota_4 → AU1, RU1, PE1, PP2(ERROR), LT3, LD3 → pet non dell’utente
TC_AggiungiNota_5 → AU1, RU1, PE1, PP1, LT1(ERROR), LD3 → titolo vuoto
TC_AggiungiNota_6 → AU1, RU1, PE1, PP1, LT2(ERROR), LD3 → titolo troppo lungo
TC_AggiungiNota_7 → AU1, RU1, PE1, PP1, LT3, LD1(ERROR) → descrizione vuota
TC_AggiungiNota_8 → AU1, RU1, PE1, PP1, LT3, LD2(ERROR) → descrizione troppo lunga
TC_AggiungiNota_9 → AU1, RU1, PE1, PP1, LT3, LD3 → caso valido

===============================================================
*/


@ExtendWith(MockitoExtension.class)
class GestionePetServiceImplTest {

    @Mock private PetRepository petRepository;
    @Mock private ProprietarioRepository proprietarioRepository;
    @Mock private NoteProprietarioRepository noteProprietarioRepository;

    @InjectMocks
    private GestionePetServiceImpl service;

    private Validator validator;
    private MockedStatic<AuthContext> authMock;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        authMock = mockStatic(AuthContext.class);
    }

    @AfterEach
    void tearDown() {
        authMock.close();
    }

    private InserimentoNoteRequestDTO buildValidDTO() {
        InserimentoNoteRequestDTO dto = new InserimentoNoteRequestDTO();
        dto.setPetId(1L);
        dto.setTitolo("Titolo valido");
        dto.setDescrizione("Descrizione valida");
        return dto;
    }

    private AuthenticatedUser buildUser() {
        return new AuthenticatedUser(10L, "mail@example.com", Role.PROPRIETARIO);
    }

    private Proprietario buildProprietario() {
        Proprietario p = new Proprietario();
        p.setId(10L);
        p.setNome("Mario");
        p.setCognome("Rossi");
        return p;
    }

    private Pet buildPet(Proprietario owner) {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setNome("Fuffy");
        pet.setProprietario(owner);
        return pet;
    }

    /**
     * TC_AggiungiNota_1
     * Scelte: AU2 (ERROR), PE1, LT3, LD3
     * Oracolo: accesso non autorizzato
     */
    @Test
    void TC_AggiungiNota_1() {
        InserimentoNoteRequestDTO dto = buildValidDTO();
        authMock.when(AuthContext::getCurrentUser).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.creaNota(dto));
        assertEquals("Accesso non autorizzato", ex.getMessage());
    }

    /**
     * TC_AggiungiNota_2
     * Scelte: AU1, RU2(ERROR), PE1, LT3, LD3
     * Oracolo: ruolo non valido → accesso non autorizzato
     */
    @Test
    void TC_AggiungiNota_2() {
        InserimentoNoteRequestDTO dto = buildValidDTO();
        AuthenticatedUser wrongRole = new AuthenticatedUser(10L, "mail", Role.VETERINARIO);

        authMock.when(AuthContext::getCurrentUser).thenReturn(wrongRole);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.creaNota(dto));
        assertEquals("Accesso non autorizzato", ex.getMessage());
    }

    /**
     * TC_AggiungiNota_3
     * Scelte: AU1, RU1, PE2(ERROR), LT3, LD3
     * Oracolo: pet non trovato
     */
    @Test
    void TC_AggiungiNota_3() {
        InserimentoNoteRequestDTO dto = buildValidDTO();

        authMock.when(AuthContext::getCurrentUser).thenReturn(buildUser());
        when(proprietarioRepository.findById(10L)).thenReturn(Optional.of(buildProprietario()));
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.creaNota(dto));
        assertEquals("Pet non trovato", ex.getMessage());
    }

    /**
     * TC_AggiungiNota_4
     * Scelte: AU1, RU1, PE1, PP2(ERROR), LT3, LD3
     * Oracolo: pet non appartenente all’utente
     */
    @Test
    void TC_AggiungiNota_4() {
        InserimentoNoteRequestDTO dto = buildValidDTO();

        Proprietario owner = buildProprietario();
        Proprietario altro = new Proprietario();
        altro.setId(99L);

        Pet pet = buildPet(altro);

        authMock.when(AuthContext::getCurrentUser).thenReturn(buildUser());
        when(proprietarioRepository.findById(10L)).thenReturn(Optional.of(owner));
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.creaNota(dto));
        assertEquals("Il pet non appartiene all'utente corrente", ex.getMessage());
    }

    /**
     * TC_AggiungiNota_5
     * Scelte: AU1, RU1, PE1, PP1, LT1(ERROR), LD3
     * Oracolo: titolo vuoto → violazione Bean Validation
     */
    @Test
    void TC_AggiungiNota_5() {
        InserimentoNoteRequestDTO dto = buildValidDTO();
        dto.setTitolo(""); // LT1

        Set<ConstraintViolation<InserimentoNoteRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titolo")));
    }

    /**
     * TC_AggiungiNota_6
     * Scelte: AU1, RU1, PE1, PP1, LT2(ERROR), LD3
     * Oracolo: titolo troppo lungo >100 → violazione Bean Validation
     */
    @Test
    void TC_AggiungiNota_6() {
        InserimentoNoteRequestDTO dto = buildValidDTO();
        dto.setTitolo("A".repeat(150)); // LT2

        Set<ConstraintViolation<InserimentoNoteRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titolo")));
    }

    /**
     * TC_AggiungiNota_7
     * Scelte: AU1, RU1, PE1, PP1, LT3, LD1(ERROR)
     * Oracolo: descrizione vuota → violazione Bean Validation
     */
    @Test
    void TC_AggiungiNota_7() {
        InserimentoNoteRequestDTO dto = buildValidDTO();
        dto.setDescrizione(""); // LD1

        Set<ConstraintViolation<InserimentoNoteRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descrizione")));
    }

    /**
     * TC_AggiungiNota_8
     * Scelte: AU1, RU1, PE1, PP1, LT3, LD2(ERROR)
     * Oracolo: descrizione lunga >300 → violazione Bean Validation
     */
    @Test
    void TC_AggiungiNota_8() {
        InserimentoNoteRequestDTO dto = buildValidDTO();
        dto.setDescrizione("B".repeat(500)); // LD2

        Set<ConstraintViolation<InserimentoNoteRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descrizione")));
    }

    /**
     * TC_AggiungiNota_9
     * Scelte: AU1, RU1, PE1, PP1, LT3, LD3
     * Oracolo: caso valido → ritorna InserimentoNoteResponseDTO corretto
     */
    @Test
    void TC_AggiungiNota_9() {
        InserimentoNoteRequestDTO dto = buildValidDTO();

        Proprietario owner = buildProprietario();
        Pet pet = buildPet(owner);

        NoteProprietario notaSalvata = new NoteProprietario();
        notaSalvata.setId(50L);
        notaSalvata.setTitolo("Titolo valido");
        notaSalvata.setDescrizione("Descrizione valida");
        notaSalvata.setPet(pet);

        authMock.when(AuthContext::getCurrentUser).thenReturn(buildUser());
        when(proprietarioRepository.findById(10L)).thenReturn(Optional.of(owner));
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(noteProprietarioRepository.save(any())).thenReturn(notaSalvata);

        InserimentoNoteResponseDTO response = service.creaNota(dto);

        assertEquals(50L, response.getIdNota());
        assertEquals("Titolo valido", response.getTitolo());
        assertEquals("Descrizione valida", response.getDescrizione());
        assertEquals(1L, response.getIdPet());
        assertEquals(10L, response.getIdProprietario());
    }

}
