package it.safepet.backend.seeders;

import it.safepet.backend.ReportVeterinariECliniche.model.Clinica;
import it.safepet.backend.ReportVeterinariECliniche.model.OrarioDiApertura;
import it.safepet.backend.ReportVeterinariECliniche.repository.ClinicaRepository;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
@Order(1)
public class VeterinariClinicheSeeder implements CommandLineRunner {
    private final ClinicaRepository clinicaRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("true")
    private boolean resetDatabase;

    public VeterinariClinicheSeeder(ClinicaRepository clinicaRepository, PasswordEncoder passwordEncoder) {
        this.clinicaRepository = clinicaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (resetDatabase) {
            clinicaRepository.deleteAll();
            System.out.println("Eliminazione Cliniche");
        }

        if (clinicaRepository.count() == 0) {
            System.out.println("Popolamento di veterinari e cliniche");
            Clinica c1 = new Clinica("Zampa Felice", "Via Giovanni Paolo II, 150 - Fisciano", "0899911111",
                    40.7715, 14.7890);
            Veterinario v1 = new Veterinario("Simone", "Cimmino",
                    new GregorianCalendar(1985, Calendar.APRIL, 12).getTime(),
                    "M", "scimmino@gmail.com", passwordEncoder.encode("SCimmino.85"),
                    "3311111111", "Cani, gatti");
            v1.setClinica(c1);
            c1.setVeterinario(v1);
            aggiungiOrariStandard(c1);

            Clinica c2 = new Clinica("Animalia", "Via Principe Amedeo, 22 - Lancusi", "0899922222",
                    40.7430, 14.7700);
            Veterinario v2 = new Veterinario("Luca", "Salvatore",
                    new GregorianCalendar(1980, Calendar.JUNE, 8).getTime(),
                    "M", "lsalvatore@gmail.com", passwordEncoder.encode("LSalvatore.80"),
                    "3322222222", "Uccelli, rettili");
            v2.setClinica(c2);
            c2.setVeterinario(v2);
            aggiungiOrariStandard(c2);

            Clinica c3 = new Clinica("Pet Harmony", "Via Roma, 10 - Penta", "0899933333",
                    40.7495, 14.7810);
            Veterinario v3 = new Veterinario("Morgan", "Vitiello",
                    new GregorianCalendar(1992, Calendar.FEBRUARY, 20).getTime(),
                    "M", "mvitiello@gmail.com", passwordEncoder.encode("MVitiello.92"),
                    "3333333333", "Animali esotici");
            v3.setClinica(c3);
            c3.setVeterinario(v3);
            aggiungiOrariStandard(c3);

            Clinica c4 = new Clinica("Vet Guardian", "Corso Garibaldi, 88 - Baronissi", "0899944444",
                    40.7385, 14.7688);
            Veterinario v4 = new Veterinario("Vincenzo", "Nappi",
                    new GregorianCalendar(1978, Calendar.SEPTEMBER, 14).getTime(),
                    "M", "vnappi@gmail.com", passwordEncoder.encode("VNappi.78"),
                    "3344444444", "Cani, cavalli");
            v4.setClinica(c4);
            c4.setVeterinario(v4);
            aggiungiOrariStandard(c4);

            Clinica c5 = new Clinica("Oasi Vet", "Via Aldo Moro, 12 - Mercato San Severino", "0899955555",
                    40.7812, 14.7550);
            Veterinario v5 = new Veterinario("Chiara", "Memoli",
                    new GregorianCalendar(1987, Calendar.MAY, 30).getTime(),
                    "F", "cmemoli@gmail.com", passwordEncoder.encode("CMemoli.87"),
                    "3355555555", "Gatti, conigli");
            v5.setClinica(c5);
            c5.setVeterinario(v5);
            aggiungiOrariStandard(c5);

            Clinica c6 = new Clinica("Arca Vet", "Via San Giovanni, 5 - Pellezzano", "0899966666",
                    40.7300, 14.7500);
            Veterinario v6 = new Veterinario("AnnaChiara", "Memoli",
                    new GregorianCalendar(1991, Calendar.DECEMBER, 2).getTime(),
                    "F", "acmemoli@gmail.com", passwordEncoder.encode("ACMemoli.91"),
                    "3366666666", "Animali domestici");
            v6.setClinica(c6);
            c6.setVeterinario(v6);
            aggiungiOrariStandard(c6);

            Clinica c14 = new Clinica("DAmatoVet", "Via San Giovanni, 18 - Pellezzano", "0899914141",
                    40.7295, 14.7510);
            Veterinario v14 = new Veterinario("Federica", "D'Amato",
                    new GregorianCalendar(1989, Calendar.SEPTEMBER, 9).getTime(),
                    "F", "fdamato@gmail.com", passwordEncoder.encode("FDamato.89"),
                    "3341414141", "Animali esotici");
            v14.setClinica(c14);
            c14.setVeterinario(v14);
            aggiungiOrariStandard(c14);

            Clinica c15 = new Clinica("MarinoVet", "Via Roma, 55 - Baronissi", "0899915151",
                    40.7392, 14.7685);
            Veterinario v15 = new Veterinario("Davide", "Marino",
                    new GregorianCalendar(1985, Calendar.DECEMBER, 21).getTime(),
                    "M", "dmarino@gmail.com", passwordEncoder.encode("DMarino.85"),
                    "3351515151", "Cani, gatti, roditori");
            v15.setClinica(c15);
            c15.setVeterinario(v15);
            aggiungiOrariStandard(c15);

            Clinica c16 = new Clinica("FerraraVet", "Via Università, 5 - Fisciano", "0899916161",
                    40.7725, 14.7895);
            Veterinario v16 = new Veterinario("Alessia", "Ferrara",
                    new GregorianCalendar(1994, Calendar.APRIL, 27).getTime(),
                    "F", "aferrara@gmail.com", passwordEncoder.encode("AFerrara.94"),
                    "3361616161", "Cani, gatti, conigli");
            v16.setClinica(c16);
            c16.setVeterinario(v16);
            aggiungiOrariStandard(c16);

            Clinica c17 = new Clinica("GrecoVet", "Via Trento, 12 - Mercato San Severino", "0899917171",
                    40.7825, 14.7555);
            Veterinario v17 = new Veterinario("Stefano", "Greco",
                    new GregorianCalendar(1981, Calendar.JANUARY, 4).getTime(),
                    "M", "sgreco@gmail.com", passwordEncoder.encode("SGreco.81"),
                    "3371717171", "Animali da fattoria");
            v17.setClinica(c17);
            c17.setVeterinario(v17);
            aggiungiOrariStandard(c17);

            Clinica c18 = new Clinica("VitaleVet", "Via San Lorenzo, 25 - Baronissi", "0899918181",
                    40.7370, 14.7675);
            Veterinario v18 = new Veterinario("Martina", "Vitale",
                    new GregorianCalendar(1992, Calendar.MAY, 6).getTime(),
                    "F", "mvitale@gmail.com", passwordEncoder.encode("MVitale.92"),
                    "3381818181", "Cani, gatti, cavalli");
            v18.setClinica(c18);
            c18.setVeterinario(v18);
            aggiungiOrariStandard(c18);

            Clinica c19 = new Clinica("RuggieroVet", "Via Diaz, 60 - Pellezzano", "0899919191",
                    40.7285, 14.7520);
            Veterinario v19 = new Veterinario("Paolo", "Ruggiero",
                    new GregorianCalendar(1986, Calendar.JUNE, 17).getTime(),
                    "M", "pruggiero@gmail.com", passwordEncoder.encode("PRuggiero.86"),
                    "3391919191", "Uccelli, rettili");
            v19.setClinica(c19);
            c19.setVeterinario(v19);
            aggiungiOrariStandard(c19);

            Clinica c20 = new Clinica("ParisiVet", "Via San Vincenzo, 33 - Pellezzano", "0899921212",
                    40.7290, 14.7530);
            Veterinario v20 = new Veterinario("Lorenzo", "Parisi",
                    new GregorianCalendar(1987, Calendar.FEBRUARY, 14).getTime(),
                    "M", "lparisi@gmail.com", passwordEncoder.encode("LParisi.87"),
                    "3312121212", "Cani, gatti, roditori");
            v20.setClinica(c20);
            c20.setVeterinario(v20);
            aggiungiOrariStandard(c20);

            clinicaRepository.save(c1);
            clinicaRepository.save(c2);
            clinicaRepository.save(c3);
            clinicaRepository.save(c4);
            clinicaRepository.save(c5);
            clinicaRepository.save(c6);

            clinicaRepository.save(c14);
            clinicaRepository.save(c15);
            clinicaRepository.save(c16);
            clinicaRepository.save(c17);
            clinicaRepository.save(c18);
            clinicaRepository.save(c19);
            clinicaRepository.save(c20);
        }
    }

    private void aggiungiOrariStandard(Clinica clinica) {
        for (OrarioDiApertura.Giorno giorno : Arrays.asList(
                OrarioDiApertura.Giorno.LUNEDI,
                OrarioDiApertura.Giorno.MARTEDI,
                OrarioDiApertura.Giorno.MERCOLEDI,
                OrarioDiApertura.Giorno.GIOVEDI,
                OrarioDiApertura.Giorno.VENERDI)) {
            clinica.getOrariApertura().add(new OrarioDiApertura(giorno,
                    LocalTime.of(9, 0), LocalTime.of(19, 0), false, clinica));
        }
        clinica.getOrariApertura().add(new OrarioDiApertura(OrarioDiApertura.Giorno.SABATO,
                LocalTime.of(9, 0), LocalTime.of(13, 0), false, clinica));
        clinica.getOrariApertura().add(new OrarioDiApertura(OrarioDiApertura.Giorno.DOMENICA,
                LocalTime.of(0, 0), LocalTime.of(23, 59), true, clinica));
    }
}
