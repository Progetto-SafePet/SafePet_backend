package it.safepet.backend.gestioneAnalisiDermatologica.service;

import it.safepet.backend.autenticazione.jwt.AuthContext;
import it.safepet.backend.autenticazione.jwt.AuthenticatedUser;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.gestioneAnalisiDermatologica.SkinDetectorAdapter;
import it.safepet.backend.gestioneAnalisiDermatologica.dto.RisultatoDiagnosiDTO;
import it.safepet.backend.gestioneUtente.repository.ProprietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class AnalisiDermatologicaServiceImpl implements AnalisiDermatologicaService {
    private static final long MAX_IMAGE_SIZE_BYTES = 10L * 1024 * 1024; // 10 MB

    private final SkinDetectorAdapter skinDetectorAdapter;

    @Autowired
    private ProprietarioRepository proprietarioRepository;

    @Autowired
    public AnalisiDermatologicaServiceImpl(SkinDetectorAdapter skinDetectorAdapter) {
        this.skinDetectorAdapter = skinDetectorAdapter;
    }


    /**
     * Avvia l'analisi dermatologica delegando la richiesta all'adapter.
     * Esegue i seguenti controlli di validazione:
     * <ul>
     *   <li>Verifica che l'utente sia autenticato e abbia il ruolo <b>PROPRIETARIO</b>.</li>
     *   <li>Verifica che il proprietario esista nel database (tramite {@code existsById}).</li>
     *   <li>L'immagine non deve essere {@code null} o vuota.</li>
     *   <li>La dimensione dell'immagine non deve superare 10 MB.</li>
     *   <li>Il formato dell'immagine deve essere PNG o JPEG (rilevato tramite magic numbers).</li>
     * </ul>
     *
     * @param image L'immagine da analizzare in formato byte array.
     * @return Un oggetto {@link RisultatoDiagnosiDTO} contenente il risultato della diagnosi.
     * @throws RuntimeException Se l'utente non è autenticato, non ha il ruolo corretto
     *                          o non è un proprietario registrato.
     * @throws IllegalArgumentException Se l'immagine è {@code null}/vuota, supera la dimensione massima
     *                                  o il formato non è supportato.
     */
    @Override
    public RisultatoDiagnosiDTO avviaAnalisiDermatologica(byte[] image) {

        AuthenticatedUser currentUser = AuthContext.getCurrentUser();
        if (currentUser == null || !Role.PROPRIETARIO.equals(currentUser.getRole())) {
            throw new RuntimeException("Accesso non autorizzato");
        }

        // Verifica che il proprietario esista nel DB
        boolean esisteProprietario = proprietarioRepository.existsById(currentUser.getId());
        if (!esisteProprietario) {
            throw new RuntimeException("Proprietario non trovato");
        }

        validaDimensioneImmagine(image);
        MediaType formato = individuaFormatoImmagine(image);
        return skinDetectorAdapter.avviaAnalisiDermatologica(image, formato);
    }

    /**
     * Verifica che l'immagine rispetti la dimensione massima consentita (10 MB).
     *
     * @param image L'immagine in formato byte array.
     * @throws IllegalArgumentException Se l'immagine è null, vuota o supera 10 MB.
     */
    private void validaDimensioneImmagine(byte[] image) {
        if (image == null || image.length == 0) {
            throw new IllegalArgumentException("Immagine non valida: il contenuto è vuoto o non presente.");
        }
        if (image.length > MAX_IMAGE_SIZE_BYTES) {
            throw new IllegalArgumentException(
                    "Immagine troppo grande: dimensione attuale " + image.length +
                            " byte, massimo consentito " + MAX_IMAGE_SIZE_BYTES + " byte (10 MB)."
            );
        }
    }

    /**
     * Rileva il tipo di media (MIME type) dell'immagine basandosi sui magic numbers.
     * PNG: 0x89 0x50 0x4E 0x47
     * JPEG: 0xFF 0xD8
     *
     * @param image L'immagine in formato byte array.
     * @return Il MediaType rilevato (IMAGE_PNG o IMAGE_JPEG).
     * @throws IllegalArgumentException Se il formato dell'immagine non è supportato.
     */
    private MediaType individuaFormatoImmagine(byte[] image) {
        if (image.length >= 4) {
            if (image[0] == (byte) 0x89 &&
                    image[1] == (byte) 0x50 &&
                    image[2] == (byte) 0x4E &&
                    image[3] == (byte) 0x47) {
                return MediaType.IMAGE_PNG;
            }
            if (image[0] == (byte) 0xFF &&
                    image[1] == (byte) 0xD8) {
                return MediaType.IMAGE_JPEG;
            }
        }
        throw new IllegalArgumentException("Formato immagine non supportato. Sono accettati PNG e JPEG.");
    }
}
