package it.safepet.backend.gestioneAnalisiDermatologica.service;

import it.safepet.backend.gestioneAnalisiDermatologica.SkinDetectorAdapter;
import it.safepet.backend.gestioneAnalisiDermatologica.dto.RisultatoDiagnosiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class AnalisiDermatologicaServiceImpl implements AnalisiDermatologicaService {
    private final SkinDetectorAdapter skinDetectorAdapter;

    @Autowired
    public AnalisiDermatologicaServiceImpl(SkinDetectorAdapter skinDetectorAdapter) {
        this.skinDetectorAdapter = skinDetectorAdapter;
    }
    
    /**
     * Avvia l'analisi dermatologica delegando la richiesta all'adapter.
     *
     * @param image L'immagine da analizzare in formato byte array.
     * @return Un oggetto RisultatoDiagnosiDTO contenente il risultato della diagnosi.
     */
    @Override
    public RisultatoDiagnosiDTO avviaAnalisiDermatologica(byte[] image) {
        MediaType formato = individuaFormatoImmagine(image);
        return skinDetectorAdapter.avviaAnalisiDermatologica(image, formato);
    }

    /**
     * Rileva il tipo di media (MIME type) dell'immagine basandosi sui magic numbers.
     * I primi byte di un'immagine PNG sono 0x89504E47.
     * I primi byte di un'immagine JPG sono 0xFFD8FF.
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

        throw new IllegalArgumentException("Formato immagine non supportato");
    }
}
