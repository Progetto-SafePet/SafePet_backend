package it.safepet.backend.gestioneCondivisioneDati.controller;

import it.safepet.backend.gestioneCondivisioneDati.service.GestioneCondivisioneDatiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestioneCondivisioneDati")
public class GestioneCondivisioneDatiController {

    @Autowired
    private GestioneCondivisioneDatiService gestioneCondivisioneDatiService;

    @GetMapping("/pdf/{petId}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long petId) {

        byte[] pdfBytes = gestioneCondivisioneDatiService.generaPdfPet(petId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(
                "attachment",
                "LibrettoSanitario_Pet_" + petId + ".pdf"
        );

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
