package it.safepet.backend.gestionePet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class InserimentoNoteRequestDTO {
    @NotBlank(message = "Il titolo della nota è obbligatorio")
    @Size(max = 20, message = "Il titolo deve contenere massimo 20 caratteri")
    private String titolo;

    @NotBlank(message = "La descrizione della nota è obbligatoria")
    @Size(max = 300, message = "La descrizione può contenere massimo 300 caratteri")
    private String descrizione;

    private Long petId;

    public InserimentoNoteRequestDTO() {
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }
}
