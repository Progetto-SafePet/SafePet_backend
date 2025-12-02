package it.safepet.backend.gestioneAnalisiDermatologica.controller;

import it.safepet.backend.gestioneAnalisiDermatologica.service.AnalisiDermatologicaService;
import it.safepet.backend.gestioneAnalisiDermatologica.dto.RisultatoDiagnosiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/analisiDermatologica")
public class AnalisiDermatologicaController {
    @Autowired
    private AnalisiDermatologicaService analisiDermatologicaService;

    /**
     * <pre>
     * Endpoint per avviare l'analisi dermatologica di un'immagine caricata.
     *
     * Esempio di risposta:
     * {
     *     "classe": "Dermatitis",
     *     "confidence": 0.9808655977249146
     * }
     * </pre>
     * @param image Il file immagine da analizzare (MultipartFile). Accetta solo .jpeg e .png.
     * @return Un oggetto RisultatoDiagnosiDTO contenente la classe diagnosticata e la confidence del modello riguardo questa scelta.
     * @throws IOException Se si verifica un errore durante l'elaborazione dell'immagine.
     */
    @PostMapping(value = "/analizza", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RisultatoDiagnosiDTO avviaAnalisiDermatologica(@RequestPart("image") MultipartFile image) throws IOException {
        byte[] bytes = image.getBytes();
        return analisiDermatologicaService.avviaAnalisiDermatologica(bytes);
    }

}
