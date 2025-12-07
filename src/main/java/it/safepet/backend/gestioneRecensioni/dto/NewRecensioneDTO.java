package it.safepet.backend.gestioneRecensioni.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NewRecensioneDTO {

    @NotNull(message = "Il punteggio è obbligatorio")
    @Min(value = 1, message = "Il punteggio minimo è 1")
    @Max(value = 5, message = "Il punteggio massimo è 5")
    private Integer punteggio;

    @Size(max = 100, message="la dimensione deve essere compresa tra 0 e 100")
    @NotBlank(message = "La descrizione è obbligatoria")
    private String descrizione;

    public NewRecensioneDTO(Integer punteggio, String descrizione) {
        this.punteggio = punteggio;
        this.descrizione = descrizione;
    }

    public Integer getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(Integer punteggio) {
        this.punteggio = punteggio;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
