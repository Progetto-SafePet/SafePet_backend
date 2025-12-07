package it.safepet.backend.gestionePet.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestionePet.dto.NewPetDTO;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.model.Pet;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class GestionePetServiceImplCreaPetTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private ProprietarioRepository proprietarioRepository;

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
    // Helper
    // ============================================================

    private NewPetDTO buildRequest(
            String nome,
            String sesso,
            String specie,
            String razza,
            LocalDate dataNascita,
            Double peso,
            String coloreMantello,
            String microchip,
            Boolean isSterilizzato,
            MultipartFile foto
    ) {
        return new NewPetDTO(
                nome,
                sesso,
                specie,
                razza,
                dataNascita,
                peso,
                coloreMantello,
                microchip,
                isSterilizzato,
                foto
        );
    }

    // Usata nei test che chiamano il service (business)
    private NewPetDTO buildValidRequestForService() {
        return buildRequest(
                "Fido",
                "M",
                "cane",
                "Labrador",
                LocalDate.now().minusYears(1),
                10.0,
                "Marrone",
                "123456789012345",
                true,
                mockValidImageFile()
        );
    }

    // Usata nei test di solo DTO (Bean Validation)
    private NewPetDTO buildValidRequestForValidation() {
        return buildRequest(
                "Fido",
                "M",
                "cane",
                "Labrador",
                LocalDate.now().minusYears(1),
                10.0,
                "Marrone",
                "123456789012345",
                true,
                null
        );
    }

    private MultipartFile mockValidImageFile() {
        MultipartFile foto = mock(MultipartFile.class);
        try {
            lenient().when(foto.isEmpty()).thenReturn(false);
            lenient().when(foto.getContentType()).thenReturn("image/png");
            lenient().when(foto.getBytes()).thenReturn(new byte[]{1, 2, 3});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return foto;
    }

    private MultipartFile mockInvalidImageFile() {
        MultipartFile foto = mock(MultipartFile.class);
        try {
            lenient().when(foto.isEmpty()).thenReturn(false);
            lenient().when(foto.getContentType()).thenReturn("image/gif");
            lenient().when(foto.getBytes()).thenReturn(new byte[]{1, 2, 3});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return foto;
    }

    private Proprietario buildProprietario(Long id) {
        Proprietario proprietario = new Proprietario();
        proprietario.setId(id);
        proprietario.setNome("Mario");
        proprietario.setCognome("Rossi");
        proprietario.setEmail("mario.rossi@example.com");
        return proprietario;
    }

    // ==========================================================
    // TC_CreaPet_1: AU2 [ERROR] - utente non autenticato
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_1<br>
     * <b>Test Frame:</b> TF1
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che, in assenza di utente autenticato, la creazione del pet
     * fallisca generando un errore di accesso non autorizzato.</p>
     *
     * <p><b>Parametri di input (solo quelli rilevanti per la richiesta):</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_1() {
        NewPetDTO dto = buildValidRequestForService();
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.creaPet(dto));
        assertThat(ex.getMessage()).isEqualTo("Accesso non autorizzato");

        verifyNoInteractions(proprietarioRepository, petRepository);
    }

    // ==========================================================
    // TC_CreaPet_2: RU2 [ERROR] - ruolo diverso da PROPRIETARIO
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID: TC_CreaPet_2</b>
     * <b>Test Frame: TF2</b>
     * <p><b></b></p>Obiettivo:</b><br>
     *   Verificare che, con utente autenticato ma ruolo diverso da PROPRIETARIO,
     *   la creazione del pet fallisca con errore di accesso non autorizzato.</p>
     *
     * <p><b></b></p>Parametri di input (solo i parametri effettivi della richiesta): </b></p>
     * <lu>
     *   <li><code>newPetDTO.nome</code>  = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code>  = "M"</li>
     *   <li><code>newPetDTO.specie</code>  = "cane"</li>
     *   <li><code>newPetDTO.razza</code>  = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code>  = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code>  = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code>  = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code>  = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code>  = true</li>
     *   <li><code>newPetDTO.foto</code>  = file PNG valido</li>
     * </lu>
     * ===========================
     */
    @Test
    void TC_CreaPet_2() {
        NewPetDTO dto = buildValidRequestForService();
        AuthenticatedUser vet = new AuthenticatedUser(10L, "vet@test.com", Role.VETERINARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(vet);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.creaPet(dto));
        assertThat(ex.getMessage()).isEqualTo("Accesso non autorizzato");

        verifyNoInteractions(proprietarioRepository, petRepository);
    }

    // ==========================================================
    // TC_CreaPet_3: LN1 [ERROR] - nome vuoto
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_3</br>
     * <b>Test Frame: </b> TF3
     * <p><b>Obiettivo:</b><br>
     *   Verificare che un nome vuoto generi una violazione di Bean Validation
     *   sul campo 'nome'.</p>
     *
     * <p><b>Parametri di input (solo i parametri effettivi della richiesta):</b></p>
     * <lu>
     *   <li><code>newPetDTO.nome</code> = ""</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code>  = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code>  = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code>  = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code>  = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code>  = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code>  = true</li>
     *   <li><code>newPetDTO.foto</code>  = file PNG valido</li>
     * </lu>
     * ===========================
     */
    @Test
    void TC_CreaPet_3() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setNome("");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("nome"));
    }

    // ==========================================================
    // TC_CreaPet_4: LN2 [ERROR] - nome troppo corto
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:<b> TC_CreaPet_4<br>
     * <b>Test Frame:</b> TF4
     * <p><b>Obiettivo:</b><br>
     *   Verificare che un nome con lunghezza inferiore a 3 caratteri
     *   generi violazione sul campo 'nome'.</p>
     *
     * <p><b>Parametri di input:</p></b>
     *<lu>
     *   <li><code>newPetDTO.nome</code> = "Al"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code>= "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </lu>
     * ===========================
     */
    @Test
    void TC_CreaPet_4() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setNome("Al");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("nome"));
    }

    // ==========================================================
    // TC_CreaPet_5: LN3 [ERROR] - nome troppo lungo
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_5<br>
     * <b>Test Frame:</b> TF5
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un nome con lunghezza superiore a 20 caratteri
     * produca una violazione del vincolo di validazione sul campo <code>nome</code>.
     * </p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "NomeMoltoMoltoLungoPet"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_5() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setNome("NomeMoltoMoltoLungoPet");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("nome"));
    }

    // ==========================================================
    // TC_CreaPet_6: FS1 [ERROR] - formato sesso non valido
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_6<br>
     * <b>Test Frame:</b> TF6
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un valore del campo <code>sesso</code> diverso da 'M' o 'F'
     * generi una violazione di validazione.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "X"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_6() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setSesso("X");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("sesso"));
    }

    // ==========================================================
    // TC_CreaPet_7: FSP1 [ERROR] - formato specie non valido
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_7<br>
     * <b>Test Frame:</b> TF7
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un valore del campo <code>specie</code> diverso da
     * 'cane', 'gatto' o 'altro' generi una violazione di validazione.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "uccello"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_7() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setSpecie("uccello");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("specie"));
    }

    // ==========================================================
    // TC_CreaPet_8: LR1 [ERROR] - razza troppo corta
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_8<br>
     * <b>Test Frame:</b> TF8
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un valore del campo <code>razza</code> con lunghezza
     * inferiore a 3 caratteri generi una violazione di validazione.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Al"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_8() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setRazza("Al");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("razza"));
    }

    // ==========================================================
    // TC_CreaPet_9: LR2 [ERROR] - razza troppo lunga
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_9<br>
     * <b>Test Frame:</b> TF9
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un valore del campo <code>razza</code> con lunghezza
     * superiore a 30 caratteri generi una violazione di validazione.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "RazzaDavveroMoltoMoltoLungaPerTest"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_9() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setRazza("RazzaDavveroMoltoMoltoLungaPerTest");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("razza"));
    }

    // ==========================================================
    // TC_CreaPet_10: FD1 [ERROR] - data mancante
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_10<br>
     * <b>Test Frame:</b> TF10
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che l'assenza del valore nel campo <code>dataNascita</code>
     * generi una violazione di validazione.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = null</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_10() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setDataNascita(null);

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("dataNascita"));
    }

    // ==========================================================
    // TC_CreaPet_11: AD1 [ERROR] - data nel futuro (errore di business)
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_11<br>
     * <b>Test Frame:</b> TF11
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che l'inserimento di una data di nascita futura produca
     * un errore di business nel service, impedendo la creazione del pet.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi + 1 giorno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_11() {
        NewPetDTO dto = buildValidRequestForService();
        dto.setDataNascita(LocalDate.now().plusDays(1));

        AuthenticatedUser user = new AuthenticatedUser(1L, "owner@test.com", Role.PROPRIETARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        Proprietario proprietario = buildProprietario(1L);
        when(proprietarioRepository.findById(1L)).thenReturn(Optional.of(proprietario));
        when(petRepository.findByMicrochip(dto.getMicrochip())).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.creaPet(dto));
        assertThat(ex.getMessage()).isEqualTo("La data di nascita non può essere nel futuro");

        verify(petRepository, never()).save(any(Pet.class));
    }

    // ==========================================================
    // TC_CreaPet_12: VP1 [ERROR] - peso troppo piccolo
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_12<br>
     * <b>Test Frame:</b> TF12
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un valore di <code>peso</code> pari a 0 generi
     * una violazione delle constraint del campo.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 0.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_12() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setPeso(0.0);

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("peso"));
    }

    // ==========================================================
    // TC_CreaPet_13: VP2 [ERROR] - peso troppo grande
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_13<br>
     * <b>Test Frame:</b> TF13
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un valore di <code>peso</code> superiore a 100
     * generi una violazione delle constraint del campo.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 150.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_13() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setPeso(150.0);

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("peso"));
    }

    // ==========================================================
    // TC_CreaPet_14: LCM1 [ERROR] - colore mantello troppo corto
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_14<br>
     * <b>Test Frame:</b> TF14
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un valore del campo <code>coloreMantello</code>
     * con lunghezza inferiore a 3 caratteri generi una violazione di validazione.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "ro"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_14() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setColoreMantello("ro");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("coloreMantello"));
    }

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_15<br>
     * <b>Test Frame:</b> TF15
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un valore del campo <code>coloreMantello</code>
     * con lunghezza superiore a 15 caratteri generi una violazione di validazione.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "ColoreMantelloMoltoLungo"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_15() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setColoreMantello("ColoreMantelloMoltoLungo");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("coloreMantello"));
    }

    // ==========================================================
    // TC_CreaPet_16: EM1 [ERROR] - microchip esistente
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_16<br>
     * <b>Test Frame:</b> TF16
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che, se il microchip esiste già a DB,
     * la creazione del pet fallisca con eccezione "Microchip già esistente".</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345" (già presente a DB)</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_16() throws IOException {
        NewPetDTO dto = buildValidRequestForService();

        AuthenticatedUser user = new AuthenticatedUser(1L, "owner@test.com", Role.PROPRIETARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        Proprietario proprietario = buildProprietario(1L);
        when(proprietarioRepository.findById(1L)).thenReturn(Optional.of(proprietario));
        when(petRepository.findByMicrochip(dto.getMicrochip()))
                .thenReturn(Optional.of(new Pet()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.creaPet(dto));
        assertThat(ex.getMessage()).isEqualTo("Microchip già esistente");

        verify(petRepository, never()).save(any(Pet.class));
    }

    // ==========================================================
    // TC_CreaPet_17: LM1 [ERROR] - microchip troppo corto
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_17<br>
     * <b>Test Frame:</b> TF17
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un microchip con lunghezza inferiore a 15 cifre
     * generi violazione di Bean Validation.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "12345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_17() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setMicrochip("12345");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    // ==========================================================
    // TC_CreaPet_18: LM2 [ERROR] - microchip troppo lungo
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_18<br>
     * <b>Test Frame:</b> TF18
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un microchip con lunghezza superiore a 15 cifre
     * generi violazione di Bean Validation.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "1234567890123456"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_18() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setMicrochip("1234567890123456");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    // ==========================================================
    // TC_CreaPet_19: FM1 [ERROR] - formato microchip non valido
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_19<br>
     * <b>Test Frame:</b> TF19
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che un microchip di lunghezza corretta ma con caratteri
     * non numerici generi violazione di Bean Validation.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "12345678901234A"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_19() {
        NewPetDTO dto = buildValidRequestForValidation();
        dto.setMicrochip("12345678901234A");

        Set<ConstraintViolation<NewPetDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    // ==========================================================
    // TC_CreaPet_20: FF1 [ERROR] - formato foto non valido
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_20<br>
     * <b>Test Frame:</b> TF20
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare che una foto con content-type non PNG/JPG
     * causi eccezione di business nel service.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto.contentType</code> = "image/gif"</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_20() throws IOException {
        MultipartFile invalidFoto = mockInvalidImageFile();

        NewPetDTO dto = buildRequest(
                "Fido",
                "M",
                "cane",
                "Labrador",
                LocalDate.now().minusYears(1),
                10.0,
                "Marrone",
                "123456789012345",
                true,
                invalidFoto
        );

        AuthenticatedUser user = new AuthenticatedUser(1L, "owner@test.com", Role.PROPRIETARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        Proprietario proprietario = buildProprietario(1L);
        when(proprietarioRepository.findById(1L)).thenReturn(Optional.of(proprietario));
        when(petRepository.findByMicrochip(dto.getMicrochip())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.creaPet(dto));
        assertThat(ex.getMessage())
                .isEqualTo("Formato immagine non valido: sono ammessi JPG o PNG");

        verify(petRepository, never()).save(any(Pet.class));
    }

    // ==========================================================
    // TC_CreaPet_21: caso valido, isSterilizzato = true
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_21<br>
     * <b>Test Frame:</b> TF21
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare la corretta creazione del pet con tutti i dati validi
     * e <code>isSterilizzato</code> = true.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = true</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_21() throws IOException {
        NewPetDTO dto = buildValidRequestForService();
        dto.setSterilizzato(true);

        AuthenticatedUser user = new AuthenticatedUser(1L, "owner@test.com", Role.PROPRIETARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        Proprietario proprietario = buildProprietario(1L);
        when(proprietarioRepository.findById(1L)).thenReturn(Optional.of(proprietario));
        when(petRepository.findByMicrochip(dto.getMicrochip())).thenReturn(Optional.empty());

        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> {
            Pet p = invocation.getArgument(0);
            p.setId(10L);
            return p;
        });

        PetResponseDTO response = service.creaPet(dto);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getNome()).isEqualTo(dto.getNome());
        assertThat(response.getSpecie()).isEqualTo(dto.getSpecie());
        assertThat(response.getDataNascita()).isEqualTo(dto.getDataNascita());
        assertThat(response.getPeso()).isEqualTo(dto.getPeso());
        assertThat(response.getColoreMantello()).isEqualTo(dto.getColoreMantello());
        assertThat(response.getSterilizzato()).isTrue();
        assertThat(response.getRazza()).isEqualTo(dto.getRazza());
        assertThat(response.getMicrochip()).isEqualTo(dto.getMicrochip());
        assertThat(response.getSesso()).isEqualTo(dto.getSesso());
        assertThat(response.getFotoBase64()).isNotBlank();

        verify(petRepository).save(any(Pet.class));
    }

    // ==========================================================
    // TC_CreaPet_22: caso valido, isSterilizzato = false
    // ==========================================================

    /**
     * ===========================
     * <br>
     * <b>Test Case ID:</b> TC_CreaPet_22<br>
     * <b>Test Frame:</b> TF22
     *
     * <p><b>Obiettivo:</b><br>
     * Verificare la corretta creazione del pet con tutti i dati validi
     * e <code>isSterilizzato</code> = false.</p>
     *
     * <p><b>Parametri di input:</b></p>
     * <ul>
     *   <li><code>newPetDTO.nome</code> = "Fido"</li>
     *   <li><code>newPetDTO.sesso</code> = "M"</li>
     *   <li><code>newPetDTO.specie</code> = "cane"</li>
     *   <li><code>newPetDTO.razza</code> = "Labrador"</li>
     *   <li><code>newPetDTO.dataNascita</code> = oggi - 1 anno</li>
     *   <li><code>newPetDTO.peso</code> = 10.0</li>
     *   <li><code>newPetDTO.coloreMantello</code> = "Marrone"</li>
     *   <li><code>newPetDTO.microchip</code> = "123456789012345"</li>
     *   <li><code>newPetDTO.isSterilizzato</code> = false</li>
     *   <li><code>newPetDTO.foto</code> = file PNG valido</li>
     * </ul>
     * ===========================
     */
    @Test
    void TC_CreaPet_22() throws IOException {
        NewPetDTO dto = buildValidRequestForService();
        dto.setSterilizzato(false);

        AuthenticatedUser user = new AuthenticatedUser(1L, "owner@test.com", Role.PROPRIETARIO);
        authContextMock.when(AuthContext::getCurrentUser).thenReturn(user);

        Proprietario proprietario = buildProprietario(1L);
        when(proprietarioRepository.findById(1L)).thenReturn(Optional.of(proprietario));
        when(petRepository.findByMicrochip(dto.getMicrochip())).thenReturn(Optional.empty());

        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> {
            Pet p = invocation.getArgument(0);
            p.setId(11L);
            return p;
        });

        PetResponseDTO response = service.creaPet(dto);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(11L);
        assertThat(response.getSterilizzato()).isFalse();

        verify(petRepository).save(any(Pet.class));
    }
}
