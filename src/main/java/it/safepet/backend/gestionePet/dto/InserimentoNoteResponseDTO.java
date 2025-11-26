package it.safepet.backend.gestionePet.dto;

public class InserimentoNoteResponseDTO {
    private Long idNota;
    private String titolo;
    private String descrizione;
    private Long idPet;
    private String nomePet;
    private Long idProprietario;
    private String nomeCompletoProprietario;

    public InserimentoNoteResponseDTO(Long idNota, String titolo, String descrizione,
                                      Long idPet, String nomePet, Long idProprietario, String nomeCompletoProprietario) {
        this.idNota = idNota;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.idPet = idPet;
        this.nomePet = nomePet;
        this.idProprietario = idProprietario;
        this.nomeCompletoProprietario = nomeCompletoProprietario;
    }

    public Long getIdNota() {
        return idNota;
    }

    public void setIdNota(Long idNota) {
        this.idNota = idNota;
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

    public Long getIdPet() {
        return idPet;
    }

    public void setIdPet(Long idPet) {
        this.idPet = idPet;
    }

    public String getNomePet() {
        return nomePet;
    }

    public void setNomePet(String nomePet) {
        this.nomePet = nomePet;
    }

    public Long getIdProprietario() {
        return idProprietario;
    }

    public void setIdProprietario(Long idProprietario) {
        this.idProprietario = idProprietario;
    }

    public String getNomeCompletoProprietario() {
        return nomeCompletoProprietario;
    }

    public void setNomeCompletoProprietario(String nomeCompletoProprietario) {
        this.nomeCompletoProprietario = nomeCompletoProprietario;
    }
}
