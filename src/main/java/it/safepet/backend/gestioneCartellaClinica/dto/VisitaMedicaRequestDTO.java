package it.safepet.backend.gestioneCartellaClinica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class VisitaMedicaRequestDTO {
    @NotBlank(message = "Il nome della visita è obbligatorio")
    @Size(min = 3, max = 20, message = "Il nome deve contenere tra 3 e 20 caratteri")
    private String nome;

    private Long petId;

    @Size(max = 300, message = "Il descrizione può contenere massimo 300 caratteri")
    private String descrizione;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private @NotNull(message = "La data della visita è obbligatoria") LocalDate data;

    private MultipartFile referto;

    public VisitaMedicaRequestDTO() {
    }

    public VisitaMedicaRequestDTO(String nome, Long petId, String descrizione,
                                  @NotNull(message = "La data della visita è obbligatoria") LocalDate data,
                                  MultipartFile referto) {
        this.nome = nome;
        this.petId = petId;
        this.descrizione = descrizione;
        this.data = data;
        this.referto = referto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public @NotNull(message = "La data della visita è obbligatoria") LocalDate getData() {
        return data;
    }

    public void setData(@NotNull(message = "La data della visita è obbligatoria") LocalDate data) {
        this.data = data;
    }

    public MultipartFile getReferto() {
        return referto;
    }

    public void setReferto(MultipartFile referto) {
        this.referto = referto;
    }
}
