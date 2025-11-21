package it.safepet.backend.gestioneCartellaClinica.dto;

import java.util.Date;

public class VisitaMedicaResponseDTO {
    private String nome;
    private Long petId;
    private Long veterinarioId;
    private String descrizione;
    private Date data;
    private byte[] referto;
    private String pdfBase64;


    public VisitaMedicaResponseDTO(String nome, Long petId, Long veterinarioId,
                                   String descrizione, Date data, byte[] referto) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.data = data;
        this.referto = referto;
        this.petId = petId;
        this.veterinarioId = veterinarioId;
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

    public Long getVeterinarioId() {
        return veterinarioId;
    }

    public void setVeterinarioId(Long veterinarioId) {
        this.veterinarioId = veterinarioId;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public byte[] getReferto() {
        return referto;
    }

    public void setReferto(byte[] referto) {
        this.referto = referto;
    }

    public String getPdfBase64() { return pdfBase64; }

    public void setPdfBase64(String pdfBase64) { this.pdfBase64 = pdfBase64; }
}
