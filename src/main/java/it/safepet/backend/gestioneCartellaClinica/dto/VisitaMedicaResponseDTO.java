package it.safepet.backend.gestioneCartellaClinica.dto;

import java.util.Date;

public class VisitaMedicaResponseDTO {
    private Long visitaMedicaId;
    private String nome;
    private Long petId;
    private Long veterinarioId;
    private String descrizione;
    private String nomeCompletoVeterinario;
    private String nomePet;
    private Date data;
    private boolean isPresentReferto;

    public VisitaMedicaResponseDTO(Long visitaMedicaId, String nome, Long petId, Long veterinarioId,
                                   String descrizione, String nomeCompletoVeterinario, String nomePet,
                                   Date data) {
        this.visitaMedicaId = visitaMedicaId;
        this.nome = nome;
        this.petId = petId;
        this.veterinarioId = veterinarioId;
        this.descrizione = descrizione;
        this.nomeCompletoVeterinario = nomeCompletoVeterinario;
        this.nomePet = nomePet;
        this.data = data;
    }

    public Long getVisitaMedicaId() {
        return visitaMedicaId;
    }

    public void setVisitaMedicaId(Long visitaMedicaId) {
        this.visitaMedicaId = visitaMedicaId;
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

    public String getNomeCompletoVeterinario() {
        return nomeCompletoVeterinario;
    }

    public void setNomeCompletoVeterinario(String nomeCompletoVeterinario) {
        this.nomeCompletoVeterinario = nomeCompletoVeterinario;
    }

    public String getNomePet() {
        return nomePet;
    }

    public void setNomePet(String nomePet) {
        this.nomePet = nomePet;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean isPresentReferto() {
        return isPresentReferto;
    }

    public void setPresentReferto(boolean presentReferto) {
        isPresentReferto = presentReferto;
    }
}
