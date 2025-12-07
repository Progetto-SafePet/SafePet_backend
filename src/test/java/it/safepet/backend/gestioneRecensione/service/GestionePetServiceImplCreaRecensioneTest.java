package it.safepet.backend.gestioneRecensione.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneRecensioni.dto.NewRecensioneDTO;
import it.safepet.backend.gestioneRecensioni.dto.RecensioneResponseDTO;
import it.safepet.backend.gestioneRecensioni.model.Recensione;
import it.safepet.backend.gestioneRecensioni.repository.RecensioneRepository;
import it.safepet.backend.gestioneRecensioni.service.GestioneRecensioniServiceImpl;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.assertj.core.api.Assertions;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GestionePetServiceImplCreaRecensioneTest {

    @Mock
    private RecensioneRepository recensioneRepository;

    @Mock
    private ProprietarioRepository proprietarioRepository;

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @InjectMocks
    private GestioneRecensioniServiceImpl service;

    private Validator validator;
    private MockedStatic<AuthContext> authContextMock;

    private final Long PROPRIETARIO_ID = 10L;
    private final Long VETERINARIO_ID = 20L;
    private final Long RECENSIONE_ID = 30L;

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

    // --- Metodi Helper ---

    private AuthenticatedUser createProprietarioAuthUser() {
        return new AuthenticatedUser(PROPRIETARIO_ID, "proprietario@test.it", Role.PROPRIETARIO);
    }

    private AuthenticatedUser createNotProprietarioAuthUser() {
        Long ALTRA_UTENZA_ID = 99L;
        return new AuthenticatedUser(ALTRA_UTENZA_ID, "altro@test.it", Role.VETERINARIO);
    }

    private Proprietario createProprietarioEntity() {
        Proprietario p = new Proprietario("Mario", "Rossi", null, "M", "proprietario@test.it", "pwd", "1234567890", "Via Roma 1");
        p.setId(PROPRIETARIO_ID);
        return p;
    }

    private Veterinario createVeterinarioEntity() {
        Veterinario v = new Veterinario("Luca", "Bianchi", null, "M", "veterinario@test.it", "pwd", "0987654321", "Cani");
        v.setId(VETERINARIO_ID);
        return v;
    }

    private Recensione createSavedRecensione(Proprietario proprietario, Veterinario veterinario, NewRecensioneDTO dto) {
        Recensione r = new Recensione();
        r.setId(RECENSIONE_ID);
        r.setPunteggio(dto.getPunteggio());
        r.setDescrizione(dto.getDescrizione());
        r.setProprietario(proprietario);
        r.setVeterinario(veterinario);
        return r;
    }

    // --- TEST CASES ---

    /**
     * ===========================
     * Test Case ID: TC_creaRecensione_1
     * Test Frame: TF1
     * Obiettivo:
     *Il Test Case ha l’obiettivo di verificare che la funzione CreaRecensione(…) restituisca un errore se l’utente non è autenticato;
     * <p>
     * Parametri di input:
     * idVeterinario = 20
     * newRecensioneDTO = {
     * punteggio: 3,
     * descrizione: "Buona"}
     * ===========================
     */
    @Test
    void TC_creaRecensione_1() {

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(null);
        NewRecensioneDTO dto = new NewRecensioneDTO(3, "Buona");


        assertThrows(RuntimeException.class, () ->
                        service.creaRecensione(VETERINARIO_ID, dto),
                "Deve lanciare un'eccezione se l'utente non è autenticato"
        );

        verify(proprietarioRepository, never()).findById(any());
        verify(veterinarioRepository, never()).findById(any());
        verify(recensioneRepository, never()).save(any());
    }

    /**
     * ===========================
     * Test Case ID: TC_creaRecensione_2
     * Test Frame: TF2
     * Obiettivo:
     * Il Test Case ha l’obiettivo di verificare che la funzione CreaRecensione(…) restituisca un errore se l’utente non è autorizzato;
     * <p>
     * Parametri di input:
     * idVeterinario = 20
     * newRecensioneDTO = {
     * punteggio: 3,
     * descrizione: "Buona"
     * }
     * ===========================
     */
    @Test
    void TC_creaRecensione_2() {

        AuthenticatedUser notProprietario = createNotProprietarioAuthUser();
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(notProprietario);
        NewRecensioneDTO dto = new NewRecensioneDTO(3, "Buona");
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                        service.creaRecensione(VETERINARIO_ID, dto),
                "Deve lanciare un'eccezione se il ruolo non è Proprietario"
        );
        Assertions.assertThat(thrown.getMessage()).isEqualTo("Accesso non autorizzato");
        verify(proprietarioRepository, never()).findById(any());
        verify(veterinarioRepository, never()).findById(any());
        verify(recensioneRepository, never()).save(any());
    }

    /**
     * ===========================
     * Test Case ID: TC_creaRecensione_3
     * Test Frame: TF3
     * Obiettivo:
     * Il Test Case ha l’obiettivo di verificare che la funzione CreaRecensione(…) restituisca un errore se il veterinario non è stato trovato;
     * <p>
     * Parametri di input:
     * idVeterinario = 20
     * newRecensioneDTO = {
     * punteggio: 3,
     * descrizione: "Buona"
     * }
     * ===========================
     */
    @Test
    void TC_creaRecensione_3() {

        AuthenticatedUser proprietarioUser = createProprietarioAuthUser();
        Proprietario proprietario = createProprietarioEntity();

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(proprietarioUser);
        when(proprietarioRepository.findById(PROPRIETARIO_ID)).thenReturn(Optional.of(proprietario));
        when(veterinarioRepository.findById(VETERINARIO_ID)).thenReturn(Optional.empty());

        NewRecensioneDTO dto = new NewRecensioneDTO(3, "Buona");

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                        service.creaRecensione(VETERINARIO_ID, dto),
                "Deve lanciare un'eccezione se il Veterinario non viene trovato"
        );
        Assertions.assertThat(thrown.getMessage()).isEqualTo("Veterinario non trovato");

        verify(proprietarioRepository, times(1)).findById(PROPRIETARIO_ID);
        verify(veterinarioRepository, times(1)).findById(VETERINARIO_ID);
        verify(recensioneRepository, never()).existsByVeterinarioAndProprietario(any(), any());
        verify(recensioneRepository, never()).save(any());
    }

    /**
     * ===========================
     * Test Case ID: TC_creaRecensione_4
     * Test Frame: TF4
     * Obiettivo:
     * Il Test Case ha l’obiettivo di verificare che la funzione CreaRecensione(…) restituisca un errore se esiste già una recensione per il veterinario selezionato;
     * <p>
     * Parametri di input :
     * idVeterinario = 20
     * newRecensioneDTO = {
     * punteggio: 3,
     * descrizione: "Buona"
     * }
     * ===========================
     */
    @Test
    void TC_creaRecensione_4() {

        AuthenticatedUser proprietarioUser = createProprietarioAuthUser();
        Proprietario proprietario = createProprietarioEntity();
        Veterinario veterinario = createVeterinarioEntity();

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(proprietarioUser);
        when(proprietarioRepository.findById(PROPRIETARIO_ID)).thenReturn(Optional.of(proprietario));
        when(veterinarioRepository.findById(VETERINARIO_ID)).thenReturn(Optional.of(veterinario));
        when(recensioneRepository.existsByVeterinarioAndProprietario(veterinario, proprietario)).thenReturn(true);

        NewRecensioneDTO dto = new NewRecensioneDTO(3, "Buona");

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () ->
                        service.creaRecensione(VETERINARIO_ID, dto),
                "Deve lanciare un'eccezione se la recensione esiste già"
        );
        Assertions.assertThat(thrown.getMessage()).isEqualTo("Hai già lasciato una recensione per questo veterinario");

        verify(proprietarioRepository, times(1)).findById(PROPRIETARIO_ID);
        verify(veterinarioRepository, times(1)).findById(VETERINARIO_ID);
        verify(recensioneRepository, times(1)).existsByVeterinarioAndProprietario(veterinario, proprietario);
        verify(recensioneRepository, never()).save(any());
    }

    /**
     * ===========================
     * Test Case ID: TC_creaRecensione_5
     * Test Frame: TF5
     * Obiettivo:
     * Il Test Case ha l’obiettivo di verificare che la funzione CreaRecensione(…) restituisca un errore se il punteggio non è valido;
     * <p>
     * Parametri di input :
     * idVeterinario = 20
     * newRecensioneDTO = {
     * punteggio: 0,
     * descrizione: "Ottimo"
     * }
     * ===========================
     */
    @Test
    void TC_creaRecensione_5() {

        NewRecensioneDTO dto = new NewRecensioneDTO(0, "Ottimo");

        Set<ConstraintViolation<NewRecensioneDTO>> violations = validator.validate(dto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations).hasSize(1);
        Assertions.assertThat(violations)
                .extracting("message")
                .contains(new String[]{"Il punteggio minimo è 1"});
    }

    /**
     * ===========================
     * Test Case ID: TC_creaRecensione_6
     * Test Frame: TF6
     * Obiettivo:
     * Il Test Case ha l’obiettivo di verificare che la funzione CreaRecensione(…) restituisca un errore se il punteggio non è valido;
     * <p>
     * Parametri di input :
     * idVeterinario = 20
     * newRecensioneDTO = {
     * punteggio: 6,
     * descrizione: "Ottimo"
     * }
     * ===========================
     */
    @Test
    void TC_creaRecensione_6() {

        NewRecensioneDTO dto = new NewRecensioneDTO(6, "Ottimo");


        Set<ConstraintViolation<NewRecensioneDTO>> violations = validator.validate(dto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations).hasSize(1);
        Assertions.assertThat(violations)
                .extracting("message")
                .contains(new String[]{"Il punteggio massimo è 5"});
    }

    /**
     * ===========================
     * Test Case ID: TC_creaRecensione_7
     * Test Frame: TF7
     * Obiettivo:
     * Il Test Case ha l’obiettivo di verificare che la funzione CreaRecensione(…) restituisca un errore se la descrizione non è valida;
     * <p>
     * Parametri di input :
     * idVeterinario = 20
     * newRecensioneDTO = {
     * punteggio: 3,
     * descrizione: ""
     * }
     * ===========================
     */
    @Test
    void TC_creaRecensione_7() {

        NewRecensioneDTO dto = new NewRecensioneDTO(3, "");

        Set<ConstraintViolation<NewRecensioneDTO>> violations = validator.validate(dto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations)
                .extracting("message")
                .contains(new String[]{"La descrizione è obbligatoria"});
    }

    /**
     * ===========================
     * Test Case ID: TC_creaRecensione_8
     * Test Frame: TF8
     * Obiettivo:
     * Il Test Case ha l’obiettivo di verificare che la funzione CreaRecensione(…) restituisca un errore se la descrizione non è valida;
     * <p>
     * Parametri di input :
     * idVeterinario = 20
     * newRecensioneDTO = {
     * punteggio: 3,
     * descrizione: "xxx...>100"
     * }
     * ===========================
     */
    @Test
    void TC_creaRecensione_8() {

        String longDescription = "A".repeat(101);
        NewRecensioneDTO dto = new NewRecensioneDTO(3, longDescription);

        Set<ConstraintViolation<NewRecensioneDTO>> violations = validator.validate(dto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations)
                .extracting("message")
                .contains(new String[]{"la dimensione deve essere compresa tra 0 e 100"});
    }

    /**
     * ===========================
     * Test Case ID: TC_creaRecensione_9
     * Test Frame: TF9
     * Obiettivo:
     * Il Test Case ha l’obiettivo di verificare che la funzione CreaRecensione(…) aggiunga correttamente la recensione al veterinario selezionato;
     * <p>
     * Parametri di input:
     * idVeterinario = 20
     * newRecensioneDTO = {
     * punteggio: 5,
     * descrizione: "Recensione perfetta per il servizio."
     * }
     * ===========================
     */
    @Test
    void TC_creaRecensione_9() throws Exception {

        NewRecensioneDTO inputDto = new NewRecensioneDTO(5, "Recensione perfetta per il servizio.");

        AuthenticatedUser proprietarioUser = createProprietarioAuthUser();
        Proprietario proprietario = createProprietarioEntity();
        Veterinario veterinario = createVeterinarioEntity();
        Recensione savedRecensione = createSavedRecensione(proprietario, veterinario, inputDto);

        authContextMock.when(AuthContext::getCurrentUser).thenReturn(proprietarioUser);
        when(proprietarioRepository.findById(PROPRIETARIO_ID)).thenReturn(Optional.of(proprietario));
        when(veterinarioRepository.findById(VETERINARIO_ID)).thenReturn(Optional.of(veterinario));
        when(recensioneRepository.existsByVeterinarioAndProprietario(veterinario, proprietario)).thenReturn(false);
        when(recensioneRepository.save(any(Recensione.class))).thenReturn(savedRecensione);

        RecensioneResponseDTO result = service.creaRecensione(VETERINARIO_ID, inputDto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getIdRecensione()).isEqualTo(RECENSIONE_ID);
        Assertions.assertThat(result.getPunteggio()).isEqualTo(5);
        Assertions.assertThat(result.getDescrizione()).isEqualTo("Recensione perfetta per il servizio.");
        Assertions.assertThat(result.getIdVeterinario()).isEqualTo(VETERINARIO_ID);
        Assertions.assertThat(result.getIdProprietario()).isEqualTo(PROPRIETARIO_ID);
        Assertions.assertThat(result.getNomeProprietario()).isEqualTo("Mario");
        Assertions.assertThat(result.getCognomeProprietario()).isEqualTo("Rossi");

        verify(proprietarioRepository, times(1)).findById(PROPRIETARIO_ID);
        verify(veterinarioRepository, times(1)).findById(VETERINARIO_ID);
        verify(recensioneRepository, times(1)).existsByVeterinarioAndProprietario(veterinario, proprietario);

        verify(recensioneRepository, times(1)).save(any(Recensione.class));
        verify(recensioneRepository).save(Mockito.argThat(rec ->
                rec.getPunteggio().equals(5) &&
                        rec.getDescrizione().equals("Recensione perfetta per il servizio.") &&
                        rec.getProprietario().equals(proprietario) &&
                        rec.getVeterinario().equals(veterinario)
        ));
    }
}