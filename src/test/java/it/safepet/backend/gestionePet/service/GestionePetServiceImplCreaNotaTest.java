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
class GestionePetServiceImplCreaNotaTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private ProprietarioRepository proprietarioRepository;

    @Mock
    private NoteProprietarioRepository noteProprietarioRepository;

    @InjectMocks
    private GestionePetServiceImpl service;

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
    // Helper objects
    // ============================================================

    private InserimentoNoteRequestDTO buildRequest(String titolo, String descrizione, Long petId) {
        InserimentoNoteRequestDTO dto = new InserimentoNoteRequestDTO();
        dto.setTitolo(titolo);
        dto.setDescrizione(descrizione);
        dto.setPetId(petId);
        return dto;
    }

    private AuthenticatedUser buildOwnerUser(Long id) {
        return new AuthenticatedUser(id, "mail@test.com", Role.PROPRIETARIO);
    }

    // ============================================================
    // TEST CASES
    // ============================================================



    /**
     * TC_12_1
     * Categorie:
     *  - AU2: Utente non autenticato [ERROR]
     *  - PE1, LT3, LD3 → irrilevanti
     *
     * Oracolo:
     *  Deve lanciare RuntimeException "Accesso non autorizzato".
     */
    @Test
    void TC_12_1() {
        // Utente NON autenticato
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(null);

        InserimentoNoteRequestDTO dto = buildRequest("Titolo valido", "Descrizione valida", 1L);

        assertThrows(RuntimeException.class, () -> service.creaNota(dto));
        verifyNoInteractions(petRepository, proprietarioRepository, noteProprietarioRepository);
    }



    /**
     * TC_12_2
     * Categorie:
     *  - AU1: autenticato
     *  - RU2: ruolo ≠ PROPRIETARIO [ERROR]
     *
     * Oracolo:
     *  Deve lanciare RuntimeException "Accesso non autorizzato".
     */
    @Test
    void TC_12_2() {
        AuthenticatedUser vet = new AuthenticatedUser(10L, "vet@test.com", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vet);

        InserimentoNoteRequestDTO dto = buildRequest("Titolo valido", "Descrizione valida", 1L);

        assertThrows(RuntimeException.class, () -> service.creaNota(dto));
        verifyNoInteractions(petRepository, proprietarioRepository, noteProprietarioRepository);
    }



    /**
     * TC_12_3
     * Categorie:
     *  - AU1, RU1
     *  - PE2: Pet inesistente [ERROR]
     *
     * Oracolo:
     *  Deve lanciare RuntimeException "Pet non trovato".
     */
    @Test
    void TC_12_3() {
        AuthenticatedUser user = buildOwnerUser(1L);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        when(proprietarioRepository.findById(1L)).thenReturn(Optional.of(new Proprietario()));
        when(petRepository.findById(10L)).thenReturn(Optional.empty());

        InserimentoNoteRequestDTO dto = buildRequest("Titolo", "Descrizione", 10L);

        assertThrows(RuntimeException.class, () -> service.creaNota(dto));
        verify(petRepository, times(1)).findById(10L);
        verifyNoMoreInteractions(noteProprietarioRepository);
    }



    /**
     * TC_12_4
     * Categorie:
     *  - AU1, RU1, PE1
     *  - PP2: il pet NON appartiene al proprietario [ERROR]
     *
     * Oracolo:
     *  Deve lanciare RuntimeException "Il pet non appartiene all'utente corrente".
     */
    @Test
    void TC_12_4() {
        AuthenticatedUser user = buildOwnerUser(1L);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        Proprietario owner = new Proprietario();
        owner.setId(1L);
        when(proprietarioRepository.findById(1L)).thenReturn(Optional.of(owner));

        Proprietario altro = new Proprietario();
        altro.setId(2L);

        Pet pet = new Pet();
        pet.setId(5L);
        pet.setProprietario(altro);

        when(petRepository.findById(5L)).thenReturn(Optional.of(pet));

        InserimentoNoteRequestDTO dto = buildRequest("Titolo", "Descrizione", 5L);

        assertThrows(RuntimeException.class, () -> service.creaNota(dto));
    }



    /**
     * TC_12_5
     * Categorie:
     *  - AU1, RU1, PE1, PP1
     *  - LT1: Titolo vuoto [ERROR]
     *
     * Oracolo:
     *  Violazione Bean Validation su @NotBlank titolo.
     */
    @Test
    void TC_12_5() {
        InserimentoNoteRequestDTO dto = buildRequest("", "Descrizione valida", 1L);

        Set<ConstraintViolation<InserimentoNoteRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }



    /**
     * TC_12_6
     * Categorie:
     *  - AU1, RU1, PE1, PP1
     *  - LT2: titolo > 100 [ERROR]
     *
     * Oracolo:
     *  Violazione Bean Validation su @Size(max=100).
     */
    @Test
    void TC_12_6() {
        String longTitle = "a".repeat(101);
        InserimentoNoteRequestDTO dto = buildRequest(longTitle, "Descrizione valida", 1L);

        Set<ConstraintViolation<InserimentoNoteRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }



    /**
     * TC_12_7
     * Categorie:
     *  - AU1, RU1, PE1, PP1
     *  - LD1: descrizione vuota [ERROR]
     *
     * Oracolo:
     *  Violazione Bean Validation su descrizione.
     */
    @Test
    void TC_12_7() {
        InserimentoNoteRequestDTO dto = buildRequest("Titolo valido", "", 1L);

        Set<ConstraintViolation<InserimentoNoteRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }



    /**
     * TC_12_8
     * Categorie:
     *  - AU1, RU1, PE1, PP1
     *  - LD2: descrizione > 300 [ERROR]
     *
     * Oracolo:
     *  Violazione Bean Validation su descrizione.
     */
    @Test
    void TC_12_8() {
        String longDesc = "a".repeat(301);
        InserimentoNoteRequestDTO dto = buildRequest("Titolo valido", longDesc, 1L);

        Set<ConstraintViolation<InserimentoNoteRequestDTO>> errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }



    /**
     * TC_12_9
     * Categorie: tutte OK
     *  - AU1, RU1, PE1, PP1
     *  - LT3, LD3
     *
     * Oracolo:
     *  - Deve salvare correttamente la nota
     *  - Deve restituire InserimentoNoteResponseDTO coerente
     *  - Devono essere chiamati i repository corretti
     */
    @Test
    void TC_12_9() {
        AuthenticatedUser user = buildOwnerUser(1L);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        Proprietario p = new Proprietario();
        p.setId(1L);
        p.setNome("Mario");
        p.setCognome("Rossi");

        Pet pet = new Pet();
        pet.setId(10L);
        pet.setNome("Fido");
        pet.setProprietario(p);

        when(proprietarioRepository.findById(1L)).thenReturn(Optional.of(p));
        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));

        NoteProprietario saved = new NoteProprietario();
        saved.setId(99L);
        saved.setTitolo("Titolo");
        saved.setDescrizione("Descrizione");
        saved.setPet(pet);

        when(noteProprietarioRepository.save(any(NoteProprietario.class))).thenReturn(saved);

        InserimentoNoteRequestDTO dto =
                buildRequest("Titolo", "Descrizione", 10L);

        InserimentoNoteResponseDTO response = service.creaNota(dto);

        assertThat(response.getIdNota()).isEqualTo(99L);
        assertThat(response.getIdPet()).isEqualTo(10L);
        assertThat(response.getNomePet()).isEqualTo("Fido");
        assertThat(response.getIdProprietario()).isEqualTo(1L);
        assertThat(response.getNomeCompletoProprietario()).isEqualTo("Mario Rossi");

        verify(noteProprietarioRepository, times(1)).save(any());
    }
}

