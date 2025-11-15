package it.safepet.backend;

import it.safepet.backend.persistence.entity.Pet;
import it.safepet.backend.persistence.entity.Proprietario;
import it.safepet.backend.persistence.repository.PetRepository;
import it.safepet.backend.persistence.repository.ProprietarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final ProprietarioRepository proprietarioRepository;
    private final PetRepository petRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("true")
    private boolean resetDatabase;

    public DatabaseSeeder(ProprietarioRepository proprietarioRepository, PetRepository petRepository, PasswordEncoder passwordEncoder) {
        this.proprietarioRepository = proprietarioRepository;
        this.petRepository = petRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (resetDatabase) {
            proprietarioRepository.deleteAll();
            petRepository.deleteAll();
            System.out.println("Eliminazione del database");
        }

        if (proprietarioRepository.count() == 0) {
            System.out.println("Popolamento iniziale del database");
            Proprietario p1 = new Proprietario();
            p1.setNome("Gianni");
            p1.setCognome("Angel");
            p1.setGenere("M");
            p1.setDataNascita(new GregorianCalendar(1970, Calendar.DECEMBER, 25).getTime());
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
            pet1.setDataNascita(new GregorianCalendar(2020, Calendar.APRIL, 5).getTime());
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
