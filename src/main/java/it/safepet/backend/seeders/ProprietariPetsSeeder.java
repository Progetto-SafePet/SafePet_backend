package it.safepet.backend.seeders;

import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestionePet.repository.PetRepository;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Calendar;

@Component
@Order(2)
public class ProprietariPetsSeeder implements CommandLineRunner {
    private final ProprietarioRepository proprietarioRepository;
    private final PetRepository petRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("true")
    private boolean resetDatabase;

    public ProprietariPetsSeeder(ProprietarioRepository proprietarioRepository, PetRepository petRepository,
                                 PasswordEncoder passwordEncoder) {
        this.proprietarioRepository = proprietarioRepository;
        this.petRepository = petRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (resetDatabase) {
            proprietarioRepository.deleteAll();
            System.out.println("Eliminazione Proprietari");
        }

        if (proprietarioRepository.count() == 0) {
            System.out.println("Popolamento di proprietari e pets");
            Proprietario p1 = new Proprietario();
            p1.setNome("Gianni");
            p1.setCognome("Angel");
            p1.setGenere("M");
            p1.setDataNascita(LocalDate.of(1970, Calendar.DECEMBER, 25));
            p1.setIndirizzoDomicilio("Via Roma 16");
            p1.setNumeroTelefono("3333333333");
            p1.setEmail("gianniangel99@gmail.com");
            p1.setPassword(passwordEncoder.encode("Password123"));
            proprietarioRepository.save(p1);

            Pet pet1 = new Pet();
            pet1.setNome("Rocky");
            pet1.setSesso("M");
            pet1.setSpecie("Cane");
            pet1.setRazza("Golden Retriever");
            pet1.setDataNascita(LocalDate.of(2020, Calendar.APRIL, 5));
            pet1.setPeso(28.5);
            pet1.setColoreMantello("Marrone chiaro");
            pet1.setMicrochip("985112003457890");
            pet1.setSterilizzato(true);
            pet1.setFoto(loadImage("images/dog1.jpg"));
            pet1.setProprietario(p1);
            p1.getPets().add(pet1);
            petRepository.save(pet1);
        }
    }

    private byte[] loadImage(String path) {
        try {
            ClassPathResource imgFile = new ClassPathResource(path);
            return Files.readAllBytes(imgFile.getFile().toPath());
        } catch (IOException e) {
            System.err.println("⚠️  Impossibile caricare immagine: " + path + " → " + e.getMessage());
            return null;
        }
    }
}
