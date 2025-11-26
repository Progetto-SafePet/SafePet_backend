package it.safepet.backend.gestioneRecensioni.dto;

public class RecensioneResponseDTO {
    private Long id;
    private Integer punteggio;
    private String descrizione;
    private Long idProprietario;
    private Long idVeterinario;

    public RecensioneResponseDTO(Long id, Integer punteggio, String descrizione, Long idProprietario,
                                 Long idVeterinario) {
        this.id = id;
        this.punteggio = punteggio;
        this.descrizione = descrizione;
        this.idProprietario = idProprietario;
        this.idVeterinario = idVeterinario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getIdProprietario() {
        return idProprietario;
    }

    public void setIdProprietario(Long idProprietario) {
        this.idProprietario = idProprietario;
    }

    public Long getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(Long idVeterinario) {
        this.idVeterinario = idVeterinario;
    }
}
