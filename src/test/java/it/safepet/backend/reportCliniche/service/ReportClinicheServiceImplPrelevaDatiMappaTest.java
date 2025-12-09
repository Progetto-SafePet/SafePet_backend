package it.safepet.backend.reportCliniche.service;

/*
 * Latitudine (LAT):
 *  1. Latitudine < -90 [ERROR]
 *  2. Latitudine > 90 [ERROR]
 *  3. -90 <= Latitudine <= 90 [PROP LAT-OK]
 *
 * Longitudine (LON)
 *  1. Longitudine < -180 [ERROR]
 *  2. Longitudine > 180 [ERROR]
 *  3. -180 <= Longitudine <= 180 [PROP LON-OK]
 *
 * TC_1: LAT1 LON3
 * TC_2: LAT2 LON3
 * TC_3: LAT3 LON1
 * TC_4: LAT3 LON2
 * TC_5: LAT3 LON3
 */

import it.safepet.backend.gestioneRecensioni.repository.RecensioneRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import it.safepet.backend.gestioneUtente.repository.VeterinarioRepository;
import it.safepet.backend.reportCliniche.dto.InfoClinicheDTO;
import it.safepet.backend.reportCliniche.model.Clinica;
import it.safepet.backend.reportCliniche.model.OrarioDiApertura;
import it.safepet.backend.reportCliniche.repository.ClinicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportClinicheServiceImplPrelevaDatiMappaTest {

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @Mock
    private ClinicaRepository clinicaRepository;

    @Mock
    private RecensioneRepository recensioneRepository;

    // @InjectMocks
    private ReportClinicheServiceImpl service;

    @BeforeEach
    void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);
        this.service = new ReportClinicheServiceImpl(veterinarioRepository, clinicaRepository, recensioneRepository);
    }

    private Veterinario buildVeterinario(Long id, String nome, String cognome) {
        Veterinario v = new Veterinario();
        v.setId(id);
        v.setNome(nome);
        v.setCognome(cognome);
        v.setDataNascita(LocalDate.of(1990,1,1));
        v.setEmail(nome.toLowerCase() + "@example.com");
        v.setPassword("Password123");
        v.setNumeroTelefono("0123456789");
        v.setSpecializzazioniAnimali("Cani");
        return v;
    }

    private Clinica buildClinica(Long id, String nome, Double lat, Double lon, Veterinario vet) {
        Clinica c = new Clinica();
        c.setId(id);
        c.setNome(nome);
        c.setLatitudine(lat);
        c.setLongitudine(lon);
        c.setIndirizzo(nome + " Address");
        c.setNumeroTelefono("0123456789");
        c.setVeterinario(vet);
        return c;
    }

    private OrarioDiApertura buildOrario(OrarioDiApertura.Giorno giorno, LocalTime open, LocalTime close, Boolean aperto24h, Clinica clinica) {
        OrarioDiApertura o = new OrarioDiApertura();
        o.setGiorno(giorno);
        o.setOrarioApertura(open);
        o.setOrarioChiusura(close);
        o.setAperto24h(aperto24h);
        o.setClinica(clinica);
        return o;
    }

    /**
     * <pre>
     * ===========================
     * Test Case ID: TC_PrelevaDatiMappa_1
     * Test Frame: TF1
     * Obiettivo:
     *   Verificare che una latitudine inferiore a -90 produca IllegalArgumentException
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - lat = -91.0
     *   - lon = 10.0
     * ===========================
     * </pre>
     */
    @Test
    void TC_PrelevaDatiMappa_1() {
        // input
        Double lat = -91.0;
        Double lon = 10.0;

        // esecuzione & assert
        assertThatThrownBy(() -> service.prelevaDatiMappa(lat, lon))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La latitudine ricevuta non è compresa tra -90 e 90");

        // Non devono avvenire chiamate ai repository
        verifyNoInteractions(clinicaRepository, recensioneRepository, veterinarioRepository);
    }

    /**
     * <pre>
     * ===========================
     * Test Case ID: TC_PrelevaDatiMappa_2
     * Test Frame: TF2
     * Obiettivo:
     *   Verificare che una latitudine superiore a 90 produca IllegalArgumentException
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - lat = 91.0
     *   - lon = 10.0
     * ===========================
     * </pre>
     */
    @Test
    void TC_PrelevaDatiMappa_2() {
        Double lat = 91.0;
        Double lon = 10.0;

        assertThatThrownBy(() -> service.prelevaDatiMappa(lat, lon))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La latitudine ricevuta non è compresa tra -90 e 90");

        verifyNoInteractions(clinicaRepository, recensioneRepository, veterinarioRepository);
    }

    /**
     * <pre>
     * ===========================
     * Test Case ID: TC_PrelevaDatiMappa_3
     * Test Frame: TF3
     * Obiettivo:
     *   Verificare che una longitudine inferiore a -180 produca IllegalArgumentException
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - lat = 0.0
     *   - lon = -181.0
     * 8===========================D
     * </pre>
     */
    @Test
    void TC_PrelevaDatiMappa_3() {
        Double lat = 0.0;
        Double lon = -181.0;

        assertThatThrownBy(() -> service.prelevaDatiMappa(lat, lon))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La longitudine ricevuta non è compresa tra -180 e 180");

        verifyNoInteractions(clinicaRepository, recensioneRepository, veterinarioRepository);
    }

    /**
     * <pre>
     * ===========================
     * Test Case ID: TC_PrelevaDatiMappa_4
     * Test Frame: TF4
     * Obiettivo:
     *   Verificare che una longitudine superiore a 180 produca IllegalArgumentException
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - lat = 0.0
     *   - lon = 181.0
     * ===========================
     * </pre>
     */
    @Test
    void TC_PrelevaDatiMappa_4() {
        Double lat = 0.0;
        Double lon = 181.0;

        assertThatThrownBy(() -> service.prelevaDatiMappa(lat, lon))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La longitudine ricevuta non è compresa tra -180 e 180");

        verifyNoInteractions(clinicaRepository, recensioneRepository, veterinarioRepository);
    }

    /**
     * <pre>
     * ===========================
     * Test Case ID: TC_PrelevaDatiMappa_5
     * Test Frame: TF5
     * Obiettivo:
     *   Verificare il comportamento valido: data lat/lon corrette, il servizio restituisce
     *   le cliniche più vicine (ordinate per distanza) mappate in InfoClinicheDTO
     *
     * Parametri di input (solo i parametri effettivi della richiesta):
     *   - lat = 45.0
     *   - lon = 7.0
     * ===========================
     * </pre>
     */
    @Test
    void TC_PrelevaDatiMappa_5() {
        // dati di input (coordinate di test)
        Double lat = 45.0;
        Double lon = 7.0;

        Veterinario v1 = buildVeterinario(1L, "Mario", "Rossi");
        Veterinario v2 = buildVeterinario(2L, "Luigi", "Bianchi");
        Veterinario v3 = buildVeterinario(3L, "Anna", "Verdi");

        Clinica c1 = buildClinica(10L, "ClinicaRoma", 45.0005, 7.0005, v1); // molto vicina
        Clinica c2 = buildClinica(11L, "ClinicaMilano", 45.1, 7.1, v2);     // più lontana
        Clinica c3 = buildClinica(12L, "ClinicaTorino", 46.0, 8.0, v3);     // ancora più lontana

        OrarioDiApertura o1 = buildOrario(currentGiorno(), null, null,true, c1);
        OrarioDiApertura o2 = buildOrario(currentGiorno(),  null, null, true, c2);
        OrarioDiApertura o3 = buildOrario(currentGiorno(), null, null, true, c3);

        c1.getOrariApertura().add(o1);
        c2.getOrariApertura().add(o2);
        c3.getOrariApertura().add(o3);

        v1.setClinica(c1);
        v2.setClinica(c2);
        v3.setClinica(c3);

        List<Clinica> all = new ArrayList<>();
        all.add(c3);
        all.add(c1);
        all.add(c2);

        when(clinicaRepository.findAll()).thenReturn(all);

        when(recensioneRepository.countByVeterinarioId(1L)).thenReturn(2);
        when(recensioneRepository.countByVeterinarioId(2L)).thenReturn(1);
        when(recensioneRepository.countByVeterinarioId(3L)).thenReturn(0);

        when(veterinarioRepository.calcolaMediaRecensioniVeterinario(1L)).thenReturn(4.5);
        when(veterinarioRepository.calcolaMediaRecensioniVeterinario(2L)).thenReturn(3.0);
        when(veterinarioRepository.calcolaMediaRecensioniVeterinario(3L)).thenReturn(0.0);

        List<InfoClinicheDTO> result = service.prelevaDatiMappa(lat, lon);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);

        assertThat(result)
                .extracting(InfoClinicheDTO::clinicaId)
                .containsExactly(c1.getId(), c2.getId(), c3.getId());

        InfoClinicheDTO first = result.getFirst();
        assertThat(first.nomeClinica()).isEqualTo(c1.getNome());
        assertThat(first.vetId()).isEqualTo(v1.getId());
        assertThat(first.nomeVeterinario()).isEqualTo(v1.getNome());
        assertThat(first.cognomeVeterinario()).isEqualTo(v1.getCognome());
        assertThat(first.numRecensioni()).isEqualTo(2);
        assertThat(first.mediaRecensioni()).isEqualTo(4.5);

        verify(clinicaRepository).findAll() ;
        verify(recensioneRepository, times(3)).countByVeterinarioId(anyLong());
        verify(veterinarioRepository, times(3)).calcolaMediaRecensioniVeterinario(anyLong());
    }

    private OrarioDiApertura.Giorno currentGiorno() {
        DayOfWeek dow = ZonedDateTime.now(Clock.systemDefaultZone()).getDayOfWeek();
        return switch (dow) {
            case MONDAY -> OrarioDiApertura.Giorno.LUNEDI;
            case TUESDAY -> OrarioDiApertura.Giorno.MARTEDI;
            case WEDNESDAY -> OrarioDiApertura.Giorno.MERCOLEDI;
            case THURSDAY -> OrarioDiApertura.Giorno.GIOVEDI;
            case FRIDAY -> OrarioDiApertura.Giorno.VENERDI;
            case SATURDAY -> OrarioDiApertura.Giorno.SABATO;
            case SUNDAY -> OrarioDiApertura.Giorno.DOMENICA;
        };
    }
}
