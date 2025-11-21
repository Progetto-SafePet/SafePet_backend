package it.safepet.backend.gestioneCartellaClinica.controller;

import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.model.VisitaMedica;
import it.safepet.backend.gestioneCartellaClinica.repository.VisitaMedicaRepository;
import it.safepet.backend.gestioneCartellaClinica.service.GestioneCartellaClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/gestioneCartellaClinica")
public class GestioneCartellaClinicaController {
    @Autowired
    private GestioneCartellaClinicaService gestioneCartellaClinicaService;
    @Autowired
    private VisitaMedicaRepository visitaMedicaRepository;

    @PostMapping(value = "/creaVisitaMedica/{petId}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VisitaMedicaResponseDTO> creaVisitaMedica(
            @PathVariable Long petId,
            @ModelAttribute VisitaMedicaRequestDTO visitaMedicaRequestDTO) {
        visitaMedicaRequestDTO.setPetId(petId);
        System.out.println(visitaMedicaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gestioneCartellaClinicaService.creaVisitaMedica(visitaMedicaRequestDTO));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) {
        Optional<VisitaMedica> visitaMedica = visitaMedicaRepository.findById(id);
        if (visitaMedica.isEmpty()) {
            throw new RuntimeException("Visita Medica non trovata");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + visitaMedica.get().getNome() + "\"")
                .body(gestioneCartellaClinicaService.leggiPFD(id).getReferto());
    }
}
