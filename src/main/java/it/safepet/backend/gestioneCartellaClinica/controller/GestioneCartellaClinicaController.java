package it.safepet.backend.gestioneCartellaClinica.controller;

import it.safepet.backend.gestioneCartellaClinica.dto.*;
import it.safepet.backend.gestioneCartellaClinica.service.GestioneCartellaClinicaService;
import it.safepet.backend.gestioneCartellaClinica.service.terapia.GestioneTerapiaService;
import it.safepet.backend.gestioneCartellaClinica.service.vaccinazione.GestioneVaccinazioneService;

import it.safepet.backend.gestioneCartellaClinica.service.patologia.GestionePatologiaService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestioneCartellaClinica")
public class GestioneCartellaClinicaController {
    @Autowired
    private GestioneVisitaMedicaService gestioneVisitaMedicaService;

    @Autowired
    private GestionePatologiaService gestionePatologiaService;

    @Autowired
    private GestioneVaccinazioneService gestioneVaccinazioneService;

    @Autowired
    private GestioneTerapiaService gestioneTerapiaService;

    @Autowired
    private GestioneCartellaClinicaService gestioneCartellaClinicaService;

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

    /**
     * Crea e registra una nuova patologia per un pet, verificando che l'utente autenticato
     * sia un veterinario e che il pet sia effettivamente associato a lui.
     *
     * <p>
     * Restituisce i dettagli della patologia appena creata, inclusi nome, diagnosi,
     * sintomi osservati e terapia associata.
     *
     * <p><b>Metodo:</b> POST<br>
     * <b>Endpoint:</b> /gestioneCartellaClinica/creaPatologia/{petId}</p>
     *
     * <p><b>Parametri di percorso:</b></p>
     * <ul>
     *   <li><b>petId</b> – identificativo del pet a cui associare la patologia</li>
     * </ul>
     *
     * <p><b>Corpo richiesta (JSON):</b></p>
     * <pre>
     * {
     *   "nome": "Dermatite",
     *   "dataDiDiagnosi": "2025-11-24",
     *   "sintomiOsservati": "Prurito intenso e perdita di pelo",
     *   "diagnosi": "Dermatite atopica",
     *   "terapiaAssociata": "Crema lenitiva e antistaminico"
     * }
     * </pre>
     *
     * <p><b>Esempio risposta (201 CREATED):</b></p>
     * <pre>
     * {
     *   "patologiaId": 7,
     *   "nome": "Dermatite",
     *   "dataDiDiagnosi": "2025-11-24",
     *   "sintomiOsservati": "Prurito intenso e perdita di pelo",
     *   "diagnosi": "Dermatite atopica",
     *   "terapiaAssociata": "Crema lenitiva e antistaminico",
     *   "petId": 15,
     *   "veterinarioId": 3
     * }
     * </pre>
     *
     * @param petId identificativo del pet a cui associare la patologia
     * @param patologiaRequestDTO DTO contenente i dati della patologia da creare
     * @return {@link PatologiaResponseDTO} con i dettagli della patologia appena creata
     * @throws RuntimeException se l'utente non è un veterinario, se il pet non esiste
     *                          o se non è associato al veterinario
     */
    @PostMapping("/aggiungiPatologia/{petId}")
    public ResponseEntity<PatologiaResponseDTO> creaPatologia(
            @PathVariable Long petId,
            @RequestBody PatologiaRequestDTO patologiaRequestDTO) {
        patologiaRequestDTO.setPetId(petId);
        System.out.println(patologiaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gestionePatologiaService.creaPatologia(patologiaRequestDTO));
    }

    /**
     * Permette al veterinario autenticato di aggiungere una nuova vaccinazione nella
     * cartella clinica del pet indicato. Il sistema verifica che:
     * <ul>
     *   <li>l'utente autenticato sia un veterinario;</li>
     *   <li>il pet esista nel sistema;</li>
     *   <li>il pet sia effettivamente associato al veterinario;</li>
     *   <li>i dati inseriti rispettino tutti i vincoli previsti dallo use case
     *       (nome vaccino, tipologia, data, dose, via di somministrazione,
     *       eventuali effetti collaterali, richiamo previsto).</li>
     * </ul>
     *
     * <p><b>Metodo:</b> POST<br>
     * <b>Endpoint:</b> /gestioneCartellaClinica/aggiungiVaccinazione/{petId}<br>
     * <b>Content-Type:</b> application/json</p>
     *
     * <p><b>Parametri di percorso:</b></p>
     * <ul>
     *   <li><b>petId</b> – identificativo del pet a cui associare la vaccinazione</li>
     * </ul>
     *
     * <p><b>Corpo richiesta (JSON):</b></p>
     * <ul>
     *   <li><b>nomeVaccino</b> – nome del vaccino (obbligatorio, 3–20 caratteri)</li>
     *   <li><b>tipologia</b> – tipologia del vaccino (obbligatoria, 3–20 caratteri)</li>
     *   <li><b>dataDiSomministrazione</b> – data nel formato dd/MM/yyyy</li>
     *   <li><b>doseSomministrata</b> – numero compreso tra 0.1 e 10 ml</li>
     *   <li><b>viaDiSomministrazione</b> – uno tra:
     *       SOTTOCUTANEA, INTRAMUSCOLARE, ORALE, INTRANASALE, TRANSDERMICA</li>
     *   <li><b>effettiCollaterali</b> – testo opzionale (max 200 caratteri)</li>
     *   <li><b>richiamoPrevisto</b> – data nel formato dd/MM/yyyy</li>
     * </ul>
     *
     * <p><b>Esempio risposta (201 CREATED):</b></p>
     * <pre>
     * {
     *   "id": 17,
     *   "nomeVaccino": "Nobivac DHPPi",
     *   "petId": 3,
     *   "veterinarioId": 5,
     *   "tipologia": "Polivalente",
     *   "dataDiSomministrazione": "2025-03-10",
     *   "doseSomministrata": 1.0,
     *   "viaDiSomministrazione": "SOTTOCUTANEA",
     *   "effettiCollaterali": "Leggera sonnolenza",
     *   "richiamoPrevisto": "2026-03-10",
     *   "nomeCompletoVeterinario": "Dr. Marco Bianchi"
     * }
     * </pre>
     *
     * @param petId identificativo del pet a cui registrare la vaccinazione
     * @param request DTO con i dati della vaccinazione
     * @return {@link VaccinazioneResponseDTO} contenente i dettagli della vaccinazione creata
     * @throws RuntimeException se il veterinario non è autorizzato, il pet non è associato a lui,
     *         oppure i dati non rispettano i vincoli previsti
     */
    @PostMapping("/aggiungiVaccinazione/{petId}")
    public ResponseEntity<VaccinazioneResponseDTO> aggiungiVaccinazione(
            @PathVariable Long petId,
            @RequestBody VaccinazioneRequestDTO request) {

        request.setPetId(petId); // imposta automaticamente il petId nel DTO

        VaccinazioneResponseDTO response =
                gestioneVaccinazioneService.aggiungiVaccinazione(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping ("/aggiungiTerapia/{petId}")
    public ResponseEntity<TerapiaResponseDTO> aggiungiTerapia(
            @PathVariable Long petId,
            @RequestBody TerapiaRequestDTO terapiaRequestDTO){
        terapiaRequestDTO.setPetId(petId);

        TerapiaResponseDTO terapiaResponseDTO=
                gestioneTerapiaService.aggiungiTerapia(terapiaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(terapiaResponseDTO);
    }

    //momentaneo, per provare su PostMan (quindi non serve Javadoc)
    @GetMapping("/cartellaClinica/{petId}")
    public ResponseEntity<CartellaClinicaResponseDTO> getCartellaClinica(@PathVariable Long petId) {
        CartellaClinicaResponseDTO dto = gestioneCartellaClinicaService.getCartellaClinica(petId);
        return ResponseEntity.ok(dto);
    }
}
