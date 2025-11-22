package it.safepet.backend.gestioneCartellaClinica.controller;

import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaRequestDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.VisitaMedicaResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.repository.VisitaMedicaRepository;
import it.safepet.backend.gestioneCartellaClinica.service.visitaMedica.GestioneVisitaMedicaService;
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

@RestController
@RequestMapping("/gestioneCartellaClinica")
public class GestioneCartellaClinicaController {
    @Autowired
    private GestioneVisitaMedicaService gestioneVisitaMedicaService;

    @Autowired
    private VisitaMedicaRepository visitaMedicaRepository;

    /**
     * Crea e registra una nuova visita medica per un pet, verificando che l'utente
     * autenticato sia un veterinario e che il pet sia effettivamente associato a lui.
     * Permette di allegare un referto in formato PDF. Restituisce i dettagli della visita
     * appena creata, inclusa una variabile booleana che indica se è presente un referto.
     *
     * <p><b>Metodo:</b> POST<br>
     * <b>Endpoint:</b> /it.safepet.backend.gestioneCartellaClinica/creaVisitaMedica/{petId}<br>
     * <b>Content-Type:</b> multipart/form-data</p>
     *
     * <p><b>Parametri di percorso:</b></p>
     * <ul>
     *   <li><b>petId</b> – identificativo del pet a cui associare la visita medica</li>
     * </ul>
     *
     * <p><b>Corpo richiesta (multipart/form-data):</b></p>
     * <ul>
     *   <li><b>nome</b> – nome della visita (obbligatorio, 3–20 caratteri)</li>
     *   <li><b>descrizione</b> – descrizione della visita (max 300 caratteri)</li>
     *   <li><b>data</b> – data della visita (obbligatoria, formato yyyy-MM-dd)</li>
     *   <li><b>referto</b> – file PDF opzionale della visita</li>
     * </ul>
     *
     * <p><b>Esempio risposta (201 CREATED):</b></p>
     * <pre>
     * {
     *   "visitaMedicaId": 12,
     *   "nome": "Controllo annuale",
     *   "petId": 3,
     *   "veterinarioId": 5,
     *   "descrizione": "Visita di routine con controllo vaccini",
     *   "nomeCompletoVeterinario": "Dr. Marco Bianchi",
     *   "nomePet": "Fuffy",
     *   "data": "2024-01-15",
     *   "isPresentReferto": true
     * }
     * </pre>
     *
     * @param petId identificativo del pet a cui associare la visita medica
     * @param visitaMedicaRequestDTO DTO contenente i dati della visita e l'eventuale referto PDF
     * @return {@link VisitaMedicaResponseDTO} con i dettagli della visita creata,
     *         inclusa la presenza o meno di un referto
     * @throws RuntimeException se l'utente non è un veterinario, il pet non esiste,
     *         non è associato al veterinario o si verifica un errore nell'apertura del file
     */
    @PostMapping(value = "/creaVisitaMedica/{petId}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VisitaMedicaResponseDTO> creaVisitaMedica(
            @PathVariable Long petId,
            @ModelAttribute VisitaMedicaRequestDTO visitaMedicaRequestDTO) {
        visitaMedicaRequestDTO.setPetId(petId);
        System.out.println(visitaMedicaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gestioneVisitaMedicaService.creaVisitaMedica(visitaMedicaRequestDTO));
    }

    /**
     * Restituisce il PDF associato a una visita medica, identificata dal suo ID,
     * permettendone il download come allegato.
     *
     * <p><b>Metodo:</b> GET<br>
     * <b>Endpoint:</b> /it.safepet.backend.gestioneCartellaClinica/{id}/pdf<br>
     * <b>Content-Type risposta:</b> application/pdf</p>
     *
     * <p><b>Parametri di percorso:</b></p>
     * <ul>
     *   <li><b>id</b> – identificativo della visita medica</li>
     * </ul>
     *
     * <p><b>Esempio risposta (200 OK):</b><br>
     * Restituisce il contenuto binario del PDF come allegato scaricabile.</p>
     *
     * <p>Header risposta:</p>
     * <pre>
     * Content-Disposition: attachment; filename="Visita_AnnualCheck_2024.pdf"
     * Content-Type: application/pdf
     * </pre>
     *
     * @param id identificativo della visita medica di cui recuperare il PDF
     * @return array di byte contenente il PDF, pronto per il download
     */
    @GetMapping("/visitaMedica/{id}/pdf")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + "VisitaMedica - " + id + "\"")
                .body(gestioneVisitaMedicaService.leggiPFD(id));
    }
}
