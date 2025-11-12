package it.safepet.backend;

import it.safepet.backend.persistence.entity.Pet;
import it.safepet.backend.persistence.entity.User;
import it.safepet.backend.persistence.repository.PetRepository;
import it.safepet.backend.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.db.reset:false}")
    private boolean resetDatabase;

    public DatabaseSeeder(UserRepository userRepository, PetRepository petRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void run(String... args) {
        if (resetDatabase) {
            System.out.println("⚠️  app.db.reset=true → Pulizia del database in corso...");
            petRepository.deleteAll();
            userRepository.deleteAll();
        }

        if (userRepository.count() == 0) {
            System.out.println("🚀 Popolamento iniziale del database...");

            // --- UTENTI ---
            User mario = new User();
            mario.setEmail("mario.rossi@example.com");
            mario.setPassword(passwordEncoder.encode("Password123"));
            mario.setNome("Mario");
            mario.setCognome("Rossi");

            User lucia = new User();
            lucia.setEmail("lucia.bianchi@example.com");
            lucia.setPassword(passwordEncoder.encode("Password123"));
            lucia.setNome("Lucia");
            lucia.setCognome("Bianchi");

            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("Password123"));
            admin.setNome("Admin");
            admin.setCognome("System");

            userRepository.save(mario);
            userRepository.save(lucia);
            userRepository.save(admin);

            // --- PETS ---
            Pet dog = new Pet();
            dog.setNome("Fido");
            dog.setRazza("Labrador");
            dog.setMicrochip("123ABC456");
            dog.setSesso("M");
            dog.setFoto(loadImage("images/dog1.jpg"));
            dog.setOwner(mario);
            mario.getPets().add(dog);
            petRepository.save(dog);

            Pet cat = new Pet();
            cat.setNome("Micia");
            cat.setRazza("Siamese");
            cat.setMicrochip("789XYZ123");
            cat.setSesso("F");
            cat.setFoto(loadImage("images/dog2.jpg"));
            cat.setOwner(lucia);
            lucia.getPets().add(cat);
            petRepository.save(cat);

            Pet rabbit = new Pet();
            rabbit.setNome("Bunny");
            rabbit.setRazza("Coniglio Nano");
            rabbit.setMicrochip("456DEF789");
            rabbit.setSesso("M");
            rabbit.setFoto(loadImage("images/dog1.jpg"));
            rabbit.setOwner(lucia);
            lucia.getPets().add(rabbit);
            petRepository.save(rabbit);


            System.out.println("✅ Database popolato con utenti e pet di esempio.");
        } else {
            System.out.println("ℹ️ Database già popolato, nessuna azione necessaria.");
        }
    }

    /**
     * Carica un'immagine dalla cartella resources in un array di byte
     */
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
