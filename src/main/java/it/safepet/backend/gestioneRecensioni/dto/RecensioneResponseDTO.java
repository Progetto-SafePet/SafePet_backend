package it.safepet.backend.gestioneRecensioni.dto;

public class RecensioneResponseDTO {
    private Long idRecensione;
    private int punteggio;
    private String descrizione;
    private Long idVeterinario;
    private Long idProprietario;

    public RecensioneResponseDTO(Long idRecensione, int punteggio, String descrizione, Long idVeterinario, Long idProprietario) {
        this.idRecensione = idRecensione;
        this.punteggio = punteggio;
        this.descrizione = descrizione;
        this.idVeterinario = idVeterinario;
        this.idProprietario = idProprietario;
    }

    public Long getIdRecensione() {
        return idRecensione;
    }

    public void setIdRecensione(Long idRecensione) {
        this.idRecensione = idRecensione;
    }

    public int getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(Long idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public Long getIdProprietario() {
        return idProprietario;
    }

    public void setIdProprietario(Long idProprietario) {
        this.idProprietario = idProprietario;
    }
}
